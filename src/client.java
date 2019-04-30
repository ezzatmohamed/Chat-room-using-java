
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
    
   // private gui GUI;

    public client(String servername,int port)
    {
        this.servername = servername;
        this.port = port;
     //   GUI = new gui();
      //  GUI.setVisible(true);
        
    }
    
    
    public static void main(String[] args) throws IOException
    {
        // first thing we create a new client
  //      client c = new client("localhost",1002);
  //      c.connect();
       
    }

    public void connect(String name) throws IOException 
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
            
            login(name);
            StartApp();
        }
        catch(Exception e)
        {
            System.out.println("Error connection !");
        }
        
        
    }
    
    
    public void login(String username) throws IOException 
    {
        String cmd = username + "\n";
        
        serverOut.write(cmd.getBytes());
        
        String response = buffer.readLine();
        System.out.println("Response : "+response);
        
    }    

    public void StartApp() throws IOException 
    {

            // A thread to read messages from other users
            Thread MsgReader = new Thread( new MessageReader(buffer) );
            MsgReader.start();
            //=============================================
            
    }
    public void SendMsg(String Msg) throws IOException
    {
            String cmd = Msg + "\n";
               // System.out.println("MessageSender.run()" + cmd);
            serverOut.write(cmd.getBytes());
    }
    
}
    
