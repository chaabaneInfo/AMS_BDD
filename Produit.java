public class Produit {
    public int id;
    public String nom;
    public String description;
    public String categorie;
    public double prixVente;
    public double prixAchat;
    public int quantite;
    public Lot lot; // Référence au lot

    public Produit(int id, String nom, String categorie) {
        this.id = id;
        this.nom = nom;
        this.description = "id" + id + " categorie " + categorie + " prix " + prixVente;
        this.categorie = categorie;
        this.prixVente = 0;
        this.prixAchat = 0;
        this.quantite = 0;
        this.lot = null;
    }

    public void setprixVente(double prixVente) {
        this.prixVente = prixVente;
    }

    public void setPrixAchat(double prixAchat) {
        this.prixAchat = prixAchat;
    }

    public double getPrixAchat() {
        return this.prixAchat;
    }

    public double getPrix() {
        return this.prixVente; // Retourne le prix de vente par défaut
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public Lot getLot() {
        return this.lot;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getQuantite() {
        return this.quantite;
    }

    public void augmenterQuantite(int valeur) {
        this.quantite += valeur;
    }

    public void diminuerQuantite(int valeur) {
        if (this.quantite >= valeur) {
            this.quantite -= valeur;
        } else {
            System.out.println("Quantité insuffisante pour effectuer cette opération.");
        }
    }

    public void afficher() {
        System.out.println(this.description + ", Quantité: " + this.quantite + ", Prix: " + this.prixVente + ", Prix Achat: " + this.prixAchat);
    }
}
