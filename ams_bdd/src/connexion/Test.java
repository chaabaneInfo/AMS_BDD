package connexion;

import data.Produit;
import data.Gestion;
import connexion.Connexion;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.HashMap;


public class Test {
    public static void main(String[] args) {
        try {
            // Initialisation de la connexion
            Connexion connexion = new Connexion();
            Connection connection = connexion.getConnection();

            if (connection == null) {
                System.err.println("Connexion échouée. Vérifiez vos paramètres.");
                return;
            }

            Gestion gestion = new Gestion(connection);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Bienvenue dans le gestionnaire de produit.");
            String command;

            while (true) {
                System.out.println("\nCommandes disponibles :\nCREATE\nINSERT\nDISPLAY\nREMOVE <id>\nSTRUCT\nDROP\nUPDATE_PRODUCT\nFIND\nEXIT");
                System.out.print("Entrez une commande : ");
                command = reader.readLine().trim().toUpperCase();

                try {
                    if (command.equals("CREATE")) {
                        gestion.createTable();
                        System.out.println("Table 'product' créée avec succès.");
                    } else if (command.equals("INSERT")) {
                        System.out.println("Entrez les informations du produit :");
                        System.out.print("ID : ");
                        int id = Integer.parseInt(reader.readLine());
                        System.out.print("Numéro de Lot : ");
                        String numLot = reader.readLine();
                        System.out.print("Nom : ");
                        String nom = reader.readLine();
                        System.out.print("Description : ");
                        String description = reader.readLine();
                        System.out.print("Catégorie : ");
                        String categorie = reader.readLine();
                        System.out.print("Prix : ");
                        double prix = Double.parseDouble(reader.readLine());

                        Produit produit = new Produit(id, numLot, nom, description, categorie, prix);
                        gestion.insert(produit, "product");
                        System.out.println("Produit inséré avec succès.");
                    } else if (command.equals("DISPLAY")) {
                        gestion.displayTable("product");
                    } else if (command.startsWith("REMOVE")) {
                        String[] parts = command.split(" ");
                        if (parts.length == 2) {
                            int id = Integer.parseInt(parts[1]);
                            String query = "DELETE FROM product WHERE id = " + id;
                            gestion.execute(query);
                            System.out.println("Produit supprimé avec succès.");
                        } else {
                            System.err.println("Commande incorrecte. Utilisez REMOVE <id>.");
                        }
                    } else if (command.equals("STRUCT")) {
                        gestion.structTable("product", true);
                    } else if (command.equals("DROP")) {
                        gestion.dropTable("product");
                        System.out.println("Table 'product' supprimée avec succès.");
                    } else if (command.equals("EXIT")) {
                        System.out.println("Fermeture du gestionnaire. Au revoir !");
                        break;
                    } else if (command.equals("UPDATE_PRODUCT")) {
                        System.out.print("Entrez l'ID du produit à mettre à jour : ");
                        int id = Integer.parseInt(reader.readLine());

                        HashMap<String, Object> updates = new HashMap<>();
                        System.out.println("Entrez les nouvelles valeurs (laissez vide pour ne pas modifier) :");
                        
                        System.out.print("Nom : ");
                        String nom = reader.readLine();
                        if (!nom.isEmpty()) updates.put("nom", nom);

                        System.out.print("Description : ");
                        String description = reader.readLine();
                        if (!description.isEmpty()) updates.put("description", description);

                        System.out.print("Catégorie : ");
                        String categorie = reader.readLine();
                        if (!categorie.isEmpty()) updates.put("categorie", categorie);

                        System.out.print("Prix : ");
                        String prix = reader.readLine();
                        if (!prix.isEmpty()) updates.put("prix", Double.parseDouble(prix));

                        gestion.update("product", id, updates);
                        System.out.println("Produit mis à jour avec succès.");
                    } else if (command.equals("FIND")) {
                        System.out.print("Entrez la colonne à rechercher (nom, categorie, etc.) : ");
                        String column = reader.readLine();

                        System.out.print("Entrez la valeur à rechercher : ");
                        String value = reader.readLine();

                        gestion.find("product", column, value);
                    }  else {
                        System.err.println("Commande inconnue. Veuillez réessayer.");
                    }
                } catch (Exception e) {
                    System.err.println("Erreur : " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur critique : " + e.getMessage());
        }
    }
}
