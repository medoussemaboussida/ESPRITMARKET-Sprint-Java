package Service;

import entities.Offre;
import entities.OffreProduit;
import entities.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class OffreProduitService implements IServiceOffreProduit<OffreProduit>{
    private Connection conn;
    private Statement statement;
    private PreparedStatement pst;
    public void ajouterProduitAuOffre(Offre offre, int idProduit) {
        String requete = "INSERT INTO offreProduit (idOffre, idProduit) VALUES (?, ?)";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, offre.getIdOffre());
            pst.setInt(2, idProduit);
            pst.executeUpdate();
            System.out.println("Product added to the offer!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void DeleteProduitAuOffre(Offre offre, int idProduit) {

    }

    public void supprimerProduitDeLOffre(Offre offre, int idProduit) {
        String requete = "DELETE FROM offreProduit WHERE idOffre = ? AND idProduit = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, offre.getIdOffre());
            pst.setInt(2, idProduit);
            pst.executeUpdate();
            System.out.println("Product removed from the offer!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ObservableList<Produit> getAllProduitsOffre() {
        String requete = "SELECT op.*, p.* FROM offreProduit op JOIN produit p ON op.idProduit = p.idProduit";
        ObservableList<Produit> produits = FXCollections.observableArrayList();

        try (PreparedStatement pst = conn.prepareStatement(requete);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Produit produit = new Produit();
                produit.setIdProduit(rs.getInt("idProduit"));
                produit.setNomProduit(rs.getString("nomProduit"));
                produit.setPrix(rs.getFloat("prix"));
                produit.setQuantite(rs.getInt("quantite"));
                // Set other product attributes if needed
                produits.add(produit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des produits de l'offre : " + e.getMessage());
        }
        return produits;
    }

}







































        /*
    @Override
    public void ajouterProduitAuOffre(Offre offre, int idProduit) {
        String requete = "INSERT INTO offreProduit (idOffre,idProduit) VALUES (?, ?)";
        try {
            PreparedStatement pst = conn.prepareStatement(requete);
            pst.setInt(1, offre.getIdOffre());
            pst.setInt(2, idProduit);

            pst.executeUpdate();
            System.out.println("Produit ajouté dans l'offre!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void DeleteProduitAuOffre(Offre offre, int idProduit) {
        String requete = "DELETE FROM offreProduit where idOffre= ? and idProduit = ?";
        try {
            pst=conn.prepareStatement(requete);
            pst.setInt(1, offre.getIdOffre());
            pst.setInt(2, idProduit);
            pst.executeUpdate();
            System.out.println("produit supprimé de cette offre!");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits du cette offre");

            throw new RuntimeException(e);
        }
    }




    @Override
    public ObservableList<OffreProduit> getAllProduitsOffre() {
        String requete = "SELECT op.*,pr.*, o.*, c.* " +
                "FROM offreProduit op " +
                "JOIN produit pr ON op.idProduit = pr.idProduit " +
                "JOIN offre o ON op.idOffre = o.idOffre " +
                "JOIN categorie c ON pr.categorie_id = c.idCategorie";


        ObservableList<OffreProduit> list = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(requete);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Offre off=new Offre(rs.getInt("pn.idOffre"));
                Categorie c = new Categorie(rs.getInt("c.idCategorie"), rs.getString("c.nomCategorie"),rs.getString("c.imageCategorie"));
                Produit prod = new Produit(rs.getInt("pr.idProduit"), rs.getString("pr.nomProduit"), rs.getInt("pr.quantite"), rs.getFloat("pr.prix"), c, rs.getString("pr.imageProduit"));
                OffreProduit pnss = new OffreProduit(rs.getInt("pc.idOffreProduit"),prod, off);
                list.add(pnss);
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage()); // Ajout d'une impression de message d'erreur
            System.out.println("Erreur lors de la récupération des produits de l'offre");

        }

        return list;
    }


}
/*@Override
    public ObservableList<OffreProduit> getProduitsDuPanierUtilisateur(Offre offre) {
        String requete = "SELECT pc.*, pr.*, o.*, c.* " +
                "FROM produitcart pc " +
                "JOIN produit pr ON pc.idProduit = pr.idProduit " +
                "JOIN offre o ON o.idOffre = pr.idOffre " +
                "JOIN categorie c ON pr.categorie_id = c.idCategorie " +
                "WHERE pc.idOffre = ?";


        ObservableList<OffreProduit> list = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(requete);
            preparedStatement.setInt(1, offre.getIdOffre());
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Categorie c = new Categorie(rs.getInt("c.idCategorie"), rs.getString("c.nomCategorie"),rs.getString("c.imageCategorie"));
                Produit prod = new Produit(rs.getInt("pr.idProduit"), rs.getString("pr.nomProduit"), rs.getInt("pr.quantite"), rs.getFloat("pr.prix"), c, rs.getString("pr.imageProduit"));
                OffreProduit pnss = new OffreProduit(rs.getInt("pc.idOffreProduit"), prod, offre);
                list.add(pnss);
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage()); // Ajout d'une impression de message d'erreur
            System.out.println("Erreur lors de la récupération des produits du panier");

        }

        return list;
    }
*/