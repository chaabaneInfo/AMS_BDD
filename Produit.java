public class Produit {
    // verifier si l'id est en private ou public
    public int id;
    public int IdLot ; 
    public String description;
    private String nom;
    private double prix;
    public String categorie;

    public Produit(String nom, double prix,String categorie,int IdLot) {
        this.nom = nom;
        this.prix = prix;
        this.categorie=categorie;
        this.IdLot=IdLot;
        this.description="Nom: "+nom+" Categorie: "+categorie+" Prix: "+prix+"€"+" lot " +IdLot;
    }

    public String getNom() {
        return nom;
    }

    public double getPrix() {
        return prix;
    }
    public int getIdLot() {
        return IdLot;
    }
    public String getCategorie() {
        return categorie;
    }


    public void afficher() {
        System.out.println(this.description);
    }
}