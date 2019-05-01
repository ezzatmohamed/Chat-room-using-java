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
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


enum states
{ 
    HOME,LOGIN,SIGNUP, MENU, PRIVATE,PUBLIC,GROUP,ONLINE_LIST,CHAT_PRIVATE; 
} 

public class user implements Runnable {
    
    private server s; 
    
    // the socket that the user is conncted from.
    private Socket clientSocket;
    
    // needed objects for sending and recieveing.
    private final BufferedReader reader;
    private final OutputStream outputStream;
    
    private ArrayList<String> chatList = new ArrayList<>();
    
    
    // Every user has an username
    private String username;
    private String password;
    private String group = null;
    private String privateName = null ;
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
       
    public String getGroup(){ return group;}
    public states getState(){ return state;}
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
        

        state = states.MENU;
                   
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
    
    public void AddToChatList(String name)
    {
        
        for ( String u : chatList)
        {
            if( u.equals(name))
            {
               return;
            }
        }
        chatList.add(name);

        for( user u : s.GetUserList())
        {
            if( u.GetUsername().equals(name))
            {
                u.AddToChatList(this.GetUsername());
            
            }
        
        }
    }
    
    public ArrayList<String> GetChatList(){return chatList;}
    
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
                   // Sending Groups names to the user
                   outputStream.write("#GroupChat\n".getBytes());
                   
                   for( String group : s.DB.getGroup() )
                   {
                       outputStream.write((group+'\n').getBytes());
                       
                   }
                   outputStream.write("#end\n".getBytes());
                   
                   //========================================
                   
                   //Sending Online users names to the client
                   outputStream.write("#OnlineUsers\n".getBytes());
                   
                   for(user u : s.GetUserList() )
                   {
                       if( u.GetUsername() != null && u != this )
                           outputStream.write((u.GetUsername()+'\n').getBytes());
                       
                   }
                   outputStream.write("#end\n".getBytes());
                   
                   //=========================================
                   
                   
                   // Waiting for an action from the user
                   line = reader.readLine();
                   
                   if("#logout".equalsIgnoreCase(line))
                   {
                       logout();
                       state = states.HOME;
                   }
                   
                   else if("#CreateGroup".equalsIgnoreCase(line))
                   {
                       String GroupName = reader.readLine();
                       s.DB.InsertGroup(GroupName);
                   }
                   
                   else if("#PrivateChat".equalsIgnoreCase(line))
                   {
                       state = states.PRIVATE;
                       
                       line = reader.readLine();
                       
                       AddToChatList(line);
                       privateName = line;
                   }
                   else
                   {
                       state= states.GROUP;
                       group = line;
                   }
               
               }
               else if ( state == states.PRIVATE)
               {
                   line = reader.readLine();
                   
                   if("#back".equalsIgnoreCase(line))
                   {
                       state = states.MENU;
                       continue;
                   }
                   
                   for ( user u : s.GetUserList() )
                   {
                       if( u.getState() == states.PRIVATE && u.GetUsername().equals(privateName)  )
                       {
                           u.outputStream.write((this.username+" : "+line+"\n").getBytes()); 
                           //System.out.println(u.GetUsername()+ " || " + u.getGroup()+ " || " + u.getState());
                   
                       }
                       else
                       {
                           //System.err.println(u.GetUsername() + " || " + u.getGroup() + " || " + u.getState());
                       }
                   }
               }
               else if ( state == states.GROUP)
               {
                   // Recieve an input from user to chat with others
                   line = reader.readLine();
                   
                   if("#back".equalsIgnoreCase(line))
                   {
                       state = states.MENU;
                       continue;
                   }
                   for ( user u : s.GetUserList() )
                   {
                       if( u.getState() == states.GROUP && group.equals(u.getGroup())  )
                       {
                           u.outputStream.write((this.username+" : "+line+"\n").getBytes()); 
                            System.out.println(u.GetUsername()+ " || " + u.getGroup()+ " || " + u.getState());
                   
                       }
                       else
                       {
                           System.err.println(u.GetUsername() + " || " + u.getGroup() + " || " + u.getState());
                       }
                   }
               }
           }
        }
        catch(Exception e)
        {}
    }
    
}
