/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.schoolis.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Vojcek
 */
public class TeacherStudentNewClassificationController implements Initializable {

    @FXML
    private Label subjectLabel;
    @FXML
    private Label studentLabel;
    @FXML
    private JFXTextField gradeTextField;
    @FXML
    private JFXTextField weightTextField;
    @FXML
    private JFXTextField descriptionTextField;
    @FXML
    private JFXButton addGradeButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addGradeButtonClicked(ActionEvent event) {
    }
    
}
