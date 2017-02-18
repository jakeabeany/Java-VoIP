package voip;

import CMPC3M06.AudioPlayer;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
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
    int lastPacketReceived = 0, currentPacketNumber = 0;
    byte[] lastPacket = new byte[512];
    
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
                System.out.println("ERROR: VoipReceiver: Could not open UDP socket to receive from.");
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
            //datagram1();
            
            datagram2(lastPacket);
        }
        //Close the socket
        receiving_socket.close();
        //***************************************************
    }
    
    public void datagram1(){
        try{
            //Create a buffer to receive the packet
            byte[] buffer = new byte[1536];
            DatagramPacket packet = new DatagramPacket(buffer, 0, 1536);

            //Receive the packet
            receiving_socket.receive(packet);


            //Play the packet
            player.playBlock(buffer);
            
        } catch (Exception e){
            System.out.println("ERROR: VoipReceiver IO error occurred.");
            e.printStackTrace();
        }
    }
    
    public void datagram2(byte[] lPacket){
        boolean playLast = false;
        try{
            //Create a buffer to receive the packet
            byte[] buffer = new byte[516];
            ByteBuffer tempBuf = ByteBuffer.wrap(buffer);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            
            //Receive the packet
            receiving_socket.receive(packet);
            
            //get the current packet number
            currentPacketNumber = tempBuf.getInt(0);
            
            if(currentPacketNumber != lastPacketReceived+1 && currentPacketNumber != 0){
                playLast = true;
                //System.out.println(currentPacketNumber + "here");
            }
            
            //populate the array that will be played
            byte[] bufferToPlay = Arrays.copyOfRange(buffer,4,buffer.length);
            
            if(playLast)
                //Play the packet
                player.playBlock(lastPacket);
            else
                player.playBlock(bufferToPlay);
            
            //get last packet
            lastPacket = Arrays.copyOfRange(buffer, 4, buffer.length);
            
            lastPacketReceived = tempBuf.getInt(0);
        } catch (Exception e){
            System.out.println("ERROR: VoipReceiver IO error occurred.");
            e.printStackTrace();
        }
    }
}