// Archivo: Vista/componentes/HeaderPanel.java
// (VERSIÓN CORREGIDA FINAL CON JMenu y Mensajes)

package Vista.componentes;

import Vista.Autenticador;
import Vista.Estilos;
import Vista.Panel_Ilustrador;
import modelos.Ilustrador;
import database.GestorDeDatos; // Importar GestorDeDatos

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Esta clase construye ÚNICAMENTE el panel del header (la barra de navegación).
 */
public class HeaderPanel {

    // --- Referencias ---
    private Panel_Ilustrador panelPrincipal;
    private Ilustrador ilustrador;
    private JPanel panel;

    // --- Variables de estado del menú ---
    private String selectedLanguage = "Español";
    private String selectedCurrency = "USD - US$";

    // --- Variables para Mensajes ---
    private JButton btnInbox;
    private Timer timerInbox;

    /**
     * Constructor que construye el panel del header.
     */
    public HeaderPanel(Panel_Ilustrador panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        this.ilustrador = panelPrincipal.getIlustrador();

        // Creamos el panel en el constructor
        this.panel = crearPanelHeader();

        // Iniciar timer para badge de mensajes
        timerInbox = new Timer(5000, e -> actualizarBadgeInbox());
        timerInbox.start();
        actualizarBadgeInbox(); // Primera llamada
    }

    /**
     * Método público para que Panel_Ilustrador obtenga el panel construido.
     */
    public JPanel getPanel() {
        return this.panel;
    }

    // ==================================================================
    // --- MÉTODOS DE CONSTRUCCIÓN DEL HEADER ---
    // ==================================================================

    private JPanel crearPanelHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        headerPanel.setPreferredSize(new Dimension(1100, 60));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblLogo = new JLabel("Furverr.");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 28));
        leftPanel.add(lblLogo);

        leftPanel.add(Box.createHorizontalStrut(20));

        JButton btnDashboard = createHeaderButton("Panel");
        btnDashboard.addActionListener(e -> {
            panelPrincipal.getMainCardLayout().show(panelPrincipal.getMainCardsPanel(),
                    Panel_Ilustrador.VISTA_DASHBOARD);
        });
        leftPanel.add(btnDashboard);

        leftPanel.add(crearMenuBoton("Mi Negocio ▾", new String[] {
                "Pedidos", "Servicios", "Perfil", "Ingresos"
        }));

        leftPanel.add(crearMenuBoton("Estadísticas ▾", new String[] {
                "Resumen de pedidos", "Repetir negocios"
        }));

        headerPanel.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        rightPanel.add(crearMenuNotificaciones());
        rightPanel.add(crearMenuInbox());

        rightPanel.add(crearMenuPerfil());

        headerPanel.add(rightPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createHeaderButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(new Color(100, 100, 100));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(new Color(26, 177, 77));
            }

            public void mouseExited(MouseEvent evt) {
                // Si es el botón de inbox y tiene mensajes, mantener verde, si no gris
                if (button == btnInbox && btnInbox.getText().contains("(")) {
                    button.setForeground(Estilos.COLOR_VERDE_PRIMARIO);
                } else {
                    button.setForeground(new Color(100, 100, 100));
                }
            }
        });
        return button;
    }

    private JButton crearMenuBoton(String text, String[] items) {
        JButton menuButton = createHeaderButton(text);
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(new LineBorder(Estilos.COLOR_BORDE, 1));

        for (String itemText : items) {
            JMenuItem menuItem = new JMenuItem(itemText);

            menuItem.setFont(Estilos.FUENTE_TEXTO_REGULAR);
            menuItem.setBackground(Estilos.COLOR_FONDO_BLANCO);
            menuItem.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
            menuItem.setOpaque(true);
            menuItem.setBorder(new EmptyBorder(5, 15, 5, 15));

            menuItem.addActionListener(e -> {
                CardLayout cl = panelPrincipal.getMainCardLayout();
                JPanel cp = panelPrincipal.getMainCardsPanel();

                switch (itemText) {
                    case "Perfil":
                        cl.show(cp, Panel_Ilustrador.VISTA_PERFIL);
                        break;
                    case "Pedidos":
                        cl.show(cp, Panel_Ilustrador.VISTA_ORDERS);
                        break;
                    case "Servicios":
                        cl.show(cp, Panel_Ilustrador.VISTA_GIGS);
                        break;
                    case "Ingresos":
                        cl.show(cp, Panel_Ilustrador.VISTA_EARNINGS);
                        break;
                    case "Resumen de pedidos":
                        cl.show(cp, Panel_Ilustrador.VISTA_ANALYTICS_OVERVIEW);
                        break;
                    case "Repetir negocios":
                        cl.show(cp, Panel_Ilustrador.VISTA_ANALYTICS_REPEAT);
                        break;
                    default:
                        JOptionPane.showMessageDialog(panelPrincipal, "WIP: Mostrar " + itemText);
                        break;
                }
            });

            popupMenu.add(menuItem);
        }

        menuButton.addActionListener(e -> {
            popupMenu.show(menuButton, 0, menuButton.getHeight());
        });
        return menuButton;
    }

    private JButton crearMenuPerfil() {
        String fotoPath = ilustrador.getRutaFotoPerfil();
        JButton profileButton = new JButton();

        if (fotoPath != null && !fotoPath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(fotoPath);
                Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                profileButton.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                profileButton.setText(ilustrador.getNombreUsuario().substring(0, 1).toUpperCase());
            }
        } else {
            profileButton.setText(ilustrador.getNombreUsuario().substring(0, 1).toUpperCase());
        }

        profileButton.setPreferredSize(new Dimension(32, 32));
        profileButton.setBorderPainted(false);
        profileButton.setFocusPainted(false);
        profileButton.setContentAreaFilled(false);
        profileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(new LineBorder(Estilos.COLOR_BORDE, 1));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel lblNombre = new JLabel(ilustrador.getNombreUsuario());
        lblNombre.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblNombre.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        userInfoPanel.add(lblNombre);

        JLabel lblCorreo = new JLabel(ilustrador.getCorreo());
        lblCorreo.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
        lblCorreo.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        userInfoPanel.add(lblCorreo);

        popupMenu.add(userInfoPanel);
        popupMenu.addSeparator();

        String[] items = {
                "Perfil", "Configuración de cuenta", "Facturación y pagos", "Recomendar a un amigo", "Cerrar sesión"
        };

        for (String itemText : items) {
            JMenuItem menuItem = new JMenuItem(itemText);

            menuItem.setFont(Estilos.FUENTE_TEXTO_REGULAR);
            menuItem.setBackground(Estilos.COLOR_FONDO_BLANCO);
            menuItem.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
            menuItem.setOpaque(true);
            menuItem.setBorder(new EmptyBorder(5, 15, 5, 15));

            menuItem.addActionListener(e -> {
                CardLayout cl = panelPrincipal.getMainCardLayout();
                JPanel cp = panelPrincipal.getMainCardsPanel();

                switch (itemText) {
                    case "Perfil":
                        cl.show(cp, Panel_Ilustrador.VISTA_PERFIL);
                        break;
                    case "Configuración de cuenta":
                        cl.show(cp, Panel_Ilustrador.VISTA_ACCOUNT_SETTINGS);
                        break;
                    case "Facturación y pagos":
                        cl.show(cp, Panel_Ilustrador.VISTA_BILLING_PAYMENTS);
                        break;
                    case "Cerrar sesión":
                        panelPrincipal.dispose();
                        new Autenticador();
                        break;
                    default:
                        JOptionPane.showMessageDialog(panelPrincipal, "WIP: Mostrar " + itemText);
                        break;
                }
            });
            popupMenu.add(menuItem);
        }

        popupMenu.addSeparator();

        // IDIOMA
        JMenu menuIdioma = new JMenu("Idioma: " + selectedLanguage);
        menuIdioma.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        menuIdioma.setBackground(Estilos.COLOR_FONDO_BLANCO);
        menuIdioma.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        menuIdioma.setOpaque(true);
        menuIdioma.setBorder(new EmptyBorder(5, 15, 5, 15));

        String[] languages = { "English", "Español" };
        for (String lang : languages) {
            JMenuItem langItem = new JMenuItem(lang);
            langItem.setFont(Estilos.FUENTE_TEXTO_REGULAR);
            langItem.setBackground(Estilos.COLOR_FONDO_BLANCO);
            langItem.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
            if (lang.equals(selectedLanguage)) {
                langItem.setText("✓ " + lang);
                langItem.setFont(Estilos.FUENTE_TEXTO_BOLD);
            }
            langItem.addActionListener(e -> {
                selectedLanguage = lang;
                menuIdioma.setText("Idioma: " + selectedLanguage);
                JOptionPane.showMessageDialog(panelPrincipal, "Idioma cambiado a: " + selectedLanguage, "Idioma",
                        JOptionPane.INFORMATION_MESSAGE);
                popupMenu.setVisible(false);
            });
            menuIdioma.add(langItem);
        }
        popupMenu.add(menuIdioma);

        // MONEDA
        JMenu menuMoneda = new JMenu("Moneda: " + selectedCurrency);
        menuMoneda.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        menuMoneda.setBackground(Estilos.COLOR_FONDO_BLANCO);
        menuMoneda.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        menuMoneda.setOpaque(true);
        menuMoneda.setBorder(new EmptyBorder(5, 15, 5, 15));

        Map<String, String> currencies = new LinkedHashMap<>();
        currencies.put("USD - US$", "Dólar estadounidense");
        currencies.put("MXN - $", "Peso Mexicano");
        currencies.put("EUR - €", "Euro");
        currencies.put("GBP - £", "Libra esterlina");
        currencies.put("AUD - A$", "Dólar australiano");
        currencies.put("CAD - CA$", "Dólar canadiense");

        for (Map.Entry<String, String> entry : currencies.entrySet()) {
            String code = entry.getKey();
            String name = entry.getValue();
            String labelText = "<html><body>" + name + "<br><font color='gray'>" + code + "</font></body></html>";
            String selectedLabelText = "<html><body><b>✓ " + name + "</b><br><font color='gray'>" + code
                    + "</font></body></html>";

            JMenuItem currencyItem = new JMenuItem();
            currencyItem.setFont(Estilos.FUENTE_TEXTO_REGULAR);
            currencyItem.setBackground(Estilos.COLOR_FONDO_BLANCO);
            currencyItem.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
            currencyItem.setOpaque(true);
            currencyItem.setBorder(new EmptyBorder(5, 15, 5, 15));
            if (code.equals(selectedCurrency)) {
                currencyItem.setText(selectedLabelText);
            } else {
                currencyItem.setText(labelText);
            }

            currencyItem.addActionListener(e -> {
                selectedCurrency = code;
                menuMoneda.setText("Moneda: " + selectedCurrency);
                JOptionPane.showMessageDialog(panelPrincipal, "Moneda cambiada a: " + selectedCurrency, "Moneda",
                        JOptionPane.INFORMATION_MESSAGE);
                popupMenu.setVisible(false);
            });
            menuMoneda.add(currencyItem);
        }
        popupMenu.add(menuMoneda);

        profileButton.addActionListener(e -> {
            popupMenu.show(profileButton, -popupMenu.getPreferredSize().width + profileButton.getWidth(),
                    profileButton.getHeight() + 5);
        });

        return profileButton;
    }

    private JButton crearMenuNotificaciones() {
        JButton btnNotificaciones = createHeaderButton("\uD83D\uDD14");
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Estilos.COLOR_FONDO_BLANCO);
        popupMenu.setBorder(new LineBorder(Estilos.COLOR_BORDE, 1));
        popupMenu.add(crearPanelVacioNotificaciones());
        btnNotificaciones.addActionListener(e -> {
            popupMenu.show(btnNotificaciones, -popupMenu.getPreferredSize().width + btnNotificaciones.getWidth(),
                    btnNotificaciones.getHeight() + 5);
        });
        return btnNotificaciones;
    }

    private JButton crearMenuInbox() {
        btnInbox = createHeaderButton("\u2709");
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(Estilos.COLOR_FONDO_BLANCO);
        popupMenu.setBorder(new LineBorder(Estilos.COLOR_BORDE, 1));
        // popupMenu.add(crearPanelInbox(popupMenu)); // Se agrega dinámicamente
        btnInbox.addActionListener(e -> {
            popupMenu.removeAll();
            popupMenu.add(crearPanelInbox(popupMenu));
            popupMenu.show(btnInbox, -popupMenu.getPreferredSize().width + btnInbox.getWidth(),
                    btnInbox.getHeight() + 5);
        });
        return btnInbox;
    }

    private JPanel crearPanelVacioNotificaciones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panel.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, Estilos.ESPACIO_MEDIANO, Estilos.ESPACIO_MEDIANO,
                Estilos.ESPACIO_MEDIANO));
        panel.setPreferredSize(new Dimension(350, 400));
        JLabel lblTitle = new JLabel("Notificaciones (0)");
        lblTitle.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE * 2));
        JPanel centerContent = new JPanel();
        centerContent.setOpaque(false);
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        JLabel lblIcon = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/dog.png"));
            lblIcon.setIcon(icon);
        } catch (Exception e) {
            lblIcon.setText("");
        }
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblMainText = new JLabel("No hay notificaciones... aún");
        lblMainText.setFont(Estilos.FUENTE_TITULO_H3);
        lblMainText.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblMainText.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblSubText = new JLabel(
                "<html><body style='text-align: center; width: 250px;'>Cuanto más hagas ahí fuera, más verás aquí dentro.</body></html>");
        lblSubText.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblSubText.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblSubText.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerContent.add(lblIcon);
        centerContent.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        centerContent.add(lblMainText);
        centerContent.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        centerContent.add(lblSubText);
        panel.add(centerContent);
        panel.add(Box.createVerticalGlue());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setOpaque(false);
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        footer.add(new JLabel("🔊 ⚙️"));
        panel.add(footer);
        return panel;
    }

    private JPanel crearPanelInbox(JPopupMenu popup) {
        JPanel panel = new JPanel(new BorderLayout(0, Estilos.ESPACIO_PEQUENO));
        panel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panel.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, Estilos.ESPACIO_MEDIANO, Estilos.ESPACIO_PEQUENO,
                Estilos.ESPACIO_MEDIANO));
        panel.setPreferredSize(new Dimension(350, 400));

        JLabel lblTitle = new JLabel("Bandeja de entrada");
        lblTitle.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        panel.add(lblTitle, BorderLayout.NORTH);

        JPanel centerContent = new JPanel();
        centerContent.setOpaque(false);
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));

        java.util.List<String> conversaciones = GestorDeDatos.obtenerConversaciones(ilustrador.getNombreUsuario());

        if (conversaciones.isEmpty()) {
            centerContent.add(Box.createVerticalGlue());
            JLabel lblIcon = new JLabel();
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/img/cat.png"));
                lblIcon.setIcon(icon);
            } catch (Exception e) {
                lblIcon.setText("");
            }
            lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel lblMainText = new JLabel("No hay mensajes... aún");
            lblMainText.setFont(Estilos.FUENTE_TITULO_H3);
            lblMainText.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
            lblMainText.setAlignmentX(Component.CENTER_ALIGNMENT);
            JLabel lblSubText = new JLabel(
                    "<html><body style='text-align: center;'>Todo empieza con un <i>hola</i>.</body></html>");
            lblSubText.setFont(Estilos.FUENTE_TEXTO_REGULAR);
            lblSubText.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
            lblSubText.setAlignmentX(Component.CENTER_ALIGNMENT);
            centerContent.add(lblIcon);
            centerContent.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
            centerContent.add(lblMainText);
            centerContent.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
            centerContent.add(lblSubText);
            centerContent.add(Box.createVerticalGlue());
        } else {
            // Mostrar ultimas 5 conversaciones
            int count = 0;
            for (String user : conversaciones) {
                if (count >= 5)
                    break;
                modelos.Usuario u = GestorDeDatos.obtenerUsuario(user);
                if (u != null) {
                    centerContent.add(crearItemInbox(u, popup));
                    centerContent.add(Box.createVerticalStrut(5));
                }
                count++;
            }
            centerContent.add(Box.createVerticalGlue());
        }

        panel.add(centerContent, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        JPanel footerLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerLeft.setOpaque(false);
        footerLeft.add(new JLabel("🔊 ⚙️"));
        JLabel lblSeeAll = new JLabel("Ver todo en la Bandeja de entrada");
        lblSeeAll.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblSeeAll.setForeground(Estilos.COLOR_TEXTO_LINK);
        lblSeeAll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        lblSeeAll.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popup.setVisible(false);
                new Panel_Mensajes(panelPrincipal, ilustrador).setVisible(true);
            }
        });

        footer.add(footerLeft, BorderLayout.WEST);
        footer.add(lblSeeAll, BorderLayout.EAST);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearItemInbox(modelos.Usuario u, JPopupMenu popup) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setOpaque(false);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(10, 5, 10, 5)));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblFoto = new JLabel();
        String initial = "?";
        String nombre = (u.getNombreMostrado() != null && !u.getNombreMostrado().isEmpty()) ? u.getNombreMostrado()
                : u.getNombreUsuario();
        if (nombre != null && !nombre.isEmpty()) {
            initial = nombre.substring(0, 1).toUpperCase();
        }
        lblFoto.setText(initial);
        lblFoto.setPreferredSize(new Dimension(35, 35));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.LIGHT_GRAY);
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setForeground(Color.WHITE);
        lblFoto.setFont(Estilos.FUENTE_TEXTO_BOLD);

        if (u.getRutaFotoPerfil() != null && !u.getRutaFotoPerfil().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(u.getRutaFotoPerfil());
                Image img = icon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
                lblFoto.setText("");
            } catch (Exception e) {
            }
        }

        JLabel lblNombre = new JLabel(u.getNombreMostrado() != null ? u.getNombreMostrado() : u.getNombreUsuario());
        lblNombre.setFont(Estilos.FUENTE_TEXTO_BOLD);

        item.add(lblFoto, BorderLayout.WEST);
        item.add(lblNombre, BorderLayout.CENTER);

        item.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                popup.setVisible(false);
                new Vista.componentes.VentanaChat(panelPrincipal, ilustrador, u).setVisible(true);
            }
        });

        return item;
    }

    private void actualizarBadgeInbox() {
        int count = GestorDeDatos.contarMensajesNoLeidos(ilustrador.getNombreUsuario());
        if (count > 0) {
            btnInbox.setText("\u2709 (" + count + ")");
            btnInbox.setForeground(Estilos.COLOR_VERDE_PRIMARIO);
        } else {
            btnInbox.setText("\u2709");
            btnInbox.setForeground(new Color(100, 100, 100));
        }
    }
}