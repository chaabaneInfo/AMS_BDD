import java.util.Date;

public class Contrat{

    public int IdContrat ;
    public Date DateDebutContrat;
    public Date DateFinContrat;
    public Lot LotProduit;
    public Fournisseur fournisseur;
    public int quantiteMinimal;
    public int PrixFixe;

    public Contrat(int IdContrat, Date DateDebutContrat, Date DateFinContrat, Fournisseur fournisseur, int quantiteMinimal, int PrixFixe, Lot lot) {

        this.IdContrat = IdContrat;
        this.DateDebutContrat = DateDebutContrat;
        this.DateFinContrat = DateFinContrat;
        this.fournisseur = fournisseur;
        this.quantiteMinimal = quantiteMinimal;
        this.PrixFixe = PrixFixe;
        this.LotProduit = lot;
        lot.setPrixLot(PrixFixe);
        
    }


}