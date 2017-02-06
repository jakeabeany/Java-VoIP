package networks_coursework1;

/**
 *
 * @author  Matthew Williams & Jake McVey
 */
public class TextDuplex {
    
    public static void main (String[] args){
        
        TextReceiverThread receiver = new TextReceiverThread();
        TextSenderThread sender = new TextSenderThread();
        
        receiver.start();
        sender.start();  
    }
}
