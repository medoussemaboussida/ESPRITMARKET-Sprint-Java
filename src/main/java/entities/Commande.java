package entities;

public class Commande {
    int idCommande;
    Panier panier;

    public Commande()
    {

    }
    public Commande(int idCommande,Panier panier)
    {
        this.idCommande=idCommande;
        this.panier=panier;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }
}
