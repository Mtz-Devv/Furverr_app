/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RespaldoBD {

    // Archivo donde se guardarán los datos (en la raíz del proyecto)
    private static final String ARCHIVO_SQL = "backup_fourverr.sql";

    // Lista de tablas en orden de dependencia (Primero las que no tienen FK, luego las que sí)
    private static final String[] TABLAS = {
        "Usuarios",
        "Ilustraciones",
        "Paquetes",
        "GigExtras",
        "Faqs",
        "GigGaleria",
        "Pedidos"
    };

    // ==========================================
    // 1. MÉTODO PARA IMPORTAR (CARGAR DATOS)
    // ==========================================
    public static void restaurarDatos() {
        File archivo = new File(ARCHIVO_SQL);
        if (!archivo.exists()) {
            System.out.println("[Respaldo] No existe archivo de respaldo previo. Se iniciará en limpio.");
            return;
        }

        // Verificar si la BD ya tiene datos para no duplicar
        if (!estaVaciaLaBD()) {
            System.out.println("[Respaldo] La base de datos ya tiene información. No se requiere restauración.");
            return;
        }

        System.out.println("[Respaldo] Restaurando base de datos desde " + ARCHIVO_SQL + "...");
        
        try (Connection conn = DatabaseConnector.connect();
             BufferedReader br = new BufferedReader(new FileReader(archivo));
             Statement stmt = conn.createStatement()) {

            StringBuilder sqlStatement = new StringBuilder();
            String linea;
            
            conn.setAutoCommit(false); // Transacción para velocidad y seguridad

            while ((linea = br.readLine()) != null) {
                // Ignorar comentarios o líneas vacías
                if (linea.trim().isEmpty() || linea.trim().startsWith("--") || linea.trim().startsWith("/*")) {
                    continue;
                }
                
                sqlStatement.append(linea);
                
                // Si la línea termina en punto y coma, ejecutamos
                if (linea.trim().endsWith(";")) {
                    try {
                        stmt.execute(sqlStatement.toString());
                    } catch (SQLException e) {
                        // Ignoramos errores de duplicados por seguridad
                        System.err.println("Error ejecutando línea importada: " + e.getMessage());
                    }
                    sqlStatement.setLength(0); // Limpiar buffer
                }
            }
            conn.commit();
            System.out.println("[Respaldo] Restauración completada con éxito.");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // 2. MÉTODO PARA EXPORTAR (GUARDAR DATOS)
    // ==========================================
    public static void generarRespaldo() {
        System.out.println("[Respaldo] Generando archivo de permanencia...");
        
        try (Connection conn = DatabaseConnector.connect();
             BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_SQL))) {

            bw.write("-- RESPALDO AUTOMATICO FOURVERR APP\n");
            bw.write("-- FECHA: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + "\n\n");

            for (String tabla : TABLAS) {
                guardarTabla(conn, bw, tabla);
            }
            
            System.out.println("[Respaldo] Datos guardados correctamente en " + ARCHIVO_SQL);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void guardarTabla(Connection conn, BufferedWriter bw, String tabla) throws SQLException, IOException {
        String sql = "SELECT * FROM " + tabla;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            
            ResultSetMetaData meta = rs.getMetaData();
            int numColumnas = meta.getColumnCount();

            while (rs.next()) {
                StringBuilder insert = new StringBuilder();
                insert.append("INSERT INTO ").append(tabla).append(" VALUES (");

                for (int i = 1; i <= numColumnas; i++) {
                    Object valor = rs.getObject(i);
                    
                    if (valor == null) {
                        insert.append("NULL");
                    } else if (valor instanceof String) {
                        // Escapar comillas simples para evitar errores SQL
                        String valStr = valor.toString().replace("'", "\\'"); 
                        // Reemplazar saltos de línea para que quede en una sola línea en el archivo
                        valStr = valStr.replace("\n", "\\n").replace("\r", ""); 
                        insert.append("'").append(valStr).append("'");
                    } else {
                        insert.append(valor.toString());
                    }

                    if (i < numColumnas) {
                        insert.append(", ");
                    }
                }
                insert.append(");\n");
                bw.write(insert.toString());
            }
            bw.write("\n"); // Espacio entre tablas
        }
    }

    // Helper para saber si debemos importar o no
    private static boolean estaVaciaLaBD() {
        // Revisamos si hay usuarios. Si no hay, asumimos que está vacía.
        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Usuarios")) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            // Si la tabla no existe, es que está vacía (o rota)
            return true;
        }
        return true;
    }
}
