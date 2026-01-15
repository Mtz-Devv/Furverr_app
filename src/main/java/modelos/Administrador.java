/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author sout
 */
public class Administrador extends Usuario{
    public Administrador(String nombreUsuario, String correo, String contraseña) {
        super(nombreUsuario, correo, contraseña, "admin");
    }
}
