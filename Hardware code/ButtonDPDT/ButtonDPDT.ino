
const int buttonPin8 = 8;     // the number of the pushbutton pin
const int buttonPin7 = 7;     // the number of the pushbutton pin

int buttonState8 = 0;         // variable for reading the pushbutton status
int buttonState7 = 0;         // variable for reading the pushbutton status

void setup() {
 
  // initialize the pushbutton pin as an input:
  pinMode(buttonPin8, INPUT);
  pinMode(buttonPin7, INPUT);

  Serial.begin(9600);

}

void loop() {
  // read the state of the pushbutton value:
  buttonState8 = digitalRead(buttonPin8);
  buttonState7 = digitalRead(buttonPin7);

  // check if the pushbutton is pressed. If it is, the buttonState is HIGH:
  if (buttonState8 == HIGH && buttonState7 == LOW) {
    Serial.println("8: HIGH && 7: LOW");
  } else if (buttonState8 == LOW && buttonState7 == HIGH) {
    Serial.println("8: LOW && 7: HIGH");
  } else {
    Serial.println("IN ERROR");
  }

  delay(1000);
}
