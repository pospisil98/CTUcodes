package cz.cvut.fel.pjv.schoolis.controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import cz.cvut.fel.pjv.schoolis.models.TeacherManager;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * AdminTeacherMenuController class
 *
 * @author Vojcek
 */
public class AdminTeacherMenuController implements Initializable {

    private TeacherManager teacherManager;

    @FXML
    private JFXButton teacherInfoButton;
    @FXML
    private JFXButton newTeacherButton;
    @FXML
    private JFXButton backButton;
    @FXML
    private JFXComboBox<String> teacherComboBox;

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
     * This version is used when accessing from main menu (first time).
     *
     * @param connection instance of connection
     */
    public void initData(Connection connection) {
        this.teacherManager = new TeacherManager(connection);

        setComboBoxData();
    }

    /**
     * Initiation of class attributes from outside.
     *
     * This version is used when accessing from inner menus.
     *
     * @param teacherManager instance of teacher manager
     */
    public void initData(TeacherManager teacherManager) {
        this.teacherManager = teacherManager;

        setComboBoxData();
    }

    private void setComboBoxData() {
        teacherComboBox.getItems().clear();
        teacherComboBox.getItems().addAll(teacherManager.getAllTeachersNames());
        teacherComboBox.getSelectionModel().select(0);
    }

    @FXML
    private void teacherInfoButtonClicked(ActionEvent event) {
        String selectedTeacherName = teacherComboBox.getSelectionModel().getSelectedItem();
        if (selectedTeacherName != null) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/fxml/admin/adminTeacherInfo.fxml"
                        )
                );

                Stage stage = new Stage();
                stage.setTitle("Informace o učiteli");
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setScene(new Scene((Pane) loader.load()));

                AdminTeacherInfoController c = loader.<AdminTeacherInfoController>getController();
                this.teacherManager.setID_teacher(this.teacherManager.getIdFromName(selectedTeacherName.split(" ")[0], selectedTeacherName.split(" ")[1]));
                c.initData(this.teacherManager);

                stage.show();
                ((Node) (event.getSource())).getScene().getWindow().hide();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("V databázi zatím není žádný učitel.");
            alert.showAndWait();
        }
    }

    @FXML
    private void newTeacherButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/fxml/admin/adminNewTeacher.fxml"
                    )
            );

            Stage stage = new Stage();
            stage.setTitle("Nový učitel");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));

            AdminNewTeacherController c = loader.<AdminNewTeacherController>getController();
            c.initData(this.teacherManager);

            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/fxml/admin/adminMenu.fxml"
                    )
            );
            Stage stage = new Stage();
            stage.setTitle("Menu");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));

            AdminMenuController c = loader.<AdminMenuController>getController();
            c.initData(this.teacherManager.getConnection());

            stage.show();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
