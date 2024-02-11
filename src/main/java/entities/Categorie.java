package entities;

public class Categorie {
    private int id;
    private String nomCategorie;
    private String imageCategorie;

    public  Categorie(int id, String nomCategorie, String imageCategorie) {
        this.id = id;
        this.nomCategorie = nomCategorie;
        this.imageCategorie = imageCategorie;
    }

    public Categorie(String nomCategorie, String imageCategorie) {
        this.nomCategorie = nomCategorie;
        this.imageCategorie = imageCategorie;
    }

    public Categorie(int id, String name) {
        this.id = id;
        this.nomCategorie = nomCategorie;
    }

    public Categorie(int id) {
        this.id = id;
    }

    public  Categorie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getImageCategorie() {
        return imageCategorie;
    }

    public void setImageCategorie(String imageCategorie) {
        this.imageCategorie = imageCategorie;
    }

    @Override
    public String toString() {

        return "Categorie{" + "idCategorie=" +id + ", NomCategorie=" + nomCategorie + ", image Categorie=" + imageCategorie + '}';
    }

}
