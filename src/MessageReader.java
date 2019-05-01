    
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
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
public class MessageReader implements Runnable 
{

    private final BufferedReader buffer;
    private gui GUI;
    public MessageReader(BufferedReader buffer) throws IOException
    {
        this.buffer = buffer;
        this.GUI =gui.GetInstance();
    }

    @Override
    public void run() 
    {
        
        try 
        {
            while(true)
            {
                String response = buffer.readLine();
                
                if("#GroupChat".equalsIgnoreCase(response) )
                {
                    ArrayList<String> groupsList = new ArrayList<>();
                    while(!"#end".equalsIgnoreCase(response = buffer.readLine()))
                    {
                        groupsList.add(response);
                    }
                    
                    GUI.GroupList(groupsList);
                    continue;
                }
                else if("#OnlineUsers".equalsIgnoreCase(response))
                {
                    ArrayList<String> userList = new ArrayList<>();
                    while(!"#end".equalsIgnoreCase(response = buffer.readLine()))
                    {
                        userList.add(response);
                    }
                    
                    GUI.UserList(userList);
                    continue;
                }
                
                
                GUI.ShowMsg(response);
                
                System.out.println(response);
                
            }
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(MessageReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
