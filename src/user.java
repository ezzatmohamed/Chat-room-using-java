/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
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


public class user implements Runnable {
    
    private server s;
    private Socket clientSocket;
    private BufferedReader reader;
    private OutputStream outputStream;
    
    
    public user(Socket clientSocket ,server s) throws IOException
    {
           // To get outputs and send inputs
           outputStream = clientSocket.getOutputStream();
           InputStream inputStream = clientSocket.getInputStream();
           reader = new BufferedReader(new InputStreamReader(inputStream));
           //--------------------------------------------------------
           
    }

    @Override
    public void run()
    {
        try
        {
  
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
        catch(Exception e)
        {}
    }
    
}
