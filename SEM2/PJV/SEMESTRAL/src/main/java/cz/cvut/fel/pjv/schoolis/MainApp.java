package cz.cvut.fel.pjv.schoolis;

import cz.cvut.fel.pjv.schoolis.controllers.login.LoginController;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Vojcek
 */
public class MainApp extends Application {
    
    /**
     * Start of application.
     * 
     * @param stage
     * @throws java.lang.Exception
     */
    @Override
    public void start(Stage stage) throws Exception {       
        // Initialize database connection otherwise shows alert and exit app
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://den1.mysql3.gear.host:3306/pjvdb?user=" + Config.DBUSER +
            "&password="+ Config.DBPASSWORD +"&" + Config.DBSETINGS);
        } catch(SQLException ex) {
            ex.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Něco se pokazilo :(\nKontaktujte prosím správce");

            alert.setTitle("Inicializace");
            alert.setHeaderText("Chyba připojení k databázi\nAplikace bude ukončena");
            alert.showAndWait();
            System.exit(1);
        }
        
        // Load login controller and pass database connection
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
              "/fxml/login/login.fxml"
            )
          );

        stage.setTitle("Login");
        stage.setScene(new Scene((Pane) loader.load()));
        LoginController c = loader.<LoginController>getController();
        c.initData(connection);

        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
