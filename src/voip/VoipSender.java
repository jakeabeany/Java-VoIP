package voip;

import CMPC3M06.AudioRecorder;
import java.net.*;
import java.io.*;
import java.util.Vector;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;
import uk.ac.uea.cmp.voip.DatagramSocket4;

/**
 * 
 * @author Jake McVey & Matthew Williams
 */
public class VoipSender implements Runnable{
    static DatagramSocket sending_socket;
    AudioRecorder recorder;
    
    public void start(){
        Thread thread = new Thread(this);
	thread.start();
    }
    
    public void run (){
        //Port to send to
        int PORT = 55555;
        //IP ADDRESS to send to
        InetAddress clientIP = null;
	try {
		clientIP = InetAddress.getByName("localhost");  //CHANGE localhost to IP or NAME of client machine
	} catch (UnknownHostException e) {
                System.out.println("ERROR: VoipSender: Could not find client IP");
		e.printStackTrace();
                System.exit(0);
	}
        
        //***************************************************
        //Open a socket to send from
        //We dont need to know its port number as we never send anything to it.
        //We need the try and catch block to make sure no errors occur.
        try{
		sending_socket = new DatagramSocket2();
	} catch (SocketException e){
                System.out.println("ERROR: VoipSender: Could not open UDP socket to send from.");
		e.printStackTrace();
                System.exit(0);
	}
        //***************************************************
     
        //Main loop.
        
        boolean running = true;
        
        try{
            //Initialise AudioRecorder objects
            recorder = new AudioRecorder();
        } catch (Exception e){
            System.out.println("Error instantiating the audio recorder.");
        }
        
        while (running){
            //datagram1(clientIP, PORT);
            
            datagram2(clientIP, PORT);
        }
        //Close the socket
        sending_socket.close();
    }
    
    public void datagram1(InetAddress clientIP, int PORT){
        try{
            //Create the block to send
            byte[] block = recorder.getBlock();

            //Make a DatagramPacket from it, with client address and port number
            DatagramPacket packet = new DatagramPacket(block, block.length, clientIP, PORT);

            //Send it
            sending_socket.send(packet);
        } catch (Exception e){
            System.out.println("ERROR: Void Sender: Some random IO error occured!");
            e.printStackTrace();
        }
    }
    
    public void datagram2(InetAddress clientIP, int PORT){
        try{
            //Create the block to send
            byte[] block = recorder.getBlock();

            //Make a DatagramPacket from it, with client address and port number
            DatagramPacket packet = new DatagramPacket(block, block.length, clientIP, PORT);

            //Send it
            sending_socket.send(packet);
        } catch (Exception e){
            System.out.println("ERROR: Void Sender: Some random IO error occured!");
            e.printStackTrace();
        }
    }
} 

