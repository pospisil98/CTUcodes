package cz.cvut.fel.pjv.schoolis.controllers.teacher;

import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.StudentManager;
import cz.cvut.fel.pjv.schoolis.models.TeacherManager;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
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
public class TeacherStudentInfoController implements Initializable {
    private TeacherManager teacherManager;
    private ClassManager classManager;
    private StudentManager studentManager;

    @FXML
    private Label sNameLabel;
    @FXML
    private Label sSurnameLabel;
    @FXML
    private Label sBirthdateLabel;
    @FXML
    private Label sAssuranceLabel;
    @FXML
    private Label sStreetLabel;
    @FXML
    private Label sCityLabel;
    @FXML
    private Label sPhoneLabel;
    @FXML
    private Label sClassLabel;
    @FXML
    private Label pNameLabel;
    @FXML
    private Label pSurnameLabel;
    @FXML
    private Label pPhoneLabel;
    @FXML
    private Label sBirthNumLabel;
    @FXML
    private Label sHouseNumLabel;
    @FXML
    private Label sMailLabel;
    @FXML
    private Label pMailLabel;
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
     * This method is used when accessing the controller from the student menu.
     * 
     * @param teacherManager    instance of class TeacherManager
     * @param classManager      instance of class ClassManager
     * @param studentManager    instance of class StudentManager
     */
    public void initData(TeacherManager teacherManager, ClassManager classManager, StudentManager studentManager) {
        this.teacherManager = teacherManager;
        this.classManager = classManager;
        this.studentManager = studentManager;
        
        this.fillInfo();
    }
    
    private void fillInfo() {
        Map<String, String> sData = studentManager.getStudentInfo();
        sNameLabel.setText(sData.get("name"));
        sSurnameLabel.setText(sData.get("surname"));
        sBirthdateLabel.setText(sData.get("birth_date"));
        sBirthNumLabel.setText(sData.get("personal_identification_number"));
        sAssuranceLabel.setText(sData.get("assurance_company"));
        sStreetLabel.setText(sData.get("street"));
        sHouseNumLabel.setText(sData.get("postcode"));
        sCityLabel.setText(sData.get("city"));
        sMailLabel.setText(sData.get("mail"));
        sPhoneLabel.setText(sData.get("phone"));
        sClassLabel.setText(classManager.getClassNameById(sData.get("ID_class")));
        
        Map<String, String> pData = studentManager.getParentInfo();
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
                  "/fxml/teacher/teacherStudentMenu.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Menu student");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
            TeacherStudentMenuController c = loader.<TeacherStudentMenuController>getController();
            c.initData(teacherManager, classManager, studentManager);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
