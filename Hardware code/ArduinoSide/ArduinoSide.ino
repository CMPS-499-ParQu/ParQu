//Arduino code
#include <SoftwareSerial.h>
SoftwareSerial s(5,6);
const int TRIG_PIN = 7;
const int ECHO_PIN = 8;
const unsigned int MAX_DIST = 23200;

void setup() {
  s.begin(9600);
  Serial.begin(9600);

  pinMode(TRIG_PIN, OUTPUT);
  digitalWrite(TRIG_PIN, LOW);
}
int data=0; 
void loop() {
  unsigned long t1;
  unsigned long t2;
  unsigned long pulse_width;
  float cm;
  
  // Hold the trigger pin high for at least 10 us
  digitalWrite(TRIG_PIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIG_PIN, LOW);

  // Wait for pulse on echo pin
  while ( digitalRead(ECHO_PIN) == 0 );

  // Measure how long the echo pin was held high (pulse width)
  // Note: the micros() counter will overflow after ~70 min
  t1 = micros();
  while ( digitalRead(ECHO_PIN) == 1);
  t2 = micros();
  pulse_width = t2 - t1;

  // Calculate distance in centimeters and inches. The constants
  // are found in the datasheet, and calculated from the assumed speed 
  //of sound in air at sea level (~340 m/s).
  cm = pulse_width / 58.0;

  if ( pulse_width > MAX_DIST ) {
    //Serial.println("Out of range");
  } else if(cm<15){
    Serial.print("Car Present: TRUE\t");
    Serial.print(cm);
    Serial.println(" cm");
    data=1;
    s.write(data);
  } else if(cm>15){
    Serial.print("Car Present: FALSE\t");
    Serial.print(cm);
    Serial.println(" cm");
    data=2;
    s.write(data);
  }
  delay(500);
}
