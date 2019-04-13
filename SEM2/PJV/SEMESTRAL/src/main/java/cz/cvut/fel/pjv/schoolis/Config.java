package cz.cvut.fel.pjv.schoolis;

/**
 * File with all important constants for this project.
 * 
 * @author Vojcek
 */
public class Config {
    
    /**
     * Username for database connection.
     */
    public static final String  DBUSER = "pjvdb";
    
    /**
     * Password for database connection.
     */
    public static final String  DBPASSWORD = "Fr4HVYaY!k!5";
    
    /**
     * All settings used for database connection.
     */
    public static final String  DBSETINGS = "zeroDateTimeBehavior=CONVERT_TO_NULL";
    
    /**
     * Administrator role value in database.
     */
    public static final int     ADMINROLE = 1;
    
    /**
     * Teacher role value in database.
     */
    public static final int     TEACHERROLE = 2;
    
    /**
     * Student role value in database.
     */
    public static final int     STUDENTROLE = 3;
    
    /**
     * Parent role value in database.
     */
    public static final int     PARENTROLE = 4;
    
    /**
     * Length of name part in login.
     */
    public static final int     LOGINNAMEPARTLEN = 2;
    
    /**
     * Length of surname part in login.
     */
    public static final int     LOGINSURNAMEPARTLEN = 5;
    
    /**
     * Alphabet to generate password from.
     */
    public static final String  PSWDALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    
    /**
     * Length of generated password.
     */
    public static final int     PSWDLEN = 10;
    
    /**
     * Folder where to put generated logins.
     */
    public static final String  LOGINFOLDER = "./logins/";
}
