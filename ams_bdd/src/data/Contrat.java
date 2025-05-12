package data;

import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Contrat {
    private int id;             // Identifiant unique du contrat
    private int produitId;      // Identifiant du produit associé
    private int fournisseurId;  // Identifiant du fournisseur associé
    private double prix;        // Prix associé au contrat
    private Date dateDebut;     // Date de début du contrat
    private Date dateFin;       // Date de fin du contrat (peut être null)

    // Constructeur complet
    public Contrat(int id, int produitId, int fournisseurId, double prix, Date dateDebut, Date dateFin) {
        this.id = id;
        this.produitId = produitId;
        this.fournisseurId = fournisseurId;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Constructeur sans ID (pour les ajouts)
    public Contrat(int produitId, int fournisseurId, double prix, Date dateDebut, Date dateFin) {
        this.produitId = produitId;
        this.fournisseurId = fournisseurId;
        this.prix = prix;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    public int getFournisseurId() {
        return fournisseurId;
    }

    public void setFournisseurId(int fournisseurId) {
        this.fournisseurId = fournisseurId;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public String toString() {
        return "Contrat{" +
               "id=" + id +
               ", produitId=" + produitId +
               ", fournisseurId=" + fournisseurId +
               ", prix=" + prix +
               ", dateDebut=" + dateDebut +
               ", dateFin=" + dateFin +
               '}';
    }

    // Méthodes pour la gestion des contrats

    // Ajouter un contrat
    public static void addContrat(Connection connection, Contrat contrat) throws SQLException {
        String query = "INSERT INTO contrats (produit_id, fournisseur_id, prix, date_debut, date_fin) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, contrat.getProduitId());
            stmt.setInt(2, contrat.getFournisseurId());
            stmt.setDouble(3, contrat.getPrix());
            stmt.setDate(4, contrat.getDateDebut());
            if (contrat.getDateFin() != null) {
                stmt.setDate(5, contrat.getDateFin());
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            stmt.executeUpdate();
        }
    }

    // Modifier un contrat
    public static void updateContrat(Connection connection, int id, Double newPrix, Date newDateFin) throws SQLException {
        // Récupérer les informations du contrat existant
        Contrat oldContract = getContratById(connection, id);
        if (oldContract == null) return;

        // Mettre à jour l'ancien contrat avec la date de fin à aujourd'hui
        String updateOldContract = "UPDATE contrats SET date_fin = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(updateOldContract)) {
            stmt.setDate(1, new Date(System.currentTimeMillis())); // Aujourd'hui
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }

        // Déterminer la date de fin du nouveau contrat
        Date finalNewDateFin = (newDateFin != null) ? newDateFin : oldContract.getDateFin();

        // Insérer un nouveau contrat avec le nouveau prix et la bonne date de fin
        addContrat(connection, new Contrat(
                oldContract.getProduitId(),
                oldContract.getFournisseurId(),
                (newPrix != null) ? newPrix : oldContract.getPrix(),
                new Date(System.currentTimeMillis()), // Nouvelle date de début
                finalNewDateFin // Conserve l'ancienne date de fin si non modifiée
        ));
    }

    // Supprimer un contrat
    public static void deleteContrat(Connection connection, int id) throws SQLException {
        String query = "DELETE FROM contrats WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Récupérer tous les contrats
    public static List<Contrat> getAllContrats(Connection connection) throws SQLException {
        List<Contrat> contrats = new ArrayList<>();
        String query = "SELECT * FROM contrats";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Contrat contrat = new Contrat(
                        rs.getInt("id"),
                        rs.getInt("produit_id"),
                        rs.getInt("fournisseur_id"),
                        rs.getDouble("prix"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin")
                );
                contrats.add(contrat);
            }
        }
        return contrats;
    }

    // Récupérer un contrat par ID
    public static Contrat getContratById(Connection connection, int id) throws SQLException {
        String query = "SELECT * FROM contrats WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Contrat(
                            rs.getInt("id"),
                            rs.getInt("produit_id"),
                            rs.getInt("fournisseur_id"),
                            rs.getDouble("prix"),	
                            rs.getDate("date_debut"),
                            rs.getDate("date_fin")
                    );
                }
            }
        }
        return null;
    }
    
    public static List<Contrat> getContratsByFournisseur(Connection connection, int fournisseurId) throws SQLException {
        List<Contrat> contrats = new ArrayList<>();
        String query = "SELECT * FROM contrats WHERE fournisseur_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, fournisseurId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contrats.add(new Contrat(
                            rs.getInt("id"),
                            rs.getInt("produit_id"),
                            rs.getInt("fournisseur_id"),
                            rs.getDouble("prix"),
                            rs.getDate("date_debut"),
                            rs.getDate("date_fin")
                    ));
                }
            }
        }
        return contrats;
    }

}
