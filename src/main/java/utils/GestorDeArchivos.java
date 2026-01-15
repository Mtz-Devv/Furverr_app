/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class GestorDeArchivos {

    // Ruta Base: Documentos/usuarios
    private static final String RUTA_BASE = System.getProperty("user.home") + File.separator + "Documents"
            + File.separator + "usuarios";

    public static final String CARPETA_CLIENTES = "clientes";
    public static final String CARPETA_ILUSTRADORES = "ilustradores";
    public static final String CARPETA_ADMIN = "admin_data";

    public static void inicializarDirectorios() {
        crearDirectorio(RUTA_BASE);
        crearDirectorio(RUTA_BASE + File.separator + CARPETA_CLIENTES);
        crearDirectorio(RUTA_BASE + File.separator + CARPETA_ILUSTRADORES);
        crearDirectorio(RUTA_BASE + File.separator + CARPETA_ADMIN);
    }

    private static void crearDirectorio(String ruta) {
        File directorio = new File(ruta);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }
    
    public static String getRutaAdmin() {
        return RUTA_BASE + File.separator + CARPETA_ADMIN;
    }

    /**
     * Guarda FOTO DE PERFIL.
     * Estructura: .../usuarios/{tipo}/{nombreUsuario}/perfil.ext
     */
    public static String guardarImagenPerfil(File archivoOrigen, String tipoUsuario, String nombreUsuario) {
        try {
            // Definir carpeta del usuario (ej: ilustradores/u)
            String carpetaUsuario = RUTA_BASE + File.separator + tipoUsuario + File.separator + nombreUsuario;
            crearDirectorio(carpetaUsuario);

            // Obtener extensión original
            String nombreOriginal = archivoOrigen.getName();
            String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
            
            // Nombre final estandarizado
            String nombreArchivoFinal = "perfil" + extension;

            Path destino = Paths.get(carpetaUsuario + File.separator + nombreArchivoFinal);
            
            // Copiar archivo
            Files.copy(archivoOrigen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

            return destino.toString();

        } catch (IOException e) {
            System.err.println("Error guardando perfil: " + e.getMessage());
            return null;
        }
    }

    /**
     * Guarda IMAGEN DE GIG.
     * Estructura: .../usuarios/ilustradores/{nombreUsuario}/gigs/{nombreGig}/{imagen}
     */
    public static String guardarImagenGig(File archivoOrigen, String nombreUsuario, String tituloGig, int indice) {
        try {
            // 1. Limpiar el título del gig para que sea un nombre de carpeta válido
            // Reemplaza caracteres inválidos por guiones bajos
            String tituloLimpio = tituloGig.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

            // 2. Construir ruta: ilustradores -> usuario -> gigs -> tituloGig
            String carpetaGig = RUTA_BASE + File.separator + CARPETA_ILUSTRADORES + File.separator 
                              + nombreUsuario + File.separator + "gigs" + File.separator + tituloLimpio;
            
            crearDirectorio(carpetaGig);

            // 3. Nombre del archivo (ej: img_0.png, img_1.jpg)
            String nombreOriginal = archivoOrigen.getName();
            String extension = "";
            if(nombreOriginal.contains(".")) {
                extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
            }
            
            String nombreFinal = "img_" + indice + extension;
            
            Path destino = Paths.get(carpetaGig + File.separator + nombreFinal);

            // 4. Copiar
            Files.copy(archivoOrigen.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

            return destino.toString();

        } catch (IOException e) {
            System.err.println("Error guardando imagen gig: " + e.getMessage());
            return null;
        }
    }
}