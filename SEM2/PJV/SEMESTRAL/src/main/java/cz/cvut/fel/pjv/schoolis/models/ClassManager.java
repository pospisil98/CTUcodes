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
 * ClassManager class.
 * 
 * @author Vojcek
 */
public class ClassManager {
    private Connection con;
    private String ID_class;

    /**
     * ClassManager constructor.
     * @param con   database connection instance
     */
    public ClassManager(Connection con) {
        this.con = con;
    }
    
    public String getID_class() {
        return ID_class;
    }

    public void setID_class(String ID_class) {
        this.ID_class = ID_class;
    }
    
    public Connection getConnection() {
        return con;
    }
    
    /**
     * Get class name from its ID.
     * 
     * @param ID_class  ID of class we want name from
     * @return          name of class
     */
    public String getClassNameById(String ID_class) {
        String name = "";
        try {
            PreparedStatement q = con.prepareStatement("SELECT `name` FROM classes WHERE `ID_class` = ? LIMIT 1");
            q.setString(1, ID_class);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                name = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return name;
    }
    
    /**
     * Get class ID by name.
     * 
     * @param name  name of class we want ID from
     * @return      ID of class
     */
    public String getClassIdByName(String name) {
        String id = "";
        try {
            PreparedStatement q = con.prepareStatement("SELECT `ID_class` FROM classes WHERE `name` = ? LIMIT 1");
            q.setString(1, name);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                id = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }
    
    /**
     * Get all class info.
     * 
     * @return map with class info [column -> data]
     */
    public Map<String, String> getClassInfo() {
        List<Map<String,String>> data = new ArrayList<>();
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM classes WHERE ID_class = ?");
            q.setString(1, this.ID_class);
            
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
        
        this.ID_class = data.get(0).get("ID_class");
        
        return data.get(0);
    }
    
    /**
     * Get names of all classes.
     * 
     * @return list of class names
     */
    public List<String> getAllClassesNames() {
        List<String> data = new ArrayList<>();
        try {
            PreparedStatement q = con.prepareStatement("SELECT `name` FROM classes");
            ResultSet results = q.executeQuery();

            while(results.next() ) {
                Object obj = results.getObject(1);
                String name = (obj == null) ? null : obj.toString();
                data.add(name);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
    
    /**
     * Get names of all students from given class.
     * 
     * @param classID   ID of class we want students from
     * @return          list of Name Surname strings
     */
    public List<String> getStudentsFromClass(String classID) {
        List<String> data = new ArrayList<>();
        try {
            PreparedStatement q = con.prepareStatement("SELECT name, surname FROM students WHERE ID_class = ?");
            q.setString(1, classID);
            
            ResultSet results = q.executeQuery();
            String name, surname;
            while(results.next() ) {
                name = results.getString("name");
                surname = results.getString("surname");
                data.add(name + " " + surname);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    /**
     * Get IDs of students from given class.
     * 
     * @param classID   ID of class we wants IDs from
     * @return          List of IDs
     */
    public List<String> getStudentFromClassIDs(String classID) {
        List<String> data = new ArrayList<>();
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_student FROM students WHERE ID_class = ?");
            q.setString(1, classID);
            
            ResultSet results = q.executeQuery();
            while(results.next() ) {
                data.add(results.getString("ID_student"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClassManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    /**
     * Add new class to DB.
     * 
     * @param name          name of new class
     * @param startYear     start year of new class
     * @param teacherID     ID of class teacher
     * @return              1 on success otherwise 0
     */
    public int addClass(String name, String startYear, String teacherID) {
        int rows = 0;
        
        try {
            PreparedStatement q = con.prepareStatement("INSERT INTO classes (name, start_year, ID_teacher) VALUES (?, ?, ?)");
            q.setString(1, name);
            q.setString(2, startYear);
            q.setString(3, teacherID);
            
            rows = q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClassManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rows;
    }

    /**
     * Delete class with given ID.
     * 
     * @param classID
     */
    public void deleteClass(String classID) {
        try {
            PreparedStatement q = con.prepareStatement("DELETE FROM has_subject WHERE ID_class = ?");
            q.setString(1, classID);
            q.executeUpdate();
            q = con.prepareStatement("DELETE FROM classes WHERE ID_class = ?");
            q.setString(1, classID);
            q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClassManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
