package cz.cvut.fel.pjv.schoolis.controllers.admin;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * AdminMenuController class.
 *
 * @author Vojcek
 */
public class AdminMenuController implements Initializable {
    private Connection con;
    
    @FXML
    private JFXButton classButton;
    @FXML
    private JFXButton subjectButton;
    @FXML
    private JFXButton teacherButton;

    /**
     * Initializes the controller class.
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
     * @param connection    database connection
     */
    public void initData(Connection connection) {
        this.con = connection;
    }

    @FXML
    private void classButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
               getClass().getResource(
                 "/fxml/admin/adminClassMenu.fxml"
               )
            );
           
            Stage stage = new Stage();
            stage.setTitle("Menu třídy");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            
            AdminClassMenuController c = loader.<AdminClassMenuController>getController();
            c.initData(this.con);
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void subjectButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
               getClass().getResource(
                 "/fxml/admin/adminSubjectMenu.fxml"
               )
            );
           
            Stage stage = new Stage();
            stage.setTitle("Menu předmětu");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
           
            AdminSubjectMenuController c = loader.<AdminSubjectMenuController>getController();
            c.initData(this.con);
            
            stage.show();
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
                 "/fxml/admin/adminTeacherMenu.fxml"
               )
            );
           
            Stage stage = new Stage();
            stage.setTitle("Menu učitele");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            
            AdminTeacherMenuController c = loader.<AdminTeacherMenuController>getController();
            c.initData(this.con);
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
