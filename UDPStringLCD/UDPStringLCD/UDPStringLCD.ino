#include <LiquidCrystal.h>
#include <SPI.h>         // needed for Arduino versions later than 0018
#include <Ethernet.h>
#include <EthernetUdp.h>         // UDP library from: bjoern@cs.stanford.edu 12/30/2008

// Enter a MAC address and IP address for your controller below.
// The IP address will be dependent on your local network:
byte mac[] = {  
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress ip(192, 168, 1, 237);
unsigned int localPort = 7000;      // local port to listen on
// initialize the library with the numbers of the interface pins
LiquidCrystal lcd(12, 11, 5, 4, 3, 2);
char packetBuffer[UDP_TX_PACKET_MAX_SIZE]; //buffer to hold incoming packet,
char ReplyBuffer[50];

EthernetUDP Udp;

void setup() {
  // set up the LCD's number of columns and rows: 
  lcd.begin(16, 2);
  // Print a message to the LCD.
  Ethernet.begin(mac,ip);
  Udp.begin(localPort);
  lcd.print("");
}

void loop() {
  // set the cursor to column 0, line 1
  // (note: line 1 is the second row, since counting begins with 0):
  lcd.setCursor(0, 1);
  // print the number of seconds since reset:
  lcd.print(millis()/1000);
  
  int packetSize = Udp.parsePacket();
  if(packetSize)
  {
    // read the packet into packetBufffer
    Udp.read(packetBuffer,UDP_TX_PACKET_MAX_SIZE);
    // send a reply, to the IP address and port that sent us the packet we received. 
    Udp.beginPacket(Udp.remoteIP(), 7001);
    lcd.print(packetBuffer);
    String msg = String(packetBuffer);
    String reply = String("Displaying message: " + msg);
    reply.toCharArray(ReplyBuffer,100);
    Udp.write(ReplyBuffer);
    Udp.endPacket();
  }
}

