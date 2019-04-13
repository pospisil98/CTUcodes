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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Karel
 */
public class ParentManager extends UserManager {
    private String ID_parent;
    private String ID_student;

    /**
     * Parent Manager basic constructor.
     * 
     * Used in other managers to recieve some basic parent oriented info.
     * 
     * @param connection
     */
    public ParentManager(Connection connection) {
        this.con = connection;
    }
    
    /**
     * ParentManager advanced constructor.
     * 
     * Used in parent menu and so on.
     * 
     * @param ID_login
     * @param connection
     */
    public ParentManager(String ID_login, Connection connection) {
        this.con = connection;
        try {
            PreparedStatement q = con.prepareStatement("SELECT `ID_parent` FROM parents WHERE `ID_login` = ? LIMIT 1");
            q.setString(1, ID_login);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                this.ID_parent = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getID_student() {
        return ID_student;
    }
    
    public void setID_parent(String ID_parent) {
        this.ID_parent = ID_parent;
    }
    
    public Connection getConnection() {
        return con;
    }
    
    /**
     * Get child name of parent.
     * 
     * Returns name of child based on pa
     * 
     * @return  name of child of this parent
     */
    public String getChildName() {
        String name = "";
        try {
            PreparedStatement q = con.prepareStatement("SELECT `name` FROM students WHERE `ID_student` = ? LIMIT 1");
            q.setString(1, ID_student);
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();
            while(results.next()) {
                name = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return name;
    }
    
    /**
     * Get names of all kids of parent.
     * 
     * Used mostly for combobox info.
     * 
     * @return  list of kids names
     */
    public List<String> getChildren() {
        List<String> data = new ArrayList<>();
        try {
            PreparedStatement q = con.prepareStatement("SELECT `name` FROM students WHERE `ID_parent` = ?");
            q.setString(1, ID_parent);
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();
            int col = md.getColumnCount();
            while(results.next() ) {
                Object obj = results.getObject(1);
                String name = (obj == null) ? null:obj.toString();
                data.add(name);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
    
    /**
     * Set child for this parent based on its name.
     * 
     * @param childName name of child to be set
     */
    public void setChild(String childName) {
        try {
            PreparedStatement q = con.prepareStatement("SELECT `ID_student` FROM students WHERE `ID_parent` = ? AND `name` = ? LIMIT 1");
            q.setString(1, ID_parent);
            q.setString(2, childName);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                ID_student = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get info of kid set in this manager.
     * 
     * @return  map with all child info [column -> value]
     */
    public Map<String, String> getChildInfo() {
        List<Map<String,String>> data = new ArrayList<>();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM students WHERE ID_student = ?");
            q.setString(1, this.ID_student);
            
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();

            List<String> columns = new ArrayList<>(md.getColumnCount());
            
            for(int i = 1; i <= md.getColumnCount(); i++){
                columns.add(md.getColumnName(i));
            }
            
            while(results.next() ) {
                Map<String,String> row = new HashMap<>(columns.size());
                for(String col : columns) {
                    row.put(col, results.getString(col));
                }
                data.add(row);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return data.get(0);
    }
    
    /**
     * Get all parent info from DB.
     * 
     * Get info of parent which is set in this manager.
     * 
     * @return  map with all child info [column -> value]
     */
    public Map<String, String> getParentInfo() {
        List<Map<String,String>> data = new ArrayList<>();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM parents WHERE ID_parent = ?");
            q.setString(1, this.ID_parent);
            
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();

            List<String> columns = new ArrayList<>(md.getColumnCount());
            
            for(int i = 1; i <= md.getColumnCount(); i++){
                columns.add(md.getColumnName(i));
            }
            
            while(results.next() ) {
                Map<String,String> row = new HashMap<>(columns.size());
                for(String col : columns) {
                    row.put(col, results.getString(col));
                }
                data.add(row);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return data.get(0);
    }
    
    /**
     * Add parent into DB.
     * 
     * Adds parent into DB with values specified in data list.
     * 
     * @param data  list with parent data
     * @return      ID of inserted parent
     */
    public long addParent(List<String> data) {
        long parentID = 0;
        
        try {
            PreparedStatement q = con.prepareStatement("INSERT INTO parents "
                    + "(name, surname, email, phone, ID_login)"
                    + " VALUES (?, ?, ?, ?, ?)", new String[] {"ID_parent"});
            for (int i = 1; i <= 5; i++) {
                q.setString(i, data.get(i - 1));
            }
            q.executeUpdate();
            
            ResultSet rs = q.getGeneratedKeys();
            if (rs.next()) {
               parentID = rs.getLong(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return parentID;
    }

    /**
     * Get login ID of parent.
     * 
     * @param parentID  ID of parent which loginID we want
     * @return          ID of parent login
     */
    public String getLoginID(String parentID) {
        String loginID = "";
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_login FROM parents WHERE ID_parent = ?");
            q.setString(1, parentID);
            
            ResultSet results = q.executeQuery();            
            while(results.next() ) {
                loginID = results.getString("ID_login");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loginID;
    }
    
    
    /**
     * Get parent ID when same parent exists.
     * 
     * Checks if there is parent with exactly same data and returns his ID.
     * 
     * @param parentData    list of correctly formated data (DB column order)
     * @return              null on failure, ID on success
     */
    public String checkParentExistence(List<String> parentData) {
        String loginID = null;
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_parent FROM parents WHERE name = ? AND surname = ? AND email = ? AND phone = ?");
            
            for (int i = 0; i < parentData.size(); i++) {
                q.setString(i + 1, parentData.get(i));
            }
            
            ResultSet results = q.executeQuery();            
            while(results.next() ) {
                loginID = results.getString("ID_parent");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return loginID;
    }

    /**
     * Delete parent from DB.
     * 
     * Deletes parent specified by ID from the database.
     * 
     * @param parentID  ID of parent to be deleted
     */
    public void deleteParent(String parentID) {       
        try {
            String parentLoginID = this.getLoginID(parentID);
            
            PreparedStatement q = con.prepareStatement("DELETE FROM parents WHERE ID_parent = ?");
            q.setString(1, parentID);
            
            // remove parent itself first then his login (FK in DB)
            q.executeUpdate();
            this.deleteLogin(parentLoginID);
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Check if parent has child.
     * 
     * Tries to select child which has parent with given ID.
     * 
     * @param parentID  ID of parent we want to check
     * @return          true on success false otherwise
     */
    public boolean hasChild(String parentID) {
        boolean ret = false;
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_student FROM students WHERE ID_parent = ?");
            q.setString(1, parentID);
            
            ResultSet results = q.executeQuery();          
            // if something was found
            if(results.isBeforeFirst()) {
                ret = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }

    /**
     * Return ID class of student set in manager.
     * 
     * @return  ID class of set student 
     */
    public String getClassOfStudent(){
        String classID = "";
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_class FROM students WHERE ID_student = ?");
            q.setString(1, this.ID_student);
            
            ResultSet results = q.executeQuery();            
            while(results.next() ) {
                classID = results.getString("ID_class");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return classID;
    }
}
