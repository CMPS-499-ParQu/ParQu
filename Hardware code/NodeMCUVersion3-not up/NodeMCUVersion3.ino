#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <SoftwareSerial.h>
#include <ArduinoJson.h>

SoftwareSerial s(D6, D5);
#define FIREBASE_HOST "testdatabase11.firebaseio.com"
#define FIREBASE_AUTH "DU7wHivvK33SkJMzhc29SYy8itzyRrdDzZb5KBlz"
#define WIFI_SSID "Ghareisa"
#define WIFI_PASSWORD "201402464"


String spot;
const char* wantedKey;
const char* zone;
bool confirm;
bool wantedSpot = false;
bool wantedZone = false;

void setup() {
  s.begin(9600);
  Serial.begin(9600);
  pinMode(D6,INPUT);
  pinMode(D5,OUTPUT);

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

}
void loop() {

  StaticJsonBuffer<1000> jsonBuffer;
  JsonObject& rootSerial = jsonBuffer.parseObject(s);

  //Serial.println("before if");
  if (rootSerial != JsonObject::invalid()) {


   // Serial.println("after if");

    int num = rootSerial["spotNo"];
    Serial.println(num);
    spot = String(num);
    char charSpot[sizeof(spot)];
    spot.toCharArray(charSpot, sizeof(charSpot));

    if (rootSerial["zoneNumb"] == 0) {
      zone = "CENG Female Zone";
    } else if (rootSerial["zoneNumb"] == 1) {
      zone = "CAAS Female Zone";
    }

    confirm = true;

    FirebaseObject root = Firebase.get("spots");
    JsonObject& spotsObject = root.getJsonVariant();

    for (auto spot : spotsObject) {
      Serial.println("in spots loop");
      for (auto nested : spot.value.as<JsonObject>()) {

        if (!strcmp(nested.key, "spotNo") && !strcmp(nested.value.as<char*>(), charSpot)) {
          wantedSpot = true;
          Serial.println("spot true");
        } else if (!strcmp(nested.key, "zoneName") && !strcmp(nested.value.as<char*>(), zone)) {
          wantedZone = true;
          Serial.println("zone true");
        }
      }

      if (wantedSpot && wantedZone) {
        wantedKey = spot.key;
        String str(wantedKey);
        Serial.println(str);
        String path("spots/" + str + "/status");
        Serial.println(path);

        if (rootSerial["status"] == 1) {
          Firebase.setString(path, "Not Available");
        } else {
          Firebase.setString(path, "Available");
        }

        if (Firebase.failed()) {
          Serial.print("setting failed:");
          Serial.println(Firebase.error());
          confirm = false;
          return;
        }

        if (confirm) {
          s.write(30);
        }
        wantedSpot = false;
        wantedZone = false;
        break;
      }
      wantedSpot = false;
      wantedZone = false;

    }


  }
  delay(60);

}
