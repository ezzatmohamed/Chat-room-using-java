/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author root
 */
public class ChatUsingJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        int port = 1002;
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true)
            {
                Socket clientSocket = serverSocket.accept();
                Thread t = new Thread(new server(clientSocket));
                t.start();
            }   
            
        }
        catch(Exception e)
        {
            System.out.println("fuck");
        }
    }
    
}
