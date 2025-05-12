package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Gestion {
    private Connection connection;

    public Gestion(Connection connection) {
        this.connection = connection;
    }

    public void createTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS product (" +
                       "id NUMERIC PRIMARY KEY, " +
                       "numLot VARCHAR(50), " +
                       "nom VARCHAR(100), " +
                       "description VARCHAR(255), " +
                       "categorie VARCHAR(50), " +
                       "prix FLOAT8" +
                       ")";
        execute(query);
    }

    public void insert(IData data, String table) throws SQLException {
        HashMap<String, fieldType> tableStruct = structTable(table, false);

        if (!data.check(tableStruct)) {
            throw new IllegalArgumentException("La structure de l'instance ne correspond pas à celle de la table.");
        }

        // Préparer les colonnes et les placeholders
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (String key : tableStruct.keySet()) {
            if (columns.length() > 0) {
                columns.append(", ");
                placeholders.append(", ");
            }
            columns.append(key);
            placeholders.append("?");
        }

        String query = "INSERT INTO " + table + " (" + columns + ") VALUES (" + placeholders + ") " +
                       "ON CONFLICT (id) DO UPDATE SET " +
                       "prix = product.prix + EXCLUDED.prix, " +
                       "description = product.description || ' ' || EXCLUDED.description";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            int index = 1;

            for (String key : tableStruct.keySet()) {
                fieldType type = tableStruct.get(key);

                // Récupérer la valeur correspondante dans IData
                Object value = switch (key) {
                    case "id" -> Integer.parseInt(data.getValues().split(", ")[0].trim());
                    case "numlot" -> data.getValues().split(", ")[1].trim();
                    case "nom" -> data.getValues().split(", ")[2].trim();
                    case "description" -> data.getValues().split(", ")[3].trim();
                    case "categorie" -> data.getValues().split(", ")[4].trim();
                    case "prix" -> Double.parseDouble(
                            data.getValues().split(", ")[5].trim().replace(",", ".")
                    );
                    default -> throw new IllegalArgumentException("Colonne inconnue : " + key);
                };

                // Assigner les valeurs selon le type
                if (type == fieldType.NUMERIC) {
                    stmt.setInt(index, (Integer) value);
                } else if (type == fieldType.FLOAT8) {
                    stmt.setDouble(index, (Double) value);
                } else if (type == fieldType.VARCHAR) {
                    stmt.setString(index, (String) value);
                } else {
                    throw new IllegalArgumentException("Type inconnu : " + type);
                }
                index++;
            }

            stmt.executeUpdate();
        }
    }


    public HashMap<String, fieldType> structTable(String table, boolean display) throws SQLException {
        HashMap<String, fieldType> struct = new HashMap<>();
        String query = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, table);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String column = rs.getString("column_name");
                String type = rs.getString("data_type");

                // Correct mapping of SQL types to fieldType
                fieldType fieldType = null;
                switch (type) {
                    case "integer":
                        fieldType = fieldType.INT4;
                        break;
                    case "numeric":
                        fieldType = fieldType.NUMERIC;
                        break;
                    case "double precision":
                        fieldType = fieldType.FLOAT8;
                        break;
                    case "character varying":
                        fieldType = fieldType.VARCHAR;
                        break;
                    default:
                        throw new IllegalArgumentException("Type SQL inconnu : " + type);
                }

                if (fieldType != null) {
                    struct.put(column, fieldType);
                }
            }
        }

        if (display) {
            struct.forEach((key, value) -> System.out.println(key + " : " + value));
        }

        return struct;
    }

    public void displayTable(String table) throws SQLException {
        String query = "SELECT * FROM " + table;
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
        }
    }

    public void execute(String query) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.executeUpdate();
        }
    }

    public void dropTable(String table) throws SQLException {
        String query = "DROP TABLE IF EXISTS " + table;
        execute(query);
    }
    
    public void update(String table, int id, HashMap<String, Object> updates) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE " + table + " SET ");

        for (String column : updates.keySet()) {
            if (query.toString().endsWith("SET ")) {
                query.append(column).append(" = ?");
            } else {
                query.append(", ").append(column).append(" = ?");
            }
        }

        query.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            int index = 1;

            for (Object value : updates.values()) {
                if (value instanceof Integer) {
                    stmt.setInt(index, (Integer) value);
                } else if (value instanceof Double) {
                    stmt.setDouble(index, (Double) value);
                } else if (value instanceof String) {
                    stmt.setString(index, (String) value);
                }
                index++;
            }

            stmt.setInt(index, id);
            stmt.executeUpdate();
        }
    }
    
    public void find(String table, String column, String value) throws SQLException {
        String query = "SELECT * FROM " + table + " WHERE " + column + " LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + value + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
        }
    }
    
}


