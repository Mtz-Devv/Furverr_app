/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;
public class Paquete {
    public String tipo; // Basic, Standard, Premium
    public String nombre;
    public String descripcion;
    public int entregaDias;
    public int revisiones; // -1 para ilimitado
    public double precio;

    public Paquete(String tipo, String nombre, String descripcion, int entregaDias, int revisiones, double precio) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.entregaDias = entregaDias;
        this.revisiones = revisiones;
        this.precio = precio;
    }
}
