package Service;

import entities.Commande;
import entities.Panier;
import javafx.collections.ObservableList;

public interface IServiceCommande <T>{
    void ajouterCommande(Panier panier);
    ObservableList<Commande> readAllCommande();
}
