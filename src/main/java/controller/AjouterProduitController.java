package controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import entities.Categorie;
import entities.Offre;
import entities.Produit;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import service.CategorieService;
import service.OffreService;
import service.ProduitService;
import utils.DataSource;

import javax.swing.*;
import java.awt.*;
import java.awt.Label;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class AjouterProduitController implements Initializable {

    @FXML
    private PieChart pieChart;
    private Connection conn;
    private PreparedStatement pst;
    private Statement statement;
    private final ProduitService ps = new ProduitService();
    private ProduitService pss = new ProduitService();
    @FXML
    private TextField RechercherProduit;
    @FXML
    private ComboBox<Offre> comboOffreP;

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
    private TableColumn<Produit, Integer> nomQuantiteTab;
    @FXML
    private TableColumn<Produit, String> imageProduitTab;
    @FXML
    private TableColumn<Produit, String> OffreProduitTab;

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

    @FXML
    private ComboBox<String> sortProduitBox;
    private List<Produit> temp;

    @FXML
    private ImageView qrcodeProduit;
    @FXML
    private Button pdfProduit;

    @FXML
    private Button excelProduit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conn = DataSource.getInstance().getCnx();
        sortProduitBox.getItems().removeAll(sortProduitBox.getItems());
        sortProduitBox.getItems().addAll("Trier", "Trier par Prix ↑", "Trier par Prix ↓");
        sortProduitBox.getSelectionModel().select("Trier");
        setCombo();
        setComboOffre();
        showProduit();

        addDataToChart();

    }

    public void btn_image_produit_action(ActionEvent actionEvent) throws SQLException, FileNotFoundException, IOException {
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
        String nomProd = tfNomProduit.getText();
        Categorie cat = ComboProduitC.getValue();
        Offre of = comboOffreP.getValue();
        Float prix = Float.parseFloat(tfPrixProduit.getText());
        int quantite = Integer.parseInt(tfQuantiteProduit.getText());
        ps.addProduit(new Produit(nomProd, quantite, prix, cat, filename, of));
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Succes");
        a.setContentText("Produit Ajoutée");
        a.showAndWait();
    }

    public void ModifierProduit(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String nomProd = tfNomProduit.getText();
        Categorie cat = ComboProduitC.getValue();
        Offre of = comboOffreP.getValue();
       Float prix = Float.parseFloat(tfPrixProduit.getText());
        int quantite = Integer.parseInt(tfQuantiteProduit.getText());
        Produit p = new Produit(idProduit, nomProd, quantite, prix, cat, fn, of);
        pss.modifyProduit(p);
        Alert a = new Alert(Alert.AlertType.WARNING);

        a.setTitle("Succes");
        a.setContentText("Produit Modifiée");
        a.showAndWait();
        showProduit();

    }

    public void SupprimerProduit(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Produit selected = tabProduit.getSelectionModel().getSelectedItem();
        if (selected != null) {
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
                    tabProduit.refresh();
                } else if (type == noButton) {
                    showProduit();
                } else {
                    showProduit();
                }
            });
        }
    }


    public void showProduit() {
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
        OffreProduitTab.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Produit, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Produit, String> param) {
                String nomOffre = param.getValue().getOffre().getNomOffre();
                return new SimpleStringProperty(nomOffre);
            }
        });
        imageProduitTab.setCellFactory(column -> new TableCell<Produit, String>() {
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
        imageProduitTab.setCellValueFactory(new PropertyValueFactory<>("imageProduit"));

        list = ps.readProduit();
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
            comboOffreP.setValue(selected.getOffre());
            idProduit = selected.getIdProduit();
            fn = selected.getImageProduit();
            Image im = new Image("file:" + uploads + selected.getImageProduit());
            tfImageP.setImage(im);
        }
    }

    public void setComboOffre() {
        OffreService tabO = new OffreService();
        List<Offre> tabListOffre = tabO.readOffre();
        ArrayList<Offre> Offres = new ArrayList<>();
        for (Offre o : tabListOffre) {
            Offre of = new Offre();
            of.setIdOffre(o.getIdOffre());
            of.setNomOffre(o.getNomOffre());
            Offres.add(of);
        }
        ObservableList<Offre> choices = FXCollections.observableArrayList(Offres);
        comboOffreP.setItems(choices);
        comboOffreP.setConverter(new StringConverter<Offre>() {
            @Override
            public String toString(Offre offre) {
                if (offre == null) {
                    return null;
                } else {
                    return offre.getNomOffre();
                }
            }

            @Override
            public Offre fromString(String string) {
                // Vous pouvez implémenter cette méthode si nécessaire
                return null;
            }
        });
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

    @FXML
    public void sortProduit(ActionEvent actionEvent) {
        String selected = sortProduitBox.getSelectionModel().getSelectedItem();
        if (selected.equals("Trier par Prix ↑")) {
            temp = pss.sortProduitPrixAsc();

        } else if (selected.equals("Trier par Prix ↓")) {
            temp = pss.sortProduitPrixDesc();

        }
        // Mettez à jour la liste observable utilisée par votre TableView (par exemple, 'list')
        ObservableList<Produit> updatedList = FXCollections.observableArrayList(temp);

        // Mettre à jour la TableView
        tabProduit.setItems(updatedList);
    }

    //qrcode produit
    @FXML
    public void generateQrCodeProduit(ActionEvent actionEvent) {
        Produit selected = tabProduit.getSelectionModel().getSelectedItem();
        // Vérifiez si un élément est sélectionné
        if (selected != null) {
            // Générez la chaîne de données pour le QR code
            String qrData = "Nom: " + selected.getNomProduit() + "Quantite: " + selected.getQuantite() + "Prix: " + selected.getPrix() + "Categorie associée: " + selected.getCategorie();

            // Générez et affichez le QR code
            generateAndDisplayQRCode(qrData);
        } else {
            // Affichez un message d'erreur ou prenez une autre action appropriée
            System.out.println("Aucun Produit sélectionnée.");
        }

    }

    //generate qrcode et l'afficher
    private void generateAndDisplayQRCode(String qrData) {
        try {
            // Configuration pour générer le QR code
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Générer le QR code avec ZXing
            BitMatrix matrix = new MultiFormatWriter().encode(qrData, BarcodeFormat.QR_CODE, 184, 199, hints);
// Ajuster la taille de l'ImageView
            qrcodeProduit.setFitWidth(184);
            qrcodeProduit.setFitHeight(199);

            // Convertir la matrice en image JavaFX
            Image qrCodeImage = matrixToImage(matrix);

            // Afficher l'image du QR code dans l'ImageView
            qrcodeProduit.setImage(qrCodeImage);
            Alert a = new Alert(Alert.AlertType.WARNING);

            a.setTitle("Succes");
            a.setContentText("qr code generer");
            a.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour convertir une matrice BitMatrix en image BufferedImage
    private Image matrixToImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelColor = matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                pixelWriter.setArgb(x, y, pixelColor);
            }
        }

        System.out.println("Matrice convertie en image avec succès");

        return writableImage;
    }

    @FXML
    public void searchProduit(KeyEvent keyEvent) {
        FilteredList<Produit> filter = new FilteredList<>(list, ev -> true);

        RechercherProduit.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(t -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(t.getNomProduit()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<Produit> sort = new SortedList<>(filter);
        sort.comparatorProperty().bind(tabProduit.comparatorProperty());
        tabProduit.setItems(sort);

    }

    @FXML
    public void generatePdfProduit(ActionEvent actionEvent) {
        ObservableList<Produit> data = tabProduit.getItems();

        try {
            // Créez un nouveau document PDF
            PDDocument document = new PDDocument();

            // Créez une page dans le document
            PDPage page = new PDPage();
            document.addPage(page);

            // Obtenez le contenu de la page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Écrivez du texte dans le document
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);


            for (Produit produit : data) {


                String ligne = "ID : " + produit.getIdProduit() + "        Nom : " + produit.getNomProduit() + "     Quantité : " + produit.getQuantite() + "        Prix : " + produit.getPrix();
                contentStream.showText(ligne);

                contentStream.newLine();
                ;
                contentStream.newLineAtOffset(0, -15);


            }

            contentStream.endText();

            // Fermez le contenu de la page
            contentStream.close();

            String outputPath = "C:/Users/Hp/Desktop/produitCategorie/src/main/java/PDF/produits.pdf";
            File file = new File(outputPath);
            document.save(file);

            // Fermez le document
            document.close();

            System.out.println("Le PDF a été généré avec succès.");
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void generateExcelProduit(ActionEvent actionEvent) throws SQLException, FileNotFoundException, IOException {

        String req = "SELECT idProduit,nomProduit,quantite,prix FROM produit ";
        statement = conn.createStatement();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(req);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Détails produit");
        HSSFRow header = sheet.createRow(0);


        header.createCell(0).setCellValue("idProduit");
        header.createCell(1).setCellValue("nomProduit");
        header.createCell(2).setCellValue("quantite");
        header.createCell(3).setCellValue("prix");


        int index = 1;
        while (rs.next()) {
            HSSFRow row = sheet.createRow(index);

            row.createCell(0).setCellValue(rs.getInt("idProduit"));
            row.createCell(1).setCellValue(rs.getString("nomProduit"));
            row.createCell(2).setCellValue(rs.getInt("quantite"));
            row.createCell(3).setCellValue(rs.getInt("prix"));

            index++;
        }

        FileOutputStream file = new FileOutputStream("C:/Users/Hp/Desktop/produitCategorie/src/main/java/EXCEL/produit.xls");
        wb.write(file);
        file.close();

        JOptionPane.showMessageDialog(null, "Exportation 'EXCEL' effectuée avec succés");

        pst.close();
        rs.close();

    }


    private void addDataToChart() {
        // Efface les données existantes
        pieChart.getData().clear();

        // Récupère les statistiques des prix
        int produitsEntre10000Et20000 = getProduitsCountByPriceRange(0, 1000);
        int produitsEntre30000Et50000 = getProduitsCountByPriceRange(2000, 10000);
        int totalProduits = getTotalProduitsCount();

        // Ajoute les données au PieChart
        PieChart.Data data1 = new PieChart.Data("Produits entre 10,000 et 20,000", produitsEntre10000Et20000);
        PieChart.Data data2 = new PieChart.Data("Produits entre 30,000 et 50,000", produitsEntre30000Et50000);
        PieChart.Data data3 = new PieChart.Data("Autres Produits", totalProduits - produitsEntre10000Et20000 - produitsEntre30000Et50000);
        pieChart.getData().addAll(data1, data2, data3);

    }

    private int getProduitsCountByPriceRange(int minPrice, int maxPrice) {
        try {
            String query = "SELECT COUNT(*) FROM produit WHERE prix BETWEEN ? AND ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, minPrice);
            pst.setInt(2, maxPrice);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getTotalProduitsCount() {
        try {
            String query = "SELECT COUNT(*) FROM produit";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @FXML
    public void refreshProduit(ActionEvent actionEvent) {
    showProduit();
    }
    /*

    private void updateTextField (String newT){
        Platform.runLater(() -> {
            tfNomProduit.setText(newT);
            tfQuantiteProduit.setText(newT);
            tfPrixProduit.setText(newT);
        });
    }*/


}



