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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * AdminSubjectInfoController class
 *
 * @author Vojcek
 */
public class AdminSubjectInfoController implements Initializable {
    private SubjectManager subjectManager;
    private ClassManager classManager;
    private TeacherManager teacherManager;
    private String subjectID;
    
    @FXML
    private Label nameLabel;
    @FXML
    private Label abbreviationLabel;
    @FXML
    private Label teacherLabel;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private JFXComboBox<String> classComboBox;
    @FXML
    private JFXComboBox<String> classesComboBox;
    @FXML
    private JFXButton addClassButton;
    @FXML
    private JFXButton removeClassButton;
    
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
     * @param subjectManager    instance of subject manager
     * @param subjectID         ID of subject to be displayed
     * @param teacherManager    instance of teacher manager
     */
    public void initData(SubjectManager subjectManager, String subjectID, TeacherManager teacherManager) {
        this.subjectManager = subjectManager;
        this.teacherManager = teacherManager;
        this.classManager = new ClassManager(teacherManager.getConnection());
        this.subjectID = subjectID;
        this.fillLabels();   
        this.setComboBoxData();
    }

    private void fillLabels() {
        Map<String, String> data = subjectManager.getSubjectInfo(subjectID);
        String teacherName = teacherManager.getNameFromId(data.get("ID_teacher"));
        nameLabel.setText(data.get("name"));
        abbreviationLabel.setText(data.get("abbreviation"));
        teacherLabel.setText(teacherName);
    }
    
    private void setComboBoxData() {
        classComboBox.getItems().clear();
        classComboBox.getItems().addAll(subjectManager.getClassesByIdSubject(subjectID));
        classComboBox.getSelectionModel().select(0);
        
        classesComboBox.getItems().clear();
        classesComboBox.getItems().addAll(classManager.getAllClassesNames());
        classesComboBox.getSelectionModel().select(0);
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
            stage.show();
            
            AdminSubjectMenuController c = loader.<AdminSubjectMenuController>getController();
            c.initData(this.subjectManager, this.teacherManager);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButtonClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrzení");
        alert.setHeaderText("Smazání předmětu");
        alert.setContentText("Jste si jisti?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            this.subjectManager.deleteSubject(this.subjectID);
            ((Node)(event.getSource())).getScene().getWindow().hide();
            
            Alert alertInner = new Alert(AlertType.INFORMATION);
            alertInner.setTitle("Smazání předmětu");
            alertInner.setHeaderText(null);
            alertInner.setContentText("Předmět byl úspěšně smazán");
            alertInner.showAndWait();
            
            backButtonClicked(event);
        }
    }

    @FXML
    private void addClassButtonClicked(ActionEvent event) {
        if(classesComboBox.getSelectionModel().getSelectedItem() != null)
        {
            this.subjectManager.addSubjectClassRelation(subjectID, classManager.getClassIdByName(classesComboBox.getSelectionModel().getSelectedItem().toString()));
            setComboBoxData();
        } else {
            Alert alert;
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("V systému zatím nejsou žádné třídy.");
            alert.showAndWait(); 
        }
    }

    @FXML
    private void removeClassButtonClicked(ActionEvent event) {
        if(classComboBox.getSelectionModel().getSelectedItem() != null)
        {
            this.subjectManager.removeSubjectClassRelation(subjectID, classManager.getClassIdByName(classComboBox.getSelectionModel().getSelectedItem().toString()));
            setComboBoxData();
        } else {
            Alert alert;
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Tento předmět nemá přiřazenou žádnou třídu.");
            alert.showAndWait(); 
        }
    }
}
