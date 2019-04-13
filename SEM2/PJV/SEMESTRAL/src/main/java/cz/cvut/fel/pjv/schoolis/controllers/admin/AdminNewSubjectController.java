package cz.cvut.fel.pjv.schoolis.controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.SubjectManager;
import cz.cvut.fel.pjv.schoolis.models.TeacherManager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
 * AdminNewSubjectController class
 *
 * @author Vojcek
 */
public class AdminNewSubjectController implements Initializable {
    private SubjectManager subjectManager;
    private TeacherManager teacherManager;
    private ClassManager classManager;
    private boolean nameValid;
    private boolean abbreviationValid;
    
    @FXML
    private JFXButton newSubjectButton;
    @FXML
    private JFXTextField nameInput;
    @FXML
    private JFXTextField abbreviationInput;
    @FXML
    private JFXComboBox<String> teacherComboBox;
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
                    if(nameInput.getText().trim().isEmpty() || nameInput.getText().length() > 60)
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
        
        abbreviationInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(abbreviationInput.getText().trim().isEmpty() || abbreviationInput.getText().length() > 3)
                    {
                        abbreviationInput.setStyle("-fx-border-color: red;");
                        abbreviationValid = false;
                    } else {
                        abbreviationInput.setStyle("-fx-border-color: none;");
                        abbreviationValid = true;
                    }
                }
            }
        });
    }    
    
    /**
     * Initiation of class attributes from outside.  
     * 
     * @param subjectManager    subject manager instance
     * @param teacherManager    teacher manager instance
     */
    public void initData(SubjectManager subjectManager, TeacherManager teacherManager) {
        this.subjectManager = subjectManager;
        this.teacherManager = teacherManager;
        this.classManager = new ClassManager(teacherManager.getConnection());
        
        setComboBoxData();
    }
    
    private void setComboBoxData() {
        teacherComboBox.getItems().clear();
        teacherComboBox.getItems().addAll(teacherManager.getAllTeachersNames());
        teacherComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void newSubjectButtonClicked(ActionEvent event) {
        Alert alert;
        if(teacherComboBox.getSelectionModel().getSelectedItem() != null)
        {
            int added = 0;
            if(nameValid && abbreviationValid) {
                List<String> data = new ArrayList<>();
                data.add(nameInput.getText());
                data.add(abbreviationInput.getText());
                data.add(teacherManager.getIdFromName(teacherComboBox.getSelectionModel().getSelectedItem().split(" ")[0], teacherComboBox.getSelectionModel().getSelectedItem().split(" ")[1]));
                added = subjectManager.addSubject(data);

                if(added != 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Předmět byl úspěšně přidán");
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                } else {
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Něco se pokazilo :(");
                }
                alert.setTitle("Tvorba předmětu");
                alert.setHeaderText(null);
                alert.showAndWait();

                // return back to menu
                backButtonClicked(event);
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Chybně vyplněný formulář");
                alert.setHeaderText("Vyplňte prosím formulář validními údaji.");
                alert.showAndWait();
            }
        } else {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("V systému zatím není žádná třída. Pro vytvoření nového předmětu musíte nejprve vytvořit třídu.");
            alert.showAndWait(); 
        }
    }

    @FXML
    private void backButtonClicked(ActionEvent event) {
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
            c.initData(this.subjectManager, this.teacherManager);
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }  
}
