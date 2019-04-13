package cz.cvut.fel.pjv.schoolis.controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import cz.cvut.fel.pjv.schoolis.Config;
import cz.cvut.fel.pjv.schoolis.SaveThread;
import cz.cvut.fel.pjv.schoolis.models.TeacherManager;
import cz.cvut.fel.pjv.schoolis.models.UserManager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
 * AdminNewTeacherController class
 *
 * @author Vojcek
 */
public class AdminNewTeacherController implements Initializable {
    private TeacherManager teacherManager;
    private boolean nameValid;
    private boolean surnameValid;
    private boolean degreeValid;
    private boolean cityValid;
    private boolean housenumValid;
    private boolean streetValid;
    private boolean phoneValid;
    private boolean mailValid;
    
    private static final String mailRegEx = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String phoneRegEx = "^[0-9]{9}$";
    private static final String housenumRegEx = "^([1-9]|[1-8][0-9]|9[0-9]|[1-8][0-9]{2}|9[0-8][0-9]|99[0-9]|[1-8][0-9]{3}|9[0-8][0-9]{2}|99[0-8][0-9]|999[0-9]|10000)$";

    
    @FXML
    private JFXButton newTeacherButton;
    @FXML
    private JFXTextField nameInput;
    @FXML
    private JFXTextField surnameInput;
    @FXML
    private JFXTextField degreeInput;
    @FXML
    private JFXTextField cityInput;
    @FXML
    private JFXTextField housenumInput;
    @FXML
    private JFXTextField streetInput;
    @FXML
    private JFXTextField phoneInput;
    @FXML
    private JFXTextField mailInput;

    /**
     * Initializes the controller class.
     * 
     * Adds listener for every input which disables form sending
     * when anything is filled incorrectly (empty).
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(nameInput.getText().trim().isEmpty() || nameInput.getText().length() > 60)
                    {
                        nameInput.setStyle("-fx-border-color: red;");
                        nameValid = false;
                    } else {
                        nameInput.setStyle("-fx-border-color: none;");
                        nameValid = true;
                    }
                }
            }
        });
        
        surnameInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(surnameInput.getText().trim().isEmpty() || surnameInput.getText().length() > 60)
                    {
                        surnameInput.setStyle("-fx-border-color: red;");
                        surnameValid = false;
                    } else {
                        surnameInput.setStyle("-fx-border-color: none;");
                        surnameValid = true;
                    }
                }
            }
        });
        
        degreeInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(degreeInput.getText().trim().isEmpty() || degreeInput.getText().length() > 60)
                    {
                        degreeInput.setStyle("-fx-border-color: red;");
                        degreeValid = false;
                    } else {
                        degreeInput.setStyle("-fx-border-color: none;");
                        degreeValid = true;
                    }
                }
            }
        });
        
        cityInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(cityInput.getText().trim().isEmpty() || cityInput.getText().length() > 60)
                    {
                        cityInput.setStyle("-fx-border-color: red;");
                        cityValid = false;
                    } else {
                        cityInput.setStyle("-fx-border-color: none;");
                        cityValid = true;
                    }
                }
            }
        });
        
        housenumInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(housenumInput.getText().trim().isEmpty() || !housenumInput.getText().matches(housenumRegEx))
                    {
                        housenumInput.setStyle("-fx-border-color: red;");
                        housenumValid = false;
                    } else {
                        housenumInput.setStyle("-fx-border-color: none;");
                        housenumValid = true;
                    }
                }
            }
        });
        
        streetInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(streetInput.getText().trim().isEmpty() || streetInput.getText().length() > 60)
                    {
                        streetInput.setStyle("-fx-border-color: red;");
                        streetValid = false;
                    } else {
                        streetInput.setStyle("-fx-border-color: none;");
                        streetValid = true;
                    }
                }
            }
        });
        
        phoneInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(phoneInput.getText().trim().isEmpty() || !phoneInput.getText().matches(phoneRegEx))
                    {
                        phoneInput.setStyle("-fx-border-color: red;");
                        phoneValid = false;
                    } else {
                        phoneInput.setStyle("-fx-border-color: none;");
                        phoneValid = true;
                    }
                }
            }
        });
        
        mailInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(mailInput.getText().trim().isEmpty() || !mailInput.getText().matches(mailRegEx))
                    {
                        mailInput.setStyle("-fx-border-color: red;");
                        mailValid = false;
                    } else {
                        mailInput.setStyle("-fx-border-color: none;");
                        mailValid = true;
                    }
                }
            }
        });
    }    

    /**
     * Initiation of class attributes from outside.  
     * 
     * @param teacherManager    teacher manager instance
     */
    public void initData(TeacherManager teacherManager) {
        this.teacherManager = teacherManager;
        
        // default state for form checking
        nameValid = surnameValid = degreeValid = cityValid = housenumValid = 
                streetValid = phoneValid = mailValid = false;
    
    }
    
    @FXML
    private void newTeacherButtonClicked(ActionEvent event) {
        boolean condition = nameValid && surnameValid && degreeValid && cityValid &&
                housenumValid && streetValid && phoneValid && mailValid;
        Alert alert;
        
        if(condition) {
            List<String> data = new ArrayList<>();
            data.add(nameInput.getText());
            data.add(surnameInput.getText());
            data.add(degreeInput.getText());
            data.add(streetInput.getText());
            data.add(cityInput.getText());
            data.add(housenumInput.getText());
            data.add(mailInput.getText());
            data.add(phoneInput.getText());


            String username = teacherManager.createUsername(nameInput.getText(), surnameInput.getText());
            String password = UserManager.generatePassword(Config.PSWDLEN);
            long loginID = teacherManager.addLogin(username, password, Config.TEACHERROLE);
            data.add(Long.toString(loginID));

            int added = teacherManager.addTeacher(data);

            if(added != 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Učitel byl úspěšně přidán");

                String dataToWrite = this.getWriteData(username, password);
                String filename = username;
                
                // save data in separate thread
                SaveThread th = new SaveThread(filename, dataToWrite);
                th.run();
            } else {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Něco se pokazilo :(");
            }
            alert.setTitle("Tvorba učitele");
            alert.setHeaderText(null);
            
            ((Node)(event.getSource())).getScene().getWindow().hide();
            alert.showAndWait();
            
            try {
                FXMLLoader loader = new FXMLLoader(
                   getClass().getResource(
                     "/fxml/admin/adminTeacherMenu.fxml"
                   )
                );

                Stage stage = new Stage();
                stage.setTitle("Menu učitele");
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setScene(new Scene((Pane) loader.load()));

                AdminTeacherMenuController c = loader.<AdminTeacherMenuController>getController();
                c.initData(this.teacherManager);

                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chybně vyplněný formulář");
            alert.setHeaderText("Vyplňte prosím formulář validními údaji.");
            alert.showAndWait();
        }
    }

    @FXML
    private void backButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
               getClass().getResource(
                 "/fxml/admin/adminTeacherMenu.fxml"
               )
            );
           
            Stage stage = new Stage();
            stage.setTitle("Menu učitele");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            
            AdminTeacherMenuController c = loader.<AdminTeacherMenuController>getController();
            c.initData(this.teacherManager);
            
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String getWriteData(String username, String password) {
        String write = nameInput.getText() + " " + surnameInput.getText() + "\n";
        write += "Učitel:   " + username + ":" + password + "\n";
        return write;
    }
}
