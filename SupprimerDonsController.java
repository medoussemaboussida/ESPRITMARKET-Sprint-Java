package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import entities.Dons;
import service.DonsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SupprimerDonsController {

    @FXML
    private TableView<Dons> donsTableView;

    @FXML
    private TableColumn<Dons, Integer> idDonsColumn;

    @FXML
    private TableColumn<Dons, Integer> idUserColumn;

    @FXML
    private TableColumn<Dons, Integer> nbPointsColumn;

    @FXML
    private TableColumn<Dons, String> dateAjoutColumn;

    @FXML
    private Button supprimerButton;

    private DonsService donsService;

    public SupprimerDonsController() {
        this.donsService = new DonsService();
    }

    @FXML
    void initialize() {
        // Initialiser les colonnes de la table
        idDonsColumn.setCellValueFactory(cellData -> cellData.getValue().idDonsProperty().asObject());
        idUserColumn.setCellValueFactory(cellData -> cellData.getValue().idUserProperty().asObject());
        nbPointsColumn.setCellValueFactory(cellData -> cellData.getValue().nbPointsProperty().asObject());
        dateAjoutColumn.setCellValueFactory(cellData -> cellData.getValue().date_ajoutProperty().asString());

        // Charger les dons existants à partir de la base de données et les afficher dans la table
        List<Dons> donsList = donsService.getAllDons();
        ObservableList<Dons> observableDonsList = FXCollections.observableArrayList(donsList);
        donsTableView.setItems(observableDonsList);
    }

    @FXML
    private void handleSupprimerDon() {
        // Récupérer le don sélectionné dans la table
        Dons donSelectionne = donsTableView.getSelectionModel().getSelectedItem();
        if (donSelectionne != null) {
            // Appeler la méthode supprimerDon de votre service DonsService
            boolean success = donsService.supprimerDon(donSelectionne.getIdDons(), donSelectionne.getNbPoints());
            if (success) {
                // Afficher une confirmation si la suppression a réussi
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Don supprimé avec succès.");
                // Rafraîchir la table après la suppression
                donsTableView.getItems().remove(donSelectionne);
            } else {
                // Afficher un message d'erreur si la suppression a échoué
                showAlert(Alert.AlertType.ERROR, "Erreur", "La suppression du don a échoué.");
            }
        } else {
            // Aucun don sélectionné, afficher un message d'erreur
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un don à supprimer.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setDonsService(DonsService donsService) {
    }
}
