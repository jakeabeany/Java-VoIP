/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package real_cw;

/**
 *
 * @author Jake McVey & Matty Williams
 */
public class voip {
    public static void main(String[] args){
        VoipReceiver receiver = new VoipReceiver();
        VoipSender sender = new VoipSender();
        
        receiver.start();
        sender.start();
    }
}
