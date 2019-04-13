package cz.cvut.fel.pjv.schoolis.controllers.teacher;

import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.AbsenceManager;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.StudentManager;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vojcek
 */
public class TeacherStudentNewAbsenceController implements Initializable {
    private TeacherManager teacherManager;
    private ClassManager classManager;
    private StudentManager studentManager;
    private AbsenceManager absenceManager;
    private boolean dateValid;
    
    @FXML
    private Label studentLabel;
    @FXML
    private JFXButton addAbsenceButton;
    @FXML
    private DatePicker dateDatePicker;
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
        dateDatePicker.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(dateDatePicker.getValue() == null)
                    {
                        dateDatePicker.setStyle("-fx-border-color: red;");
                        dateValid = false;
                    } else {
                        dateDatePicker.setStyle("-fx-border-color: none;");
                        dateValid = true;
                    }
                }
            }
        });
    }    
    
    /**
     * Initiation of class attributes from outside.
     * 
     * This method is used when accessing the controller from the student menu.
     * 
     * @param teacherManager    instance of class TeacherManager
     * @param classManager      instance of class ClassManager
     * @param studentManager    instance of class StudentManager
     * @param absenceManager    instance of class AbsenceManager
     */
    public void initData(TeacherManager teacherManager, ClassManager classManager, StudentManager studentManager, AbsenceManager absenceManager) {
        this.teacherManager = teacherManager;
        this.classManager = classManager;
        this.studentManager = studentManager;
        this.absenceManager = absenceManager;
        
        studentLabel.setText(studentManager.getFullName());
        
        dateValid = false;
    }

    @FXML
    private void addAbsenceButtonClicked(ActionEvent event) {
        if(dateValid)
        {
            absenceManager.newAbsence(studentManager.getID_student(), dateDatePicker.getValue().toString());
            try {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                      "/fxml/teacher/teacherStudentAbsence.fxml"
                    )
                  );
                Stage stage = new Stage();
                stage.setTitle("Absence studenta");
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setScene(new Scene((Pane) loader.load()));
                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
                TeacherStudentAbsenceController c = loader.<TeacherStudentAbsenceController>getController();
                c.initData(teacherManager, classManager, studentManager, absenceManager);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chybně vyplněný formulář");
            alert.setHeaderText("Vyplňte prosím formulář validními údaji.");
            alert.showAndWait();
        }
    }

    @FXML
    private void backButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/teacher/teacherStudentAbsence.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Absence studenta");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
            TeacherStudentAbsenceController c = loader.<TeacherStudentAbsenceController>getController();
            c.initData(teacherManager, classManager, studentManager, absenceManager);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void dateDatePickerClicked(ActionEvent event) {
    }
    
}
