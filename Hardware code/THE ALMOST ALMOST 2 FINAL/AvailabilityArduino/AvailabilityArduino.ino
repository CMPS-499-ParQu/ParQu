// Availability Arduino

#include <SoftwareSerial.h>
#include <ArduinoJson.h>

SoftwareSerial s(11, 10);

const int TRIG_PINS[4] = {2, 4, 6, 8};
const int ECHO_PINS[4] = {3, 5, 7, 9};

const unsigned int MAX_DIST = 23200;
const int buttonPin = 12;
int buttonState = 0;

void setup() {
  s.begin(9600);
  Serial.begin(9600);
  
  pinMode(buttonPin, INPUT);

  for (auto trig : TRIG_PINS) {
    pinMode(trig, OUTPUT);
    digitalWrite(trig, LOW);
  }

}
//          Spot 1    Spot 2    Spot 3    Spot 4
// Zone A     x         x         x         x
// Zone B     x         x         x         x

int spotStatus[2][4] = { {0, 0, 0, 0}, {0, 0, 0, 0} };
int prevSpotStatus[2][4] = { {0, 0, 0, 0}, {0, 0, 0, 0} };
//bool changeInStatus[4] = { false, false, false, false };
bool changeInStatus = false;
int spotChangesSerial[4] = { 0, 0, 0, 0};
int arraySize = 4;
int zoneNumb = -1;
int spot;

int confirm = 0;

unsigned long t1;
unsigned long t2;
unsigned long pulse_width;
float cm;

void loop() {

  //Switching zones
  buttonState = digitalRead(buttonPin);
  if (buttonState == LOW) {
    zoneNumb = 0;
  } else if (buttonState == HIGH) {
    zoneNumb = 1;
  }

  takeReading();
  checkForChanges();
  sendChangesToSerial();
  prepareForAnotherReading();

  delay(60);
}

void takeReading() {
  for (int i = 0; i < arraySize; i++) {
 
    // Hold the trigger pin high for at least 10 us
    digitalWrite(TRIG_PINS[i], HIGH);
    delayMicroseconds(10);
    digitalWrite(TRIG_PINS[i], LOW);

    // Wait for pulse on echo pin
    //Serial.println("before echo = 0");
    while ( digitalRead(ECHO_PINS[i]) == 0 );

    // Measure how long the echo pin was held high (pulse width)
    // Note: the micros() counter will overflow after ~70 min
    //Serial.println("before echo = 1");
    t1 = micros();
    while ( digitalRead(ECHO_PINS[i]) == 1);
    t2 = micros();
    pulse_width = t2 - t1;

    // Calculate distance in centimeters and inches. The constants
    // are found in the datasheet, and calculated from the assumed speed
    //of sound in air at sea level (~340 m/s).
    cm = pulse_width / 58.0;

    if (cm < 20) {
      spotStatus[zoneNumb][i] = 1;
      //Serial.print("Car Present: ");
      //Serial.print(cm);
      //Serial.println(" cm");
    } else if (cm > 20) {
      spotStatus[zoneNumb][i] = 2;
      //Serial.print("Car Not Present: ");
      //Serial.print(cm);
      //Serial.println(" cm");
    }
    delay(100);
  }
  //  Serial.print(spotStatus[zoneNumb][0]);
  //  Serial.print(",");
  //  Serial.print(spotStatus[zoneNumb][1]);
  //  Serial.print(",");
  //  Serial.print(spotStatus[zoneNumb][2]);
  //  Serial.print(",");
  //  Serial.print(spotStatus[zoneNumb][3]);
  //  Serial.println("");


}

void checkForChanges() {
  for (int i = 0; i < arraySize; i++) {
    if (spotStatus[zoneNumb][i] != prevSpotStatus[zoneNumb][i]) {
      changeInStatus = true;
      spotChangesSerial[i] = spotStatus[zoneNumb][i];
      Serial.print("CHANGE IN SPOT ");
      Serial.print(i + 1);
      Serial.print(" - STAT ");
      Serial.println(spotStatus[zoneNumb][i]);
    } else {
      spotChangesSerial[i] = 3;
    }
  }
}

void sendChangesToSerial() {
  if (changeInStatus) {
    StaticJsonBuffer<200> jsonBuffer;
    JsonObject& root = jsonBuffer.createObject();

    root["zoneNumb"] = zoneNumb;

    root["spot1"] = spotChangesSerial[0];
    root["spot2"] = spotChangesSerial[1];
    root["spot3"] = spotChangesSerial[2];
    root["spot4"] = spotChangesSerial[3];

    root.printTo(s);
    delay(500);
    confirm = s.read();
    if (confirm != 30) {
      root.printTo(s);
    }
    
    changeInStatus = false;
    confirm = 0;
  }
}

void prepareForAnotherReading() {
  for (int i = 0; i < arraySize; i++) {
    prevSpotStatus[zoneNumb][i] = spotStatus[zoneNumb][i];
  }

}
