package cz.cvut.fel.pjv.schoolis.models;

import cz.cvut.fel.pjv.schoolis.Config;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UserManager class.
 * 
 * Abstract class for inheritance in user oriented managers. 
 * 
 * @author Vojcek
 */
public abstract class UserManager {

    /**
     * Database connection
     */
    protected Connection con;
    
    /**
     * Creates unique username for login.
     * 
     * Creates username from first n characters from name and surname and 
     * appends number to make the username unique.
     * 
     * @param name      users name
     * @param surname   users surname
     * @return          generated unique username
     */
    public String createUsername(String name, String surname) {
        // strip most used czech special characters
        name = UserManager.replaceAccents(name);
        surname = UserManager.replaceAccents(surname);
        
        // start with number one and proceed to higher ones
        int number = 1;
        boolean usernameCorrect = false;
        String username = "";
        
        // take first n characters based on lenght in config
        String namePart = name.toLowerCase().substring(0, Math.min(name.length(), Config.LOGINNAMEPARTLEN));
        String surnamePart = surname.toLowerCase().substring(0, Math.min(surname.length(), Config.LOGINSURNAMEPARTLEN));
        
        while(!usernameCorrect) {
            // combine all parts together
            username = surnamePart + namePart + Integer.toString(number);
            
            // select created username from DB and if it exists then go with higher number
            try {
                PreparedStatement q = con.prepareStatement("SELECT ID_login FROM login WHERE username = ?");
                q.setString(1, username);
                ResultSet results = q.executeQuery();

                if(!results.isBeforeFirst()){
                    usernameCorrect = true;
                }
                else {
                    number++;
                }  
            } catch (SQLException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return username;
    }
    
    /**
     * Get username from user login ID.
     * 
     * @param loginID   login ID of user with username we want
     * @return          username we want
     */
    public String getUsernamefromLoginID(String loginID) {
        String username = "";
        try {
            PreparedStatement q = con.prepareStatement("SELECT username FROM login WHERE ID_login = ?");
            q.setString(1, loginID);
            
            ResultSet results = q.executeQuery();            
            while(results.next() ) {
                username = results.getString("username");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return username;
    }
    
    /**
     * Creates MD5 hash.
     * 
     * Creates MD5 hash of given string..
     * 
     * @param pswd  password to be hashed
     * @return      hash of password
     */
    protected String createPasswordHash(String pswd) {
        if ("".equals(pswd)) {
            return "";
        }
        
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Add password bytes to digest
            md.update(pswd.getBytes());
            // Get the hash's bytes
            byte[] bytes = md.digest();
            // This bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            // Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return generatedPassword;
    }
    
    /**
     * Inserts login into DB.
     * 
     * Inserts login into database and return its ID.
     * 
     * @param username  username of user
     * @param password  password of user
     * @param role      role of user
     * @return          ID of created login
     */
    public long addLogin(String username, String password, int role) {
        String hash = this.createPasswordHash(password);
        
        long loginID = 0;
        try {
            PreparedStatement q = con.prepareStatement("INSERT INTO login (username, hash, ID_role) VALUES (?, ?, ?)", new String[] {"ID_login"});
            q.setString(1, username);
            q.setString(2, hash);
            q.setString(3, Integer.toString(role));
            q.executeUpdate();
            
            // recieve last inserts ID
            ResultSet rs = q.getGeneratedKeys();
            if (rs.next()) {
               loginID = rs.getLong(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return loginID;
    }
    
    /**
     * Delete login from DB.
     * 
     * Deletes login with given ID from database.
     * 
     * @param loginID   ID of login to be deleted
     */
    public void deleteLogin(String loginID) {
        try {
            PreparedStatement q = con.prepareStatement("DELETE FROM login WHERE ID_login = ?");
            q.setString(1, loginID);
            q.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Generate random password.
     * 
     * Generates random password based on alphabet in Config and with specified length.
     * 
     * @param len   length of generated password
     * @return      generated password
     */
    public static String generatePassword(int len) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        
        for(int i = 0; i < len; i++ ) {
           sb.append(Config.PSWDALPHABET.charAt(rnd.nextInt(Config.PSWDALPHABET.length())));
        }
        
        return sb.toString();
    }
    
    /**
     * Replace accents in given string.
     * 
     * Dummy way to convert most of Czech alphabet to standard one. 
     * 
     * @param input
     * @return
     */
    public static String replaceAccents(String input) {
        String[] bef = {"ě", "š", "č", "ř", "ž", "ý", "á", "í", "é", "ď", "ť", "ů", "ú", "ň", "ó"};
        String[] aft = {"e", "s", "c", "r", "z", "y", "a", "i", "e", "d", "t", "u", "u", "n", "o"};
        
        for (int i = 0; i < bef.length; i++) {
            input = input.replace(bef[i], aft[i]);
        }
        
        return input;
    }
}
