package networks_labs;
/*
 * TextReceiver.java
 *
 * Created on 15 January 2003, 15:43
 */

/**
 *
 * @author  abj
 */
import java.net.*;
import java.io.*;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;

public class VoIPReceiverThread implements Runnable{
    
    static DatagramSocket receiving_socket;
    
    public void start(){
        Thread thread = new Thread(this);
	thread.start();
    }
    
    public void run (){
     
        //***************************************************
        //Port to open socket on
        int PORT = 55555;
        //***************************************************
        
        //***************************************************
        //Open a socket to receive from on port PORT
        
        //DatagramSocket receiving_socket;
        try{
		receiving_socket = new DatagramSocket3(PORT);
	} catch (SocketException e){
                System.out.println("ERROR: TextReceiver: Could not open UDP socket to receive from.");
		e.printStackTrace();
                System.exit(0);
	}
        //***************************************************
        
        //***************************************************
        //Main loop.
        
        boolean running = true;
        int i = 0;
        String[] testArray = new String[100];
        while (running){
         
            try{
                if(i == 99)
                    running = false;
                
                
                
                //Receive a DatagramPacket (note that the string cant be more than 80 chars)
                byte[] buffer = new byte[80];
                DatagramPacket packet = new DatagramPacket(buffer, 0, 80);
                
                //receiving_socket.setSoTimeout(500);
                
                receiving_socket.receive(packet);
                
                //Get a string from the byte buffer
                String str = new String(buffer);
                
                testArray[i] = str;
                
                //Display it
                System.out.print((i+1) + ": " + str);
                
                i++;
                
            } catch (SocketTimeoutException e){
                System.out.println(".");
            } catch (IOException e){
                System.out.println("ERROR: TextReceiver: Some random IO error occured!");
                e.printStackTrace();
            }
        }
        //Close the socket
        receiving_socket.close();
        //***************************************************
    }
}
