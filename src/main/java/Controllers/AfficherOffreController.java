package Controllers;

import Service.OffreService;
import entities.Offre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AfficherOffreController implements Initializable {
    private OffreService offreService;
    @FXML
    private TableColumn<Offre, String> rNom;
    @FXML
    private TableColumn<Offre, Void> actionCol;

    @FXML
    private TableView<Offre> rList;
    @FXML
    private TableColumn<Offre, Date> rDebut;
    @FXML
    private TableColumn<Offre, String> rDesc;
    @FXML
    private TableColumn<Offre, Integer> rId;
    @FXML
    private TableColumn<Offre, Date> rFin;
    @FXML
    private TextField searchField;
    @FXML
    private Button triTitre;

    @FXML
    private Button trieDate;
    @FXML
    void search(MouseEvent event) {
        String keyword = searchField.getText().trim().toLowerCase();
        List<Offre> filteredList = originalOffreList.stream()
                .filter(offre -> offre.getNomOffre().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        offreList.setAll(filteredList); // Mettez à jour offreList avec la nouvelle liste filtrée
    }


    private ObservableList<Offre> offreList;

    private ObservableList<Offre> originalOffreList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            // Récupérer les données des offres depuis le service
            OffreService offreService = new OffreService();
            List<Offre> offres = offreService.readOffre();
            configureActionColumn();

            offreList = FXCollections.observableArrayList();
            originalOffreList = FXCollections.observableArrayList(offres); // Initialisez la liste originale


            // Créer une ObservableList à partir des offres récupérées
            ObservableList<Offre> offreList = FXCollections.observableArrayList(offres);

            // Associer les propriétés des objets Offre aux colonnes de la TableView
            rId.setCellValueFactory(new PropertyValueFactory<>("idOffre"));
            rNom.setCellValueFactory(new PropertyValueFactory<>("nomOffre"));
            rDesc.setCellValueFactory(new PropertyValueFactory<>("descriptionOffre"));
            rDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
            rFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));

            // Définir les données de la TableView
            rList.setItems(offreList);
        } catch (Exception e) {
            // Gérer les exceptions
            Logger.getLogger(AfficherOffreController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    private void configureActionColumn() {
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox pane = new HBox(editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Offre offre = getTableView().getItems().get(getIndex());
                    // Logique pour modifier l'offre
                    openModifierOffreDialog(offre);
                });
                deleteButton.setOnAction(event -> {
                    Offre offre = getTableView().getItems().get(getIndex());
                    // Logique pour supprimer l'offre
                    deleteOffre(offre);
                    // Refresh the list
                    loadOffreData();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    ObservableList<Offre> ListeOffre = FXCollections.observableArrayList();

    @FXML
    void update(MouseEvent event) {
        try {
            // Clear the existing data in the ObservableList
            ListeOffre.clear();

            // Retrieve data from the database using your OffreService
            offreService = new OffreService();
            List<Offre> offres = offreService.readOffre();

            // Add the retrieved data to the ObservableList
            ListeOffre.addAll(offres);

            // Set the items of the TableView to the updated data
            rList.setItems(ListeOffre);
            // Mettez à jour la liste des offres dans la TableView
            loadOffreData();
        } catch (Exception e) {
            // Handle any exceptions
            Logger.getLogger(AfficherOffreController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    void ajouter(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/AjouterOffre.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AfficherOffreController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }



    public void setRDesc(String rDesc) {
        this.rDesc.setText(rDesc);
    }

    public void setRFin(String rFin) {
        this.rFin.setText(rFin);
    }

    public void setRNom(String rNom) {
        this.rNom.setText(rNom);
    }

    public void setRDebut(String rdebut) {
        this.rDebut.setText(rdebut);
    }

    @FXML
    private void openModifierOffreDialog(Offre offre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierOffre.fxml"));
            Parent root = loader.load();

            ModifierOffreController controller = loader.getController();
            controller.setOffreData(offre);
            controller.setOffreService(offreService);  // Injecter l'instance de OffreService

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier Offre");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception
        }
    }



    @FXML
    private void deleteOffre(Offre offre) {
        try {
            OffreService offreService = new OffreService();
            offreService.deleteOffre(offre.getIdOffre());

            // Actualiser la liste des offres après la suppression
            loadOffreData();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'exception
        }
    }

    private void loadOffreData() {
        try {
            OffreService offreService = new OffreService();
            List<Offre> offres = offreService.readOffre();

            ObservableList<Offre> offreList = FXCollections.observableArrayList(offres);
            rList.setItems(offreList);
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'exception
        }
    }



}
