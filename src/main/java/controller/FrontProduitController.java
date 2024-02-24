package controller;
import entities.*;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
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
import java.awt.ScrollPane;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.text.DecimalFormat;
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

   /* @FXML
    private TableColumn<Produit, HBox> panierTab;
    @FXML
    private TableColumn<PanierProduit, HBox> tabDeletePanierr;

    @FXML
    private TableColumn<Produit, Integer> nomPrixTab;

    @FXML
    private TableColumn<Produit, String> imageProduitTab;

    @FXML
    private TableColumn<Produit, Integer> nomQuantiteTab;

    @FXML
    private TableView<Produit> tabProduitFront;
    */

    @FXML
    private ComboBox<Categorie> ComboProduitC;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


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




        setCombo();
        ComboProduitC.setOnAction(this::filtrerProduit);
       // showProduitFront();
        showProduitFrontp();

    }

/*
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
*/

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

        // Clear the existing content in ListView
        listView.getItems().clear();

        // Add each product to ListView as GridPane
        for (int i = 0; i < updatedList.size(); i += 4) {
            GridPane productGridPane = createProductGridPane(
                    (i < updatedList.size()) ? updatedList.get(i) : null,
                    (i + 1 < updatedList.size()) ? updatedList.get(i + 1) : null,
                    (i + 2 < updatedList.size()) ? updatedList.get(i + 2) : null,
                    (i + 3 < updatedList.size()) ? updatedList.get(i + 3) : null
            );
            listView.getItems().add(productGridPane);
        }
    }

    @FXML
    private ImageView PanierImage;

    public void checkPanier(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FrontPanierCommande.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Votre Panier");
        stage.setScene(new Scene(root1));
        Node source = (Node) mouseEvent.getSource();
        stage.show();
    }



    @FXML
    private ListView<GridPane> listView;


    public void showProduitFrontp() {
        // Clear the existing content in ListView
        listView.getItems().clear();

        // Fetch the list of products from your service
        list = ps.readProduit();

        // Add each product to ListView
        for (int i = 0; i < list.size(); i += 4) {
            GridPane gridPane = createProductGridPane(
                    list.get(i),
                    (i + 1 < list.size()) ? list.get(i + 1) : null,
                    (i + 2 < list.size()) ? list.get(i + 2) : null,
                    (i + 3 < list.size()) ? list.get(i + 3) : null
            );
            listView.getItems().add(gridPane);
            //css
            gridPane.getStyleClass().add("grid-pane-product");

        }

    }

    private GridPane createProductGridPane(Produit produit1, Produit produit2, Produit produit3, Produit produit4) {
        GridPane gridPane = new GridPane();

        // Create and set up UI components for each product
        VBox vbox1 = createProductBox(produit1);
        VBox vbox2 = (produit2 != null) ? createProductBox(produit2) : new VBox(); // Empty VBox if no second product
        VBox vbox3 = (produit3 != null) ? createProductBox(produit3) : new VBox(); // Empty VBox if no third product
        VBox vbox4 = (produit4 != null) ? createProductBox(produit4) : new VBox(); // Empty VBox if no fourth product

        // Add components to GridPane
        gridPane.add(vbox1, 0, 0);
        gridPane.add(vbox2, 1, 0);
        gridPane.add(vbox3, 2, 0);
        gridPane.add(vbox4, 3, 0);

        // Set horizontal gap between columns
        gridPane.setHgap(10);

        return gridPane;
    }
    private VBox createProductBox(Produit produit) {
        VBox vbox = new VBox();
        float prix = produit.getPrix();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.000"); // Format avec trois chiffres après la virgule
        String prixFormate = decimalFormat.format(prix);

        // Create and set up UI components for each product
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView();
        loadAndSetImage(imageView, produit.getImageProduit());

        Label nameLabel = new Label(produit.getNomProduit());
        Label priceLabel = new Label("Prix: " + prixFormate);
        Label quantityLabel = new Label("Quantité en stock: " + produit.getQuantite());
        Button addButton = new Button("+");
        addButton.getStyleClass().add("addbuttonPanier");
        nameLabel.getStyleClass().add("product-label");
        priceLabel.getStyleClass().add("product-label");
        quantityLabel.getStyleClass().add("product-label");

        // Add components to VBox
        vbox.getChildren().addAll(imageView, nameLabel, priceLabel, quantityLabel, addButton);

        // Set spacing and alignment as needed
        vbox.setSpacing(11);
        vbox.setAlignment(Pos.CENTER);


        addButton.setOnAction(event -> {
            int idUtilisateur = 1; // Remplacez cela par l'ID réel de l'utilisateur connecté
            UtilisateurService utilisateurService = new UtilisateurService();
            Utilisateur utilisateur = utilisateurService.getUserById(idUtilisateur);
            Panier panier = pns.selectPanierParUserId(utilisateur.getIdUser());
            Panier panierExistant = pns.selectPanierParUserId(utilisateur.getIdUser());

            if (panierExistant != null) {
                PanierProduitService panierProduitService = new PanierProduitService();
                panierProduitService.ajouterProduitAuPanier(panier, produit.getIdProduit());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Avertissement");
                alert.setHeaderText(null);
                alert.setContentText("Produit ajouté dans votre panier .");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Avertissement");
                alert.setHeaderText(null);
                alert.setContentText("Vous n'avez pas de panier. Veuillez créer un panier d'abord.");
                alert.showAndWait();
                pns.ajouterPanier(utilisateur.getIdUser());
            }
            // Rafraîchir la vue du produit
            showProduitFrontp();
        });



        return vbox;
    }
    private void loadAndSetImage(ImageView imageView, String imagePath) {
        // Charger et afficher l'image en utilisant le chemin complet du fichier
        File imageFile = new File(uploads + imagePath);
        if (imageFile.exists()) {
            Image image = new Image("file:///" + imageFile.getAbsolutePath());
            imageView.setImage(image);
            imageView.setFitWidth(190);
            imageView.setFitHeight(190);
        } else {
            // Gérer le cas où le fichier d'image n'existe pas
            System.out.println("Le fichier d'image n'existe pas : " + imagePath);
            imageView.setImage(null);
        }
    }

}






