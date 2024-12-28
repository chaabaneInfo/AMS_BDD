import java.util.Date;

public class Achat {

    public Lot lot;
    public int quantiteAchat;
    public double PrixUnitaireAchat;
    public Date DateAchat;

    public Achat(Lot lot, int quantiteAchat, double PrixUnitaireAchat, Date DateAchat) {
        if (lot == null) {
            throw new IllegalArgumentException("Le lot ne peut pas être null.");
        }
        if (quantiteAchat <= 0) {
            throw new IllegalArgumentException("La quantité achetée doit être supérieure à zéro.");
        }
        if (PrixUnitaireAchat <= 0) {
            throw new IllegalArgumentException("Le prix unitaire doit être supérieur à zéro.");
        }

        this.lot = lot;
        this.quantiteAchat = quantiteAchat;
        this.PrixUnitaireAchat = PrixUnitaireAchat;
        this.DateAchat = DateAchat;

        System.out.println("[Achat] Création d'un achat pour le lot ID: " + lot.getId());
        System.out.println("[Achat] Quantité totale achetée : " + quantiteAchat);
        System.out.println("[Achat] Prix unitaire calculé : " + PrixUnitaireAchat);

        // Mise à jour des stocks
        mettreAJourStock();
    }

    // Méthode pour mettre à jour le stock des produits du lot
    private void mettreAJourStock() {
        System.out.println("[Achat] Mise à jour des stocks pour le lot ID: " + lot.getId());
        for (Produit produit : lot.produits) {
            produit.augmenterQuantite(quantiteAchat / lot.nbProduits);
            System.out.println("[Achat] Produit: " + produit.nom + ", Quantité après mise à jour: " + produit.getQuantite());
        }
    }

    public void afficher() {
        System.out.println("Lot : " + this.lot.getId());
        System.out.println("Quantité achetée : " + this.quantiteAchat);
        System.out.println("Prix Unitaire : " + this.PrixUnitaireAchat);
        System.out.println("Date Achat : " + this.DateAchat);
    }
}
