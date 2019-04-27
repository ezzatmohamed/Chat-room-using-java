
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
public class MessageSender implements Runnable
{
    
     
    private OutputStream serverOut;
    
    public MessageSender(OutputStream serverOut)
    {
        this.serverOut = serverOut;
    }

    @Override
    public void run() 
    {
        
        try 
        {
            
            Scanner scan = new Scanner(System.in);
            
            while(true)
            {
                String cmd = scan.nextLine() + "\n";
               // System.out.println("MessageSender.run()" + cmd);
                serverOut.write(cmd.getBytes());
            }
        } catch (IOException ex) 
        {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
