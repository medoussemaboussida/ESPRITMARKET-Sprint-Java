package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.Publication;
import entities.commentaire;
import entities.user;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import service.CommentaireService;
import service.PublicationService;
import service.UserService;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FrontPublicationController implements Initializable {

    @FXML
    private ListView<VBox> listView;

    private PublicationService publicationService;
    private CommentaireService commentaireService;
    private UserService userService;

    private user utilisateur;

    private String htdocsPath = "C:/xampp/htdocs/";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int idUtilisateur = 4;
        userService = new UserService();
        utilisateur = userService.findById(idUtilisateur);
        if (utilisateur != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Bienvenue");
            alert.setHeaderText("Bienvenue " + utilisateur.getPrenomUser());
            alert.setContentText("Vous êtes connecté avec succès!\n"
                    + "Nom: " + utilisateur.getNomUser() + "\n"
                    + "Prenom: " + utilisateur.getPrenomUser());
            alert.showAndWait();
        } else {
            System.out.println("Utilisateur non trouvé");
        }

        publicationService = new PublicationService();
        commentaireService = new CommentaireService();

        loadPublications();
    }

    private void loadPublications() {
        ObservableList<VBox> publicationBoxes = FXCollections.observableArrayList();

        List<Publication> publications = publicationService.readAll();

        for (Publication publication : publications) {
            VBox publicationBox = new VBox();
            publicationBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: black; -fx-border-radius: 10px; -fx-padding: 10px;");

            Label titleLabel = new Label(publication.getTitrePublication());
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-family: 'Arial'; -fx-font-size: 24px;");
            titleLabel.setAlignment(Pos.CENTER);

            ImageView imageView = new ImageView();
            try {
                Image image = new Image("file:///" + htdocsPath + publication.getImagePublication());
                imageView.setImage(image);
                imageView.setFitWidth(400);
                imageView.setFitHeight(300);
                VBox.setMargin(imageView, new Insets(10, 0, 10, 0));
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }

            Label descriptionLabel = new Label(publication.getDescription());
            descriptionLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16px; -fx-wrap-text: true;");
            descriptionLabel.setAlignment(Pos.CENTER);
            VBox.setMargin(descriptionLabel, new Insets(0, 10, 10, 10));

            List<commentaire> commentaires = commentaireService.readAll(publication.getIdPublication());

            VBox commentairesBox = new VBox();
            commentairesBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: black; -fx-border-radius: 10px; -fx-padding: 10px;");
            for (commentaire commentaire : commentaires) {
                user commentaireUser = commentaire.getIdUser();

                Label commentaireLabel = new Label(commentaireUser.getNomUser() + " " + commentaireUser.getPrenomUser() + " : " + commentaire.getDescriptionCommentaire());
                commentaireLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 14px; -fx-wrap-text: true;");
                VBox.setMargin(commentaireLabel, new Insets(5, 0, 5, 0));

                if (commentaireUser.getIdUser() == utilisateur.getIdUser()) {
                    FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                    deleteIcon.setStyle("-fx-cursor: hand; -glyph-size: 20px; -fx-fill: #ff1544;");
                    deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmationDialog.setTitle("Confirmation de suppression");
                        confirmationDialog.setHeaderText("Voulez-vous vraiment supprimer ce commentaire ?");
                        Optional<ButtonType> result = confirmationDialog.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            commentaireService.deleteCommentaire(commentaire.getIdCommentaire());
                            loadPublications();
                        }
                    });

                    FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                    editIcon.setStyle("-fx-cursor: hand; -glyph-size: 20px; -fx-fill: #00E676;");
                    editIcon.setOnMouseClicked((MouseEvent event) -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCommentaire.fxml"));
                            Parent root = loader.load();
                            ModifierCommentaireController controller = loader.getController();
                            controller.initData(commentaire);
                            Scene scene = new Scene(root);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    HBox commentBox = new HBox(commentaireLabel, deleteIcon, editIcon);
                    commentBox.setSpacing(10);
                    commentBox.setAlignment(Pos.CENTER_LEFT);
                    commentairesBox.getChildren().add(commentBox);
                } else {
                    commentairesBox.getChildren().add(commentaireLabel);
                }
            }

            Button addCommentButton = new Button("Ajouter un commentaire");
            addCommentButton.setStyle(
                    "-fx-background-color: #f58c1e; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px;" // Optionnel : ajustez la taille de la police si nécessaire
            );
            addCommentButton.setAlignment(Pos.CENTER);
            addCommentButton.setOnAction(e -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterCommentaire.fxml"));
                try {
                    Parent root = loader.load();
                    AjouterCommentaireController controller = loader.getController();
                    controller.initData(utilisateur, publication);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            VBox.setMargin(addCommentButton, new Insets(10, 0, 0, 0));

            publicationBox.getChildren().addAll(titleLabel, imageView, descriptionLabel, commentairesBox, addCommentButton);
            publicationBox.setAlignment(Pos.CENTER);
            publicationBox.setSpacing(10);

            publicationBoxes.add(publicationBox);
        }

        listView.setItems(publicationBoxes);
    }

    @FXML
    void refresh(MouseEvent event) {
        loadPublications();
    }
}
