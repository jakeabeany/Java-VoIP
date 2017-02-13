package networks_labs;
/*
 * TextSender.java
 *
 * Created on 15 January 2003, 15:29
 */

/**
 *
 * @author  abj
 */
import java.net.*;
import java.io.*;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;

public class VoIPSenderThread implements Runnable{
    
    static DatagramSocket sending_socket;
    
    public void start(){
        Thread thread = new Thread(this);
	thread.start();
    }
    
    public void run (){
     
        //***************************************************
        //Port to send to
        int PORT = 55555;
        //IP ADDRESS to send to
        InetAddress clientIP = null;
	try {
		clientIP = InetAddress.getByName("DESKTOP-7D13IVJ");  //CHANGE localhost to IP or NAME of client machine
	} catch (UnknownHostException e) {
                System.out.println("ERROR: TextSender: Could not find client IP");
		e.printStackTrace();
                System.exit(0);
	}
        //***************************************************
        
        //***************************************************
        //Open a socket to send from
        //We dont need to know its port number as we never send anything to it.
        //We need the try and catch block to make sure no errors occur.
        
        //DatagramSocket sending_socket;
        try{
		sending_socket = new DatagramSocket3();
	} catch (SocketException e){
                System.out.println("ERROR: TextSender: Could not open UDP socket to send from.");
		e.printStackTrace();
                System.exit(0);
	}
        //***************************************************
      
        //***************************************************
        //Get a handle to the Standard Input (console) so we can read user input
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        //***************************************************
        
        //***************************************************
        //Main loop.
        
        boolean running = true;
        int i = 1;
        while (running){
            try{
                //Read in a string from the standard input
                //String str = in.readLine();
                //String str = in.readLine();
                
                String str = null;
                
                if(i == 100)
                    running = false;
                
                for(i = 0; i < 100; i++){
                    str = "packet" + (i+1) + "\n";
                    
                    //Convert it to an array of bytes
                    byte[] buffer = str.getBytes();

                    //Make a DatagramPacket from it, with client address and port number
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, clientIP, PORT);

                    //Send it
                    sending_socket.send(packet);
                }
                

            } catch (IOException e){
                System.out.println("ERROR: TextSender: Some random IO error occured!");
                e.printStackTrace();
            }
        }
        //Close the socket
        sending_socket.close();
        //***************************************************
    }
} 
