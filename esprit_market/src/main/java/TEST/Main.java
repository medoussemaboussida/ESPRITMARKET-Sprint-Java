package TEST;
import Service.UtilisateurService;
import entities.Utilisateur;
import utils.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main {



    public static void main(String[] args) {
        UtilisateurService utilisateurService = new UtilisateurService();


        Utilisateur utilisateurAUpdate = new Utilisateur( "fatma" , "benrabiaa", "f","elaa", 5,4,"client");
        utilisateurAUpdate.setIdUser(1);
        utilisateurAUpdate.setNomUser("benrabii");
        utilisateurAUpdate.setPrenomUser("elaa");


        utilisateurService.update(utilisateurAUpdate);

        System.out.println("Utilisateur mis à jour avec succès !");




    }

}
