package testing;
import java.util.Scanner;
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
