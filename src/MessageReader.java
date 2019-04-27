
import java.io.BufferedReader;
import java.io.IOException;
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
public class MessageReader implements Runnable 
{

    private final BufferedReader buffer;
    
    public MessageReader(BufferedReader buffer)
    {
        this.buffer = buffer;
    }

    @Override
    public void run() 
    {
        
        try 
        {
            while(true)
            {
                String response = buffer.readLine();
                System.out.println(response);
            }
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(MessageReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
