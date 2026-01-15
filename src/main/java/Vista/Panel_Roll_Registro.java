/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import modelos.Usuario;
import database.GestorDeDatos;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class Panel_Roll_Registro extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardsPanel;
    private Usuario usuario;
    private File fotoPerfilSeleccionada;
    private JLabel lblPreviewFoto;

    // Nombres de los paneles (Ilustrador)
    private static final String PANEL_ROL = "PanelRol";
    private static final String PANEL_READY = "PanelReady";
    private static final String PANEL_SUCCESSFUL = "PanelSuccessful";
    private static final String PANEL_STEER_CLEAR = "PanelSteerClear";
    private static final String PANEL_PERSONAL_INFO = "PanelPersonalInfo";

    // Nombres de los paneles (Cliente)
    private static final String PANEL_CLIENTE_INTENCION = "PanelClienteIntencion";
    private static final String PANEL_CLIENTE_EXPLORANDO = "PanelClienteExplorando";
    private static final String PANEL_CLIENTE_PROYECTO = "PanelClienteProyecto";

    // --- NUEVO: Bordes reutilizables para el efecto hover ---
    private static final int RADIO_BORDE = 25;

    private static final Border BORDE_GRIS_REDONDO = BorderFactory.createCompoundBorder(
            new RoundBorder(Color.LIGHT_GRAY, 1, RADIO_BORDE),
            new EmptyBorder(20, 20, 20, 20));

    private static final Border BORDE_VERDE_REDONDO = BorderFactory.createCompoundBorder(
            new RoundBorder(new Color(26, 177, 77), 2, RADIO_BORDE),
            new EmptyBorder(19, 19, 19, 19) // 1px menos por el grosor
    );
    // --- FIN NUEVO ---

    public Panel_Roll_Registro(Usuario user) {
        this.usuario = user;

        setTitle("Completa tu registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 750));
        setSize(1000, 750);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        cardsPanel.add(crearPanelRol(), PANEL_ROL);

        // Paneles Flujo Ilustrador (Sin cambios)
        cardsPanel.add(crearPanelInformativo(
                "¿Listo para empezar a vender en Furverr?",
                "Aquí está el resumen:",
                new String[] { "Aprende qué hace a un perfil exitoso", "Crea tu perfil de vendedor",
                        "Publica tu Gig (Servicio)" },
                PANEL_SUCCESSFUL, true), PANEL_READY);
        cardsPanel.add(crearPanelInformativo(
                "¿Qué hace a un perfil de Furverr exitoso?",
                "Tu primera impresión importa. Crea un perfil que destaque.",
                new String[] { "Tómate tu tiempo en crear tu perfil", "Añade credibilidad enlazando redes",
                        "Describe tus habilidades con precisión", "¡Ponle una cara a tu nombre!",
                        "Verifica tu ID por seguridad" },
                PANEL_STEER_CLEAR, true), PANEL_SUCCESSFUL);
        cardsPanel.add(crearPanelInformativo(
                "Ahora, hablemos de lo que quieres evitar.",
                "Tu éxito en Furverr es importante para nosotros. Evita lo siguiente:",
                new String[] { "Proveer información falsa o inexacta", "Abrir cuentas duplicadas",
                        "Solicitar trabajo a otros miembros", "Pedir pagos o comunicación fuera de Furverr" },
                PANEL_PERSONAL_INFO, true), PANEL_STEER_CLEAR);
        cardsPanel.add(crearPanelPersonalInfo(), PANEL_PERSONAL_INFO);

        // Paneles Flujo Cliente Interactivo
        cardsPanel.add(crearPanelClienteIntencion(), PANEL_CLIENTE_INTENCION);
        cardsPanel.add(crearPanelClienteExplorando(), PANEL_CLIENTE_EXPLORANDO);
        cardsPanel.add(crearPanelClienteProyecto(), PANEL_CLIENTE_PROYECTO);

        add(cardsPanel);
        cardLayout.show(cardsPanel, PANEL_ROL);
        setVisible(true);
    }

    private JButton crearBotonVolver() {
        JButton btnVolver = new JButton("←");
        btnVolver.setFont(new Font("SansSerif", Font.BOLD, 22));
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVolver.setBorderPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setMargin(new Insets(0, 0, 15, 0));
        return btnVolver;
    }

    private JPanel crearPanelImagenIzquierda() {
        ImageIcon bgIcon = null;
        try {
            String rutaImagen = "/img/furross.png";
            bgIcon = new ImageIcon(getClass().getResource(rutaImagen));
        } catch (Exception e) {
            System.err.println("Error al cargar imagen izquierda: " + e.getMessage());
        }
        final Image bgImage = (bgIcon != null) ? bgIcon.getImage() : null;
        JPanel izq = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(230, 230, 230));
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.GRAY);
                    g.drawString("[Imagen Izquierda]", getWidth() / 2 - 50, getHeight() / 2);
                }
            }
        };
        izq.setOpaque(false);
        return izq;
    }

    // --- MODIFICADO: PanelRol ahora usa las nuevas tarjetas ---
    private JPanel crearPanelRol() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(50, 80, 50, 80));

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setOpaque(false);

        JLabel lblTitulo = new JLabel("<html><body style='text-align: center;'>" + usuario.getNombreUsuario()
                + ", tu cuenta ha sido creada! ¿Qué te trae por aquí?</body></html>");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblSubtitulo = new JLabel(
                "<html><body style='text-align: center;'>Adaptaremos tu experiencia a tus necesidades</body></html>");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        panelCentro.add(lblTitulo);
        panelCentro.add(Box.createVerticalStrut(10));
        panelCentro.add(lblSubtitulo);
        panelCentro.add(Box.createVerticalStrut(40));

        // --- Se eliminan JRadioButtons y ButtonGroup ---
        // --- Definimos las acciones de clic ---
        Runnable accionCliente = () -> {
            GestorDeDatos.actualizarRolUsuario(usuario.getNombreUsuario(), "cliente");
            cardLayout.show(cardsPanel, PANEL_CLIENTE_INTENCION);
        };

        Runnable accionIlustrador = () -> {
            this.usuario = GestorDeDatos.actualizarRolUsuario(usuario.getNombreUsuario(), "ilustrador");
            cardLayout.show(cardsPanel, PANEL_READY);
        };

        // --- Creamos las tarjetas interactivas ---
        JPanel panelOpcionCliente = crearPanelOpcion(
                "Soy un cliente",
                "/img/customer.png",
                "Quiero contratar ilustradores.",
                accionCliente);

        JPanel panelOpcionIlustrador = crearPanelOpcion(
                "Soy un ilustrador",
                "/img/ilustrator.png",
                "Quiero vender mis servicios.",
                accionIlustrador);

        JPanel panelOpciones = new JPanel(new GridLayout(1, 2, 20, 20));
        panelOpciones.setOpaque(false);
        panelOpciones.add(panelOpcionCliente);
        panelOpciones.add(panelOpcionIlustrador);

        panelCentro.add(panelOpciones);
        panel.add(panelCentro, BorderLayout.CENTER);

        // --- Se elimina el panel de navegación (botón "Siguiente") ---
        return panel;
    }

    // --- MODIFICADO: crearPanelOpcion (para Cliente/Ilustrador) ---
    // Ahora incluye texto hover y maneja el clic
    private JPanel crearPanelOpcion(String titulo, String iconPath, String descripcion, Runnable onClickAction) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.setBorder(BORDE_GRIS_REDONDO); // Borde por defecto

        // Icono
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
            JLabel lblIcon = new JLabel(new ImageIcon(img));
            lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(lblIcon, BorderLayout.CENTER);
        } catch (Exception e) {
            JLabel lblIcon = new JLabel("Icono");
            lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(lblIcon, BorderLayout.CENTER);
        }

        // --- Panel Sur (para Título y Descripción) ---
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setOpaque(false);
        southPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Descripción (oculta por defecto)
        JLabel lblDescripcion = new JLabel(descripcion, SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDescripcion.setForeground(Color.GRAY);
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDescripcion.setVisible(false); // Oculta

        // Título
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        southPanel.add(lblDescripcion); // Descripción (oculta)
        southPanel.add(Box.createVerticalStrut(5));
        southPanel.add(lblTitulo); // Título

        panel.add(southPanel, BorderLayout.SOUTH);

        // --- Listener para Hover y Clic ---
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickAction.run(); // Ejecuta la acción de clic
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblDescripcion.setVisible(true); // Muestra descripción
                panel.setBorder(BORDE_VERDE_REDONDO); // Borde verde
                // Mover imagen un poco (opcional)
                // lblIcon.setBorder(new EmptyBorder(0, 0, 5, 0)); // Mueve 5px abajo
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblDescripcion.setVisible(false); // Oculta descripción
                panel.setBorder(BORDE_GRIS_REDONDO); // Borde gris
                // lblIcon.setBorder(null); // Restaura
            }
        });

        return panel;
    }

    // Panel Informativo (Ilustrador) - Sin cambios
    private JPanel crearPanelInformativo(String titulo, String subtitulo, String[] items, String panelSiguiente,
            boolean showBackButton) {
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2));
        panelPrincipal.add(crearPanelImagenIzquierda());
        JPanel panelDerecho = new JPanel(new BorderLayout(20, 20));
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setBorder(new EmptyBorder(30, 40, 30, 40));
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelNorte.setOpaque(false);
        if (showBackButton) {
            JButton btnVolver = crearBotonVolver();
            btnVolver.addActionListener(e -> cardLayout.previous(cardsPanel));
            panelNorte.add(btnVolver);
        }
        panelDerecho.add(panelNorte, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("<html><body style='width: 350px;'>" + titulo + "</body></html>");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblSubtitulo = new JLabel("<html><body style='width: 350px;'>" + subtitulo + "</body></html>");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.DARK_GRAY);
        lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lblTitulo);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(lblSubtitulo);
        contentPanel.add(Box.createVerticalStrut(25));
        for (String item : items) {
            JLabel lblItem = new JLabel("<html><body style='width: 350px;'>• " + item + "</body></html>");
            lblItem.setFont(new Font("SansSerif", Font.PLAIN, 14));
            lblItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(lblItem);
            contentPanel.add(Box.createVerticalStrut(10));
        }
        contentPanel.add(Box.createVerticalGlue());
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelSur.setOpaque(false);
        JButton btnContinue = new JButton("Continuar");
        btnContinue.setBackground(new Color(26, 177, 77));
        btnContinue.setForeground(Color.BLACK);
        btnContinue.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnContinue.setFocusPainted(false);
        btnContinue.addActionListener(e -> cardLayout.show(cardsPanel, panelSiguiente));
        panelSur.add(btnContinue);
        contentPanel.add(panelSur);
        panelDerecho.add(contentPanel, BorderLayout.CENTER);
        panelPrincipal.add(panelDerecho);
        return panelPrincipal;
    }

    // --- MODIFICADO: crearPanelOpcionCliente (para el flujo de cliente) ---
    // Ahora incluye texto hover y maneja el clic.
    // Se elimina el parámetro JRadioButton.
    private JPanel crearPanelOpcionCliente(String titulo, String iconPath, String descripcion, Runnable onClickAction) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.setBorder(BORDE_GRIS_REDONDO); // Borde por defecto

        // --- Se elimina el panel del RadioButton ---
        // Panel de Contenido (Título, Descripción, Imagen)
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10)); // 10px de espacio vertical
        contentPanel.setOpaque(false); // Transparente

        // Título (Arriba)
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setBorder(new EmptyBorder(10, 0, 0, 0)); // Espacio arriba
        contentPanel.add(lblTitulo, BorderLayout.NORTH);

        // Descripción (Centro, oculta)
        JLabel lblDescripcion = new JLabel(descripcion, SwingConstants.CENTER);
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblDescripcion.setForeground(Color.GRAY);
        lblDescripcion.setVisible(false); // Oculta
        contentPanel.add(lblDescripcion, BorderLayout.CENTER);

        // Icono (Abajo)
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
            JLabel lblIcon = new JLabel(new ImageIcon(img));
            lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(lblIcon, BorderLayout.SOUTH);
        } catch (Exception e) {
            JLabel lblIcon = new JLabel("");
            lblIcon.setFont(new Font("SansSerif", Font.PLAIN, 16));
            lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
            contentPanel.add(lblIcon, BorderLayout.SOUTH);
            System.err.println("Error cargando icono: " + iconPath);
        }

        panel.add(contentPanel, BorderLayout.CENTER);

        // --- Listener para Hover y Clic ---
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClickAction.run(); // Ejecuta la acción de clic
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblDescripcion.setVisible(true); // Muestra descripción
                panel.setBorder(BORDE_VERDE_REDONDO); // Borde verde
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblDescripcion.setVisible(false); // Oculta descripción
                panel.setBorder(BORDE_GRIS_REDONDO); // Borde gris
            }
        });

        return panel;
    }

    // --- MODIFICADO: Panel de 3 opciones (Intención) ---
    // Ahora usa las nuevas tarjetas y no tiene botón "Siguiente"
    private JPanel crearPanelClienteIntencion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(50, 80, 50, 80));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("¡Bien! ¿Que estas buecando?");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblSubtitulo = new JLabel("Estamos aqui para ayudarte a lograrlo.");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(lblTitulo);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(lblSubtitulo);
        contentPanel.add(Box.createVerticalStrut(40));

        JPanel panelOpciones = new JPanel(new GridLayout(1, 3, 20, 20));
        panelOpciones.setOpaque(false);

        // --- Se eliminan ButtonGroup, Listas de bordes, ItemListener ---
        // --- Definimos las acciones de clic ---
        Runnable accionProyecto = () -> cardLayout.show(cardsPanel, PANEL_CLIENTE_PROYECTO);

        Runnable accionServicio = () -> {
            new Panel_Cliente(usuario); // Acción final
            dispose();
        };

        Runnable accionExplorar = () -> cardLayout.show(cardsPanel, PANEL_CLIENTE_EXPLORANDO);

        // --- Creamos las tarjetas ---
        JPanel cardProyecto = crearPanelOpcionCliente(
                "Empezar un proyecto",
                "/img/start_proj.png",
                "Solicitar algo que necesite de varios servicios.",
                accionProyecto);

        JPanel cardServicio = crearPanelOpcionCliente(
                "Servicio especifico",
                "/img/spec_serv.png",
                "Buscar un ilustrador para una tarea específica.",
                accionServicio);

        JPanel cardExplorar = crearPanelOpcionCliente(
                "Solo explorando",
                "/img/just_ex.png",
                "Ver el trabajo de los artistas.",
                accionExplorar);

        panelOpciones.add(cardProyecto);
        panelOpciones.add(cardServicio);
        panelOpciones.add(cardExplorar);

        contentPanel.add(panelOpciones);
        panel.add(contentPanel, BorderLayout.CENTER);

        // --- Panel de Navegación (Solo "Atrás") ---
        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setOpaque(false);

        JButton btnVolver = crearBotonVolver();
        btnVolver.addActionListener(e -> cardLayout.show(cardsPanel, PANEL_ROL));
        panelNav.add(btnVolver, BorderLayout.WEST);

        // --- Se elimina el botón "Siguiente" ---
        panel.add(panelNav, BorderLayout.SOUTH);

        return panel;
    }

    // --- MODIFICADO: Panel "Explorando" ---
    // Ahora usa las nuevas tarjetas y no tiene botón "Siguiente"
    private JPanel crearPanelClienteExplorando() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(50, 80, 50, 80));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("¡Genial! ¿Qué te gustaría explorar?");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblSubtitulo = new JLabel("Cuéntanos qué te interesa ver.");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(lblTitulo);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(lblSubtitulo);
        contentPanel.add(Box.createVerticalStrut(40));

        JPanel panelOpciones = new JPanel(new GridLayout(1, 2, 20, 20));
        panelOpciones.setOpaque(false);

        // --- Acción final (es la misma para ambas tarjetas) ---
        Runnable accionFinal = () -> {
            new Panel_Cliente(usuario);
            dispose();
        };

        JPanel cardPopulares = crearPanelOpcionCliente(
                "Lo más popular",
                "/img/most_pop.png",
                "Ver los servicios más vendidos.",
                accionFinal);

        JPanel cardNuevos = crearPanelOpcionCliente(
                "Nuevos artistas",
                "/img/new_arts.png",
                "Descubrir talento emergente.",
                accionFinal);

        panelOpciones.add(cardPopulares);
        panelOpciones.add(cardNuevos);
        contentPanel.add(panelOpciones);
        panel.add(contentPanel, BorderLayout.CENTER);

        // --- Navegación (Solo "Atrás") ---
        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setOpaque(false);

        JButton btnVolver = crearBotonVolver();
        btnVolver.addActionListener(e -> cardLayout.show(cardsPanel, PANEL_CLIENTE_INTENCION));
        panelNav.add(btnVolver, BorderLayout.WEST);

        // --- Se elimina el botón "Siguiente" ---
        panel.add(panelNav, BorderLayout.SOUTH);

        return panel;
    }

    // --- MODIFICADO: Panel "Proyecto" ---
    // Ahora usa las nuevas tarjetas y no tiene botón "Siguiente"
    private JPanel crearPanelClienteProyecto() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(50, 80, 50, 80));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel("¿Qué tipo de proyecto tienes en mente?");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(lblTitulo);
        contentPanel.add(Box.createVerticalStrut(40));

        JPanel panelOpciones = new JPanel(new GridLayout(2, 2, 20, 20)); // 2x2
        panelOpciones.setOpaque(false);

        // --- Acción final (es la misma para las 4 tarjetas) ---
        Runnable accionFinal = () -> {
            new Panel_Cliente(usuario);
            dispose();
        };

        JPanel cardGraficos = crearPanelOpcionCliente(
                "Gráficos y Diseño",
                "/img/charactera.png", // (Usa iconos apropiados si tienes)
                "Logos, Arte de Juegos, Web...",
                accionFinal);

        JPanel cardMarketing = crearPanelOpcionCliente(
                "Marketing Digital",
                "/img/porta.png",
                "Redes, SEO, Marcas...",
                accionFinal);

        JPanel cardVideo = crearPanelOpcionCliente(
                "Video y Animación",
                "/img/scenea.png",
                "Edición, Animación 2D/3D...",
                accionFinal);

        JPanel cardNegocios = crearPanelOpcionCliente(
                "Negocios",
                "/img/othera.png",
                "Gestión, Legal, Asistencia...",
                accionFinal);

        panelOpciones.add(cardGraficos);
        panelOpciones.add(cardMarketing);
        panelOpciones.add(cardVideo);
        panelOpciones.add(cardNegocios);
        contentPanel.add(panelOpciones);
        panel.add(contentPanel, BorderLayout.CENTER);

        // --- Navegación (Solo "Atrás") ---
        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setOpaque(false);

        JButton btnVolver = crearBotonVolver();
        btnVolver.addActionListener(e -> cardLayout.show(cardsPanel, PANEL_CLIENTE_INTENCION));
        panelNav.add(btnVolver, BorderLayout.WEST);

        // --- Se elimina el botón "Siguiente" ---
        panel.add(panelNav, BorderLayout.SOUTH);

        return panel;
    }

    // --- Panel Personal Info (Sin cambios, solo para Ilustrador) ---
    private JScrollPane crearPanelPersonalInfo() {

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(30, 0, 30, 0));

        JPanel panelBotonVolver = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBotonVolver.setOpaque(false);
        JButton btnVolver = crearBotonVolver();
        btnVolver.addActionListener(e -> cardLayout.previous(cardsPanel));
        panelBotonVolver.add(btnVolver);
        panelBotonVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelBotonVolver.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelBotonVolver.getPreferredSize().height));
        formPanel.add(panelBotonVolver);

        formPanel.add(Box.createVerticalStrut(10));

        JLabel lblTitulo = new JLabel("Información Personal");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblTitulo);
        formPanel.add(Box.createVerticalStrut(5));

        JLabel lblSub = new JLabel("Cuéntanos un poco sobre ti. Esta información aparecerá en tu perfil público.");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSub.setForeground(Color.DARK_GRAY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblSub);
        formPanel.add(Box.createVerticalStrut(25));

        JPanel panelNombres = crearCampoTextoConPlaceholder("Nombre Completo*", "Nombre", "Apellido");
        panelNombres.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(panelNombres);

        JLabel lblDisplay = new JLabel("Nombre a Mostrar*");
        lblDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);

        String placeholderDisplay = "Tu nombre público (ej. Daniel E.)";
        JTextField txtDisplay = crearJTextFieldConPlaceholder(placeholderDisplay, placeholderDisplay);
        txtDisplay.setEditable(true);
        txtDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(lblDisplay);
        formPanel.add(txtDisplay);
        formPanel.add(Box.createVerticalStrut(20));

        JLabel lblFoto = new JLabel("Foto de Perfil*");
        lblFoto.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblFoto);

        lblPreviewFoto = new JLabel();
        lblPreviewFoto.setPreferredSize(new Dimension(150, 150));
        lblPreviewFoto.setMinimumSize(new Dimension(150, 150));
        lblPreviewFoto.setMaximumSize(new Dimension(150, 150));
        lblPreviewFoto.setBorder(new RoundBorder(Color.LIGHT_GRAY, 1, 10)); // Borde redondo
        lblPreviewFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblPreviewFoto.setText("Haz clic para subir");
        lblPreviewFoto.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblPreviewFoto.setForeground(Color.GRAY);
        lblPreviewFoto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblPreviewFoto.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblPreviewFoto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarFotoPerfil();
            }
        });

        formPanel.add(lblPreviewFoto);
        formPanel.add(Box.createVerticalStrut(20));

        JLabel lblDesc = new JLabel("Descripción* (mín. 150 caracteres)");
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextArea txtDesc = new JTextArea();
        txtDesc.setRows(5);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDesc.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(5, 5, 5, 5)));

        String placeholderDesc = "Comparte un poco sobre tu experiencia laboral, proyectos geniales que has completado y tu área de especialización.";
        txtDesc.setText(placeholderDesc);
        txtDesc.setForeground(Color.GRAY);
        txtDesc.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtDesc.getText().equals(placeholderDesc)) {
                    txtDesc.setText("");
                    txtDesc.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtDesc.getText().isEmpty()) {
                    txtDesc.setForeground(Color.GRAY);
                    txtDesc.setText(placeholderDesc);
                }
            }
        });

        JScrollPane scrollDesc = new JScrollPane(txtDesc);
        scrollDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(lblDesc);
        formPanel.add(scrollDesc);
        formPanel.add(Box.createVerticalStrut(20));

        JLabel lblIdioma = new JLabel("Idiomas*");
        lblIdioma.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblIdioma);

        JPanel panelIdiomas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelIdiomas.setOpaque(false);
        panelIdiomas.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIdiomas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JComboBox<String> comboIdioma = new JComboBox<>(new String[] { "Español", "Inglés" });
        comboIdioma.setMaximumSize(new Dimension(150, comboIdioma.getPreferredSize().height));
        JComboBox<String> comboNivel = new JComboBox<>(new String[] { "Bajo", "Medio", "Alto", "Nativo" });
        comboNivel.setMaximumSize(new Dimension(150, comboNivel.getPreferredSize().height));

        panelIdiomas.add(comboIdioma);
        panelIdiomas.add(comboNivel);
        formPanel.add(panelIdiomas);

        formPanel.add(Box.createVerticalGlue());
        formPanel.add(Box.createVerticalStrut(20));

        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.setBackground(new Color(26, 177, 77));
        btnContinuar.setForeground(Color.BLACK);
        btnContinuar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnContinuar.setFocusPainted(false);
        btnContinuar.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnContinuar.addActionListener(e -> {
            String nombreMostrado = txtDisplay.getText();
            String desc = txtDesc.getText();

            if (nombreMostrado.equals(placeholderDisplay) || nombreMostrado.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa un nombre a mostrar.", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (desc.equals(placeholderDesc) || desc.trim().length() < 150) {
                JOptionPane.showMessageDialog(this, "La descripción debe tener al menos 150 caracteres.", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (fotoPerfilSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Por favor, sube una foto de perfil.", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String rutaGuardada = guardarFotoPerfil();
            if (rutaGuardada == null) {
                JOptionPane.showMessageDialog(this, "Error al guardar la foto de perfil.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String idioma = (String) comboIdioma.getSelectedItem();
            String nivel = (String) comboNivel.getSelectedItem();

            GestorDeDatos.actualizarPerfilIlustrador(
                    usuario.getNombreUsuario(),
                    nombreMostrado,
                    desc,
                    idioma,
                    nivel,
                    rutaGuardada);
            Usuario usuarioActualizado = GestorDeDatos.obtenerUsuario(usuario.getNombreUsuario());
            new Panel_Ilustrador(usuarioActualizado);
            dispose();
        });

        formPanel.add(btnContinuar);

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(new EmptyBorder(0, 150, 0, 150));

        formWrapper.add(formPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(formWrapper);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setEnabled(false);

        return scrollPane;
    }

    // --- Helpers (Sin cambios) ---
    private JPanel crearCampoTextoConPlaceholder(String label, String placeholder1, String placeholder2) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        JPanel campos = new JPanel(new GridLayout(1, 2, 10, 0));
        campos.setOpaque(false);
        campos.setAlignmentX(Component.LEFT_ALIGNMENT);
        campos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        JTextField txt1 = crearJTextFieldConPlaceholder(placeholder1, placeholder1);
        JTextField txt2 = crearJTextFieldConPlaceholder(placeholder2, placeholder2);
        campos.add(txt1);
        campos.add(txt2);
        panel.add(campos);
        panel.add(Box.createVerticalStrut(20));
        return panel;
    }

    private JTextField crearJTextFieldConPlaceholder(String initialText, String placeholder) {
        JTextField textField = new JTextField(initialText);
        textField.setForeground(Color.GRAY);
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(5, 5, 5, 5)));
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
        return textField;
    }

    private void seleccionarFotoPerfil() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "png", "jpeg");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            fotoPerfilSeleccionada = chooser.getSelectedFile();
            try {
                ImageIcon icon = new ImageIcon(fotoPerfilSeleccionada.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblPreviewFoto.setIcon(new ImageIcon(img));
                lblPreviewFoto.setText("");
            } catch (Exception ex) {
                lblPreviewFoto.setText("Error de preview");
            }
        }
    }

    // En Panel_Roll_Registro.java
    private String guardarFotoPerfil() {
        if (fotoPerfilSeleccionada == null) {
            return null;
        }

        // Determinar carpeta según el rol actual del usuario
        // (El usuario ya tiene el rol asignado en este punto del flujo)
        String tipoCarpeta;
        if ("ilustrador".equalsIgnoreCase(usuario.getTipo())) {
            tipoCarpeta = utils.GestorDeArchivos.CARPETA_ILUSTRADORES;
        } else {
            tipoCarpeta = utils.GestorDeArchivos.CARPETA_CLIENTES;
        }

        // Llamamos al nuevo método simplificado
        return utils.GestorDeArchivos.guardarImagenPerfil(
                fotoPerfilSeleccionada,
                tipoCarpeta,
                usuario.getNombreUsuario()
        );
    }

    // Clase Borde Redondo (Sin cambios, ya la tenías)
    private static class RoundBorder extends LineBorder {

        private int radius;

        public RoundBorder(Color color, int thickness, int radius) {
            super(color, thickness, true);
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int arc = radius * 2;
            g2.setColor(getLineColor());
            g2.setStroke(new BasicStroke(getThickness()));
            g2.drawRoundRect(x, y, width - getThickness(), height - getThickness(), arc, arc);
            g2.dispose();
        }
    }
}
