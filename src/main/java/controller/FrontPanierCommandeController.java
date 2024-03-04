package controller;
import entities.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import service.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

public class FrontPanierCommandeController implements Initializable{

    @FXML
    private TableView<PanierProduit> panierTable;

    @FXML
    private Label facture;
    private Connection conn;
    private PreparedStatement pst;
    private Statement statement;
    private final ProduitService ps = new ProduitService();
    private ProduitService pss = new ProduitService();
    private CommandeService commandeService=new CommandeService();

    @FXML
    private TableColumn<PanierProduit, String> tabProduitcart;

    @FXML
    private TableColumn<PanierProduit, HBox> tabDeletePanierr;
    PanierService pns = new PanierService();
    PanierProduitService pps=new PanierProduitService();
    ObservableList<PanierProduit> list1 = FXCollections.observableArrayList();
    @FXML
    private Button commandeButton;
    int idUtilisateur = 2; //  l'utilisateur connecté
    UtilisateurService utilisateurService = new UtilisateurService();
    Utilisateur utilisateur = utilisateurService.getUserById(idUtilisateur);
    //selectionner la panier de user qui est connecté
    Panier panier = pns.selectPanierParUserId(utilisateur.getIdUser());
    float total = pps.facture(panier);
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        facture.setText(String.format("Montant à payer (DT): %.3f", total));
        showProduitDuPanierUser();
    }

    public void showProduitDuPanierUser() {

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
                DeleteButton.getStyleClass().add("addbuttonPanier");
                DeleteButton.setOnAction(event -> {
                    PanierProduit pn = getTableView().getItems().get(getIndex());
                    Panier panier = pns.selectPanierParUserId(utilisateur.getIdUser());
                    pps.DeleteProduitAuPanier(panier,pn.getProduit().getIdProduit());
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Avertissement");
                    alert.setHeaderText(null);
                    alert.setContentText("produit supprimé de votre panier.");
                    alert.showAndWait();
showProduitDuPanierUser();
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

    @FXML
    public void passerCommande(ActionEvent actionEvent) throws SQLException {

        commandeService.ajouterCommande(panier);
        String subject = "Confirmation Commande ESPRIT MARKET";
        String body = "Cher Client  "+utilisateur.getNomUser()+", Votre commande a été traitée avec succès et est en cours de préparation.";
        sendEmail(utilisateur.getEmailUser(), subject, body); // Envoyer l'email
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Succes");
        a.setContentText("Commande passée avec succées ! Check your Email");

        a.showAndWait();
    }

    private void sendEmail(String to, String subject, String body) {
        String username = "medoussemaboussida@gmail.com";
        String password = "wmgq nkfy btsz ubdf";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // Change this to your SMTP server host(yahoo...)
        props.put("mail.smtp.port", "587"); // Change this to your SMTP server port
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        jakarta.mail.Session session;
        session = jakarta.mail.Session.getInstance(props,new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(username, password);
            }
        });


        try {
            // Create a MimeMessage object

            // Create a new message
            jakarta.mail.internet.MimeMessage message = new MimeMessage(session);
            // Set the From, To, Subject, and Text fields of the message
            message.setFrom(new jakarta.mail.internet.InternetAddress(username));
            message.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);

            // Send the message using Transport.send
            jakarta.mail.Transport.send(message);

            System.out.println("Email sent successfully");
        } catch (MessagingException ex) {
            System.err.println("Failed to send email: " + ex.getMessage());
        }

    }
}
