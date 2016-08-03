/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timotei;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * FXML Controller class
 *
 * @author root
 */
public class FXMLStartScreenController implements Initializable {
    @FXML
    private Button lataaDB;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void lataaDBAction(ActionEvent event) throws ParserConfigurationException, SAXException {
        Stage currentStage = (Stage) lataaDB.getScene().getWindow();
        
        
        try {
            
            //Luodaan itse ohjelmalle ikkuna
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 820, 550);
            stage.setResizable(false);
            stage.setTitle("TIMOTEI");
            stage.setScene(scene);
            stage.show();
            currentStage.close();
        } catch (IOException ex) {
            System.err.println("I/O ex");;
        }
    }
    
}
