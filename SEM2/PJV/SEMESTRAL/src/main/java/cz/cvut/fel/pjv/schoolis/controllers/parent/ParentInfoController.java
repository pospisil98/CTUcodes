package cz.cvut.fel.pjv.schoolis.controllers.parent;

import com.jfoenix.controls.JFXButton;
import cz.cvut.fel.pjv.schoolis.models.ClassManager;
import cz.cvut.fel.pjv.schoolis.models.ParentManager;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karel
 */
public class ParentInfoController implements Initializable {
    private ParentManager parentManager;
    private ClassManager classManager;
    
    @FXML
    private Label sClassLabel;
    @FXML
    private Label sPhoneLabel;
    @FXML
    private Label sMailLabel;
    @FXML
    private Label sCityLabel;
    @FXML
    private Label sHouseNumLabel;
    @FXML
    private Label sStreetLabel;
    @FXML
    private Label sAssuranceLabel;
    @FXML
    private Label sBirthNumLabel;
    @FXML
    private Label sBirthdateLabel;
    @FXML
    private Label sSurnameLabel;
    @FXML
    private Label sNameLabel;
    @FXML
    private Label pPhoneLabel;
    @FXML
    private Label pMailLabel;
    @FXML
    private Label pSurnameLabel;
    @FXML
    private Label pNameLabel;
    @FXML
    private JFXButton backButton;
    
    
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
        this.classManager = new ClassManager(parentManager.getConnection());
        
        this.fillInfo();
    }

    private void fillInfo() {
        Map<String, String> sData = parentManager.getChildInfo();
        sNameLabel.setText(sData.get("name"));
        sSurnameLabel.setText(sData.get("surname"));
        sBirthdateLabel.setText(sData.get("birth_date"));
        sBirthNumLabel.setText(sData.get("personal_identification_number"));
        sAssuranceLabel.setText(sData.get("assurance_company"));
        sStreetLabel.setText(sData.get("street"));
        sHouseNumLabel.setText(sData.get("postcode"));
        sCityLabel.setText(sData.get("city"));
        sMailLabel.setText(sData.get("mail"));
        sPhoneLabel.setText(sData.get("phone"));
        sClassLabel.setText(classManager.getClassNameById(sData.get("ID_class")));
        
        Map<String, String> pData = parentManager.getParentInfo();
        pNameLabel.setText(pData.get("name"));
        pSurnameLabel.setText(pData.get("surname"));
        pMailLabel.setText(pData.get("email"));
        pPhoneLabel.setText(pData.get("phone"));
    }

    @FXML
    private void backButtonClicked(ActionEvent event) {
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