// Archivo NUEVO: Vista/componentes/ComponentUtils.java
package Vista.componentes;

import Vista.Estilos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

/**
 * Clase de Utilidades (Caja de Herramientas).
 * Contiene los métodos públicos que varias vistas necesitan.
 * Todos los métodos aquí son "static", no se necesita un objeto.
 */
public class ComponentUtils {

    /**
     * Crea un botón de pestaña (ej. "ACTIVOS", "PENDIENTES").
     */
    public static JButton crearBotonTab(String text) {
        JButton button = new JButton(text);
        Estilos.estilizarBotonTab(button);
        return button;
    }

    /**
     * Crea una tarjeta de "placeholder" para vistas en construcción.
     */
    public static JPanel crearPanelPlaceholderWIP(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilos.COLOR_FONDO_GRIS);
        panel.setBorder(Estilos.PADDING_TARJETA);
        JLabel lblTitle = new JLabel(titulo, SwingConstants.CENTER);
        lblTitle.setFont(Estilos.FUENTE_TITULO_H2);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        JLabel lblWIP = new JLabel("Esta sección está en construcción.", SwingConstants.CENTER);
        lblWIP.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblWIP.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblTitle);
        centerPanel.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        centerPanel.add(lblWIP);
        centerPanel.add(Box.createVerticalGlue());
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Crea una tarjeta simple para mostrar montos (usada en Earnings y Analytics).
     */
    public static JPanel crearCardFondo(String titulo, String monto, String desc, boolean mostrarBoton) {
        JPanel panel = new JPanel();
        panel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panel.setBorder(Estilos.BORDE_TARJETA);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblDesc = new JLabel(desc);
        lblDesc.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblDesc.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblMonto = new JLabel(monto);
        lblMonto.setFont(Estilos.FUENTE_TITULO_H1);
        lblMonto.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        panel.add(lblDesc);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        panel.add(lblMonto);
        panel.add(Box.createVerticalGlue());
        if (mostrarBoton) {
            panel.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
            JButton btnPayout = new JButton("Añadir método de pago");
            Estilos.estilizarBotonNegro(btnPayout);
            btnPayout.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(btnPayout);
        }
        return panel;
    }

    /**
     * Crea una tabla vacía con cabeceras y un mensaje.
     */
    public static JPanel crearPanelTablaWIP(String titulo, String[] headers, String mensajeVacio) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, 0, 0));
        if (titulo != null) {
            JLabel lblTitle = new JLabel(titulo);
            lblTitle.setFont(Estilos.FUENTE_TITULO_H3);
            lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
            lblTitle.setBorder(new EmptyBorder(0, 0, Estilos.ESPACIO_MEDIANO, 0));
            panel.add(lblTitle, BorderLayout.NORTH);
        }
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panelTabla.setBorder(BorderFactory.createLineBorder(Estilos.COLOR_BORDE));
        JPanel tableHeader = new JPanel(new GridLayout(1, headers.length, 10, 10));
        tableHeader.setOpaque(false);
        tableHeader.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, Estilos.ESPACIO_PEQUENO, Estilos.ESPACIO_MEDIANO,
                Estilos.ESPACIO_PEQUENO));
        for (String header : headers) {
            JLabel lbl = new JLabel(header);
            lbl.setFont(Estilos.FUENTE_TEXTO_BOLD);
            lbl.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
            tableHeader.add(lbl);
        }
        panelTabla.add(tableHeader, BorderLayout.NORTH);
        panelTabla.add(new JSeparator(), BorderLayout.CENTER);
        JLabel lblNoData = new JLabel(mensajeVacio, SwingConstants.CENTER);
        lblNoData.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblNoData.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblNoData.setBorder(new EmptyBorder(Estilos.ESPACIO_GRANDE * 2, Estilos.ESPACIO_MEDIANO,
                Estilos.ESPACIO_GRANDE * 2, Estilos.ESPACIO_MEDIANO));
        panelTabla.add(lblNoData, BorderLayout.SOUTH);
        panel.add(panelTabla, BorderLayout.CENTER);
        return panel;
    }
}