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
String zone="";
int zoneNumb=-1;
int spot; 


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
    
  zone = rootSerial["zoneName"];
  if(zone=="A"){
    zoneNumb=0;
  } else if(zone=="B"){
    zoneNumb=1;
  }
  spot = rootSerial["spotNo"];
  data[zoneNumb][spot-1]= rootSerial["status"];
   
  confirm=true; 
  //data=s.read();
  //Serial.print("data: ");
  //Serial.print(data);
  //Serial.println("");
 
  
  if (data[zoneNumb][spot-1]==1 && data[zoneNumb][spot-1]!=prevData[zoneNumb][spot-1]){

  prevData[zoneNumb][spot-1]=1;
  
  Firebase.remove("spots");
  root["spotNo"] = spot;
  root["status"] = 1;
  root["zoneName"] = zone;
  String name1 = Firebase.push("spots",root);
  Serial.println("Car is present in the parking spot.");
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
    
    Serial.println("Parking spot is vacant.");
    Firebase.remove("spots");
    root["spotNo"] = spot;
    root["status"] = 2;
    root["zoneName"] = zone;
    String name2 = Firebase.push("spots",root); 
    // handle error
    if (Firebase.failed()) {
      Serial.print("setting failed:");
      Serial.println(Firebase.error());
      prevData=0;
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
