package service;
import entities.Categorie;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService implements IServiceCategorie<Categorie> {

    private Connection conn;
    private Statement statement;
    private PreparedStatement pst;
    public CategorieService()
    {
        conn= DataSource.getInstance().getCnx();
    }

    @Override
    public void addCategorie(Categorie c) {
        String requete = "insert into categorie (nomCategorie,imageCategorie) values (?,?)";
        try {
            pst = conn.prepareStatement(requete);
            pst.setString(1,c.getNomCategorie() );
            pst.setString(2,c.getImageCategorie());
            pst.executeUpdate();
            System.out.println("Categorie ajoutée!");
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Override
   public List<Categorie> readCategorie()
    {
        String requete = "select * from categorie";
        List<Categorie> list=new ArrayList<>();
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

    @Override
    public void deleteCategorie(int id) {
        String requete = "delete from categorie where id = ?";
        try {
            pst=conn.prepareStatement(requete);
            pst.setInt(1,id);
            pst.executeUpdate();
            System.out.println("Categorie supprimé!");
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    @Override
   public void modifyCategorie(Categorie c)
    {
        String requete = "UPDATE categorie set nomCategorie = ?, imageCategorie = ? where id= ?";
        try {
            pst = conn.prepareStatement(requete);
            pst.setString(1,c.getNomCategorie() );
            pst.setString(2,c.getImageCategorie());
            pst.setInt(3,c.getId());
            pst.executeUpdate();
            System.out.println("Categorie Modifiée!");
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    }

