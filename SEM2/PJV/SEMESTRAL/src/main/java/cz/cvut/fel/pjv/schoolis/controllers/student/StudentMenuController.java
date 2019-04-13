package cz.cvut.fel.pjv.schoolis.controllers.student;

import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.StudentManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vojcek
 */
public class StudentMenuController implements Initializable {
    private StudentManager studentManager;

    @FXML
    private JFXButton classificationButton;
    @FXML
    private JFXButton absenceButton;
    @FXML
    private JFXButton teacherButton;
    @FXML
    private JFXButton infoButton;

    /**
     * 
     * Initializes the controller class.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nohing to be initialized
    }
    
    /**
     * Initiation of class attributes from outside. 
     * 
     * This version is used when accessing the menu from the login form (for first time).
     * 
     * @param ID_login  user ID
     * @param con       database connection
     */
    public void initData(String ID_login, Connection con) {
        this.studentManager = new StudentManager(ID_login, con);
    }

    /**
     * Initiation of class attributes from outside.
     * 
     * This version is used when accessing the menu from inside of application.
     * 
     * @param studentManager    instance of class StudentManager
     */
    public void initData(StudentManager studentManager) {
        this.studentManager = studentManager;
    }
    
    @FXML
    private void classificationButtonClicked(ActionEvent event) {
        try {
             FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/student/studentClassification.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Klasifikace");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            
            StudentClassificationController c = loader.<StudentClassificationController>getController();
            c.initData(studentManager);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void absenceButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/student/studentAbsence.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Absence");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            
            StudentAbsenceController c = loader.<StudentAbsenceController>getController();
            c.initData(studentManager);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void teacherButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/student/studentTeachers.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Seznam učitelů");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            
            StudentTeachersController c = loader.<StudentTeachersController>getController();
            c.initData(studentManager);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void infoButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/student/studentInfo.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Osobní údaje");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            
            StudentInfoController c = loader.<StudentInfoController>getController();
            c.initData(studentManager);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
