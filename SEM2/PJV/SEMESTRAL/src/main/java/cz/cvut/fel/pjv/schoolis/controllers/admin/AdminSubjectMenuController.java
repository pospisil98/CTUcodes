package cz.cvut.fel.pjv.schoolis.controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import cz.cvut.fel.pjv.schoolis.models.SubjectManager;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * AdminSubjectMenuController class
 *
 * @author Vojcek
 */
public class AdminSubjectMenuController implements Initializable {
    private SubjectManager subjectManager;
    private TeacherManager teacherManager;
    
    @FXML
    private JFXButton subjectInfoButton;
    @FXML
    private JFXButton createSubjectButton;
    @FXML
    private JFXComboBox<String> subjectComboBox;
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
     * This version is used when accessing menu from main menu (for first time).
     * 
     * @param connection    database connection
     */
    public void initData(Connection connection) {
        this.subjectManager = new SubjectManager(connection);
        this.teacherManager = new TeacherManager(connection);
        
        setComboBoxData();
    }
    
    /**
     * Initiation of class attributes from outside.
     * 
     * This version is used when accessing menu from inside of application.
     * 
     * @param subjectManager    instance of class SubjectManager
     * @param teacherManager    instance of class TeacherManager
     */
    public void initData(SubjectManager subjectManager, TeacherManager teacherManager) {
        this.subjectManager = subjectManager;
        this.teacherManager = teacherManager;
        setComboBoxData();
    }
    
    private void setComboBoxData() {
        subjectComboBox.getItems().clear();
        subjectComboBox.getItems().addAll(subjectManager.getSubjectsAndTeachers(teacherManager));
        subjectComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void subjectInfoButtonClicked(ActionEvent event) {
        String selectedSubjectName = subjectComboBox.getSelectionModel().getSelectedItem();
        if(selectedSubjectName != null)
        {
            try {           
                FXMLLoader loader = new FXMLLoader(
                   getClass().getResource(
                     "/fxml/admin/adminSubjectInfo.fxml"
                   )
                );
                Stage stage = new Stage();
                stage.setTitle("Informace o předmětu");
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setScene(new Scene((Pane) loader.load()));

                AdminSubjectInfoController c = loader.<AdminSubjectInfoController>getController();
                String[] split = selectedSubjectName.split(" ");
                c.initData(this.subjectManager, subjectManager.getSubjectIdByNameAndTeacher(split[0], split[1], split[2]), teacherManager);

                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("V databázi zatím není žádný předmět.");
            alert.showAndWait();
        }
    }

    @FXML
    private void createSubjectButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
               getClass().getResource(
                 "/fxml/admin/adminNewSubject.fxml"
               )
            );
           
            Stage stage = new Stage();
            stage.setTitle("Nový předmět");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            
            AdminNewSubjectController c = loader.<AdminNewSubjectController>getController();
            c.initData(subjectManager, teacherManager);
            
            stage.show();
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
                  "/fxml/admin/adminMenu.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            
            AdminMenuController c = loader.<AdminMenuController>getController();
            c.initData(this.subjectManager.getConnection());
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
