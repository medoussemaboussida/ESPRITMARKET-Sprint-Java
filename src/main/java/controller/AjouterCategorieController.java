package controller;

import entities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import service.CategorieService;
import javafx.scene.image.Image;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.management.Notification;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import utils.DataSource;

public class AjouterCategorieController implements Initializable {

    private final CategorieService cs =new CategorieService();
    private CategorieService css =new CategorieService();


    @FXML
    private Button ajouterCategorie;

    @FXML
    private Button btnImageC;

    @FXML
    private ImageView tfImage;

    private String ImagePath;
    @FXML
    private Label image_link;
    @FXML
    private TextField tfNomCategorie;

    @FXML
    private TableColumn<Categorie, String> imageCategorieTab;

    @FXML
    private TableColumn<Categorie, String> nomCategorieTab;

    @FXML
    private TableView<Categorie> tabCategorie;
    String filepath = null, filename = null, fn = null;
    String uploads = "C:/Users/Hp/Desktop/produitCategorie/src/main/java/Images/";
    FileChooser fc = new FileChooser();
    ObservableList<Categorie> list = FXCollections.observableArrayList();
    public int idCategorie;
    public int getIdCategorie() {
        return idCategorie;
    }

    public void setIdCategorie(int id) {
        this.idCategorie = id;
    }


    @FXML
    private Button modifierCategorie;

    @FXML
    private Button supprimerCategorie;
    @Override
    public void initialize(URL url, ResourceBundle rb) {

showCategorie();

    }



   public void btn_image_action(ActionEvent actionEvent) throws SQLException, FileNotFoundException, IOException {
       File file = fc.showOpenDialog(null);
       // Shows a new file open dialog.
       if (file != null) {
           // URI that represents this abstract pathname
          tfImage.setImage(new Image(file.toURI().toString()));

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
    public void AjouterCategorie(javafx.event.ActionEvent actionEvent) throws SQLException {
       cs.addCategorie(new Categorie(tfNomCategorie.getText(),filename));
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Succes");
        a.setContentText("Cateygorie Ajoutée");
        a.showAndWait();
    }
    public void showCategorie()
    {
        nomCategorieTab.setCellValueFactory(new PropertyValueFactory<>("nomCategorie"));
        imageCategorieTab.setCellValueFactory(new PropertyValueFactory<>("imageCategorie"));
        list= cs.readCategorie();
        tabCategorie.setItems(list);

    }


    public void SetValue(MouseEvent mouseEvent)throws SQLException, ClassNotFoundException {
        Categorie selected = tabCategorie.getSelectionModel().getSelectedItem();

        if (selected != null) {
            tfNomCategorie.setText(selected.getNomCategorie());
            fn = selected.getImageCategorie();
            idCategorie = selected.getIdCategorie();
            Image im = new Image("file:" + uploads + selected.getImageCategorie());
            tfImage.setImage(im);
        }
    }

    @FXML
    public void ModifierCategorie(ActionEvent actionEvent)throws SQLException, ClassNotFoundException {
        String nomC=tfNomCategorie.getText();
        Categorie c = new Categorie(idCategorie,nomC,fn);
        css.modifyCategorie(c);
        Alert a = new Alert(Alert.AlertType.WARNING);

        a.setTitle("Succes");
        a.setContentText("Cateygorie Modifiée");
        a.showAndWait();
        showCategorie();

    }

    public void SupprimerCategorie(ActionEvent actionEvent)throws SQLException, ClassNotFoundException  {
        Categorie selected=tabCategorie.getSelectionModel().getSelectedItem();
        if(selected!=null)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Voulez-Vous Supprimer cet Categorie?");
            alert.setContentText("Supprimer?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    css.deleteCategorie(idCategorie);
                } else if (type == noButton) {
                    showCategorie();
                } else {
                    showCategorie();
                }
            });
        }
    }
}