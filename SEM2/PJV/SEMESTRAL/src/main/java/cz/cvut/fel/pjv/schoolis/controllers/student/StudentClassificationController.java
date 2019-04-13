package cz.cvut.fel.pjv.schoolis.controllers.student;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import cz.cvut.fel.pjv.schoolis.models.ClassificationManager;
import cz.cvut.fel.pjv.schoolis.models.StudentManager;
import cz.cvut.fel.pjv.schoolis.models.SubjectManager;
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
public class StudentClassificationController implements Initializable {
    private StudentManager studentManager;
    private ClassificationManager classificationManager;
    private SubjectManager subjectManager;
    private TeacherManager teacherManager;

    @FXML
    private JFXComboBox<String> subjectComboBox;
    @FXML
    private JFXButton backButton;
    @FXML
    private TableView<String[]> classificationTableView;
    @FXML
    private TableColumn<String[], String> dateColumn;
    @FXML
    private TableColumn<String[], String> gradeColumn;
    @FXML
    private TableColumn<String[], String> weightColumn;
    @FXML
    private TableColumn<String[], String> descriptionColumn;
    @FXML
    private Label meanLabel;
    @FXML
    private Label teacherName;

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
     * @param studentManager    instance of class StudentManager
     */
    public void initData(StudentManager studentManager) {
        this.studentManager = studentManager;
        this.classificationManager = new ClassificationManager(studentManager.getConnection());
        this.subjectManager = new SubjectManager(studentManager.getConnection());
        this.teacherManager = new TeacherManager(studentManager.getConnection());
        setComboBoxData();
        setTableData();
        meanLabel.setText("Průměr známek: " + String.format("%.2f", this.getMean()));
        teacherName.setText(teacherManager.getNameFromId(subjectManager.getTeacher(subjectManager.getSubjectIdByName(subjectComboBox.getSelectionModel().getSelectedItem()))));
    }

    private void setComboBoxData() {
        subjectComboBox.getItems().clear();
        subjectComboBox.getItems().addAll(subjectManager.getStudentsSubjects(studentManager.getClassId()));
        subjectComboBox.getSelectionModel().select(0);
    }
    
    private void setTableData() {
        ObservableList<String[]> data = this.classificationManager.getClassification(studentManager.getID_student(), subjectManager.getSubjectIdByName(subjectComboBox.getSelectionModel().getSelectedItem().toString()));
        
        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[4]));
                }
            });
        gradeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[1]));
                }
            });
        weightColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[2]));
                }
            });
        descriptionColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[3]));
                }
            });
        classificationTableView.setItems(data);
    }

    @FXML
    private void subjectComboBoxClicked(ActionEvent event) {
        setTableData();
        meanLabel.setText("Průměr známek: " + String.format("%.2f", this.getMean()));        
        teacherName.setText(teacherManager.getNameFromId(subjectManager.getTeacher(subjectManager.getSubjectIdByName(subjectComboBox.getSelectionModel().getSelectedItem()))));
    }
    
    private float getMean() {
        float xw = 0;
        float w = 0;
        int numberOfRows = classificationTableView.getItems().size();
        for(int i = 0; i < numberOfRows; i++) {
            xw += Float.parseFloat(classificationTableView.getColumns().get(1).getCellObservableValue(i).getValue().toString())
                  * Float.parseFloat(classificationTableView.getColumns().get(2).getCellObservableValue(i).getValue().toString());
            w += Float.parseFloat(classificationTableView.getColumns().get(2).getCellObservableValue(i).getValue().toString());
            
        }
        if(xw == 0 && w == 0)
        {
            return 0;
        } else {
            return xw/w;
        }
    }

    @FXML
    private void backButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/student/studentMenu.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
            
            StudentMenuController c = loader.<StudentMenuController>getController();
            c.initData(studentManager);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    } 
}
