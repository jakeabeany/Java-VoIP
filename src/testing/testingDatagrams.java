package testing;

/**
 *
 * @author Jake McVey & Matty Williams
 */
public class testingDatagrams {
    
    public static void main (String[] args){
        TextReceiverThread receiver = new TextReceiverThread();
        TextSenderThread sender = new TextSenderThread();

        receiver.start();
        sender.start(); 
    }
}
