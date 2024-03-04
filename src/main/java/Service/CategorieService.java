package Service;

import entities.Categorie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DataSource;

import java.sql.*;

public class CategorieService implements IServiceCategorie<Categorie> {

    private Connection conn;
    private Statement statement;
    private PreparedStatement pst;
    public CategorieService()
    {
        conn= DataSource.getInstance().getCnx();
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



    @Override
    public ObservableList<Categorie> sortCategorieAsc()
    {
        ObservableList<Categorie> list = FXCollections.observableArrayList();
        try {
            String req = "SELECT * FROM categorie order by nomCategorie asc";
            statement = conn.createStatement();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Categorie c = new Categorie(rs.getString("nomCategorie"), rs.getString("imageCategorie"));
                list.add(c);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;
    }

    @Override
    public ObservableList<Categorie> sortCategorieDesc()
    {
        ObservableList<Categorie> list = FXCollections.observableArrayList();
        try {
            String req = "SELECT * FROM categorie order by nomCategorie desc";
            statement = conn.createStatement();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Categorie c = new Categorie(rs.getString("nomCategorie"), rs.getString("imageCategorie"));
                list.add(c);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return list;
    }

}
