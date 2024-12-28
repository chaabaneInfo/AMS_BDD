public class Produit {
    public int id;
    public String nom;
    public String description;
    public String categorie;
    public double prixVente;
    public Lot lot;
    public int quantite; 


    public Produit(int id, String nom, String categorie) {
        this.id = id;
        this.nom = nom;
        this.description = "id"+id+"categorie"+categorie+"prix"+prixVente;
        this.categorie = categorie;
        this.prixVente = prixVente;
    }

    public void setprixVente(double prixVente) {
        this.prixVente = prixVente;
    }
    public double getPrix() {
        return this.prixVente;
    }
    public int getId() {
        return this.id;
    }

    public int getQuantite() {
        return this.quantite;
    }
    
    public void afficher() {
        System.out.println(this.description);
    }
    public void setLot(Lot lot) {
        this.lot = lot;
    }
    public void setQuantite(int quantite) {
        this.quantite = quantite;
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
}
