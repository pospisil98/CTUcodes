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
 * TeacherManager class.
 * 
 * @author Vojcek
 */
public class TeacherManager extends UserManager{
    private String ID_teacher;

    /**
     * TeacherManager constructor.
     * 
     * Basic TeacherManager constructor for usage in other managers.
     * 
     * @param con   instance of DB connection
     */
    public TeacherManager(Connection con) {
        this.con = con;
    }
    
    /**
     * TeacherManager constructor.
     * 
     * TeacherManager constructor for using in teacher parts of application.
     * 
     * @param ID_login      ID of logged user (teacher)
     * @param connection    instance of DB connection
     */
    public TeacherManager(String ID_login, Connection connection) {
        this.con = connection;
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT `ID_teacher` FROM teachers WHERE `ID_login` = ? LIMIT 1");
            q.setString(1, ID_login);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                this.ID_teacher = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getID_teacher() {
        return ID_teacher;
    }
    
    public Connection getConnection() {
        return this.con;
    }

    public void setID_teacher(String ID_teacher) {
        this.ID_teacher = ID_teacher;
    }
    
    /**
     * Get info of set teacher from DB.
     * 
     * @return  map of info [column -> value]
     */
    public Map<String, String> getTeacherInfo() {
        Map<String,String> info = null;
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM teachers WHERE ID_teacher = ?");
            q.setString(1, this.ID_teacher);
            
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();

            // get column names
            List<String> columns = new ArrayList<>(md.getColumnCount());
            info = new HashMap<>(columns.size());
            for(int i = 1; i <= md.getColumnCount(); i++){
                columns.add(md.getColumnName(i));
            }
            
            // fill columns with values
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
     * Get name of teacher class.
     * 
     * @return name of class
     */
    public String getTeacherClass() {
        String name = "";
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT `name` FROM classes WHERE `ID_teacher` = ? LIMIT 1");
            q.setString(1, ID_teacher);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                name = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return name;
    }

    
    /**
     * Return info of all teacher for table display.
     * 
     * @return Observable List of all teacher info
     */
    public ObservableList<String[]> getAllTeachers() {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM teachers");
            
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
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        } 

        return data;
    }
    
    /**
     * Return all teacher names.
     * 
     * @return name of teacher in "Name Surname" format
     */
    public List<String> getAllTeachersNames() {
        List<String> data = new ArrayList<>();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT name, surname FROM teachers");
            
            ResultSet results = q.executeQuery();            
            
            String name, surname;
            while(results.next() ) {
                name = results.getString("name");
                surname = results.getString("surname");
                data.add(name + " " + surname);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }

    /**
     * Get class info from DB.
     * 
     * @param id    ID of class we want info about
     * @return      map of info [column -> values]
     */
    public Map<String, String> getInfoForClass(String id) {
        Map<String,String> info = null;
        
        try {
            PreparedStatement q = this.con.prepareStatement("SELECT name, surname, title FROM teachers WHERE ID_teacher = ?");
            q.setString(1, id);
            
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();
            
            // gat column names
            List<String> columns = new ArrayList<>(md.getColumnCount());
            for(int i = 1; i <= md.getColumnCount(); i++){
                columns.add(md.getColumnName(i));
            }
            info = new HashMap<>(columns.size());
            
            // fill columns with data
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
     * Get teacher ID from name.
     * 
     * @param name      name of teacher
     * @param surname   surname of teacher
     * @return          ID of teacher
     */
    public String getIdFromName(String name, String surname) {
        String id = "";
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_teacher FROM teachers WHERE name = ? AND surname = ? LIMIT 1");
            q.setString(1, name);
            q.setString(2, surname);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                id = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }

    /**
     * Get teacher name from ID.
     * 
     * @param id    id of teacher 
     * @return      name in "Name Surname" format
     */
    public String getNameFromId(String id) {
        String name = "";
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT name, surname FROM teachers WHERE ID_teacher = ?");
            q.setString(1, id);
            
            ResultSet results = q.executeQuery();            
            while(results.next() ) {
                name = results.getString("name") + " " + results.getString("surname");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return name;
    }

    /**
     * Get all teacher info from DB.
     * 
     * @param teacherID ID of teacher we want info about
     * @return          map of info [column -> value] 
     */
    public Map<String, String> getTeacherInfo(String teacherID) {
        Map<String,String> info = null;
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM teachers WHERE ID_teacher = ?");
            q.setString(1, teacherID);
            
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();

            List<String> columns = new ArrayList<>(md.getColumnCount());
            for(int i = 1; i <= md.getColumnCount(); i++){
                columns.add(md.getColumnName(i));
            }
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
     * Get list of all teacher subject names.
     * 
     * @param teacherID ID of teacher
     * @return          list of subject names
     */
    public List<String> getAllTeacherSubjects(String teacherID) {
        List<String> data = new ArrayList<>();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT name FROM subjects WHERE ID_teacher = ?");
            q.setString(1, teacherID);
            
            ResultSet results = q.executeQuery();
            while(results.next() ) {
                data.add(results.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }
    
    /**
     * Get list of all teacher subject IDs.
     * 
     * @param teacherID ID of teacher
     * @return          list of subject IDs
     */
    public List<String> getAllTeacherSubjectIDs(String teacherID) {
        List<String> data = new ArrayList<>();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_subject FROM subjects WHERE ID_teacher = ?");
            q.setString(1, teacherID);
            
            ResultSet results = q.executeQuery();            
            while(results.next() ) {
                data.add(results.getString("ID_subject"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }

    /**
     * Get all classes that given teacher teaches.
     * 
     * @param teacherID ID of teacher 
     * @return          list of class names
     */
    public List<String> getAllTeachersClasses(String teacherID) {
        List<String> data = new ArrayList<>();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT name FROM classes WHERE ID_teacher = ?");
            q.setString(1, teacherID);
            
            ResultSet results = q.executeQuery();
            while(results.next() ) {
                data.add(results.getString("name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }

    /**
     * Get login ID of given teacher.
     * 
     * @param teacherID ID of teacher we want login ID for
     * @return          login ID of teacher 
     */
    public String getLoginID(String teacherID) {
        String loginID = "";
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_login FROM teachers WHERE ID_teacher = ?");
            q.setString(1, teacherID);
            
            ResultSet results = q.executeQuery();            
            while(results.next() ) {
                loginID = results.getString("ID_login");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return loginID;
    }
    
    /**
     * Add teacher into DB.
     * 
     * Add teacher with data that are correctly formated from controller (by DB column order).
     * 
     * @param data  correctly formated data
     * @return      1 on success 0 on failure
     */
    public int addTeacher(List<String> data) {
        int rows = 0;
        
        try {
            PreparedStatement q = con.prepareStatement("INSERT INTO teachers "
                    + "(name, surname, title, street, city, postcode, mail, phone, ID_login)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 1; i <= 9; i++) {
                q.setString(i, data.get(i - 1));
            }
            rows = q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rows;
    }

    /**
     * Delete teacher from DB.
     * 
     * @param teacherID ID of teacher to be deleted
     */
    public void deleteTeacher(String teacherID) {       
        try {
            SubjectManager subjectManager = new SubjectManager(con);
            String loginID = this.getLoginID(teacherID);
            List<String> subjects = this.getAllTeacherSubjectIDs(teacherID);
            
            for (String idSubject : subjects) {
                subjectManager.deleteSubject(idSubject);
            }
            
            // delete teacher
            PreparedStatement q = con.prepareStatement("DELETE FROM teachers WHERE ID_teacher = ?");
            q.setString(1, teacherID);
            q.executeUpdate();
            
            // delete its login
            this.deleteLogin(loginID);
        } catch (SQLException ex) {
            Logger.getLogger(TeacherManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Check if this teacher teaches given subject.
     * 
     * @param ID_subject    ID_subject to be checked
     * @return              boolean according to question
     */
    public boolean teaches(String ID_subject) {
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM subjects WHERE ID_subject = ? AND ID_teacher = ? LIMIT 1");
            q.setString(1, ID_subject);
            q.setString(2, ID_teacher);
            
            ResultSet results = q.executeQuery();            
            return results.isBeforeFirst();
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
