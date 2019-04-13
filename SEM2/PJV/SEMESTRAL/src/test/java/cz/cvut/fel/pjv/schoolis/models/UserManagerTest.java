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
import static org.junit.Assert.*;

/**
 * Test class for User Manager.
 * 
 * @author Vojcek
 */
public class UserManagerTest { 
    private static Connection con;
    
    /**
     * UserManagerTest constructor.
     */
    public UserManagerTest() {
    }
    
    /**
     * SetUp class before testing.
     */
    @BeforeClass
    public static void setUpClass() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://den1.mysql3.gear.host:3306/pjvdb?user=" + Config.DBUSER +
                "&password="+ Config.DBPASSWORD +"&" + Config.DBSETINGS);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Test of createPasswordHash method, of class UserManager.
     */
    @org.junit.Test
    public void testCreatePasswordHashEmpty() {
        System.out.println("createPasswordHash - Empty");
        
        String pswd = "";
        UserManager instance = new UserManagerImpl();
        String expResult = "";
        
        String result = instance.createPasswordHash(pswd);
        
        // hash of emptz string should be emptz string
        assertEquals(expResult, result);
    }
    
     /**
     * Test of createPasswordHash method, of class UserManager.
     */
    @org.junit.Test
    public void testCreatePasswordHashComplex() {
        System.out.println("createPasswordHash - Complex");
        
        String pswd = "TestMD5HashFunction";
        UserManager instance = new UserManagerImpl();
        String expResult = "f720be86b53973998990704c21d9af1d";
        
        String result = instance.createPasswordHash(pswd);
        
        // compare wit prehashed result from internet
        assertEquals(expResult, result);
    }

    /**
     * Test of replaceAccents method, of class UserManager.
     */
    @org.junit.Test
    public void testReplaceAccentsEmptyString() {
        System.out.println("replaceAccents - Empty");
        
        String input = "";
        String expResult = "";
        String result = UserManager.replaceAccents(input);
        
        // replacing in empty string shouldnt mess with anything
        assertEquals(expResult, result);
    }
    
    /**
     * Test of replaceAccents method, of class UserManager.
     */
    @org.junit.Test
    public void testReplaceAccentsAllPossible() {
        System.out.println("replaceAccents - All");
        
        String input = "ěščřžýáíéďťůúňó";
        String expResult = "escrzyaiedtuuno";
        
        String result = UserManager.replaceAccents(input);
        
        // we should replace all characters with their non-accentic version
        assertEquals(expResult, result);
    }
    
    /**
     * Test of replaceAccents method, of class UserManager.
     */
    @org.junit.Test
    public void testReplaceAccentsComplexName() {
        System.out.println("replaceAccents - Complex");
        
        String input = "příliš žluťoučký kůň úpěl ďábelské ódy";
        String expResult = "prilis zlutoucky kun upel dabelske ody";
        
        String result = UserManager.replaceAccents(input);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of addLogin method, of class UserManager.
     */
    @org.junit.Test
    public void testAddLogin() {
        System.out.println("Add login");
        UserManager instance = new UserManagerImpl(con);
        
        // add login and get inserted ID
        long result = instance.addLogin("username", "password", 1);
        
        // ID shouldn't be 0
        assertNotEquals(0, result);
        
        // delete inserted login
        instance.deleteLogin(Long.toString(result));
    }
    
    /**
     * Test of deleteLogin method, of class UserManager.
     */
    @org.junit.Test
    public void testDeleteLogin() {
        System.out.println("replaceAccents - Complex");
        
        UserManagerImpl instance = new UserManagerImpl(con);
        
        // insert ID, get it's id and delete it
        long ID = instance.addLogin("username", "password", 1);
        instance.deleteLogin(Long.toString(ID));
        
        // try to select id with now nonexisting ID
        String result = instance.selectLoginUsername(Long.toString(ID));
        
        // result should be empty
        assertEquals("", result);
    }
    
    /**
     * UserManager Implementation for tests with few more functions.
     */
    private class UserManagerImpl extends UserManager {
        private UserManagerImpl() {
        }
        
        private UserManagerImpl(Connection con) {
            this.con = con;
        }
        
        private String selectLoginUsername(String ID_login) {
            String name = "";
        
            try {
                PreparedStatement q = con.prepareStatement("SELECT username FROM login WHERE ID_login = ?");
                q.setString(1, ID_login);

                ResultSet results = q.executeQuery();
                while(results.next()) {
                    name = results.getObject(1).toString();
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            return name;
       }
    }
}
