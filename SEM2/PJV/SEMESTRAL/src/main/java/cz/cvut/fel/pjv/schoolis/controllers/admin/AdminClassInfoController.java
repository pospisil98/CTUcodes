package cz.cvut.fel.pjv.schoolis.controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.StudentManager;
import cz.cvut.fel.pjv.schoolis.models.TeacherManager;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
 * AdminClassInfoController class.
 * 
 * This responsible for handling class info.
 * 
 * @author Vojcek
 */
public class AdminClassInfoController implements Initializable {
    private ClassManager classManager; 
    private TeacherManager teacherManager;
    private StudentManager studentManager;
    
    @FXML
    private JFXButton newStudentButton;
    @FXML
    private JFXComboBox<String> studentComboBox;
    @FXML
    private JFXButton infoStudentButton;
    @FXML
    private Label classNameLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private Label teacherLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label surnameLabel;
    @FXML
    private Label degreeLabel;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton deleteButton;

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
     * @param classManager class manager with data of class to be shown
     */
    public void initData(ClassManager classManager) {
        this.classManager = classManager;
        this.teacherManager = new TeacherManager(classManager.getConnection());
        this.studentManager = new StudentManager(classManager.getConnection());
        
        this.fillLabels();
        this.setComboBoxData();
    }
    
    private void fillLabels() {
        Map<String, String> data = classManager.getClassInfo();
        Map<String, String> tdata = teacherManager.getInfoForClass(data.get("ID_teacher"));

        classNameLabel.setText(data.get("name"));
        yearLabel.setText(data.get("start_year"));
        nameLabel.setText(tdata.get("name"));
        surnameLabel.setText(tdata.get("surname"));
        degreeLabel.setText(tdata.get("title"));   
    }
    
    private void setComboBoxData() {
        studentComboBox.getItems().clear();
        studentComboBox.getItems().addAll(classManager.getStudentsFromClass(classManager.getID_class()));
        studentComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void newStudentButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
               getClass().getResource(
                 "/fxml/admin/adminNewStudent.fxml"
               )
            );
            Stage stage = new Stage();
            stage.setTitle("Nový student");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            
            AdminNewStudentController c = loader.<AdminNewStudentController>getController();
            c.initData(classManager, studentManager);
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void infoStudentButtonClicked(ActionEvent event) {
        if(studentComboBox.getSelectionModel().getSelectedItem() != null)
        {
            try {         
                FXMLLoader loader = new FXMLLoader(
                   getClass().getResource(
                     "/fxml/admin/adminStudentInfo.fxml"
                   )
                );

                Stage stage = new Stage();
                stage.setTitle("Informace o studentovi");
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setScene(new Scene((Pane) loader.load()));

                AdminStudentInfoController c = loader.<AdminStudentInfoController>getController();
                String[] student = studentComboBox.getSelectionModel().getSelectedItem().split(" ");

                c.initData(this.classManager, this.studentManager,
                        studentManager.getStudentIdFromNameSurnameClassID(student[0],
                                student[1], classManager.getID_class())
                );

                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("V této třídě zatím není žádný student.");
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

    @FXML
    private void deleteButtonClicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrzení");
        alert.setHeaderText("Smazání třídy a jejích studentů");
        alert.setContentText("Jste si jisti? Smažete veškeré studenty a rodiče náležící k dané třídě.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // get ids of students from this class
            List<String> studentIDs = classManager.getStudentFromClassIDs(classManager.getID_class());
            
            // delete every student in class
            for (String id : studentIDs) {
                studentManager.deleteStudent(id);
            }
            
            classManager.deleteClass(classManager.getID_class());
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
            Alert alertInner = new Alert(Alert.AlertType.INFORMATION);
            alertInner.setTitle("Smazání třídy a jejích studentů");
            alertInner.setHeaderText(null);
            alertInner.setContentText("Třída včetně studentů a rodičů byla úspěšně smazána");
            alertInner.showAndWait();
            
            // return back after modal closing
            backButtonClicked(event);
        }
    }
}
