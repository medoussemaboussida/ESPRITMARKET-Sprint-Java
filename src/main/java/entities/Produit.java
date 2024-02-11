package entities;

public class Produit {
    private int id;
    private String nomProduit;
    private int prix;
    private int quantite;
    private Categorie categorie;
    private String imageProduit;

    public Produit(int id, String nomProduit, int quantite, int prix, Categorie categorie, String imageProduit) {
        this.id = id;
        this.nomProduit = nomProduit;
        this.prix = prix;
        this.quantite = quantite;
        this.categorie = categorie;
        this.imageProduit = imageProduit;
    }

    public Produit(String nomProduit, int quantite, int prix, Categorie categorie, String imageProduit) {
        this.nomProduit = nomProduit;
        this.prix = prix;
        this.quantite = quantite;
        this.categorie = categorie;
        this.imageProduit = imageProduit;
    }


    public Produit() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit= nomProduit;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getImageProduit() {
        return imageProduit;
    }

    public void setImageProduit(String imageProduit) {
        this.imageProduit = imageProduit;
    }

    @Override
    public String toString() {
        return "Produit{" + "nom produit=" + nomProduit + ", prix=" + prix + ", quantite=" + quantite + ", categorie=" + categorie + ", image produit=" + imageProduit + '}';
    }

}


