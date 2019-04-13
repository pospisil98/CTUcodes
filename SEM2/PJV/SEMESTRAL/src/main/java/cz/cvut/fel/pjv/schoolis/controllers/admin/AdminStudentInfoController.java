package cz.cvut.fel.pjv.schoolis.controllers.admin;

import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.ParentManager;
import cz.cvut.fel.pjv.schoolis.models.StudentManager;
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
 * AdminStudentInfoController class
 *
 * @author Vojcek
 */
public class AdminStudentInfoController implements Initializable {
    private ClassManager classManager;
    private StudentManager studentManager;
    private ParentManager parentManager;
    private String studentID;
    
    @FXML
    private Label sClassLabel;
    @FXML
    private Label sPhoneLabel;
    @FXML
    private Label sMailLabel;
    @FXML
    private Label sCityLabel;
    @FXML
    private Label sHousenumLabel;
    @FXML
    private Label sStreetLabel;
    @FXML
    private Label sAssuranceLabel;
    @FXML
    private Label sBirthnumberLabel;
    @FXML
    private Label sBirthdateLabel;
    @FXML
    private Label sSurnameLabel;
    @FXML
    private Label sNameLabel;
    @FXML
    private Label pNameLabel;
    @FXML
    private Label pSurnameLabel;
    @FXML
    private Label pMailLabel;
    @FXML
    private Label pPhoneLabel;
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
        // nothing to be initialized
    }    

    /**
     * Initiation of class attributes from outside. 
     * 
     * @param classManager      class manager instance
     * @param studentManager    student manager instance
     * @param studentId         id of student which should be displayed
     */
    public void initData(ClassManager classManager, StudentManager studentManager, String studentId) {
        this.classManager = classManager;
        this.studentManager = studentManager;
        this.studentID = studentId;
        this.parentManager = new ParentManager(classManager.getConnection());
        
        fillLabels(studentId);
    }

    private void fillLabels(String studentId) {
        Map<String, String> sData = studentManager.getStudentInfo(studentId);
        this.parentManager.setID_parent(sData.get("ID_parent"));
        
        sNameLabel.setText(sData.get("name"));
        sSurnameLabel.setText(sData.get("surname"));
        sBirthdateLabel.setText(sData.get("birth_date"));
        sBirthnumberLabel.setText(sData.get("personal_identification_number"));
        sAssuranceLabel.setText(sData.get("assurance_company"));
        sStreetLabel.setText(sData.get("street"));
        sHousenumLabel.setText(sData.get("postcode"));
        sCityLabel.setText(sData.get("city"));
        sMailLabel.setText(sData.get("mail"));
        sPhoneLabel.setText(sData.get("phone"));
        sClassLabel.setText(classManager.getClassNameById(sData.get("ID_class")));
        
        Map<String, String> pData = parentManager.getParentInfo();
        pNameLabel.setText(pData.get("name"));
        pSurnameLabel.setText(pData.get("surname"));
        pMailLabel.setText(pData.get("email"));
        pPhoneLabel.setText(pData.get("phone"));
    }

    @FXML
    private void backButtonClicked(ActionEvent event) {
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
            c.initData(classManager);
            
            stage.show();
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
        alert.setHeaderText("Smazání studenta a rodiče");
        alert.setContentText("Jste si jisti?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            this.studentManager.deleteStudent(studentID);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
            
            Alert alertInner = new Alert(Alert.AlertType.INFORMATION);
            alertInner.setTitle("Smazání studenta a rodiče");
            alertInner.setHeaderText(null);
            alertInner.setContentText("Student a rodič byli úspěšně smazáni");
            alertInner.showAndWait();
            
            backButtonClicked(event);
        }
    }
    
}
