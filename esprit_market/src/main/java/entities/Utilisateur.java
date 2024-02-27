package entities;

public class Utilisateur {

        private int idUser;
        private String nomUser;
        private String prenomUser;
        private String emailUser;
        private String motDePasse;
        private int nbPoints;
        private int numeroTel;
        private String role ; // "administrateur" ou "client"

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }


    public Utilisateur (String nomUserTFText, String text, String id , int nbPoints, String nomUser , String prenomUser){}




        public Utilisateur(String nomUser, String prenomUser, String emailUser, String motDePasse,
                           int nbPoints, int numeroTel, String role) {
            this.nomUser = nomUser;
            this.prenomUser = prenomUser;
            this.emailUser = emailUser;
            this.motDePasse = motDePasse;
            this.nbPoints = nbPoints;
            this.numeroTel = numeroTel;
            this.role = role;
        }

    public Utilisateur(int idUser, String nomUser, String prenomUser, String emailUser, String motDePasse, int nbPoints, int numeroTel, String role) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.emailUser = emailUser;
        this.motDePasse = motDePasse;
        this.nbPoints = nbPoints;
        this.numeroTel = numeroTel;
        this.role = role;
    }

    public Utilisateur() {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.emailUser = emailUser;
        this.motDePasse = motDePasse;
        this.nbPoints = nbPoints;
        this.numeroTel = numeroTel;
        this.role = role;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }
    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }


    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public int getNbPoints() {
        return nbPoints;
    }

    public void setNbPoints(int nbPoints) {
        this.nbPoints = nbPoints;
    }
    public int getNumeroTel() {
        return numeroTel;
    }

    public void setNumeroTel(int numeroTel) {
        this.numeroTel = numeroTel;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return "utilisateur{" +
                "idUser=" + idUser +
                ", nomUser='" + nomUser + '\'' +
                ", prenomUser='" + prenomUser + '\'' +
                ", emailUser='" + emailUser + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", nbPoints=" + nbPoints +
                ", numeroTel='" + numeroTel + '\'' +
                ", role='" + role + '\'' +
                '}';
    }


}


