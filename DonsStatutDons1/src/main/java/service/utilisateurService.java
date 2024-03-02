package service;

import entities.utilisateur;
import utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class utilisateurService {
    private static Connection conn;
    private static PreparedStatement pst;

    public utilisateurService() {
        conn = DataSource.getInstance().getCnx();
    }



    public List<utilisateur> getAllUsers() {
        List<utilisateur> userList = new ArrayList<>();
        try {
            String query = "SELECT * FROM utilisateur";
            pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idUser");
                int nbpoints = rs.getInt("nbpoints");
                String nomUser = rs.getString("nomUser");
                String prenomUser = rs.getString("prenomUser");
                String emailUser = rs.getString("emailUser");
                String numTel = rs.getString("numTel");
                utilisateur user = new utilisateur(id, nbpoints, nomUser, prenomUser, emailUser, numTel);
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs: " + e.getMessage());
        } finally {
            // Fermeture des ressources (PreparedStatement, ResultSet, etc.)
        }
        return userList;
    }

    public static utilisateur getUserById(int id) {
        utilisateur user = null;
        try {
            String query = "SELECT * FROM utilisateur WHERE idUser = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int nbpoints = rs.getInt("nbpoints");
                String nomUser = rs.getString("nomUser");
                String prenomUser = rs.getString("prenomUser");
                String emailUser = rs.getString("emailUser");
                String numTel = rs.getString("numTel");
                user = new utilisateur(id, nbpoints, nomUser, prenomUser, emailUser, numTel);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'utilisateur par ID: " + e.getMessage());
        } finally {
            // Fermeture des ressources (PreparedStatement, ResultSet, etc.)
        }
        return user;
    }

    public void updateUserPoints(int userId, int newNbPoints) {
        try {
            String query = "UPDATE utilisateur SET nbpoints = ? WHERE idUser = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, newNbPoints);
            pst.setInt(2, userId);
            pst.executeUpdate();
            System.out.println("Points de l'utilisateur mis à jour avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour des points de l'utilisateur: " + e.getMessage());
        } finally {
            // Fermeture des ressources (PreparedStatement, ResultSet, etc.)
        }
    }





}
