package cz.cvut.fel.pjv.schoolis.controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.TeacherManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
 * AdminNewClassController class
 *
 * @author Vojcek
 */
public class AdminNewClassController implements Initializable {
    private ClassManager classManager;
    private TeacherManager teacherManager;
    private boolean nameValid;
    private boolean yearValid;
    
    private static final String yearRegEx = "^\\d{4}$";
    
    @FXML
    private JFXButton newClassButton;
    @FXML
    private JFXComboBox<String> teacherComboBox;
    @FXML
    private JFXTextField nameInput;
    @FXML
    private JFXTextField yearInput;
    @FXML
    private JFXButton backButton;

    /**
     * Initializes the controller class.
     * 
     * Adds listener for every input which disables form sending
     * when anything is filled incorrectly (empty).
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(nameInput.getText().trim().isEmpty() || nameInput.getText().length() > 10)
                    {
                        nameInput.setStyle("-fx-border-color: red;");
                        nameValid = false;
                    } else {
                        nameInput.setStyle("-fx-border-color: none;");
                        nameValid = true;
                    }
                }
            }
        });
        
        yearInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(yearInput.getText().trim().isEmpty() || !yearInput.getText().matches(yearRegEx))
                    {
                        yearInput.setStyle("-fx-border-color: red;");
                        yearValid = false;
                    } else {
                        yearInput.setStyle("-fx-border-color: none;");
                        yearValid = true;
                    }
                }
            }
        });
    }  
    
    /**
     * Initiation of class attributes from outside.
     * 
     * @param classManager instance of class manager
     */
    public void initData(ClassManager classManager) {
        this.classManager = classManager;
        teacherManager = new TeacherManager(classManager.getConnection());
        
        setComboBoxData();
        
        // default state for form checking
        nameValid = yearValid = false;
    }
    
    private void setComboBoxData() {
        teacherComboBox.getItems().clear();
        teacherComboBox.getItems().addAll(teacherManager.getAllTeachersNames());
        teacherComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void newClassButtonClicked(ActionEvent event) {
        Alert alert;
        if(teacherComboBox.getSelectionModel().getSelectedItem() != null)
        {
            // Only when inputs are correct we can ad the class
            if(nameValid && yearValid) {
                String[] name = teacherComboBox.getSelectionModel().getSelectedItem().split(" ");
                String teacherID = teacherManager.getIdFromName(name[0], name[1]);

                int added = classManager.addClass(nameInput.getText(), yearInput.getText(), teacherID);

                if(added != 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Třídy byla úspěšně přidána");
                } else {
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Něco se pokazilo :(");
                }

                alert.setTitle("Tvorba třídy");
                alert.setHeaderText(null);

                alert.showAndWait();
                ((Node)(event.getSource())).getScene().getWindow().hide();

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
                    c.initData(this.classManager.getConnection());

                    stage.show();
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Chybně vyplněný formulář");
                alert.setHeaderText("Vyplňte prosím formulář validními údaji.");
                alert.showAndWait();
            }
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("V systému zatím není žádný učitel. Pro vytvoření nové třídy musíte nejprve vytvořit učitele.");
            alert.showAndWait();
        }
    }

    @FXML
    private void backButtonClicked(ActionEvent event) {
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
            c.initData(this.classManager.getConnection());
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
