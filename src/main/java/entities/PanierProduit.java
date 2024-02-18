package entities;

public class PanierProduit {

    int idPanierProduit;
    Produit produit;
    Panier panier;

    public PanierProduit()
    {

    }
    public PanierProduit(int idPanierProduit,Produit produit,Panier panier)
    {
        this.idPanierProduit=idPanierProduit;
        this.produit=produit;
        this.panier=panier;
    }

    public Produit getProduit() {
        return produit;
    }

    public Panier getPanier() {
        return panier;
    }

    public int getIdPanierProduit() {
        return idPanierProduit;
    }
}
