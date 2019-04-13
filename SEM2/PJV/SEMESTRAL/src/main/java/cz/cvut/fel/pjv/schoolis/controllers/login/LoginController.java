package cz.cvut.fel.pjv.schoolis.controllers.login;
import cz.cvut.fel.pjv.schoolis.controllers.teacher.TeacherMenuController;
import cz.cvut.fel.pjv.schoolis.controllers.student.StudentMenuController;
import cz.cvut.fel.pjv.schoolis.controllers.parent.ParentMenuController;
import cz.cvut.fel.pjv.schoolis.controllers.admin.AdminMenuController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import cz.cvut.fel.pjv.schoolis.Config;
import cz.cvut.fel.pjv.schoolis.models.LoginManager;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Window;

/**
 * LoginController class
 * 
 * @author Vojcek
 */

public class LoginController implements Initializable {
    private LoginManager lm;
    private Map<String,String> user;
    
    @FXML
    private JFXTextField usernameText;
    @FXML
    private JFXPasswordField passwordText;
    @FXML
    private JFXButton loginButton;
    
    
     /**
     * Initializes the controller class.
     * 
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // nothing to be initialized
    }
    
    /**
     * Initiation of class attributes from outside.
     * 
     * @param connection    instance of connection 
     */
    public void initData(Connection connection) {
        lm = new LoginManager(connection);
    }

    @FXML
    private void loginButtonClicked(ActionEvent event) {        
        login(((Node)(event.getSource())).getScene().getWindow());
    }
    
    @FXML
    private void onEnter(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ENTER))
        {
            login(((Node)(event.getSource())).getScene().getWindow());
        }
    }
    
    private boolean validateLogin(String username, String password) {
        List<Map<String,String>> data = lm.getLoginByUsername(username);
        this.user = data.get(0);
        
        if(user == null) {
            return false;
        }
        if(!getPasswordHash(password).equals(user.get("hash"))) {
            return false;
        }
        return true;
    }
    
    private String getPasswordHash(String pswd) {
        String generatedPassword = null;
        
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(pswd.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return generatedPassword;
    }
    
    private void login(Window w) {
        if(validateLogin(usernameText.getText(), passwordText.getText()) == true) {            
            int role = Integer.parseInt(user.get("ID_role"));
            String controllerPath = "";

            // based on role decide which path to choose
            switch(role){
                case Config.ADMINROLE: {
                    controllerPath = "/fxml/admin/adminMenu.fxml";
                    break;
                }
                case Config.TEACHERROLE: {
                    controllerPath = "/fxml/teacher/teacherMenu.fxml";
                    break;
                }
                case Config.STUDENTROLE: {
                    controllerPath = "/fxml/student/studentMenu.fxml";
                    break;
                }
                case Config.PARENTROLE: {
                    controllerPath = "/fxml/parent/parentMenu.fxml";
                    break;
                }
            }

            try {             
                FXMLLoader loader = new FXMLLoader(
                getClass().getResource(controllerPath));

                Stage stage = new Stage();
                stage.setTitle("Menu");
                stage.setResizable(false);
                stage.setMaximized(false);
                stage.setScene(new Scene((Pane) loader.load()));

                stage.show();
                w.hide();

                switch(role){
                    case Config.ADMINROLE: {
                        AdminMenuController c = loader.<AdminMenuController>getController();
                        c.initData(lm.getConnection());
                        break;
                    }
                    case Config.TEACHERROLE: {
                        TeacherMenuController c = loader.<TeacherMenuController>getController();
                        c.initData(user.get("ID_login"), lm.getConnection());
                        break;
                    }
                    case Config.STUDENTROLE: {
                        StudentMenuController c = loader.<StudentMenuController>getController();
                        c.initData(user.get("ID_login"), lm.getConnection());
                        break;
                    }
                    case Config.PARENTROLE: {
                        ParentMenuController c = loader.<ParentMenuController>getController();
                        c.initData(user.get("ID_login"), lm.getConnection());
                        break;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }   
        } else {
            passwordText.setStyle("-fx-background-color: red;");
        }
    }
}
