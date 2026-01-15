// Archivo: Vista/componentes/OrdersView.java
package Vista.componentes;

import Vista.Estilos;
import Vista.ModernScrollBarUI;

import javax.swing.*;

import java.awt.*;

/**
 * Esta clase construye ÚNICAMENTE el panel de Pedidos (Orders).
 */
public class OrdersView {

    private JScrollPane panel; // El panel que esta clase construye

    public OrdersView() {
        this.panel = crearPanelOrders();
    }

    /**
     * Método público para que Panel_Ilustrador obtenga el panel construido.
     */
    public JScrollPane getPanel() {
        return this.panel;
    }

    // ==================================================================
    // --- MÉTODOS DE CONSTRUCCIÓN DE LA VISTA "ORDERS" ---
    // ==================================================================

    public JScrollPane crearPanelOrders() {
        JPanel panelVistaOrders = new JPanel(new BorderLayout(20, 20));
        panelVistaOrders.setBackground(Estilos.COLOR_FONDO_GRIS);
        panelVistaOrders.setBorder(Estilos.PADDING_PANEL_GRIS);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Gestionar Pedidos");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H1);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        panelHeader.add(lblTitle, BorderLayout.WEST);
        JTextField txtSearch = new JTextField("Buscar en mi historial... \uD83D\uDD0D");
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        txtSearch.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        panelHeader.add(txtSearch, BorderLayout.EAST);
        panelVistaOrders.add(panelHeader, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel();
        panelContenido.setOpaque(false);
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        JPanel panelTabs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTabs.setOpaque(false);

        // **MODIFICACIÓN IMPORTANTE**
        // Ahora usa la utilidad estática ComponentUtils
        panelTabs.add(ComponentUtils.crearBotonTab("PRIORIDAD"));
        panelTabs.add(ComponentUtils.crearBotonTab("ACTIVOS"));
        panelTabs.add(ComponentUtils.crearBotonTab("RETRASADOS"));
        panelTabs.add(ComponentUtils.crearBotonTab("ENTREGADOS"));
        panelTabs.add(ComponentUtils.crearBotonTab("COMPLETADOS"));
        panelTabs.add(ComponentUtils.crearBotonTab("CANCELADOS"));
        panelTabs.add(ComponentUtils.crearBotonTab("DESTACADOS"));

        panelContenido.add(panelTabs);
        panelContenido.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));

        // Usa la utilidad estática ComponentUtils
        panelContenido.add(ComponentUtils.crearPanelTablaWIP(
                "PEDIDOS PRIORITARIOS",
                new String[] { "COMPRADOR", "SERVICIO", "VENCE EL", "TOTAL", "NOTA", "ESTADO" },
                "No hay pedidos prioritarios que mostrar."));

        panelVistaOrders.add(panelContenido, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(panelVistaOrders);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getViewport().setBackground(Estilos.COLOR_FONDO_GRIS);
        return scrollPane;
    }
}