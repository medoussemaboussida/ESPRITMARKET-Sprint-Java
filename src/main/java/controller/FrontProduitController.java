package controller;
import entities.*;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import service.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class FrontProduitController implements Initializable {
    private final ProduitService ps = new ProduitService();
    String filepath = null, filename = null, fn = null;
    String uploads = "C:/Users/Hp/Desktop/produitCategorie/src/main/java/Images/";
    FileChooser fc = new FileChooser();
    ObservableList<Produit> list = FXCollections.observableArrayList();
    public int idProduit;
    PanierService pns = new PanierService();

    public int getIdProduit() {
        return getIdProduit();
    }

    public void setIdProduit(int id) {
        this.idProduit = id;
    }

    @FXML
    private TableColumn<Produit, HBox> panierTab;
  //  @FXML
    //private TableColumn<PanierProduit, HBox> tabDeletePanierr;

    @FXML
    private TableColumn<Produit, Integer> nomPrixTab;

    @FXML
    private TableColumn<Produit, String> imageProduitTab;

    @FXML
    private TableColumn<Produit, Integer> nomQuantiteTab;

    @FXML
    private TableView<Produit> tabProduitFront;
    @FXML
    private ComboBox<Categorie> ComboProduitC;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        panierTab.setCellFactory(column -> new TableCell<Produit, HBox>() {
            String uploads = "C:/Users/Hp/Desktop/produitCategorie/src/main/resources/Photos/panier.png";

            private final Button addButton = new Button("+");
            private final ImageView imageView = new ImageView(new Image("file:///" + uploads));

            //partieUser

            {

                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                addButton.setOnAction(event -> {
                    int idUtilisateur = 1; // Remplacez cela par l'ID réel de l'utilisateur connecté
                    UtilisateurService utilisateurService = new UtilisateurService();
                    Utilisateur utilisateur = utilisateurService.getUserById(idUtilisateur);
                    Produit produit = getTableView().getItems().get(getIndex());
                    Panier panier = pns.selectPanierParUserId(utilisateur.getIdUser());
                    Panier panierExistant = pns.selectPanierParUserId(utilisateur.getIdUser()); // Remplacez userId par l'ID de l'utilisateur connecté

                    if (panierExistant != null) {
                        PanierProduitService panierProduitService = new PanierProduitService();
                        panierProduitService.ajouterProduitAuPanier(panier, produit.getIdProduit());
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Avertissement");
                        alert.setHeaderText(null);
                        alert.setContentText("produit ajouté au  panier existe deja.");
                        alert.showAndWait();

                    } else {
                        // Le panier n'existe pas, afficher un message d'alerte
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Avertissement");
                        alert.setHeaderText(null);
                        alert.setContentText("Vous n'avez pas de panier. Veuillez créer un panier d'abord.");
                        alert.showAndWait();
                        pns.ajouterPanier(utilisateur.getIdUser());
                    }
                    tabProduitFront.refresh();

                });
            }

            @Override
            protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    // Créez un conteneur HBox pour le bouton et l'image
                    HBox hbox = new HBox(addButton, imageView);
                    hbox.setSpacing(5);  // Définissez l'espacement entre le bouton et l'image

                    setGraphic(hbox);
                }
            }
        });


        int idUtilisateur = 1; // Remplacez cela par l'ID réel de l'utilisateur connecté
        UtilisateurService utilisateurService = new UtilisateurService();
        Utilisateur utilisateur = utilisateurService.getUserById(idUtilisateur);
        // Vérifiez si l'utilisateur est récupéré avec succès
        if (utilisateur != null) {
            // Afficher une alerte de bienvenue avec le nom de l'utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Bienvenue");
            alert.setHeaderText("Bienvenue " + utilisateur.getNomUser());
            alert.setContentText("Vous êtes connecté avec succès!\n"
                    + "Email: " + utilisateur.getEmailUser() + "\n"
                    + "Numéro de téléphone: " + utilisateur.getNumTel());
            alert.showAndWait();
        } else {
            // Gérer le cas où l'utilisateur n'a pas été trouvé
            System.out.println("Utilisateur non trouvé");
        }



       // showProduitFront();
        setCombo();
        ComboProduitC.setOnAction(this::filtrerProduit);
        showProduitFront();
       // showProduitDuPanierUser();

    }


    public void showProduitFront() {
        imageProduitTab.setCellFactory(column -> new TableCell<Produit, String>() {
            private final javafx.scene.image.ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    // Charger et afficher l'image
                    javafx.scene.image.Image image = new Image("file:///" + uploads + imagePath);
                    imageView.setImage(image);
                    imageView.setFitWidth(120); // Réglez la largeur de l'image selon vos besoins
                    imageView.setFitHeight(100); // Réglez la hauteur de l'image selon vos besoins
                    setGraphic(imageView);
                }
            }
        });
        imageProduitTab.setCellValueFactory(new PropertyValueFactory<>("imageProduit"));
        nomQuantiteTab.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        nomPrixTab.setCellValueFactory(new PropertyValueFactory<>("prix"));

        list = ps.readProduit();
        tabProduitFront.setItems(list);

    }


    public void setCombo() {
        CategorieService tabC = new CategorieService();
        List<Categorie> tabList = tabC.readCategorie();
        ArrayList<Categorie> cats = new ArrayList<>();
        for (Categorie c : tabList) {
            Categorie cat = new Categorie();
            cat.setIdCategorie(c.getIdCategorie());
            cat.setNomCategorie(c.getNomCategorie());
            cats.add(cat);
        }

        ObservableList<Categorie> choices = FXCollections.observableArrayList(cats);
        ComboProduitC.setItems(choices);

        ComboProduitC.setConverter(new StringConverter<Categorie>() {
            @Override
            public String toString(Categorie categorie) {
                if (categorie == null) {
                    return null;
                } else {
                    return categorie.getNomCategorie();
                }
            }

            @Override
            public Categorie fromString(String string) {
                // Vous pouvez implémenter cette méthode si nécessaire
                return null;
            }
        });
    }

    private List<Produit> temp;
    private List<PanierProduit> temp1;

    @FXML
    public void filtrerProduit(ActionEvent actionEvent) {
        Categorie selectedCategorie = ComboProduitC.getValue();
        int categorieId = selectedCategorie.getIdCategorie();
        temp = ps.readProduitByCategorie(categorieId);
        ObservableList<Produit> updatedList = FXCollections.observableArrayList(temp);

        // Mettre à jour la TableView
        tabProduitFront.setItems(updatedList);
    }
    @FXML
    private ImageView PanierImage;

    public void checkPanier(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FrontPanierCommande.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        Node source = (Node) mouseEvent.getSource();
        stage.show();
    }



    /*private void updateGridPane(List<Produit> produits) {
        Categorie selectedCategorie = ComboProduitC.getValue();
        int categorieId = selectedCategorie.getIdCategorie();
        temp = ps.readProduitByCategorie(categorieId);
        ObservableList<Produit> updatedList = FXCollections.observableArrayList(temp);

        // Mettre à jour la TableView
        tabProduitFront.setItems(updatedList);
    }
*/
/*
    @FXML
    private TableView<PanierProduit> panierTable;



    @FXML
    private TableColumn<PanierProduit, String> tabProduitcart;


    PanierProduitService pps=new PanierProduitService();
    ObservableList<PanierProduit> list1 = FXCollections.observableArrayList();
    public void showProduitDuPanierUser() {
        int idUtilisateur = 1; // Remplacez cela par l'ID réel de l'utilisateur connecté
        UtilisateurService utilisateurService = new UtilisateurService();
        Utilisateur utilisateur = utilisateurService.getUserById(idUtilisateur);
        Panier panier = pns.selectPanierParUserId(utilisateur.getIdUser());

        list1 = pps.getProduitsDuPanierUtilisateur(panier);

        tabProduitcart.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PanierProduit, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<PanierProduit, String> param) {
                String nomProduit = param.getValue().getProduit().getNomProduit();
                return new SimpleStringProperty(nomProduit);
            }

        });


        tabDeletePanierr.setCellFactory(column -> new TableCell<PanierProduit, HBox>() {
            private final Button DeleteButton = new Button("Delete");
            {
                DeleteButton.setOnAction(event -> {
                    PanierProduit pn = getTableView().getItems().get(getIndex());
                    Panier panier = pns.selectPanierParUserId(utilisateur.getIdUser());
                    pps.DeleteProduitAuPanier(panier,pn.getProduit().getIdProduit());
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Avertissement");
                    alert.setHeaderText(null);
                    alert.setContentText("produit produit supprimé de votre panier.");
                    alert.showAndWait();

                });
                }



                @Override
                protected void updateItem(HBox item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    // Créez un conteneur HBox pour le bouton et l'image
                    HBox hbox = new HBox(DeleteButton);

                    setGraphic(hbox);
                }
            }

            });


        panierTable.setItems(list1);

    }
*/

    //check le panier

}






