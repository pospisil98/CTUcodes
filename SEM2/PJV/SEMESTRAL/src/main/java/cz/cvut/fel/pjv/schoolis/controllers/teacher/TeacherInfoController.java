package cz.cvut.fel.pjv.schoolis.controllers.teacher;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vojcek
 */
public class TeacherInfoController implements Initializable {
    private TeacherManager teacherManager;
    private ClassManager classManager;

    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label houseNumberLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label degreeLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXComboBox<String> classComboBox;
    @FXML
    private JFXComboBox<String> subjectComboBox;

    /**
     * Initializes the controller class.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    /**
     * Initiation of class attributes from outside.
     * 
     * This method is used when accessing the controller from the menu.
     * 
     * @param teacherManager    instance of class TeacherManager
     */
    public void initData(TeacherManager teacherManager) {
        this.teacherManager = teacherManager;
        this.classManager = new ClassManager(teacherManager.getConnection());
        
        this.fillInfo();
    }

    private void fillInfo() {
        Map<String, String> sData = teacherManager.getTeacherInfo();
        nameLabel.setText(sData.get("name"));
        surnameLabel.setText(sData.get("surname"));
        degreeLabel.setText(sData.get("title"));
        streetLabel.setText(sData.get("street"));
        houseNumberLabel.setText(sData.get("postcode"));
        cityLabel.setText(sData.get("city"));
        emailLabel.setText(sData.get("mail"));
        phoneLabel.setText(sData.get("phone"));
        setComboBoxData();
    }
    
    private void setComboBoxData() {
        subjectComboBox.getItems().clear();
        subjectComboBox.getItems().addAll(teacherManager.getAllTeacherSubjects(teacherManager.getID_teacher()));
        subjectComboBox.getSelectionModel().select(0);
        classComboBox.getItems().clear();
        classComboBox.getItems().addAll(teacherManager.getAllTeachersClasses(teacherManager.getID_teacher()));
        classComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void backButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/teacher/teacherMenu.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
                        
            TeacherMenuController c = loader.<TeacherMenuController>getController();
            c.initData(teacherManager);
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
