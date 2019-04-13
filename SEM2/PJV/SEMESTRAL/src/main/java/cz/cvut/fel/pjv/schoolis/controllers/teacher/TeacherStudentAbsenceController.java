package cz.cvut.fel.pjv.schoolis.controllers.teacher;

import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.AbsenceManager;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.StudentManager;
import cz.cvut.fel.pjv.schoolis.models.TeacherManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Vojcek
 */
public class TeacherStudentAbsenceController implements Initializable {
    private TeacherManager teacherManager;
    private ClassManager classManager;
    private StudentManager studentManager;
    private AbsenceManager absenceManager;
    
    @FXML
    private Label studentLabel;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton addAbsenceButton;
    @FXML
    private JFXButton removeAbsenceButton;
    @FXML
    private TableView<String[]> absenceTable;
    @FXML
    private TableColumn<String[], String> dateColumn;
    @FXML
    private TableColumn<String[], String> apologizedColumn;

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
        this.absenceManager = new AbsenceManager(teacherManager.getConnection());
        
        setTableData();
        
        studentLabel.setText(studentManager.getFullName());
    }
    
    /**
     * Initiation of class attributes from outside.
     * 
     * This method is used when accessing the controller from inside of application.
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
        
        setTableData();
        
        studentLabel.setText(studentManager.getFullName());
    }

    private void setTableData() {
        ObservableList<String[]> data = this.absenceManager.getAbsences(studentManager.getID_student());
        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[1]));
                }
            });
        apologizedColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[2]));
                }
            });
        absenceTable.setItems(data);
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
            
            TeacherStudentMenuController c = loader.<TeacherStudentMenuController>getController();
            c.initData(teacherManager, classManager, studentManager);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addAbsenceButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/teacher/teacherStudentNewAbsence.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Nová absence");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            
            TeacherStudentNewAbsenceController c = loader.<TeacherStudentNewAbsenceController>getController();
            c.initData(teacherManager, classManager, studentManager, absenceManager);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void removeAbsenceButtonClicked(ActionEvent event) {
        if(absenceTable.getSelectionModel().getSelectedItem() != null) {
            this.absenceManager.removeAbsence(absenceTable.getSelectionModel().getSelectedItem()[0]);
            setTableData();
        }
    }
    
}
