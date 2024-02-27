package Service;

import entities.Utilisateur;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService implements IService<Utilisateur> {
    private Connection conn;
    private Statement ste;
    private PreparedStatement pst;


    public UtilisateurService() {
        conn = DataSource.getInstance().getCnx();
    }
    public void add(Utilisateur x){
        String requete = "INSERT INTO utilisateur (nomUser, prenomUser, emailUser, mdp, nbPoints, numTel, role) VALUES ('"
                + x.getNomUser() + "','" + x.getPrenomUser() + "','" + x.getEmailUser() + "','" + x.getMotDePasse() + "',"
                + x.getNbPoints() + ",'" + x.getNumeroTel() + "','" + x.getRole() + "')";

        try {
            ste= conn.createStatement();
            ste.executeUpdate(requete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ajouterPersonne(Utilisateur x) throws SQLException {
        Connection conn = DataSource.getInstance().getCnx();
        if (conn != null) {

            String query = "INSERT INTO Utilisateur (nomUser, prenomUser, emailUser, mdp, nbPoints, numTel, role) VALUES (?, ?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, x.getNomUser());
            preparedStatement.setString(2, x.getPrenomUser());
            preparedStatement.setString(3, x.getEmailUser());
            preparedStatement.setString(4, x.getMotDePasse());
            preparedStatement.setInt(5, x.getNbPoints());
            preparedStatement.setInt(6, x.getNumeroTel());
            preparedStatement.setString(7, x.getRole());
            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Person added successfully.");
            } else {
                System.out.println("Failed to add person.");
            }

        } else {
            System.out.println("Failed to establish database connection.");
        }
    }




    @Override
    public void delete(Utilisateur utilisateur) {
        String requete = "DELETE FROM utilisateur WHERE idUser = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(requete)) {
            preparedStatement.setInt(1, utilisateur.getIdUser());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Utilisateur utilisateur) {
        String requete = "UPDATE utilisateur SET nomUser=?, prenomUser=?, emailUser=?, mdp=?, nbPoints=?, numTel=?, role=? WHERE idUser=?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(requete)) {
            preparedStatement.setString(1, utilisateur.getNomUser());
            preparedStatement.setString(2, utilisateur.getPrenomUser());
            preparedStatement.setString(3, utilisateur.getEmailUser());
            preparedStatement.setString(4, utilisateur.getMotDePasse());
            preparedStatement.setInt(5, utilisateur.getNbPoints());
            preparedStatement.setInt(6, utilisateur.getNumeroTel());
            preparedStatement.setString(7, utilisateur.getRole());
            preparedStatement.setInt(8, utilisateur.getIdUser());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public List<Utilisateur> readAll() {
        String requete = "SELECT * FROM Utilisateur ";
        List<Utilisateur> list=new ArrayList<>();


        try {
            ste = conn.createStatement();
            ResultSet rs = ste.executeQuery(requete);
            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setIdUser(rs.getInt("idUser"));
                utilisateur.setNomUser(rs.getString("nomUser"));
                utilisateur.setPrenomUser(rs.getString("prenomUser"));
                utilisateur.setEmailUser(rs.getString("emailUser"));
                utilisateur.setMotDePasse(rs.getString("mdp"));
                utilisateur.setNbPoints(rs.getInt("nbPoints"));
                utilisateur.setNumeroTel(rs.getInt("numTel"));
                utilisateur.setRole(rs.getString("role"));

                list.add(utilisateur);

                list.add(new Utilisateur());
                //pst.close();
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }


    @Override
    public Utilisateur readbyId(int idUser) {

            String requete = "SELECT * FROM utilisateur WHERE idUser = ?";
            Utilisateur utilisateur = null;

            try (PreparedStatement preparedStatement = conn.prepareStatement(requete)) {
                preparedStatement.setInt(1, idUser);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // L'utilisateur a été trouvé, créez un objet Utilisateur avec les détails
                        utilisateur  = new Utilisateur();
                        utilisateur.setIdUser(resultSet.getInt("idUser"));
                        utilisateur.setNomUser(resultSet.getString("nomUser"));
                        utilisateur.setPrenomUser(resultSet.getString("prenomUser"));
                        utilisateur.setEmailUser(resultSet.getString("emailUser"));
                        utilisateur.setMotDePasse(resultSet.getString("mdp"));
                        utilisateur.setNbPoints(resultSet.getInt("nbPoints"));
                        utilisateur.setNumeroTel(resultSet.getInt("numTel"));
                        utilisateur.setRole(resultSet.getString("role"));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return utilisateur;
        }
//    public Utilisateur getUserByEmail(String email) {
//
//
//
//        String requete = "SELECT user.* FROM user WHERE email='" + email + "'";
//
//        Utilisateur t = null;
//        try {
//            Statement st = conn.createStatement();
//            ResultSet rs = st.executeQuery(requete);
//            while (rs.next()) {
//
//
//                t = new Utilisateur(rs.getInt("idUser"), rs.getString("nomUser"), rs.getString("prenomUser"), rs.getString("emailUser"), rs.getString("mdp"), rs.getInt("nbPoints"), rs.getInt("numeroTel"), rs.getString("role"));
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(UtilisateurService.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return t;
//
//    }

    public Utilisateur login(String email, String password) {
        String query = "SELECT * FROM utilisateur WHERE emailUser=? AND mdp=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Utilisateur(
                            resultSet.getInt("idUser"),
                            resultSet.getString("nomUser"),
                            resultSet.getString("prenomUser"),
                            resultSet.getString("emailUser"),
                            resultSet.getString("mdp"),
                            resultSet.getInt("nbPoints"),
                            resultSet.getInt("numTel"),
                            resultSet.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null; // Return null if login is unsuccessful
    }



    }









