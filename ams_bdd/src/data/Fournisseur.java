package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Fournisseur {
    private int id;             // Identifiant unique
    private String nom;         // Nom de la société
    private String siret;       // Numéro SIRET
    private String adresse;     // Adresse complète
    private String email;       // Adresse email
    private String telephone;   // Numéro de téléphone

    // Constructeur complet
    public Fournisseur(int id, String nom, String siret, String adresse, String email, String telephone) {
        this.id = id;
        this.nom = nom;
        this.siret = siret;
        this.adresse = adresse;
        this.email = email;
        this.telephone = telephone;
    }

    // Constructeur sans ID (pour les ajouts)
    public Fournisseur(String nom, String siret, String adresse, String email, String telephone) {
        this.nom = nom;
        this.siret = siret;
        this.adresse = adresse;
        this.email = email;
        this.telephone = telephone;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", siret='" + siret + '\'' +
                ", adresse='" + adresse + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }

    // Méthodes pour la gestion des fournisseurs

    // Ajouter un fournisseur
    public static void addFournisseur(Connection connection, Fournisseur fournisseur) throws SQLException {
        String query = "INSERT INTO fournisseurs (nom, siret, adresse, email, telephone) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getSiret());
            stmt.setString(3, fournisseur.getAdresse());
            stmt.setString(4, fournisseur.getEmail());
            stmt.setString(5, fournisseur.getTelephone());
            stmt.executeUpdate();
        }
    }

    // Modifier un fournisseur
    public static void updateFournisseur(Connection connection, Fournisseur fournisseur) throws SQLException {
        String query = "UPDATE fournisseurs SET nom = ?, siret = ?, adresse = ?, email = ?, telephone = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, fournisseur.getNom());
            stmt.setString(2, fournisseur.getSiret());
            stmt.setString(3, fournisseur.getAdresse());
            stmt.setString(4, fournisseur.getEmail());
            stmt.setString(5, fournisseur.getTelephone());
            stmt.setInt(6, fournisseur.getId());
            stmt.executeUpdate();
        }
    }

    // Supprimer un fournisseur
    public static void deleteFournisseur(Connection connection, int id) throws SQLException {
        String query = "DELETE FROM fournisseurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Récupérer tous les fournisseurs
    public static List<Fournisseur> getAllFournisseurs(Connection connection) throws SQLException {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String query = "SELECT * FROM fournisseurs";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Fournisseur fournisseur = new Fournisseur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("siret"),
                        rs.getString("adresse"),
                        rs.getString("email"),
                        rs.getString("telephone")
                );
                fournisseurs.add(fournisseur);
            }
        }
        return fournisseurs;
    }

    // Récupérer un fournisseur par ID
    public static Fournisseur getFournisseurById(Connection connection, int id) throws SQLException {
        String query = "SELECT * FROM fournisseurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Fournisseur(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("siret"),
                            rs.getString("adresse"),
                            rs.getString("email"),
                            rs.getString("telephone")
                    );
                }
            }
        }
        return null;
    }
    
    public static String getNomById(Connection connection, int fournisseurId) throws SQLException {
        String query = "SELECT nom FROM fournisseurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, fournisseurId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom");
                }
            }
        }
        return "Inconnu";
    }

}

