package cz.cvut.fel.pjv.schoolis.controllers.parent;

import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.ParentManager;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Karel
 */
public class ParentTeachersController implements Initializable {
    private ParentManager parentManager;
    private TeacherManager teacherManager;
    
    @FXML
    private JFXButton backToMenuButton;
    @FXML
    private TableView<String[]> teachersTable;
    @FXML
    private TableColumn<String[], String> nameColumn;
    @FXML
    private TableColumn<String[], String> surnameColumn;
    @FXML
    private TableColumn<String[], String> degreeColumn;
    @FXML
    private TableColumn<String[], String> mailColumn;
    @FXML
    private TableColumn<String[], String> phoneColumn;

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
        this.teacherManager = new TeacherManager(parentManager.getConnection());
        
        setTableData();
    }
    
    private void setTableData() {
        ObservableList<String[]> teachers = this.teacherManager.getAllTeachers();
        
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
        degreeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[3]));
                }
            });
        mailColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[7]));
                }
            });
        phoneColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[8]));
                }
            });

        teachersTable.setItems(teachers);
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
            ((Node)(event.getSource())).getScene().getWindow().hide();
            
            ParentMenuController c = loader.<ParentMenuController>getController();
            c.initData(parentManager);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
