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

public class user  implements Runnable {
    
    private server s;
    private Socket clientSocket;
    private final BufferedReader reader;
    private final OutputStream outputStream;
    
    private String username;
    
    private states state;
    
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
        System.out.println("user.login()\n");
        
        
        //outputStream.write("Username : ".getBytes());
        username = reader.readLine();
           
        outputStream.write(("You are logged now as " + username+ "\n").getBytes());
        
        System.out.println("you are now logged as " + username);
        
        /*for (user u : s.GetUserList() ) 
        {
            if( u != this)
                u.outputStream.write(("A new user logged as " + username+ "\n").getBytes());
        }*/
        
        state = states.MENU;
        //  outputStream.write("\n\n1)Public\n2)Online Users\n3)Logout\n\n".getBytes());
                   
    }
    public void logout() throws IOException
    {
        System.out.println("user.logout()");
        
        s.GetUserList().remove(this);
        clientSocket.close();
        
    }
    
    public String GetUsername()
    {
        System.out.println("user.GetUsername()");
        
        return username;
    }
    
    
    @Override
    public void run()
    {
        System.out.println("user.run()");
        try
        {
           String line;
           
           while(true)
           {
               
               if( state == states.LOGIN)
               {
                   System.out.println("logging");
                  
                   login();
               }
               else if( state == states.MENU)
               {
                   state = states.PUBLIC;
                   
                   /*line = reader.readLine();
                   
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
                   
                   }*/
               }
               else if ( state == states.PUBLIC)
               {
                   System.out.println("Public Chat");
                   
                   line = reader.readLine();
                   
                   System.out.println(line);
                   System.err.println("input");
                   
                   if("#back".equalsIgnoreCase(line))
                   {
                       state = states.MENU;
                       outputStream.write("\n1)Public\n2)Online Users\n3)Logout\n\n".getBytes());
                       
                   }
                   else
                   {
                       for ( user u : s.GetUserList())
                       {
                           u.outputStream.write((this.username+" : "+line+"\n").getBytes());
                           
                       }
                   }
               }
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
