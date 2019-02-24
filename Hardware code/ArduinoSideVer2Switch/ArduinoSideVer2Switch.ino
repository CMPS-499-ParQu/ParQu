//Arduino code
#include <SoftwareSerial.h>
#include <ArduinoJson.h>

SoftwareSerial s(5,6);
const int TRIG_PIN = 7;
const int ECHO_PIN = 8;
const unsigned int MAX_DIST = 23200;
const int buttonPin = 12;
int buttonState = 0;
void setup() {
  s.begin(9600);
  Serial.begin(9600);
  pinMode(buttonPin, INPUT);
  pinMode(TRIG_PIN, OUTPUT);
  digitalWrite(TRIG_PIN, LOW);
}
//          Spot 1    Spot 2    Spot 3    Spot 4
// Zone A     x         x         x         x
// Zone B     x         x         x         x

int data[2][4]={ {0,0,0,0},{0,0,0,0} }; 
int prevData[2][4]={ {0,0,0,0},{0,0,0,0} }; 

int confirm=0;
String zone="";
int zoneNumb=-1;
int spot; 
bool delivered=false;

void loop() {
  StaticJsonBuffer<1000> jsonBuffer;
  JsonObject& root = jsonBuffer.createObject();
  
  //Switching zones
  buttonState = digitalRead(buttonPin);
  if(buttonState == LOW){
    zone="A";
    zoneNumb=0;
  } else if(buttonState == HIGH){
    zone="B"; 
    zoneNumb=1;
  }

  unsigned long t1;
  unsigned long t2;
  unsigned long pulse_width;
  float cm;
  
  // Hold the trigger pin high for at least 10 us
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN, LOW);

  // Wait for pulse on echo pin
  Serial.println("before ECHO_PIN == 0");
  while ( digitalRead(ECHO_PIN) == 0 );

  // Measure how long the echo pin was held high (pulse width)
  // Note: the micros() counter will overflow after ~70 min
  t1 = micros();
  Serial.println("before ECHO_PIN == 1");
  while ( digitalRead(ECHO_PIN) == 1);
  t2 = micros();
  pulse_width = t2 - t1;
  
  // Calculate distance in centimeters and inches. The constants
  // are found in the datasheet, and calculated from the assumed speed 
  //of sound in air at sea level (~340 m/s).
  cm = pulse_width / 58.0;
  
  if ( pulse_width > MAX_DIST ) {
    Serial.println("Out of range");
  } else if(cm<15){
    Serial.print("Car Present: TRUE\t");
    Serial.print(cm);
    Serial.println(" cm");

    if(prevData[zoneNumb][0]!=1){
      delivered=false;
      data[zoneNumb][0]=1;
      prevData[zoneNumb][0]=1;
      
      root["spotNo"] = 1;
      root["status"] = 1;
      root["zoneName"] = zone;
      if(s.available()>0) {
        root.printTo(s);
      }
      confirm=s.read();
      if(confirm==30){
        delivered=true;
      }
    } else if(!delivered){
      s.write(data);
      confirm=s.read();
      if(confirm==30){
        delivered=true;
      }
    }

  } else if(cm>15){
    Serial.print("Car Present: FALSE\t");
    Serial.print(cm);
    Serial.println(" cm");
    
    if(prevData[zoneNumb][0]!=2){
      delivered=false;
      data[zoneNumb][0]=2;
      prevData[zoneNumb][0]=2;
      
      root["spotNo"] = 1;
      root["status"] = 2;
      root["zoneName"] = zone;
      if(s.available()>0) {
        root.printTo(s);
      } 
      
      confirm=s.read();
      if(confirm==40){
        delivered=true;
      }
    } else if(!delivered){
      s.write(data);
      confirm=s.read();
      if(confirm==40){
        delivered=true;
      }
    }
  }
  delay(60);
}
