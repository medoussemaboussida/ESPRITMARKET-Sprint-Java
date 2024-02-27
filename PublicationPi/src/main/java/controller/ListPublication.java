package controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entities.Publication;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import service.CommentaireService;
import service.PublicationService;
import javafx.scene.image.ImageView;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ListPublication implements Initializable {

    @FXML
    private TableColumn<Publication, String> idFx;

    @FXML
    private TableColumn<Publication, String> descriptionFx;

    @FXML
    private TableColumn<Publication, String> titreFx;

    @FXML
    private TableColumn<Publication, String> imageFx;

    @FXML
    private TableColumn<Publication, Date> dateFx;


    @FXML
    private TableColumn<Publication, String> edit;

    @FXML
    private TableView<Publication> publicationTable;

    @FXML
    private ComboBox<String> sortPublicationBox;

    @FXML
    private TextField RechercherPublication;

    private List<Publication> temp;

    ObservableList<Publication> list = FXCollections.observableArrayList();

    String htdocsPath = "C:/xampp/htdocs/";



    private PublicationService publicationService;
    private CommentaireService commentaireService;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        publicationService = new PublicationService();
        commentaireService = new CommentaireService();
        sortPublicationBox.getItems().removeAll(sortPublicationBox.getItems());
        sortPublicationBox.getItems().addAll("Trier", "Trier par Date ↑", "Trier par Date ↓");
        sortPublicationBox.getSelectionModel().select("Trier");
        loadPublicationData();
    }




    private void loadPublicationData() {
        ObservableList<Publication> publicationList = FXCollections.observableArrayList(publicationService.readAll());

        // Associez les propriétés des objets Publication aux colonnes de la TableView

        descriptionFx.setCellValueFactory(new PropertyValueFactory<>("description"));
        titreFx.setCellValueFactory(new PropertyValueFactory<>("titrePublication"));
        imageFx.setCellValueFactory(new PropertyValueFactory<>("imagePublication"));
        imageFx.setCellFactory(column -> new TableCell<Publication, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    // Charger et afficher l'image depuis le chemin spécifié
                    Image image = new Image("file:///" + htdocsPath + imagePath);
                    imageView.setImage(image);
                    imageView.setFitWidth(120); // Réglez la largeur de l'image selon vos besoins
                    imageView.setFitHeight(100); // Réglez la hauteur de l'image selon vos besoins
                    setGraphic(imageView);
                }
            }
        });

        dateFx.setCellValueFactory(new PropertyValueFactory<>("datePublication"));

        // Ajoutez la liste des publications à la TableView
        publicationTable.setItems(publicationList);

        Callback<TableColumn<Publication, String>, TableCell<Publication, String>> cellFactory = (TableColumn<Publication, String> param) -> {
            final TableCell<Publication, String> cell = new TableCell<Publication, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                        FontAwesomeIconView commentIcon = new FontAwesomeIconView(FontAwesomeIcon.COMMENTING);

                        deleteIcon.setStyle("-fx-cursor: hand; -glyph-size:28px; -fx-fill:#ff1544;");
                        editIcon.setStyle("-fx-cursor: hand; -glyph-size:28px; -fx-fill:#00E676;");
                        commentIcon.setStyle("-fx-cursor: hand; -glyph-size:28px; -fx-fill:#3366ff;");



                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            // Vérifier si une publication est sélectionnée dans la table
                            if (getTableView().getSelectionModel().getSelectedItem() != null) {
                                // Afficher une boîte de dialogue de confirmation
                                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                                confirmationDialog.setTitle("Confirmation");
                                confirmationDialog.setHeaderText("Confirmation de suppression");
                                confirmationDialog.setContentText("Êtes-vous sûr de vouloir supprimer cette publication ?");

                                // Ajouter des boutons à la boîte de dialogue
                                ButtonType ouiButton = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
                                ButtonType nonButton = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
                                confirmationDialog.getButtonTypes().setAll(ouiButton, nonButton);

                                // Attendre la réponse de l'utilisateur
                                Optional<ButtonType> result = confirmationDialog.showAndWait();

                                // Vérifier la réponse de l'utilisateur
                                if (result.isPresent() && result.get() == ouiButton) {
                                    // Récupérer la publication sélectionnée
                                    Publication publication = getTableView().getSelectionModel().getSelectedItem();
                                    commentaireService.deleteAllCommentairesForPublication(publication.getIdPublication());

                                    // Supprimer la publication
                                    publicationService.deletePublication(publication.getIdPublication());

                                    // Recharger les données de la table
                                    loadPublicationData();
                                }
                            } else {
                                // Afficher un message d'erreur si aucune publication n'est sélectionnée
                                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                errorAlert.setTitle("Erreur");
                                errorAlert.setHeaderText(null);
                                errorAlert.setContentText("Veuillez sélectionner une publication à supprimer.");
                                errorAlert.showAndWait();
                            }
                        });

                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            Publication publication = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierPublication.fxml"));
                            try {
                                loader.load();
                                ModifierPublicationController controller = loader.getController();
                                controller.setFields(
                                        String.valueOf(publication.getIdPublication()),
                                        publication.getDescription(),
                                        publication.getTitrePublication(),
                                        publication.getImagePublication(),
                                        publication.getDatePublication()
                                );
                                Parent parent = loader.getRoot();
                                Stage stage = new Stage();
                                stage.setScene(new Scene(parent));
                                stage.initStyle(StageStyle.UTILITY);
                                stage.show();
                            } catch (IOException ex) {
                                Logger.getLogger(ListPublication.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                        commentIcon.setOnMouseClicked((MouseEvent event) -> {
                            // Récupérer la publication sélectionnée
                            Publication publication = getTableView().getItems().get(getIndex());

                            // Charger la vue DetailPublication.fxml
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailPublication.fxml"));
                            try {
                                Parent parent = loader.load();

                                // Récupérer le contrôleur associé à la vue DetailPublication.fxml
                                DetailPublicationController controller = loader.getController();

                                // Transmettre l'ID de la publication sélectionnée au contrôleur de détails de publication
                                controller.loadCommentairesData(publication.getIdPublication());
                                controller.setFields(
                                        String.valueOf(publication.getIdPublication()),
                                        publication.getDescription(),
                                        publication.getTitrePublication(),
                                        publication.getImagePublication(),
                                        publication.getDatePublication()
                                );


                                // Créer une nouvelle scène avec la vue chargée
                                Scene scene = new Scene(parent);

                                // Créer une nouvelle fenêtre
                                Stage stage = new Stage();

                                // Définir la scène sur la nouvelle fenêtre
                                stage.setScene(scene);

                                // Afficher la nouvelle fenêtre
                                stage.show();
                            } catch (IOException ex) {
                                Logger.getLogger(ListPublication.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });




                        HBox manageBtn = new HBox(editIcon, deleteIcon,commentIcon);
                        manageBtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                        HBox.setMargin(commentIcon, new Insets(2, 3, 0, 2));

                        setGraphic(manageBtn);
                        setText(null);
                    }
                }
            };
            return cell;
        };

        edit.setCellFactory(cellFactory);
    }


    @FXML
    void addPublication(MouseEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/AjouterPublication.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();

    }

    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void refresh(MouseEvent event) {
        loadPublicationData();
    }

    public void sortPublication(javafx.event.ActionEvent actionEvent) {
        String selected = sortPublicationBox.getSelectionModel().getSelectedItem();
        if (selected.equals("Trier par Date ↑")) {
            temp = publicationService.sortProduitPrixAsc();

        } else if (selected.equals("Trier par Date ↓")) {
            temp = publicationService.sortProduitPrixDesc();

        }
        // Mettez à jour la liste observable utilisée par votre TableView (par exemple, 'list')
        ObservableList<Publication> updatedList = FXCollections.observableArrayList(temp);

        // Mettre à jour la TableView
        publicationTable.setItems(updatedList);
    }

    @FXML
    public void searchPublication(KeyEvent keyEvent) {
        FilteredList<Publication> filter = new FilteredList<>(publicationTable.getItems(), p -> true);

        RechercherPublication.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(publication -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return publication.getTitrePublication().toLowerCase().contains(lowerCaseFilter)
                        || publication.getDescription().toLowerCase().contains(lowerCaseFilter)
                        || publication.getImagePublication().toLowerCase().contains(lowerCaseFilter)
                        || publication.getDatePublication().toString().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Publication> sort = new SortedList<>(filter);
        sort.comparatorProperty().bind(publicationTable.comparatorProperty());
        publicationTable.setItems(sort);
    }


}