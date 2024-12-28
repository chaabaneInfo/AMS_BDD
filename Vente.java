import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Vente{
    public int IdVente;
    public Date DateVente;
    public Produit produit;
    public Lot lot;
    public double PrixVente;
    String description;

    public Vente(int IdVente, Date DateVente, Produit produit) {
        this.IdVente = IdVente;
        this.DateVente = DateVente;
        this.produit = produit;
        this.PrixVente = PrixVente(produit);
        produit.setprixVente(PrixVente);

        this.lot.RemoveProduit(produit); // Suppression du produit du lot


    }

    // fonction quii detérmine le prix de vente d'un produit en fonction du prix unitaire du lot
    public double PrixVente(Produit produit){
        return (this.lot.PrixLot/this.lot.nbProduits)*1.2;
    }





}