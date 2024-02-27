package entities;

public class commentaire {
    private int idCommentaire;
    private String descriptionCommentaire;

    private user idUser;

    private Publication idPublication;

    public commentaire() {
    }

    public commentaire(int idCommentaire, String descriptionCommentaire, user idUser, Publication idPublication) {
        this.idCommentaire = idCommentaire;
        this.descriptionCommentaire = descriptionCommentaire;
        this.idUser = idUser;
        this.idPublication = idPublication;
    }

    public commentaire(String descriptionCommentaire, user idUser, Publication idPublication) {
        this.descriptionCommentaire = descriptionCommentaire;
        this.idUser = idUser;
        this.idPublication = idPublication;
    }

    public int getIdCommentaire() {
        return idCommentaire;
    }

    public void setIdCommentaire(int idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    public String getDescriptionCommentaire() {
        return descriptionCommentaire;
    }

    public void setDescriptionCommentaire(String descriptionCommentaire) {
        this.descriptionCommentaire = descriptionCommentaire;
    }

    public user getIdUser() {
        return idUser;
    }

    public void setIdUser(user idUser) {
        this.idUser = idUser;
    }

    public Publication getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(Publication idPublication) {
        this.idPublication = idPublication;
    }

    @Override
    public String toString() {
        return "commentaire{" +
                "idCommentaire=" + idCommentaire +
                ", descriptionCommentaire='" + descriptionCommentaire + '\'' +
                ", idUser=" + idUser +
                ", idPublication=" + idPublication +
                '}';
    }

    public user getUser() {
        return this.idUser;
    }
}
