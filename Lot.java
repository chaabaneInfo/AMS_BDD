public class Lot {
    private int IdLot;
    private String description;
    private Produit[] produits;
    public int nbProduits;
    public Date DateExpirationLot;

    public Lot(int id) {
        this.IdLot = id;
        this.produits = new Produit[100];
        this.nbProduits = 0;$
        this.DateExpiration=DateExpiration;

    }

    public int getId() {
        return id;
    }

    public void ajouterProduit(Produit produit) {
        this.produits[this.nbProduits] = produit;
        this.nbProduits++;
    }

    public void afficher() {
        System.out.println(this.description);
        for (int i = 0; i < this.nbProduits; i++) {
            this.produits[i].afficher();
        }
    }

    
}