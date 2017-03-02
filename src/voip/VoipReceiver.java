package voip;

import CMPC3M06.AudioPlayer;
import java.net.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
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
    
    public VoipReceiver(DatagramSocket dg){
        receiving_socket = dg;
    }
    
    public void start(){
        Thread thread = new Thread(this);
	thread.start();
    }
    
    public void run (){
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
            
            //datagram4();
        }
        //Close the socket
        receiving_socket.close();
        //***************************************************
    }
    
    public void datagram1(){
        //System.out.println("Receiving on datagram1");
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
    int rp = 1;
    public void datagram2(){
        //System.out.println("Receiving on datagram2");
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
            
            //playList becomess true if packets arrived out of order
            //repeatTimes is the difference in order between the current packet and the last one received
            if(currentPacketNumber != lastPacketReceived+1 && currentPacketNumber != 0){
                playLast = true;
                repeatTimes = (currentPacketNumber - lastPacketReceived);
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
        //System.out.println("Receiving on datagram3");
        try{
            ArrayList<byte[]> packetList = new ArrayList<byte[]>();
            ByteBuffer tempBuf;
            
            //receive 5 packets at a time and add them to alist
            for(int looper = 0; looper < 5; looper++){
                //Create a buffer to receive the packet
                byte[] buffer = new byte[516];
                tempBuf = ByteBuffer.wrap(buffer);
                
                //create and empty packet to receive into
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                //Receive the packet
                receiving_socket.receive(packet);
                
                System.out.println("Received Packets: " + rp);
                rp++;
                //add packet to arraylist and sort it using comparator
                packetList.add(buffer);
            }
            
            //sort the bursts of 5 packets
            Collections.sort(packetList, sortByPacketNumber());
            
            //play packets
            for(byte[] b : packetList){
                int repeatTimes = 1;
                boolean playLast = false;
                
                ByteBuffer bufferForRepeating = ByteBuffer.wrap(b);
                currentPacketNumber = bufferForRepeating.getInt(0);
                
                
                //ignore packet if it is still out of position
                if(currentPacketNumber >= lastPacketReceived){                    
                    if(currentPacketNumber != (lastPacketReceived+1)){
                        playLast = true;
                        repeatTimes = (currentPacketNumber - lastPacketReceived)-1;
                    }
                    
                    //only create a new buffer to play if no repeating is needed
                    if(!playLast || currentPacketNumber == 0){
                        bufferToPlay = new byte[512];
                        bufferToPlay = Arrays.copyOfRange(b,4,b.length);
                    }

                    //play the packet however many times we need to
                    for(int i = 0; i < repeatTimes; i++){
                        player.playBlock(bufferToPlay);
                    }
                    lastPacketReceived = bufferForRepeating.getInt(0);
                }
            }
        } catch (Exception e){
            System.out.println("ERROR: VoipReceiver IO error occurred.");
            e.printStackTrace();
        }
    }
    
    public void datagram4(){
        int repeatTimes = 1;
        try{
            Checksum checksum = new CRC32();
            
            //Create a buffer to receive the packet
            byte[] buffer = new byte[524];
            ByteBuffer tempBuf = ByteBuffer.wrap(buffer);
            
            //create and empty packet to receive into
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            
            //Receive the packet
            receiving_socket.receive(packet);
            
            //Get the CRC code calculated before sending
            long receivedLong = tempBuf.getLong(0);
            
            //Get the packet number of the packet
            currentPacketNumber = tempBuf.getInt(8);
            
            
            //Get the CRC code from the current packet
            checksum.update(buffer, 12, buffer.length-12);
            long checkSumVal = checksum.getValue();
            
            //System.out.print("CP: " + currentPacketNumber + " LP : " + lastPacketReceived + " ----> " + (receivedLong == checkSumVal));
            
            //packet sent is the one received, add packet to bufferToPlay
            if(receivedLong == checkSumVal){
                bufferToPlay = new byte[512];
                bufferToPlay = Arrays.copyOfRange(buffer,12,buffer.length);
            }
            
            player.playBlock(bufferToPlay);
            
            
            
            lastPacketReceived = tempBuf.getInt(8);
        } catch (Exception e){
            System.out.println("ERROR: VoipReceiver IO error occurred.");
            e.printStackTrace();
        }
    }
    
    /**
     * if the first 4 bytes of the element are integers represented
     * as bytes, compares them.
     * 
     */
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