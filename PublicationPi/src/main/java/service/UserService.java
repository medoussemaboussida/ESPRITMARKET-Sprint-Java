package service;

import entities.user;
import utils.DataSource;

import java.sql.*;

public class UserService implements IServiceUser<user>{

    private Connection conn;
    private PreparedStatement pste;

    private Statement ste;

    public UserService()
    {
        conn= DataSource.getInstance().getCnx();
    }
    @Override
    public user readById(int idUser) {
        String query = "SELECT * FROM utilisateur WHERE idUser = ?";
        try {
            conn = DataSource.getInstance().getCnx();
            pste = conn.prepareStatement(query);
            pste.setInt(1, idUser);
            ResultSet rs = pste.executeQuery();
            if (rs.next()) {
                // Récupérer les données de l'utilisateur à partir du résultat de la requête
                int id = rs.getInt("idUser");


                // Créer et retourner un objet utilisateur avec les données récupérées
                return new user(id);
            } else {
                // L'utilisateur avec cet ID n'existe pas
                return null;
            }
        } catch (SQLException e) {
            // Gérer les exceptions liées à la base de données
            e.printStackTrace();
            return null;
        } finally {
            // Fermer les ressources
            try {
                if (pste != null) {
                    pste.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public user findById(int idUser) {
        user utilisateur = null;
        ResultSet resultSet = null;

        try {
            // Préparez la requête SQL
            String query = "SELECT * FROM utilisateur WHERE idUser = ?";
            pste = conn.prepareStatement(query);
            pste.setInt(1, idUser);

            // Exécutez la requête
            resultSet = pste.executeQuery();

            // Vérifiez s'il y a des résultats
            if (resultSet.next()) {
                // Construisez l'objet Utilisateur à partir des résultats
                utilisateur = new user(
                        resultSet.getInt("idUser"),
                        resultSet.getString("nomUser"),
                        resultSet.getString("prenomUser")


                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
        } finally {
            // Fermez les ressources
            closeResources(pste, resultSet);
        }

        return utilisateur;
    }



    private void closeResources(PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL lors de la fermeture des ressources
        }
    }
}


