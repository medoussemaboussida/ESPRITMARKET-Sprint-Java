package entities;

public class OffreProduit {
    int idOffreProduit;
    Produit produit;
    Offre offre;

    public OffreProduit(int idOffreProduit, Produit produit, Offre offre) {
        this.idOffreProduit = idOffreProduit;
        this.produit = produit;
        this.offre = offre;
    }

    public int getIdOffreProduit() {
        return idOffreProduit;
    }

    public void setIdOffreProduit(int idOffreProduit) {
        this.idOffreProduit = idOffreProduit;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }

    @Override
    public String toString() {
        return "OffreProduit{" +
                "idOffreProduit=" + idOffreProduit +
                ", produit=" + produit +
                ", offre=" + offre +
                '}';
    }
}
