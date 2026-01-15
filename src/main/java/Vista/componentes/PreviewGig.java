// Archivo: Vista/componentes/PreviewGig.java

package Vista.componentes;

import Vista.Estilos;
import Vista.ModernScrollBarUI;
import Vista.Panel_Ilustrador;
import database.GestorDeDatos;
import modelos.GigExtra;
import modelos.Ilustracion;
import modelos.Ilustrador;
import modelos.Paquete;
import modelos.Faq;
import modelos.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PreviewGig extends JDialog {

    private Ilustracion gig;
    private Ilustrador ilustrador;
    private Usuario clienteComprador;
    private Panel_Ilustrador ventanaPrincipal;

    // Datos
    private List<String> galeriaImagenes;
    private List<Paquete> paquetes;
    private List<GigExtra> extras;
    private List<Faq> faqs;

    // Componentes UI
    private JLabel lblMainImage;
    private JPanel panelThumbnails;
    private int currentImageIndex = 0;

    // Variables de Compra
    private JButton btnContinue;
    private double precioBasePaquete = 0;
    private double precioExtras = 0;
    private int cantidad = 1;
    private int diasEntregaBase = 3;

    // Constantes de diseño
    private final int ANCHO_CARRUSEL = 650;
    private final int ALTO_MAX_CARRUSEL = 450;
    private final int ANCHO_SIDEBAR = 320;

    public PreviewGig(Ilustracion gig, Ilustrador ilustrador, Panel_Ilustrador ventanaPrincipal, Usuario cliente) {
        super(ventanaPrincipal, "Vista Previa: " + gig.getTitulo(), false);

        this.gig = gig;
        this.ilustrador = ilustrador;
        this.ventanaPrincipal = ventanaPrincipal;
        this.clienteComprador = cliente;

        // Cargar datos
        this.galeriaImagenes = GestorDeDatos.obtenerGaleriaGig(gig.getId());
        this.paquetes = GestorDeDatos.obtenerPaquetesGig(gig.getId());
        this.extras = GestorDeDatos.obtenerExtrasGig(gig.getId());
        this.faqs = GestorDeDatos.obtenerFaqsGig(gig.getId());

        setSize(1200, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        add(crearHeaderNavegacion(), BorderLayout.NORTH);

        JPanel mainWrapper = new JPanel(new BorderLayout());
        mainWrapper.setBackground(Color.WHITE);
        JPanel contentContainer = new JPanel(new GridBagLayout());
        contentContainer.setBackground(Color.WHITE);
        contentContainer.setBorder(new EmptyBorder(30, 40, 50, 40));

        GridBagConstraints gbc = new GridBagConstraints();

        // COLUMNA IZQUIERDA
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 0, 20);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(crearHeaderGig());
        leftPanel.add(Box.createVerticalStrut(25));
        leftPanel.add(crearCarruselImagenes());
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(crearSeccionDescripcion());
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(crearSeccionMetadata());
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(crearSeccionFaq());
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(crearSeccionTags());
        leftPanel.add(Box.createVerticalStrut(50));

        contentContainer.add(leftPanel, gbc);

        // COLUMNA DERECHA (Sidebar Fijo)
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel rightPanelWrapper = new JPanel(new BorderLayout());
        rightPanelWrapper.setBackground(Color.WHITE);
        rightPanelWrapper.setPreferredSize(new Dimension(ANCHO_SIDEBAR, 600));

        rightPanelWrapper.add(crearSidebarPrecios(), BorderLayout.NORTH);

        contentContainer.add(rightPanelWrapper, gbc);

        JPanel aligner = new JPanel(new BorderLayout());
        aligner.setBackground(Color.WHITE);
        aligner.add(contentContainer, BorderLayout.NORTH);
        mainWrapper.add(aligner, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(mainWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel crearSidebarPrecios() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(new LineBorder(new Color(220, 220, 220), 1));

        Paquete pack = paquetes.isEmpty() ? new Paquete("Basic", "Standard", "Desc", 3, 1, gig.getPrecio())
                : paquetes.get(0);
        precioBasePaquete = pack.precio;
        diasEntregaBase = pack.entregaDias;

        JPanel headerPack = new JPanel(new BorderLayout());
        headerPack.setBackground(new Color(250, 250, 250));
        headerPack.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                new EmptyBorder(15, 20, 15, 20)));

        JLabel lblPackName = new JLabel(pack.tipo);
        lblPackName.setFont(Estilos.FUENTE_TEXTO_BOLD);
        JLabel lblPackPrice = new JLabel("US$" + pack.precio);
        lblPackPrice.setFont(new Font("SansSerif", Font.BOLD, 18));

        headerPack.add(lblPackName, BorderLayout.WEST);
        headerPack.add(lblPackPrice, BorderLayout.EAST);
        sidebar.add(headerPack);

        JPanel bodyPack = new JPanel();
        bodyPack.setLayout(new BoxLayout(bodyPack, BoxLayout.Y_AXIS));
        bodyPack.setBackground(Color.WHITE);
        bodyPack.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblPackDescName = new JLabel("<html><b>" + pack.nombre + "</b></html>");
        lblPackDescName.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPack.add(lblPackDescName);

        JTextArea descPack = new JTextArea(pack.descripcion);
        descPack.setLineWrap(true);
        descPack.setWrapStyleWord(true);
        descPack.setEditable(false);
        descPack.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        descPack.setAlignmentX(Component.LEFT_ALIGNMENT);
        bodyPack.add(descPack);

        JPanel details = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        details.setBackground(Color.WHITE);
        details.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lDel = new JLabel("\u23F0 " + pack.entregaDias + "-day delivery    ");
        details.add(lDel);
        bodyPack.add(details);
        bodyPack.add(Box.createVerticalStrut(20));

        if (!extras.isEmpty()) {
            JLabel lExt = new JLabel("Add Extras");
            lExt.setFont(Estilos.FUENTE_TEXTO_BOLD);
            lExt.setAlignmentX(Component.LEFT_ALIGNMENT);
            bodyPack.add(lExt);

            for (GigExtra ex : extras) {
                JCheckBox chk = new JCheckBox(ex.titulo + " (+US$" + ex.precio + ")");
                chk.setBackground(Color.WHITE);
                chk.setAlignmentX(Component.LEFT_ALIGNMENT);
                chk.addActionListener(e -> {
                    if (chk.isSelected())
                        precioExtras += ex.precio;
                    else
                        precioExtras -= ex.precio;
                    actualizarTotal();
                });
                bodyPack.add(chk);
            }
            bodyPack.add(Box.createVerticalStrut(20));
        }

        JPanel qtyWrapper = new JPanel(new BorderLayout());
        qtyWrapper.setBackground(Color.WHITE);
        qtyWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        qtyWrapper.setMaximumSize(new Dimension(1000, 30));

        JLabel lQty = new JLabel("Gig Quantity");
        lQty.setFont(Estilos.FUENTE_TEXTO_BOLD);

        JComboBox<Integer> comboQty = new JComboBox<>(new Integer[] { 1, 2, 3, 4, 5, 10 });
        comboQty.setPreferredSize(new Dimension(60, 25));
        comboQty.setBackground(Color.WHITE);
        comboQty.addActionListener(e -> {
            cantidad = (Integer) comboQty.getSelectedItem();
            actualizarTotal();
        });

        qtyWrapper.add(lQty, BorderLayout.WEST);
        qtyWrapper.add(comboQty, BorderLayout.EAST);

        bodyPack.add(qtyWrapper);
        bodyPack.add(Box.createVerticalGlue());

        sidebar.add(bodyPack);

        JPanel footer = new JPanel(new GridLayout(2, 1, 0, 10));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(20, 20, 20, 20));

        btnContinue = new JButton("Order (US$" + pack.precio + ")");
        btnContinue.setBackground(Color.BLACK);
        btnContinue.setForeground(Color.WHITE);
        btnContinue.setFont(Estilos.FUENTE_TEXTO_BOLD);
        btnContinue.setFocusPainted(false);
        btnContinue.setPreferredSize(new Dimension(100, 40));
        btnContinue.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnContinue.addActionListener(e -> realizarPedido());

        JButton btnContact = new JButton("Contact me");
        Estilos.estilizarBotonSecundario(btnContact);
        btnContact.setPreferredSize(new Dimension(100, 40));
        btnContact.addActionListener(e -> abrirChat());

        footer.add(btnContinue);
        footer.add(btnContact);

        sidebar.add(footer);

        return sidebar;
    }

    private void actualizarTotal() {
        double total = (precioBasePaquete * cantidad) + (precioExtras * cantidad);
        btnContinue.setText("Order (US$" + total + ")");
    }

    private void realizarPedido() {
        if (clienteComprador == null) {
            JOptionPane.showMessageDialog(this, "Error: No se ha identificado al usuario comprador.");
            return;
        }

        double totalFinal = (precioBasePaquete * cantidad) + (precioExtras * cantidad);

        String fechaEntrega = LocalDate.now().plusDays(diasEntregaBase)
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Confirmar pedido de '" + gig.getTitulo() + "'?\nTotal: $" + totalFinal + "\nEntrega estimada: "
                        + fechaEntrega,
                "Confirmar Compra", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean exito = GestorDeDatos.crearNuevoPedido(
                    clienteComprador.getNombreUsuario(),
                    gig.getNombreAutor(),
                    gig.getTitulo(),
                    totalFinal,
                    fechaEntrega);

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Pedido realizado con éxito! \nPuedes verlo en 'Mis Pedidos'.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Hubo un error al guardar el pedido en la base de datos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirChat() {
        if (clienteComprador == null) {
            JOptionPane.showMessageDialog(this, "Debes iniciar sesión para chatear.");
            return;
        }
        VentanaChat chat = new VentanaChat(this, clienteComprador, ilustrador);
        chat.setVisible(true);
    }

    private JButton crearBotonNav(String txt) {
        JButton b = new JButton(txt);
        b.setFont(new Font("SansSerif", Font.BOLD, 16));
        b.setBackground(new Color(255, 255, 255));
        b.setBorder(new LineBorder(Color.LIGHT_GRAY));
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(30, 40));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void cambiarImagen(int direction) {
        if (galeriaImagenes.isEmpty())
            return;
        currentImageIndex += direction;
        if (currentImageIndex < 0)
            currentImageIndex = galeriaImagenes.size() - 1;
        if (currentImageIndex >= galeriaImagenes.size())
            currentImageIndex = 0;
        actualizarCarrusel();
    }

    private void actualizarCarrusel() {
        if (galeriaImagenes.isEmpty()) {
            lblMainImage.setText("Sin imágenes");
            return;
        }

        String ruta = galeriaImagenes.get(currentImageIndex);
        ImageIcon icon = new ImageIcon(ruta);

        Image imgOriginal = icon.getImage();
        int origW = icon.getIconWidth();
        int origH = icon.getIconHeight();

        if (origW > 0 && origH > 0) {
            double ratio = (double) origH / origW;
            int newW = ANCHO_CARRUSEL;
            int newH = (int) (ANCHO_CARRUSEL * ratio);

            if (newH > ALTO_MAX_CARRUSEL) {
                newH = ALTO_MAX_CARRUSEL;
                newW = (int) (newH / ratio);
            }

            Image imgEscalada = imgOriginal.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
            lblMainImage.setIcon(new ImageIcon(imgEscalada));
        } else {
            lblMainImage.setText("Error imagen");
        }

        panelThumbnails.removeAll();
        for (int i = 0; i < galeriaImagenes.size(); i++) {
            String r = galeriaImagenes.get(i);
            ImageIcon thIcon = new ImageIcon(r);
            Image thImg = thIcon.getImage().getScaledInstance(80, 50, Image.SCALE_SMOOTH);

            JLabel lblTh = new JLabel(new ImageIcon(thImg));
            lblTh.setPreferredSize(new Dimension(80, 50));
            lblTh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            if (i == currentImageIndex) {
                lblTh.setBorder(new LineBorder(Color.BLACK, 2));
            } else {
                lblTh.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
            }

            int finalI = i;
            lblTh.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    currentImageIndex = finalI;
                    actualizarCarrusel();
                }
            });
            panelThumbnails.add(lblTh);
        }
        panelThumbnails.revalidate();
        panelThumbnails.repaint();
    }

    private JPanel crearHeaderNavegacion() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JButton btnHome = new JButton("\u2302");
        btnHome.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHome.addActionListener(e -> {
            this.dispose();
        });

        String catTexto = (gig.getCategoria() != null) ? gig.getCategoria().replace("/", " / ") : "Servicio";
        JLabel lblBreadcrumb = new JLabel(catTexto);
        lblBreadcrumb.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblBreadcrumb.setForeground(Color.GRAY);

        p.add(btnHome);
        p.add(lblBreadcrumb);
        return p;
    }

    private JPanel crearHeaderGig() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel(
                "<html><body style='width: 600px'><h1>" + gig.getTitulo() + "</h1></body></html>");
        lblTitulo.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pAutor = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pAutor.setBackground(Color.WHITE);
        pAutor.setAlignmentX(Component.LEFT_ALIGNMENT);
        pAutor.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(30, 30));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.LIGHT_GRAY);
        if (ilustrador.getRutaFotoPerfil() != null) {
            try {
                ImageIcon ico = new ImageIcon(ilustrador.getRutaFotoPerfil());
                Image img = ico.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
            } catch (Exception e) {
            }
        }
        lblFoto.setBorder(new LineBorder(new Color(200, 200, 200), 1));

        JLabel lblNombre = new JLabel("<html><b>" + ilustrador.getNombreMostrado() + "</b> | Nivel Nuevo</html>");
        lblNombre.setFont(Estilos.FUENTE_TEXTO_REGULAR);

        pAutor.add(lblFoto);
        pAutor.add(lblNombre);

        p.add(lblTitulo);
        p.add(pAutor);
        return p;
    }

    private JPanel crearCarruselImagenes() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setPreferredSize(new Dimension(ANCHO_CARRUSEL, ALTO_MAX_CARRUSEL + 80));
        p.setMaximumSize(new Dimension(ANCHO_CARRUSEL, ALTO_MAX_CARRUSEL + 80));
        p.setMinimumSize(new Dimension(ANCHO_CARRUSEL, ALTO_MAX_CARRUSEL + 80));

        lblMainImage = new JLabel();
        lblMainImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblMainImage.setBackground(new Color(248, 248, 248));
        lblMainImage.setOpaque(true);
        lblMainImage.setPreferredSize(new Dimension(ANCHO_CARRUSEL, ALTO_MAX_CARRUSEL));
        lblMainImage.setBorder(new LineBorder(new Color(230, 230, 230)));

        JButton btnPrev = crearBotonNav("<");
        JButton btnNext = crearBotonNav(">");
        btnPrev.addActionListener(e -> cambiarImagen(-1));
        btnNext.addActionListener(e -> cambiarImagen(1));

        JPanel pImageWrapper = new JPanel(new BorderLayout());
        pImageWrapper.setPreferredSize(new Dimension(ANCHO_CARRUSEL, ALTO_MAX_CARRUSEL));
        pImageWrapper.add(btnPrev, BorderLayout.WEST);
        pImageWrapper.add(lblMainImage, BorderLayout.CENTER);
        pImageWrapper.add(btnNext, BorderLayout.EAST);

        panelThumbnails = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelThumbnails.setBackground(Color.WHITE);
        panelThumbnails.setPreferredSize(new Dimension(ANCHO_CARRUSEL, 70));

        p.add(pImageWrapper, BorderLayout.CENTER);
        p.add(panelThumbnails, BorderLayout.SOUTH);

        actualizarCarrusel();
        return p;
    }

    private JPanel crearSeccionDescripcion() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("About this gig");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H3);

        JTextArea txtDesc = new JTextArea(gig.getDescripcion());
        txtDesc.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setBorder(null);
        txtDesc.setBackground(Color.WHITE);
        txtDesc.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);

        p.add(lblTitle, BorderLayout.NORTH);
        p.add(Box.createVerticalStrut(15), BorderLayout.CENTER);
        p.add(txtDesc, BorderLayout.SOUTH);
        return p;
    }

    private JPanel crearSeccionMetadata() {
        JPanel p = new JPanel(new GridLayout(1, 3, 20, 0));
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(230, 230, 230)),
                new EmptyBorder(20, 0, 20, 0)));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(ANCHO_CARRUSEL, 80));

        agregarMetaItem(p, "Purpose", "Illustration");
        agregarMetaItem(p, "Art dimension", "2D");
        agregarMetaItem(p, "Style", "Anime/Manga");

        return p;
    }

    private void agregarMetaItem(JPanel p, String title, String val) {
        JPanel item = new JPanel(new BorderLayout(0, 5));
        item.setBackground(Color.WHITE);
        JLabel t = new JLabel(title);
        t.setForeground(Color.GRAY);
        t.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
        JLabel v = new JLabel(val);
        v.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        v.setForeground(Color.BLACK);
        item.add(t, BorderLayout.NORTH);
        item.add(v, BorderLayout.CENTER);
        p.add(item);
    }

    private JPanel crearSeccionFaq() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("FAQ");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H3);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblTitle);
        p.add(Box.createVerticalStrut(15));

        if (faqs.isEmpty()) {
            JLabel l = new JLabel("No hay preguntas frecuentes.");
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(l);
        } else {
            for (Faq f : faqs) {
                JPanel faqItem = new JPanel(new BorderLayout());
                faqItem.setBackground(Color.WHITE);
                faqItem.setAlignmentX(Component.LEFT_ALIGNMENT);
                faqItem.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));

                JLabel q = new JLabel("▼  " + f.pregunta);
                q.setFont(Estilos.FUENTE_TEXTO_BOLD);
                q.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                q.setBorder(new EmptyBorder(15, 0, 15, 0));

                JPanel aPanel = new JPanel(new BorderLayout());
                aPanel.setBackground(new Color(250, 250, 250));
                aPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                aPanel.setVisible(false);

                JTextArea a = new JTextArea(f.respuesta);
                a.setLineWrap(true);
                a.setWrapStyleWord(true);
                a.setEditable(false);
                a.setOpaque(false);
                a.setFont(Estilos.FUENTE_TEXTO_REGULAR);
                aPanel.add(a, BorderLayout.CENTER);

                q.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        boolean visible = !aPanel.isVisible();
                        aPanel.setVisible(visible);
                        q.setText((visible ? "▲  " : "▼  ") + f.pregunta);
                        p.revalidate();
                        p.repaint();
                    }
                });

                faqItem.add(q, BorderLayout.NORTH);
                faqItem.add(aPanel, BorderLayout.CENTER);
                p.add(faqItem);
            }
        }
        return p;
    }

    private JPanel crearSeccionTags() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(ANCHO_CARRUSEL, 200));

        JLabel lblTitle = new JLabel("Related tags:");
        lblTitle.setFont(Estilos.FUENTE_TEXTO_BOLD);
        p.add(lblTitle);

        if (gig.getEtiquetas() != null && !gig.getEtiquetas().isEmpty()) {
            String[] tags = gig.getEtiquetas().split(",");
            for (String tag : tags) {
                JLabel t = new JLabel(tag.trim());
                t.setFont(new Font("SansSerif", Font.PLAIN, 12));
                t.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(Color.LIGHT_GRAY, 1, true),
                        new EmptyBorder(5, 10, 5, 10)));
                t.setForeground(Color.DARK_GRAY);
                p.add(t);
            }
        }
        return p;
    }
}