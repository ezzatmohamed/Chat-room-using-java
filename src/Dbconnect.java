/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.PrintWriter;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author root
 */
public class Dbconnect {
    
    
    private Connection con;
    private Statement st;
    private ResultSet rs;
    
    
    public Dbconnect()
    {
    
        try
        {
        
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat?","root","");
            st = con.createStatement();
        //    JOptionPane.showMessageDialog(null,"Connected");
            
        }
        catch(Exception e)
        {
            System.out.println("Error  "+e);
        }
        
       
    }
    
    public boolean getUser(String username,String password)
    {
    
        try 
        {
            String res = "";
        
            String query = "select * from users where username =\""+username+"\" and password =\""+password+"\"";
            rs = st.executeQuery(query);
            while(rs.next())
            {
       
                return true;
            }            
            
        }
        catch(Exception e)
        {
             System.err.println("error connection ");
        }
        
        return false;
    }
    public void InsertUser(String username,String password)
    {
        try
        {
        
            String query = "INSERT INTO users(username,password) VALUES(\""+username+"\",\""+password+"\")";
            st.executeUpdate(query);
          
        }
        catch(Exception e)
        {
            System.out.println("Error  "+e);
        }
    }

}
