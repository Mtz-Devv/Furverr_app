/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author sout
 */
public class Ilustrador extends Usuario {
    private String descripcion;
    private double precioBase;
    private String rutaFotoPerfil;
    private String idioma;
    private String nivelIdioma;
    private String nombreMostrado;
    private String educacion;
    private String certificaciones;
    private String habilidades;

    public Ilustrador(String nombreUsuario, String correo, String contraseña) {
        super(nombreUsuario, correo, contraseña);
        this.descripcion = "";
        this.precioBase = 10.0;
        this.rutaFotoPerfil = "";
        this.idioma = "";
        this.nivelIdioma = "";
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }
    public String getRutaFotoPerfil() { return rutaFotoPerfil; }
    public void setRutaFotoPerfil(String rutaFotoPerfil) { this.rutaFotoPerfil = rutaFotoPerfil; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public String getNivelIdioma() { return nivelIdioma; }
    public void setNivelIdioma(String nivelIdioma) { this.nivelIdioma = nivelIdioma; }
    public String getNombreMostrado() { return nombreMostrado; }
    public void setNombreMostrado(String nombreMostrado) { this.nombreMostrado = nombreMostrado; }
    public String getEducacion() { return educacion; }
    public void setEducacion(String educacion) { this.educacion = educacion; }
    public String getCertificaciones() { return certificaciones; }
    public void setCertificaciones(String certificaciones) { this.certificaciones = certificaciones; }
    public String getHabilidades() { return habilidades; }
    public void setHabilidades(String habilidades) { this.habilidades = habilidades; }
}
