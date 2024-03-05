/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo4;
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author asus
 */
public class hh extends Application {
   
    @Override
    public void start(Stage primaryStage) throws IOException {
               Parent root = FXMLLoader.load(getClass().getResource("../../../../../../../../../wannesi/src/gui/evenement.fxml"));
        Scene scene = new Scene(root,950,650); 
        primaryStage.setTitle("Gérer evenements");
        //primaryStage.setIconified(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}