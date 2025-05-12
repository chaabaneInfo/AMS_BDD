package data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Vente {
    private int id;
    private int produitId;
    private String lot;
    private int quantite;
    private double prixVente;
    private Date dateVente;

    // Constructeur complet
    public Vente(int id, int produitId, String lot, int quantite, double prixVente, Date dateVente) {
        this.id = id;
        this.produitId = produitId;
        this.lot = lot;
        this.quantite = quantite;
        this.prixVente = prixVente;
        this.dateVente = dateVente;
    }

    // Constructeur sans ID (pour les ajouts)
    public Vente(int produitId, String lot, int quantite, double prixVente, Date dateVente) {
        this.produitId = produitId;
        this.lot = lot;
        this.quantite = quantite;
        this.prixVente = prixVente;
        this.dateVente = dateVente;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getProduitId() { return produitId; }
    public void setProduitId(int produitId) { this.produitId = produitId; }
    public String getLot() { return lot; }
    public void setLot(String lot) { this.lot = lot; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
    public double getPrixVente() { return prixVente; }
    public void setPrixVente(double prixVente) { this.prixVente = prixVente; }
    public Date getDateVente() { return dateVente; }
    public void setDateVente(Date dateVente) { this.dateVente = dateVente; }

    @Override
    public String toString() {
        return "Vente{" +
                "id=" + id +
                ", produitId=" + produitId +
                ", lot='" + lot + '\'' +
                ", quantite=" + quantite +
                ", prixVente=" + prixVente +
                ", dateVente=" + dateVente +
                '}';
    }

    // Ajouter une vente
    public static void addVente(Connection connection, Vente vente) throws SQLException {
        String query = "INSERT INTO ventes (produit_id, lot, quantite, prix_vente, date_vente) " +
                       "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vente.getProduitId());
            stmt.setString(2, vente.getLot());
            stmt.setInt(3, vente.getQuantite());
            stmt.setDouble(4, vente.getPrixVente());
            stmt.setDate(5, vente.getDateVente());
            stmt.executeUpdate();
        }
    }

    // Récupérer toutes les ventes
    public static List<Vente> getAllVentes(Connection connection) throws SQLException {
        List<Vente> ventes = new ArrayList<>();
        String query = "SELECT * FROM ventes";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Vente vente = new Vente(
                        rs.getInt("id"),
                        rs.getInt("produit_id"),
                        rs.getString("lot"),
                        rs.getInt("quantite"),
                        rs.getDouble("prix_vente"),
                        rs.getDate("date_vente")
                );
                ventes.add(vente);
            }
        }
        return ventes;
    }

    public static double getVentesJournalieres(Connection connection) throws SQLException {
        String query = "SELECT SUM(prix_vente * quantite) FROM ventes WHERE date_vente = CURRENT_DATE";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    // Récupérer le total des ventes du mois
    public static double getVentesMensuelles(Connection connection) throws SQLException {
        String query = "SELECT SUM(prix_vente * quantite) FROM ventes WHERE date_vente >= DATE_TRUNC('month', CURRENT_DATE)";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }
    

    public static List<Vente> getTopVentes(Connection connection) throws SQLException {
        List<Vente> ventes = new ArrayList<>();
        String query = "SELECT produit_id, SUM(quantite) as total_quantite FROM ventes GROUP BY produit_id ORDER BY total_quantite DESC LIMIT 10";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int produitId = rs.getInt("produit_id");
                int quantite = rs.getInt("total_quantite");

                // On crée une vente avec les données récupérées (ID fictif car non nécessaire ici)
                Vente vente = new Vente(produitId, "TOP", quantite, 0.0, null);
                ventes.add(vente);
            }
        }
        return ventes;
    }

    
    // Enregistrer un produit expiré ou endommagé
    public static void enregistrerProduitExpire(Connection connection, Vente vente) throws SQLException {
        vente.prixVente = 0.0; // Produit expiré vendu à 0€
        addVente(connection, vente); // Ajoute la vente dans la base de données

        //Mise à jour du stock
        String query = "UPDATE achats SET quantite = quantite - ? WHERE produit_id = ? AND quantite > 0";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vente.getQuantite());
            stmt.setInt(2, vente.getProduitId());
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Stock mis à jour pour produit expiré : " + vente.getProduitId());
            } else {
                System.out.println("Aucun stock à mettre à jour pour produit expiré : " + vente.getProduitId());
            }
        }
    }


    
    public static void mettreAJourStock(Connection connection, int produitId, String lot, int quantiteVendue) throws SQLException {
        // Vérification si c'est un produit expiré (lot commence par "EXP-")
        boolean estProduitExpire = lot.startsWith("EXP-");

        String query;
        if (estProduitExpire) {
            // Cas où le produit est expiré : mise à jour du premier lot disponible
            query = "UPDATE achats SET quantite = quantite - ? WHERE produit_id = ? AND quantite > 0 ORDER BY date_achat ASC LIMIT 1";
        } else {
            // Cas normal : mise à jour du stock pour le lot spécifique
            query = "UPDATE achats SET quantite = quantite - ? WHERE produit_id = ? AND CAST(id AS VARCHAR) = ?";
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, quantiteVendue);
            stmt.setInt(2, produitId);

            if (!estProduitExpire) {
                stmt.setString(3, lot); // Ajout du lot seulement si ce n'est pas un produit expiré
            }

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 0) {
                System.err.println("Aucune mise à jour du stock. Vérifiez si le produit et le lot existent.");
            } else {
                System.out.println("Stock mis à jour avec succès pour Produit ID: " + produitId + ", Lot: " + lot);
            }
        }
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

