/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sout
 */
package Vista.componentes.cliente;

import modelos.Ilustracion;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderClientFeed {

    public static void aplicarOrden(List<Ilustracion> gigs, String criterio) {
        if (gigs == null || gigs.isEmpty()) return;

        switch (criterio) {
            case "Relevancia":
                // Orden por defecto (por ID ascendente, o como vinieron de la BD)
                Collections.sort(gigs, Comparator.comparingInt(Ilustracion::getId));
                break;

            case "Lo más nuevo":
                // ID descendente (suponiendo que IDs más altos son más nuevos)
                Collections.sort(gigs, (g1, g2) -> Integer.compare(g2.getId(), g1.getId()));
                break;

            case "Precio: Menor a Mayor":
                // Precio ascendente
                Collections.sort(gigs, Comparator.comparingDouble(Ilustracion::getPrecio));
                break;

            case "Precio: Mayor a Menor":
                // Precio descendente
                Collections.sort(gigs, (g1, g2) -> Double.compare(g2.getPrecio(), g1.getPrecio()));
                break;
        }
    }
}
