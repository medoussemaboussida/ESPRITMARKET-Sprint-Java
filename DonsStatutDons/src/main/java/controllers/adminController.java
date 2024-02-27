package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import service.utilisateurService;
import entities.utilisateur;

public class adminController {



    @FXML
    private Button GestionDonsButton;

    @FXML
    private Button GestiondemandesDonsButton;




    public void initialize() {


        // Configuration des actions des boutons
        GestionDonsButton.setOnAction(this::handleGestionDons);
        GestiondemandesDonsButton.setOnAction(this::handleGestionDemande);
    }

    @FXML
    void handleGestionDons(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherDons.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGestionDemande(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionDemandesDons.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
