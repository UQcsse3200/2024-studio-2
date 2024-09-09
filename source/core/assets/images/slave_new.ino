int state=0; //variable to store bluetooth signal

void setup() {
  Serial.begin(38400);
  pinMode(6,OUTPUT);
  pinMode(7,OUTPUT);
  digitalWrite(6,1); //testing pin 6
  digitalWrite(7,1); //testing pin 7
  delay(3000);
  digitalWrite(6,0);
  digitalWrite(7,0);
}

void loop() {
  if (Serial.available()>0) { //check message availablility
    state=Serial.read(); //updating state
    Serial.println(state); //debugging check
  }
  if(state==0) {
    digitalWrite(6,0);
    digitalWrite(7,0);
  }
  else if(state==1) {
    digitalWrite(6,1);
    digitalWrite(7,0);
  }
  else if(state==2) {
    digitalWrite(6,0);
    digitalWrite(7,1);
  }
  delay(10);
}