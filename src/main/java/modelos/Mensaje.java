package modelos;

import java.sql.Timestamp;

public class Mensaje {
    private int id;
    private String remitente;
    private String destinatario;
    private String mensaje;
    private Timestamp fecha;
    private String archivo;
    private boolean leido;

    public Mensaje(int id, String remitente, String destinatario, String mensaje, Timestamp fecha, String archivo,
            boolean leido) {
        this.id = id;
        this.remitente = remitente;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.archivo = archivo;
        this.leido = leido;
    }

    public int getId() {
        return id;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public String getArchivo() {
        return archivo;
    }

    public boolean isLeido() {
        return leido;
    }
}
