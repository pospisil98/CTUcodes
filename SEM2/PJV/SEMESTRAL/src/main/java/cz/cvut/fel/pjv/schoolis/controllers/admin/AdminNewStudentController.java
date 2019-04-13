package cz.cvut.fel.pjv.schoolis.controllers.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import cz.cvut.fel.pjv.schoolis.Config;
import cz.cvut.fel.pjv.schoolis.SaveThread;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.ParentManager;
import cz.cvut.fel.pjv.schoolis.models.StudentManager;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karel
 */
public class AdminNewStudentController implements Initializable {
    private StudentManager studentManager;
    private ParentManager parentManager;
    private ClassManager classManager;
    private boolean sNameValid;
    private boolean sSurnameValid;
    private boolean sBirthdateValid;
    private boolean sBirthnumValid;
    private boolean sAsuranceValid;
    private boolean sPhoneValid;
    private boolean sMailValid;
    private boolean sCityValid;
    private boolean sHousenumValid;
    private boolean sStreetValid;
    private boolean pPhoneValid;
    private boolean pMailValid;
    private boolean pSurnameValid;
    private boolean pNameValid;
    
    private static final String birthnumRegEx = "^\\s*(\\d\\d)(\\d\\d)(\\d\\d)[ \\/]*(\\d\\d\\d)(\\d?)\\s*$";
    private static final String mailRegEx = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private static final String phoneRegEx = "^[0-9]{9}$";
    private static final String housenumRegEx = "^([1-9]|[1-8][0-9]|9[0-9]|[1-8][0-9]{2}|9[0-8][0-9]|99[0-9]|[1-8][0-9]{3}|9[0-8][0-9]{2}|99[0-8][0-9]|999[0-9]|10000)$";
    
    @FXML
    private JFXButton addStudentButton;
    @FXML
    private Label classLabel;
    @FXML
    private JFXTextField sNameInput;
    @FXML
    private JFXTextField sSurnameInput;
    @FXML
    private DatePicker sBirthdateDatePicker;
    @FXML
    private JFXTextField sBirthnumInput;
    @FXML
    private JFXTextField sAsuranceInput;
    @FXML
    private JFXTextField sPhoneInput;
    @FXML
    private JFXTextField sMailInput;
    @FXML
    private JFXTextField sCityInput;
    @FXML
    private JFXTextField sHousenumInput;
    @FXML
    private JFXTextField sStreetInput;
    @FXML
    private JFXTextField pPhoneInput;
    @FXML
    private JFXTextField pMailInput;
    @FXML
    private JFXTextField pSurnameInput;
    @FXML
    private JFXTextField pNameInput;
    @FXML
    private JFXButton backButton;   
    
    /**
     * Initializes the controller class.
     * 
     * Add listener for every input which disables form sending
     * when anything is filled incorrectly (empty).
     * 
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sNameInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(sNameInput.getText().trim().isEmpty() || sNameInput.getText().length() > 60)
                    {
                        sNameInput.setStyle("-fx-border-color: red;");
                        sNameValid = false;
                    } else {
                        sNameInput.setStyle("-fx-border-color: none;");
                        sNameValid = true;
                    }
                }
            }
        });
        
        sSurnameInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(sSurnameInput.getText().trim().isEmpty() || sSurnameInput.getText().length() > 60)
                    {
                        sSurnameInput.setStyle("-fx-border-color: red;");
                        sSurnameValid = false;
                    } else {
                        sSurnameInput.setStyle("-fx-border-color: none;");
                        sSurnameValid = true;
                    }
                }
            }
        });
        
        sBirthdateDatePicker.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(sBirthdateDatePicker.getValue() == null)
                    {
                        sBirthdateDatePicker.setStyle("-fx-border-color: red;");
                        sBirthdateValid = false;
                    } else {
                        sBirthdateDatePicker.setStyle("-fx-border-color: none;");
                        sBirthdateValid = true;
                    }
                }
            }
        });
        
        sBirthnumInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(sBirthnumInput.getText().trim().isEmpty() || !sBirthnumInput.getText().matches(birthnumRegEx))
                    {
                        sBirthnumInput.setStyle("-fx-border-color: red;");
                        sBirthnumValid = false;
                    } else {
                        sBirthnumInput.setStyle("-fx-border-color: none;");
                        sBirthnumValid = true;
                    }
                }
            }
        });
        
        sAsuranceInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(sAsuranceInput.getText().trim().isEmpty() || sAsuranceInput.getText().length() > 128)
                    {
                        sAsuranceInput.setStyle("-fx-border-color: red;");
                        sAsuranceValid = false;
                    } else {
                        sAsuranceInput.setStyle("-fx-border-color: none;");
                        sAsuranceValid = true;
                    }
                }
            }
        });
        
        sPhoneInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(sPhoneInput.getText().trim().isEmpty() || !sPhoneInput.getText().matches(phoneRegEx))
                    {
                        sPhoneInput.setStyle("-fx-border-color: red;");
                        sPhoneValid = false;
                    } else {
                        sPhoneInput.setStyle("-fx-border-color: none;");
                        sPhoneValid = true;
                    }
                }
            }
        });
        
        sMailInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(sMailInput.getText().trim().isEmpty() || !sMailInput.getText().matches(mailRegEx))
                    {
                        sMailInput.setStyle("-fx-border-color: red;");
                        sMailValid = false;
                    } else {
                        sMailInput.setStyle("-fx-border-color: none;");
                        sMailValid = true;
                    }
                }
            }
        });
        
        sCityInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(sCityInput.getText().trim().isEmpty() || sCityInput.getText().length() > 60)
                    {
                        sCityInput.setStyle("-fx-border-color: red;");
                        sCityValid = false;
                    } else {
                        sCityInput.setStyle("-fx-border-color: none;");
                        sCityValid = true;
                    }
                }
            }
        });
        
        sHousenumInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(sHousenumInput.getText().trim().isEmpty() || !sHousenumInput.getText().matches(housenumRegEx))
                    {
                        sHousenumInput.setStyle("-fx-border-color: red;");
                        sHousenumValid = false;
                    } else {
                        sHousenumInput.setStyle("-fx-border-color: none;");
                        sHousenumValid = true;
                    }
                }
            }
        });
        
        sStreetInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(sStreetInput.getText().trim().isEmpty() || sStreetInput.getText().length() > 60)
                    {
                        sStreetInput.setStyle("-fx-border-color: red;");
                        sStreetValid = false;
                    } else {
                        sStreetInput.setStyle("-fx-border-color: none;");
                        sStreetValid = true;
                    }
                }
            }
        });
        
        pPhoneInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(pPhoneInput.getText().trim().isEmpty() || !pPhoneInput.getText().matches(phoneRegEx))
                    {
                        pPhoneInput.setStyle("-fx-border-color: red;");
                        pPhoneValid = false;
                    } else {
                        pPhoneInput.setStyle("-fx-border-color: none;");
                        pPhoneValid = true;
                    }
                }
            }
        });
        
        pMailInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(pMailInput.getText().trim().isEmpty() || !pMailInput.getText().matches(mailRegEx))
                    {
                        pMailInput.setStyle("-fx-border-color: red;");
                        pMailValid = false;
                    } else {
                        pMailInput.setStyle("-fx-border-color: none;");
                        pMailValid = true;
                    }
                }
            }
        });
        
        pSurnameInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(pSurnameInput.getText().trim().isEmpty() || pSurnameInput.getText().length() > 60)
                    {
                        pSurnameInput.setStyle("-fx-border-color: red;");
                        pSurnameValid = false;
                    } else {
                        pSurnameInput.setStyle("-fx-border-color: none;");
                        pSurnameValid = true;
                    }
                }
            }
        });
        
        pNameInput.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    if(pNameInput.getText().trim().isEmpty() || pNameInput.getText().length() > 60)
                    {
                        pNameInput.setStyle("-fx-border-color: red;");
                        pNameValid = false;
                    } else {
                        pNameInput.setStyle("-fx-border-color: none;");
                        pNameValid = true;
                    }
                }
            }
        });
    }
    
    /**
     * Initiation of class attributes from outside.
     * 
     * @param classManager      class manager instance
     * @param studentManager    student manager instance
     */
    public void initData(ClassManager classManager, StudentManager studentManager) {
        classLabel.setText(classManager.getClassNameById(classManager.getID_class()));
        
        this.studentManager = studentManager;
        this.classManager = classManager;
        this.parentManager = new ParentManager(studentManager.getConnection());
        
        // default state for form checking
        sNameValid = sSurnameValid = sBirthdateValid = sBirthnumValid =
                sAsuranceValid = sPhoneValid = sMailValid = sCityValid = 
                sHousenumValid = sStreetValid = pPhoneValid = pMailValid = 
                pSurnameValid = pNameValid = false;
    }

    @FXML
    private void addStudentButtonClicked(ActionEvent event) {
        boolean condition = sNameValid && sSurnameValid && sBirthdateValid &&
                sBirthnumValid && sAsuranceValid && sPhoneValid && sMailValid &&
                sCityValid && sHousenumValid && sStreetValid && pPhoneValid && 
                pMailValid && pSurnameValid && pNameValid;
        
        Alert alert;
        if(condition) {
            List<String> studentData = new ArrayList<>();        
            studentData.add(sNameInput.getText());
            studentData.add(sSurnameInput.getText());
            studentData.add(sStreetInput.getText());
            studentData.add(sCityInput.getText());
            studentData.add(sHousenumInput.getText());
            studentData.add(sMailInput.getText());
            studentData.add(sPhoneInput.getText());
            studentData.add(sAsuranceInput.getText());
            studentData.add(sBirthnumInput.getText());
            studentData.add(sBirthdateDatePicker.getValue().toString());
            studentData.add(classManager.getID_class());

            List<String> parentData = new ArrayList<>();
            parentData.add(pNameInput.getText());
            parentData.add(pSurnameInput.getText());
            parentData.add(pMailInput.getText());
            parentData.add(pPhoneInput.getText());

            String parentID;
            String parentUsername = "";
            String parentPassword;
            // try to select parent with same data
            parentID = parentManager.checkParentExistence(parentData);
            
            // when there isn't such parent then create new one
            if (parentID == null) {
                // adding generated data because of database insert
                parentUsername = parentManager.createUsername(pNameInput.getText(), pSurnameInput.getText());
                parentPassword = UserManager.generatePassword(Config.PSWDLEN);
                
                long parentLoginID = parentManager.addLogin(parentUsername, parentPassword, Config.PARENTROLE);
                parentData.add(Long.toString(parentLoginID));
                parentID = Long.toString(parentManager.addParent(parentData));
            } else {
                // get parent login 
                parentUsername = parentManager.getUsernamefromLoginID(parentManager.getLoginID(parentID));
                parentPassword = "heslo je nezměněno";
            }

            String studentUsername = studentManager.createUsername(sNameInput.getText(), sSurnameInput.getText());
            String studentPassword = UserManager.generatePassword(Config.PSWDLEN);

            long studentLoginID = studentManager.addLogin(studentUsername, studentPassword, Config.STUDENTROLE);
            studentData.add(Long.toString(studentLoginID));
            studentData.add(parentID);
            
            int added = studentManager.addStudent(studentData);
            
            if(added != 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Student byl úspěšně přidán");

                String dataToWrite = this.getWriteData(studentUsername, studentPassword, parentUsername, parentPassword);
                String filename = studentUsername;
                
                // save generated things in separate thread
                SaveThread th = new SaveThread(filename, dataToWrite);
                th.run();
            } else {
                alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Něco se pokazilo :(");
            }

            alert.setTitle("Tvorba studenta");
            alert.setHeaderText(null);

            ((Node)(event.getSource())).getScene().getWindow().hide();
            alert.showAndWait();
            
            
            try {
                FXMLLoader loader = new FXMLLoader(
                   getClass().getResource(
                     "/fxml/admin/adminClassInfo.fxml"
                   )
                );
                Stage stage = new Stage();
                stage.setTitle("Informace o třídě");
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setScene(new Scene((Pane) loader.load()));
                
                AdminClassInfoController c = loader.<AdminClassInfoController>getController();
                c.initData(classManager);
                
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
                 "/fxml/admin/adminClassInfo.fxml"
               )
            );
            Stage stage = new Stage();
            stage.setTitle("Informace o třídě");
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.setScene(new Scene((Pane) loader.load()));
            AdminClassInfoController c = loader.<AdminClassInfoController>getController();
            c.initData(classManager);
            stage.show();
            ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
     private String getWriteData(String sUsername, String sPassword, String pUsername, String pPassword) {
        String write = sNameInput.getText() + " " + sSurnameInput.getText() + "\n";
        write += "Student:   " + sUsername + ":" + sPassword + "\n";
        write += "Rodič:   " + pUsername + ":" + pPassword + "\n";
        return write;
    }
}
