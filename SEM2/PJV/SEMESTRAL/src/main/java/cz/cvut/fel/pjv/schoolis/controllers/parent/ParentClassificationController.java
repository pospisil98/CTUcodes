package cz.cvut.fel.pjv.schoolis.controllers.parent;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import cz.cvut.fel.pjv.schoolis.models.ClassificationManager;
import cz.cvut.fel.pjv.schoolis.models.ParentManager;
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
 * ParentClassificationController class
 *
 * @author Karel
 */
public class ParentClassificationController implements Initializable {
    private ParentManager parentManager;
    private ClassificationManager classificationManager;
    private SubjectManager subjectManager;
    private TeacherManager teacherManager;
    
    @FXML
    private JFXComboBox<String> subjectComboBox;
    @FXML
    private JFXButton backToMenuButton;
    @FXML
    private Label childNameLabel;
    @FXML
    private TableView<String[]> classificationTable;
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
     * @param parentManager instance of class ParentManager
     */
    public void initData(ParentManager parentManager) {
        this.parentManager = parentManager;
        this.classificationManager = new ClassificationManager(parentManager.getConnection());
        this.subjectManager = new SubjectManager(parentManager.getConnection());
        this.teacherManager = new TeacherManager(parentManager.getConnection());
        childNameLabel.setText(parentManager.getChildName());
        setComboBoxData();
        setTableData();
        meanLabel.setText("Průměr známek: " + String.format("%.2f", this.getMean()));
        teacherName.setText(teacherManager.getNameFromId(subjectManager.getTeacher(subjectManager.getSubjectIdByName(subjectComboBox.getSelectionModel().getSelectedItem()))));
    }

    private void setComboBoxData() {
        subjectComboBox.getItems().clear();
        subjectComboBox.getItems().addAll(subjectManager.getStudentsSubjects(parentManager.getClassOfStudent()));
        subjectComboBox.getSelectionModel().select(0);
    }
    
    private void setTableData() {
        ObservableList<String[]> data = this.classificationManager.getClassification(parentManager.getID_student(), subjectManager.getSubjectIdByName(subjectComboBox.getSelectionModel().getSelectedItem().toString()));
        
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
        classificationTable.setItems(data);
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
        int numberOfRows = classificationTable.getItems().size();
        for(int i = 0; i < numberOfRows; i++) {
            xw += Float.parseFloat(classificationTable.getColumns().get(1).getCellObservableValue(i).getValue().toString())
                  * Float.parseFloat(classificationTable.getColumns().get(2).getCellObservableValue(i).getValue().toString());
            w += Float.parseFloat(classificationTable.getColumns().get(2).getCellObservableValue(i).getValue().toString());
            
        }
        if(xw == 0 && w == 0)
        {
            return 0;
        } else {
            return xw/w;
        }
    }

    @FXML
    private void backToMenuButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                  "/fxml/parent/parentMenu.fxml"
                )
              );
            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            stage.show();
            
            ParentMenuController c = loader.<ParentMenuController>getController();
            c.initData(parentManager);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
