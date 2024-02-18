package entities;

public class Utilisateur {
    int idUser;
    String nomUser;
    String prenomUser;
    String emailUser;
    String mdp;
    String nbPoints;
    int numTel;
    String role;

    public Utilisateur(int idUser,String nomUser,String prenomUser, String emailUser, String mdp, String nbPoints, int numTel, String role)
    {
this.idUser=idUser;
this.nomUser=nomUser;
this.prenomUser=prenomUser;
this.emailUser=emailUser;
this.numTel=numTel;
this.mdp=mdp;
this.nbPoints=nbPoints;
this.role=role;
    }
    public Utilisateur()
    {

    }
    public int getIdUser() {
        return idUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public int getNumTel() {
        return numTel;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public String getNbPoints() {
        return nbPoints;
    }

    public String getMdp() {
        return mdp;
    }

    public String getRole() {
        return role;
    }
}
