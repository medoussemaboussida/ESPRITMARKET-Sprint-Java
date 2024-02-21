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
    public void ajouterCommande(Panier panier)
    {
        String requete = "insert into commande (idPanier) values (?)";
        try {
            pst = conn.prepareStatement(requete);
            pst.setInt(1, panier.getIdPanier());
            pst.executeUpdate();
            System.out.println("Commande ajoutée!");
        } catch (SQLException e)
        {
            System.out.println("problème commande !");
            throw new RuntimeException(e);
        }
    }
}
