/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.schoolis.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karel
 */
public class AdminClassMenuController implements Initializable {

    @FXML
    private JFXButton editClassButton;
    @FXML
    private JFXButton createClassButton;
    @FXML
    private JFXComboBox<?> classComboBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void editClassButtonClicked(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/admin/adminClassInfoMenu.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Informace o třídě");
            stage.setScene(new Scene(root));
            
            stage.show();
            // Hide this current window (if this is what you want)
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void createClassButtonClicked(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/admin/adminNewClass.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Nová třída");
            stage.setScene(new Scene(root));
            
            stage.show();
            // Hide this current window (if this is what you want)
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}