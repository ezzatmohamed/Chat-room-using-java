/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static java.lang.System.in;
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
        try 
        {
           OutputStream outputStream = clientSocket.getOutputStream();
           InputStream inputStream = clientSocket.getInputStream();

           BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
           
           String line;
           
           while(true)
           {
               line = reader.readLine();
               
               if( line != null)
               {
                   if("quit".equalsIgnoreCase(line))
                   {
                       break;
                   }
                   else
                   {
                       
                   }
               
               }
           
           }
           
            clientSocket.close();
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    
}
