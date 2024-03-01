package controllers;

import entities.DemandeDons;
import entities.utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import service.DemandeDonsService;

import java.awt.event.ActionEvent;
import java.util.Optional;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import service.PDFExporterService;
import service.utilisateurService;



import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class DemandeDonsController {
    private int userIdConnecte = 1; // Définir directement l'ID de l'utilisateur connecté à 1


    @FXML
    private TextArea contenuTextArea;

    @FXML
    private Button posterDemandeButton;

    @FXML
    private ImageView imageView;

    @FXML
    private ListView<DemandeDons> demandeListView;

    private DemandeDonsService demandeDonsService;
    private utilisateurService userService;
    private entities.utilisateur utilisateur;
    private PDFExporterService pdfExporterService;


    public DemandeDonsController() {
        demandeDonsService = new DemandeDonsService();
        userService = new utilisateurService(); // Initialize the userService
        pdfExporterService = new PDFExporterService(); // Ajoutez cette ligne pour initialiser pdfExporterService



    }
    @FXML
    public void initialize() {
        // Personnaliser l'affichage des demandes dans la ListView
        demandeListView.setCellFactory(param -> new ListCell<DemandeDons>() {
            @Override
            protected void updateItem(DemandeDons demande, boolean empty) {
                super.updateItem(demande, empty);
                if (empty || demande == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Construire le texte à afficher dans la cellule
                    StringBuilder sb = new StringBuilder();
                    sb.append("Utilisateur: ").append(demande.getNomUser()).append(" ").append(demande.getPrenomUser());
                    sb.append("\nContenu: ").append(demande.getContenu());
                    sb.append("\nDate de publication: ").append(demande.getDatePublication());
                    sb.append("\nPoints gagnés: ").append(demande.getNbPoints());

                    // Créer un bouton Supprimer avec une icône
                    Button deleteButton = new Button("Supprimer");
                    ImageView deleteImageView = new ImageView(new Image(getClass().getResourceAsStream("/img/deleteimg.png")));
                    deleteImageView.setFitWidth(16);
                    deleteImageView.setFitHeight(16);
                    deleteButton.setGraphic(deleteImageView);

                    // Vérifier si l'utilisateur connecté est l'auteur de la demande
                    if (demande.getIdUtilisateur() == userIdConnecte) {
                        deleteButton.setOnAction(event -> deleteDemande(demande));
                    } else {
                        // Si l'utilisateur n'est pas l'auteur de la demande, ne pas créer le bouton Supprimer
                        deleteButton.setVisible(false);
                        deleteButton.setManaged(false);
                    }

                    // Vérifier si l'utilisateur connecté est l'auteur de la demande pour afficher le bouton "Exporter en PDF"
                    if (demande.getIdUtilisateur() == userIdConnecte) {
                        // Créer un bouton Exporter en PDF
                        Button exportButton = new Button("Exporter en PDF");
                        ImageView exportImageView = new ImageView(new Image(getClass().getResourceAsStream("/img/exporter.png")));
                        exportImageView.setFitWidth(16);
                        exportImageView.setFitHeight(16);
                        exportButton.setGraphic(exportImageView);
                        exportButton.setOnAction(event -> exportDemandToPDF(demande));
                        exportButton.setOnAction(event -> exportDemandToPDF(demande));

                        // Créer un conteneur pour afficher le texte et les boutons
                        VBox container = new VBox(new Label(sb.toString()), deleteButton, exportButton);
                        setGraphic(container);
                    } else {
                        // Créer un conteneur pour afficher le texte et le bouton Exporter en PDF
                        VBox container = new VBox(new Label(sb.toString()), deleteButton);
                        setGraphic(container);
                    }
                }
            }
        });
        // Charger les demandes existantes lors de l'initialisation
        loadDemandes();

        // Ajouter un écouteur d'événement au bouton "Poster Demande"
        posterDemandeButton.setOnAction(event -> posterDemande());
    }



    @FXML
    private void exportDemandToPDF(DemandeDons demande) {
        // Afficher un sélecteur de fichier pour choisir l'emplacement de sauvegarde du fichier PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf"));
        fileChooser.setInitialFileName("demande_" + demande.getIdDemande() + ".pdf");
        Stage stage = (Stage) demandeListView.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        // Si un fichier est sélectionné, exporter la demande en PDF
        if (file != null) {
            boolean success = pdfExporterService.exportDemandToPDF(demande, file);
            if (success) {
                afficherAlerte("Succès", "La demande a été exportée avec succès en PDF.");
            } else {
                afficherAlerte("Erreur", "Une erreur est survenue lors de l'exportation en PDF.");
            }
        }
    }





    private void deleteDemande(DemandeDons demande) {
        // Vérifier si l'utilisateur connecté est l'auteur de la demande
        int userIdConnecte = 1; // ID de l'utilisateur connecté (à remplacer par l'ID réel de l'utilisateur connecté)
        if (demande.getIdUtilisateur() == userIdConnecte) {
            // Confirmer la suppression avec une boîte de dialogue
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation de suppression");
            confirmationDialog.setHeaderText("Voulez-vous vraiment supprimer cette demande ?");
            confirmationDialog.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = confirmationDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Appeler le service pour supprimer la demande
                if (demandeDonsService.supprimerDemande(demande.getIdDemande())) {
                    // Recharger les demandes après la suppression
                    loadDemandes();
                } else {
                    afficherAlerte("Erreur", "Erreur lors de la suppression de la demande.");
                }
            }
        } else {
            afficherAlerte("Erreur", "Vous n'êtes pas autorisé à supprimer cette demande.");
        }
    }



    public void setUserIdConnecte(int userId) {
        this.userIdConnecte = userId;
    }
    @FXML
    public void posterDemande() {
        // Vérifier si un utilisateur est connecté
        if (userIdConnecte != 0) {
            // Récupérer le contenu de la demande
            String contenu = contenuTextArea.getText();

            // Créer un objet DemandeDons avec les informations
            DemandeDons nouvelleDemande = new DemandeDons();
            nouvelleDemande.setIdUtilisateur(userIdConnecte); // Utiliser l'ID de l'utilisateur connecté
            nouvelleDemande.setContenu(contenu);

            // Poster la demande en utilisant le service
            demandeDonsService.posterDemande(nouvelleDemande);

            // Réinitialiser le contenu du TextArea après la soumission de la demande
            contenuTextArea.clear();

            // Recharger les demandes pour afficher la nouvelle demande
            loadDemandes();
        } else {
            // Afficher un message d'erreur si aucun utilisateur n'est connecté
            afficherAlerte("Erreur", "Aucun utilisateur connecté.");
        }
    }


    @FXML
    public void transferPoints() {
        utilisateur sender = utilisateurService.getUserById(1); // Récupérer l'utilisateur avec l'ID 1 (l'utilisateur qui transfère les points)
        if (sender != null) {
            DemandeDons selectedDemande = demandeListView.getSelectionModel().getSelectedItem();
            if (selectedDemande != null) {
                // Demander à l'utilisateur le nombre de points à transférer
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Transférer des points");
                dialog.setHeaderText("Transférer des points à l'utilisateur");
                dialog.setContentText("Entrez le nombre de points à transférer:");
                Optional<String> result = dialog.showAndWait();

                // Vérifier si l'utilisateur a saisi un nombre valide
                if (result.isPresent()) {
                    try {
                        int donPoints = Integer.parseInt(result.get());
                        if (donPoints > 0) {
                            int userId = selectedDemande.getIdUtilisateur(); // Récupérer l'ID de l'utilisateur associé à la demande
                            int idDemande = selectedDemande.getIdDemande(); // Récupérer l'ID de la demande
                            // Vérifier si l'utilisateur a suffisamment de points
                            int userPoints = demandeDonsService.getUserPoints(userId);
                            if (userPoints >= donPoints) {
                                // Ajouter les points transférés à l'utilisateur qui a fait la demande
                                utilisateur receiver = utilisateurService.getUserById(userId);
                                if (receiver != null) {
                                    int updatedReceiverPoints = receiver.getNbPoints() + donPoints;
                                    userService.updateUserPoints(userId, updatedReceiverPoints);
                                } else {
                                    afficherAlerte("Erreur", "Utilisateur non trouvé.");
                                    return;
                                }

                                // Soustraire les points transférés des points disponibles de l'utilisateur qui transfère
                                int updatedSenderPoints = sender.getNbPoints() - donPoints;
                                userService.updateUserPoints(sender.getIdUser(), updatedSenderPoints);

                                // Appeler la méthode pour ajouter les dons pour la demande sélectionnée
                                int donId = demandeDonsService.addDonsForDemande(userId, donPoints, idDemande);

                                // Mettre à jour les points gagnés dans la demande sélectionnée en ajoutant les nouveaux points aux points existants
                                if (donId != -1) { // Vérifier si l'ajout des dons a réussi
                                    selectedDemande.setNbPoints(selectedDemande.getNbPoints() + donPoints);
                                    selectedDemande.setTotalPointsGagnes(selectedDemande.getTotalPointsGagnes() + donPoints);

                                    // Rafraîchir l'affichage des demandes pour refléter les modifications
                                    loadDemandes();
                                } else {
                                    afficherAlerte("Erreur", "Erreur lors de l'ajout des points pour la demande.");
                                }
                            } else {
                                afficherAlerte("Erreur", "Points insuffisants. Vous avez actuellement " + userPoints + " points.");
                            }
                        } else {
                            afficherAlerte("Erreur", "Veuillez saisir un nombre valide de points.");
                        }
                    } catch (NumberFormatException e) {
                        afficherAlerte("Erreur", "Veuillez saisir un nombre entier valide.");
                    }
                }
            } else {
                afficherAlerte("Erreur", "Veuillez sélectionner une demande pour transférer des points.");
            }
        } else {
            afficherAlerte("Erreur", "Utilisateur expéditeur non trouvé.");
        }
    }






    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




    private void loadDemandes() {
        demandeListView.getItems().clear();
        demandeListView.getItems().addAll(demandeDonsService.getDemandesAvecUtilisateurs());
    }
}
