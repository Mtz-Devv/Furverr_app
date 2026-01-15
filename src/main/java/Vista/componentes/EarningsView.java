// Archivo: Vista/componentes/EarningsView.java
package Vista.componentes;

import Vista.Estilos;
import Vista.ModernScrollBarUI;
import modelos.Ilustrador;
import database.GestorDeDatos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Esta clase construye ÚNICAMENTE el panel de Ingresos (Earnings).
 */
public class EarningsView {

    private JScrollPane panel; // El panel que esta clase construye
    private Ilustrador ilustrador;

    // Etiquetas dinámicas
    private JLabel lblFondosDisponibles;
    private JLabel lblPagosActivos;
    private JLabel lblIngresosTotales;

    public EarningsView(Ilustrador ilustrador) {
        this.ilustrador = ilustrador;
        this.panel = crearPanelEarnings();
        actualizarEarnings();
    }

    /**
     * Método público para que Panel_Ilustrador obtenga el panel construido.
     */
    public JScrollPane getPanel() {
        return this.panel;
    }

    /**
     * Actualiza los datos de ganancias consultando la base de datos.
     */
    public void actualizarEarnings() {
        if (ilustrador == null)
            return;

        double ganancias = GestorDeDatos.obtenerGananciasTotales(ilustrador.getNombreUsuario());
        double activos = GestorDeDatos.obtenerTotalPedidosActivos(ilustrador.getNombreUsuario());

        if (lblFondosDisponibles != null) {
            lblFondosDisponibles.setText("USD " + ganancias);
        }
        if (lblPagosActivos != null) {
            lblPagosActivos.setText("USD " + activos);
        }
        if (lblIngresosTotales != null) {
            lblIngresosTotales.setText("USD " + ganancias);
        }
    }

    // ==================================================================
    // --- MÉTODOS DE CONSTRUCCIÓN DE LA VISTA "EARNINGS" ---
    // ==================================================================

    public JScrollPane crearPanelEarnings() {
        JPanel panelVistaEarnings = new JPanel(new BorderLayout(20, 20));
        panelVistaEarnings.setBackground(Estilos.COLOR_FONDO_GRIS);
        panelVistaEarnings.setBorder(Estilos.PADDING_PANEL_GRIS);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Ingresos");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H1);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        panelHeader.add(lblTitle, BorderLayout.NORTH);
        JPanel panelTabs = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTabs.setOpaque(false);

        // **MODIFICACIÓN IMPORTANTE**
        // Ahora usa la utilidad estática ComponentUtils
        panelTabs.add(ComponentUtils.crearBotonTab("Resumen"));
        panelTabs.add(ComponentUtils.crearBotonTab("Documentos financieros"));

        panelHeader.add(panelTabs, BorderLayout.CENTER);
        panelVistaEarnings.add(panelHeader, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel(new GridLayout(1, 3, Estilos.ESPACIO_GRANDE, Estilos.ESPACIO_GRANDE));
        panelContenido.setOpaque(false);
        panelContenido.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, 0, 0));

        // Inicializamos las etiquetas
        lblFondosDisponibles = new JLabel("USD 0.00");
        lblPagosActivos = new JLabel("USD 0.00");
        lblIngresosTotales = new JLabel("USD 0.00");

        panelContenido.add(crearCardDinamica(
                "Fondos disponibles", lblFondosDisponibles, "Saldo disponible para usar", true));

        // Swapped descriptions so dynamic label matches "Pagos de pedidos activos"
        panelContenido.add(crearCardEarningsPagosDinamico(
                "Pagos futuros", lblPagosActivos, "Pagos de pedidos activos", "USD 0.00", "Pagos procesándose"));

        panelContenido.add(crearCardEarningsPagosDinamico(
                "Ingresos y gastos", lblIngresosTotales, "Ingresos hasta la fecha", "USD 0.00",
                "Gastos hasta la fecha"));

        JPanel wrapperContenido = new JPanel(new BorderLayout());
        wrapperContenido.setOpaque(false);
        wrapperContenido.add(panelContenido, BorderLayout.NORTH);
        panelVistaEarnings.add(wrapperContenido, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(panelVistaEarnings);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getViewport().setBackground(Estilos.COLOR_FONDO_GRIS);
        return scrollPane;
    }

    private JPanel crearCardDinamica(String titulo, JLabel lblMonto, String descripcion, boolean esVerde) {
        JPanel panel = new JPanel();
        panel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panel.setBorder(Estilos.BORDE_TARJETA);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblDesc.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblMonto.setFont(Estilos.FUENTE_TITULO_H2);
        lblMonto.setForeground(esVerde ? Estilos.COLOR_VERDE_PRIMARIO : Estilos.COLOR_TEXTO_PRINCIPAL);
        lblMonto.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        panel.add(lblDesc);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        panel.add(lblMonto);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    /**
     * Crea una tarjeta específica para la vista de Ingresos (con 2 secciones)
     * usando JLabels dinámicos.
     */
    private JPanel crearCardEarningsPagosDinamico(String titulo, JLabel lblMonto1, String desc1, String monto2,
            String desc2) {
        JPanel panel = new JPanel();
        panel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panel.setBorder(Estilos.BORDE_TARJETA);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        JLabel lblDesc1 = new JLabel(desc1);
        lblDesc1.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblDesc1.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
        lblDesc1.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblMonto1.setFont(Estilos.FUENTE_TITULO_H2);
        lblMonto1.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblMonto1.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblDesc1);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        panel.add(lblMonto1);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        JLabel lblDesc2 = new JLabel(desc2);
        lblDesc2.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblDesc2.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
        lblDesc2.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblMonto2 = new JLabel(monto2);
        lblMonto2.setFont(Estilos.FUENTE_TITULO_H2);
        lblMonto2.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblMonto2.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblDesc2);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        panel.add(lblMonto2);
        panel.add(Box.createVerticalGlue());
        return panel;
    }
}