package Service;
import entities.Utilisateur;
import utils.DataSource;

import java.sql.*;
public class UtilisateurService implements IServiceUtilisateur<Utilisateur>{
    private Connection conn;
    private Statement statement;
    private PreparedStatement pst;
    public UtilisateurService()
    {
        conn= DataSource.getInstance().getCnx();
    }

    public Utilisateur getUserById(int idUser) {
        Utilisateur utilisateur = null;
        ResultSet resultSet = null;

        try {
            // Préparez la requête SQL
            String query = "SELECT * FROM utilisateur WHERE idUser = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, idUser);

            // Exécutez la requête
            resultSet = pst.executeQuery();

            // Vérifiez s'il y a des résultats
            if (resultSet.next()) {
                // Construisez l'objet Utilisateur à partir des résultats
                utilisateur = new Utilisateur(
                        resultSet.getInt("idUser"),
                        resultSet.getString("nomUser"),
                        resultSet.getString("prenomUser"),
                        resultSet.getString("emailUser"),
                        resultSet.getString("mdp"),
                        resultSet.getString("nbPoints"),
                        resultSet.getInt("numTel"),
                        resultSet.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les exceptions SQL
        } finally {
            // Fermez les ressources
            closeResources(pst, resultSet);
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
