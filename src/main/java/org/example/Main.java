package org.example;

import entities.Categorie;
import entities.Produit;
import service.ProduitService;
import utils.DataSource;
import service.CategorieService;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main {

    public static void main(String[] args) {


        DataSource ds= DataSource.getInstance();
        Categorie c = new Categorie("pattes", "pattes.jpg");
        CategorieService cs=new CategorieService();
        //cs.addCategorie(c);
        cs.readCategorie().forEach(System.out::println);
        //cs.deleteCategorie(1);
        //cs.modifyCategorie(new Categorie(2,"Pattes","pattes.jpg"));

        ProduitService ps=new ProduitService();
        Produit p=new Produit("Spagetti",10, 7000,cs.readCategorie().get(1),"Spagetti.jpg");
        //ps.addProduit(p);
        //ps.readProduit().forEach(System.out::println);
        //ps.modifyProduit(new Produit(3,"Spagetti numero 2",10, 7000,cs.readCategorie().get(0),"Spagetti.jpg"));
        ps.deleteProduit(3);

    }
}