#include <Wire.h>
#include "Adafruit_TCS34725.h"

// Pick analog outputs, for the UNO these three work well
// use ~560  ohm resistor between Red & Blue, ~1K for green (its brighter)
#define redpin 3
#define greenpin 5
#define bluepin 6
// for a common anode LED, connect the common pin to +5V
// for common cathode, connect the common to ground

// set to false if using a common cathode LED
#define commonAnode true

// our RGB -> eye-recognized gamma color
byte gammatable[256];

int delayTime = 50;
String state = "";

Adafruit_TCS34725 tcs = Adafruit_TCS34725(TCS34725_INTEGRATIONTIME_50MS, TCS34725_GAIN_16X);

void setup() {
  Serial.begin(9600);


  // thanks PhilB for this gamma table!
  // it helps convert RGB colors to what humans see
  for (int i = 0; i < 256; i++) {
    float x = i;
    x /= 255;
    x = pow(x, 2.5);
    x *= 255;

    if (commonAnode) {
      gammatable[i] = 255 - x;
    } else {
      gammatable[i] = x;
    }
    //Serial.println(gammatable[i]);
  }
}

// The commented out code in loop is example of getRawData with clear value.
// Processing example colorview.pde can work with this kind of data too, but It requires manual conversion to
// [0-255] RGB value. You can still uncomments parts of colorview.pde and play with clear value.
void loop() {
  readInput();

  float red, green, blue;

  Serial.print("delay ");
  Serial.println(delayTime);
  delay(delayTime);  // Delay for one new integ. time period (to allow new reading)
  tcs.getRGB(&red, &green, &blue);
  printColor(red, green, blue);
}

void readInput() {
  while (Serial.available()) {

    state = "";
    state = Serial.readStringUntil("$");
    state.remove(2);

    Serial.print("data ");
    Serial.println(state);
    switch (state.toInt()) {
      case 22:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_2_4MS);
        delayTime = 4;
        break;
      case 23:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_24MS);
        delayTime = 24;
        break;
      case 24:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_50MS);
        delayTime = 50;
        break;
      case 25:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_60MS);
        delayTime = 60;
        break;
      case 26:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_101MS);
        delayTime = 101;
        break;
      case 27:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_120MS);
        delayTime = 120;
        break;
      case 28:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_154MS);
        delayTime = 154;
        break;
      case 29:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_180MS);
        delayTime = 180;
        break;
      case 32:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_199MS);
        delayTime = 199;
        break;
      case 33:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_240MS);
        delayTime = 240;
        break;
      case 34:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_300MS);
        delayTime = 300;
        break;
      case 35:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_360MS);
        delayTime = 360;
        break;
      case 36:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_401MS);
        delayTime = 401;
        break;
      case 37:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_420MS);
        delayTime = 420;
        break;
      case 38:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_480MS);
        delayTime = 480;
        break;
      case 39:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_499MS);
        delayTime = 499;
        break;
      case 42:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_540MS);
        delayTime = 540;
        break;
      case 44:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_600MS);
        delayTime = 600;
        break;
      case 46:
        tcs.setIntegrationTime(TCS34725_INTEGRATIONTIME_614MS);
        delayTime = 614;
        break;
      case 52:
        tcs.setGain(TCS34725_GAIN_1X);
        break;
      case 53:
        tcs.setGain(TCS34725_GAIN_4X);
        break;
      case 54:
        tcs.setGain(TCS34725_GAIN_16X);
        break;
      case 55:
        tcs.setGain(TCS34725_GAIN_60X);
        break;
    }
  }
}

void printColor(int red, int green, int blue) {
  Serial.print("Temp:");
  Serial.print(0);
  Serial.println("E");
  Serial.print("Lux:");
  Serial.print(0);
  Serial.println("E");
  Serial.print("Red:");
  Serial.print(int(red));
  Serial.println("E");
  Serial.print("Green:");
  Serial.print(int(green));
  Serial.println("E");
  Serial.print("Blue:");
  Serial.print(int(blue));
  Serial.println("E");
  Serial.print("Clear:");
  Serial.print(0);
  Serial.println("E");
  Serial.print("Html:");
  Serial.print((int)red, HEX);
  Serial.print((int)green, HEX);
  Serial.print((int)blue, HEX);
  Serial.println("E");
  delay(100);
}