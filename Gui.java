import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Gui {

    static List<Lot> lots = new ArrayList<>();
    static List<Vente> ventes = new ArrayList<>();
    static List<Fournisseur> fournisseurs = new ArrayList<>();

    public static void main(String[] args) {
        // Initialisation des données
        initialiserDonnees();

        // Création de la fenêtre principale
        JFrame frame = new JFrame("Gestion de Stock");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Création des onglets
        JTabbedPane tabbedPane = new JTabbedPane();

        // Onglet "Acheter des lots"
        JPanel achatPanel = new JPanel();
        achatPanel.setLayout(new BorderLayout());
        JButton acheterButton = new JButton("Acheter un lot");
        acheterButton.addActionListener(e -> acheterLot());
        achatPanel.add(acheterButton, BorderLayout.CENTER);
        tabbedPane.addTab("Acheter des lots", achatPanel);

        // Onglet "Vendre des produits"
        JPanel ventePanel = new JPanel();
        ventePanel.setLayout(new BorderLayout());
        JButton vendreButton = new JButton("Vendre un produit");
        vendreButton.addActionListener(e -> vendreProduit());
        ventePanel.add(vendreButton, BorderLayout.CENTER);
        tabbedPane.addTab("Vendre des produits", ventePanel);

        // Onglet "Liste des fournisseurs"
        JPanel fournisseurPanel = new JPanel(new BorderLayout());
        JTextArea fournisseurTextArea = new JTextArea();
        fournisseurTextArea.setEditable(false);
        for (Fournisseur f : fournisseurs) {
            fournisseurTextArea.append(f.NomSociete + "\n");
        }
        JScrollPane fournisseurScrollPane = new JScrollPane(fournisseurTextArea);
        fournisseurPanel.add(fournisseurScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Liste des fournisseurs", fournisseurPanel);

        // Ajouter les onglets au cadre principal
        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private static void initialiserDonnees() {
        Produit produit1 = new Produit(1, "Banane", "Fruit");
        produit1.setprixVente(2.0);
        produit1.setQuantite(50);
        Produit produit2 = new Produit(2, "Pomme", "Fruit");
        produit2.setprixVente(3.0);
        produit2.setQuantite(30);

        Lot lot1 = new Lot(1, new Date());
        lot1.ajouterProduit(produit1);

        lots.add(lot1);

        Fournisseur fournisseur1 = new Fournisseur("Fournisseur1", "123456", "Adresse1", "email@example.com");
        Fournisseur fournisseur2 = new Fournisseur("Fournisseur2", "789012", "Adresse2", "email2@example.com");

        Contrat contrat1 = new Contrat(1, new Date(), new Date(), fournisseur1, 10, 100, lot1);
        fournisseur1.ajouterContrat(contrat1);

        fournisseurs.add(fournisseur1);
        fournisseurs.add(fournisseur2);
    }

    private static void acheterLot() {
        JFrame achatFrame = new JFrame("Acheter un lot");
        achatFrame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(4, 2));

        JLabel fournisseurLabel = new JLabel("Fournisseur :");
        JComboBox<String> fournisseurCombo = new JComboBox<>();
        for (Fournisseur f : fournisseurs) {
            fournisseurCombo.addItem(f.NomSociete);
        }

        JLabel lotsLabel = new JLabel("Lots disponibles :");
        JComboBox<String> lotsCombo = new JComboBox<>();

        JLabel prixLabel = new JLabel("Prix total :");
        JTextField prixField = new JTextField();
        prixField.setEditable(false);

        fournisseurCombo.addActionListener(e -> {
            lotsCombo.removeAllItems();
            prixField.setText("");
            String fournisseurSelectionne = (String) fournisseurCombo.getSelectedItem();
            Fournisseur fournisseur = fournisseurs.stream()
                    .filter(f -> f.NomSociete.equals(fournisseurSelectionne))
                    .findFirst()
                    .orElse(null);

            if (fournisseur != null) {
                for (Contrat contrat : fournisseur.contrats) {
                    if (contrat.LotProduit != null) {
                        lotsCombo.addItem("Lot ID: " + contrat.LotProduit.IdLot);
                    }
                }
            }
        });

        lotsCombo.addActionListener(e -> {
            String lotSelectionne = (String) lotsCombo.getSelectedItem();
            if (lotSelectionne != null && !lotSelectionne.isEmpty()) {
                String fournisseurSelectionne = (String) fournisseurCombo.getSelectedItem();
                Fournisseur fournisseur = fournisseurs.stream()
                        .filter(f -> f.NomSociete.equals(fournisseurSelectionne))
                        .findFirst()
                        .orElse(null);

                if (fournisseur != null) {
                    for (Contrat contrat : fournisseur.contrats) {
                        if (contrat.LotProduit != null && ("Lot ID: " + contrat.LotProduit.IdLot).equals(lotSelectionne)) {
                            prixField.setText(String.valueOf(contrat.PrixFixe));
                        }
                    }
                }
            }
        });

        JButton confirmerButton = new JButton("Confirmer");
        confirmerButton.addActionListener(e -> {
            String lotSelectionne = (String) lotsCombo.getSelectedItem();
            if (lotSelectionne != null && !lotSelectionne.isEmpty()) {
                Lot lotAchete = null;
                for (Lot lot : lots) {
                    if (("Lot ID: " + lot.IdLot).equals(lotSelectionne)) {
                        lotAchete = lot;
                        break;
                    }
                }

                if (lotAchete != null) {
                    for (Produit produit : lotAchete.produits) {
                        produit.augmenterQuantite(10); // Augmente la quantité de chaque produit de 10 (exemple)
                    }
                    JOptionPane.showMessageDialog(achatFrame, "Lot acheté avec succès et stock mis à jour !");
                }
            } else {
                JOptionPane.showMessageDialog(achatFrame, "Veuillez sélectionner un lot.");
            }
            achatFrame.dispose();
        });

        panel.add(fournisseurLabel);
        panel.add(fournisseurCombo);
        panel.add(lotsLabel);
        panel.add(lotsCombo);
        panel.add(prixLabel);
        panel.add(prixField);
        panel.add(confirmerButton);

        achatFrame.add(panel);
        achatFrame.setVisible(true);
    }

    private static void vendreProduit() {
        JFrame venteFrame = new JFrame("Vendre un produit");
        venteFrame.setSize(400, 300);
    
        JPanel panel = new JPanel(new GridLayout(2, 2));
    
        JLabel produitLabel = new JLabel("Produit :");
        JComboBox<String> produitCombo = new JComboBox<>();
        updateProductList(produitCombo);
    
        JButton confirmerButton = new JButton("Vendre");
        confirmerButton.addActionListener(e -> {
            String produitNom = (String) produitCombo.getSelectedItem();
            Produit produitAVendre = null;
    
            for (Lot lot : lots) {
                for (Produit p : lot.produits) {
                    if (p.nom.equals(produitNom)) {
                        produitAVendre = p;
                        break;
                    }
                }
            }
    
            if (produitAVendre != null && produitAVendre.getQuantite() > 0) {
                produitAVendre.diminuerQuantite(1);
                System.out.println("[Vente] Produit vendu : " + produitAVendre.nom);
                System.out.println("[Vente] Quantité restante : " + produitAVendre.getQuantite());
    
                try (FileWriter writer = new FileWriter("ticket_de_caisse.txt", true)) {
                    writer.write("Vente Produit: " + produitAVendre.nom + "\n");
                    writer.write("Prix: " + produitAVendre.prixVente + "\n");
                    writer.write("Date: " + new Date() + "\n\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
    
                JOptionPane.showMessageDialog(venteFrame, "Produit vendu avec succès et ticket généré !");
                updateProductList(produitCombo);
            } else {
                JOptionPane.showMessageDialog(venteFrame, "Produit introuvable ou en rupture de stock !");
            }
        });
    
        panel.add(produitLabel);
        panel.add(produitCombo);
        panel.add(confirmerButton);
    
        venteFrame.add(panel);
        venteFrame.setVisible(true);
    }

    private static void updateProductList(JComboBox<String> produitCombo) {
        produitCombo.removeAllItems(); // Efface la liste actuelle
        for (Lot lot : lots) {
            for (Produit p : lot.produits) {
                if (p.getQuantite() > 0) { // N'ajouter que les produits en stock
                    produitCombo.addItem(p.nom);
                }
            }
        }
    }
}
