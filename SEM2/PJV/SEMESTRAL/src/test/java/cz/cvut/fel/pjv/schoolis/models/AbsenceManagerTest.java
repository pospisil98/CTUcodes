package cz.cvut.fel.pjv.schoolis.models;

import cz.cvut.fel.pjv.schoolis.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 * Test class for Absence Manager.
 * 
 * @author Vojcek
 */
public class AbsenceManagerTest {
    private static String DATE = "1998-01-02";
    private static Connection con;
    private String idStudent;
    private AbsenceManager absenceManager;

    /**
     * Absence Manager Test constructor.
     */
    public AbsenceManagerTest() {
    }

    /**
     * Set up class before testing.
     */
    @BeforeClass
    public static void setUpClass() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://den1.mysql3.gear.host:3306/pjvdb?user=" + Config.DBUSER
                    + "&password=" + Config.DBPASSWORD + "&" + Config.DBSETINGS);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Non static method for setup.
     */
    @Before
    public void setUp() {
        idStudent = this.getExistingStudentId();
    }
    
    private String getExistingStudentId() {
        String id = "";
                    
            try {
                PreparedStatement q = con.prepareStatement("SELECT ID_student FROM students LIMIT 1");

                ResultSet results = q.executeQuery();
                while (results.next()) {
                    id = results.getString("ID_student");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AbsenceManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
            return id;
    }

    /**
     * Test of apologize method, of class AbsenceManager.
     */
    @Test
    public void testApologize() {
        System.out.println("apologize");

        // prepare managers
        absenceManager = new AbsenceManager(con);
        AbsenceManagerImpl aMI = new AbsenceManagerImpl(con);
        
        // insert absence ang get its ID
        absenceManager.newAbsence(idStudent, DATE);
        String inserted = aMI.getIDbyIdDate(idStudent, DATE);

        // try to apologize absence and get its ID
        absenceManager.apologize(inserted, idStudent);
        String state = aMI.getAbsenceState(inserted);
        
        // absence should be apologized - so true
        assertNotEquals("0", state);
        
        // remove insterted absence
        absenceManager.removeAbsence(inserted);   
    }

    /**
     * Test of newAbsence method, of class AbsenceManager.
     */
    @Test
    public void testNewAbsence() {
        System.out.println("newAbsence");

        // prepare managers
        absenceManager = new AbsenceManager(con);
        AbsenceManagerImpl aMI = new AbsenceManagerImpl(con);
        
        // insert absence and get its ID
        absenceManager.newAbsence(idStudent, DATE);
        String inserted = aMI.getIDbyIdDate(idStudent, DATE);
        
        // we should get some ID
        assertNotEquals("", inserted);
        
        // remove inserted absence
        absenceManager.removeAbsence(inserted);
    }

    /**
     * Test of removeAbsence method, of class AbsenceManager.
     */
    @Test
    public void testRemoveAbsence() {
        System.out.println("removeAbsence");
        
        // prepare managers
        absenceManager = new AbsenceManager(con);
        AbsenceManagerImpl aMI = new AbsenceManagerImpl(con);
        
        // insert new absence and get its ID
        absenceManager.newAbsence(idStudent, DATE);
        String inserted = aMI.getIDbyIdDate(idStudent, DATE);
        
        // remove inserted absence and try to get the ID again
        absenceManager.removeAbsence(inserted);
        String afterDelete = aMI.getIDbyIdDate(idStudent, DATE);
        
        // inserted should be ID, afterDelete empty string
        assertNotEquals(inserted, afterDelete);        
    }
    
    /**
     * AbsenceManager Implementation with few functions for using in tests.
     */
    private class AbsenceManagerImpl extends AbsenceManager {
        private AbsenceManagerImpl(Connection connection) {
            super(connection);
        }
        
        private String getIDbyIdDate(String studentID, String date) {
            String absenceID = "";
                    
            try {
                PreparedStatement q = con.prepareStatement("SELECT ID_absence FROM absences WHERE ID_student = ? AND date = ?");
                q.setString(1, studentID);
                q.setString(2, date);

                ResultSet results = q.executeQuery();
                while (results.next()) {
                    absenceID = results.getObject(1).toString();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AbsenceManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
            return absenceID;
        }
        
        private String getAbsenceState(String absenceID) {
            String state = "";
                    
            try {
                PreparedStatement q = con.prepareStatement("SELECT apologized FROM absences WHERE ID_absence = ?");
                q.setString(1, absenceID);
                
                ResultSet results = q.executeQuery();
                while (results.next()) {
                    state = results.getString("apologized");
                }
            } catch (SQLException ex) {
                Logger.getLogger(AbsenceManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
            return state;
        }
    }

}
