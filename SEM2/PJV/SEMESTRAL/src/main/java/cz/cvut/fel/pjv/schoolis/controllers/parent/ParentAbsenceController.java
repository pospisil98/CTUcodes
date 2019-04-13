package cz.cvut.fel.pjv.schoolis.controllers.parent;

import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.AbsenceManager;
import cz.cvut.fel.pjv.schoolis.models.ParentManager;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * ParentAbsenceController class
 *
 * @author Karel
 */
public class ParentAbsenceController implements Initializable {

    private ParentManager parentManager;
    private AbsenceManager absenceManager;
    
    @FXML
    private JFXButton backToMenuButton;
    @FXML
    private JFXButton apologizeButton;
    @FXML
    private TableView<String[]> absenceTable;
    @FXML
    private TableColumn<String[], String> dateColumn;
    @FXML
    private TableColumn<String[], String> apologizedColumn;

    /**
     * Initializes the controller class.
     * 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        this.absenceManager = new AbsenceManager(parentManager.getConnection());
        
        setTableData();
    }
    
    private void setTableData() {
        ObservableList<String[]> data = this.absenceManager.getAbsences(parentManager.getID_student());
        
        dateColumn.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[1]));
                }
            });
        apologizedColumn.setCellValueFactory(new Callback<CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(CellDataFeatures<String[], String> p) {
                    return new SimpleStringProperty((p.getValue()[2]));
                }
            });
        absenceTable.setItems(data);
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

    @FXML
    private void apologizeButtonClicked(ActionEvent event) {
        if(absenceTable.getSelectionModel().getSelectedItem() != null) {
            absenceManager.apologize(absenceTable.getSelectionModel().getSelectedItem()[0], absenceTable.getSelectionModel().getSelectedItem()[3]);
            ObservableList<String[]> data = absenceManager.getAbsences(parentManager.getID_student());
            absenceTable.setItems(data);
        }
    }
    
}
