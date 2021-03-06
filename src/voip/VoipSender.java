package voip;

import CMPC3M06.AudioRecorder;
import java.net.*;
import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.nio.ByteBuffer;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;
import uk.ac.uea.cmp.voip.DatagramSocket4;

/**
 * 
 * @author Jake McVey & Matthew Williams
 */
public class VoipSender implements Runnable{
    static DatagramSocket sending_socket;
    Interleaver inter = new Interleaver();
    AudioRecorder recorder;
    int packetNumber = 0;
    
    public VoipSender(DatagramSocket dg){
        sending_socket = dg;
    }
    
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
            
            //datagram2(clientIP, PORT);
            
            datagram3(clientIP, PORT);
            
            //datagram4(clientIP, PORT);
        }
        //Close the socket
        sending_socket.close();
    }
    
    public void datagram1(InetAddress clientIP, int PORT){
        //System.out.println("Sending on datagram1");
        try{ 
            byte[] temp = new byte[1536];
            
            ByteBuffer tempBuf = ByteBuffer.wrap(temp);
            
            // send 3 packets at once
            for(int i = 0; i < 3; i++){
                //Create the block to send
                byte[] block = recorder.getBlock();
                
                //put the packet into the temp buffer
                tempBuf.put(block);
            }
            
            //Make a DatagramPacket from it, with client address and port number
            DatagramPacket packet = new DatagramPacket(temp, temp.length, 
                    clientIP, PORT);
            
            //Send it
            sending_socket.send(packet);
        } catch (Exception e){
            System.out.println("ERROR: Void Sender: Some random IO error occured!");
            e.printStackTrace();
        }
    }
    
    public void datagram2(InetAddress clientIP, int PORT){
        //System.out.println("Sending on datagram2");
        try{       
            //Create the block to contain the data to transfer
            byte[] block = recorder.getBlock();
            
            //create a temp block with 4 extra bits for the packet number
            byte[] temp = new byte[block.length + 4];
            ByteBuffer tempBuf = ByteBuffer.wrap(temp);
            
            //put header int on the packet to transfer
            tempBuf.putInt(packetNumber);
            
            //add data block to transfer packet
            tempBuf.put(block);
            
            //Make a DatagramPacket
            DatagramPacket packet = new DatagramPacket(temp, temp.length, clientIP, PORT);

            //Send it
            sending_socket.send(packet);
            
            packetNumber++;
        } catch (Exception e){
            System.out.println("ERROR: Void Sender: Some random IO error occured!");
            e.printStackTrace();
        }
    }
    
    public void datagram3(InetAddress clientIP, int PORT){
        //System.out.println("Sending on datagram3");
        try{
            //Create the block to contain the data to transfer
            byte[] block = recorder.getBlock();
            
            //create a temp block with 4 extra bits for the packet number
            byte[] temp = new byte[block.length + 4];
            ByteBuffer tempBuf = ByteBuffer.wrap(temp);
            
            //put header int on the packet to transfer
            tempBuf.putInt(packetNumber);
            
            //add data block to transfer packet
            tempBuf.put(block);
            
            //Make a DatagramPacket
            DatagramPacket packet = new DatagramPacket(temp, temp.length, clientIP, PORT);

            //Send it
            sending_socket.send(packet);
            
            packetNumber++;
            System.out.println("Sent Packets: " + (packetNumber+1));
        } catch (Exception e){
            System.out.println("ERROR: Void Sender: Some random IO error occured!");
            e.printStackTrace();
        }
    }
    
    public void datagram4(InetAddress clientIP, int PORT){
        //System.out.println("Sending on datagram4");
        try{
            Checksum checksum = new CRC32();
            
            //Create the block to contain the data to transfer
            byte[] block = recorder.getBlock();
            
            //create a temp block with 8 extra bits for CRC to send
            byte[] temp = new byte[block.length + 12];
            ByteBuffer tempBuf = ByteBuffer.wrap(temp);
            
            
            checksum.update(block, 0, block.length);
            
            long checkSumVal = checksum.getValue();
            
            //put header int on the packet to transfer
            tempBuf.putLong(checkSumVal);
            
            //put packet number into the packet
            tempBuf.putInt(packetNumber);
            
            //add data block to transfer packet
            tempBuf.put(block);
             
            //Make a DatagramPacket
            DatagramPacket packet = new DatagramPacket(temp, temp.length, clientIP, PORT);

            //Send it
            sending_socket.send(packet);
            
            packetNumber++;
        } catch (Exception e){
            System.out.println("ERROR: Void Sender: Some random IO error occured!");
            e.printStackTrace();
        }
    }
} 

