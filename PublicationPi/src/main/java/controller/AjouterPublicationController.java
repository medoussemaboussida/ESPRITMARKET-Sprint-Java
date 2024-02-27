package controller;

import entities.Publication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import service.PublicationService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;

public class AjouterPublicationController {
    private final PublicationService ps = new PublicationService();

    @FXML
    private TextField descriptionTF;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField titreTF;

    String filepath = null, filename = null, fn = null;

    String uploads = "C:/Users/yassi/Desktop/ESPRIT/Java/PublicationPi/src/main/java/image/";

    FileChooser fc = new FileChooser();

    @FXML
    void ajouterImagePublication(ActionEvent event) throws IOException {
        // Chemin vers le répertoire htdocs de XAMPP
        String htdocsPath = "C:/xampp/htdocs"; // Changez le chemin selon votre installation

        File file = fc.showOpenDialog(null);
        // Shows a new file open dialog.
        if (file != null) {
            // URI that represents this abstract pathname
            imageView.setImage(new Image(file.toURI().toString()));

            filename = file.getName();
            filepath = file.getAbsolutePath();

            fn = filename;

            // Chemin de destination dans le répertoire htdocs
            String destinationPath = htdocsPath + "/" + filename;

            // Copie du fichier vers le répertoire htdocs
            Files.copy(Paths.get(filepath), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
        } else {
            System.out.println("Fichier invalide!");
        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        // Obtenir la date de publication actuelle
        LocalDate datePublication = LocalDate.now();

        try {
            // Convertir LocalDate en Date
            Date datePublicationDate = java.sql.Date.valueOf(datePublication);

            // Validation des champs de saisie
            String description = descriptionTF.getText();
            String titre = titreTF.getText();

            if (!isValidInput(description, titre)) {
                return;
            }

            // Enregistrer la publication avec la date de publication actuelle
            ps.addPublication(new Publication(
                    titre,
                    datePublicationDate,
                    description,
                    filename
            ));

            // Afficher une confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Publication ajoutée avec succès.");
            alert.showAndWait();

            // Effacer les champs après l'ajout
            titreTF.clear();
            descriptionTF.clear();

        } catch (Exception e) {
            // Afficher une alerte en cas d'erreur lors de l'ajout
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur est survenue lors de l'ajout de la publication: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private boolean isValidInput(String description, String titre) {
        if (titre == null || titre.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le titre est requis.");
            return false;
        }
        if (description == null || description.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "La description est requise.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void clear(ActionEvent event) {
        // Effacer le contenu de tous les champs de texte
        titreTF.clear();
        descriptionTF.clear();
    }
}
