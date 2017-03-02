package voip;

import java.net.DatagramSocket;
import uk.ac.uea.cmp.voip.DatagramSocket2;
import uk.ac.uea.cmp.voip.DatagramSocket3;
import uk.ac.uea.cmp.voip.DatagramSocket4;

/**
 * 
 * @author Jake McVey & Matthew Williams
 */
public class DatagramFactory {
    /**
     * the possible datagrams to be called
     */
    public enum Datagram{
        ONE,
        TWO,
        THREE,
        FOUR
    }
    
    /**
     * return an instance of the chosen datagram
     * @param dgram enum of the datagram to be called
     * @param port the port to be called
     * @return an instance of that datagram
     */
    public DatagramSocket setDatagram(Datagram dgram, int port){
        DatagramSocket returnSocket = null;
        switch(dgram){
            case ONE:
                try{
                    returnSocket = new DatagramSocket(port);
                }catch(Exception e){
                    System.out.println("Problem creating datagram!");
                }
                break;
            case TWO:
                try{
                    returnSocket = new DatagramSocket2(port);
                }catch(Exception e){
                    System.out.println("Problem creating datagram!");
                }
                break;
            case THREE:
                try{
                    returnSocket = new DatagramSocket3(port);
                }catch(Exception e){
                    System.out.println("Problem creating datagram!");
                }
                break; 
            case FOUR:
                try{
                    returnSocket = new DatagramSocket4(port);
                }catch(Exception e){
                    System.out.println("Problem creating datagram!");
                }
                break;
            default://default to datagram 1
                try{
                    returnSocket = new DatagramSocket(port);
                }catch(Exception e){
                    System.out.println("Problem creating datagram!");
                }
                break;
        }
        return returnSocket;      
    }
}