package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {


        // --- CONFIGURACIÓN DE CONEXIÓN ---

    private static final DatabaseConfig databaseConfig = new DatabaseConfig(); // Carga configuración desde DatabaseConfig.java
    private static final String HOST = databaseConfig.getHost(); // public ipv4 of AWS
    private static final String PORT = databaseConfig.getPort();
    private static final String DB_NAME = databaseConfig.getDbName();

    private static final String USER = databaseConfig.getUser();
    private static final String PASS = databaseConfig.getPassword();


    private static final String URL_ADMIN = "jdbc:mysql://" + HOST + ":" + PORT
            + "/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String URL_APP = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL_APP, USER, PASS);
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Falta el driver mysql-connector-j.");
        }
        return conn;
    }

    public static void crearDatabaseSiNoExiste() {
        try (Connection conn = DriverManager.getConnection(URL_ADMIN, USER, PASS);
                Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        } catch (SQLException e) {
            System.err.println("Error creando DB: " + e.getMessage());
        }
    }

    public static void crearTablasSiNoExisten() {

        // 1. Tabla USUARIOS
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS Usuarios ("
                + " nombreUsuario VARCHAR(100) PRIMARY KEY,"
                + " correo VARCHAR(255) UNIQUE NOT NULL,"
                + " contraseña VARCHAR(255) NOT NULL,"
                + " tipo VARCHAR(50),"
                + " estatus TINYINT(1) DEFAULT 1,"
                + " nombreMostrado VARCHAR(255),"
                + " descripcion TEXT,"
                + " precioBase DOUBLE,"
                + " rutaFotoPerfil VARCHAR(255),"
                + " idioma VARCHAR(100),"
                + " nivelIdioma VARCHAR(50),"
                + " educacion TEXT,"
                + " certificaciones TEXT,"
                + " habilidades TEXT"
                + ");";

        // 2. Tabla ILUSTRACIONES (Gigs)
        // Nota: Si la tabla ya existe, este CREATE no añade columnas nuevas.
        // Para eso usamos el método actualizarEsquema() más abajo.
        String sqlIlustraciones = "CREATE TABLE IF NOT EXISTS Ilustraciones ("
                + " id INT PRIMARY KEY AUTO_INCREMENT,"
                + " titulo VARCHAR(255),"
                + " descripcion TEXT,"
                + " precio DOUBLE,"
                + " rutaImagen VARCHAR(255),"
                + " nombreAutor VARCHAR(100),"
                + " categoria VARCHAR(100),"
                + " etiquetas TEXT,"
                + " estado VARCHAR(20) DEFAULT 'ACTIVO'," // Campo nuevo
                + " FOREIGN KEY (nombreAutor) REFERENCES Usuarios(nombreUsuario)"
                + ");";

        // 3. Tabla PEDIDOS
        String sqlPedidos = "CREATE TABLE IF NOT EXISTS Pedidos ("
                + " id INT PRIMARY KEY AUTO_INCREMENT,"
                + " comprador VARCHAR(100),"
                + " ilustrador VARCHAR(100),"
                + " servicio VARCHAR(255),"
                + " fechaVence VARCHAR(50),"
                + " total DOUBLE,"
                + " estado VARCHAR(50),"
                + " FOREIGN KEY (ilustrador) REFERENCES Usuarios(nombreUsuario)"
                + ");";

        String sqlPaquetes = "CREATE TABLE IF NOT EXISTS Paquetes ("
                + " id INT PRIMARY KEY AUTO_INCREMENT,"
                + " gigId INT NOT NULL,"
                + " tipo VARCHAR(20), "
                + " nombre VARCHAR(100),"
                + " descripcion TEXT,"
                + " tiempoEntrega INT,"
                + " revisiones INT,"
                + " precio DOUBLE,"
                + " FOREIGN KEY (gigId) REFERENCES Ilustraciones(id)"
                + ");";

        // Tabla EXTRAS
        String sqlExtras = "CREATE TABLE IF NOT EXISTS GigExtras ("
                + " id INT PRIMARY KEY AUTO_INCREMENT,"
                + " gigId INT NOT NULL,"
                + " titulo VARCHAR(100),"
                + " descripcion TEXT,"
                + " precio DOUBLE,"
                + " tiempoAdicional INT,"
                + " FOREIGN KEY (gigId) REFERENCES Ilustraciones(id)"
                + ");";

        // Tabla FAQs
        String sqlFaqs = "CREATE TABLE IF NOT EXISTS Faqs ("
                + " id INT PRIMARY KEY AUTO_INCREMENT,"
                + " gigId INT NOT NULL,"
                + " pregunta TEXT,"
                + " respuesta TEXT,"
                + " FOREIGN KEY (gigId) REFERENCES Ilustraciones(id)"
                + ");";

        // Tabla Galeria
        String sqlGaleria = "CREATE TABLE IF NOT EXISTS GigGaleria ("
                + " id INT PRIMARY KEY AUTO_INCREMENT,"
                + " gigId INT NOT NULL,"
                + " rutaImagen VARCHAR(255),"
                + " FOREIGN KEY (gigId) REFERENCES Ilustraciones(id)"
                + ");";

        // Tabla MENSAJES
        String sqlMensajes = "CREATE TABLE IF NOT EXISTS Mensajes ("
                + " id INT PRIMARY KEY AUTO_INCREMENT,"
                + " remitente VARCHAR(100),"
                + " destinatario VARCHAR(100),"
                + " mensaje TEXT,"
                + " fecha DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + " archivo VARCHAR(255),"
                + " leido TINYINT(1) DEFAULT 0,"
                + " FOREIGN KEY (remitente) REFERENCES Usuarios(nombreUsuario),"
                + " FOREIGN KEY (destinatario) REFERENCES Usuarios(nombreUsuario)"
                + ");";

        try (Connection conn = connect();
                Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(sqlUsuarios);
                stmt.execute(sqlIlustraciones);
                stmt.execute(sqlPedidos);
                stmt.execute(sqlPaquetes);
                stmt.execute(sqlExtras);
                stmt.execute(sqlFaqs);
                stmt.execute(sqlGaleria);
                stmt.execute(sqlMensajes);

                // --- PARCHE DE ACTUALIZACIÓN ---
                // Intenta agregar columnas nuevas si la tabla ya existía
                actualizarEsquema(stmt);
            }
        } catch (SQLException e) {
            System.err.println("Error creando tablas: " + e.getMessage());
        }
    }

    /**
     * Método para actualizar tablas existentes que no tienen las nuevas columnas.
     * Ejecuta ALTER TABLE y captura el error si la columna ya existe.
     */
    private static void actualizarEsquema(Statement stmt) {
        try {
            // Intenta agregar la columna 'estado' a Ilustraciones
            stmt.execute("ALTER TABLE Ilustraciones ADD COLUMN estado VARCHAR(20) DEFAULT 'ACTIVO'");
            System.out.println("[Database] Esquema actualizado: Columna 'estado' agregada.");
        } catch (SQLException e) {
            // Error 1060 = Duplicate column name (Significa que ya existe, lo ignoramos)
            if (e.getErrorCode() != 1060) {
                // Si es otro error, lo mostramos (opcional)
                // System.err.println("Info esquema: " + e.getMessage());
            }
        }

        // --- Actualización tabla Mensajes ---
        try {
            stmt.execute("ALTER TABLE Mensajes ADD COLUMN mensaje TEXT");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1060) {
            }
        }

        try {
            stmt.execute("ALTER TABLE Mensajes ADD COLUMN archivo VARCHAR(255)");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1060) {
            }
        }

        try {
            stmt.execute("ALTER TABLE Mensajes ADD COLUMN leido TINYINT(1) DEFAULT 0");
        } catch (SQLException e) {
            if (e.getErrorCode() != 1060) {
            }
        }
    }
}