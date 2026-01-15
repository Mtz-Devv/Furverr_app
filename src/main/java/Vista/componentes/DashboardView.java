// Archivo: Vista/componentes/DashboardView.java
package Vista.componentes;

import Vista.Estilos;
import Vista.ModernScrollBarUI;
import Vista.Panel_Ilustrador;
import modelos.Ilustrador;
import database.GestorDeDatos;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Esta clase construye ÚNICAMENTE el panel del Dashboard.
 */
public class DashboardView {

    // --- Referencias ---
    private Panel_Ilustrador panelPrincipal; // Para la navegación
    private Ilustrador ilustrador;
    private JScrollPane panel; // El panel que esta clase construye

    // --- Componentes dinámicos ---
    private JLabel lblGanancias;
    private JPanel panelOrdenes;

    /**
     * Constructor que construye el panel del Dashboard.
     * 
     * @param panelPrincipal La ventana principal (para navegar)
     */
    public DashboardView(Panel_Ilustrador panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        this.ilustrador = panelPrincipal.getIlustrador();

        // Creamos el panel en el constructor
        this.panel = crearPanelDashboard();
    }

    /**
     * Método público para que Panel_Ilustrador obtenga el panel construido.
     */
    public JScrollPane getPanel() {
        return this.panel;
    }

    /**
     * Actualiza los datos del dashboard (Ganancias y Tabla de Pedidos).
     */
    public void actualizarDashboard() {
        // 1. Actualizar Ganancias
        double ganancias = GestorDeDatos.obtenerGananciasTotales(ilustrador.getNombreUsuario());
        if (lblGanancias != null) {
            lblGanancias.setText("USD " + ganancias);
        }

        // 2. Actualizar Tabla de Ordenes
        if (panelOrdenes != null) {
            cargarOrdenes();
        }
    }

    // ==================================================================
    // --- MÉTODOS DE CONSTRUCCIÓN DEL DASHBOARD ---
    // ==================================================================

    public JScrollPane crearPanelDashboard() {
        JPanel dashboardPanel = new JPanel(new BorderLayout(Estilos.ESPACIO_GRANDE * 2, Estilos.ESPACIO_GRANDE));
        dashboardPanel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        dashboardPanel.setBorder(Estilos.PADDING_PANEL_GRIS);
        dashboardPanel.add(crearPanelDashboardIzquierdo(), BorderLayout.WEST);
        dashboardPanel.add(crearPanelDashboardDerecho(), BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(dashboardPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getViewport().setBackground(Estilos.COLOR_FONDO_BLANCO);
        return scrollPane;
    }

    private JPanel crearPanelDashboardIzquierdo() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(250, 1));

        panel.add(crearCardDashboardPerfil());
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        panel.add(crearCardDashboardNivel());
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        panel.add(crearCardDashboardGanancias());
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        panel.add(crearCardDashboardInbox());
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel crearPanelDashboardDerecho() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        String nombreMostrado = ilustrador.getNombreMostrado();
        if (nombreMostrado == null || nombreMostrado.isEmpty()) {
            nombreMostrado = ilustrador.getNombreUsuario();
        }
        JLabel lblWelcome = new JLabel("Bienvenido, " + nombreMostrado + ".");
        lblWelcome.setFont(Estilos.FUENTE_TITULO_H2);
        lblWelcome.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblSubWelcome = new JLabel("Encuentra mensajes importantes, consejos y enlaces a recursos útiles aquí:");
        lblSubWelcome.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblSubWelcome.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblSubWelcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblWelcome);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        panel.add(lblSubWelcome);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        panel.add(crearCardDashboardOrdenesActivas());
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel crearCardDashboardPerfil() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel panelCentro = new JPanel();
        panelCentro.setOpaque(false);
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        JLabel lblFoto = new JLabel();
        String fotoPath = ilustrador.getRutaFotoPerfil();
        if (fotoPath != null && !fotoPath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(fotoPath);
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                lblFoto.setText("[Sin Foto]");
            }
        } else {
            lblFoto.setText("[Sin Foto]");
        }
        lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFoto.setPreferredSize(new Dimension(100, 100));
        lblFoto.setBorder(new LineBorder(Estilos.COLOR_BORDE));
        String nombreMostrado = ilustrador.getNombreMostrado();
        if (nombreMostrado == null || nombreMostrado.isEmpty()) {
            nombreMostrado = ilustrador.getNombreUsuario();
        }
        JLabel lblNombre = new JLabel(nombreMostrado);
        lblNombre.setFont(Estilos.FUENTE_TITULO_H3);
        lblNombre.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblUsername = new JLabel("@" + ilustrador.getNombreUsuario());
        lblUsername.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblUsername.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton btnViewProfile = new JButton("Ver perfil");
        Estilos.estilizarBotonSecundario(btnViewProfile);
        btnViewProfile.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnViewProfile.addActionListener(e -> panelPrincipal.getMainCardLayout()
                .show(panelPrincipal.getMainCardsPanel(), Panel_Ilustrador.VISTA_PERFIL));
        panelCentro.add(lblFoto);
        panelCentro.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        panelCentro.add(lblNombre);
        panelCentro.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        panelCentro.add(lblUsername);
        panelCentro.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        panelCentro.add(btnViewProfile);
        panel.add(panelCentro, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearCardDashboardNivel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panel.setBorder(Estilos.BORDE_TARJETA);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(0, 0, 0, 0));
        JLabel lblTitle = new JLabel("Resumen de nivel");
        lblTitle.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        content.add(lblTitle);
        content.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        content.add(crearFilaDashboard("Mi nivel", "Nivel 0"));
        content.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        content.add(crearFilaDashboard("Tasa de éxito", "-"));
        content.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        content.add(crearFilaDashboard("Valoración", "★ -"));
        content.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        content.add(crearFilaDashboard("Tasa de respuesta", "-"));
        content.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        JButton btnProgress = new JButton("Ver progreso");
        Estilos.estilizarBotonSecundario(btnProgress);

        btnProgress.addActionListener(e -> {
            panelPrincipal.getMainCardLayout().show(panelPrincipal.getMainCardsPanel(),
                    Panel_Ilustrador.VISTA_LEVEL_OVERVIEW);
        });

        content.add(btnProgress);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearFilaDashboard(String key, String value) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        JLabel lblKey = new JLabel(key);
        lblKey.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblKey.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblValue.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        fila.add(lblKey, BorderLayout.WEST);
        fila.add(lblValue, BorderLayout.EAST);
        return fila;
    }

    private JPanel crearCardDashboardGanancias() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panel.setBorder(Estilos.BORDE_TARJETA);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(0, 0, 0, 0));
        JLabel lblKey = new JLabel("Ganado en Noviembre");
        lblKey.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblKey.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);

        // Calcular ganancias totales
        double ganancias = GestorDeDatos.obtenerGananciasTotales(ilustrador.getNombreUsuario());
        lblGanancias = new JLabel("USD " + ganancias);

        lblGanancias.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblGanancias.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        content.add(lblKey, BorderLayout.WEST);
        content.add(lblGanancias, BorderLayout.EAST);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearCardDashboardInbox() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panel.setBorder(Estilos.BORDE_TARJETA);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(0, 0, 0, 0));
        JLabel lblKey = new JLabel("Bandeja de entrada");
        lblKey.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblKey.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        JLabel lblValue = new JLabel("Ver todo");
        lblValue.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblValue.setForeground(Estilos.COLOR_TEXTO_LINK);
        content.add(lblKey, BorderLayout.WEST);
        content.add(lblValue, BorderLayout.EAST);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearCardDashboardOrdenesActivas() {
        panelOrdenes = new JPanel(new BorderLayout(10, Estilos.ESPACIO_MEDIANO));
        panelOrdenes.setOpaque(false);
        panelOrdenes.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelOrdenes.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, 0, 0));

        cargarOrdenes();

        return panelOrdenes;
    }

    private void cargarOrdenes() {
        panelOrdenes.removeAll();

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        // Obtenemos los datos REALES
        Object[][] data = GestorDeDatos.getPedidosActivos(ilustrador.getNombreUsuario());
        // Columnas: ID (0), COMPRADOR (1), SERVICIO (2), VENCE EL (3), TOTAL (4),
        // ESTADO (5)
        String[] columnNames = { "ID", "COMPRADOR", "SERVICIO", "VENCE EL", "TOTAL", "ESTADO" };

        // Calcular ganancias totales (para el título, aunque sea redundante con el
        // widget)
        double ganancias = GestorDeDatos.obtenerGananciasTotales(ilustrador.getNombreUsuario());

        JLabel lblTitle = new JLabel("Pedidos activos - " + data.length + " ($" + ganancias + ")");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H3);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        header.add(lblTitle, BorderLayout.WEST);
        header.add(new JLabel("Pedidos activos (" + data.length + ") ▾"), BorderLayout.EAST);
        panelOrdenes.add(header, BorderLayout.NORTH);

        // Si no hay datos, mostramos el placeholder
        if (data.length == 0) {
            panelOrdenes.add(ComponentUtils.crearPanelTablaWIP(
                    null,
                    columnNames,
                    "No hay pedidos activos que mostrar."), BorderLayout.CENTER);
        } else {
            // --- Si SÍ hay datos, creamos la tabla real ---
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Hacemos que la tabla no sea editable
                }
            };

            JTable table = new JTable(model);

            // Ocultar la columna ID (índice 0)
            table.getColumnModel().getColumn(0).setMinWidth(0);
            table.getColumnModel().getColumn(0).setMaxWidth(0);
            table.getColumnModel().getColumn(0).setWidth(0);

            // Estilos de la tabla
            table.setFont(Estilos.FUENTE_TEXTO_REGULAR);
            table.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
            table.setRowHeight(25);
            table.getTableHeader().setFont(Estilos.FUENTE_TEXTO_BOLD);
            table.getTableHeader().setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
            table.getTableHeader().setBackground(Estilos.COLOR_FONDO_GRIS);
            table.getTableHeader().setBorder(new LineBorder(Estilos.COLOR_BORDE));
            table.setGridColor(Estilos.COLOR_BORDE);
            table.setSelectionBackground(Estilos.COLOR_VERDE_PRIMARIO.brighter());
            table.setSelectionForeground(Estilos.COLOR_TEXTO_PRINCIPAL);

            // Listener para clics
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = table.rowAtPoint(evt.getPoint());
                    if (row >= 0) {
                        String estado = (String) table.getValueAt(row, 5); // Columna 5 es ESTADO
                        if ("PENDIENTE".equals(estado)) {
                            int confirm = JOptionPane.showConfirmDialog(panelOrdenes,
                                    "¿Deseas marcar este pedido como TERMINADO?",
                                    "Actualizar Estado",
                                    JOptionPane.YES_NO_OPTION);

                            if (confirm == JOptionPane.YES_OPTION) {
                                int idPedido = (int) table.getValueAt(row, 0); // Columna 0 es ID
                                boolean exito = GestorDeDatos.actualizarEstadoPedido(idPedido, "TERMINADO");
                                if (exito) {
                                    JOptionPane.showMessageDialog(panelOrdenes, "Pedido actualizado correctamente.");
                                    actualizarDashboard(); // <--- REFRESCAR DATOS
                                    panelPrincipal.actualizarEarningsView(); // <--- REFRESCAR VISTA DE INGRESOS
                                } else {
                                    JOptionPane.showMessageDialog(panelOrdenes, "Error al actualizar el pedido.",
                                            "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                }
            });

            // Añadimos la tabla a un ScrollPane
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.getViewport().setBackground(Estilos.COLOR_FONDO_BLANCO);
            scrollPane.setBorder(BorderFactory.createLineBorder(Estilos.COLOR_BORDE));

            panelOrdenes.add(scrollPane, BorderLayout.CENTER);
        }

        panelOrdenes.revalidate();
        panelOrdenes.repaint();
    }
}