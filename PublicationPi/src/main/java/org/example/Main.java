package org.example;

import entities.Publication;
import entities.commentaire;
import service.CommentaireService;
import service.PublicationService;
import service.UserService;
import utils.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class Main {


    public static void main(String[] args) {
        DataSource ds=DataSource.getInstance();
        Date date = new Date();
        Publication p1 = new Publication("prix", date, "tomate.jpg", "tomate");
        PublicationService ps =new PublicationService();
        UserService us= new UserService();
        //ps.addPublication(p1);
        //ps.readAll().forEach(System.out::println);
        //ps.updatePublication(new Publication(3,"yassine", date, "felfel.jpg", "felfel"));
        //ps.deletePublication(2);
        // Récupération d'une publication avec le titre spécifié
        String titrePublicationToRead = "tomate";
        Publication publication = ps.readByTitre(titrePublicationToRead);

        if (publication != null) {
            // Affichage des détails de la publication récupérée
            System.out.println("Publication récupérée : " + publication);
        } else {
            System.out.println("Aucune publication trouvée avec le titre spécifié : " + titrePublicationToRead);
        }

        commentaire c1 = new commentaire("yassine", us.readById(2), ps.readAll().get(p1.getIdPublication()));
        CommentaireService commentaireService = new CommentaireService();
        commentaireService.addCommentaire(c1);
        //commentaireService.deleteCommentaire(2);

    }


    }
