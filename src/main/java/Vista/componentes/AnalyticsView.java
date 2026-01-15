// Archivo: Vista/componentes/AnalyticsView.java
package Vista.componentes;

import Vista.Estilos;
import Vista.ModernScrollBarUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Esta clase construye ÚNICAMENTE los paneles de Analytics (Overview y Repeat).
 */
public class AnalyticsView {

    /**
     * Constructor vacío. Esta clase solo crea paneles cuando se le piden.
     */
    public AnalyticsView() {
        // No necesita referencias, solo usa utilidades estáticas
    }

    // ==================================================================
    // --- MÉTODOS PÚBLICOS PARA OBTENER LOS PANELES ---
    // ==================================================================

    public JScrollPane getPanelOverview() {
        return crearPanelAnalyticsOverview();
    }

    public JScrollPane getPanelRepeat() {
        return crearPanelAnalyticsRepeat();
    }

    // ==================================================================
    // --- MÉTODOS DE CONSTRUCCIÓN ---
    // ==================================================================

    private JScrollPane crearPanelAnalyticsOverview() {
        JPanel panelVistaAnalytics = new JPanel(new BorderLayout(20, 20));
        panelVistaAnalytics.setBackground(Estilos.COLOR_FONDO_GRIS);
        panelVistaAnalytics.setBorder(Estilos.PADDING_PANEL_GRIS);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Resumen de pedidos");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H1);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        panelHeader.add(lblTitle, BorderLayout.NORTH);
        panelVistaAnalytics.add(panelHeader, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel(new BorderLayout(20, Estilos.ESPACIO_GRANDE));
        panelContenido.setOpaque(false);
        panelContenido.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, 0, 0));
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 3, Estilos.ESPACIO_GRANDE, Estilos.ESPACIO_GRANDE));
        panelTarjetas.setOpaque(false);

        // **MODIFICACIÓN IMPORTANTE**
        // Llama al método estático de ComponentUtils
        panelTarjetas.add(ComponentUtils.crearCardFondo(
            "Total de pedidos", "0", "Completados en total", false
        ));
        panelTarjetas.add(ComponentUtils.crearCardFondo(
            "Ingresos totales", "USD 0.00", "Ganancias de todos los pedidos", false
        ));
        panelTarjetas.add(ComponentUtils.crearCardFondo(
            "Precio promedio", "USD 0.00", "Por pedido completado", false
        ));
        
        panelContenido.add(panelTarjetas, BorderLayout.NORTH);
        
        panelContenido.add(ComponentUtils.crearPanelTablaWIP(
            "Pedidos Recientes",
            new String[]{"CLIENTE", "SERVICIO", "COMPLETADO", "TOTAL"},
            "No hay pedidos recientes para mostrar."
        ), BorderLayout.CENTER);
        
        panelVistaAnalytics.add(panelContenido, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(panelVistaAnalytics);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getViewport().setBackground(Estilos.COLOR_FONDO_GRIS);
        return scrollPane;
    }

    private JScrollPane crearPanelAnalyticsRepeat() {
        JPanel panelVistaAnalyticsRepeat = new JPanel(new BorderLayout(20, 20));
        panelVistaAnalyticsRepeat.setBackground(Estilos.COLOR_FONDO_GRIS);
        panelVistaAnalyticsRepeat.setBorder(Estilos.PADDING_PANEL_GRIS);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Repetir negocios");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H1);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        panelHeader.add(lblTitle, BorderLayout.NORTH);
        panelVistaAnalyticsRepeat.add(panelHeader, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel(new BorderLayout(20, Estilos.ESPACIO_GRANDE));
        panelContenido.setOpaque(false);
        panelContenido.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, 0, 0));
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 3, Estilos.ESPACIO_GRANDE, Estilos.ESPACIO_GRANDE));
        panelTarjetas.setOpaque(false);

        // **MODIFICACIÓN IMPORTANTE**
        panelTarjetas.add(ComponentUtils.crearCardFondo(
            "Tasa de repetición", "0%", "Clientes que volvieron a comprar", false
        ));
        panelTarjetas.add(ComponentUtils.crearCardFondo(
            "Clientes recurrentes", "0", "Clientes con 2+ pedidos", false
        ));
        panelTarjetas.add(ComponentUtils.crearCardFondo(
            "Pedidos recurrentes", "0", "Pedidos de clientes existentes", false
        ));
        
        panelContenido.add(panelTarjetas, BorderLayout.NORTH);

        panelContenido.add(ComponentUtils.crearPanelTablaWIP(
            "Clientes Recurrentes",
            new String[]{"CLIENTE", "PEDIDOS TOTALES", "ÚLTIMO PEDIDO"},
            "No hay clientes recurrentes para mostrar."
        ), BorderLayout.CENTER);
        
        panelVistaAnalyticsRepeat.add(panelContenido, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(panelVistaAnalyticsRepeat);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getViewport().setBackground(Estilos.COLOR_FONDO_GRIS);
        return scrollPane;
    }
}