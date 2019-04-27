
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
public class client {

    private String servername;
    private int port;
    private Socket socket;
    private OutputStream serverOut;
    private InputStream serverIN;
    private BufferedReader buffer;

    public client(String servername,int port)
    {
        this.servername = servername;
        this.port = port;
        
    }
    
    
    public static void main(String[] args) throws IOException
    {
        // first thing we create a new client
        client c = new client("localhost",1002);
        c.connect();
       
    }

    public void connect() throws IOException 
    {
        try
        {
            // connect the client to the server 
            socket = new Socket(servername,port);
            
            // "ServerOut" for sending to the server 
            // "buffer" for recieveing from the server
            
            this.serverOut = socket.getOutputStream();
            this.serverIN = socket.getInputStream();
            this.buffer = new BufferedReader(new InputStreamReader(serverIN));
            
            
            System.out.println("client.connect() success");
            
            
            
            // Log in
            Scanner scan = new Scanner(System.in);
            String name = scan.nextLine();
            login(name);
            
            // now i am a user. i should be able to read and write messages
            
            StartApp();
            
            while(true){}
            
        }
        catch(Exception e)
        {
            System.out.println("Error connection !");
        }
        
        
    }
    
    
    private void login(String username) throws IOException 
    {
        String cmd = username + "\n";
        
        serverOut.write(cmd.getBytes());
        
        String response = buffer.readLine();
        System.out.println("Response : "+response);
        
    }    

    private void StartApp() throws IOException 
    {
            // I should be able to read and write messages concurrently.
            // So we make a thread for reading and another for writing.
        
        
            // A thread to send message to others
            Thread MsgSend = new Thread( new MessageSender(serverOut) );
            MsgSend.start();
            //=============================================
            
            
            // A thread to read messages from other users
            Thread MsgReader = new Thread( new MessageReader(buffer) );
            MsgReader.start();
            //=============================================
         
    }
    
}
