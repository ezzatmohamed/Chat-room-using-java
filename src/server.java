/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class server implements Runnable 
{
    private final Socket clientSocket;
            
    public server(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    
    }
    @Override
    public void run() 
    {
        System.out.println("fff\n");
        
        
        try 
        {
            
            OutputStream outputStream = clientSocket.getOutputStream();
            for ( int i =0; i < 10 ; i++)
            {
                outputStream.write(("Hello "+i+"\n").getBytes());
                Thread.sleep(1000);
            }            
            clientSocket.close();
        } 
        
        
        catch (IOException ex) 
        {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (InterruptedException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
