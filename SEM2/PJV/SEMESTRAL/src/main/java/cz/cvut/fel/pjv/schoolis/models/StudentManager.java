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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * StudentManager class.
 * 
 * @author Karel
 */
public class StudentManager extends UserManager {
    private String ID_student;
   
    /**
     * StudentManager constructor.
     * 
     * Used when using student manager inside other managers, ...
     * 
     * @param connection    instance of CB connection
     */
    public StudentManager(Connection connection) {
        this.con = connection;
    }
    
    /**
     * StudentManager constructor.
     * 
     * Used when using student manager in student parts (fully).
     * 
     * @param ID_login      id of logged user (student)
     * @param connection    instance of DB connection
     */
    public StudentManager(String ID_login, Connection connection) {
        this.con = connection;
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT `ID_student` FROM students WHERE `ID_login` = ? LIMIT 1");
            q.setString(1, ID_login);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                this.ID_student = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getID_student() {
        return ID_student;
    }

    public void setID_student(String ID_student) {
        this.ID_student = ID_student;
    }

    public Connection getConnection() {
        return con;
    }
    
    /**
     * Get all student info from DB.
     * 
     * @return map of info [column -> value]
     */
    public Map<String, String> getStudentInfo() {
        Map<String,String> info = null;
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM students WHERE ID_student = ?");
            q.setString(1, this.ID_student);
            
            // gather results
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();

            // get column names
            List<String> columns = new ArrayList<>(md.getColumnCount());          
            for(int i = 1; i <= md.getColumnCount(); i++){
                columns.add(md.getColumnName(i));
            }
            
            // fill column values
            info = new HashMap<>(columns.size());
            while(results.next() ) {
                for(String col : columns) {
                    info.put(col, results.getString(col));
                }
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return info;
    }
    
    /**
     * Get all parent info from DB.
     * 
     * @return  map of info [column -> value]
     */
    public Map<String, String> getParentInfo() {
        Map<String,String> info = null;
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM parents WHERE ID_parent = ?");
            q.setString(1, this.getId_Parent());
            
            // gather results
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();

            // get column names
            List<String> columns = new ArrayList<>(md.getColumnCount());
            for(int i = 1; i <= md.getColumnCount(); i++){
                columns.add(md.getColumnName(i));
            }
            info = new HashMap<>(columns.size());
            
            // fill column values
            while(results.next() ) {
                for(String col : columns) {
                    info.put(col, results.getString(col));
                }
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return info;
    }
    
    /**
     * Get students of given class for table display.
     * 
     * @param ID_class  ID of class we want students from
     * @return          ObservableList with student info
     */
    public ObservableList<String[]> getStudentsByClassId(String ID_class) {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM students WHERE ID_class = ?");
            q.setString(1, ID_class);
            
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();
            int col = md.getColumnCount();
            
            while(results.next() ) {
                String[] row = new String[md.getColumnCount()];
                for(int i = 1; i <= col; i++) {
                    Object obj = results.getObject(i);
                    row[i-1] = (obj == null) ? null : obj.toString();
                }
                data.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return data;
    }
    
    /**
     * Get full name of student from this manager.
     * 
     * @return  string with "Name Surname" format
     */
    public String getFullName() {
        String name = "";
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT name, surname FROM students WHERE ID_student = ? LIMIT 1");
            q.setString(1, ID_student);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                name = results.getObject(1).toString() + " " + results.getObject(2).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return name;
    }
    
    /**
     * Get student ID by full name and his class.
     * 
     * @param name      name of wanted student
     * @param surname   surname of wanted student
     * @param classID   ID of student class
     * @return          ID of wanted student
     */
    public String getStudentIdFromNameSurnameClassID(String name, String surname, String classID) {
        String id = "";
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_student FROM students WHERE name = ? AND surname = ? AND ID_class = ? LIMIT 1");
            q.setString(1, name);
            q.setString(2, surname);
            q.setString(3, classID);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                id = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }
    
    /**
     * Get student info from DB.
     * 
     * @param studentID ID of student we want info about
     * @return          map with student info [column -> value]
     */
    public Map<String, String> getStudentInfo(String studentID) {
        Map<String,String> info = null;
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM students WHERE ID_student = ? LIMIT 1");
            q.setString(1, studentID);
            
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();

            // get column names
            List<String> columns = new ArrayList<>(md.getColumnCount());
            for(int i = 1; i <= md.getColumnCount(); i++){
                columns.add(md.getColumnName(i));
            }
            
            // fill columns with corresponing value
            info = new HashMap<>(columns.size());
            while(results.next() ) {
                for(String col : columns) {
                    info.put(col, results.getString(col));
                }
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        return info;
    }
    
    /**
     * Add student into DB.
     * 
     * @param data  correctly formated student data
     * @return      0 on failure 1 on success
     */
    public int addStudent(List<String> data) {
        int rows = 0;
        
        try {
            PreparedStatement q = con.prepareStatement("INSERT INTO students "
                    + "(name, surname, street, city, postcode, mail, phone,"
                    + " assurance_company, personal_identification_number, birth_date,"
                    + " ID_class, ID_login, ID_parent)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            
            for (int i = 1; i <= 13; i++) {
                q.setString(i, data.get(i - 1));
            }
            
            rows = q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rows;
    }

    /**
     * Get login ID of given student.
     * 
     * @param studentID ID student we want login for
     * @return          ID login of wanted student
     */
    public String getLoginID(String studentID) {
        String loginID = "";
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_login FROM students WHERE ID_student = ?");
            q.setString(1, studentID);
            
            ResultSet results = q.executeQuery();
            while(results.next() ) {
                loginID = results.getString("ID_login");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return loginID;
    }

    /**
     * Get parent ID of given student.
     * 
     * @param studentID student which parent we want
     * @return          ID of wanted parent
     */
    public String getParentID(String studentID) {
        String parentID = "";
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_parent FROM students WHERE ID_student = ?");
            q.setString(1, studentID);
            
            ResultSet results = q.executeQuery();            
            while(results.next() ) {
                parentID = results.getString("ID_parent");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return parentID;
    }
    
    /**
     * Delete student with given ID from DB.
     * 
     * @param studentID ID of student we want to delete
     */
    public void deleteStudent(String studentID) {        
        try {
            String parentID = this.getParentID(studentID);
            String studentLoginID = this.getLoginID(studentID);
            
            // delete student
            PreparedStatement q = con.prepareStatement("DELETE FROM students WHERE ID_student = ?");
            q.setString(1, studentID);
            q.executeUpdate();
            
            // delete students login
            this.deleteLogin(studentLoginID);
            
            // check if parent has anoter child otherwise delete it
            ParentManager parentManager = new ParentManager(con);
            if (!parentManager.hasChild(parentID)) {
                parentManager.deleteParent(parentID);
            }            
        } catch (SQLException ex) {
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String getId_Parent() {
        String ID_parent = "";
        try {
            PreparedStatement q = con.prepareStatement("SELECT `ID_parent` FROM students WHERE `ID_student` = ? LIMIT 1");
            q.setString(1, ID_student);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                ID_parent = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(StudentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ID_parent;
    }

    /**
     * Get ID_class of set student.
     *
     * @return  ID_class of wanted student
     */
    public String getClassId() {
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
