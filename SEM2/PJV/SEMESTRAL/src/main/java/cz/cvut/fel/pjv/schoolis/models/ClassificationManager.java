package cz.cvut.fel.pjv.schoolis.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ClassificationManager class.
 * 
 * @author Vojcek
 */
public class ClassificationManager {
    private Connection con;
    
    /**
     * ClassificationManager constructor.
     * 
     * @param connection    instance of DB connection
     */ 
    public ClassificationManager(Connection connection) {
        this.con = connection;
    }

    /**
     * Get student classification from subject.
     * 
     * Get given student classification from given subject for display
     * in classification table.
     * 
     * @param ID_student    ID of student
     * @param ID_subject    ID of subject
     * @return              observable list of data
     */
    public ObservableList<String[]> getClassification(String ID_student, String ID_subject) {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM grades WHERE `ID_student` = ? AND `ID_subject` = ?");
            q.setString(1, ID_student);
            q.setString(2, ID_subject);
            
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
            Logger.getLogger(ClassificationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }

    /**
     * Add new grade into DB.
     * 
     * Creates new grade with given data in database.
     * 
     * @param grade         grade value
     * @param weight        grade weight
     * @param description   grade description (e.g. Semestral test, ...)
     * @param date          date when grade was given
     * @param ID_subject    ID of subject graded
     * @param ID_student    ID of student to be graded
     */
    public void newGrade(String grade, String weight, String description, String date, String ID_subject, String ID_student) {
        try {
            PreparedStatement q = con.prepareStatement("INSERT INTO grades (grade, weight, description, date, ID_subject, ID_student) VALUES (?, ?, ?, ?, ?, ?)");
            q.setString(1, grade);
            q.setString(2, weight);
            q.setString(3, description);
            q.setString(4, date);
            q.setString(5, ID_subject);
            q.setString(6, ID_student);
            
            q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClassificationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Remove grade with given ID from DB.
     * 
     * @param ID_grade  ID of grade
     */
    public void removeGrade(String ID_grade) {
        try {
            PreparedStatement q = con.prepareStatement("DELETE FROM grades WHERE `ID_grade` = ?");
            q.setString(1, ID_grade);
            
            q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ClassificationManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
