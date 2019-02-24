#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#include <SoftwareSerial.h>
#include <ArduinoJson.h>

SoftwareSerial s(D6,D5);
#define FIREBASE_HOST "nodemcu-31e7c.firebaseio.com"
#define FIREBASE_AUTH "N9AKR2YBkRgAYXRTJik0NtIMsH7KiTP5EC06xQ1Z"
#define WIFI_SSID "iPhone"
#define WIFI_PASSWORD "ghareisa"

//          Spot 1    Spot 2    Spot 3    Spot 4
// Zone A     x         x         x         x
// Zone B     x         x         x         x

int data[2][4]={ {0,0,0,0},{0,0,0,0} }; 
int prevData[2][4]={ {0,0,0,0},{0,0,0,0} }; 

bool confirm;
int zoneNumb=-1;
String zone="";
String prevz="";
int prevZone=-1;
int spot; 
String stat="";

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
  JsonObject& root = jsonBuffer.createObject();
  JsonObject& rootSerial = jsonBuffer.parseObject(s);

  if (root == JsonObject::invalid())
    return;
    
  zoneNumb = rootSerial["zoneName"];
  
  if(zoneNumb==0){
    zone="CENG Female Zone";
    prevz="CAAS Female Zone";
    prevZone=1;
  } else if(zoneNumb==1){
    zone="CAAS Female Zone";
    prevz="CENG Female Zone";
    prevZone=0;
  }
  
  spot = rootSerial["spotNo"];
  data[zoneNumb][spot-1]= rootSerial["status"];
   
  confirm=true; 
  //data=s.read();
  //Serial.print("data: ");
  //Serial.print(data);
  //Serial.println("");
  if(data[prevZone][spot-1]==1){
    stat="Not Available";
  } else{
    stat="Available";
  }
  
  if (data[zoneNumb][spot-1]==1 && data[zoneNumb][spot-1]!=prevData[zoneNumb][spot-1]){

  prevData[zoneNumb][spot-1]=1;
  
  Firebase.remove("spots");
  root["spotNo"] = spot;
  root["status"] = "Not Available";
  root["zoneName"] = zone;
  String name1 = Firebase.push("spots",root);
  //Serial.println("Car is present in the parking spot.");
  root["spotNo"] = spot;
  root["status"] = stat;
  root["zoneName"] = prevz;
  String name2 = Firebase.push("spots",root);
  // handle error
  if (Firebase.failed()) {
      Serial.print("setting failed:");
      Serial.println(Firebase.error());  
      prevData[zoneNumb][spot-1]=0;
      confirm=false;
      return;
  }
  if(confirm){ 
    s.write(30); //confirm for a car is present - unavailable
  }
  } else if (data[zoneNumb][spot-1]==2 && data[zoneNumb][spot-1]!=prevData[zoneNumb][spot-1]){
    prevData[zoneNumb][spot-1]=2;
    
    //Serial.println("Parking spot is vacant.");
    Firebase.remove("spots");
    root["spotNo"] = spot;
    root["status"] = "Available";
    root["zoneName"] = zone;
    String name2 = Firebase.push("spots",root); 
    root["spotNo"] = spot;
    root["status"] = stat;
    root["zoneName"] = prevz;
  String name3 = Firebase.push("spots",root);
    // handle error
    if (Firebase.failed()) {
      Serial.print("setting failed:");
      Serial.println(Firebase.error());
      prevData[zoneNumb][spot-1]=0;
      confirm=false;  
      return;
    }
    if(confirm){
      s.write(40); //confirm for a car is not present - available
    }
  }

  if(data[zoneNumb][spot-1]==1 && confirm) {
    s.write(30);
  }
  else if(data[zoneNumb][spot-1]==2 && confirm) {
    s.write(40);
  }
  delay(60);
}
