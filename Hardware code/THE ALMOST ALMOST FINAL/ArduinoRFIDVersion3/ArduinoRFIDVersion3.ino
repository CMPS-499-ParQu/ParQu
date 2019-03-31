#include <ArduinoJson.h>
#include <SPI.h>
#include <MFRC522.h>
#include <SoftwareSerial.h>


SoftwareSerial s(5, 6);
#define SS_PIN 10
#define RST_PIN 9
MFRC522 mfrc522(SS_PIN, RST_PIN);   // Create MFRC522 instance.
const int LED_OPEN = 3;
const int LED_CLOSE = 2;
const int buttonPin = 8;
bool inProgress = false;
int buttonState = 0;
int zoneNumb = -1;
int n;
void setup() {
  s.begin(9600);
  Serial.begin(9600);   // Initiate a serial communication
  SPI.begin();      // Initiate  SPI bus
  mfrc522.PCD_Init();   // Initiate MFRC522
  Serial.println("Approximate your card to the reader...");
  Serial.println();
  pinMode(LED_OPEN, OUTPUT);
  pinMode(LED_CLOSE, OUTPUT);
  digitalWrite(LED_CLOSE, HIGH);
  digitalWrite(LED_OPEN, LOW);
  pinMode(buttonPin, INPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  // Look for new cards
  StaticJsonBuffer<1000> jsonBuffer;
  JsonObject& root = jsonBuffer.createObject();

  if (!inProgress) {
    
    if ( ! mfrc522.PICC_IsNewCardPresent())
    {
      return;
    }
    // Select one of the cards
    if ( ! mfrc522.PICC_ReadCardSerial())
    {
      return;
    }
    zoneNumb=-1;
    buttonState = digitalRead(buttonPin);
    if(buttonState == LOW){
      zoneNumb = 0;
      Serial.println("low: Library Building");
    } else if(buttonState == HIGH){
      zoneNumb = 1;
      Serial.println("high: Business Building");
    }
    //Serial.println(mfrc522.uid.size);
    //Show UID on serial monitor
    Serial.print("UID tag :");
    int numbers[mfrc522.uid.size];
    String content = "";
    byte letter;
    for (byte i = 0; i < mfrc522.uid.size; i++)
    {
      Serial.print(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " ");
      Serial.print(mfrc522.uid.uidByte[i], HEX);
      numbers[i] = mfrc522.uid.uidByte[i];
      content.concat(String(mfrc522.uid.uidByte[i] < 0x10 ? " 0" : " "));
      content.concat(String(mfrc522.uid.uidByte[i], HEX));
    }
    Serial.println();
    root["zoneNumb"] = zoneNumb;
    root["A"] = numbers[0];
    root["B"] = numbers[1];
    root["C"] = numbers[2];
    root["D"] = numbers[3];
    root.printTo(s);
    delay(500);
    n = s.read();
    if (n == 10) {
      inProgress = true;
    }


  } else {
    n = s.read();
    if (n == 20) {
      Serial.println("Access allowed");
      inProgress = false;
      digitalWrite(LED_CLOSE, LOW);
      digitalWrite(LED_OPEN, HIGH);
      delay(5000);
      digitalWrite(LED_CLOSE, HIGH);
      digitalWrite(LED_OPEN, LOW);
      delay(100);

    } else if (n == 40) {
      Serial.println("Access denied");
      inProgress = false;
      delay(100);
    }
  }
  delay(200);
}
