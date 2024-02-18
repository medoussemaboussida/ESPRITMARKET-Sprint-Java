package entities;

import java.util.Date;

public class Offre {
    int idOffre;
    String descriptionOffre;
    String nomOffre;
    Date dateDebut;
    Date dateFin;
    public  Offre(int idOffre, String descriptionOffre, String nomOffre,Date dateDebut,Date dateFin) {
        this.idOffre=idOffre;
        this.descriptionOffre=descriptionOffre;
        this.nomOffre=nomOffre;
        this.dateDebut=dateDebut;
        this.dateFin=dateFin;
    }
    public Offre()
    {}

    public int getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(int idOffre) {
        this.idOffre = idOffre;
    }

    public String getNomOffre() {
        return nomOffre;
    }

    public void setNomOffre(String nomOffre) {
        this.nomOffre = nomOffre;
    }
}
