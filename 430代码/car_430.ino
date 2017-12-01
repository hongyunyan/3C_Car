#define LEFT_AHEAD 9
#define LEFT_BACK 10
#define RIGHT_AHEAD 13
#define RIGHT_BACK 12
#define STEER 14
int RUNL=80;
int RUNR=70;
int TURN=1;
int Solid=1470;
char incomingByte;
void Advance();
void turnLeft();
void turnRight();
void Back();
void Stop();
void steer(float portion);
void setup()
{
  Serial.begin(9600);
  pinMode(LEFT_BACK,OUTPUT);
  pinMode(RIGHT_BACK,OUTPUT); 
  pinMode(LEFT_AHEAD,OUTPUT);
  pinMode(RIGHT_AHEAD,OUTPUT);
  pinMode(STEER,OUTPUT);
  digitalWrite(LEFT_AHEAD,LOW);
  digitalWrite(RIGHT_AHEAD,LOW);
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_BACK,LOW);
  digitalWrite(STEER,LOW);
}

void loop()
{
  if (Serial.available()>0)
  {
    incomingByte=Serial.read();
    if (incomingByte=='A')
    {
      Serial.println("GO AHEAD");
      Advance();
      Serial.read();
    }
    else if (incomingByte=='B')
    {
      Serial.println("GO BACK");
      Back();
      Serial.read();
    }
    else if (incomingByte=='L')
    {
      Serial.println("TURN LEFT");
      turnLeft();
      Serial.read();
    }
    else if (incomingByte=='R')
    {
      Serial.println("TURN RIGHT");
      turnRight();
      Serial.read();
    }
    else if (incomingByte=='S')
    {
      Serial.println("STOP");
      Stop();
      Serial.read();
    }
  }
}

void Advance()
{
  for(int i=0;i<200;i++)
  {
    analogWrite(LEFT_AHEAD,i);
    analogWrite(RIGHT_AHEAD,i);
  }
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_BACK,LOW);  
  analogWrite(LEFT_AHEAD,RUNL);
  analogWrite(RIGHT_AHEAD,RUNR);
  steer(1);
}

void Back()
{
  for(int i=0;i<200;i++)
  {
    analogWrite(LEFT_BACK,i);
    analogWrite(RIGHT_BACK,i);
  }
  digitalWrite(LEFT_AHEAD,LOW);
  digitalWrite(RIGHT_AHEAD,LOW);  
  analogWrite(LEFT_BACK,RUNL);
  analogWrite(RIGHT_BACK,RUNR);
  steer(1);
}

void turnLeft()
{ analogWrite(LEFT_BACK,TURN*0.0);
  analogWrite(RIGHT_AHEAD,TURN*0.01);
  analogWrite(LEFT_AHEAD,0);
  analogWrite(RIGHT_AHEAD,RUNR);  
  steer(0.8);
}

void turnRight()
{ 
  analogWrite(LEFT_AHEAD,TURN*0.01);
  analogWrite(RIGHT_AHEAD,TURN*0.0);
  analogWrite(LEFT_AHEAD,RUNL);
  analogWrite(RIGHT_AHEAD,0);
  steer(0.8);
}

void Stop()
{
  digitalWrite(LEFT_BACK,LOW);
  digitalWrite(RIGHT_BACK,LOW);  
  analogWrite(LEFT_AHEAD,LOW);
  analogWrite(RIGHT_AHEAD,LOW);
}

void steer(float portion)
{
  int tmp=portion*Solid;
  for (int i=0; i<8; i++)
  {
     digitalWrite(STEER,HIGH);
     delayMicroseconds(tmp);
     digitalWrite(STEER,LOW);
     delayMicroseconds(10000-tmp);
     delayMicroseconds(6550);
  }
}
  
  
  




 
