package Service;

import entities.Offre;
import entities.Produit;
import utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OffreService implements IServiceOffre<Offre> {
    private Connection conn;
    private Statement statement;
    private PreparedStatement pst;
    private ProduitService produitService; // Service pour les opérations liées aux produits

    public OffreService() {
        conn = DataSource.getInstance().getCnx();
        produitService = new ProduitService(); // Initialisation du service des produits
    }
    public void addOffre(Offre o) {
        System.out.println("********************offre" + o.toString());
        String insertOffreQuery = "INSERT INTO offre (nomOffre, descriptionOffre,dateDebut,dateFin, imageOffre,reduction) VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement pstOffre = conn.prepareStatement(insertOffreQuery, Statement.RETURN_GENERATED_KEYS);

            pstOffre.setString(1, o.getNomOffre());
            pstOffre.setString(2, o.getDescriptionOffre());
            if (o.getDateDebut() != null) {
                pstOffre.setDate(3, new java.sql.Date(o.getDateDebut().getTime()));
            } else {
                pstOffre.setNull(3, Types.DATE);
            }
            // Gestion de la date de fin
            if (o.getDateFin() != null) {
                pstOffre.setDate(4, new java.sql.Date(o.getDateFin().getTime()));
            } else {
                pstOffre.setNull(4, Types.DATE);
            }
            pstOffre.setString(5, o.getImageOffre());
            pstOffre.setInt(6, o.getReduction());

            int affectedRows = pstOffre.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = pstOffre.getGeneratedKeys();
                if (generatedKeys.next()) {
                    // Handle generated keys if necessary
                }

                System.out.println("Offer inserted successfully!");
            } else {
                System.out.println("No rows affected. Offer not inserted.");
            }
            ResultSet generatedKeys = pstOffre.getGeneratedKeys();
            int idOffre = -1;
            if (generatedKeys.next()) {
                idOffre = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve the ID of the offer.");
            }
            insertOffreProduits(idOffre, o.getProduits());

        } catch (SQLException e) {
            e.printStackTrace(); // Print the detailed error message

            // Optionally, print specific information from the SQLException
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Error Message: " + e.getMessage());

            // Print the values you're trying to insert for further inspection
            System.err.println("Nom Offre: " + o.getNomOffre());
            System.err.println("Description Offre: " + o.getDescriptionOffre());
        }


    }

    private void insertOffreProduits(int idOffre, List<Produit> produits) {
        String insertOffreProduitQuery = "INSERT INTO offreProduit (idOffre, idProduit) VALUES (?, ?)";
        try {
            if (produits == null) {
                System.out.println("La liste des produits est null. Aucun produit à ajouter.");
                return;
            }
            PreparedStatement pstOffreProduit = conn.prepareStatement(insertOffreProduitQuery);
            for (Produit produit : produits) {
                pstOffreProduit.setInt(1, idOffre);
                pstOffreProduit.setInt(2, produit.getIdProduit());
                pstOffreProduit.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting offer products: " + e.getMessage());
        }
    }

    public List<Offre> readOffre() {
        String query = "SELECT * FROM offre";
        List<Offre> offres = new ArrayList<>();

        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                Offre offre = new Offre();
                offre.setIdOffre(rs.getInt("idOffre"));
                offre.setNomOffre(rs.getString("nomOffre"));
                offre.setDescriptionOffre(rs.getString("descriptionOffre"));
                offre.setDateDebut(rs.getDate("dateDebut"));
                offre.setDateFin(rs.getDate("dateFin"));
                offre.setProduits(getProduitsOfOffre(offre.getIdOffre())); // Set associated products
                offres.add(offre);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching offers: " + e.getMessage());
        }

        return offres;
    }

    @Override
    public void deleteOffre(int id) {
        // Suppression des relations dans la table 'offre_produit' avant de supprimer l'offre
        String deleteOffreProduitQuery = "DELETE FROM offreProduit WHERE idOffre = ?";
        try {
            pst = conn.prepareStatement(deleteOffreProduitQuery);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Relations entre l'offre et les produits associés supprimées avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression des relations entre l'offre et les produits associés : " + e.getMessage());
        }

        // Suppression de l'offre
        String deleteOffreQuery = "DELETE FROM offre WHERE idOffre = ?";
        try {
            pst = conn.prepareStatement(deleteOffreQuery);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Offre supprimée avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'offre : " + e.getMessage());
        }
    }

    @Override
    public void updateOffre(Offre o) {
        System.out.println( "ddddddddddddddddddddddddddddddd");
// Mise à jour des données de l'offre dans la table 'offre'
        String updateOffreQuery = "UPDATE offre SET nomOffre = ?, descriptionOffre = ?, dateDebut        = ?, dateFin = ? WHERE idOffre = ?";
        try {
            pst = conn.prepareStatement(updateOffreQuery);
            pst.setString(1, o.getNomOffre());
            pst.setString(2, o.getDescriptionOffre());
            pst.setDate(3, new java.sql.Date(o.getDateDebut().getTime()));
            pst.setDate(4, new java.sql.Date(o.getDateFin().getTime()));
            pst.setInt(5, o.getIdOffre());
            pst.executeUpdate();
            System.out.println("Offre mise à jour avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'offre : " + e.getMessage());
        }
    }

    private List<Produit> getProduitsOfOffre(int idOffre) {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT p.* FROM produit p INNER JOIN offreProduit op ON p.idProduit = op.idProduit WHERE op.idOffre = ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, idOffre);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Produit produit = new Produit();
                    produit.setIdProduit(rs.getInt("idProduit"));
                    produit.setNomProduit(rs.getString("nomProduit"));
                    // Set other product attributes if needed
                    produits.add(produit);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching products of the offer: " + e.getMessage());
        }

        return produits;
    }

    // Other methods like readOffre, updateOffre, deleteOffre, etc.
}






































































    /*
    // Méthode pour récupérer les produits associés à une offre spécifique
    private List<Produit> getProduitsAssocies(int idOffre) {
        String query = "SELECT p.* FROM produit p " +
                "INNER JOIN offre_produit op ON p.idProduit = op.idProduit " +
                "WHERE op.idOffre = ?";
        List<Produit> produits = new ArrayList<>();
        try {
            pst = conn.prepareStatement(query);
            pst.setInt(1, idOffre);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                // Création d'un objet Produit pour chaque enregistrement récupéré
                Produit produit = new Produit(
                        rs.getInt("idProduit"),
                        rs.getString("nomProduit"),
                        rs.getInt("quantite"),
                        rs.getInt("prix"),
                        // Vous devez également récupérer la catégorie associée au produit ici
                        null, // Remplacer null par le code pour récupérer la catégorie
                        rs.getString("imageProduit")
                );
                produits.add(produit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des produits associés : " + e.getMessage());
        }
        return produits;
    }

    public void addOffre(Offre o) {
        String insertOffreQuery = "INSERT INTO offre (nomOffre, descriptionOffre, dateDebut, dateFin) VALUES (?, ?, ?, ?)";
        String insertOffreProduitQuery = "INSERT INTO offre_produit (idOffre, idProduit) VALUES (?, ?)";

        try {
            // Début de la transaction
            conn.setAutoCommit(false);

            // Insertion des données dans la table 'offre'
            PreparedStatement pstOffre = conn.prepareStatement(insertOffreQuery, Statement.RETURN_GENERATED_KEYS);

            pstOffre.setString(1, o.getNomOffre());
            pstOffre.setString(2, o.getDescriptionOffre());
            if (o.getDateDebut() != null) {
                pstOffre.setDate(3, new java.sql.Date(o.getDateDebut().getTime()));
            } else {
                pstOffre.setNull(3, Types.DATE); // Ou fournir une valeur par défaut
            }

            if (o.getDateFin() != null) {
                pstOffre.setDate(4, new java.sql.Date(o.getDateFin().getTime()));
            } else {
                pstOffre.setNull(4, Types.DATE); // Ou fournir une valeur par défaut
            }
            pstOffre.executeUpdate();

            // Récupération de l'ID de l'offre nouvellement insérée
            ResultSet generatedKeys = pstOffre.getGeneratedKeys();
            int idOffre = -1;
            if (generatedKeys.next()) {
                idOffre = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Échec de la récupération de l'ID de l'offre.");
            }

            // Insertion des relations dans la table 'offre_produit'
            PreparedStatement pstOffreProduit = conn.prepareStatement(insertOffreProduitQuery);
            for (Produit produit : o.getProduits()) {
                pstOffreProduit.setInt(1, idOffre);
                pstOffreProduit.setInt(2, produit.getIdProduit());
                pstOffreProduit.executeUpdate();
            }

            // Validation de la transaction
            conn.commit();
            conn.setAutoCommit(true);

            System.out.println("Offre ajoutée avec succès !");
        } catch (SQLException e) {
            try {
                // En cas d'erreur, annulation de la transaction
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                throw new RuntimeException("Erreur lors de l'annulation de la transaction : " + ex.getMessage());
            }
            throw new RuntimeException("Erreur lors de l'ajout de l'offre : " + e.getMessage());
        }
    }


    @Override
    public List<Offre> readOffre() {
        String requete = "SELECT * FROM offre";
        List<Offre> list = new ArrayList<>();
        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(requete);
            while (rs.next()) {
                // Récupération des produits associés à l'offre
                List<Produit> produits = getProduitsAssocies(rs.getInt("idOffre"));
                Offre offre = new Offre(
                        rs.getInt("idOffre"),
                        new ArrayList<>(),// Liste des produits associés à l'offre
                        rs.getString("descriptionOffre"),
                        rs.getString("nomOffre"),
                        rs.getDate("dateDebut"),
                        rs.getDate("dateFin"),
                        rs.getString("imageOffre"), // Assurez-vous que votre table offre a une colonne imageOffre
                        rs.getBoolean("statutOffre"), // De même pour statutOffre
                        rs.getInt("reduction")
                );
                offre.setProduits(getProduitsAssocies(offre.getIdOffre()));
                list.add(offre);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void deleteOffre(int id) {
        // Suppression des relations dans la table 'offre_produit' avant de supprimer l'offre
        String deleteOffreProduitQuery = "DELETE FROM offre_produit WHERE idOffre = ?";
        try {
            pst = conn.prepareStatement(deleteOffreProduitQuery);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Relations entre l'offre et les produits associés supprimées avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression des relations entre l'offre et les produits associés : " + e.getMessage());
        }

        // Suppression de l'offre
        String deleteOffreQuery = "DELETE FROM offre WHERE idOffre = ?";
        try {
            pst = conn.prepareStatement(deleteOffreQuery);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Offre supprimée avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'offre : " + e.getMessage());
        }
    }

    @Override
    public void updateOffre(Offre o) {
        // Mise à jour des données de l'offre dans la table 'offre'
        String updateOffreQuery = "UPDATE offre SET nomOffre = ?, descriptionOffre = ?, dateDebut        = ?, dateFin = ? WHERE idOffre = ?";
        try {
            pst = conn.prepareStatement(updateOffreQuery);
            pst.setString(1, o.getNomOffre());
            pst.setString(2, o.getDescriptionOffre());
            pst.setDate(3, new java.sql.Date(o.getDateDebut().getTime()));
            pst.setDate(4, new java.sql.Date(o.getDateFin().getTime()));
            pst.setInt(5, o.getIdOffre());
            pst.executeUpdate();
            System.out.println("Offre mise à jour avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'offre : " + e.getMessage());
        }

        // Suppression des anciennes relations entre l'offre et les produits associés
        String deleteOldRelationsQuery = "DELETE FROM offre_produit WHERE idOffre = ?";
        try {
            pst = conn.prepareStatement(deleteOldRelationsQuery);
            pst.setInt(1, o.getIdOffre());
            pst.executeUpdate();
            System.out.println("Anciennes relations entre l'offre et les produits associés supprimées avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression des anciennes relations entre l'offre et les produits associés : " + e.getMessage());
        }

        // Insertion des nouvelles relations entre l'offre mise à jour et les produits associés
        String insertNewRelationsQuery = "INSERT INTO offre_produit (idOffre, idProduit) VALUES (?, ?)";
        try {
            PreparedStatement pstNewRelations = conn.prepareStatement(insertNewRelationsQuery);
            for (Produit produit : o.getProduits()) {
                pstNewRelations.setInt(1, o.getIdOffre());
                pstNewRelations.setInt(2, produit.getIdProduit());
                pstNewRelations.executeUpdate();
            }
            System.out.println("Nouvelles relations entre l'offre et les produits associés ajoutées avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout des nouvelles relations entre l'offre et les produits associés : " + e.getMessage());
        }
    }*/




