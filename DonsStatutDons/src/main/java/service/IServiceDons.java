package service;

import entities.Dons;
import entities.utilisateur;

import java.util.List;

public interface IServiceDons {
    int addDons(utilisateur user, int donPoints);
    void updateDonsPoints(int donsId, int newPoints);


    boolean supprimerDon(int donsId, int nbPoints);

    boolean donExists(int idDon);

    List<Dons> getAllDons();


    void addDons(utilisateur user, int donPoints, String etatStatutDons);

    int addDonsWithStatus(utilisateur user, int donPoints);


    void updateDons(Dons don);
}
