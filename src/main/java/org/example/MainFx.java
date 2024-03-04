package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
      //FXMLLoader loader =new FXMLLoader(getClass().getResource("/CodePromoAfficher.fxml"));
        //FXMLLoader loader =new FXMLLoader(getClass().getResource("/AjouterOffre.fxml"));
         FXMLLoader loader =new FXMLLoader(getClass().getResource("/CodePromoAjouter.fxml"));
         //FXMLLoader loader =new FXMLLoader(getClass().getResource("/AfficherOffre.fxml"));
        // FXMLLoader loader =new FXMLLoader(getClass().getResource("/FrontPanierCommande.fxml"));
        // FXMLLoader loader =new FXMLLoader(getClass().getResource("/FrontProduitOffre.fxml"));

        Parent root = loader.load();
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ajouter Offre");
        primaryStage.show();
    }
}
