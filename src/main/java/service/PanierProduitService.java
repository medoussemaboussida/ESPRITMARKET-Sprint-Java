package service;

import entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanierProduitService  implements IServicePanierProduit<PanierProduit>{
    private Connection conn;
    private Statement statement;
    private PreparedStatement pst;
    public PanierProduitService()
    {
        conn= DataSource.getInstance().getCnx();
    }
    @Override
    public void ajouterProduitAuPanier(Panier panier, int idProduit) {
        String requete = "INSERT INTO produitcart (idPanier,idProduit) VALUES (?, ?)";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, panier.getIdPanier());
            pst.setInt(2, idProduit);

            pst.executeUpdate();
            System.out.println("Produit ajouté au panier!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<PanierProduit> getProduitsDuPanierUtilisateur(Panier panier) {
        String requete = "SELECT pc.*, pr.*, pn.*, c.*, o.* " +
                "FROM produitcart pc " +
                "JOIN produit pr ON pc.idProduit = pr.idProduit " +
                "JOIN panier pn ON pc.idPanier = pn.idPanier " +
                "JOIN categorie c ON pr.categorie_id = c.idCategorie " +
                "JOIN offre o ON pr.idOffre = o.idOffre " +
                "WHERE pc.idPanier = ?";


        ObservableList<PanierProduit> list = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(requete);
            preparedStatement.setInt(1, panier.getIdPanier());
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Categorie c = new Categorie(rs.getInt("c.idCategorie"), rs.getString("c.nomCategorie"),rs.getString("c.imageCategorie"));
                Offre o=new Offre(rs.getInt("o.idOffre"), rs.getString("o.descriptionOffre"), rs.getString("o.nomOffre"),rs.getDate("o.dateDebut"),rs.getDate("o.dateFin") );
                Produit prod = new Produit(rs.getInt("pr.idProduit"), rs.getString("pr.nomProduit"), rs.getInt("pr.quantite"), rs.getFloat("pr.prix"), c, rs.getString("pr.imageProduit"), o);
                PanierProduit pnss = new PanierProduit(rs.getInt("pc.idPanierProduit"), panier, prod);
                list.add(pnss);
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage()); // Ajout d'une impression de message d'erreur
            System.out.println("Erreur lors de la récupération des produits du panier");

        }

        return list;
    }

    @Override
    public void DeleteProduitAuPanier(Panier panier, int idProduit) {
        String requete = "DELETE FROM produitcart where idPanier= ? and idProduit = ?";
        try {
            pst=conn.prepareStatement(requete);
            pst.setInt(1, panier.getIdPanier());
            pst.setInt(2, idProduit);
            pst.executeUpdate();
            System.out.println("produit supprimé de votre panier!");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits du panier");

            throw new RuntimeException(e);
        }
    }

}
