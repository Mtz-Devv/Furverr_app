/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author sout
 */
public class Usuario {
    protected String nombreUsuario;
    protected String correo;
    protected String contraseña;
    protected String tipo; // "cliente", "ilustrador", "admin", "usuario_nuevo"
    protected boolean estatus = true; // true = activo, false = inhabilitado

    // --- VARIABLES NUEVAS PARA PERFIL ---
    protected String nombreMostrado;
    protected String descripcion;
    protected String rutaFotoPerfil;

    // Constructor completo
    public Usuario(String nombreUsuario, String correo, String contraseña, String tipo) {
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.contraseña = contraseña;
        this.tipo = tipo;
    }
    
    // Constructor simplificado (por defecto "usuario_nuevo")
    public Usuario(String nombreUsuario, String correo, String contraseña) {
        this(nombreUsuario, correo, contraseña, "usuario_nuevo");
    }

    // --- GETTERS Y SETTERS ---

    public String getNombreUsuario() { return nombreUsuario; }
    
    public String getCorreo() { return correo; }
    
    public String getContraseña() { return contraseña; }
    public void setContraseña(String nueva) { this.contraseña = nueva; }

    // Compatibilidad: Panel_Cuenta busca getTipoUsuario(), aquí lo redirigimos
    public String getTipoUsuario() { return tipo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public boolean isActivo() { return estatus; }
    public void setEstatus(boolean estatus) { this.estatus = estatus; }

    // --- NUEVOS GETTERS Y SETTERS (La solución a tus errores) ---

    public String getNombreMostrado() {
        return nombreMostrado;
    }

    public void setNombreMostrado(String nombreMostrado) {
        this.nombreMostrado = nombreMostrado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRutaFotoPerfil() {
        return rutaFotoPerfil;
    }

    public void setRutaFotoPerfil(String rutaFotoPerfil) {
        this.rutaFotoPerfil = rutaFotoPerfil;
    }
}