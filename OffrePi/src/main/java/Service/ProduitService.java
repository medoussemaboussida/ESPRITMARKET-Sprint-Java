package Service;

import entities.Categorie;
import entities.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DataSource;

import java.sql.*;

public class ProduitService  implements IServiceProduit<Produit> {
    private Connection conn;
    private Statement statement;
    private PreparedStatement pst;
    public ProduitService()
    {
        conn= DataSource.getInstance().getCnx();
    }


    @Override
    public ObservableList<Produit> readProduit()
    {
        String requete = " select * from produit";
        ObservableList<Produit> list= FXCollections.observableArrayList();
        try {
            statement = conn.createStatement();
            ResultSet rs=statement.executeQuery(requete);
            while (rs.next()) {
                Categorie c = new Categorie(rs.getInt("c.idCategorie"), rs.getString("c.nomCategorie"),rs.getString("c.imageCategorie"));
                Produit prod = new Produit(rs.getInt(1),rs.getString(3),rs.getInt(4),rs.getInt(5),c,rs.getString(6));
                list.add(prod);
            }
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return list;
    }
    @Override
    public ObservableList<Categorie> readCategorie()
    {
        String requete = "select * from categorie";
        ObservableList<Categorie> list = FXCollections.observableArrayList();

        try {
            statement = conn.createStatement();
            ResultSet rs=statement.executeQuery(requete);
            while (rs.next()) {
                Categorie cat = new Categorie(rs.getInt(1), rs.getString(2), rs.getString(3));
                list.add(cat);
            }
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return list;
    }
    public Categorie getCategorieById(int idCategorie) {
        String query = "SELECT * FROM categorie WHERE idCategorie = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, idCategorie);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Categorie(
                            rs.getInt("idCategorie"),
                            rs.getString("nomCategorie"),
                            rs.getString("imageCategorie")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la catégorie par ID : " + e.getMessage());
        }
        return null; // Retourne null si aucune catégorie trouvée pour l'ID donné
    }

    public ObservableList<Produit> getAllProduits() {
        ObservableList<Produit> produits = FXCollections.observableArrayList();
        String query = "SELECT * FROM produit p JOIN categorie c ON p.categorie_id = c.idCategorie ";

        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                Categorie categorie = getCategorieById(rs.getInt("c.idCategorie")); // Récupérer la catégorie associée au produit
                Produit produit = new Produit(
                        rs.getInt("idProduit"),
                        rs.getString("nomProduit"),
                        rs.getInt("quantite"),
                        rs.getInt("prix"),
                        categorie,
                        rs.getString("imageProduit")
                );
                produits.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produits;
    }


}

