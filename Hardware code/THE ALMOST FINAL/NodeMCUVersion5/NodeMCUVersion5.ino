#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <ArduinoJson.h>
#include <SoftwareSerial.h>

#define FIREBASE_HOST "testdatabase11.firebaseio.com"
#define FIREBASE_AUTH "DU7wHivvK33SkJMzhc29SYy8itzyRrdDzZb5KBlz"
#define WIFI_SSID "Ghareisa"
#define WIFI_PASSWORD "201402464"
int spotStatus[4] = {0, 0, 0, 0};
String spotNames[4] = {"spot1", "spot2", "spot3", "spot4"};
bool changeInStatus[4] = { false, false, false, false};
int countChangedSpots = 0;
bool spotsObjectNull = false;
const char* wantedKey;
const char* zone;
bool confirm;
bool wantedSpot = false;
bool wantedZone = false;
SoftwareSerial s(D6, D5);
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

//  FirebaseObject root = Firebase.get("spots");
//  
//  if (Firebase.failed()) {
//      Serial.print("getting failed");
//      Serial.println(Firebase.error());
//      return;
//  }
//  
//  JsonObject& obj = root.getJsonVariant();
//  obj.printTo(Serial);
//  Serial.println("");

  pinMode(D6, INPUT);
  pinMode(D5, OUTPUT);
}
int i=0;
void loop() {

  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& rootSerial = jsonBuffer.parseObject(s);
  //JsonObject& rootSerial = jsonBuffer.createObject();

  if (rootSerial == JsonObject::invalid())
    return;

  unpackSerialData(rootSerial);
  
  sendChangesToFirebase();


  delay(60);

}

void unpackSerialData(JsonObject& rootSerial){
  //rootSerial.printTo(Serial);
  //Serial.println("");
  if (rootSerial["zoneNumb"] == 0) {
    zone = "CENG Female Zone";
  } else if (rootSerial["zoneNumb"] == 1) {
    zone = "CAAS Female Zone";
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
  int chosenSpot;
  confirm = true;
  
  FirebaseObject root = Firebase.get("spots");
  
  if (Firebase.failed()) {
      Serial.print("getting failed");
      Serial.println(Firebase.error());
      ESP.reset();
      return;
  }
  
  JsonObject& obj = root.getJsonVariant();
  //obj.printTo(Serial);
  //Serial.println("");
  
  for (auto spot : obj) {
    for (auto nested : spot.value.as<JsonObject>()) {
      if (!strcmp(nested.key, "spotNo") && ( ((nested.value.as<int>() == 1) && changeInStatus[0])
                                             || ((nested.value.as<int>() == 2) && changeInStatus[1]) 
                                             || ((nested.value.as<int>() == 3) && changeInStatus[2])
                                             || ((nested.value.as<int>() == 4) && changeInStatus[3]) ) ) {
        wantedSpot = true;
        chosenSpot = nested.value.as<int>();
      } else if (!strcmp(nested.key, "zoneName") && !strcmp(nested.value.as<char*>(), zone)) {
        wantedZone = true;
      }
    }

    if (wantedSpot && wantedZone) {
      wantedKey = spot.key;
      String str(wantedKey);
      String path("spots/" + str + "/status");

      if (spotStatus[chosenSpot-1] == 1) {
        Firebase.setString(path, "not available");
      } else if (spotStatus[chosenSpot-1] == 2) {
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
        wantedSpot = false;
        wantedZone = false;
        break;
      }
    }
    wantedSpot = false;
    wantedZone = false;

  }
}
