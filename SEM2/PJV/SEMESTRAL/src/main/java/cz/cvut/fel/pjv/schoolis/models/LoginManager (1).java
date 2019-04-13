/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.schoolis.models;

import cz.cvut.fel.pjv.schoolis.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Vojcek
 */
public class LoginManager {
    
    public LoginManager() {
    }
    
    public List<Map<String,String>> getUserByUsername(String username) {
        System.out.println("LM");
        List<Map<String,String>> data = new ArrayList<Map<String,String>>();
        
        try (Connection con = DriverManager.getConnection("jdbc:mysql://den1.mysql3.gear.host:3306/pjvdb?user=" + Config.DBUSER +
                "&password="+ Config.DBPASSWORD +"&" + Config.DBSETINGS)) {
            PreparedStatement q = con.prepareStatement("SELECT * FROM login WHERE username = ?");
            q.setString(1, username);
            
            ResultSet results = q.executeQuery();
            
            ResultSetMetaData md = results.getMetaData();
            System.out.println(md);
            List<String> columns = new ArrayList<String>(md.getColumnCount());
            for(int i = 1; i <= md.getColumnCount(); i++){
                columns.add(md.getColumnName(i));
            }
            System.out.println(md.getColumnCount());
            while(results.next() ) {
                Map<String,String> row = new HashMap<String, String>(columns.size());
                for(String col : columns) {
                    System.out.println(results.getString(col));
                    row.put(col, results.getString(col));
                }
                data.add(row);
            }
            System.out.println("Data: " + data);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return data;
    }
}
