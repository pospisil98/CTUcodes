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
 * AbsenceManager class.
 * 
 * @author Karel
 */
public class AbsenceManager {
    /*
    * database connection
    */
    private Connection con;
    
    /**
     * AbsenceManager constructor.
     * 
     * @param connection
     */
    public AbsenceManager(Connection connection) {
        this.con = connection;
    }

    /**
     * Apologise absence for student.
     * 
     * Apologises given absence of given student and update 
     * 
     * @param ID_absence    ID of absence to be apologised
     * @param ID_student    ID of apologising student
     */
    public void apologize(String ID_absence, String ID_student) {
        try {       
            PreparedStatement q = con.prepareStatement("UPDATE absences SET `apologized` = true WHERE `ID_absence` = ?");
            q.setString(1, ID_absence);
            
            q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AbsenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get absences of student.
     * 
     * Gets absences of student with given ID. It returns all info about absence
     * which is in DB.
     * 
     * @param ID_student    ID of student whose absences we want
     * @return              observable list of absences
     */
    public ObservableList<String[]> getAbsences(String ID_student) {
        ObservableList<String[]> data = FXCollections.observableArrayList();
        
        try {
            PreparedStatement q = con.prepareStatement("SELECT * FROM absences WHERE `ID_student` = ?");
            q.setString(1, ID_student);
            
            ResultSet results = q.executeQuery();
            ResultSetMetaData md = results.getMetaData();
            
            int col = md.getColumnCount();
            while(results.next() ) {
                String[] row = new String[md.getColumnCount()];
                for(int i = 1; i <= col; i++) {
                    Object obj = results.getObject(i);
                    row[i-1] = (obj == null) ? null : obj.toString();
                    
                    if(i == 3) {
                        if("true".equals(row[i-1])) {
                            row[i-1] = "omluveno";
                        } else {
                            row[i-1] = "neomluveno";
                        }
                    }
                }
                data.add(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AbsenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
    
    /**
     * Add new absence into DB.
     * 
     * Adds new absence into database to given student with given date.
     * Default value for absence is FALSE = not apologised.
     * 
     * @param ID_student    ID of absenting student
     * @param date          date of absence
     */
    public void newAbsence(String ID_student, String date) {
        try {
            PreparedStatement q = con.prepareStatement("INSERT INTO absences (date, apologized, ID_student) VALUES (?, false, ?)");
            q.setString(1, date);
            q.setString(2, ID_student);
            
            q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AbsenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Remove absence from DB.
     * 
     * Removes absence with given ID form DB.
     * 
     * @param ID_absence
     */
    public void removeAbsence(String ID_absence) {
        try {
            PreparedStatement q = con.prepareStatement("DELETE FROM absences WHERE `ID_absence` = ?");
            q.setString(1, ID_absence);
            
            q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AbsenceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
