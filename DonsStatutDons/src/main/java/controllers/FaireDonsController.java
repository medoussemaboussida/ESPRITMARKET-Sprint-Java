package controllers;

import entities.Dons;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.DonsService;
import service.utilisateurService;
import entities.utilisateur;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;


import java.text.SimpleDateFormat;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Optional;

public class FaireDonsController {

    @FXML
    private Label donsLabel;


    @FXML
    private TableView<Dons> donsTable;

    @FXML
    private TableColumn<Dons, String> nomUserColumn;

    @FXML
    private TableColumn<Dons, String> prenomUserColumn;




    @FXML
    private TableColumn<Dons, Integer> nbPointsColumn;

    @FXML
    private TableColumn<Dons, String> dateAjoutColumn;

    @FXML
    private TableColumn<Dons, String> etatStatutDonsColumn;


    @FXML
    private Label pointsLabel;

    @FXML
    private TextField donPointsField;

    private DonsService donsService;
    private utilisateurService userService;
    private utilisateur utilisateur;




    public FaireDonsController() {
        donsService = new DonsService();
        userService = new utilisateurService();
    }
    @FXML
    private Label pointsDisponiblesLabel;

    @FXML
    void initialize() {
        // Récupérer l'utilisateur avec l'ID 1
        utilisateur user = utilisateurService.getUserById(1);
        if (user != null) {
            // Afficher les points disponibles dans le label
            int nbPointsDisponibles = user.getNbPoints();
            pointsDisponiblesLabel.setText(String.valueOf(nbPointsDisponibles));
        } else {
            // Gérer le cas où l'utilisateur n'est pas trouvé
            pointsDisponiblesLabel.setText("Utilisateur non trouvé");
        }
        loadDons(1);
    }




    public void setUser(utilisateur user) {
        this.utilisateur = user;
        updatePointsLabel();
    }

    private void updatePointsLabel() {
        int pointsUtilisateur = userService.getUserById(utilisateur.getIdUser()).getNbPoints();
        pointsLabel.setText("Points disponibles : " + pointsUtilisateur);
    }

    @FXML
    private void handleSupprimerDon(Dons don) {
        boolean success = donsService.supprimerDons(don);
        if (success) {
            // Mettez à jour l'affichage ou fournissez un retour d'information approprié
            System.out.println("Don supprimé avec succès");
        } else {
            // Gérer le cas où la suppression a échoué
            System.out.println("Erreur lors de la suppression du don");
        }
        loadDons(1);
    }



    @FXML
    private void handleAjouterDon(ActionEvent event) {
        // Récupérer l'utilisateur avec l'ID 1 (ou autre ID approprié)
        utilisateur user = utilisateurService.getUserById(1);

        String donPointsText = donPointsField.getText();
        if (donPointsText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Champ vide", "Veuillez saisir le nombre de points.");
            return;
        }

        try {
            int donPoints = Integer.parseInt(donPointsText);

            if (donPoints <= 0) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Points invalides", "Veuillez entrer un nombre de points valide.");
                return;
            }

            if (donPoints > user.getNbPoints()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Points insuffisants", "Vous n'avez pas suffisamment de points pour effectuer ce don.");
                return;
            }

            int remainingPoints = donsService.addDonsWithStatus(user, donPoints);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Don effectué", "Votre don a été ajouté avec succès.");

            // Mettre à jour l'affichage des points dans l'interface utilisateur
            remainingPoints = user.getNbPoints() - donPoints;
            pointsDisponiblesLabel.setText(String.valueOf(remainingPoints));
            loadDons(1); // Remplacez 1 par l'ID de l'utilisateur approprié
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ouverture", "Impossible d'ouvrir la vue pour supprimer un don.");
        }
    }


    private void showModifierDialog(Dons don) {
        // Créer une boîte de dialogue pour modifier les points du don
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Modifier les points du don");
        dialog.setHeaderText(null);
        dialog.setContentText("Entrez le nouveau nombre de points:");

        // Afficher la boîte de dialogue et attendre la saisie de l'utilisateur
        Optional<String> result = dialog.showAndWait();

        // Traiter la saisie de l'utilisateur
        result.ifPresent(newPoints -> {
            try {
                int newPointsValue = Integer.parseInt(newPoints);
                // Mettre à jour les points du don dans la base de données
                don.setNbPoints(newPointsValue);
                donsService.updateDons(don); // Mettre à jour le don dans la base de données
                // Mettre à jour l'affichage
                loadDons(1);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Points invalides", "Veuillez entrer un nombre de points valide.");
            }
        });

    }



    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void loadDons(int userId) {
        utilisateur user = utilisateurService.getUserById(userId);
        if (user != null) {
            VBox vbox = new VBox(); // Conteneur pour les boutons et le texte

            List<Dons> donsList = donsService.getDonsByUserId(userId);
            for (Dons don : donsList) {
                HBox hbox = new HBox(); // Conteneur pour un bouton et le texte du don

                // Créer le texte du don
                Label donLabel = new Label("L'état de votre don de " + don.getNbPoints() +
                        " points effectué le " + don.getDate_ajout() +
                        " est " + don.getEtatStatutDons());

                Button btnUpdate = new Button();

                {
                    // Ajouter une icône au bouton
                    Image image = new Image(getClass().getResourceAsStream("/img/modifier.png"));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(16); // Ajustez la taille de l'icône si nécessaire
                    imageView.setFitHeight(16);
                    btnUpdate.setGraphic(imageView);

                    // Définir l'action du bouton
                    btnUpdate.setOnAction(event -> {
                        showModifierDialog(don);
                    });
                }




                // Créer un conteneur pour les boutons
                HBox buttonsContainer = new HBox(btnUpdate);

                // Ajouter les éléments dans l'ordre souhaité à la HBox
                hbox.getChildren().addAll(donLabel, buttonsContainer);

                // Ajouter le conteneur hbox au conteneur vbox
                vbox.getChildren().add(hbox);
            }

            // Afficher les dons avec les boutons dans le label "donsLabel"
            donsLabel.setGraphic(vbox);
        } else {
            System.out.println("Utilisateur non trouvé");
        }
    }



}
