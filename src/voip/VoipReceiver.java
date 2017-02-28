package voip;

import CMPC3M06.AudioPlayer;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;
import uk.ac.uea.cmp.voip.DatagramSocket4;

/**
 * 
 * @author Jake McVey & Matthew Williams
 */
public class VoipReceiver implements Runnable{
    
    static DatagramSocket receiving_socket;
    Interleaver inter = new Interleaver();
    AudioPlayer player;
    int lastPacketReceived = 0, currentPacketNumber = 0;
    byte[] bufferToPlay = new byte[512];
    
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
            
            //datagram2();
            
            datagram3();
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
    
    public void datagram2(){
        boolean playLast = false;
        int repeatTimes = 1;
        try{
            //Create a buffer to receive the packet
            byte[] buffer = new byte[516];
            ByteBuffer tempBuf = ByteBuffer.wrap(buffer);
            
            //create and empty packet to receive into
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            
            //Receive the packet
            receiving_socket.receive(packet);
            
            //get the current packet number
            currentPacketNumber = tempBuf.getInt(0);
            
            inter.deInterleave(buffer);
            
            //playList becomess true if packets arrived out of order
            //repeatTimes is the difference in order between the current packet and the last one received
            if(currentPacketNumber != lastPacketReceived+1 && currentPacketNumber != 0){
                playLast = true;
                repeatTimes = currentPacketNumber - lastPacketReceived;
            }
            
            //if the packets are in order, create space for a new buffer and fill it
            if(!playLast || currentPacketNumber == 0){
                bufferToPlay = new byte[512];
                bufferToPlay = Arrays.copyOfRange(buffer,4,buffer.length);
            }
            
            //play the packet however many times we need to
            for(int i = 0; i < repeatTimes; i++){
                player.playBlock(bufferToPlay);
            }
            
            //get the last packet number
            lastPacketReceived = tempBuf.getInt(0);
        } catch (Exception e){
            System.out.println("ERROR: VoipReceiver IO error occurred.");
            e.printStackTrace();
        }
    }
    
    public void datagram3(){
        try{
            ArrayList<byte[]> packetList = new ArrayList<byte[]>();
            ByteBuffer tempBuf;
            
            //receive 4 packets at a time and add them to alist
            for(int looper = 0; looper < 4; looper++){
                //Create a buffer to receive the packet
                byte[] buffer = new byte[516];
                tempBuf = ByteBuffer.wrap(buffer);
                
                //create and empty packet to receive into
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                //Receive the packet
                receiving_socket.receive(packet);

                inter.deInterleave(buffer);
                
                //add packet to arraylist and sort it using comparator
                packetList.add(buffer);
            }
            
            //sort the bursts of 4 packets
            Collections.sort(packetList, sortByPacketNumber());
            
            
            for(byte[] b : packetList){
                ByteBuffer lmao = ByteBuffer.wrap(b);
                int lmaoo = lmao.getInt(0);
                System.out.println(lmaoo);
                
                bufferToPlay = new byte[512];
                bufferToPlay = Arrays.copyOfRange(b,4,516);
                
                player.playBlock(bufferToPlay);
            }
        } catch (Exception e){
            System.out.println("ERROR: VoipReceiver IO error occurred.");
            e.printStackTrace();
        }
    }
    
    public void datagram4(InetAddress clientIP, int PORT){
     
    }
    
    public static Comparator<byte[]> sortByPacketNumber(){
        Comparator comp = new Comparator<byte[]>() {
            @Override
            public int compare(byte[] p1, byte[] p2){
                int p1Num, p2Num;

                ByteBuffer tempP1 = ByteBuffer.wrap(p1);
                ByteBuffer tempP2 = ByteBuffer.wrap(p2);

                p1Num = tempP1.getInt(0);
                p2Num = tempP2.getInt(0);
                return p1Num - p2Num;
            }
        };
        return comp;
    }          
}