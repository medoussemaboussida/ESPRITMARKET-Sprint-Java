package Controllers;

import Service.UtilisateurService;
import entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class EditerleprofilController {

    @FXML
    private TextField emailUser;

    @FXML
    private int idUser;

    @FXML
    private TextField mdp;

    @FXML
    private TextField nbPoints;

    @FXML
    private TextField nomUser;

    @FXML
    private TextField numTel;

    @FXML
    private TextField prenomUser;

    @FXML
    private TextField role;
    private Utilisateur utilisateur;
    @FXML
    private TextField idTextField;

// ...




    public void initialize() {
        if (utilisateur != null) {
            nomUser.setText(utilisateur.getNomUser());
            prenomUser.setText(utilisateur.getPrenomUser());
            emailUser.setText(utilisateur.getEmailUser());
            mdp.setText(utilisateur.getMotDePasse());
            numTel.setText(String.valueOf(utilisateur.getNumeroTel()));
            nbPoints.setText(String.valueOf(utilisateur.getNbPoints()));
            role.setText(utilisateur.getRole());
        }
    }

    @FXML
    public void update(ActionEvent event) {
        try {
            String nom = nomUser.getText();
            String prenom = prenomUser.getText();
            String email = emailUser.getText();
            String mdpValue = mdp.getText();
            int nbPointsValue = Integer.parseInt(nbPoints.getText());
            int numTelValue = Integer.parseInt(numTel.getText());
            String roleValue = role.getText();

            Utilisateur utilisateur = new Utilisateur(nom, prenom, email, mdpValue, numTelValue, nbPointsValue, roleValue);

            UtilisateurService utilisateurService = new UtilisateurService(); // Créez une instance de UtilisateurService
            utilisateurService.update(utilisateur);

            showAlert("Success!", "Profile updated successfully", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input. Please enter valid numeric values.", Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}






