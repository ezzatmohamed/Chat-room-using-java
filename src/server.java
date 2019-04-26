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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class server implements Runnable 
{
    private final int port;
    private ArrayList<user> usersList = new ArrayList<>();
    
    public server(int port)
    {
        this.port = port;
        
    }
    @Override
    public void run() 
    {
        
        try 
        {
            
           ServerSocket serverSocket = new ServerSocket(port);
           while(true)
           {
                
                // When a new user connects to the server
                //, we add him to the list so we can keep track of him.
                Socket clientSocket = serverSocket.accept();   
                user User = new user(clientSocket, this);
                usersList.add(User);
                
                
                Thread t = new Thread(User);
                t.start();
           }
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
