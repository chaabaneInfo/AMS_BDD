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
    static double chiffreAffaires = 0;
    static double totalBenefice = 0;

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
        produit1.setPrixAchat(1.0);
        produit1.setQuantite(50);
        Produit produit2 = new Produit(2, "Pomme", "Fruit");
        produit2.setprixVente(3.0);
        produit2.setPrixAchat(1.5);
        produit2.setQuantite(30);

        Lot lot1 = new Lot(1, new Date());
        lot1.ajouterProduit(produit1);
        lot1.ajouterProduit(produit2);

        lots.add(lot1);

        Fournisseur fournisseur1 = new Fournisseur("Fournisseur1", "123456", "Adresse1", "email@example.com");
        Fournisseur fournisseur2 = new Fournisseur("Fournisseur2", "789012", "Adresse2", "email2@example.com");

        Contrat contrat1 = new Contrat(1, new Date(), new Date(), fournisseur1, 10, 100, lot1);
        fournisseur1.ajouterContrat(contrat1);

        fournisseurs.add(fournisseur1);
        fournisseurs.add(fournisseur2);

        System.out.println("[Initialisation] Données initialisées avec succès.");
    }

    private static void acheterLot() {
        JFrame achatFrame = new JFrame("Acheter un lot");
        achatFrame.setSize(500, 400);
    
        JPanel panel = new JPanel(new GridLayout(5, 2));
    
        JLabel fournisseurLabel = new JLabel("Fournisseur :");
        JComboBox<String> fournisseurCombo = new JComboBox<>();
        for (Fournisseur f : fournisseurs) {
            fournisseurCombo.addItem(f.NomSociete);
        }
    
        JLabel lotsLabel = new JLabel("Lots disponibles :");
        JComboBox<String> lotsCombo = new JComboBox<>();
    
        JLabel quantiteLabel = new JLabel("Quantité à acheter :");
        JSpinner quantiteSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); // Permet de sélectionner le nombre de lots
    
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
                System.out.println("[Achat] Fournisseur sélectionné : " + fournisseurSelectionne);
            }
        });
    
        lotsCombo.addActionListener(e -> {
            String lotSelectionne = (String) lotsCombo.getSelectedItem();
            if (lotSelectionne != null && !lotSelectionne.isEmpty()) {
                Lot lot = lots.stream()
                        .filter(l -> ("Lot ID: " + l.IdLot).equals(lotSelectionne))
                        .findFirst()
                        .orElse(null);
                if (lot != null) {
                    int quantite = (int) quantiteSpinner.getValue();
                    prixField.setText(String.valueOf(lot.PrixLot * quantite));
                }
            }
        });
    
        quantiteSpinner.addChangeListener(e -> {
            String lotSelectionne = (String) lotsCombo.getSelectedItem();
            if (lotSelectionne != null && !lotSelectionne.isEmpty()) {
                Lot lot = lots.stream()
                        .filter(l -> ("Lot ID: " + l.IdLot).equals(lotSelectionne))
                        .findFirst()
                        .orElse(null);
                if (lot != null) {
                    int quantite = (int) quantiteSpinner.getValue();
                    prixField.setText(String.valueOf(lot.PrixLot * quantite));
                }
            }
        });
    
        JButton confirmerButton = new JButton("Confirmer");
        confirmerButton.addActionListener(e -> {
            String lotSelectionne = (String) lotsCombo.getSelectedItem();
            int quantite = (int) quantiteSpinner.getValue();
            if (lotSelectionne != null && !lotSelectionne.isEmpty()) {
                Lot lotAchete = lots.stream()
                        .filter(l -> ("Lot ID: " + l.IdLot).equals(lotSelectionne))
                        .findFirst()
                        .orElse(null);
    
                if (lotAchete != null) {
                    for (int i = 0; i < quantite; i++) {
                        for (Produit produit : lotAchete.produits) {
                            produit.augmenterQuantite(10); // Exemple : augmente de 10 pour chaque lot acheté
                            System.out.println("[Achat] Stock mis à jour pour le produit : " + produit.nom + ", Quantité : " + produit.getQuantite());
                        }
                    }
                    JOptionPane.showMessageDialog(achatFrame, "Lot(s) acheté(s) avec succès et stock mis à jour !");
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
        panel.add(quantiteLabel);
        panel.add(quantiteSpinner);
        panel.add(prixLabel);
        panel.add(prixField);
        panel.add(confirmerButton);
    
        achatFrame.add(panel);
        achatFrame.setVisible(true);
    }
    
    private static void vendreProduit() {
        JFrame venteFrame = new JFrame("Vendre des produits");
        venteFrame.setSize(600, 400);
    
        JPanel panel = new JPanel(new BorderLayout());
    
        // Table pour afficher les produits disponibles et leurs quantités à vendre
        String[] columnNames = {"Produit", "Quantité en stock", "Quantité à vendre"};
        List<Produit> produitsDispo = new ArrayList<>();
        List<Object[]> data = new ArrayList<>();
    
        // Ajouter les produits disponibles dans la table
        for (Lot lot : lots) {
            for (Produit produit : lot.produits) {
                if (produit.getQuantite() > 0) {
                    produitsDispo.add(produit);
                    data.add(new Object[]{produit.nom, produit.getQuantite(), "0"}); // "0" comme valeur par défaut pour la quantité à vendre
                }
            }
        }
    
        Object[][] tableData = data.toArray(new Object[0][]);
        JTable table = new JTable(tableData, columnNames);
    
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        JButton confirmerButton = new JButton("Confirmer la vente");
        confirmerButton.addActionListener(e -> {
            double totalPrixVente = 0.0;
            double totalBenefice = 0.0;
            StringBuilder ticketDeCaisse = new StringBuilder("Ticket de caisse :\n\n");
    
            for (int i = 0; i < table.getRowCount(); i++) {
                String produitNom = (String) table.getValueAt(i, 0);
                int quantiteStock = (int) table.getValueAt(i, 1);
    
                // Convertir la quantité à vendre en entier
                int quantiteVente;
                try {
                    quantiteVente = Integer.parseInt((String) table.getValueAt(i, 2)); // Conversion sécurisée
                } catch (NumberFormatException ex) {
                    quantiteVente = 0; // Valeur par défaut si la conversion échoue
                }
    
                if (quantiteVente > 0 && quantiteVente <= quantiteStock) {
                    Produit produit = produitsDispo.stream()
                            .filter(p -> p.nom.equals(produitNom))
                            .findFirst()
                            .orElse(null);
    
                    if (produit != null) {
                        produit.diminuerQuantite(quantiteVente);
                        double prixVente = produit.prixVente * quantiteVente;
                        double prixAchat = produit.getPrixAchat() * quantiteVente;
                        double benefice = prixVente - prixAchat;
    
                        totalPrixVente += prixVente;
                        totalBenefice += benefice;
    
                        ticketDeCaisse.append("Produit : ").append(produit.nom)
                                .append("\nQuantité vendue : ").append(quantiteVente)
                                .append("\nPrix de vente : ").append(prixVente)
                                .append("\nPrix d'achat : ").append(prixAchat)
                                .append("\nBénéfice : ").append(benefice).append("\n\n");
                    }
                }
            }
    
            ticketDeCaisse.append("Total des ventes : ").append(totalPrixVente).append("\n");
            ticketDeCaisse.append("Total des bénéfices : ").append(totalBenefice).append("\n");
    
            // Mise à jour des totaux globaux
            chiffreAffaires += totalPrixVente;
            totalBenefice += totalBenefice;
    
            // Sauvegarde dans le fichier ticket_de_caisse.txt
            try (FileWriter writer = new FileWriter("ticket_de_caisse.txt", true)) {
                writer.write(ticketDeCaisse.toString());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
    
            JOptionPane.showMessageDialog(venteFrame, "Vente confirmée ! Ticket généré.\n\n" + ticketDeCaisse);
            venteFrame.dispose();
        });
    
        panel.add(confirmerButton, BorderLayout.SOUTH);
        venteFrame.add(panel);
        venteFrame.setVisible(true);
    }
    
    

    private static void updateProductList(JComboBox<String> produitCombo) {
        produitCombo.removeAllItems();
        System.out.println("[UpdateProductList] Actualisation de la liste des produits disponibles...");
        for (Lot lot : lots) {
            for (Produit produit : lot.produits) {
                if (produit.getQuantite() > 0) {
                    produitCombo.addItem(produit.nom);
                    System.out.println("[UpdateProductList] Produit ajouté : " + produit.nom + ", Quantité : " + produit.getQuantite());
                }
            }
        }
    }
}
