int up=0; //variable for up button
int down=0; //variable for down button

void setup() {
  pinMode(7,INPUT); //button 1 on pin 7
  digitalWrite(7,HIGH); //turn on pin 7 inate pullup 20k resistor
  pinMode(6,INPUT); //button 2 on pin 6
  digitalWrite(6,HIGH); //turn on pin 6 inate pullup 20k resistor
  Serial.begin(38400); 
  Serial.write("N");
}

void loop() {
  up = digitalRead(7); //updating up
  down = digitalRead(6); //updating down
  if(up==down) { //checking up and down signals
    Serial.write(0);
  }
  else if(up==0) {
    Serial.write(1);
  }
  else if(down==0) {
    Serial.write(2);
  }
  delay(10);
}
