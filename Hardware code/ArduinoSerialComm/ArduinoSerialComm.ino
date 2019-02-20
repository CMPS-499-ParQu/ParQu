//Arduino code
#include <SoftwareSerial.h>
SoftwareSerial s(5,6);
void setup() {
  s.begin(9600);
  Serial.begin(9600);

}
void loop() {
  int data=50;
  if(s.available()>0)
  {
    s.write(data);
    data=s.read();
    Serial.println(data);
  }
}
