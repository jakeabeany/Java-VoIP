package voip;

/**
 *
 * @author Jake McVey & Matty Williams
 */
public class voip {
    public static void main(String[] args){
        //instantiate receiver and sender objects
        VoipReceiver receiver = new VoipReceiver();
        VoipSender sender = new VoipSender();
        
        //run threaded classes
        receiver.start();
        sender.start();
        
    }
}
