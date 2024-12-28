public class Contrat{

    public int IdContrat ;
    public Date DateDebutContrat;
    public Date DateFinContrat;
    public Produit produit;
    public Fournisseur fournisseur;
    public int quantiteMinimal;
    public int PrixFixe;

    public Contrat(int IdContrat, Date DateDebutContrat, Date DateFinContrat, Produit produit, Fournisseur fournisseur, int quantiteMinimal, int PrixFixe) {
        this.IdContrat = IdContrat;
        this.DateDebutContrat = DateDebutContrat;
        this.DateFinContrat = DateFinContrat;
        this.produit = produit;
        this.fournisseur = fournisseur;
        this.quantiteMinimal = quantiteMinimal;
        this.PrixFixe = PrixFixe;
    }


}