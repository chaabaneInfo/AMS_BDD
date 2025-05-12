package data;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;


public class Produit implements IData {
    private int id;
    private String numLot;
    private String nom;
    private String description;
    private String categorie;
    private double prix;
    private HashMap<String, fieldType> map;

    public Produit(int id, String numLot, String nom, String description, String categorie, double prix) {
        this.id = id;
        this.numLot = numLot;
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.prix = prix;
        this.getStruct();
    }

    @Override
    public void getStruct() {
        map = new HashMap<>();
        map.put("id", fieldType.NUMERIC); 
        map.put("numlot", fieldType.VARCHAR); 
        map.put("nom", fieldType.VARCHAR); 
        map.put("description", fieldType.VARCHAR); 
        map.put("categorie", fieldType.VARCHAR); 
        map.put("prix", fieldType.FLOAT8); 
    }


    @Override
    public String getValues() {
        return String.format("%d, '%s', '%s', '%s', '%s', %f", id, numLot, nom, description, categorie, prix);
    }

    @Override
    public HashMap<String, fieldType> getMap() {
        return map;
    }

    @Override
    public boolean check(HashMap<String, fieldType> tableStruct) {
        return map.equals(tableStruct);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumLot() {
        return numLot;
    }

    public void setNumLot(String numLot) {
        this.numLot = numLot;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", numLot='" + numLot + '\'' +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", categorie='" + categorie + '\'' +
                ", prix=" + prix +
                '}';
    }
    
    public static String getNomById(Connection connection, int produitId) throws SQLException {
        String query = "SELECT nom FROM product WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, produitId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom");
                }
            }
        }
        return "Inconnu"; 
    }
    
    public static List<Produit> getAllProduits(Connection connection) throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM product"; 
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produit produit = new Produit(
                    rs.getInt("id"),
                    rs.getString("numlot"), 
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getString("categorie"),
                    rs.getDouble("prix")
                );
                produits.add(produit);
            }
        }
        return produits;
    }



}
