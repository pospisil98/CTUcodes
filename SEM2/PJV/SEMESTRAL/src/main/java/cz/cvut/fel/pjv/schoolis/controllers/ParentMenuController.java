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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karel
 */
public class ParentMenuController implements Initializable {

    @FXML
    private JFXComboBox<String> childComboBox;
    @FXML
    private JFXButton classificationButton;
    @FXML
    private JFXButton absenceButton;
    @FXML
    private JFXButton teachersButton;
    @FXML
    private JFXButton InfoButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setComboBoxData();
    }    
    
    public void setComboBoxData() {
        childComboBox.getItems().clear();

        childComboBox.getItems().addAll(
                    "Roman Vokál",
                    "Romanova Kamarádka");
        
        childComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void childComboBoxClicked(ActionEvent event) {
    }

    @FXML
    private void classificationButtonClicked(ActionEvent event) {
        Parent root;
        try {
             FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/parent/parentClassification.fxml"
                )
              );
            //root = FXMLLoader.load(getClass().getResource("/fxml/parent/parentClassification.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Klasifikace");
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            
            ParentClassificationController c = loader.<ParentClassificationController>getController();
            c.initData(childComboBox.getSelectionModel().getSelectedItem());

            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void absenceButtonClicked(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/parent/parentAbsence.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Absence");
            stage.setScene(new Scene(root));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void teachersButtonClicked(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/parent/parentTeachers.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Seznam učitelů");
            stage.setScene(new Scene(root));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void infoButtonClicked(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/parent/parentInfo.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Osobní údaje");
            stage.setScene(new Scene(root));
            
            stage.show();
            // Hide this current window (if this is what you want)
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
