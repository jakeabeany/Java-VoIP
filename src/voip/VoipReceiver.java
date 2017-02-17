package voip;

import CMPC3M06.AudioPlayer;
import java.net.*;
import java.io.*;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;
import uk.ac.uea.cmp.voip.DatagramSocket4;

/**
 * 
 * @author Jake McVey & Matthew Williams
 */
public class VoipReceiver implements Runnable{
    
    static DatagramSocket receiving_socket;
    AudioPlayer player;
    
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
		receiving_socket = new DatagramSocket2(PORT);
	} catch (SocketException e){
                System.out.println("ERROR: TextReceiver: Could not open UDP socket to receive from.");
		e.printStackTrace();
                System.exit(0);
	}
        //***************************************************
        
        try{
            player = new AudioPlayer();
        } catch (Exception e){
            System.out.println("Error creating player object.");
        }
        
        //***************************************************
        //Main loop.
        boolean running = true;
        while (running){
            try{
                //Create a buffer to receive the packet
                byte[] buffer = new byte[512];
                DatagramPacket packet = new DatagramPacket(buffer, 0, 512);
                
                //Receive the packet
                receiving_socket.receive(packet);
                
                //Play the packet
                player.playBlock(buffer);
            } catch (SocketTimeoutException e){
                System.out.println(".");
            } catch (Exception e){
                System.out.println("ERROR: TextReceiver: Some random IO error occured!");
                e.printStackTrace();
            }
        }
        //Close the socket
        receiving_socket.close();
        //***************************************************
    }
}