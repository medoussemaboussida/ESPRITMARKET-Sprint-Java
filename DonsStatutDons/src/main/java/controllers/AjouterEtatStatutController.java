package controllers;

import entities.Dons;
import entities.utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import service.DonsService;
import service.utilisateurService;

import java.sql.SQLException;
import java.util.List;


public class AjouterEtatStatutController {

    private final DonsService donsService = new DonsService();
    private final utilisateurService utilisateurService = new utilisateurService();

    @FXML
    private ComboBox<utilisateur> utilisateurComboBox;

    @FXML
    private ComboBox<String> etatComboBox;

    @FXML
    private ComboBox<Dons> donsComboBox;

    @FXML
    void initialize() {
        // Récupérer la liste des dons avec les détails des utilisateurs
        List<Dons> donsList = donsService.getAllDonsWithUserDetails();
        ObservableList<Dons> observableDonsList = FXCollections.observableArrayList(donsList);
        donsComboBox.setItems(observableDonsList);

        // Définir la cellFactory personnalisée pour afficher les dons dans le ComboBox
        donsComboBox.setCellFactory(param -> new DonListCell());

        // Définir les options de la ComboBox d'état
        ObservableList<String> etatOptions = FXCollections.observableArrayList("Reçu", "En cours");
        etatComboBox.setItems(etatOptions);
    }

    private static class DonListCell extends ListCell<Dons> {
        @Override
        protected void updateItem(Dons don, boolean empty) {
            super.updateItem(don, empty);

            if (empty || don == null) {
                setText(null);
            } else {
                // Personnalisez l'affichage du don ici
                setText(don.getNomUser() + " " + don.getPrenomUser() + " - " + don.getEmailUser() + " - Points: " + don.getNbPoints() + " - Statut: " + don.getEtatStatutDons());
            }
        }
    }



    @FXML
    void handleAjouterEtatStatut() {
        Dons selectedDon = donsComboBox.getValue();
        String selectedEtat = etatComboBox.getValue();

        if (selectedDon != null && selectedEtat != null) {
            int idDons = selectedDon.getIdDons(); // Récupérer l'ID du don à partir de la liste des dons
            try {
                donsService.addEtatStatutDons(idDons, selectedEtat);
                System.out.println("État du statut de don ajouté avec succès pour le don avec l'ID " + idDons);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Veuillez sélectionner un don et un état.");
        }
    }






}