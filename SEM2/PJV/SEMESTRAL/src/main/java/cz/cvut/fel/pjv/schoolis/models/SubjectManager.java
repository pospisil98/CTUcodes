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
 * SubjectManager class.
 * 
 * @author Karel
 */
public class SubjectManager {
    private Connection con;
    private String ID_subject;

    /**
     * SubjectManager constructor.
     * @param con
     */
    public SubjectManager(Connection con) {
        this.con = con;
    }
        
    public String getID_subject() {
        return ID_subject;
    }

    public void setID_subject(String ID_subject) {
        this.ID_subject = ID_subject;
    }
    
    public Connection getConnection() {
        return this.con;
    }
    
    /**
     * Get names of all subjects.
     * 
     * @return  list of subject names
     */
    public List<String> getSubjects() {
        List<String> data = new ArrayList<>();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT `name` FROM subjects");
            ResultSet results = q.executeQuery();
            while(results.next() ) {
                Object obj = results.getObject(1);
                String name = (obj == null) ? null : obj.toString();
                data.add(name);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }
    
    /**
     * Get subjects and their teachers
     * 
     * @param teacherManager
     * @return 
     */
    public List<String> getSubjectsAndTeachers(TeacherManager teacherManager) {
        List<String> data = new ArrayList<>();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT `name`, `ID_teacher` FROM subjects");
            ResultSet results = q.executeQuery();
            while(results.next() ) {
                Object obj1 = results.getObject(1);
                Object obj2 = results.getObject(2);
                String name = (obj1 == null) ? null : obj1.toString() + " ";
                name += (obj1 == null) ? null : teacherManager.getNameFromId(obj2.toString());
                data.add(name);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }
    
    /**
     * Get all classes by ID of subject.
     * 
     * @param ID_subject    ID of subject
     * @return              list of classes
     */
    public List<String> getClassesByIdSubject(String ID_subject) {
        List<String> ID_data = new ArrayList<>();
        List<String> nameData = new ArrayList<>();
        try {
            PreparedStatement q = con.prepareStatement("SELECT `ID_class` FROM has_subject WHERE `ID_subject` = ?");
            q.setString(1, ID_subject);
            ResultSet results = q.executeQuery();
            while(results.next() ) {
                Object obj = results.getObject(1);
                String name = (obj == null) ? null : obj.toString();
                ID_data.add(name);
            }
            for(String ID_class: ID_data) {
                q = con.prepareStatement("SELECT `name` FROM classes WHERE `ID_class` = ?");
                q.setString(1, ID_class);
                results = q.executeQuery();
                while(results.next() ) {
                    Object obj = results.getObject(1);
                    String name = (obj == null) ? null : obj.toString();
                    nameData.add(name);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return nameData;
    }
    
    /**
     * Get subject ID by its name.
     * 
     * @param name  name of subject
     * @return      ID of subject
     */
    public String getSubjectIdByName(String name) {
        String id = "";
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT `ID_subject` FROM subjects WHERE `name` = ? LIMIT 1");
            q.setString(1, name);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                id = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }
    
    /**
     * Get subject ID by name of subject and by name and surname of teacher.
     * 
     * @param subjectName       name of subject
     * @param teacherName       name of teacher
     * @param teacherSurname    surname of teacher
     * @return                  ID of subject
     */     
    public String getSubjectIdByNameAndTeacher(String subjectName, String teacherName, String teacherSurname) {
        String id = "";
        try {
            PreparedStatement q = con.prepareStatement("SELECT `ID_teacher` FROM teachers WHERE `name` = ?  AND `surname` = ? LIMIT 1");
            q.setString(1, teacherName);
            q.setString(2, teacherSurname);
            ResultSet results = q.executeQuery();
            while(results.next()) {
                id = results.getObject(1).toString();
            }
            q = con.prepareStatement("SELECT `ID_subject` FROM subjects WHERE `name` = ?  AND `ID_teacher` = ? LIMIT 1");
            q.setString(1, subjectName);
            q.setString(2, id);
            results = q.executeQuery();
            while(results.next()) {
                id = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }

    /**
     * Get set subject name.
     * 
     * @return  name of subject
     */
    public String getSubject() {
        String subject = "";
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT `name` FROM subjects WHERE `ID_subject` = ? LIMIT 1");
            q.setString(1, ID_subject);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                subject = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return subject;
    }
    
    /**
     * Checks subject existence.
     * 
     * @param name          name of subject
     * @param teacherID     id of teacher of the subject
     * @return
     */
    public boolean checkSubjectExistence(String name, String teacherID) {
        boolean exists = false;
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM subjects WHERE name = ? AND ID_teacher = ?");
            q.setString(1, name);
            q.setString(2, teacherID);
            
            ResultSet results = q.executeQuery();
            // when the iterator is before first it means that the result is not empty
            if(!results.isBeforeFirst()){
                exists = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return exists;
    }

    /**
     * Return all subject info from DB.
     * 
     * @param subjectID ID of subject we want info about
     * @return          map of info [column -> value]
     */
    public Map<String, String> getSubjectInfo(String subjectID) {
        Map<String,String> info = null;
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM subjects WHERE ID_subject = ?");
            q.setString(1, subjectID);
            
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();

            // get column names
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
     * Add subject into DB.
     * 
     * Adds subject into database from correctly formated data.
     * 
     * @param data  data arranged by DB column order
     * @return      0 on failure 1 on success
     */
    public int addSubject(List<String> data) {
        int rows = 0;
        long subjectID = 0;
        try {
            PreparedStatement q = con.prepareStatement("INSERT INTO subjects (name, abbreviation, ID_teacher) VALUES (?, ?, ?)");
            q.setString(1, data.get(0));
            q.setString(2, data.get(1));
            q.setString(3, data.get(2));
            rows = q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rows;
    }
    
    /**
     * Creates M:N database relation between table of classes and table of subjects.
     * 
     * @param ID_subject    ID of subject
     * @param ID_class      ID of class
     * @return              returns 1 if relation was added, 0 otherwise
     */
    public int addSubjectClassRelation(String ID_subject, String ID_class) {
        int rows = 0;
        try {
            PreparedStatement q = con.prepareStatement("INSERT INTO has_subject (ID_subject, ID_class) VALUES (?, ?)");
            q.setString(1, ID_subject);
            q.setString(2, ID_class);

            rows = q.executeUpdate();
            rows = 1;
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rows;
    }
    
    /**
     * Removes row from m:n table.
     * 
     * @param ID_subject    ID of subject to be deleted
     * @param ID_class      ID of class to be deleted
     */
    public void removeSubjectClassRelation(String ID_subject, String ID_class) {
        try {
            PreparedStatement q = con.prepareStatement("DELETE FROM has_subject WHERE ID_subject = ? AND ID_class = ?");
            q.setString(1, ID_subject);
            q.setString(2, ID_class);
            q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Checks subject existence.
     * 
     * @param subjectData   subject data
     * @return              returns ID of subject if subject exists, null otherwise
     */
    public String checkSubjectExistence(List<String> subjectData) {
        String ID_subject = null;
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_subject FROM subjects WHERE name = ? AND abbreviation = ? AND ID_teacher = ?");
            q.setString(1, subjectData.get(0));
            q.setString(2, subjectData.get(1));
            q.setString(3, subjectData.get(3));
            
            ResultSet results = q.executeQuery();            
            while(results.next() ) {
                ID_subject = results.getString("ID_subject");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ParentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ID_subject;
    }

    /**
     * Delete subject from DB.
     * 
     * @param subjectID ID of subject to be deleted
     */
    public void deleteSubject(String subjectID) {
        try {
            PreparedStatement q = con.prepareStatement("DELETE FROM has_subject WHERE ID_subject = ?");
            q.setString(1, subjectID);
            q.executeUpdate();
            q = con.prepareStatement("DELETE FROM subjects WHERE ID_subject = ?");
            q.setString(1, subjectID);
            q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return list of names of students subjects.
     * 
     * @param ID_class  ID_class class which is student going into
     * @return          list of class names
     */
    public List<String> getStudentsSubjects(String ID_class) {
        List<String> dataID = new ArrayList<>();
        List<String> dataName = new ArrayList<>();
        try {
            PreparedStatement q = con.prepareStatement("SELECT ID_subject FROM has_subject WHERE ID_class = ?");
            q.setString(1, ID_class);
            ResultSet results = q.executeQuery();
            while(results.next() ) {
                Object obj = results.getObject(1);
                String name = (obj == null) ? null : obj.toString();
                dataID.add(name);
            }
            for(String id: dataID)
            {
                dataName.add(this.getSubjectNameById(id));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataName;
    }

    private String getSubjectNameById(String id) {
        String name = "";
        try {
            PreparedStatement q = con.prepareStatement("SELECT `name` FROM subjects WHERE `ID_subject` = ? LIMIT 1");
            q.setString(1, id);
            
            ResultSet results = q.executeQuery();
            while(results.next()) {
                name = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return name;
    }

    /**
     * Return ID_teacher which teaches given subject.
     * 
     * @param ID_subject    ID_subject we want to know teacher for
     * @return              ID_teacher of given subject
     */
    public String getTeacher(String ID_subject) {
        String id = "";
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT `ID_teacher` FROM subjects WHERE `ID_subject` = ? LIMIT 1");
            q.setString(1, ID_subject);
            ResultSet results = q.executeQuery();
            while(results.next()) {
                id = results.getObject(1).toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }
}
