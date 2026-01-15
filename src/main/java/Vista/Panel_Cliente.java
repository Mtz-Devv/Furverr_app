package Vista;

import Vista.componentes.cliente.PanelFeed;
import Vista.componentes.cliente.Panel_Cuenta;
import Vista.componentes.cliente.Panel_Pedido;
import Vista.componentes.Panel_Mensajes;
import modelos.Usuario;
import database.GestorDeDatos;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class Panel_Cliente extends JFrame {

    private Usuario clienteActual;
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private JButton btnMenuUsuario;
    private JButton btnMensajes;
    private Timer timerMensajes;

    // Referencia al PanelFeed para poder filtrar
    private PanelFeed panelFeedRef;

    private final String VISTA_FEED = "FEED";
    private final String VISTA_CUENTA = "CUENTA";

    public Panel_Cliente(Usuario user) {
        this.clienteActual = user;

        setTitle("Furrver - Feed de Ilustradores");
        setSize(1200, 800);
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        Color colorFondoGeneral = new Color(245, 245, 245);
        getContentPane().setBackground(colorFondoGeneral);

        // INIZIALIZACION DEL FEED
        panelFeedRef = new PanelFeed();
        panelFeedRef.setParentContext(this);
        panelFeedRef.setCliente(this.clienteActual);
        panelFeedRef.cargarFeed("");

        // Header y Contenedores
        add(crearHeader(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(colorFondoGeneral);

        // Feed
        JPanel panelVistaFeed = crearVistaFeedPrincipal(colorFondoGeneral);
        mainContainer.add(panelVistaFeed, VISTA_FEED);

        // Cuenta
        Panel_Cuenta panelCuenta = new Panel_Cuenta(
                clienteActual,
                () -> cardLayout.show(mainContainer, VISTA_FEED),
                () -> actualizarAvatarHeader());

        mainContainer.add(panelCuenta, VISTA_CUENTA);

        add(mainContainer, BorderLayout.CENTER);

        // Iniciar timer para badge
        timerMensajes = new Timer(5000, e -> actualizarBadgeMensajes());
        timerMensajes.start();
        actualizarBadgeMensajes();

        setVisible(true);
    }

    @Override
    public void dispose() {
        if (timerMensajes != null)
            timerMensajes.stop();
        super.dispose();
    }

    private JPanel crearVistaFeedPrincipal(Color bg) {
        JPanel view = new JPanel();
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        view.setBackground(bg);
        view.setBorder(new EmptyBorder(20, 40, 20, 40));

        JLabel lblBienvenida = new JLabel("Bienvenido a Furrver, " + clienteActual.getNombreUsuario());
        lblBienvenida.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblBienvenida.setForeground(Color.GRAY);
        lblBienvenida.setAlignmentX(Component.LEFT_ALIGNMENT);
        view.add(lblBienvenida);
        view.add(Box.createVerticalStrut(10));

        view.add(crearPanelEtiquetas());
        view.add(Box.createVerticalStrut(20));

        panelFeedRef.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelFeedRef.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        view.add(panelFeedRef);

        return view;
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 40, 15, 40));
        header.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel lblLogo = new JLabel("Furrver.");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblLogo.setPreferredSize(new Dimension(120, 50));

        // --- SEARCH BAR FUNCIONAL ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setOpaque(false);

        String placeholder = "¿Qué servicio buscas hoy?";
        JTextField txtSearch = new JTextField(placeholder);
        txtSearch.setPreferredSize(new Dimension(400, 40));
        txtSearch.setBorder(new RoundedBorder(20));
        txtSearch.setForeground(Color.GRAY);

        // Listener Enter
        txtSearch.addActionListener(e -> {
            String termino = txtSearch.getText().trim();
            if (termino.equals(placeholder))
                termino = "";
            panelFeedRef.cargarFeed(termino);
        });

        // Listener Placeholder
        txtSearch.addFocusListener(new java.awt.event.FocusListener() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().equals(placeholder)) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText(placeholder);
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        JButton btnSearch = new JButton("Buscar");
        Estilos.estilizarBotonPrimario(btnSearch);
        btnSearch.setPreferredSize(new Dimension(80, 40));
        btnSearch.addActionListener(e -> {
            String termino = txtSearch.getText().trim();
            if (termino.equals(placeholder))
                termino = "";
            panelFeedRef.cargarFeed(termino);
        });

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        // --- ICONOS / MENU USUARIO ---
        JPanel iconsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        iconsPanel.setOpaque(false);

        // BOTON MENSAJES
        btnMensajes = new JButton("✉");
        btnMensajes.setFont(new Font("SansSerif", Font.BOLD, 20));
        btnMensajes.setForeground(Color.GRAY);
        btnMensajes.setContentAreaFilled(false);
        btnMensajes.setBorder(null);
        btnMensajes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMensajes.addActionListener(e -> new Panel_Mensajes(this, clienteActual).setVisible(true));

        iconsPanel.add(btnMensajes);

        btnMenuUsuario = new JButton();
        btnMenuUsuario.setPreferredSize(new Dimension(40, 40));

        btnMenuUsuario.setIcon(crearIconoAvatarPeque(40));

        btnMenuUsuario.setContentAreaFilled(false);
        btnMenuUsuario.setBorder(null);
        btnMenuUsuario.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnMenuUsuario.setFocusPainted(false);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem itemCuenta = new JMenuItem("Mi Cuenta");
        JMenuItem itemPedidos = new JMenuItem("Mis Pedidos");
        JMenuItem itemLogout = new JMenuItem("Cerrar Sesión");

        itemCuenta.addActionListener(e -> cardLayout.show(mainContainer, VISTA_CUENTA));
        itemPedidos.addActionListener(e -> new Panel_Pedido(clienteActual).setVisible(true));
        itemLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Salir?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new Autenticador();
            }
        });

        popupMenu.add(itemCuenta);
        popupMenu.add(itemPedidos);
        popupMenu.addSeparator();
        popupMenu.add(itemLogout);

        btnMenuUsuario.addActionListener(e -> popupMenu.show(btnMenuUsuario, 0, btnMenuUsuario.getHeight()));
        iconsPanel.add(btnMenuUsuario);

        header.add(lblLogo, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.CENTER);
        header.add(iconsPanel, BorderLayout.EAST);

        return header;
    }

    private void actualizarBadgeMensajes() {
        int count = GestorDeDatos.contarMensajesNoLeidos(clienteActual.getNombreUsuario());
        if (count > 0) {
            btnMensajes.setText("✉ (" + count + ")");
            btnMensajes.setForeground(Estilos.COLOR_VERDE_PRIMARIO);
        } else {
            btnMensajes.setText("✉");
            btnMensajes.setForeground(Color.GRAY);
        }
    }

    // ACTUALIZA PP
    public void actualizarAvatarHeader() {
        if (btnMenuUsuario != null) {
            btnMenuUsuario.setIcon(crearIconoAvatarPeque(40));
            btnMenuUsuario.repaint();
        }
    }

    private JPanel crearPanelEtiquetas() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] tags = { "Anime", "Pixel Art", "Logo", "Furry", "Retratos", "Paisajes" };
        for (String t : tags) {
            JButton b = new JButton(t);
            b.setFocusPainted(false);
            b.setBackground(Color.WHITE);
            b.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            b.addActionListener(e -> panelFeedRef.cargarFeed(t)); // Filtrar al clic
            p.add(b);
        }
        return p;
    }

    // Clase Borde Redondo interna
    private static class RoundedBorder extends AbstractBorder {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        public Insets getBorderInsets(Component c, Insets insets) {
            return getBorderInsets(c);
        }
    }

    /**
     * Genera un icono redondo para el botón del menú.
     * Si hay foto, la usa. Si no, usa la inicial.
     */
    private Icon crearIconoAvatarPeque(int size) {
        BufferedImage imagenFinal = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagenFinal.createGraphics();

        // Suavizado para que el círculo no se vea pixelado
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 1. Intentar cargar la imagen del usuario
        Image imgUsuario = null;
        if (clienteActual.getRutaFotoPerfil() != null && !clienteActual.getRutaFotoPerfil().isEmpty()) {
            try {
                File f = new File(clienteActual.getRutaFotoPerfil());
                if (f.exists()) {
                    imgUsuario = new ImageIcon(f.getAbsolutePath()).getImage();
                }
            } catch (Exception e) {
            }
        }

        // 2. Dibujar
        if (imgUsuario != null) {
            // Si hay imagen: Hacemos el recorte circular (Clip)
            g2.setClip(new Ellipse2D.Float(0, 0, size, size));
            g2.drawImage(imgUsuario, 0, 0, size, size, null);
        } else {
            // Si NO hay imagen: Dibujamos círculo negro con inicial blanca
            g2.setColor(Color.BLACK);
            g2.fillOval(0, 0, size, size);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, size / 2));
            String letra = clienteActual.getNombreUsuario().substring(0, 1).toUpperCase();

            FontMetrics fm = g2.getFontMetrics();
            int x = (size - fm.stringWidth(letra)) / 2;
            int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(letra, x, y);
        }

        g2.dispose();
        return new ImageIcon(imagenFinal);
    }
}