package entities;

public class OffreProduit {
    int id;
    Produit produit;
    Offre offre;

    public OffreProduit(int id, Produit produit, Offre offre) {
        this.id = id;
        this.produit = produit;
        this.offre = offre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", produit=" + produit +
                ", offre=" + offre +
                '}';
    }
}
