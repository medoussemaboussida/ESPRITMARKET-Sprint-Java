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

public class UtilisateurController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button ajouterDonsButton;

    @FXML
    private Button demanderDonsButton;

    private utilisateurService userService;

    private utilisateur currentUser; // Utilisateur actuellement connecté

    public UtilisateurController() {
        userService = new utilisateurService();
    }

    public void initialize() {
        // Récupérer l'utilisateur actuellement connecté depuis la base de données (ici, supposons que l'ID de l'utilisateur est 1)
        currentUser = userService.getUserById(1); // Remplacez 1 par l'ID de l'utilisateur actuellement connecté

        // Afficher un message de bienvenue avec le nom de l'utilisateur
        welcomeLabel.setText("Bonjour " + currentUser.getNomUser() + "! Bienvenue sur notre plateforme de dons.");

        // Configuration des actions des boutons
        ajouterDonsButton.setOnAction(this::handleAjouterDons);
        demanderDonsButton.setOnAction(this::handleDemanderDons);
    }

    @FXML
    void handleAjouterDons(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FaireDons.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleDemanderDons(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demande_dons.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
