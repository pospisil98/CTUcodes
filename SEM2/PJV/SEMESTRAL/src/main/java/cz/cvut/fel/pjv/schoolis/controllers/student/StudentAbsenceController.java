package cz.cvut.fel.pjv.schoolis.controllers.student;

import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.AbsenceManager;
import cz.cvut.fel.pjv.schoolis.models.StudentManager;
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
public class StudentAbsenceController implements Initializable {

    private StudentManager studentManager;
    private AbsenceManager absenceManager;

    @FXML
    private JFXButton backButton;
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
     * This method is used when accessing the controller from the menu.
     * 
     * @param studentManager    instance of class StudentManager
     */
    public void initData(StudentManager studentManager) {
        this.studentManager = studentManager;
        this.absenceManager = new AbsenceManager(studentManager.getConnection());
        
        setTableData();
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
