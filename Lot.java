import java.util.Date;
import java.util.ArrayList;
import java.util.List;
public class Lot {

    public int IdLot;
    public String description;
    public List <Produit> produits;
    public int nbProduits;
    public Date DateExpirationLot;
    public double PrixLot;

    public Lot(int id, Date DateExpiration) {
        this.IdLot = id;
        this.produits = new ArrayList<Produit>();
        this.nbProduits = 0;
        this.DateExpirationLot=DateExpiration;
        this.PrixLot=0;

    }

    public int getId() {
        return IdLot;
    }

    public void ajouterProduit(Produit produit) {
        this.produits.add(produit);
        this.nbProduits++;
        this.PrixLot+=produit.getPrix();
        produit.setLot(this);
    }

    public void afficher() {
        System.out.println(this.description);
        for (int i = 0; i < this.nbProduits; i++) {
            this.produits.get(i).afficher();
        }
    }
    public void setPrixLot(double prix){
        this.PrixLot=prix;
    }
    public void RemoveProduit(Produit produit){
        this.produits.remove(produit);
        this.nbProduits--;
    }



    
}