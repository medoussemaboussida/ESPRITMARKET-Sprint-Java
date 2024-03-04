package Service;

import entities.Panier;
import entities.PanierProduit;
import javafx.collections.ObservableList;

public interface IServicePanierProduit <T>{
    void ajouterProduitAuPanier(Panier panier, int idProduit);
    ObservableList<T> getProduitsDuPanierUtilisateur(Panier panier);
    void DeleteProduitAuPanier(Panier panier, int idProduit);

    public ObservableList<PanierProduit> getAllProduitsPanier();

}