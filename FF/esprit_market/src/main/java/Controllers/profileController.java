package Controllers;

import Service.UtilisateurService;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class profileController implements Initializable {


    public Label successLabel;
    @FXML
    private TextField emailtxt;

    @FXML
    private TextField nametxt;

    @FXML
    private TextField pointstxt;

    @FXML
    private TextField prenomtxt;

    @FXML
    private TextField pwdtxt;

    @FXML
    private TextField roletxt;

    @FXML
    private TextField telephonetxt;

    private Utilisateur userData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        

        
    }

    public void setUserData(Utilisateur user) {
        this.userData = user;
        // Now you can use this.userData to access the user's information in your controller
        System.out.println("Received User ID: " + user.getIdUser());
        System.out.println("Received User Name: " + user.getNomUser());
        System.out.println("Received User Email: " + user.getEmailUser());
        prenomtxt.setText(user.getPrenomUser());
        nametxt.setText(user.getNomUser());
        emailtxt.setText(user.getEmailUser());
        pwdtxt.setText(user.getMotDePasse());
        telephonetxt.setText(String.valueOf(user.getNumeroTel()));
        roletxt.setText(user.getRole());
        pointstxt.setText(String.valueOf(user.getNbPoints()));
    }

    public void update(ActionEvent actionEvent) {
        try {
            String nom = nametxt.getText();
            System.out.println(nom);
            String prenom = prenomtxt.getText();
            String email = emailtxt.getText();
            String mdpValue = pwdtxt.getText();
            int nbPointsValue = Integer.parseInt(pointstxt.getText());
            int numTelValue = Integer.parseInt(telephonetxt.getText());
            String roleValue = roletxt.getText();

            Utilisateur utilisateur = new Utilisateur(userData.getIdUser(),nom, prenom, email, mdpValue, numTelValue, nbPointsValue, roleValue);

            UtilisateurService utilisateurService = new UtilisateurService();
            utilisateurService.update(utilisateur);

            System.out.println("Updated");
            successLabel.setVisible(true);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input. Please enter valid numbers for points and phone.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void delete(ActionEvent actionEvent) {
        try {
            UtilisateurService utilisateurService = new UtilisateurService();
            utilisateurService.delete(userData); // Assuming userData contains the user to be deleted

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("do you really want to delete your account ?");
            alert.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Seconnecter.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            SeconnecterController lu = loader.getController();
            roletxt.getScene().setRoot(root);


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Format numero  non valide, must contain 8 digits");
            alert.showAndWait();
        }

    }

    public void navigatetoLogin(ActionEvent actionEvent) {

    }
}
