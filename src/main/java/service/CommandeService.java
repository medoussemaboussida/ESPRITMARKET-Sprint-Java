package service;

import entities.Commande;
import entities.Panier;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CommandeService implements IServiceCommande<Commande>{
    private Connection conn;
    private Statement statement;
    private PreparedStatement pst;
    public CommandeService()
    {
        conn= DataSource.getInstance().getCnx();
    }

    @Override
    public void ajouterCommande(Panier panier) {
        String insertCommandeQuery = "INSERT INTO commande (idPanier, dateCommande) VALUES (?, NOW())";
        String updateProduitcartQuery = "UPDATE produitcart pc " +
                "JOIN produit p ON pc.idProduit = p.idProduit " +
                "SET p.quantite = p.quantite - " +
                "(SELECT COUNT(idProduit) FROM produitcart WHERE idPanier = ?) " +
                "WHERE pc.idPanier = ?";

        try (PreparedStatement insertCommandeStatement = conn.prepareStatement(insertCommandeQuery);
             PreparedStatement updateProduitcartStatement = conn.prepareStatement(updateProduitcartQuery)) {

            // Insérer la commande dans la table de commandes
            insertCommandeStatement.setInt(1, panier.getIdPanier());
            insertCommandeStatement.executeUpdate();
            System.out.println("Commande ajoutée!");

            // Mettre à jour la quantité dans la table produitcart
            updateProduitcartStatement.setInt(1, panier.getIdPanier());
            updateProduitcartStatement.setInt(2, panier.getIdPanier());
            updateProduitcartStatement.executeUpdate();
            System.out.println("Quantité mise à jour!");

        } catch (SQLException e) {
            System.out.println("Problème lors de l'ajout de la commande ou mise à jour de la quantité !");
            throw new RuntimeException(e);
        }
    }

}
