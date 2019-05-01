/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
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
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


enum states
{ 
    HOME,LOGIN,SIGNUP, MENU, PUBLIC,ONLINE_LIST,CHAT_PRIVATE; 
} 

public class user implements Runnable {
    
    private server s; 
    
    // the socket that the user is conncted from.
    private Socket clientSocket;
    
    // needed objects for sending and recieveing.
    private final BufferedReader reader;
    private final OutputStream outputStream;
    
    // Every user has an username
    private String username;
    private String password;
   
    // State machine to determine if he's supposed to login or chat in public or ..etc.  
    private states state;
    
    public boolean pub = false;
    
    
    // needed initialization in the constructor
    public user(Socket clientSocket ,server s) throws IOException
    {
           // To get outputs and send inputs
           outputStream = clientSocket.getOutputStream();
           InputStream inputStream = clientSocket.getInputStream();
           reader = new BufferedReader(new InputStreamReader(inputStream));
           //--------------------------------------------------------
           
           this.s = s;
           this.clientSocket = clientSocket;
           
           state = states.HOME;
    }
        
    public void signup() throws IOException
    {
        while (true)
        {
            // waiting for entering a username from the client
            
            String cmd = reader.readLine();
            
            if("#back".equalsIgnoreCase(cmd))
            {
                state = states.HOME;
                System.out.println("returning to home !");        
                return;
            }
            
            username = cmd;
            password = reader.readLine();
      
            if ( !s.DB.InsertUser(username, password) )
            {
                outputStream.write(("Invalid"+"\n").getBytes());
                return;
            }
            else
                break;
        }
        
        outputStream.write(("You are registered now as " + username+ "\n").getBytes());
        
        state = states.HOME;
    }
    public void login() throws IOException
    {
        
        
        while (true)
        {
            // waiting for entering a username from the client
            
            String cmd = reader.readLine();
            
            if("#back".equalsIgnoreCase(cmd))
            {
                state = states.HOME;
                
                System.out.println("returning to home !");
                
                return;
            }
            
            username = cmd;
            password = reader.readLine();

            if ( !s.DB.getUser(username, password) )
            {
                outputStream.write(("Invalid"+"\n").getBytes());
                return;
            }
            else
                break;
        }
        
        outputStream.write(("You are logged now as " + username+ "\n").getBytes());
        
        System.out.println("a new user is online :  " + username + " , password => " + password);
        

        state = states.PUBLIC;
                   
    }
    
    public void logout() throws IOException
    {
        System.out.println("user.logout()");
    
        // When he logs out we remove him from the users list
        s.GetUserList().remove(this);
 //       clientSocket.close();
        
    }
    
    public String GetUsername()
    {
        return username;
    }
    
    
    @Override
    public void run()
    {
        try
        {
           String line;
           while(true)
           {
               
               if( state == states.HOME)
               {
                   String cmd = reader.readLine();
                   
                   if( cmd.equalsIgnoreCase("#signup"))
                   {
                       state =states.SIGNUP;
                   }
                   else if(cmd.equalsIgnoreCase("#login"))
                   {
                       state = states.LOGIN;
                   }               
               }
               else if( state == states.LOGIN)
               {
                   login();
               }
               else if( state == states.SIGNUP)
               {
                   signup();
               }
               else if( state == states.MENU)
               {
                   pub = false;
                   
                   String OnlineUsers = "";
                   
                   for( user u : s.GetUserList() )
                   {
                       if( u != this)
                           OnlineUsers +=u.GetUsername() + ':';
                   }
                   
                   outputStream.write(OnlineUsers.getBytes());
               
               }
               else if ( state == states.PUBLIC)
               {
               
                   // Recieve an input from user to chat with others
                   line = reader.readLine();
                   
                   if("#logout".equalsIgnoreCase(line))
                   {
                       logout();
                       state = states.HOME;
                   }
                   for ( user u : s.GetUserList() )
                   {
                       System.err.println("msg to all :  " + line);
                       u.outputStream.write((this.username+" : "+line+"\n").getBytes());
                           
                   }
               
               }
            
           
           }
        }
        catch(Exception e)
        {}
    }
    
}
