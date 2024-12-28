public class Fournisseur{

    public String NomSociete;
    public String NumeroSiret;
    public String Adresse;
    public String Email;
    public Contact[] contacts;
    public Contrat[] contrats;

    public Fournisseur(String NomSociete, String NumeroSiret, String Adresse, String Email) {
        this.NomSociete = NomSociete;
        this.NumeroSiret = NumeroSiret;
        this.Adresse = Adresse;
        this.Email = Email;
        this.contacts = new Contact[100];
        this.contrats = new Contrat[100];
    }


    public void ajouterContact(Contact contact) {
        this.contacts[this.contacts.length] = contact;
    }
    public void ajouterContrat(Contrat contrat) {
        this.contrats[this.contrats.length] = contrat;
    }
    


}