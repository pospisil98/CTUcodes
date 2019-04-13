package cz.cvut.fel.pjv.schoolis.controllers.teacher;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
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
import javafx.scene.control.Alert;
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
public class TeacherStudentClassificationController implements Initializable {

    private TeacherManager teacherManager;
    private ClassManager classManager;
    private StudentManager studentManager;
    private ClassificationManager classificationManager;
    private SubjectManager subjectManager;

    @FXML
    private Label studentLabel;
    @FXML
    private JFXComboBox<String> subjectComboBox;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXButton gradeButton;
    @FXML
    private TableView<String[]> classificationTable;
    @FXML
    private TableColumn<String[], String> dateColumn;
    @FXML
    private TableColumn<String[], String> gradeColumn;
    @FXML
    private TableColumn<String[], String> descriptionColumn;
    @FXML
    private TableColumn<String[], String> weightColumn;
    @FXML
    private JFXButton removeGradeButton;
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
        // TODO
    }

    /**
     * Initiation of class attributes from outside.
     *
     * This method is used when accessing the controller from the student menu.
     *
     * @param teacherManager instance of class TeacherManager
     * @param classManager instance of class ClassManager
     * @param studentManager instance of class StudentManager
     */
    public void initData(TeacherManager teacherManager, ClassManager classManager, StudentManager studentManager) {
        this.teacherManager = teacherManager;
        this.classManager = classManager;
        this.studentManager = studentManager;
        this.classificationManager = new ClassificationManager(teacherManager.getConnection());
        this.subjectManager = new SubjectManager(teacherManager.getConnection());
        setComboBoxData();
        setTableData();
        studentLabel.setText(studentManager.getFullName());
        meanLabel.setText("Průměr známek: " + String.format("%.2f", this.getMean()));
        teacherName.setText(teacherManager.getNameFromId(subjectManager.getTeacher(subjectManager.getSubjectIdByName(subjectComboBox.getSelectionModel().getSelectedItem()))));
    }

    /**
     * Initiation of class attributes from outside.
     *
     * This method is used when accessing the controller from inside of
     * application.
     *
     * @param teacherManager instance of class TeacherManager
     * @param classManager instance of class ClassManager
     * @param studentManager instance of class StudentManager
     * @param classificationManager instance of class ClassificationManager
     * @param subjectManager instance of class SubjectManager
     */
    public void initData(TeacherManager teacherManager, ClassManager classManager, StudentManager studentManager, ClassificationManager classificationManager, SubjectManager subjectManager) {
        this.teacherManager = teacherManager;
        this.classManager = classManager;
        this.studentManager = studentManager;
        this.classificationManager = classificationManager;
        this.subjectManager = subjectManager;
        setComboBoxData();
        setTableData();
        studentLabel.setText(studentManager.getFullName());
        meanLabel.setText("Průměr známek: " + String.format("%.2f", this.getMean()));
        teacherName.setText(teacherManager.getNameFromId(subjectManager.getTeacher(subjectManager.getSubjectIdByName(subjectComboBox.getSelectionModel().getSelectedItem()))));
    }

    private void setComboBoxData() {
        subjectComboBox.getItems().clear();
        subjectComboBox.getItems().addAll(subjectManager.getStudentsSubjects(classManager.getID_class()));
        subjectComboBox.getSelectionModel().select(0);
    }

    private void setTableData() {
        if (subjectComboBox.getSelectionModel().getSelectedItem() != null) {
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
            classificationTable.setItems(data);
        }
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
            ((Node) (event.getSource())).getScene().getWindow().hide();

            TeacherStudentMenuController c = loader.<TeacherStudentMenuController>getController();
            c.initData(teacherManager, classManager, studentManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gradeButtonClicked(ActionEvent event) {
        if(teacherManager.teaches(subjectManager.getSubjectIdByName(subjectComboBox.getSelectionModel().getSelectedItem())))
        {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/fxml/teacher/teacherStudentNewClassification.fxml"
                        )
                );
                Stage stage = new Stage();
                stage.setTitle("Nová známka");
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setScene(new Scene((Pane) loader.load()));
                stage.show();

                TeacherStudentNewClassificationController c = loader.<TeacherStudentNewClassificationController>getController();
                subjectManager.setID_subject(subjectManager.getSubjectIdByName(subjectComboBox.getSelectionModel().getSelectedItem().toString()));
                c.initData(teacherManager, classManager, studentManager, classificationManager, subjectManager);

                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Nemůžete udělit známku v předmětu který neučíte.");
            alert.showAndWait();
        }
    }

    private float getMean() {
        float xw = 0;
        float w = 0;
        int numberOfRows = classificationTable.getItems().size();
        for (int i = 0; i < numberOfRows; i++) {
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
    private void subjectComboBoxClicked(ActionEvent event) {
        setTableData();
        meanLabel.setText("Průměr známek: " + String.format("%.2f", this.getMean()));
        teacherName.setText(teacherManager.getNameFromId(subjectManager.getTeacher(subjectManager.getSubjectIdByName(subjectComboBox.getSelectionModel().getSelectedItem()))));
    }

    @FXML
    private void removeGradeButtonClicked(ActionEvent event) {
        if (classificationTable.getSelectionModel().getSelectedItem() != null) {
            this.classificationManager.removeGrade(classificationTable.getSelectionModel().getSelectedItem()[0]);
            setTableData();
        }
    }

}
