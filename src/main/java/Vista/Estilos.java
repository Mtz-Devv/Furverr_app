// Archivo: Vista/Estilos.java
package Vista;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Estilos {

    // --- COLORES ---
    public static final Color COLOR_FONDO_GRIS = new Color(245, 245, 245);
    public static final Color COLOR_FONDO_BLANCO = Color.WHITE;
    public static final Color COLOR_VERDE_PRIMARIO = new Color(26, 177, 77);
    public static final Color COLOR_TEXTO_PRINCIPAL = new Color(34, 34, 34); // Casi negro
    public static final Color COLOR_TEXTO_SECUNDARIO = new Color(110, 110, 110); // Gris
    public static final Color COLOR_TEXTO_LINK = new Color(74, 120, 222);
    public static final Color COLOR_BORDE = new Color(220, 220, 220); // Borde gris claro

    // --- FUENTES ---
    public static final Font FUENTE_TITULO_H1 = new Font("SansSerif", Font.BOLD, 28);
    public static final Font FUENTE_TITULO_H2 = new Font("SansSerif", Font.BOLD, 24);
    public static final Font FUENTE_TITULO_H3 = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FUENTE_TEXTO_BOLD = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FUENTE_TEXTO_REGULAR = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FUENTE_TEXTO_SECUNDARIO = new Font("SansSerif", Font.PLAIN, 13);

    // --- BORDES Y ESPACIADO (AHORA ESTANDARIZADO) ---
    public static final int ESPACIO_GRANDE = 25;
    public static final int ESPACIO_MEDIANO = 15;
    public static final int ESPACIO_PEQUENO = 8;
    
    // Padding para las tarjetas blancas
    public static final Border PADDING_TARJETA = new EmptyBorder(ESPACIO_GRANDE, ESPACIO_GRANDE, ESPACIO_GRANDE, ESPACIO_GRANDE);
    // Padding para los páneles principales (fondo gris)
    public static final Border PADDING_PANEL_GRIS = new EmptyBorder(ESPACIO_GRANDE, 40, ESPACIO_GRANDE, 40);

    public static final Border BORDE_TARJETA = BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDE, 1),
            PADDING_TARJETA // Usar el padding estándar
    );
    
    public static final Border BORDE_CAMPO_TEXTO = BorderFactory.createCompoundBorder(
            new LineBorder(COLOR_BORDE, 1),
            new EmptyBorder(ESPACIO_PEQUENO, ESPACIO_PEQUENO, ESPACIO_PEQUENO, ESPACIO_PEQUENO)
    );


    // --- MÉTODOS PARA ESTILIZAR BOTONES ---

    public static void estilizarBotonPrimario(JButton btn) {
        btn.setBackground(COLOR_VERDE_PRIMARIO);
        btn.setForeground(Color.WHITE);
        btn.setFont(FUENTE_TEXTO_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    public static void estilizarBotonSecundario(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(COLOR_TEXTO_PRINCIPAL);
        btn.setFont(FUENTE_TEXTO_BOLD);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1),
                new EmptyBorder(ESPACIO_PEQUENO, ESPACIO_MEDIANO, ESPACIO_PEQUENO, ESPACIO_MEDIANO) // Padding
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    public static void estilizarBotonNegro(JButton btn) {
        btn.setBackground(COLOR_TEXTO_PRINCIPAL);
        btn.setForeground(Color.WHITE);
        btn.setFont(FUENTE_TEXTO_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void estilizarBotonTexto(JButton btn, boolean conSubrayado) {
        btn.setFont(FUENTE_TEXTO_REGULAR);
        btn.setForeground(COLOR_TEXTO_SECUNDARIO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        if (conSubrayado) {
            // (Lógica para subrayar al pasar el mouse)
        }
    }
    
    public static void estilizarBotonTab(JButton btn) {
        btn.setFont(FUENTE_TEXTO_BOLD);
        btn.setForeground(COLOR_TEXTO_SECUNDARIO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}