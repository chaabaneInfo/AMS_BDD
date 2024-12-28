public class Contact {

    public String Nom;
    public String Prenom;
    public String fontion;
    public String Telephone;
    public String Email;

    public Contact(String Nom, String Prenom, String fontion, String Telephone, String Email) {
        this.Nom = Nom;
        this.Prenom = Prenom;
        this.fontion = fontion;
        this.Telephone = Telephone;
        this.Email = Email;
    }
    
    public String GetInfo() {
        return "Nom: " + this.Nom + " Prenom: " + this.Prenom + " Fonction: " + this.fontion + " Telephone: " + this.Telephone + " Email: " + this.Email;
    }
}