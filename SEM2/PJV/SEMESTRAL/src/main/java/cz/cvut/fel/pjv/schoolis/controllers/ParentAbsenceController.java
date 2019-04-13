/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.schoolis.controllers;



import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.ParentAbsenceManager;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Karel
 */
public class ParentAbsenceController implements Initializable {

    private ParentAbsenceManager manager;
    
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
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.manager = new ParentAbsenceManager();
        ObservableList<String[]> data = manager.getAbsences();
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
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/parent/parentMenu.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setScene(new Scene(root));
            stage.show();
            // Hide this current window (if this is what you want)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void apologizeButtonClicked(ActionEvent event) {
        manager.apologize(absenceTable.getSelectionModel().getSelectedItem()[0]);
        ObservableList<String[]> data = manager.getAbsences();
        absenceTable.setItems(data);
        
    }
    
}
