package cz.cvut.fel.pjv.schoolis.controllers.teacher;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.ClassificationManager;
import cz.cvut.fel.pjv.schoolis.models.StudentManager;
import cz.cvut.fel.pjv.schoolis.models.SubjectManager;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Vojcek
 */
public class TeacherStudentNewClassificationController implements Initializable {
    private TeacherManager teacherManager;
    private ClassManager classManager;
    private StudentManager studentManager;
    private ClassificationManager classificationManager;
    private SubjectManager subjectManager;
    private boolean gradeValid;
    private boolean weightValid;
    private boolean descriptionValid;
    private boolean dateValid;
    
    @FXML
    private Label subjectLabel;
    @FXML
    private Label studentLabel;
    @FXML
    private JFXTextField gradeTextField;
    @FXML
    private JFXTextField weightTextField;
    @FXML
    private JFXTextField descriptionTextField;
    @FXML
    private JFXButton addGradeButton;
    @FXML
    private JFXButton backButton;
    @FXML
    private DatePicker dateDatePicker;

    /**
     * Initializes the controller class.
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gradeTextField.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(!gradeTextField.getText().matches("[1-5]") || gradeTextField.getText().trim().isEmpty())
                    {
                        gradeTextField.setStyle("-fx-border-color: red;");
                        gradeValid = false;
                    } else {
                        gradeTextField.setStyle("-fx-border-color: none;");
                        gradeValid = true;
                    }
                }
            }
        });
        
        weightTextField.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(!weightTextField.getText().matches("[1-9]|10") || weightTextField.getText().trim().isEmpty())
                    {
                        weightTextField.setStyle("-fx-border-color: red;");
                        weightValid = false;
                    } else {
                        weightTextField.setStyle("-fx-border-color: none;");
                        weightValid = true;
                    }
                }
            }
        });
        
        descriptionTextField.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(descriptionTextField.getText().trim().isEmpty())
                    {
                        descriptionTextField.setStyle("-fx-border-color: red;");
                        descriptionValid = false;
                    } else {
                        descriptionTextField.setStyle("-fx-border-color: none;");
                        descriptionValid = true;
                    }
                }
            }
        });
        
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
     * @param teacherManager        instance of class TeacherManager
     * @param classManager          instance of class ClassManager
     * @param studentManager        instance of class StudentManager
     * @param classificationManager instance of class ClassificationManager
     * @param subjectManager        instance of class SubjectManager
     */
    public void initData(TeacherManager teacherManager, ClassManager classManager, StudentManager studentManager, ClassificationManager classificationManager, SubjectManager subjectManager) {
        this.teacherManager = teacherManager;
        this.classManager = classManager;
        this.studentManager = studentManager;
        this.classificationManager = classificationManager;
        this.subjectManager = subjectManager;
        studentLabel.setText(studentManager.getFullName());
        subjectLabel.setText(subjectManager.getSubject());
        gradeValid = weightValid = descriptionValid = dateValid = false;
    }
    
    @FXML
    private void addGradeButtonClicked(ActionEvent event) {
        if(gradeValid && weightValid && descriptionValid && dateValid)
        {
            classificationManager.newGrade(gradeTextField.getText(), weightTextField.getText(), descriptionTextField.getText(), dateDatePicker.getValue().toString(), subjectManager.getID_subject(), studentManager.getID_student());
            try {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                      "/fxml/teacher/teacherStudentClassification.fxml"
                    )
                  );
                Stage stage = new Stage();
                stage.setTitle("Klasifikace studenta");
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setScene(new Scene((Pane) loader.load()));
                stage.show();
                TeacherStudentClassificationController c = loader.<TeacherStudentClassificationController>getController();
                c.initData(teacherManager, classManager, studentManager, classificationManager, subjectManager);
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(AlertType.ERROR);
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
                  "/fxml/teacher/teacherStudentClassification.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Klasifikace studenta");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            TeacherStudentClassificationController c = loader.<TeacherStudentClassificationController>getController();
            c.initData(teacherManager, classManager, studentManager, classificationManager, subjectManager);
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
