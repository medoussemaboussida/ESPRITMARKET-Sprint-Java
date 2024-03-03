package org.example;

import Service.CodePromoService;
import entities.CodePromo;
import entities.Offre;
import entities.Produit;
import utils.DataSource;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParseException, SQLException {
        DataSource ds= DataSource.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            // Supposons que vous ayez ces dates sous forme de chaînes
            String strDateDebut = "01/01/2024";
            String strDateFin = "31/01/2024";

            // Conversion de chaîne en java.util.Date
            Date dateDebut = sdf.parse(strDateDebut);
            Date dateFin = sdf.parse(strDateFin);
        List<Produit> produits = new ArrayList<>();

        Offre o = new Offre(3, "offre spéciale","offre Ramadhane",dateDebut,dateFin,"" ,54);
     //   OffreService cs=new OffreService();
       // cs.readOffre().forEach(System.out::println);
       // cs.deleteOffre(22);
        //cs.updateOffre(new Offre(23,"superrr","Aid Kbir", dateDebut,dateFin ));

        CodePromo c = new CodePromo(50, 25,"hbel",dateDebut,dateFin);
        CodePromoService cps=new CodePromoService();
        //cps.addCodePromo(c);
        cps.readCodePromo().forEach(System.out::println);
         //cps.deleteCodePromo(8);
         //cps.modifyCodePromo(new CodePromo(7,50,"ey baba",dateDebut,dateFin ));

    }
}