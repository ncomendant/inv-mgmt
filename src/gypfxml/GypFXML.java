/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gypfxml;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class GypFXML extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        URL rsc = getClass().getResource("ui/main/MainScreen.fxml");
//        URL rsc = getClass().getResource("Simple.fxml");
        Parent root = FXMLLoader.load(rsc);
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}