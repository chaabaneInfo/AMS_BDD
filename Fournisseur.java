import java.util.ArrayList;
import java.util.List;
public class Fournisseur{

    public String NomSociete;
    public String NumeroSiret;
    public String Adresse;
    public String Email;
    public List<Contact> contacts;
    public List <Contrat> contrats;

    public Fournisseur(String NomSociete, String NumeroSiret, String Adresse, String Email) {
        this.NomSociete = NomSociete;
        this.NumeroSiret = NumeroSiret;
        this.Adresse = Adresse;
        this.Email = Email;
        this.contacts = new ArrayList<Contact>();
        this.contrats = new ArrayList<Contrat>();

    }


    public void ajouterContact(Contact contact) {
        this.contacts.add(contact);

    }
    public void ajouterContrat(Contrat contrat) {
        this.contrats.add(contrat);

    }
    


}