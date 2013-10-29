#include <SPI.h>         // needed for Arduino versions later than 0018
#include <Ethernet.h>
#include <EthernetUdp.h>         // UDP library from: bjoern@cs.stanford.edu 12/30/2008

// Enter a MAC address and IP address for your controller below.
// The IP address will be dependent on your local network:
byte mac[] = {  
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress ip(192, 168, 1, 237);

unsigned int localPort = 7000;      // local port to listen on

// buffers for receiving and sending data
char packetBuffer[UDP_TX_PACKET_MAX_SIZE]; //buffer to hold incoming packet,
char ReplyBuffer[50];       // a string to send back
String SensorString;
int sensorPin = A0;            // select the input pin for the ldr
unsigned int sensorValue = 0;  // variable to store the value coming from the ldr

// An EthernetUDP instance to let us send and receive packets over UDP
EthernetUDP Udp;
int led = 13;

void setup() {
  pinMode(led, OUTPUT);
  Serial.begin(9600);
  // start the Ethernet and UDP:
  Ethernet.begin(mac,ip);
  Udp.begin(localPort);
}

void loop() {
  sensorValue = analogRead(sensorPin);
  String val = String(sensorValue);
  if(sensorValue<400) {
    digitalWrite(13, HIGH);   // set the LED on
    SensorString = String("Low Light, less than value 400. Value: " + val);
    Serial.print("Low Light, less than value 400. Value: " + val+"\n");
  }
  else { 
    digitalWrite(13, LOW);   // set the LED on
    SensorString = String("High Light, more than value 400. Value: " + val);
    Serial.print("High Light, more than value 400. Value: " + val+"\n");
  }
  delay(1000);
  // String SensorString = String(sensorValue);
  SensorString.toCharArray(ReplyBuffer,45);
  // if there's data available, read a packet
  int packetSize = Udp.parsePacket();
  if(packetSize)
  {
    digitalWrite(led, HIGH);
    // read the packet into packetBufffer
    Udp.read(packetBuffer,UDP_TX_PACKET_MAX_SIZE);
    // send a reply, to the IP address and port that sent us the packet we received. 
    Udp.beginPacket(Udp.remoteIP(), 7001);
    Udp.write(ReplyBuffer);
    Udp.endPacket();
  }
}

