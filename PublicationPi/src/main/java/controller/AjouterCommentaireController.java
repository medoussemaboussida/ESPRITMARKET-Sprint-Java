package controller;

import entities.Publication;
import entities.commentaire;
import entities.user;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.CommentaireService;
import service.UserService;
import service.PublicationService;

public class AjouterCommentaireController {

    @FXML
    private TextField descriptionCM;

    private CommentaireService commentaireService;
    private UserService userService;
    private PublicationService publicationService;
    private user utilisateur;
    private Publication publication;

    public AjouterCommentaireController() {
        commentaireService = new CommentaireService();
        userService = new UserService();
        publicationService = new PublicationService();
    }

    public void initData(user utilisateur, Publication publication) {
        this.utilisateur = utilisateur;
        this.publication = publication;
    }

    @FXML
    void ajouter(ActionEvent event) {
        // Récupérer la description du commentaire à partir du champ de texte
        String description = descriptionCM.getText().trim();

        // Vérifier si la description est vide
        if (description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer une description pour le commentaire !");
            alert.showAndWait();
            return;
        }

        // Vérifier que l'utilisateur et la publication sont définis
        if (utilisateur == null || publication == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ajouter le commentaire. Utilisateur ou publication non défini !");
            alert.showAndWait();
            return;
        }

        // Créer un nouvel objet commentaire
        commentaire newComment = new commentaire();
        newComment.setDescriptionCommentaire(description);
        newComment.setIdUser(utilisateur); // Utilisateur connecté
        newComment.setIdPublication(publication); // Publication actuelle

        // Ajouter le commentaire à la base de données en utilisant le service
        commentaireService.addCommentaire(newComment);

        // Afficher une confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Le commentaire a été ajouté avec succès !");
        alert.showAndWait();

        // Effacer le contenu du champ de texte après l'ajout
        descriptionCM.clear();

        // Fermer la fenêtre de modification après la mise à jour
        Stage stage = (Stage) descriptionCM.getScene().getWindow();
        stage.close();

    }

    @FXML
    void clear(ActionEvent event) {
        // Effacer le contenu du champ de texte
        descriptionCM.clear();
    }
}