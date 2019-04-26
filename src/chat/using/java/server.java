/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.using.java;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author root
 */
public class server {
    
    public static void main(String[] args)
    {
        int port = 1002;
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true)
            {
                Socket clientSocket = serverSocket.accept();
                OutputStream outputStream = clientSocket.getOutputStream();
                outputStream.write("Hello\n".getBytes());
                clientSocket.close();
                
            }   
            
        }
        catch(Exception e)
        {
            System.out.println("fuck");
        }
        
    }
    
}
