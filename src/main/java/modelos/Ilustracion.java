/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import javax.swing.ImageIcon;

public class Ilustracion {
    private int id; // Añadido ID para identificar al editar/borrar
    private String titulo;
    private String descripcion;
    private double precio;
    private String rutaImagen; 
    private String nombreAutor; 
    private String categoria;
    private String etiquetas;
    private String estado; // "ACTIVO" o "BORRADOR"

    // Constructor completo
    public Ilustracion(int id, String titulo, String descripcion, double precio, String rutaImagen, String nombreAutor, String categoria, String etiquetas, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.rutaImagen = rutaImagen;
        this.nombreAutor = nombreAutor;
        this.categoria = categoria;
        this.etiquetas = etiquetas;
        this.estado = estado;
    }

    // Constructor simplificado (para creación)
    public Ilustracion(String titulo, String descripcion, double precio, String rutaImagen, String nombreAutor, String categoria, String etiquetas) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.rutaImagen = rutaImagen;
        this.nombreAutor = nombreAutor;
        this.categoria = categoria;
        this.etiquetas = etiquetas;
        this.estado = "ACTIVO"; // Por defecto
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public String getRutaImagen() { return rutaImagen; }
    public String getNombreAutor() { return nombreAutor; }
    public String getCategoria() { return categoria; }
    public String getEtiquetas() { return etiquetas; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public ImageIcon getImageIcon() {
        try {
            return new ImageIcon(rutaImagen); 
        } catch (Exception e) {
            return null; 
        }
    }
}