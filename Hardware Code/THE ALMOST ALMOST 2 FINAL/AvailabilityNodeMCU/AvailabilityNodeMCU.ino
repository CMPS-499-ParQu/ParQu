// Availability NodeMCU
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <ArduinoJson.h>
#include <SoftwareSerial.h>
#include <NTPClient.h>
#include <WiFiUdp.h>


WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP);

#define FIREBASE_HOST "fir-auth-45665.firebaseio.com"
#define FIREBASE_AUTH "oYCggxTfYvvMEwPoQN2vM59ZzTX2Lt2A7KFBT31U"
#define WIFI_SSID "Ghareisa"
#define WIFI_PASSWORD "201402464"


const char* ssid     = WIFI_SSID;
const char* password = WIFI_PASSWORD;
int spotStatus[4] = {0, 0, 0, 0};
String spotNames[4] = {"spot1", "spot2", "spot3", "spot4"};
bool changeInStatus[4] = { false, false, false, false};
int countChangedSpots = 0;
int zoneNo = -1;
String prevDate = "";
bool gotData[17] = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
bool needHistData = false;
String dateStamp = "";
int hourStamp = 0;
int minuteStamp = 0;
int dayStamp = -1;

SoftwareSerial s(D6, D5);

//            Spot 1    Spot 2    Spot 3    Spot 4
// Zone CENG     x         x         x         x
// Zone CAAS     x         x         x         x
String keys[2][4] = { {"-LZaE7RMP-v3D7gQ3-eb", "-LZaERMQCZkwimFRwbqP", "-LZaEZRLocS__F1B1Vjb", "-LZaF1wa2lFvfc7USq-U"},
  {"-LZaF9HMxT4d7muUSfc_", "-LZaFFBqEK3dW9sGiMra", "-LZaFNyTtBvdVRnCjvoC", "-LZaFWD0k8LqCGYuvVZ-"}
};
//                              CENG ,                  CAAS
String zonesKeys[2] = {"-LbTJRAAq_Vmvu3iBMQj", "-LbTH446mhzSZ10rZixT"};
int histSpotsCounter[2] = {          0   ,                0  };
void setup() {
  Serial.begin(9600);
  s.begin(9600);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  pinMode(D6, INPUT);
  pinMode(D5, OUTPUT);


  // Initialize a NTPClient to get time
  timeClient.begin();
  // Set offset time in seconds to adjust for your timezone, for example:
  // GMT +1 = 3600
  // GMT +8 = 28800
  // GMT -1 = -3600
  // GMT 0 = 0
  timeClient.setTimeOffset(10800);
}

void loop() {

  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& rootSerial = jsonBuffer.parseObject(s);

  getCurrentTime();
  checkIfHistogramDataIsNeeded();
  if (needHistData) {
    sendNewHistogramData();
  }

  if (rootSerial == JsonObject::invalid())
    return;

  unpackSerialData(rootSerial);

  sendChangesToFirebase();


  delay(60);

}

void unpackSerialData(JsonObject& rootSerial) {

  if (rootSerial["zoneNumb"] == 0) {
    zoneNo = 0;
  } else if (rootSerial["zoneNumb"] == 1) {
    zoneNo = 1;
  }

  for (int i = 0; i < 4; i++) {
    if (rootSerial[spotNames[i]] != 3) {
      changeInStatus[i] = true;
      spotStatus[i] = rootSerial[spotNames[i]];
      countChangedSpots++;
      //      Serial.print("CHANGE IN SPOT ");
      //      Serial.print(i + 1);
      //      Serial.print(" - STAT ");
      //      Serial.println(spotStatus[i]);
    } else {
      changeInStatus[i] = false;
    }
  }

}

void sendChangesToFirebase() {
  for (int i = 0; i < 4; i++) {
    if (changeInStatus[i]) {
      String path("spots/" + keys[zoneNo][i] + "/status");

      if (spotStatus[i] == 1) {
        Firebase.setString(path, "not available");
      } else if (spotStatus[i] == 2) {
        Firebase.setString(path, "available");
      }

      if (Firebase.failed()) {
        Serial.print("setting failed:");
        Serial.println(Firebase.error());

        delay(500);
        if (spotStatus[i] == 1) {
          Firebase.setString(path, "not available");
        } else if (spotStatus[i] == 2) {
          Firebase.setString(path, "available");
        }
      }

      s.write(30);

      if (--countChangedSpots == 0) {
        break;
      }
    }

  }
}
void getCurrentTime() {
  while (!timeClient.update()) {
    timeClient.forceUpdate();
  }
  // 2018-05-28T16:00:13Z
  // We need to extract date and time
  String formattedDate = timeClient.getFormattedDate();

  // Extract date
  int splitT = formattedDate.indexOf("T");
  dateStamp = String(formattedDate.substring(0, splitT));
  hourStamp = timeClient.getHours();
  minuteStamp = timeClient.getMinutes();
  dayStamp = timeClient.getDay();

}
void checkIfHistogramDataIsNeeded() {

  if (!dateStamp.equals(prevDate)) {
    prevDate = dateStamp;
    for (int i = 0; i < 17; i++)
      gotData[i] = false;
  }

  if (hourStamp >= 6 && hourStamp <= 22) {
    if (!gotData[hourStamp - 6] && minuteStamp >= 30) {
      needHistData = true;
      gotData[hourStamp - 6] = true;
      return;
    }
  }

  needHistData = false;

}
void sendNewHistogramData() {
  needHistData = false;

  for (int i = 0; i < 2; i++) {
    for (int j = 0; j < 4; j++) {
      String spotPath("spots/" + keys[i][j] + "/status");
      String spotStatus = Firebase.getString(spotPath);

      if (Firebase.failed()) {
        Serial.print("setting failed:");
        Serial.println(Firebase.error());

        delay(500);
        spotStatus = Firebase.getString(spotPath);
      }

      if (spotStatus.equals("not available")) {
        histSpotsCounter[i] += 1;
      }
    }
    int hr = hourStamp - 6;
    
    for (int k = 1; k < 4; k++) {
      //change count
      String pathCount("zones/" + zonesKeys[i] + "/statistics/" + dayStamp + "/hoursInfo/" + hr + "/count/" + k);
      int histCount = Firebase.getInt(pathCount);
      if (Firebase.failed()) {
        Serial.print("getting hist count failed:");
        Serial.println(Firebase.error());

        delay(500);
        histCount = Firebase.getInt(pathCount);
      }
      String pathCount2("zones/" + zonesKeys[i] + "/statistics/" + dayStamp + "/hoursInfo/" + hr + "/count/" + (k - 1));
      Firebase.setInt(pathCount2, histCount);
      if (Firebase.failed()) {
        Serial.print("setting hist count failed:");
        Serial.println(Firebase.error());

        delay(500);
        Firebase.setInt(pathCount2, histCount);
      }
      
      //change date
      String pathDate("zones/" + zonesKeys[i] + "/statistics/" + dayStamp + "/hoursInfo/" + hr + "/date/" + k);
      String histDate = Firebase.getString(pathDate);
      if (Firebase.failed()) {
        Serial.print("setting failed:");
        Serial.println(Firebase.error());

        delay(500);
        histDate = Firebase.getString(pathDate);
      }

      String pathDate2("zones/" + zonesKeys[i] + "/statistics/" + dayStamp + "/hoursInfo/" + hr + "/date/" + (k - 1));
      Firebase.setString(pathDate2, histDate);
      if (Firebase.failed()) {
        Serial.print("setting failed:");
        Serial.println(Firebase.error());

        delay(500);
        Firebase.setString(pathDate2, histDate);
      }

      if (k == 3) {
        Firebase.setString(pathDate, dateStamp);
        Firebase.setInt(pathCount,histSpotsCounter[i]);
        if (Firebase.failed()) {
          Serial.print("setting failed:");
          Serial.println(Firebase.error());

          delay(500);
          Firebase.setString(pathDate, dateStamp);
          Firebase.setInt(pathCount,histSpotsCounter[i]);
        }
      }

    }
    histSpotsCounter[i] = 0;
  }

}
