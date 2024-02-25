package controller;
import entities.Commande;
import entities.Panier;
import entities.PanierProduit;
import entities.Produit;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import service.CommandeService;
import service.PanierProduitService;
import utils.DataSource;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class BackPanierCommandeController implements Initializable {
    @FXML
    private TableColumn<Commande, Date> dateCommandeBack;

    @FXML
    private TableColumn<PanierProduit, String> produitPanierBack;

    @FXML
    private TableView<Commande> tableCommandeBack;

    @FXML
    private TableView<PanierProduit> tablePanierBack;

    @FXML
    private TableColumn<PanierProduit,String> userPanierBack;
    @FXML
    private TableColumn<PanierProduit, String> imageProduitPanierBack;
    String uploads = "C:/xampp/htdocs/";


    @FXML
    private TableColumn<Commande,String> utilisateurCommandeBack;
    private PanierProduitService pp= new PanierProduitService();
    private CommandeService cs =new CommandeService();
    ObservableList<PanierProduit> list = FXCollections.observableArrayList();
    ObservableList<Commande> list1 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showPanierProduitAll();
showCommandeAll();

    }
    public void showPanierProduitAll() {
        userPanierBack.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PanierProduit, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PanierProduit, String> param) {
                String nomPanierUser = param.getValue().getPanier().getUtilisateur().getNomUser();
                return new SimpleStringProperty(nomPanierUser);
            }
        });

        produitPanierBack.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PanierProduit, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PanierProduit, String> param) {
                String nomProduit = param.getValue().getProduit().getNomProduit();
                return new SimpleStringProperty(nomProduit);
            }
        });

        imageProduitPanierBack.setCellFactory(column -> new TableCell<PanierProduit, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    // Charger et afficher l'image
                    Image image = new Image("file:///" + uploads + imagePath);
                    imageView.setImage(image);
                    imageView.setFitWidth(120); // Réglez la largeur de l'image selon vos besoins
                    imageView.setFitHeight(100); // Réglez la hauteur de l'image selon vos besoins
                    setGraphic(imageView);
                }
            }
        });
        imageProduitPanierBack.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PanierProduit, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PanierProduit, String> param) {
                String nomProduit = param.getValue().getProduit().getImageProduit();
                return new SimpleStringProperty(nomProduit);
            }
        });

        list = pp.getAllProduitsPanier();
        tablePanierBack.setItems(list);

    }

    public void showCommandeAll()
    {

       dateCommandeBack.setCellValueFactory(new PropertyValueFactory<>("dateCommande"));

        utilisateurCommandeBack.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Commande, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Commande, String> param) {
                String userCommande = param.getValue().getPanier().getUtilisateur().getNomUser();
                return new SimpleStringProperty(userCommande);
            }
        });
        list1 = cs.readAllCommande();
        tableCommandeBack.setItems(list1);

    }
}