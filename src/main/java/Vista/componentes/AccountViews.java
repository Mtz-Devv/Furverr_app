// Archivo: Vista/componentes/AccountViews.java
package Vista.componentes;

import Vista.Estilos;
import Vista.ModernScrollBarUI;
import Vista.Panel_Ilustrador;
import modelos.Ilustrador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Esta clase construye los paneles de:
 * 1. Configuración de Cuenta
 * 2. Facturación y Pagos
 * 3. Resumen de Nivel
 */
public class AccountViews {

    // --- Referencias ---
    private Panel_Ilustrador panelPrincipal;
    private Ilustrador ilustrador;

    public AccountViews(Panel_Ilustrador panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        this.ilustrador = panelPrincipal.getIlustrador();
    }

    // ==================================================================
    // --- MÉTODOS PÚBLICOS PARA OBTENER LOS PANELES ---
    // ==================================================================
    
    public JScrollPane getPanelAccountSettings() {
        return crearPanelAccountSettings();
    }
    
    public JScrollPane getPanelBillingAndPayments() {
        return crearPanelBillingAndPayments();
    }
    
    public JScrollPane getPanelLevelOverview() {
        return crearPanelLevelOverview();
    }

    // ==================================================================
    // --- MÉTODOS DE CONSTRUCCIÓN ---
    // ==================================================================

    private JScrollPane crearPanelAccountSettings() {
        JPanel panelVistaAccount = new JPanel(new BorderLayout(20, 20));
        panelVistaAccount.setBackground(Estilos.COLOR_FONDO_GRIS);
        panelVistaAccount.setBorder(Estilos.PADDING_PANEL_GRIS);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        JPanel panelTitle = new JPanel();
        panelTitle.setOpaque(false);
        panelTitle.setLayout(new BoxLayout(panelTitle, BoxLayout.Y_AXIS));
        JLabel lblTitle = new JLabel("Configuración de cuenta");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H1);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        String email = ilustrador.getCorreo();
        String emailOculto = email.replaceAll("(?<=.{2}).(?=.*@)", "*");
        JLabel lblEmail = new JLabel(emailOculto);
        lblEmail.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblEmail.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        panelTitle.add(lblTitle);
        panelTitle.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        panelTitle.add(lblEmail);
        panelHeader.add(panelTitle, BorderLayout.WEST);
        JLabel lblGoToProfile = new JLabel("Ir al perfil");
        lblGoToProfile.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblGoToProfile.setForeground(Estilos.COLOR_TEXTO_LINK);
        lblGoToProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblGoToProfile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Esta llamada usa el 'panelPrincipal' de ESTA clase
                panelPrincipal.getMainCardLayout().show(panelPrincipal.getMainCardsPanel(), Panel_Ilustrador.VISTA_PERFIL);
            }
        });
        JPanel panelLink = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelLink.setOpaque(false);
        panelLink.add(lblGoToProfile);
        panelHeader.add(panelLink, BorderLayout.EAST);
        panelVistaAccount.add(panelHeader, BorderLayout.NORTH);

        JPanel panelContent = new JPanel(new GridLayout(2, 2, Estilos.ESPACIO_GRANDE, Estilos.ESPACIO_GRANDE));
        panelContent.setOpaque(false);
        panelContent.setBorder(new EmptyBorder(Estilos.ESPACIO_GRANDE, 0, 0, 0));
        panelContent.add(crearCardAccountSetting("Información Personal", "\uD83D\uDC64", "Actualiza tu nombre, correo, visibilidad y estado de la cuenta."));
        panelContent.add(crearCardAccountSetting("Seguridad de la cuenta", "\uD83D\uDD12", "Actualiza tu contraseña y gestiona ajustes adicionales de seguridad."));
        panelContent.add(crearCardAccountSetting("Notificaciones", "\uD83D\uDD14", "Selecciona qué notificaciones quieres recibir y cómo."));
        panelContent.add(crearCardAccountSetting("Información Personal y de Negocio", "\u2699", "Ayuda a fiverr a mantener un mercado seguro y confiable."));
        JPanel wrapperContenido = new JPanel(new BorderLayout());
        wrapperContenido.setOpaque(false);
        wrapperContenido.add(panelContent, BorderLayout.NORTH);
        panelVistaAccount.add(wrapperContenido, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(panelVistaAccount);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getViewport().setBackground(Estilos.COLOR_FONDO_GRIS);
        return scrollPane;
    }

    private JPanel crearCardAccountSetting(String title, String icon, String description) {
        JPanel card = new JPanel(new BorderLayout(Estilos.ESPACIO_MEDIANO, 0));
        card.setBackground(Estilos.COLOR_FONDO_BLANCO);
        card.setBorder(Estilos.BORDE_TARJETA);
        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("SansSerif", Font.PLAIN, 36));
        lblIcon.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblIcon.setVerticalAlignment(SwingConstants.TOP);
        card.add(lblIcon, BorderLayout.WEST);
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        JLabel lblDescription = new JLabel("<html><p style='width:250px;'>" + description + "</p></html>");
        lblDescription.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblDescription.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        textPanel.add(lblTitle);
        textPanel.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        textPanel.add(lblDescription);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private JScrollPane crearPanelBillingAndPayments() {
        JPanel panelVistaBilling = new JPanel(new BorderLayout(20, 20));
        panelVistaBilling.setBackground(Estilos.COLOR_FONDO_GRIS);
        panelVistaBilling.setBorder(Estilos.PADDING_PANEL_GRIS);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Facturación y pagos");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H1);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        panelHeader.add(lblTitle, BorderLayout.NORTH);
        panelVistaBilling.add(panelHeader, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Estilos.FUENTE_TEXTO_BOLD);
        tabbedPane.setBackground(Estilos.COLOR_FONDO_GRIS);
        tabbedPane.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        tabbedPane.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, 0, 0));
        JPanel billingHistoryPanel = new JPanel(new BorderLayout(Estilos.ESPACIO_MEDIANO, Estilos.ESPACIO_MEDIANO));
        billingHistoryPanel.setOpaque(false);
        billingHistoryPanel.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, 0, 0));
        JLabel lblHistoryTitle = new JLabel("Historial de facturación");
        lblHistoryTitle.setFont(Estilos.FUENTE_TITULO_H3);
        lblHistoryTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        billingHistoryPanel.add(lblHistoryTitle, BorderLayout.NORTH);
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(new EmptyBorder(Estilos.ESPACIO_PEQUENO, 0, 0, 0));
        
        JButton btnDateRange = ComponentUtils.crearBotonTab("Rango de fechas ▾");
        Estilos.estilizarBotonSecundario(btnDateRange); 
        btnDateRange.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        
        JButton btnDocument = ComponentUtils.crearBotonTab("Documento ▾");
        Estilos.estilizarBotonSecundario(btnDocument); 
        btnDocument.setFont(Estilos.FUENTE_TEXTO_REGULAR);

        JButton btnCurrency = ComponentUtils.crearBotonTab("Moneda ▾");
        Estilos.estilizarBotonSecundario(btnCurrency); 
        btnCurrency.setFont(Estilos.FUENTE_TEXTO_REGULAR);

        JTextField txtSearch = new JTextField(" Buscar por factura o nro. de pedido");
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        txtSearch.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        txtSearch.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        JButton btnDownloadReport = new JButton(" Descargar reporte");
        btnDownloadReport.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        Estilos.estilizarBotonSecundario(btnDownloadReport);
        filterPanel.add(btnDateRange);
        filterPanel.add(btnDocument);
        filterPanel.add(btnCurrency);
        filterPanel.add(Box.createHorizontalGlue());
        filterPanel.add(txtSearch);
        filterPanel.add(btnDownloadReport);
        JPanel wrapperFilter = new JPanel(new BorderLayout());
        wrapperFilter.setOpaque(false);
        wrapperFilter.add(filterPanel, BorderLayout.CENTER);
        billingHistoryPanel.add(wrapperFilter, BorderLayout.CENTER);
        
        billingHistoryPanel.add(ComponentUtils.crearPanelTablaWIP(
            null,
            new String[]{"FECHA", "DESCRIPCIÓN", "MONTO", "FACTURA"},
            "No hay actividad de facturación para mostrar."
        ), BorderLayout.SOUTH);
        
        tabbedPane.addTab("Historial de facturación", billingHistoryPanel);
        
        // ***** INICIO DE LA CORRECCIÓN *****
        // Argumentos invertidos: (Título, Componente)
        tabbedPane.addTab("Métodos de pago", ComponentUtils.crearPanelPlaceholderWIP("Métodos de pago"));
        tabbedPane.addTab("Información de facturación", ComponentUtils.crearPanelPlaceholderWIP("Información de facturación"));
        // ***** FIN DE LA CORRECCIÓN *****
        
        panelVistaBilling.add(tabbedPane, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(panelVistaBilling);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getViewport().setBackground(Estilos.COLOR_FONDO_GRIS);
        return scrollPane;
    }

    private JScrollPane crearPanelLevelOverview() {
        JPanel panelVistaLevel = new JPanel(new BorderLayout(20, 20));
        panelVistaLevel.setBackground(Estilos.COLOR_FONDO_GRIS);
        panelVistaLevel.setBorder(Estilos.PADDING_PANEL_GRIS);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Resumen de Nivel");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H1);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        panelHeader.add(lblTitle, BorderLayout.NORTH);
        panelVistaLevel.add(panelHeader, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel();
        panelContenido.setOpaque(false);
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, 0, 0));
        panelContenido.add(crearCardNivelPrincipal());
        panelContenido.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        JPanel panelMetricas = new JPanel(new GridLayout(0, 3, Estilos.ESPACIO_GRANDE, Estilos.ESPACIO_GRANDE));
        panelMetricas.setOpaque(false);
        panelMetricas.add(crearCardNivelMetrica("Tasa de éxito", "-", "Mantenido durante 60 días", "100%"));
        panelMetricas.add(crearCardNivelMetrica("Valoración", "★ -", "Total", "4.2"));
        panelMetricas.add(crearCardNivelMetrica("Tasa de respuesta", "-", "Mantenido durante 60 días", "90%"));
        panelMetricas.add(crearCardNivelMetrica("Pedidos completados", "-", "Total", "10"));
        panelMetricas.add(crearCardNivelMetrica("Ingresos", "$0", "Total", "$50"));
        panelMetricas.add(crearCardNivelMetrica("Días sin advertencias", "-", "Total", "30"));
        panelContenido.add(panelMetricas);
        panelContenido.add(Box.createVerticalGlue());
        panelVistaLevel.add(panelContenido, BorderLayout.CENTER);
        
        JScrollPane scrollPane = new JScrollPane(panelVistaLevel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getViewport().setBackground(Estilos.COLOR_FONDO_GRIS);
        return scrollPane;
    }

    private JPanel crearCardNivelPrincipal() {
        JPanel card = new JPanel();
        card.setBackground(Estilos.COLOR_FONDO_BLANCO);
        card.setBorder(Estilos.BORDE_TARJETA);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblTitle = new JLabel("Nivel 0");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H2);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblDesc = new JLabel("Siguiente nivel: Nivel 1");
        lblDesc.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblDesc.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(10);
        progressBar.setStringPainted(true);
        progressBar.setString("10%");
        progressBar.setForeground(Estilos.COLOR_VERDE_PRIMARIO);
        progressBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        progressBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        card.add(lblDesc);
        card.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        card.add(progressBar);
        return card;
    }

    private JPanel crearCardNivelMetrica(String titulo, String valor, String desc, String objetivo) {
        JPanel card = new JPanel();
        card.setBackground(Estilos.COLOR_FONDO_BLANCO);
        card.setBorder(Estilos.BORDE_TARJETA);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(Estilos.FUENTE_TITULO_H2);
        lblValor.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
        lblDesc.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblObjetivo = new JLabel("Objetivo: " + objetivo);
        lblObjetivo.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
        lblObjetivo.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblObjetivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        card.add(lblDesc);
        card.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        card.add(lblValor);
        card.add(Box.createVerticalGlue());
        card.add(lblObjetivo);
        return card;
    }
}