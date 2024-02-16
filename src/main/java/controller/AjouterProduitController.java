package controller;

import entities.Categorie;
import entities.Produit;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import service.CategorieService;
import service.ProduitService;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterProduitController implements Initializable {
    private final ProduitService ps=new ProduitService();
    private ProduitService pss=new ProduitService();
    @FXML
    private ComboBox<Categorie> ComboProduitC;

    @FXML
    private Button ajouterProduit;

    @FXML
    private Button btImageProduit;

    @FXML
    private Button modifierProduit;

    @FXML
    private Button supprimerProduit;

    @FXML
    private ImageView tfImageP;

    @FXML
    private TextField tfNomProduit;

    @FXML
    private TextField tfPrixProduit;

    @FXML
    private TextField tfQuantiteProduit;
    @FXML
    private TableView<Produit> tabProduit;
    @FXML
    private TableColumn<Produit, String> nomCategorieTab;

    @FXML
    private TableColumn<Produit, Integer> nomPrixTab;

    @FXML
    private TableColumn<Produit, String> nomProduitTab;

    @FXML
    private TableColumn<Produit,Integer> nomQuantiteTab;
    @FXML
    private TableColumn<Produit, String> imageProduitTab;
    String filepath = null, filename = null, fn = null;
    String uploads = "C:/Users/Hp/Desktop/produitCategorie/src/main/java/Images/";
    FileChooser fc = new FileChooser();
    ObservableList<Produit> list = FXCollections.observableArrayList();
    public int idProduit;

    public int getIdProduit() {
        return getIdProduit();
    }

    public void setIdProduit(int id) {
        this.idProduit = id;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showProduit();
        setCombo();
    }
    public void btn_image_produit_action(ActionEvent actionEvent)throws SQLException, FileNotFoundException, IOException {
        File file = fc.showOpenDialog(null);
        // Shows a new file open dialog.
        if (file != null) {
            // URI that represents this abstract pathname
            tfImageP.setImage(new Image(file.toURI().toString()));

            filename = file.getName();
            filepath = file.getAbsolutePath();

            fn = filename;

            FileChannel source = new FileInputStream(filepath).getChannel();
            FileChannel dest = new FileOutputStream(uploads + filename).getChannel();
            dest.transferFrom(source, 0, source.size());
        } else {
            System.out.println("Fichier invalide!");
        }
    }

    @FXML
    public void AjouterProduit(ActionEvent actionEvent) throws SQLException {
        String nomProd =tfNomProduit.getText();
        Categorie cat = ComboProduitC.getValue();
        int prix = Integer.parseInt(tfPrixProduit.getText());
        int quantite= Integer.parseInt(tfQuantiteProduit.getText());
        ps.addProduit(new Produit(nomProd,quantite,prix,cat,filename));
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Succes");
        a.setContentText("Produit Ajoutée");
        a.showAndWait();
    }

    public void ModifierProduit(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String nomProd =tfNomProduit.getText();
        Categorie cat = ComboProduitC.getValue();
        int prix = Integer.parseInt(tfPrixProduit.getText());
        int quantite= Integer.parseInt(tfQuantiteProduit.getText());
        Produit p = new Produit(idProduit,nomProd,quantite,prix,cat,fn);
        pss.modifyProduit(p);
        Alert a = new Alert(Alert.AlertType.WARNING);

        a.setTitle("Succes");
        a.setContentText("Produit Modifiée");
        a.showAndWait();
        showProduit();

    }

    public void SupprimerProduit(ActionEvent actionEvent) throws SQLException, ClassNotFoundException  {
       Produit selected=tabProduit.getSelectionModel().getSelectedItem();
        if(selected!=null)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Voulez-Vous Supprimer ce produit ?");
            alert.setContentText("Supprimer?");
            ButtonType okButton = new ButtonType("Oui", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Non", ButtonBar.ButtonData.NO);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    pss.deleteProduit(selected.getIdProduit());
                    showProduit();
                } else if (type == noButton) {
                    showProduit();
                } else {
                    showProduit();
                }
            });
        }
    }




    public void showProduit()
    {
        nomProduitTab.setCellValueFactory(new PropertyValueFactory<>("nomProduit"));
        nomQuantiteTab.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        nomPrixTab.setCellValueFactory(new PropertyValueFactory<>("prix"));
        nomCategorieTab.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Produit, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Produit, String> param) {
                String nomCategorie = param.getValue().getCategorie().getNomCategorie();
                return new SimpleStringProperty(nomCategorie);
            }
        });
        imageProduitTab.setCellFactory(column -> new TableCell<Produit,String>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty || imagePath == null) {
                    setGraphic(null);
                } else {
                    // Charger et afficher l'image
                    Image image = new Image("file:///"+uploads+ imagePath);
                    imageView.setImage(image);
                    imageView.setFitWidth(120); // Réglez la largeur de l'image selon vos besoins
                    imageView.setFitHeight(100); // Réglez la hauteur de l'image selon vos besoins
                    setGraphic(imageView);
                }
            }
        });
        imageProduitTab.setCellValueFactory(new PropertyValueFactory<>("imageProduit"));

        list= ps.readProduit();
        tabProduit.setItems(list);

    }
    public void setValue(MouseEvent mouseEvent) throws SQLException, ClassNotFoundException {
        Produit selected = tabProduit.getSelectionModel().getSelectedItem();
        CategorieService tabC = new CategorieService();
        if (selected != null) {
            ComboProduitC.setValue(selected.getCategorie());
            tfNomProduit.setText(selected.getNomProduit());
            tfPrixProduit.setText(String.valueOf(selected.getPrix()));
            tfQuantiteProduit.setText(String.valueOf(selected.getQuantite()));
            idProduit = selected.getIdProduit();
            fn = selected.getImageProduit();
            Image im = new Image("file:" + uploads + selected.getImageProduit());
            tfImageP.setImage(im);
        }
    }
    public void setCombo()
    {
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

    public void backProduit(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MenuProduitCategorie.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        Node source = (Node) actionEvent.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.close();
        stage.show();

    }
}
