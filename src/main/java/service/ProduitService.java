package service;

import entities.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DataSource;
import entities.Categorie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitService implements IServiceProduit<Produit> {
    private Connection conn;
    private Statement statement;
    private PreparedStatement pst;
    public ProduitService()
    {
        conn= DataSource.getInstance().getCnx();
    }

    @Override
    public void addProduit(Produit p)
    {
        String requete = "insert into Produit (categorie_id,nomProduit,quantite,prix,imageProduit) values (?,?,?,?,?)";
        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1,p.getCategorie().getIdCategorie());
            pst.setString(2,p.getNomProduit());
            pst.setInt(3,p.getQuantite());
            pst.setInt(4,p.getPrix());
            pst.setString(5,p.getImageProduit());
            pst.executeUpdate();
            System.out.println("Produit ajoutée!");
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
@Override
public ObservableList<Produit> readProduit()
{
    String requete = "select * from produit p,categorie c where p.categorie_id=c.idCategorie";
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
    public void modifyProduit(Produit p)
    {
        String requete = "UPDATE produit set categorie_id = ?,nomProduit = ?,quantite = ?,prix = ? ,imageProduit = ? where  idProduit= ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1,p.getCategorie().getIdCategorie());
            pst.setString(2,p.getNomProduit());
            pst.setInt(3,p.getQuantite());
            pst.setInt(4,p.getPrix());
            pst.setString(5,p.getImageProduit());
            pst.setInt(6,p.getIdProduit());
            pst.executeUpdate();
            System.out.println("Produit Modifiée!");
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
   public void deleteProduit(int idProduit)
    {

        String requete = "delete from produit where  idProduit = ?";
        try {
            pst=conn.prepareStatement(requete);
            pst.setInt(1, idProduit);
            pst.executeUpdate();
            System.out.println("produit supprimé!");
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

    }


    @Override
    public ObservableList<Produit> sortProduitPrixAsc()
    {
        ObservableList<Produit> list = FXCollections.observableArrayList();
        try {
            String req = "SELECT * FROM  produit p,categorie c where p.categorie_id=c.idCategorie order by p.prix asc";
            statement = conn.createStatement();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Categorie c = new Categorie(rs.getInt("c.idCategorie"), rs.getString("c.nomCategorie"),rs.getString("c.imageCategorie"));
                Produit prod = new Produit(rs.getInt(1),rs.getString(3),rs.getInt(4),rs.getInt(5),c,rs.getString(6));
                list.add(prod);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;
    }

    @Override
    public ObservableList<Produit> sortProduitPrixDesc()
    {
        ObservableList<Produit> list = FXCollections.observableArrayList();
        try {
            String req = "SELECT * FROM produit p,categorie c where p.categorie_id=c.idCategorie order by p.prix desc";
            statement = conn.createStatement();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Categorie c = new Categorie(rs.getInt("c.idCategorie"), rs.getString("c.nomCategorie"),rs.getString("c.imageCategorie"));
                Produit prod = new Produit(rs.getInt(1),rs.getString(3),rs.getInt(4),rs.getInt(5),c,rs.getString(6));
                list.add(prod);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;
    }
}


