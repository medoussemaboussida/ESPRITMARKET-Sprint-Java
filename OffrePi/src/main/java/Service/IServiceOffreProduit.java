package Service;

import entities.Offre;
import entities.Produit;
import javafx.collections.ObservableList;

public interface IServiceOffreProduit <T>{
    void ajouterProduitAuOffre(Offre offre, int idProduit);

    void DeleteProduitAuOffre(Offre offre, int idProduit);

    public ObservableList<Produit> getAllProduitsOffre();

}
