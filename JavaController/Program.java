import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Class Program. 
 * The main class for the background program. 
 * Does not have a user interface. 
 * 
 * @author (Aneesh Neelam)
 * @version (2013.09.10)
 */

public class Program
{
    private Thread UDPSendThread;
    private Thread UDPListenThread;
    
    private boolean startsending;
    private boolean startlistening;
    private boolean send;
    private boolean listen;
    
    private String rhost;
    private int lport;
    private int rport;
    
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private String sendStr;
    private String receiveStr;
    
    public Program()
    {
        UDPSendThread = null;
        UDPListenThread = null;
        
        rhost = "192.168.1.237";
        rport = 7000;
        lport = 7001;
        
        sendStr = "Get Data. ";
        
        startlistening = true;
        startUDPListenThread();
        
        startsending = true;
        startUDPSendThread();
    }
    
    private void startUDPSendThread()
    {
        if(startsending)
        {
            UDPSendThread = new Thread(UDPSend);
            UDPSendThread.start();
            send = true;
        }
    }
    
    private void startUDPListenThread()
    {
        if(startlistening)
        {
            UDPSendThread = new Thread(UDPReceive);
            UDPSendThread.start();
            listen = true;
        }
    }
    
    public void changeMsg(String msg)
    {
        send = false;
        sendStr = msg;
        send = true;
    }
    
    public String getMsg()
    {
        return receiveStr;
    }
    
    Runnable UDPSend = new Runnable()
    {
        public void run() 
        {
            Scanner sc = new Scanner(System.in);
            try 
            {
                while (send) 
                {
                    // System.out.println("\n\nEnter message: ");
                    // sendStr = sc.nextLine();
                    byte [] sendData = sendStr.getBytes(UTF8_CHARSET);
                    Network.Send_Packet(rhost, rport, sendData);
                    Thread.sleep(1000);
                }
            } 
            catch (Exception ex) 
            {
                System.err.println(ex.toString());
            }
            sc.close();
        }
    };
    
    Runnable UDPReceive = new Runnable()
    {
        public void run() 
        {
            try 
            {
                while (listen) 
                {
                    byte [] receiveData = Network.Receive_Packet(lport);
                    receiveStr = new String(receiveData, UTF8_CHARSET);
                    
                    System.out.println("Received: " + receiveStr +" \n\n");
                }
            } 
            catch (Exception ex) 
            {
                System.err.println(ex.toString());
            }
        }
    };
}

