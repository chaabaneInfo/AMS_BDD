package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import data.Fournisseur;
import connexion.Connexion;
import java.sql.Date;
import data.Contrat;
import data.Achat;
import data.Produit;
import java.util.Map;
import java.util.HashMap;
import data.Vente;
import java.io.FileWriter;
import java.io.IOException;

public class SuperetteApp extends JFrame {
    private Connection connection;
    private JTable fournisseurTable;
    private DefaultTableModel fournisseurTableModel;
    private JTable contratTable;
    private DefaultTableModel contratTableModel;
    private JTable achatTable;
    private DefaultTableModel achatTableModel;
    private JTable stockTable;
    private DefaultTableModel stockTableModel;
    private JTable salesTable;
    private DefaultTableModel salesTableModel;
    private JTable dashboardTable;
    private DefaultTableModel dashboardTableModel;



    public SuperetteApp(Connection connection) {
        this.connection = connection;
        if (this.connection == null) {
            JOptionPane.showMessageDialog(this, "La connexion à la base de données a échoué.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        setTitle("Gestion de la Supérette");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Fournisseurs", createSupplierPanel());
        tabbedPane.add("Contrats", createContractPanel());
        tabbedPane.add("Achats", createPurchasePanel());
        tabbedPane.add("Ventes", createSalesPanel());

        add(tabbedPane);
    }

    private JPanel createSupplierPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Gestion des Fournisseurs");
        panel.add(label, BorderLayout.NORTH);
        
        // Table pour afficher les fournisseurs
        fournisseurTableModel = new DefaultTableModel(new String[]{"ID", "Nom", "SIRET", "Adresse", "Email", "Téléphone"}, 0);
        fournisseurTable = new JTable(fournisseurTableModel);
        JScrollPane scrollPane = new JScrollPane(fournisseurTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Boutons pour gérer les fournisseurs
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        JButton updateButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Charger les fournisseurs
        loadFournisseurs();

        // Actions des boutons
        addButton.addActionListener(e -> addFournisseur());
        updateButton.addActionListener(e -> updateFournisseur());
        deleteButton.addActionListener(e -> deleteFournisseur());
        
        return panel;
    }
    
    private void loadFournisseurs() {
        try {
            List<Fournisseur> fournisseurs = Fournisseur.getAllFournisseurs(connection);
            fournisseurTableModel.setRowCount(0);
            for (Fournisseur f : fournisseurs) {
                fournisseurTableModel.addRow(new Object[]{f.getId(), f.getNom(), f.getSiret(), f.getAdresse(), f.getEmail(), f.getTelephone()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des fournisseurs : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addFournisseur() {
        String nom = JOptionPane.showInputDialog(this, "Nom du fournisseur :");
        String siret = JOptionPane.showInputDialog(this, "SIRET :");
        String adresse = JOptionPane.showInputDialog(this, "Adresse :");
        String email = JOptionPane.showInputDialog(this, "Email :");
        String telephone = JOptionPane.showInputDialog(this, "Téléphone :");
        
        if (nom != null && siret != null && adresse != null && email != null && telephone != null) {
            try {
                Fournisseur fournisseur = new Fournisseur(nom, siret, adresse, email, telephone);
                Fournisseur.addFournisseur(connection, fournisseur);
                loadFournisseurs();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateFournisseur() {
        int selectedRow = fournisseurTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fournisseur à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Récupération des anciennes valeurs
        int id = (int) fournisseurTableModel.getValueAt(selectedRow, 0);
        String oldNom = (String) fournisseurTableModel.getValueAt(selectedRow, 1);
        String oldSiret = (String) fournisseurTableModel.getValueAt(selectedRow, 2);
        String oldAdresse = (String) fournisseurTableModel.getValueAt(selectedRow, 3);
        String oldEmail = (String) fournisseurTableModel.getValueAt(selectedRow, 4);
        String oldTelephone = (String) fournisseurTableModel.getValueAt(selectedRow, 5);

        // Demander les nouvelles valeurs (pré-remplies)
        String newNom = JOptionPane.showInputDialog(this, "Nom :", oldNom);
        String newSiret = JOptionPane.showInputDialog(this, "SIRET :", oldSiret);
        String newAdresse = JOptionPane.showInputDialog(this, "Adresse :", oldAdresse);
        String newEmail = JOptionPane.showInputDialog(this, "Email :", oldEmail);
        String newTelephone = JOptionPane.showInputDialog(this, "Téléphone :", oldTelephone);

        if (newNom != null && newSiret != null && newAdresse != null && newEmail != null && newTelephone != null) {
            try {
                Fournisseur fournisseur = new Fournisseur(id, newNom, newSiret, newAdresse, newEmail, newTelephone);
                Fournisseur.updateFournisseur(connection, fournisseur);
                loadFournisseurs();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void deleteFournisseur() {
        int selectedRow = fournisseurTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fournisseur à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) fournisseurTableModel.getValueAt(selectedRow, 0);
        try {
            Fournisseur.deleteFournisseur(connection, id);
            loadFournisseurs();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    private JPanel createContractPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Gestion des Contrats");
        panel.add(label, BorderLayout.NORTH);
        
        // Table pour afficher les contrats
        contratTableModel = new DefaultTableModel(new String[]{"ID", "Produit ID", "Fournisseur ID", "Prix", "Date Début", "Date Fin"}, 0);
        contratTable = new JTable(contratTableModel);
        JScrollPane scrollPane = new JScrollPane(contratTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Boutons pour gérer les contrats
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Ajouter");
        JButton updateButton = new JButton("Modifier Prix");
        JButton deleteButton = new JButton("Supprimer");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Charger les contrats
        loadContrats();

        // Actions des boutons
        addButton.addActionListener(e -> addContrat());
        updateButton.addActionListener(e -> updateContrat());
        deleteButton.addActionListener(e -> deleteContrat());
        
        return panel;
    }
    
    private void loadContrats() {
        try {
            List<Contrat> contrats = Contrat.getAllContrats(connection);
            contratTableModel.setRowCount(0);
            for (Contrat c : contrats) {
                contratTableModel.addRow(new Object[]{c.getId(), c.getProduitId(), c.getFournisseurId(), c.getPrix(), c.getDateDebut(), c.getDateFin()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des contrats : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addContrat() {
        String produitId = JOptionPane.showInputDialog(this, "ID du produit :");
        String fournisseurId = JOptionPane.showInputDialog(this, "ID du fournisseur :");
        String prix = JOptionPane.showInputDialog(this, "Prix :");
        String dateDebutStr = JOptionPane.showInputDialog(this, "Date début (YYYY-MM-DD) :");
        String dateFinStr = JOptionPane.showInputDialog(this, "Date fin (YYYY-MM-DD ou vide si indéfini) :");

        if (produitId != null && fournisseurId != null && prix != null && dateDebutStr != null) {
            try {
                int produitIdInt = Integer.parseInt(produitId);
                int fournisseurIdInt = Integer.parseInt(fournisseurId);
                double prixDouble = Double.parseDouble(prix);
                Date dateDebut = Date.valueOf(dateDebutStr);
                Date dateFin = (dateFinStr == null || dateFinStr.isEmpty()) ? null : Date.valueOf(dateFinStr);

                Contrat contrat = new Contrat(produitIdInt, fournisseurIdInt, prixDouble, dateDebut, dateFin);
                Contrat.addContrat(connection, contrat);
                loadContrats();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Format de date invalide. Utilisez YYYY-MM-DD.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateContrat() {
        int selectedRow = contratTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contrat à modifier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) contratTableModel.getValueAt(selectedRow, 0);
        String prixStr = JOptionPane.showInputDialog(this, "Nouveau Prix (laissez vide pour ne pas modifier) :");
        String dateFinStr = JOptionPane.showInputDialog(this, "Nouvelle Date de Fin (YYYY-MM-DD, laissez vide pour ne pas modifier) :");

        Double newPrix = (prixStr == null || prixStr.isEmpty()) ? null : Double.parseDouble(prixStr);
        Date newDateFin = (dateFinStr == null || dateFinStr.isEmpty()) ? null : Date.valueOf(dateFinStr);

        try {
            Contrat.updateContrat(connection, id, newPrix, newDateFin);
            loadContrats();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteContrat() {
        int selectedRow = contratTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contrat à supprimer.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) contratTableModel.getValueAt(selectedRow, 0);
        try {
            Contrat.deleteContrat(connection, id);
            loadContrats();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createPurchasePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Gestion des Achats");
        panel.add(label, BorderLayout.NORTH);
        
        // Table pour afficher les achats
        achatTableModel = new DefaultTableModel(new String[]{"ID", "FournisseurID", "Fournisseur", "ProduitID", "Produit", "Quantité", "Prix Unitaire", "Prix Total", "Date Achat", "Date Péremption"}, 0);
        achatTable = new JTable(achatTableModel);
        JScrollPane achatScrollPane = new JScrollPane(achatTable);
        
        // Table pour afficher les stocks
        stockTableModel = new DefaultTableModel(new String[]{"Fournisseur", "Produit", "Stock Disponible"}, 0);
        stockTable = new JTable(stockTableModel);
        JScrollPane stockScrollPane = new JScrollPane(stockTable);
        
        // Panel pour afficher les achats et les stocks ensemble
        JPanel tablesPanel = new JPanel(new GridLayout(2, 1));
        tablesPanel.add(achatScrollPane);
        tablesPanel.add(stockScrollPane);
        panel.add(tablesPanel, BorderLayout.CENTER);
        
        // Boutons pour gérer les achats
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Effectuer un Achat");
        
        buttonPanel.add(addButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Charger les achats et stocks
        loadAchats();
        loadStocks();

        // Actions des boutons
        addButton.addActionListener(e -> effectuerAchat());

        return panel;
    }
    
    private void loadAchats() {
        try {
            List<Achat> achats = Achat.getAllAchats(connection);
            achatTableModel.setRowCount(0);
            for (Achat a : achats) {
                String fournisseurNom = Fournisseur.getNomById(connection, a.getFournisseurId());
                String produitNom = Produit.getNomById(connection, a.getProduitId());
                achatTableModel.addRow(new Object[]{a.getId(), a.getFournisseurId(), fournisseurNom, a.getProduitId(), produitNom, a.getQuantite(), a.getPrixUnitaire(), a.getPrixTotal(), a.getDateAchat(), a.getDatePeremption()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des achats : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadStocks() {
        try {
            stockTableModel.setRowCount(0); // Vider le tableau avant de recharger les stocks

            // Utiliser une HashMap pour cumuler les stocks par (Fournisseur, Produit)
            Map<String, Integer> stockMap = new HashMap<>();

            List<Achat> achats = Achat.getAllAchats(connection);

            for (Achat a : achats) {
                String fournisseurNom = Fournisseur.getNomById(connection, a.getFournisseurId());
                String produitNom = Produit.getNomById(connection, a.getProduitId());
                String key = fournisseurNom + " - " + produitNom; // Clé unique basée sur fournisseur et produit

                // Ajouter la quantité au stock existant ou initialiser
                stockMap.put(key, stockMap.getOrDefault(key, 0) + a.getQuantite());
            }

            // Ajouter les stocks consolidés au tableau
            for (Map.Entry<String, Integer> entry : stockMap.entrySet()) {
                String[] parts = entry.getKey().split(" - ");
                String fournisseurNom = parts[0];
                String produitNom = parts[1];
                int stockDisponible = entry.getValue();

                stockTableModel.addRow(new Object[]{fournisseurNom, produitNom, stockDisponible});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement du stock : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void effectuerAchat() {
        try {
            //Sélection du fournisseur
            List<Fournisseur> fournisseurs = Fournisseur.getAllFournisseurs(connection);
            String[] fournisseurNames = fournisseurs.stream().map(Fournisseur::getNom).toArray(String[]::new);

            String selectedFournisseur = (String) JOptionPane.showInputDialog(this, 
                    "Choisissez un fournisseur:", "Sélection Fournisseur",
                    JOptionPane.QUESTION_MESSAGE, null, fournisseurNames, fournisseurNames[0]);

            if (selectedFournisseur == null) return;

            Fournisseur fournisseur = fournisseurs.stream()
                    .filter(f -> f.getNom().equals(selectedFournisseur))
                    .findFirst()
                    .orElse(null);

            if (fournisseur == null) return;

            //Sélection du produit à acheter
            List<Contrat> contrats = Contrat.getContratsByFournisseur(connection, fournisseur.getId());

            String[] produitNames = contrats.stream()
                    .map(c -> {
                        try {
                            return "Produit: " + Produit.getNomById(connection, c.getProduitId()) + " | Prix: " + c.getPrix();
                        } catch (SQLException e) {
                            return "Produit inconnu | Prix: " + c.getPrix();
                        }
                    })
                    .toArray(String[]::new);

            String selectedProduit = (String) JOptionPane.showInputDialog(this, 
                    "Choisissez un produit à acheter:", "Sélection Produit",
                    JOptionPane.QUESTION_MESSAGE, null, produitNames, produitNames[0]);

            if (selectedProduit == null) return;

            //Extraire le nom du produit sélectionné
            String selectedProduitNom = selectedProduit.split(" \\| Prix: ")[0].replace("Produit: ", "");

            //Trouver le contrat associé à ce produit
            Contrat contrat = contrats.stream()
                    .filter(c -> {
                        try {
                            return Produit.getNomById(connection, c.getProduitId()).equals(selectedProduitNom);
                        } catch (SQLException e) {
                            return false;
                        }
                    })
                    .findFirst()
                    .orElse(null);

            if (contrat == null) return;

            //Saisir la quantité
            String quantiteStr = JOptionPane.showInputDialog(this, "Quantité à acheter:");
            if (quantiteStr == null || quantiteStr.isEmpty()) return;
            int quantite = Integer.parseInt(quantiteStr);

            //Saisir la date de péremption
            String datePeremptionStr = JOptionPane.showInputDialog(this, "Date de péremption (YYYY-MM-DD, laissez vide si non applicable):");
            Date datePeremption = (datePeremptionStr == null || datePeremptionStr.isEmpty()) ? null : Date.valueOf(datePeremptionStr);

            //Enregistrer l'achat
            Achat achat = new Achat(contrat.getProduitId(), fournisseur.getId(), quantite, contrat.getPrix(), new Date(System.currentTimeMillis()), datePeremption);
            
            try {
                Achat.addAchat(connection, achat);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'achat : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Rafraîchir l'affichage des achats et stocks
            loadAchats();
            loadStocks();

            JOptionPane.showMessageDialog(this, "Achat effectué avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'achat : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    
    private JPanel createSalesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Gestion des Ventes");
        panel.add(label, BorderLayout.NORTH);

        //Table pour afficher les ventes
        salesTableModel = new DefaultTableModel(new String[]{"ID", "Produit", "Lot", "Quantité", "Prix Vente", "Date Vente"}, 0);
        salesTable = new JTable(salesTableModel);
        JScrollPane scrollPane = new JScrollPane(salesTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        //Tableau de bord
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        JLabel dashboardLabel = new JLabel("Tableau de Bord");
        dashboardPanel.add(dashboardLabel, BorderLayout.NORTH);

        dashboardTableModel = new DefaultTableModel(new String[]{"Type", "Valeur"}, 0);
        dashboardTable = new JTable(dashboardTableModel);
        JScrollPane dashboardScrollPane = new JScrollPane(dashboardTable);
        dashboardPanel.add(dashboardScrollPane, BorderLayout.CENTER);

        //Boutons d'actions
        JPanel buttonPanel = new JPanel();
        JButton sellButton = new JButton("Effectuer une Vente");
        JButton expiredButton = new JButton("Enregistrer Produit Expiré");
        buttonPanel.add(sellButton);
        buttonPanel.add(expiredButton);

        //Ajout d'un conteneur pour le tableau de bord + boutons
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(dashboardPanel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(containerPanel, BorderLayout.SOUTH);

        // Actions des boutons
        sellButton.addActionListener(e -> effectuerVente());
        expiredButton.addActionListener(e -> enregistrerProduitExpire());

        loadSales();
        loadDashboard();
        dashboardTable.repaint();
        dashboardTable.revalidate();


        return panel;
    }

    
    private void loadSales() {
        try {
            List<Vente> ventes = Vente.getAllVentes(connection);
            salesTableModel.setRowCount(0);
            
            for (Vente v : ventes) {
                String produitNom = Produit.getNomById(connection, v.getProduitId());
                salesTableModel.addRow(new Object[]{
                    v.getId(),
                    produitNom,
                    v.getLot(),
                    v.getQuantite(),
                    v.getPrixVente(),
                    v.getDateVente()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des ventes : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void effectuerVente() {
        try {
            // Sélection du produit
            List<Produit> produits = Produit.getAllProduits(connection);
            String selectedProduit = (String) JOptionPane.showInputDialog(this, "Choisissez un produit:", "Sélection Produit",
                    JOptionPane.QUESTION_MESSAGE, null, produits.stream().map(Produit::getNom).toArray(String[]::new), null);
            Produit produit = produits.stream().filter(p -> p.getNom().equals(selectedProduit)).findFirst().orElse(null);
            if (produit == null) return;

            // Sélection de la quantité
            int quantite = Integer.parseInt(JOptionPane.showInputDialog(this, "Quantité à vendre:"));
            double prixVente = produit.getPrix();

            // Récupérer les lots disponibles pour ce produit
            List<Achat> achats = Achat.getAchatsByProduit(connection, produit.getId());
            if (achats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Stock insuffisant pour ce produit.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Achat achat = achats.get(0); // Utiliser le premier lot disponible 

            // Enregistrer la vente avec le lot correspondant
            Vente vente = new Vente(produit.getId(), String.valueOf(achat.getId()), quantite, prixVente, new Date(System.currentTimeMillis()));
            Vente.addVente(connection, vente);

            // Mettre à jour le stock
            Vente.mettreAJourStock(connection, produit.getId(), String.valueOf(achat.getId()), quantite);
            genererTicketCaisse(vente);
            
            loadStocks();
            loadSales();
            loadDashboard();

            JOptionPane.showMessageDialog(this, "Vente effectuée !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void genererTicketCaisse(Vente vente) {
        try {
            // Récupérer le nom du produit
            String produitNom = Produit.getNomById(connection, vente.getProduitId());

            // Construire le ticket
            String ticket = "------------------------------------\n" +
                            "      SUPÉRETTE - TICKET DE CAISSE  \n" +
                            "------------------------------------\n" +
                            "Produit         | Lot  | Qté | Prix U | Total  \n" +
                            "------------------------------------------------\n" +
                            produitNom + " | " + vente.getLot() + " |  " +
                            vente.getQuantite() + "  |  " + vente.getPrixVente() + "€  |  " +
                            (vente.getQuantite() * vente.getPrixVente()) + "€\n" +
                            "------------------------------------------------\n" +
                            "TOTAL : " + (vente.getQuantite() * vente.getPrixVente()) + "€\n" +
                            "Date : " + vente.getDateVente() + "\n" +
                            "MERCI DE VOTRE VISITE !";

            // Afficher le ticket dans une boîte de dialogue
            JOptionPane.showMessageDialog(this, ticket, "Ticket de Caisse", JOptionPane.INFORMATION_MESSAGE);

            // Option pour enregistrer en fichier texte
            try (FileWriter writer = new FileWriter("ticket_caisse.txt")) {
                writer.write(ticket);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde du ticket.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des données produit : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    
    private void enregistrerProduitExpire() {
        try {
            List<Produit> produits = Produit.getAllProduits(connection);
            String selectedProduit = (String) JOptionPane.showInputDialog(this, "Sélectionnez un produit expiré:", "Produit Expiré",
                    JOptionPane.QUESTION_MESSAGE, null, produits.stream().map(Produit::getNom).toArray(String[]::new), null);
            Produit produit = produits.stream().filter(p -> p.getNom().equals(selectedProduit)).findFirst().orElse(null);
            if (produit == null) return;

            int quantite = Integer.parseInt(JOptionPane.showInputDialog(this, "Quantité expirée:"));

            // Création d'un lot spécial "EXP" pour identifier les produits expirés
            Vente vente = new Vente(produit.getId(), "EXP-" + System.currentTimeMillis(), quantite, 0.0, new Date(System.currentTimeMillis()));
            Vente.enregistrerProduitExpire(connection, vente);
            loadStocks();
            loadSales();
            loadDashboard();
            JOptionPane.showMessageDialog(this, "Produit expiré enregistré à 0€", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void loadDashboard() {
        try {
            // 🔹 Récupérer les statistiques des ventes
            double ventesJour = Vente.getVentesJournalieres(connection);
            double ventesMois = Vente.getVentesMensuelles(connection);
            List<Vente> topVentes = Vente.getTopVentes(connection);


            //Réinitialiser le tableau du Dashboard
            dashboardTableModel.setRowCount(0);

            //Ajouter les ventes journalières et mensuelles
            dashboardTableModel.addRow(new Object[]{"Ventes du jour", ventesJour});
            dashboardTableModel.addRow(new Object[]{"Ventes du mois", ventesMois});

            //Afficher le TOP 10 des produits les plus vendus
            for (Vente v : topVentes) {
                String produitNom = Produit.getNomById(connection, v.getProduitId());
                dashboardTableModel.addRow(new Object[]{"Top Vente", produitNom + " - " + v.getQuantite()});
            }

            //Rafraîchir l'affichage
            dashboardTable.repaint();
            dashboardTable.revalidate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement tableau de bord: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = Connexion.getConnection(); // Récupérer une connexion valide
                SuperetteApp app = new SuperetteApp(connection);
                app.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

}
