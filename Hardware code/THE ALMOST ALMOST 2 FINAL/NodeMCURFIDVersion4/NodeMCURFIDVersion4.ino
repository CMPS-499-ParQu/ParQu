#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <SoftwareSerial.h>
#include <ArduinoJson.h>
#include <NTPClient.h>
#include <WiFiUdp.h>


WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP);

SoftwareSerial s(D6, D5);
#define FIREBASE_HOST "testdatabase11.firebaseio.com"
#define FIREBASE_AUTH "DU7wHivvK33SkJMzhc29SYy8itzyRrdDzZb5KBlz"
#define WIFI_SSID "Ghareisa"
#define WIFI_PASSWORD "201402464"

const char* ssid     = WIFI_SSID;
const char* password = WIFI_PASSWORD;
String uid = "";
bool validReserv = true;
bool accepted = false;
int entryStat = 5;
String date;
int zoneNumb;
int* hours = 0;
int hoursSize = 0;
int cancelledHours = 0;
String reservStatus;
int data[4];
String formattedDate = "";
String dateStamp = "";
String timeStamp = "";
int hourStamp = 0;
int minuteStamp = 0;
String reservKey;
void setup()
{
  //Serial.begin(9600);   // Initiate a serial communication
  s.begin(9600);
  pinMode(D6, INPUT);
  pinMode(D5, OUTPUT);
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

  // Initialize a NTPClient to get time
  timeClient.begin();
  // Set offset time in seconds to adjust for your timezone, for example:
  // GMT +1 = 3600
  // GMT +8 = 28800
  // GMT -1 = -3600
  // GMT 0 = 0
  timeClient.setTimeOffset(10800);
}



void loop()
{
  StaticJsonBuffer<1000> jsonBuffer;
  JsonObject& root = jsonBuffer.parseObject(s);
  if (root == JsonObject::invalid())
    return;
  s.write(10);
  //Serial.println("JSON received and parsed");
  //root.prettyPrintTo(Serial);

  zoneNumb = root["zoneNumb"];
  data[0] = root["A"];
  data[1] = root["B"];
  data[2] = root["C"];
  data[3] = root["D"];

  String content = "";
  for (int i = 0; i < 4; i++) {
    content.concat(String(data[i] < 0x10 ? " 0" : " "));
    content.concat(String(data[i], HEX));
  }
  content.toUpperCase();
  uid = content.substring(1);

  getUserData();

  if (validReserv) {
    compareForEntry();
  }

  sendResultsSerially();
  //delay(100);
}





void getUserData() {
  char charUID[sizeof(uid)];
  uid.toCharArray(charUID, sizeof(charUID));
  bool validReservCreated = false;
  String reservKeyCreated = "";
  validReserv = true;
  date = "";
  reservStatus = "";
  hoursSize = 0;
  entryStat = 5;
  cancelledHours = 0;

  getCurrentTime();

  FirebaseObject nodeReserv = Firebase.get("reservations");
  if (Firebase.failed()) {
    Serial.print("getting failed");
    Serial.println(Firebase.error());
    delay(500);
    nodeReserv = Firebase.get("reservations");
  }

  JsonObject& reservations = nodeReserv.getJsonVariant();
  //Serial.println("before reservations loop");
  for (auto reserv : reservations) {
    //Serial.println("reserv");
    validReserv = true; // stays true if it is the wanted reservation, otherwise it's assigned to false
    for (auto nested : reserv.value.as<JsonObject>()) {

      // if key == uid and value != wantedUID i.e. if the uid of this reservation is
      // not the wanted uid then break and go to next reservation
      if (!strcmp(nested.key, "uid") && strcmp(nested.value.as<char*>(), charUID)) {
        validReserv = false;
        break;
      }

      // check if data of reservation is today
      if (!strcmp(nested.key, "date")) {
        date = String(nested.value.as<char*>());
        if (!dateStamp.equals(date)) {
          validReserv = false;
          break;
        }
      }
      // check if reservation is at the current zone
      if (!strcmp(nested.key, "zoneName")) {
        String zone = String(nested.value.as<char*>());
        if (zoneNumb == 0) {
          if (!zone.equals("LIB Female & Male Zone")) {
            validReserv = false;
            break;
          }
        } else if (zoneNumb == 1) {
          if (!zone.equals("CBAE Female & Male Zone") ) {
            validReserv = false;
            break;
          }
        }
      }

      if (!strcmp(nested.key, "time")) {
        hoursSize = nested.value.as<JsonArray>().size();
        hours = new int[hoursSize];
        int i = 0;
        for (auto hour : nested.value.as<JsonArray>()) {
          hours[i] = hour.as<int>();
          i++;
        }
      }

      if (!strcmp(nested.key, "status")) {
        reservStatus = String(nested.value.as<char*>());
      }

      if (!strcmp(nested.key, "cancelledHours")) {
        cancelledHours = nested.value.as<int>();
      }

    }

    if (validReserv) {
      // valid reservation
      if ((hourStamp >= hours[0] && hourStamp <= hours[hoursSize - 1 - cancelledHours])
          || (hourStamp > hours[hoursSize - 1 - cancelledHours] && !reservStatus.equals("created"))) {
        if (reservStatus == "created") {
          reservKeyCreated = String(reserv.key);
          validReservCreated = true;
        } else if (reservStatus.equals("extended") || reservStatus.equals("arrived") || reservStatus.equals("subcancelled")) {
          entryStat = 6;
          reservKey = String(reserv.key);
          validReservCreated = false;
          break;
        } else {
          validReserv = false;
          continue;
        }
      } else {
        validReserv = false;
        continue;
      }
    }
  }
  if(validReservCreated){
    entryStat = 3;
    reservKey = reservKeyCreated;
    validReserv=true;
  }
}
void compareForEntry() {
  if (entryStat == 3) {
    accepted = true;
    String path("reservations/" + reservKey + "/status");
    Firebase.setString(path, "arrived");
    if (Firebase.failed()) {
      Serial.print("getting failed");
      Serial.println(Firebase.error());
      delay(500);
      Firebase.setString(path, "arrived");
    }
  } else if (entryStat == 6) {
    float penalties = 0;
    accepted = true;

    if (hourStamp < hours[hoursSize - 1 - cancelledHours]) { //Automatic Cancellation Penalty
      penalties -= 2.5 * (hours[hoursSize - 1 - cancelledHours] - hourStamp);
    } else if (hourStamp > hours[hoursSize - 1 - cancelledHours]) { //Exiting after reservation time
      if (!(hourStamp == (hours[hoursSize - 1 - cancelledHours] + 1) && minuteStamp <= 5))
        penalties += 15 * ((hourStamp + 1) - (hours[hoursSize - 1 - cancelledHours] + 1));
    }

    String path("reservations/" + reservKey + "/status");
    Firebase.setString(path, "ended");
    if (Firebase.failed()) {
      Serial.print("getting failed");
      Serial.println(Firebase.error());
      delay(500);
      Firebase.setString(path, "ended");
    }
    if (penalties != 0) {
      String penaltyPath("reservations/" + reservKey + "/price");
      penalties += Firebase.getFloat(penaltyPath);
      Firebase.setFloat(penaltyPath, penalties);
      
      if (Firebase.failed()) {
        Serial.print("getting failed");
        Serial.println(Firebase.error());
        delay(500);
        Firebase.setFloat(penaltyPath, penalties);
      }
    }

  }
}

void sendResultsSerially() {
  if (accepted) {
    Serial.println("Access allowed");
    s.write(20);
  } else {
    Serial.println("Access denied");
    s.write(40);
  }
  accepted = false;
}

void getCurrentTime() {
  while (!timeClient.update()) {
    timeClient.forceUpdate();
  }
  // 2018-05-28T16:00:13Z
  // We need to extract date and time
  formattedDate = timeClient.getFormattedDate();

  // Extract date
  int splitT = formattedDate.indexOf("T");
  dateStamp = String(formattedDate.substring(0, splitT));

  // Extract time
  timeStamp = formattedDate.substring(splitT + 1, formattedDate.length() - 1);
  // Extract hour from time
  splitT = timeStamp.indexOf(":");
  String hr = timeStamp.substring(0, splitT);
  hourStamp = hr.toInt();

  // Extract minute
  String minAndSecStamp = timeStamp.substring(splitT + 1, timeStamp.length() - 1);
  splitT = minAndSecStamp.indexOf(":");
  String minn = timeStamp.substring(0, splitT);
  minuteStamp = minn.toInt();

}
