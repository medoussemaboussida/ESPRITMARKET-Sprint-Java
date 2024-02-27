package service;

import entities.Dons;
import entities.utilisateur;
import utils.DataSource;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


public class DonsService implements IServiceDons {
    private Connection conn;
    private PreparedStatement pst;

    public DonsService() {
        conn = DataSource.getInstance().getCnx();
    }

    // Méthode pour récupérer les dons d'un utilisateur spécifique par son ID
    public List<Dons> getDonsByUserId(int userId) {
        List<Dons> donsList = new ArrayList<>();
        String query = "SELECT * FROM dons WHERE idUser = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Dons don = new Dons();
                don.setIdDons(resultSet.getInt("idDons"));
                don.setIdUser(resultSet.getInt("idUser"));
                don.setNbPoints(resultSet.getInt("nbpoints"));
                don.setDate_ajout(resultSet.getTimestamp("date_ajout"));
                don.setEtatStatutDons(resultSet.getString("etatStatutDons"));
                donsList.add(don);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérez les exceptions de manière appropriée
        }

        return donsList;
    }


    public List<Dons> getAllDonsWithUserDetails() {
        List<Dons> donsList = new ArrayList<>();
        String query = "SELECT D.idDons, D.idUser, U.nomUser, U.prenomUser, U.emailUser, U.numTel, D.nbPoints, D.date_ajout, D.etatStatutDons " +
                "FROM Dons D " +
                "JOIN Utilisateur U ON D.idUser = U.idUser";
        try {
            pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Dons dons = new Dons();
                dons.setIdDons(rs.getInt("idDons"));
                dons.setIdUser(rs.getInt("idUser"));
                dons.setNomUser(rs.getString("nomUser"));
                dons.setPrenomUser(rs.getString("prenomUser"));
                dons.setEmailUser(rs.getString("emailUser"));
                dons.setNumTel(rs.getString("numTel"));
                dons.setNbPoints(rs.getInt("nbPoints"));
                dons.setDate_ajout(rs.getDate("date_ajout"));
                dons.setEtatStatutDons(rs.getString("etatStatutDons"));
                donsList.add(dons);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donsList;
    }

    @Override
    public int addDons(utilisateur user, int donPoints) {
        try {
            // Obtenir la date actuelle
            Date dateAjout = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateAjoutString = sdf.format(dateAjout);

            // Ajouter le don avec la date actuelle dans la table Dons
            String insertQuery = "INSERT INTO Dons (idUser, nbpoints, date_ajout) VALUES (?, ?, ?)";
            pst = conn.prepareStatement(insertQuery);
            pst.setInt(1, user.getIdUser());
            pst.setInt(2, donPoints);
            pst.setString(3, dateAjoutString);
            pst.executeUpdate();

            System.out.println("Don ajouté avec succès.");

            // Soustraire les points du don de l'utilisateur
            utilisateurService userService = new utilisateurService();
            int remainingPoints = user.getNbPoints() - donPoints;
            userService.updateUserPoints(user.getIdUser(), remainingPoints);

            // Afficher les points de l'utilisateur après l'ajout du don
            System.out.println("Points de l'utilisateur après l'ajout du don : " + remainingPoints);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du don: " + e.getMessage());
        } finally {
            closeStatement();
        }
        return donPoints;
    }

    @Override
    public boolean supprimerDon(int donsId, int nbPoints) {
        try {
            // Récupérer le nombre de points actuel du don
            int currentPoints = 0;
            String selectQuery = "SELECT nbpoints FROM Dons WHERE idDons = ?";
            try (PreparedStatement pstSelect = conn.prepareStatement(selectQuery)) {
                pstSelect.setInt(1, donsId);
                try (ResultSet rs = pstSelect.executeQuery()) {
                    if (rs.next()) {
                        currentPoints = rs.getInt("nbpoints");
                    } else {
                        System.out.println("Le don spécifié n'existe pas.");
                        return false; // Le don spécifié n'existe pas
                    }
                }
            }

            // Vérifier si le don a suffisamment de points à supprimer
            if (currentPoints >= nbPoints) {
                // Mettre à jour le nombre de points du don après la suppression
                int remainingPoints = currentPoints - nbPoints;
                String updateQuery = "UPDATE Dons SET nbpoints = ? WHERE idDons = ?";
                try (PreparedStatement pstUpdate = conn.prepareStatement(updateQuery)) {
                    pstUpdate.setInt(1, remainingPoints);
                    pstUpdate.setInt(2, donsId);
                    pstUpdate.executeUpdate();
                }
                System.out.println("Points supprimés avec succès du don.");
                return true; // Succès de la suppression des points du don
            } else {
                System.out.println("Le don spécifié ne contient pas suffisamment de points à supprimer.");
                return false; // Le don ne contient pas suffisamment de points à supprimer
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des points du don: " + e.getMessage());
            return false; // Échec de la suppression des points du don
        }
    }

    public boolean supprimerDons(Dons don) {
        String query = "DELETE FROM Dons WHERE idDons = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, don.getIdDons());
            int rowsDeleted = pst.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean donExists(int idDon) {
        String query = "SELECT COUNT(*) FROM Dons WHERE idDons = ?";
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, idDon);
            ResultSet rs = pst.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'existence du don : " + e.getMessage());
            return false;
        } finally {
            closeStatement();
        }
    }

    @Override
    public void updateDonsPoints(int donsId, int newPoints) {
        try {
            String query = "UPDATE Dons SET nbpoints = ? WHERE idDons = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, newPoints);
            pst.setInt(2, donsId);
            pst.executeUpdate();
            System.out.println("Points du don mis à jour avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour des points du don : " + e.getMessage());
        } finally {
            closeStatement();
        }
    }

    private void closeStatement() {
        try {
            if (pst != null) {
                pst.close();
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la fermeture du PreparedStatement: " + e.getMessage());
        }
    }



    @Override
    public List<Dons> getAllDons() {
        List<Dons> donsList = new ArrayList<>();
        String query = "SELECT D.idDons, U.nomUser, U.prenomUser, U.emailUser, D.nbPoints, D.date_ajout, D.etatStatutDons " +
                "FROM Dons D " +
                "JOIN Utilisateur U ON D.idUser = U.idUser";


        try (PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                int idDons = rs.getInt("idDons");
                int nbPoints = rs.getInt("nbPoints");
                Date dateAjout = rs.getTimestamp("date_ajout");
                String etatStatutDons = rs.getString("etatStatutDons");
                String nomUser = rs.getString("nomUser");
                String prenomUser = rs.getString("prenomUser");
                String emailUser = rs.getString("emailUser");

                Dons dons = new Dons(idDons, nomUser, prenomUser, emailUser, nbPoints, dateAjout, etatStatutDons);
                donsList.add(dons);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des dons : " + e.getMessage());
        }

        return donsList;
    }
    @Override
    public int addDonsWithStatus(utilisateur user, int donPoints) {
        try {
            // Obtenir la date actuelle
            Date dateAjout = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateAjoutString = sdf.format(dateAjout);

            // Ajouter le don avec la date actuelle et l'état du statut par défaut "En cours"
            String insertQuery = "INSERT INTO Dons (idUser, nbpoints, date_ajout, etatStatutDons) VALUES (?, ?, ?, ?)";
            pst = conn.prepareStatement(insertQuery);
            pst.setInt(1, user.getIdUser());
            pst.setInt(2, donPoints);
            pst.setString(3, dateAjoutString);
            pst.setString(4, "En cours"); // Par défaut, l'état du statut est "En cours"
            pst.executeUpdate();

            System.out.println("Don ajouté avec succès.");

            // Soustraire les points du don de l'utilisateur
            utilisateurService userService = new utilisateurService();
            int remainingPoints = user.getNbPoints() - donPoints;
            userService.updateUserPoints(user.getIdUser(), remainingPoints);

            // Afficher les points de l'utilisateur après l'ajout du don
            System.out.println("Points de l'utilisateur après l'ajout du don : " + remainingPoints);

            return remainingPoints;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du don: " + e.getMessage());
            return -1;
        } finally {
            closeStatement();
        }
    }




    @Override
    public void addDons(utilisateur user, int donPoints, String etatStatutDons) {
        try {
            // Obtenir la date actuelle
            Date dateAjout = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateAjoutString = sdf.format(dateAjout);

            // Ajouter le don avec la date actuelle et l'état du statut dans la table Dons
            String insertQuery = "INSERT INTO Dons (idUser, nbpoints, date_ajout, etatStatutDons) VALUES (?, ?, ?, ?)";
            pst = conn.prepareStatement(insertQuery);
            pst.setInt(1, user.getIdUser());
            pst.setInt(2, donPoints);
            pst.setString(3, dateAjoutString);
            pst.setString(4, etatStatutDons); // Ajout de l'état du statut
            pst.executeUpdate();

            System.out.println("Don ajouté avec succès.");

            // Soustraire les points du don de l'utilisateur
            utilisateurService userService = new utilisateurService();
            int remainingPoints = user.getNbPoints() - donPoints;
            userService.updateUserPoints(user.getIdUser(), remainingPoints);

            // Afficher les points de l'utilisateur après l'ajout du don
            System.out.println("Points de l'utilisateur après l'ajout du don : " + remainingPoints);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du don: " + e.getMessage());
        } finally {
            closeStatement();
        }
    }


    @Override
    public void updateDons(Dons don) {
        try {
            String query = "UPDATE Dons SET nbpoints = ?, etatStatutDons = ? WHERE idDons = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, don.getNbPoints());
            pst.setString(2, don.getEtatStatutDons());
            pst.setInt(3, don.getIdDons());
            pst.executeUpdate();
            System.out.println("Don mis à jour avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du don : " + e.getMessage());
        } finally {
            closeStatement();
        }
    }


    public void addEtatStatutDons(int idDons, String nouvelEtat) throws SQLException {
        // Mettre à jour l'état du statut des dons pour le don spécifié
        String query = "UPDATE Dons SET etatStatutDons = ? WHERE idDons = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, nouvelEtat);
            pst.setInt(2, idDons);
            pst.executeUpdate();
            System.out.println("État du statut de don mis à jour avec succès pour le don avec l'ID " + idDons);
        }
    }











    public void updateEtatStatutDons(int donsId, String nouvelEtat) {
        try {
            String query = "UPDATE Dons SET etatStatutDons = ? WHERE idDons = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, nouvelEtat);
            pst.setInt(2, donsId);
            pst.executeUpdate();
            System.out.println("État du statut de don mis à jour avec succès.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'état du statut de don : " + e.getMessage());
        } finally {
            closeStatement();
        }
    }


    public boolean deleteEtatStatutDons(String etatStatutDons) {
        try {
            // Vérifier si l'état du statut de dons existe
            String checkQuery = "SELECT COUNT(*) FROM EtatStatutDons WHERE etatStatutDons = ?";
            pst = conn.prepareStatement(checkQuery);
            pst.setString(1, etatStatutDons);
            ResultSet rs = pst.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            if (count == 0) {
                System.out.println("L'état du statut de dons spécifié n'existe pas.");
                return false;
            }

            // Supprimer l'état du statut de dons de la table EtatStatutDons
            String deleteQuery = "DELETE FROM EtatStatutDons WHERE etatStatutDons = ?";
            pst = conn.prepareStatement(deleteQuery);
            pst.setString(1, etatStatutDons);
            pst.executeUpdate();

            System.out.println("État du statut de dons supprimé avec succès : " + etatStatutDons);
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'état du statut de dons : " + e.getMessage());
            return false;
        } finally {
            closeStatement();
        }
    }
    // Ajouter la méthode addDonsForDemande dans la classe DonsService
    public void addDonsForDemande(utilisateur user, int donPoints, int idDemande) {
        // Insérer le don dans la table Dons avec l'ID de l'utilisateur, les points de don et la date actuelle
        int donId = addDons(user, donPoints);

        // Mettre à jour la table demande_dons avec l'ID du don correspondant
        if (donId != -1) {
            String updateQuery = "UPDATE demandedons SET idDons = ?, nbpoints = ? WHERE idDemande = ?";
            try {
                PreparedStatement pst = conn.prepareStatement(updateQuery);
                pst.setInt(1, donId);
                pst.setInt(2, donPoints); // Ajout du nombre de points ajoutés
                pst.setInt(3, idDemande);
                pst.executeUpdate();
                System.out.println("Don ajouté avec succès pour la demande avec l'ID : " + idDemande);
            } catch (SQLException e) {
                System.out.println("Erreur lors de l'ajout du don pour la demande : " + e.getMessage());
            }
        }
    }

    // Méthode pour récupérer l'ID du dernier don ajouté
    public int getLastInsertedDonId() {
        String query = "SELECT LAST_INSERT_ID() AS last_id FROM dons";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("last_id");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'ID du dernier don : " + e.getMessage());
        }
        return -1; // Retourne -1 si aucune ID n'est trouvée ou s'il y a une erreur
    }
 //   @Override



    // Méthode pour récupérer tous les e-mails des utilisateurs


    public Map<String, Integer> getAllUserNbPoints() throws SQLException {
        Map<String, Integer> userNbPoints = new HashMap<>();
        String query = "SELECT emailUser, nbPoints FROM Utilisateur JOIN Dons ON Utilisateur.idUser = Dons.idUser";
        try (PreparedStatement pst = conn.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                String email = rs.getString("emailUser");
                int nbPoints = rs.getInt("nbPoints");
                userNbPoints.put(email, nbPoints);
            }
        }
        return userNbPoints;
    }
    public int getDonsIdByEmail(String email) throws SQLException {
        String query = "SELECT idDons FROM Dons JOIN Utilisateur ON Dons.idUser = Utilisateur.idUser WHERE emailUser = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idDons");
                }
            }
        }
        // Gérer le cas où aucun don n'est associé à cet e-mail
        return -1;
    }

}