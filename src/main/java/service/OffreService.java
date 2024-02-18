package service;

import entities.Categorie;
import entities.Offre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.DataSource;

import java.sql.*;

public class OffreService implements IServiceOffre<Offre>{
    private Connection conn;
    private Statement statement;
    private PreparedStatement pst;
    public OffreService()
    {
        conn= DataSource.getInstance().getCnx();
    }


    @Override
    public ObservableList<Offre> readOffre()
    {
        String requete = "select * from offre";
        ObservableList<Offre> list = FXCollections.observableArrayList();

        try {
            statement = conn.createStatement();
            ResultSet rs=statement.executeQuery(requete);
            while (rs.next()) {
                Offre o = new Offre(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getDate(4),rs.getDate(5));
                list.add(o);
            }
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return list;
    }

}
