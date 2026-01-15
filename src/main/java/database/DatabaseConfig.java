package database;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {
    private static final String PROPERTIES_FILE = "ConnectionDB.properties";

    private String host;
    private String port;
    private String dbName;
    private String user;
    private String password;

    public DatabaseConfig() {
        loadProperties();
    }

    private void loadProperties() {
        try (InputStream input = getClass()
                                .getClassLoader()
                                .getResourceAsStream(PROPERTIES_FILE)) {
            Properties prop = new Properties();
            if (input == null) {
                System.err.println("No se pudo encontrar el archivo de propiedades: " + PROPERTIES_FILE);
                return;
            }
            prop.load(input);
            this.host = prop.getProperty("db.host");
            this.port = prop.getProperty("db.port");
            this.dbName = prop.getProperty("db.name");
            this.user = prop.getProperty("db.username");
            this.password = prop.getProperty("db.password");
        } catch (Exception e) {
            System.err.println("Error cargando las propiedades de la base de datos: " + e.getMessage());
        }
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}