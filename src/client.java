
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
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
            
        //   login(name,password);
        }
        catch(Exception e)
        {
            System.out.println("Error connection !");
        }
        
        
    }
    
    
    public boolean login(String username,String password) throws IOException 
    {
        String cmd = username + "\n";
        serverOut.write(cmd.getBytes());
        
        cmd = password +"\n";
        serverOut.write(cmd.getBytes());
        
        String response = buffer.readLine();
        System.out.println("Response : "+response);
        
        if("Invalid".equalsIgnoreCase(response))
        {
            return false;
        }
        
        StartApp();
        return true;
        
    }    
    
    public boolean signup(String username,String password) throws IOException
    {
        String cmd = username +"\n";
        serverOut.write(cmd.getBytes());
        
        cmd = password +"\n";
        serverOut.write(cmd.getBytes());
        
        String response = buffer.readLine();
        System.out.println("Response : "+response);
        
        if("Invalid".equalsIgnoreCase(response))
        {
            return false;
        }
        return true;
    
    }
    public void logout() throws IOException
    {
      serverOut.write("#logout".getBytes());
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
    
