package cz.cvut.fel.pjv.schoolis;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.sql.*;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
 
    int counter = 0;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws SQLException {
        System.out.println("You clicked me!");
        
        try (Connection con = DriverManager.getConnection("jdbc:mysql://den1.mysql3.gear.host:3306/pjvdb?user=pjvdb&password=Fr4HVYaY!k!5&zeroDateTimeBehavior=CONVERT_TO_NULL")) {
            PreparedStatement q = con.prepareStatement("INSERT INTO role (value) VALUES (?)");
            q.setString(1, Integer.toString(counter));
            counter++;
            int r = q.executeUpdate();
            label.setText("Inserted");
        } catch(SQLException ex) {
            System.out.println("Error " + ex.getMessage());
            label.setText(ex.getMessage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
