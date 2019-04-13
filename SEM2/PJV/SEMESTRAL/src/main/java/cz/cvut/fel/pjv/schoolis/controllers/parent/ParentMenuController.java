package cz.cvut.fel.pjv.schoolis.controllers.parent;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import cz.cvut.fel.pjv.schoolis.models.ParentManager;
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
 * @author Karel
 */
public class ParentMenuController implements Initializable {
    private ParentManager parentManager;
    
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
     * @param ID_login  user ID
     * @param con       database connection
     */
    public void initData(String ID_login, Connection con) {
        this.parentManager = new ParentManager(ID_login, con);
        
        setComboBoxData();
    }
    
    /**
     * Initiation of class attributes from outside. 
     * 
     * This version is used when accessing the menu from inside of application.
     * 
     * @param manager   instance of parent manager
     */
    public void initData(ParentManager manager) {
        this.parentManager = manager;
        
        setComboBoxData();
    }
    
    private void setComboBoxData() {
        childComboBox.getItems().clear();
        childComboBox.getItems().addAll(parentManager.getChildren());
        childComboBox.getSelectionModel().select(0);
        
    }

    @FXML
    private void classificationButtonClicked(ActionEvent event) {
        try {
             FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/parent/parentClassification.fxml"
                )
              );
            //root = FXMLLoader.load(getClass().getResource("/fxml/parent/parentClassification.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Klasifikace");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            
            ParentClassificationController c = loader.<ParentClassificationController>getController();
            parentManager.setChild(childComboBox.getSelectionModel().getSelectedItem());
            c.initData(parentManager);
            
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
                  "/fxml/parent/parentAbsence.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Absence");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
            
            ParentAbsenceController c = loader.<ParentAbsenceController>getController();
            parentManager.setChild(childComboBox.getSelectionModel().getSelectedItem());
            c.initData(parentManager);
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
                  "/fxml/parent/parentTeachers.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Seznam učitelů");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
            
            ParentTeachersController c = loader.<ParentTeachersController>getController();
            parentManager.setChild(childComboBox.getSelectionModel().getSelectedItem());
            c.initData(parentManager);
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
                  "/fxml/parent/parentInfo.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Osobní údaje");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            
            ParentInfoController c = loader.<ParentInfoController>getController();
            parentManager.setChild(childComboBox.getSelectionModel().getSelectedItem());
            c.initData(parentManager);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void childComboBoxClicked(ActionEvent event) {
    }
    
}
