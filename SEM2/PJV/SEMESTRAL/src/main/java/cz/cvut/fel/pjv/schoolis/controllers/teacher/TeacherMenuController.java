package cz.cvut.fel.pjv.schoolis.controllers.teacher;

import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.TeacherManager;
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
public class TeacherMenuController implements Initializable {
    private TeacherManager teacherManager;

    @FXML
    private JFXButton classButton;
    @FXML
    private JFXButton teachersButton;
    @FXML
    private JFXButton infoButton;

    /**
     * Initializes the controller class.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nothing to be initialized    
    }    
    
    /**
     * Initiation of class attributes from outside. 
     * 
     * This version is used when accessing the menu from the login form (for first time).
     * 
     * @param ID_login      user ID
     * @param connection    database connection
     */
    public void initData(String ID_login, Connection connection) {
        this.teacherManager = new TeacherManager(ID_login, connection);
    }
    
    /**
     * Initiation of class attributes from outside. 
     * 
     * This version is used when accessing the menu from inside of application.
     * 
     * @param teacherManager    instance of class TeacherManager
     */
    public void initData(TeacherManager teacherManager) {
        this.teacherManager = teacherManager;
    }

    @FXML
    private void classButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/teacher/teacherClass.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Menu třídy");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            TeacherClassController c = loader.<TeacherClassController>getController();
            c.initData(teacherManager);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }   
    }

    @FXML
    private void teachersButtonClicked(ActionEvent event) {
        try {
             FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/teacher/teacherTeachers.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Seznam kolegů");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            
            TeacherTeachersController c = loader.<TeacherTeachersController>getController();
            c.initData(teacherManager);
            
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
                  "/fxml/teacher/teacherInfo.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Osobní údaje");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            
            TeacherInfoController c = loader.<TeacherInfoController>getController();
            c.initData(teacherManager);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }   
    }
    
}
