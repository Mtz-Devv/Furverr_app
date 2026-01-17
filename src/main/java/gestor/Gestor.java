package gestor;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import database.DatabaseConnector;
import database.GeneradorDeDatos; // <--- IMPORTAR
import database.GestorDeDatos;
import database.RespaldoBD;

public class Gestor {

    public static void main(String[] args) {

        System.setProperty("sun.java2d.d3d", "false");

        try {
            // Forzar Nimbus para evitar problemas de colores en Linux (GTK)
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("No se pudo establecer el Look and Feel.");
        }

        // 1. INICIALIZAR CARPETAS
        utils.GestorDeArchivos.inicializarDirectorios();

        // 2. Iniciar Base de Datos
        DatabaseConnector.crearDatabaseSiNoExiste();
        DatabaseConnector.crearTablasSiNoExisten();

        // 3. Restaurar o Cargar Datos
        RespaldoBD.restaurarDatos(); 
        
        // Si la BD estaba vacía y restaurarDatos no hizo nada (porque no había backup),
        // insertarDatosDePruebaSiVacio insertará los usuarios básicos.
        GestorDeDatos.insertarDatosDePruebaSiVacio();

        // --- NUEVO: RELLENAR DATOS DETALLADOS Y GENERAR FOTOS ---
        // Esto buscará a 'ArtByLaura' y 'PixelPanda', creará sus carpetas/fotos
        // y actualizará su info si no tienen ya una foto asignada manualmente.
        GeneradorDeDatos.inicializarIlustradoresDetallados();

        // 4. Hook de cierre
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cerrando aplicación... Guardando datos...");
            RespaldoBD.generarRespaldo();
        }));

        // 5. Abrir Ventana
        SwingUtilities.invokeLater(() -> {
            new Vista.Autenticador();
        });
    }
}
