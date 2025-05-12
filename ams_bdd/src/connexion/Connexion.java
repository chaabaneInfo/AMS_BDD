package connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Connexion {
    private static Connection connection;

    // Méthode pour établir la connexion une seule fois
    public static Connection getConnection() {
        if (connection == null) {
            try {
                String dataBase = "etd";
                String url = "jdbc:postgresql://pedago.univ-avignon.fr:5432/" + dataBase;
                Properties props = new Properties();
                props.setProperty("user", "uapv2307072");
                props.setProperty("password", "MxEbeq");
                connection = DriverManager.getConnection(url, props);
                System.out.println("Connexion réussie !");
            } catch (Exception e) {
                System.err.println("Erreur de connexion : " + e.getMessage());
            }
        }
        return connection;
    }

    public static void main(String[] args) {
        Connection conn = Connexion.getConnection();
        if (conn != null) {
            System.out.println("La connexion est prête à être utilisée.");
        } else {
            System.out.println("Échec de la connexion.");
        }
    }
}

