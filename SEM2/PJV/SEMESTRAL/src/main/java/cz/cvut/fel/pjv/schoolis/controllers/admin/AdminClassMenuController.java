package cz.cvut.fel.pjv.schoolis.controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
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
 * FXML Controller class
 *
 * @author Vojcek
 */
public class AdminClassMenuController implements Initializable {
    private ClassManager classManager;
    
    @FXML
    private JFXButton editClassButton;
    @FXML
    private JFXButton createClassButton;
    @FXML
    private JFXComboBox<String> classComboBox;
    @FXML
    private JFXButton backButton;

    /**
     * Initialize the controller class.
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
     * @param con   database connection
     */
    public void initData(Connection con) {
        this.classManager = new ClassManager(con);
        setComboBoxData();
    }
    
    private void setComboBoxData() {
        classComboBox.getItems().clear();
        classComboBox.getItems().addAll(classManager.getAllClassesNames());
        classComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void editClassButtonClicked(ActionEvent event) {
        String selectedClassName = classComboBox.getSelectionModel().getSelectedItem();
        if(selectedClassName != null)
        {
            try {           
                FXMLLoader loader = new FXMLLoader(
                   getClass().getResource(
                     "/fxml/admin/adminClassInfo.fxml"
                   )
                );

                Stage stage = new Stage();
                stage.setTitle("Informace o třídě");
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setScene(new Scene((Pane) loader.load()));

                AdminClassInfoController c = loader.<AdminClassInfoController>getController();

                this.classManager.setID_class(classManager.getClassIdByName(selectedClassName));
                c.initData(this.classManager);

                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("V databázi zatím není žádná třída.");
            alert.showAndWait();
        }
    }

    @FXML
    private void createClassButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
               getClass().getResource(
                 "/fxml/admin/adminNewClass.fxml"
               )
            );
           
            Stage stage = new Stage();
            stage.setTitle("Nová třída");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            
            AdminNewClassController c = loader.<AdminNewClassController>getController();
            c.initData(this.classManager);
            
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
            c.initData(this.classManager.getConnection());
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
