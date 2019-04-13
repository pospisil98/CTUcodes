package cz.cvut.fel.pjv.schoolis.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LoginManager class.
 * 
 * @author Vojcek
 */
public class LoginManager {
    private Connection connection;
    
    /**
     *  Constructor of LoginManager.
     * 
     * @param con   instance of DB connection
     */
    public LoginManager(Connection con) {
        connection = con;
    }

    /**
     * Getter for connection.
     * 
     * @return database connection
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Get user login info from his username
     * 
     * @param username  username of user which data we want
     * @return  Map of data from database
     */
    public List<Map<String,String>> getLoginByUsername(String username) {
        List<Map<String,String>> data = new ArrayList<>();
        try {
            PreparedStatement q = connection.prepareStatement("SELECT * FROM login WHERE username = ?");
            q.setString(1, username);
            
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();
            
            // get list with names of columns
            List<String> columns = new ArrayList<>(md.getColumnCount());
            for(int i = 1; i <= md.getColumnCount(); i++){
                columns.add(md.getColumnName(i));
            }

            while(results.next() ) {
                Map<String,String> row = new HashMap<>(columns.size());
                for(String col : columns) {
                    System.out.println(results.getString(col));
                    row.put(col, results.getString(col));
                }
                data.add(row);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
