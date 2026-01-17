package utils;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.util.UUID;

public class S3Service {

    // CONFIGURACIÓN CENTRALIZADA
    private static final String BUCKET_NAME = "furverr-server"; 
    private static final Region REGION = Region.US_EAST_1; 

    private static S3Client s3;

    // Singleton Lazy para no reconectar cada vez
    private static S3Client getClient() {
        if (s3 == null) {
            s3 = S3Client.builder()
                    .region(REGION)
                    .credentialsProvider(DefaultCredentialsProvider.create()) // Busca en variables de entorno o .aws/credentials
                    .build();
        }
        return s3;
    }

    /**
     * Sube un archivo a S3 y retorna la URL pública.
     * @param archivo El archivo local a subir.
     * @param nombreUsuario El usuario (para organizar carpetas).
     * @param carpeta La "carpeta" lógica (ej: "perfiles", "gigs").
     * @return La URL pública de la imagen o NULL si falla.
     */
    public static String subirImagen(File archivo, String nombreUsuario, String carpeta) {
        try {
            S3Client client = getClient();
            
            // Generar nombre único: carpeta/usuario/timestamp_uuid.png
            String extension = obtenerExtension(archivo.getName());
            String keyName = carpeta + "/" + nombreUsuario + "/" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + extension;

            // Preparar petición
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(keyName)
                    //.acl("public-read") 
                    .build();

            // Subir
            client.putObject(request, RequestBody.fromFile(archivo));

            // Retornar URL
            return "https://" + BUCKET_NAME + ".s3." + REGION.id() + ".amazonaws.com/" + keyName;

        } catch (Exception e) {
            System.err.println("Error subiendo a S3: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static String obtenerExtension(String nombre) {
        int i = nombre.lastIndexOf('.');
        return (i > 0) ? nombre.substring(i) : ".png"; // Default png
    }
}