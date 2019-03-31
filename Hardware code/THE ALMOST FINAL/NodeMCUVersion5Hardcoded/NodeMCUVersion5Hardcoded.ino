#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <ArduinoJson.h>
#include <SoftwareSerial.h>

#define FIREBASE_HOST "fir-auth-45665.firebaseio.com"
#define FIREBASE_AUTH "oYCggxTfYvvMEwPoQN2vM59ZzTX2Lt2A7KFBT31U"
#define WIFI_SSID "Ghareisa"
#define WIFI_PASSWORD "201402464"

int spotStatus[4] = {0, 0, 0, 0};
String spotNames[4] = {"spot1", "spot2", "spot3", "spot4"};
bool changeInStatus[4] = { false, false, false, false};
int countChangedSpots = 0;
int zoneNo = -1;
bool spotsObjectNull = false;
const char* wantedKey;
const char* zone;
bool confirm;
bool wantedSpot = false;
bool wantedZone = false;
SoftwareSerial s(D6, D5);

//            Spot 1    Spot 2    Spot 3    Spot 4
// Zone CENG     x         x         x         x
// Zone CAAS     x         x         x         x
String keys[2][4] = { {"-LZaE7RMP-v3D7gQ3-eb", "-LZaERMQCZkwimFRwbqP", "-LZaEZRLocS__F1B1Vjb", "-LZaF1wa2lFvfc7USq-U"}, 
      {"-LZaF9HMxT4d7muUSfc_", "-LZaFFBqEK3dW9sGiMra", "-LZaFNyTtBvdVRnCjvoC", "-LZaFWD0k8LqCGYuvVZ-"} }; 
void setup() {
 // Serial.begin(9600);
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
}
void loop() {

  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& rootSerial = jsonBuffer.parseObject(s);

  if (rootSerial == JsonObject::invalid())
    return;

  unpackSerialData(rootSerial);
  
  sendChangesToFirebase();


  delay(60);

}

void unpackSerialData(JsonObject& rootSerial){

  if (rootSerial["zoneNumb"] == 0) {
    zone = "CENG Female Zone";
    zoneNo = 0;
  } else if (rootSerial["zoneNumb"] == 1) {
    zone = "CAAS Female Zone";
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

void sendChangesToFirebase(){
  confirm = true;
  for(int i=0; i<4; i++){
    if(changeInStatus[i]){
      // wantedKey = keys[zoneNo][i];
      String path("spots/" + keys[zoneNo][i] + "/status");
      
      if (spotStatus[i] == 1) {
        Firebase.setString(path, "not available");
      } else if (spotStatus[i] == 2) {
        Firebase.setString(path, "available");
      }

      if (Firebase.failed()) {
        Serial.print("setting failed:");
        Serial.println(Firebase.error());
        confirm = false;
        ESP.reset();
        return;
      }

      if (confirm) {
        s.write(30);
      }
      if (--countChangedSpots == 0) {
        break;
      }
    }
    
  }
  
  
//  int chosenSpot;
//  
//  FirebaseObject root = Firebase.get("spots");
//  
//  if (Firebase.failed()) {
//      Serial.print("getting failed");
//      Serial.println(Firebase.error());
//      ESP.reset();
//      return;
//  }
//  
//  JsonObject& obj = root.getJsonVariant();
//  //obj.printTo(Serial);
//  //Serial.println("");
//  
//  for (auto spot : obj) {
//    for (auto nested : spot.value.as<JsonObject>()) {
//      if (!strcmp(nested.key, "spotNo") && ( ((nested.value.as<int>() == 1) && changeInStatus[0])
//                                             || ((nested.value.as<int>() == 2) && changeInStatus[1]) 
//                                             || ((nested.value.as<int>() == 3) && changeInStatus[2])
//                                             || ((nested.value.as<int>() == 4) && changeInStatus[3]) ) ) {
//        wantedSpot = true;
//        chosenSpot = nested.value.as<int>();
//      } else if (!strcmp(nested.key, "zoneName") && !strcmp(nested.value.as<char*>(), zone)) {
//        wantedZone = true;
//      }
//    }
//
//    if (wantedSpot && wantedZone) {
//      wantedKey = spot.key;
//      String str(wantedKey);
//      String path("spots/" + str + "/status");
//
//      if (spotStatus[chosenSpot-1] == 1) {
//        Firebase.setString(path, "not available");
//      } else if (spotStatus[chosenSpot-1] == 2) {
//        Firebase.setString(path, "available");
//      }
//
//      if (Firebase.failed()) {
//        Serial.print("setting failed:");
//        Serial.println(Firebase.error());
//        confirm = false;
//        ESP.reset();
//        return;
//      }
//
//      if (confirm) {
//        s.write(30);
//      }
//      
//      if (--countChangedSpots == 0) {
//        wantedSpot = false;
//        wantedZone = false;
//        break;
//      }
//    }
//    wantedSpot = false;
//    wantedZone = false;
//
//  }
}
