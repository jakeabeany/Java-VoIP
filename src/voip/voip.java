package voip;

import java.net.DatagramSocket;

/**
 *
 * @author Jake McVey & Matty Williams
 */
public class voip {
    public static void main(String[] args){
        //set chosenDatagram
        DatagramSocket dg = null;
        
        dg = new DatagramFactory().setDatagram(DatagramFactory.Datagram.TWO, 55555);
        
        //instantiate receiver and sender objects
        VoipReceiver receiver = new VoipReceiver(dg);
        VoipSender sender = new VoipSender(dg);
        
        //run threaded classes
        receiver.start();
        sender.start();
        
    }
}
