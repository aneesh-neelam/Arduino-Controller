import java.net.*;

/**
 * Class Network. 
 * Has all the functions needed to send and receive UDP packets over the network. 
 * 
 * Send_Packet function to send a UDP packet. 
 * Takes Remote Host, Remote Port and a Byte Array as arguements. Remote Host can be Broadcast. 
 * Does not return anything
 * 
 * Receive_Packet function to listen for and receive UDP packets. 
 * Takes Remote host and Local Port on as arguements. Remote Host can be Broadcast. 
 * Returns a Byte Array, which is the data of the received packet. 
 * 
 * @author (Aneesh Neelam)
 * @version (2013.08.16)
 */

public class Network
{      
    public static void Send_Packet(String rhost, int rport, byte [] data)
    {
        DatagramSocket sock = null;
        try
        {
            InetAddress host = InetAddress.getByName(rhost);
            sock = new DatagramSocket();
            sock.setBroadcast(true);
            
            DatagramPacket packet = new DatagramPacket(data, data.length, host, rport);
            sock.send(packet);
        }
        catch (Exception ex) 
        {
            System.err.println(ex.toString());
        }
        finally
        {
            if( sock != null )
            {
                sock.close();
            }
        }
    }
    
    public static byte[] Receive_Packet(int lport)
    {
        DatagramSocket sock = null;
        byte[] data = new byte[512];
        try
        {
            sock = new DatagramSocket(lport);
            sock.setBroadcast(true);
            
            DatagramPacket packet = new DatagramPacket(data, data.length);            
            sock.receive(packet);
            data = packet.getData();
        }
        catch (Exception ex) 
        {
            System.err.println(ex.toString());
        }
        finally
        {
            if( sock != null )
            {
                sock.close();
            }
        }
        return data;
    }
}

