int sensor1 = A2;
int sensor2 = A3;
int sensor3 = A4;
int sensor4 = A5;

//int's for arduino motor shield (left)
const int dirl = 12;
const int brel =  9;
const int pmwl =  3;

//int's for arduino motor shield (right)
const int dirr = 13;
const int brer =  8;
const int pmwr = 11;

int maxspeed = 200;

bool pressed = false;

//***change those values to suit ur enviroment***speed declaration for motors speeds (forward & reverse = speedmax)(turnright or turnleft = speeedmin
int speedmax = 255; //set defult speed of motor 0-255
int speedmin;//set the defult min speed to turn 0-255
int mode = 800;  //value of difference for motors to react 0-1023

char mov = '5';
char robotMode = 's';
char exmov = '5';

int sensors = 0;
int exsensors = 0;

int threshold;

void setup()//here those things run once and that is when the arduino first gets power
{
  //set all pinmodes of motor driver
  pinMode(dirl, OUTPUT);
  pinMode(dirr, OUTPUT);
  pinMode(brel, OUTPUT);
  pinMode(brer, OUTPUT);
  pinMode(pmwl, OUTPUT);
  pinMode(pmwr, OUTPUT);

  //set pinmodes of inputs or ir detectors
  pinMode(sensor1, INPUT);
  pinMode(sensor2, INPUT);
  pinMode(sensor3, INPUT);
  pinMode(sensor4, INPUT);

  //begin the serial monitor for input monitoring values and for calebrating the motors' turning speed
  Serial.begin(9600);

  //make sure to set brakes low so the motor can move
  digitalWrite(brel, LOW);
  digitalWrite(brer, LOW);

  //Detect the threshold (the 1st sensor must be on white while the second on black)
  threshold = (analogRead(sensor1) + analogRead(sensor2) + analogRead(sensor3) + analogRead(sensor4))/4;
  

}

void loop()//here is where everyting run sover and over again until the power shuts off, the arduino.
{
  if(mov!='5')
      exmov = mov;
  if(Serial.available()>0)
  {
    pressed = true;
    mov = Serial.read();
    if( mov == 'H' || mov == 'C' || mov == 'A')
    {
      robotMode = mov;
      mov = '5';
    }
    if( mov > 'I')
    {
      maxspeed = ((mov) - 'J')*5;
      mov = '5';
    }
  }
  else
    pressed = false;
   

  if (robotMode == 'C' || robotMode == 'A')
  {
    
    exsensors = sensors;
    
    sensors = 0;
  
    int s1 = analogRead(sensor1);
    int s2 = analogRead(sensor2);
    int s3 = analogRead(sensor3);
    int s4 = analogRead(sensor4);

    sensors += (s1>threshold)?1:0;
    sensors += (s2>threshold)?2:0;
    sensors += (s3>threshold)?4:0;
    sensors += (s4>threshold)?8:0;
    //if( robotMode == 'C')
    //{
      if ((sensors != exsensors) || (mov != exmov))
      {
        Serial.print(sensors);
        Serial.print("-");
        if(pressed)
          Serial.print(mov);
        else
          Serial.print('0');
        Serial.print("#"); 
      }
    //}
    /*
     if ( robotMode == 'A')
    {
      if(sensors != exsensors)
      {
        Serial.print(sensors);
        Serial.print("#"); 
      }
    }
    */
    
    switch(mov)
    {
      case '1': backleft(); break;
      case '2': backward(); break;
      case '3': backright(); break;
      case '4': turnleft(); break;
      case '5': STOP(); break;
      case '6': turnright(); break;
      case '7': forleft(); break;
      case '8': forward(); break;
      case '9': forright(); break;
    }
  }
  
}

void RELEASE()
{
  digitalWrite(brel, LOW);
  digitalWrite(brer, LOW);
}  

void forward(void)//decelares the forward() void
{
  digitalWrite(dirl, LOW);
  digitalWrite(dirr, LOW);
  digitalWrite(brel, LOW);
  digitalWrite(brer, LOW);
  analogWrite(pmwl, maxspeed);
  analogWrite(pmwr, maxspeed);
}

void backward(void)//decelares the forward() void
{
  digitalWrite(dirl, HIGH);
  digitalWrite(dirr, HIGH);
  digitalWrite(brel, LOW);
  digitalWrite(brer, LOW);
  analogWrite(pmwl, maxspeed);
  analogWrite(pmwr, maxspeed);
}

void turningspeed(void)//decelares the forward() void
{
  digitalWrite(dirl, LOW);
  digitalWrite(dirr, LOW);
  digitalWrite(brel, LOW);
  digitalWrite(brer, LOW);
  analogWrite(pmwl, 80);
  analogWrite(pmwr, 80);
}

void STOP(void)//decelares the reverse() void
{
  digitalWrite(dirl, LOW);
  digitalWrite(dirr, LOW);
  digitalWrite(brel, HIGH);
  digitalWrite(brer, HIGH);
  analogWrite(pmwl, speedmax);
  analogWrite(pmwr, speedmax);
}

void turnright(void)//decelares the turnright() void
{
  digitalWrite(dirl, LOW);
  digitalWrite(dirr, LOW);
  digitalWrite(brel, HIGH);
  digitalWrite(brer, LOW);
  analogWrite(pmwl, maxspeed);
  analogWrite(pmwr, maxspeed);
}

void turnleft(void)//decelares the turnleft() void
{
  digitalWrite(dirl, LOW);
  digitalWrite(dirr, LOW);
  digitalWrite(brel, LOW);
  digitalWrite(brer, HIGH);
  analogWrite(pmwl, maxspeed);
  analogWrite(pmwr, maxspeed);
}

void forright(void)
{
  digitalWrite(dirl, LOW);
  digitalWrite(dirr, LOW);
  digitalWrite(brel, LOW);
  digitalWrite(brer, LOW);
  analogWrite(pmwl, maxspeed*0.75);
  analogWrite(pmwr, maxspeed);
  
}

void forleft(void)
{
  
  digitalWrite(dirl, LOW);
  digitalWrite(dirr, LOW);
  digitalWrite(brel, LOW);
  digitalWrite(brer, LOW);
  analogWrite(pmwl, maxspeed);
  analogWrite(pmwr, maxspeed*0.75);
}

void backright(void)
{
  digitalWrite(dirl, HIGH);
  digitalWrite(dirr, HIGH);
  digitalWrite(brel, LOW);
  digitalWrite(brer, LOW);
  analogWrite(pmwl, maxspeed);
  analogWrite(pmwr, maxspeed*0.75);
  
}

void backleft(void)
{
  
  digitalWrite(dirl, HIGH);
  digitalWrite(dirr, HIGH);
  digitalWrite(brel, LOW);
  digitalWrite(brer, LOW);
  analogWrite(pmwl, maxspeed*0.75);
  analogWrite(pmwr, maxspeed);
}
