package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Achat {
    private int id;
    private int produitId;
    private int fournisseurId;
    private int quantite;
    private double prixUnitaire;
    private Date dateAchat;
    private Date datePeremption;

    // Constructeur complet
    public Achat(int id, int produitId, int fournisseurId, int quantite, double prixUnitaire, Date dateAchat, Date datePeremption) {
        this.id = id;
        this.produitId = produitId;
        this.fournisseurId = fournisseurId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.dateAchat = dateAchat;
        this.datePeremption = datePeremption;

    }

    // Constructeur sans ID (pour les ajouts)
    public Achat(int produitId, int fournisseurId, int quantite, double prixUnitaire, Date dateAchat, Date datePeremption) {
        this.produitId = produitId;
        this.fournisseurId = fournisseurId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.dateAchat = dateAchat;
        this.datePeremption = datePeremption;

    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProduitId() { return produitId; }
    public void setProduitId(int produitId) { this.produitId = produitId; }
    public int getFournisseurId() { return fournisseurId; }
    public void setFournisseurId(int fournisseurId) { this.fournisseurId = fournisseurId; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(double prixUnitaire) { this.prixUnitaire = prixUnitaire; }
    public Date getDateAchat() { return dateAchat; }
    public void setDateAchat(Date dateAchat) { this.dateAchat = dateAchat; }
    public Date getDatePeremption() { return datePeremption; }
    public void setDatePeremption(Date datePeremption) { this.datePeremption = datePeremption; }

    @Override
    public String toString() {
        return "Achat{" +
                "id=" + id +
                ", produitId=" + produitId +
                ", fournisseurId=" + fournisseurId +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", dateAchat=" + dateAchat +
                ", datePeremption=" + datePeremption +
                '}';
    }

    // Ajouter un achat
    public static void addAchat(Connection connection, Achat achat) throws SQLException {


        String query = "INSERT INTO achats (produit_id, fournisseur_id, quantite, prix_unitaire, date_achat, date_peremption) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, achat.getProduitId());
            stmt.setInt(2, achat.getFournisseurId());
            stmt.setInt(3, achat.getQuantite());
            stmt.setDouble(4, achat.getPrixUnitaire());
            stmt.setDate(5, achat.getDateAchat());
            if (achat.getDatePeremption() != null) {
                stmt.setDate(6, achat.getDatePeremption());
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }
            stmt.executeUpdate();
        }
    }
    
    // Calculer le stock total d'un produit
    public static int getStockProduit(Connection connection, int produitId) throws SQLException {
        String query = "SELECT SUM(quantite) AS stock FROM achats WHERE produit_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, produitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock");
                }
            }
        }
        return 0;
    }


    // Récupérer tous les achats
    public static List<Achat> getAllAchats(Connection connection) throws SQLException {
        List<Achat> achats = new ArrayList<>();
        String query = "SELECT * FROM achats";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Achat achat = new Achat(
                        rs.getInt("id"),
                        rs.getInt("produit_id"),
                        rs.getInt("fournisseur_id"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix_unitaire"),
                        rs.getDate("date_achat"),
                        rs.getDate("date_peremption")
                );
                achats.add(achat);
            }
        }
        return achats;
    }

    // Supprimer un achat
    public static void deleteAchat(Connection connection, int id) throws SQLException {
        String query = "DELETE FROM achats WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public double getPrixTotal() {
        return this.quantite * this.prixUnitaire;
    }

    public static List<Achat> getAchatsByProduit(Connection connection, int produitId) throws SQLException {
        List<Achat> achats = new ArrayList<>();
        String query = "SELECT * FROM achats WHERE produit_id = ? AND quantite > 0 ORDER BY date_achat ASC";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, produitId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Achat achat = new Achat(
                            rs.getInt("id"),
                            rs.getInt("produit_id"),
                            rs.getInt("fournisseur_id"),
                            rs.getInt("quantite"),
                            rs.getDouble("prix_unitaire"),
                            rs.getDate("date_achat"),
                            rs.getDate("date_peremption")
                    );
                    achats.add(achat);
                }
            }
        }
        return achats;
    }

}


