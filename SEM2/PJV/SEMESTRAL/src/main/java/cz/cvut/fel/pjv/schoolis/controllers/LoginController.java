/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.schoolis.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import cz.cvut.fel.pjv.schoolis.models.LoginManager;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author Vojcek
 */

public class LoginController implements Initializable {

    private LoginManager lm;
    
    @FXML
    private JFXTextField usernameText;
    @FXML
    private JFXPasswordField passwordText;
    @FXML
    private JFXButton loginButton;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.lm = new LoginManager();
    }

    @FXML
    void loginButtonClicked(ActionEvent event) {
        System.out.println("HERE AM I");
        
        if(login(usernameText.getText(), passwordText.getText()) == true) {
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("/fxml/student/studentMenu.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Menu");
                stage.setScene(new Scene(root));
                stage.show();
                // Hide this current window (if this is what you want)
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            passwordText.setStyle("-fx-background-color: red;");
            System.out.println("User neexistuje");
        }
    }
    
    private boolean login(String username, String password) {
        System.out.println("LOGGING");
        
        List<Map<String,String>> data = lm.getUserByUsername(username);
        if(data.get(0).get("username") == null) {
            return false;
        }
        if(!getPasswordHash(password).equals(data.get(0).get("hash"))) {
            System.out.println("NO");
            return false;
        }
        
        return true;
    }
    
    private String getPasswordHash(String pswd) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(pswd.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        System.out.println(generatedPassword);
        
        return generatedPassword;
    }
}