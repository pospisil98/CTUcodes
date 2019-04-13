package cz.cvut.fel.pjv.schoolis.controllers.teacher;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
public class TeacherClassController implements Initializable {
    private TeacherManager teacherManager;
    private ClassManager classManager;
    private StudentManager studentManager;
    
    @FXML
    private JFXComboBox<String> classComboBox;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton studentButton;
    @FXML
    private TableView<String[]> studentTable;
    @FXML
    private TableColumn<String[], String> nameColumn;
    @FXML
    private TableColumn<String[], String> surnameColumn;

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
     * This method is used when accessing the controller from the menu.
     * 
     * @param teacherManager    instance of class TeacherManager
     */
    public void initData(TeacherManager teacherManager) {
        this.teacherManager = teacherManager;
        this.classManager = new ClassManager(teacherManager.getConnection());
        this.studentManager = new StudentManager(teacherManager.getConnection());
        
        this.setComboBoxData();
        this.setTableData();
    }
    
    /**
     * Initiation of class attributes from outside.
     * 
     * This version is used when accessing the menu from inside of application.
     * 
     * @param teacherManager    instance of class TeacherManager
     * @param classManager      instance of class ClassManager
     * @param studentManager    instance of class StudentManager
     */
    public void initData(TeacherManager teacherManager, ClassManager classManager, StudentManager studentManager) {
        this.teacherManager = teacherManager;
        this.classManager = classManager;
        this.studentManager = studentManager;
        
        this.setComboBoxData();
        this.setTableData();
     }
    
    private void setComboBoxData() {
        classComboBox.getItems().clear();
        classComboBox.getItems().addAll(classManager.getAllClassesNames());
        classComboBox.getSelectionModel().select(0);
    }
    
    private void setTableData() {
        ObservableList<String[]> data = this.studentManager.getStudentsByClassId(classManager.getClassIdByName(classComboBox.getSelectionModel().getSelectedItem().toString()));
        
        nameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[1]));
                }
            });
        surnameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[2]));
                }
            });
        studentTable.setItems(data);
    }

    @FXML
    private void classComboBoxClicked(ActionEvent event) {
        setTableData();
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
            stage.show();
            
            TeacherMenuController c = loader.<TeacherMenuController>getController();
            c.initData(teacherManager);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void studentButtonClicked(ActionEvent event) {
        if(studentTable.getSelectionModel().getSelectedItem() != null)
        {
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
                classManager.setID_class(classManager.getClassIdByName(classComboBox.getSelectionModel().getSelectedItem()));
                studentManager.setID_student(studentTable.getSelectionModel().getSelectedItem()[0]);
                c.initData(teacherManager, classManager, studentManager);
                
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
