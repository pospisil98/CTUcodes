package cz.cvut.fel.pjv.schoolis.controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.SubjectManager;
import cz.cvut.fel.pjv.schoolis.models.TeacherManager;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * AdminTeacherInfoController class
 *
 * @author Vojcek
 */
public class AdminTeacherInfoController implements Initializable {
    private TeacherManager teacherManager;
    private SubjectManager subjectManager;
    private ClassManager classManager;
    private String teacherID;
    
    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label degreeLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label housenumberLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label mailLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private JFXComboBox<String> subjectComboBox;
    @FXML
    private JFXComboBox<String> classComboBox;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton deleteButton;
    
    /**
     * Initializes the controller class.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    /**
     * Initiation of class attributes from outside. 
     * 
     * @param teacherManager    instance of teacher manager
     */
    public void initData(TeacherManager teacherManager) {
        this.teacherManager = teacherManager;
        
        fillLabels(this.teacherManager.getTeacherInfo(this.teacherManager.getID_teacher()));
    }
    
    private void fillLabels(Map<String, String> data) {
        this.teacherID = data.get("ID_teacher");
        nameLabel.setText(data.get("name"));
        surnameLabel.setText(data.get("surname"));
        degreeLabel.setText(data.get("title"));
        streetLabel.setText(data.get("street"));
        housenumberLabel.setText(data.get("postcode"));
        cityLabel.setText(data.get("city"));
        mailLabel.setText(data.get("mail"));
        phoneLabel.setText(data.get("phone"));
        
        setSubjectComboBoxData();
        setClassComboBoxData();
    }

    private void setSubjectComboBoxData() {
        subjectComboBox.getItems().clear();
        subjectComboBox.getItems().addAll(teacherManager.getAllTeacherSubjects(this.teacherID));
        subjectComboBox.getSelectionModel().select(0);
    }

    private void setClassComboBoxData() {
        classComboBox.getItems().clear();
        classComboBox.getItems().addAll(teacherManager.getAllTeachersClasses(this.teacherID));
        classComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void backButtonClicked(ActionEvent event) {
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
            c.initData(this.teacherManager);
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButtonClicked(ActionEvent event) {
        if(this.classComboBox.getSelectionModel().getSelectedItem() == null && this.subjectComboBox.getSelectionModel().getSelectedItem() == null)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrzení");
            alert.setHeaderText("Smazání učitele");
            alert.setContentText("Jste si jisti?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                this.teacherManager.deleteTeacher(this.teacherID);

                ((Node)(event.getSource())).getScene().getWindow().hide();

                Alert alertInner = new Alert(Alert.AlertType.INFORMATION);
                alertInner.setTitle("Smazání učitele");
                alertInner.setHeaderText(null);
                alertInner.setContentText("Učitel byl úspěšně smazán");
                alertInner.showAndWait();

                // return to previous menu
                backButtonClicked(event);
            }
        } else {
            Alert alert;
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Pro smazání učitele nejprve smažte všechny jeho předměty a třídy.");
            alert.showAndWait(); 
        }
    }  
}
