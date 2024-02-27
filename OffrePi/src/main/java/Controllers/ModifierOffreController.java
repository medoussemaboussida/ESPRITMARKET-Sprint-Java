package Controllers;

import Service.OffreService;
import Service.ProduitService;
import entities.Offre;
import entities.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ModifierOffreController implements Initializable {

    private OffreService offreService;
    private ProduitService produitService = new ProduitService();  ;

    private final OffreService os = new OffreService();
    private Offre offre;
    public void setOffreService(OffreService offreService) {
        this.offreService = offreService;
    }

    @FXML
    private ImageView imageView;

    @FXML
    private ListView<Produit> ListeViewAModifier;

    @FXML
    private TextField reductionTF;

    @FXML
    private TextField titreTF; // Correspond au champ du titre dans le FXML
    @FXML
    private TextField descriptionTF; // Correspond au champ de la description dans le FXML
    @FXML
    private DatePicker debutTF; // Correspond au champ de la date de début dans le FXML
    @FXML
    private DatePicker finTF;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Produit> produits = FXCollections.observableArrayList(produitService.getAllProduits());
        ListeViewAModifier.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ListeViewAModifier.setItems(produits);
        // Enforce numeric input
        reductionTF.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            if (!keyEvent.getCharacter().matches("\\d")) { // Regex to allow only digits
                keyEvent.consume();
            }
        });
    }
    public void processInput() {
        try {
            int intValue = Integer.parseInt(reductionTF.getText());
            // Use intValue as needed
            System.out.println("Integer value: " + intValue);
        } catch (NumberFormatException e) {
            // Handle case where input is not a valid integer
            System.err.println("Input is not a valid integer.");
        }
    }

    String filepath = null, filename = null, fn = null;

    String htdocsPath = "C:/xampp/htdocs";

    FileChooser fc = new FileChooser();

    @FXML
    void modifierImage(ActionEvent event) throws SQLException, FileNotFoundException, IOException {
        File file = fc.showOpenDialog(null);
        // Shows a new file open dialog.
        if (file != null) {
            // URI that represents this abstract pathname
            imageView.setImage(new Image(file.toURI().toString()));

            filename = file.getName();
            filepath = file.getAbsolutePath();

            fn = filename;

            FileChannel source = new FileInputStream(filepath).getChannel();
            FileChannel dest = new FileOutputStream(htdocsPath + filename).getChannel();
            dest.transferFrom(source, 0, source.size());
        } else {
            System.out.println("Fichier invalide!");
        }
    }

   @FXML
   void modifier(ActionEvent event) throws IOException {
       // Récupérer les données de l'offre à modifier
      // Offre offreAModifier = tableView.getSelectionModel().getSelectedItem();

      /* if (offreAModifier == null) {
           // Gérer le cas où aucun élément n'est sélectionné dans le TableView
           System.out.println("Aucune offre sélectionnée pour la modification.");
           return;
       }*/

       // Récupérer les nouvelles valeurs des champs de texte et des autres contrôles
       String nouveauNomOffre = titreTF.getText();
       String nouvelleDescriptionOffre = descriptionTF.getText();
       LocalDate nouvelleDateDebut = debutTF.getValue();
       LocalDate nouvelleDateFin = finTF.getValue();
       int nouvelleReductionOffre = Integer.parseInt(reductionTF.getText());

       // Vérifier que les champs obligatoires sont remplis
       if (nouveauNomOffre.isEmpty() || nouvelleDescriptionOffre.isEmpty() || nouvelleDateDebut == null || nouvelleDateFin == null) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Erreur");
           alert.setHeaderText(null);
           alert.setContentText("Veuillez remplir tous les champs.");
           alert.showAndWait();
           return;
       }


       // Récupérer les produits sélectionnés par l'utilisateur
       ObservableList<Produit> produitsSelectionnes = ListeViewAModifier.getSelectionModel().getSelectedItems();

       if (produitsSelectionnes.isEmpty()) {
           afficherAlerteErreur("Veuillez sélectionner au moins un produit.");
           return;
       }

       // Mettre à jour les propriétés de l'offre à modifier avec les nouvelles valeurs
       offre.setNomOffre(nouveauNomOffre);
       offre.setDescriptionOffre(nouvelleDescriptionOffre);
       offre.setDateDebut(Date.valueOf(nouvelleDateDebut));
       offre.setDateFin(Date.valueOf(nouvelleDateFin));
       offre.setReduction(nouvelleReductionOffre);
       // Vous pouvez également mettre à jour d'autres propriétés si nécessaire

       // Mettre à jour les produits de l'offre à modifier avec les nouveaux produits sélectionnés
       List<Produit> nouveauxProduits = produitsSelectionnes.stream().collect(Collectors.toList());
       offre.setProduits(nouveauxProduits);

       // Appeler la méthode de service pour mettre à jour l'offre dans la base de données
       os.updateOffre(offre);

       // Afficher une boîte de dialogue pour informer l'utilisateur que l'offre a été modifiée avec succès
       Alert a = new Alert(Alert.AlertType.INFORMATION);
       a.setTitle("Succès");
       a.setContentText("Offre Modifiée");
       a.showAndWait();

       // Recharger la vue AfficherOffre pour refléter les modifications
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOffre.fxml"));
       Parent root = loader.load();
       AfficherOffreController controller = loader.getController();
       controller.update(null); // Mettez à jour la liste des offres dans la vue AfficherOffre
       Stage stage = new Stage();
       stage.setScene(new Scene(root));
       stage.show();

       // Fermer la fenêtre actuelle après la modification de l'offre
       Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
       currentStage.close();
   }


    public void setOffreData(Offre offre) {
            this.offre = offre;
            if (offre != null) {
                titreTF.setText(offre.getNomOffre());
                descriptionTF.setText(offre.getDescriptionOffre());
                java.util.Date dateDebutUtil = new java.util.Date(offre.getDateDebut().getTime());
                java.util.Date dateFinUtil = new java.util.Date(offre.getDateFin().getTime());
                reductionTF.setText(String.valueOf(offre.getReduction()));
                // Convertir java.util.Date en LocalDate
                debutTF.setValue(dateDebutUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                finTF.setValue(dateFinUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                // Chargement de l'image
                try {
                    // Charger l'image à partir du fichier et l'afficher dans l'ImageView

                    File file = new File(htdocsPath + offre.getImageOffre());
                    if (file.exists()) {
                        Image image = new Image(file.toURI().toString());
                        imageView.setImage(image);
                    } else {
                        System.out.println("Le fichier spécifié n'existe pas : " + file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Afficher l'erreur dans la console
                }
            }
        }

    // Méthode pour annuler l'opération et fermer la fenêtre
    @FXML
    private void annuler() {
        // Fermer simplement la fenêtre
        Stage stage = (Stage) titreTF.getScene().getWindow();
        stage.close();
    }
    private void afficherAlerteErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

   /* @FXML
    private void modifier(ActionEvent event) {
        // Récupérez les valeurs des TextField
        String titre = titreTF.getText();
        System.out.println("hhhhhhhhhh"+titreTF.getText());
        String description = descriptionTF.getText();
        int reduction = Integer.parseInt(reductionTF.getText());

        Date dateDebut = Date.valueOf(debutTF.getValue());
        Date dateFin = Date.valueOf(finTF.getValue());

        // Mise à jour de l'offre avec les nouvelles valeurs
        offre.setNomOffre(titre);
        offre.setDescriptionOffre(description);
        offre.setDateDebut(dateDebut);
        offre.setDateFin(dateFin);
        offre.setReduction(reduction);
        System.out.println("offfreee for update " + offre);
        // Vérifier si le champ d'image n'est pas vide
        if (filename != null && !filename.isEmpty()) {
            offre.setImageOffre(filename);
        }

        // Utilisez la variable de classe offreService pour appeler la méthode updateOffre
        os.updateOffre(offre);

        // Afficher une confirmation à l'utilisateur
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setContentText("mise à jour de l'offre faite avec succèes.");

        alert.showAndWait();

        // Fermer la fenêtre après la mise à jour
        Stage stage = (Stage) titreTF.getScene().getWindow();
        stage.close();
    }
*/