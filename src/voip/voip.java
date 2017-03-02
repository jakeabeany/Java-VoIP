package voip;

import java.net.DatagramSocket;

/**
 *
 * @author Jake McVey & Matty Williams
 */
public class voip {
    public static void main(String[] args){
        //set chosenDatagram
        DatagramSocket dg;
        
        dg = new DatagramFactory().setDatagram(DatagramFactory.Datagram.THREE, 55555);
        
        //instantiate receiver and sender objects
        VoipReceiver receiver = new VoipReceiver(dg);
        VoipSender sender = new VoipSender(dg);
        
        //run threaded classes
        receiver.start();
        sender.start();
        
    }
    
    public void audio(DatagramSocket socket, int PORT){
        VoipReceiver receiver = new VoipReceiver(socket);
        VoipSender   sender   = new VoipSender(socket);
        
        receiver.start();
        sender.start();
    }
}
