package service;

import entities.Panier;
import entities.PanierProduit;
import entities.Produit;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class PanierProduitService  implements IServicePanierProduit<PanierProduitService>{
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

}
