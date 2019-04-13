package cz.cvut.fel.pjv.schoolis.controllers.teacher;

import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.StudentManager;
import cz.cvut.fel.pjv.schoolis.models.TeacherManager;
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
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vojcek
 */
public class TeacherStudentMenuController implements Initializable {
    private TeacherManager teacherManager;
    private ClassManager classManager;
    private StudentManager studentManager;

    @FXML
    private Label studentLabel;
    @FXML
    private JFXButton classificationButton;
    @FXML
    private JFXButton absenceButton;
    @FXML
    private JFXButton infoButton;
    @FXML
    private JFXButton backButton;

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
     * This method is used when accessing the menu from the class controller.
     * 
     * @param teacherManager    instance of class TeacherManager
     * @param classManager      instance of class ClassManager
     * @param studentManager    instance of class StudentManager
     */
    public void initData(TeacherManager teacherManager, ClassManager classManager, StudentManager studentManager) {
        this.teacherManager = teacherManager;
        this.classManager = classManager;
        this.studentManager = studentManager;
        studentLabel.setText(studentManager.getFullName());
    }

    @FXML
    private void classificationButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/teacher/teacherStudentClassification.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Klasifikace studenta");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            TeacherStudentClassificationController c = loader.<TeacherStudentClassificationController>getController();
            c.initData(teacherManager, classManager, studentManager);
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
                  "/fxml/teacher/teacherStudentAbsence.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Absence studenta");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            TeacherStudentAbsenceController c = loader.<TeacherStudentAbsenceController>getController();
            c.initData(teacherManager, classManager, studentManager);
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
                  "/fxml/teacher/teacherStudentInfo.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Údaje o studentovi");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            TeacherStudentInfoController c = loader.<TeacherStudentInfoController>getController();
            c.initData(teacherManager, classManager, studentManager);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backButtonClicked(ActionEvent event) {
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
            c.initData(teacherManager, classManager, studentManager);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
