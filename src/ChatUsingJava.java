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
        Thread t1 = new Thread(new server(port));
        t1.start();
  
    }
    
}
