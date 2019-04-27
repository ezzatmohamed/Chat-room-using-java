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
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


enum states
{ 
    LOGIN, MENU, PUBLIC,ONLINE_LIST,CHAT_PRIVATE; 
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
    
   
    // State machine to determine if he's supposed to login or chat in public or ..etc.  
    private states state;
    
    
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
           
           state = states.LOGIN;
           
    }
        
    public void login() throws IOException
    {
        // waiting for entering a username from the client
        username = reader.readLine();
        
        
        outputStream.write(("You are logged now as " + username+ "\n").getBytes());
        
        System.out.println("a new user is online :  " + username);
        
        /*for (user u : s.GetUserList() ) 
        {
            if( u != this)
                u.outputStream.write(("A new user logged as " + username+ "\n").getBytes());
        }*/
        
        // now he's logged in. So he's redirected to Public Chat.
        state = states.PUBLIC;
                   
    }
    
    public void logout() throws IOException
    {
        System.out.println("user.logout()");
    
        // When he logs out we remove him from the users list
        s.GetUserList().remove(this);
        clientSocket.close();
        
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
               
               if( state == states.LOGIN)
               {
                   login();
               }
              
               else if ( state == states.PUBLIC)
               {
                   
                   // Recieve an input from user to chat with others
                   line = reader.readLine();
     
                   
                   if("#back".equalsIgnoreCase(line))
                   {
                       state = states.MENU;
                       outputStream.write("\n1)Public\n2)Online Users\n3)Logout\n\n".getBytes());
                       
                   }
                   else
                   {
                       // when the user write an input, we want to send it to every user in the user list
                       for ( user u : s.GetUserList())
                       {
                           u.outputStream.write((this.username+" : "+line+"\n").getBytes());
                           
                       }
                   }
               }
               /*
               else if( state == states.MENU)
               {
                   state = states.PUBLIC;
                   
                   line = reader.readLine();
                   
                   if("public".equalsIgnoreCase(line))
                   {
                       state = states.PUBLIC;
                       outputStream.write("\n\nWrite message to everyone !\n\n ".getBytes());
                 
                   }
                   
                   else if("online users".equalsIgnoreCase(line))
                   {
                       for (user u : s.GetUserList()) 
                       {
                           outputStream.write((u.GetUsername()+" is online\n\n").getBytes());
                       }
                       
                       state = states.ONLINE_LIST;
                   }
                   
                   else if("logout".equalsIgnoreCase(line))
                   {
                       state = states.LOGIN;
                      // outputStream.write("1)Public\n2)Online Users\n3)Logout\n\n".getBytes());
                       
                   }
                   else
                   {
                       outputStream.write("invalid input ! \n\n".getBytes());
                   
                   }
               }
               */
               /*
               else if( state == states.ONLINE_LIST)
               {
                   line = reader.readLine();
                   
                   if("#back".equalsIgnoreCase(line))
                   {
                       state = states.MENU;
                       outputStream.write("\n1)Public\n2)Online Users\n3)Logout".getBytes());
                  }
               }*/
           
           }
          // clientSocket.close();
        }
        catch(Exception e)
        {}
    }
    
}
