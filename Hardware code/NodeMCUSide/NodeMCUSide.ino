#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <SoftwareSerial.h>
#include <ArduinoJson.h>

SoftwareSerial s(D6,D5);
#define FIREBASE_HOST "nodemcu-31e7c.firebaseio.com"
#define FIREBASE_AUTH "N9AKR2YBkRgAYXRTJik0NtIMsH7KiTP5EC06xQ1Z"
#define WIFI_SSID "iPhone"
#define WIFI_PASSWORD "ghareisa"
int data;
int prevData=0;
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

  StaticJsonBuffer<200> jsonBuffer;
  JsonObject& root = jsonBuffer.createObject();
  
  Serial.println("In loop");
  Serial.print("prevData: ");
  Serial.print(prevData);
  Serial.println("");
  prevData=data;
  Serial.print("New prevData: ");
  Serial.print(prevData);
  Serial.println("");  
  data=s.read();
  Serial.print("data: ");
  Serial.print(data);
  Serial.println("");
  
  if (data==1 && data!=prevData){
  // set bool value
  Firebase.remove("spots");
  root["spotNo"] = 1;
  root["status"] = 1;
  root["zoneName"] = "A";
  String name1 = Firebase.push("spots",root);
  Serial.println("Car is present in the parking spot.");

  // handle error
  if (Firebase.failed()) {
      Serial.print("setting failed:");
      Serial.println(Firebase.error());  
      return;
  }
  } else if (data==2 && data!=prevData){
    // set bool value
  Firebase.remove("spots");
  root["spotNo"] = 1;
  root["status"] = 2;
  root["zoneName"] = "A";
  String name2 = Firebase.push("spots",root);  
  Serial.println("Parking spot is vacant.");
  // handle error
  if (Firebase.failed()) {
      Serial.print("setting failed:");
      Serial.println(Firebase.error());  
      return;
  }
  }
  delay(500);
}
