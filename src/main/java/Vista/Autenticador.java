package Vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author sout
 */
public class Autenticador extends JFrame {

    private JPanel derechoCards;
    private CardLayout cardLayout;

    private volatile boolean detenerParpadeo = false;

    // Usar los bordes de Estilos.java
    private final Border bordeOriginal = Estilos.BORDE_CAMPO_TEXTO;
    private final Border bordeError = BorderFactory.createCompoundBorder(
            new LineBorder(Color.RED, 2),
            new EmptyBorder(5, 8, 5, 8) // Asegurar padding
    );

    // nombres para las tarjetas
    private static final String INICIO = "inicio";
    private static final String REGISTRO = "registro";
    private static final String RECUPERAR = "recuperar";
    private static final String EXITO_REGISTRO = "exitoRegistro";
    private static final String EXITO_CAMBIO = "exitoCambio";
    private static final String CAMBIO_PASS = "cambio_pass";

    private String usuarioARecuperar;

    public Autenticador() {
        setTitle("IlustratonGest - Autenticación");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        add(crearPanelIzquierdo());

        cardLayout = new CardLayout();
        derechoCards = new JPanel(cardLayout);
        derechoCards.add(crearPanelInicio(), INICIO);
        derechoCards.add(crearPanelRegistro(), REGISTRO);
        derechoCards.add(crearPanelRecuperacion(), RECUPERAR);
        derechoCards.add(crearPanelExitoRegistro(), EXITO_REGISTRO);
        derechoCards.add(crearPanelExitoCambio(), EXITO_CAMBIO);
        derechoCards.add(crearPanelCambioPass(), CAMBIO_PASS);

        add(derechoCards);

        setVisible(true);
    }

    private JPanel crearPanelIzquierdo() {

        ImageIcon bgIcon = null;
        try {
            String rutaImagen = "/img/furross.png";
            bgIcon = new ImageIcon(getClass().getResource(rutaImagen));
        } catch (Exception e) {
            System.err.println("Error al cargar imagen de fondo: " + e.getMessage());
        }

        final Image bgImage = (bgIcon != null) ? bgIcon.getImage() : null;

        JPanel izq = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(123, 30, 64));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        JPanel pTexto = new JPanel();
        pTexto.setLayout(new BoxLayout(pTexto, BoxLayout.Y_AXIS));
        pTexto.setOpaque(false);
        pTexto.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel tit = new JLabel("<html><h1 style='color:white'>El éxito comienza aquí</h1></html>");
        tit.setAlignmentX(Component.LEFT_ALIGNMENT);
        pTexto.add(tit);
        pTexto.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        String check = "<span style='color:white; font-size:14pt;'>&#10003; &nbsp;</span>";
        String font = "<span style='color:white; font-family:SansSerif; font-size:12pt;'>";

        JLabel check1 = new JLabel(
                "<html>" + check + font + "Contamos con una gran variedad de ilustraciones</span></html>");
        check1.setAlignmentX(Component.LEFT_ALIGNMENT);
        pTexto.add(check1);
        pTexto.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));

        JLabel check2 = new JLabel(
                "<html>" + check + font + "Trabajos de calidad realizados en el menor tiempo posible</span></html>");
        check2.setAlignmentX(Component.LEFT_ALIGNMENT);
        pTexto.add(check2);
        pTexto.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));

        JLabel check3 = new JLabel("<html>" + check + font
                + "Acceso rápido y amigable a talentosos ilustradores alrededor del mundo</span></html>");
        check3.setAlignmentX(Component.LEFT_ALIGNMENT);
        pTexto.add(check3);

        izq.add(pTexto, BorderLayout.NORTH);

        return izq;
    }

    private JButton crearBotonVolver() {
        JButton btnVolver = new JButton("←");
        Estilos.estilizarBotonTexto(btnVolver, false);
        btnVolver.setFont(Estilos.FUENTE_TITULO_H2);
        btnVolver.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVolver.setMargin(new Insets(0, 0, 0, 0));
        btnVolver.setBorder(BorderFactory.createEmptyBorder(0, 0, Estilos.ESPACIO_MEDIANO, 0));
        btnVolver.addActionListener(e -> cardLayout.show(derechoCards, INICIO));
        return btnVolver;
    }

    private JPanel crearPanelInicio() {
        JPanel p = new JPanel();
        p.setBackground(Estilos.COLOR_FONDO_BLANCO);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Inicia sesión en tu cuenta");
        title.setFont(Estilos.FUENTE_TITULO_H2);
        title.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JLabel userLabel = new JLabel("Usuario o correo:");
        userLabel.setFont(Estilos.FUENTE_TEXTO_BOLD);
        userLabel.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(Estilos.FUENTE_TEXTO_BOLD);
        passLabel.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);

        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUser.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtUser.setFont(Estilos.FUENTE_TEXTO_REGULAR);

        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPass.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtPass.setFont(Estilos.FUENTE_TEXTO_REGULAR);

        MouseAdapter listenerDetenerParpadeo = new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                detenerParpadeo = true;
            }
        };
        txtUser.addMouseListener(listenerDetenerParpadeo);
        txtPass.addMouseListener(listenerDetenerParpadeo);

        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pCampos = new JPanel();
        pCampos.setOpaque(false);
        pCampos.setLayout(new BoxLayout(pCampos, BoxLayout.Y_AXIS));
        pCampos.setAlignmentX(Component.LEFT_ALIGNMENT);

        pCampos.add(userLabel);
        pCampos.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        pCampos.add(txtUser);
        pCampos.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        pCampos.add(passLabel);
        pCampos.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        pCampos.add(txtPass);

        p.add(pCampos);
        p.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));

        // ERROR LABEL
        JLabel lblError = new JLabel("");
        lblError.setForeground(Color.RED);
        lblError.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblError);

        p.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        JButton btnLogin = new JButton("Iniciar sesión");
        Estilos.estilizarBotonPrimario(btnLogin);
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(150, 40));
        btnLogin.setForeground(Color.BLACK);

        btnLogin.addActionListener((ActionEvent e) -> {
            lblError.setText("");
            String id = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            boolean parpadearUsuario = false;
            boolean parpadearPass = false;
            if (id.isEmpty()) {
                parpadearUsuario = true;
            }
            if (pass.isEmpty()) {
                parpadearPass = true;
            }

            if (!id.isEmpty()) {
                int resultado = database.GestorDeDatos.validarLoginConCodigo(id, pass);
                switch (resultado) {
                    case 0, -1 -> {
                        parpadearUsuario = true;
                        parpadearPass = true;
                        lblError.setText("Invalid username or password");
                    }
                    case 2 -> {
                        parpadearPass = true;
                        lblError.setText("Invalid username or password");
                    }
                    case 1 -> {
                        abrirDashboard(database.GestorDeDatos.validarLogin(id, pass));
                        this.dispose();
                        return;
                    }
                }
            }
            if (parpadearUsuario) {
                parpadearCampo(txtUser);
            }
            if (parpadearPass) {
                parpadearCampo(txtPass);
            }
        });
        p.add(btnLogin);
        txtPass.addActionListener(e -> btnLogin.doClick());
        txtUser.addActionListener(e -> btnLogin.doClick());

        p.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        JLabel join = new JLabel("<html>¿Aún no tienes una cuenta? <a href=''>Crea una aqui</a></html>");
        join.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        join.setAlignmentX(Component.LEFT_ALIGNMENT);
        join.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        join.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(derechoCards, REGISTRO);
            }
        });
        p.add(join);

        p.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        JLabel forgot = new JLabel("<html><a href=''>¿Olvidaste tu contraseña?</a></html>");
        forgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgot.setAlignmentX(Component.LEFT_ALIGNMENT);
        forgot.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        forgot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(derechoCards, RECUPERAR);
            }
        });
        p.add(forgot);

        return p;
    }

    private void parpadearCampo(JComponent campo) {
        detenerParpadeo = false;
        Timer timer = new Timer(300, null);
        timer.addActionListener(new ActionListener() {
            private int contador = 0;
            private boolean enRojo = false;
            private final int MAX_PARPADEOS = 6;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (detenerParpadeo || contador >= MAX_PARPADEOS) {
                    campo.setBorder(bordeOriginal);
                    timer.stop();
                    return;
                }
                if (enRojo) {
                    campo.setBorder(bordeOriginal);
                } else {
                    campo.setBorder(bordeError);
                }
                enRojo = !enRojo;
                contador++;
            }
        });
        timer.start();
    }

    private JPanel crearPanelRegistro() {
        JPanel p = new JPanel();
        p.setBackground(Estilos.COLOR_FONDO_BLANCO);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        p.add(crearBotonVolver());

        JLabel title = new JLabel("Registra tu cuenta");
        title.setFont(Estilos.FUENTE_TITULO_H3);
        title.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));

        JTextField txtUser = new JTextField();
        JTextField txtCorreo = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JPasswordField txtPass2 = new JPasswordField();
        JLabel userLabel = new JLabel("Nombre de usuario:");
        userLabel.setFont(Estilos.FUENTE_TEXTO_BOLD);
        JLabel correoLabel = new JLabel("Correo electrónico:");
        correoLabel.setFont(Estilos.FUENTE_TEXTO_BOLD);
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(Estilos.FUENTE_TEXTO_BOLD);
        JLabel pass2Label = new JLabel("Verificar contraseña:");
        pass2Label.setFont(Estilos.FUENTE_TEXTO_BOLD);

        JPanel pCamposRegistro = new JPanel();
        pCamposRegistro.setOpaque(false);
        pCamposRegistro.setLayout(new BoxLayout(pCamposRegistro, BoxLayout.Y_AXIS));
        pCamposRegistro.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUser.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtUser.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtCorreo.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtCorreo.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtCorreo.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        correoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPass.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtPass.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPass2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPass2.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPass2.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtPass2.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        pass2Label.setAlignmentX(Component.LEFT_ALIGNMENT);

        pCamposRegistro.add(userLabel);
        pCamposRegistro.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        pCamposRegistro.add(txtUser);
        pCamposRegistro.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        pCamposRegistro.add(correoLabel);
        pCamposRegistro.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        pCamposRegistro.add(txtCorreo);
        pCamposRegistro.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        pCamposRegistro.add(passLabel);
        pCamposRegistro.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        pCamposRegistro.add(txtPass);
        pCamposRegistro.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        pCamposRegistro.add(pass2Label);
        pCamposRegistro.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        pCamposRegistro.add(txtPass2);
        p.add(pCamposRegistro);
        p.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        JButton btnRegister = new JButton("Registrar");
        Estilos.estilizarBotonPrimario(btnRegister);
        btnRegister.setForeground(Color.BLACK);
        btnRegister.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRegister.setMaximumSize(new Dimension(150, 40));

        btnRegister.addActionListener(e -> {
            txtUser.setBorder(bordeOriginal);
            txtCorreo.setBorder(bordeOriginal);
            txtPass.setBorder(bordeOriginal);
            txtPass2.setBorder(bordeOriginal);

            detenerParpadeo = false;

            String u = txtUser.getText().trim();
            String c = txtCorreo.getText().trim();
            String p1 = new String(txtPass.getPassword());
            String p2 = new String(txtPass2.getPassword());

            boolean camposVacios = u.isEmpty() || c.isEmpty() || p1.isEmpty() || p2.isEmpty();
            boolean passNoCoinciden = !p1.equals(p2);

            if (camposVacios) {
                if (u.isEmpty())
                    parpadearCampo(txtUser);
                if (c.isEmpty())
                    parpadearCampo(txtCorreo);
                if (p1.isEmpty())
                    parpadearCampo(txtPass);
                if (p2.isEmpty())
                    parpadearCampo(txtPass2);
                return;
            }

            boolean existeU = database.GestorDeDatos.existeUsuario(u);
            boolean existeC = database.GestorDeDatos.existeCorreo(c);

            if (existeU || existeC) {
                if (existeU)
                    parpadearCampo(txtUser);
                if (existeC)
                    parpadearCampo(txtCorreo);
                if (passNoCoinciden) {
                    parpadearCampo(txtPass);
                    parpadearCampo(txtPass2);
                }
                return;
            }

            if (passNoCoinciden) {
                parpadearCampo(txtPass);
                parpadearCampo(txtPass2);
                return;
            }

            // Validacion Regex
            if (!c.matches("^.+@[a-zA-Z]+\\.com$")) {
                JOptionPane.showMessageDialog(this, "El correo debe tener el formato: caracteres@letras.com",
                        "Formato inválido", JOptionPane.WARNING_MESSAGE);
                parpadearCampo(txtCorreo);
                return;
            }

            if (p1.length() < 8) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 8 caracteres.",
                        "Contraseña débil", JOptionPane.WARNING_MESSAGE);
                parpadearCampo(txtPass);
                parpadearCampo(txtPass2);
                return;
            }

            modelos.Usuario nuevo = new modelos.Usuario(u, c, p1);
            boolean registrado = database.GestorDeDatos.registrarUsuario(nuevo);

            if (!registrado) {
                parpadearCampo(txtUser);
                parpadearCampo(txtCorreo);
                return;
            }

            txtUser.setText("");
            txtCorreo.setText("");
            txtPass.setText("");
            txtPass2.setText("");

            mostrarExitoYVolver(EXITO_REGISTRO);
        });
        p.add(btnRegister);

        return p;
    }

    private JPanel crearPanelRecuperacion() {
        JPanel p = new JPanel();
        p.setBackground(Estilos.COLOR_FONDO_BLANCO);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        p.add(crearBotonVolver());

        JLabel title = new JLabel("Recuperar contraseña");
        title.setFont(Estilos.FUENTE_TITULO_H3);
        title.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));

        JTextField txtUser = new JTextField();
        JTextField txtCorreo = new JTextField();
        JLabel userLabel = new JLabel("Nombre de usuario:");
        userLabel.setFont(Estilos.FUENTE_TEXTO_BOLD);
        JLabel correoLabel = new JLabel("Correo asociado:");
        correoLabel.setFont(Estilos.FUENTE_TEXTO_BOLD);

        txtUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUser.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtUser.setFont(Estilos.FUENTE_TEXTO_REGULAR);

        txtCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtCorreo.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtCorreo.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtCorreo.setFont(Estilos.FUENTE_TEXTO_REGULAR);

        MouseAdapter listenerDetenerParpadeo = new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                detenerParpadeo = true;
            }
        };
        txtUser.addMouseListener(listenerDetenerParpadeo);
        txtCorreo.addMouseListener(listenerDetenerParpadeo);

        JPanel pCamposRecuperar = new JPanel();
        pCamposRecuperar.setOpaque(false);
        pCamposRecuperar.setLayout(new BoxLayout(pCamposRecuperar, BoxLayout.Y_AXIS));
        pCamposRecuperar.setAlignmentX(Component.LEFT_ALIGNMENT);

        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        correoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        pCamposRecuperar.add(userLabel);
        pCamposRecuperar.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        pCamposRecuperar.add(txtUser);
        pCamposRecuperar.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        pCamposRecuperar.add(correoLabel);
        pCamposRecuperar.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        pCamposRecuperar.add(txtCorreo);
        p.add(pCamposRecuperar);
        p.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        JButton btnVerificar = new JButton("Verificar identidad");
        Estilos.estilizarBotonPrimario(btnVerificar);
        btnVerificar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVerificar.setMaximumSize(new Dimension(180, 40));

        btnVerificar.addActionListener(e -> {
            String u = txtUser.getText().trim();
            String c = txtCorreo.getText().trim();

            boolean parpadearUser = false;
            boolean parpadearCorreo = false;

            if (u.isEmpty())
                parpadearUser = true;
            if (c.isEmpty())
                parpadearCorreo = true;

            if (parpadearUser || parpadearCorreo) {
                if (parpadearUser)
                    parpadearCampo(txtUser);
                if (parpadearCorreo)
                    parpadearCampo(txtCorreo);
                return;
            }

            boolean sonCompatibles = database.GestorDeDatos.verificarIdentidad(u, c);

            if (sonCompatibles) {
                this.usuarioARecuperar = u;
                cardLayout.show(derechoCards, CAMBIO_PASS);
                txtUser.setText("");
                txtCorreo.setText("");
            } else {
                boolean userExiste = database.GestorDeDatos.existeUsuario(u);
                if (userExiste) {
                    parpadearCorreo = true;
                } else {
                    boolean correoExiste = database.GestorDeDatos.existeCorreo(c);
                    if (correoExiste) {
                        parpadearUser = true;
                    } else {
                        parpadearUser = true;
                        parpadearCorreo = true;
                    }
                }
            }
            if (parpadearUser)
                parpadearCampo(txtUser);
            if (parpadearCorreo)
                parpadearCampo(txtCorreo);
        });

        p.add(btnVerificar);
        return p;
    }

    private JPanel crearPanelCambioPass() {
        JPanel p = new JPanel();
        p.setBackground(Estilos.COLOR_FONDO_BLANCO);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        p.add(crearBotonVolver());

        JLabel title = new JLabel("Crea una nueva contraseña");
        title.setFont(Estilos.FUENTE_TITULO_H3);
        title.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(title);
        p.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));

        JPanel pCamposPass = new JPanel();
        pCamposPass.setOpaque(false);
        pCamposPass.setLayout(new BoxLayout(pCamposPass, BoxLayout.Y_AXIS));
        pCamposPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel pass1Label = new JLabel("Nueva contraseña:");
        pass1Label.setFont(Estilos.FUENTE_TEXTO_BOLD);
        pass1Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField txtPass1 = new JPasswordField();
        txtPass1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPass1.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPass1.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtPass1.setFont(Estilos.FUENTE_TEXTO_REGULAR);

        JLabel pass2Label = new JLabel("Confirmar contraseña:");
        pass2Label.setFont(Estilos.FUENTE_TEXTO_BOLD);
        pass2Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPasswordField txtPass2 = new JPasswordField();
        txtPass2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPass2.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPass2.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtPass2.setFont(Estilos.FUENTE_TEXTO_REGULAR);

        MouseAdapter listenerDetenerParpadeo = new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                detenerParpadeo = true;
            }
        };
        txtPass1.addMouseListener(listenerDetenerParpadeo);
        txtPass2.addMouseListener(listenerDetenerParpadeo);

        pCamposPass.add(pass1Label);
        pCamposPass.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        pCamposPass.add(txtPass1);
        pCamposPass.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        pCamposPass.add(pass2Label);
        pCamposPass.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        pCamposPass.add(txtPass2);

        p.add(pCamposPass);
        p.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        JButton btnConfirmar = new JButton("Confirmar");
        Estilos.estilizarBotonPrimario(btnConfirmar);
        btnConfirmar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnConfirmar.setMaximumSize(new Dimension(150, 40));

        btnConfirmar.addActionListener(e -> {
            String p1 = new String(txtPass1.getPassword());
            String p2 = new String(txtPass2.getPassword());

            boolean parpadearP1 = false;
            boolean parpadearP2 = false;

            if (p1.isEmpty())
                parpadearP1 = true;
            if (p2.isEmpty())
                parpadearP2 = true;

            if (p1.isEmpty() || p2.isEmpty()) {
                if (parpadearP1)
                    parpadearCampo(txtPass1);
                if (parpadearP2)
                    parpadearCampo(txtPass2);
                return;
            }

            if (!p1.equals(p2)) {
                parpadearCampo(txtPass1);
                parpadearCampo(txtPass2);
                return;
            }

            database.GestorDeDatos.actualizarContraseña(this.usuarioARecuperar, p1);

            this.usuarioARecuperar = null;
            txtPass1.setText("");
            txtPass2.setText("");

            mostrarExitoYVolver(EXITO_CAMBIO);
        });

        p.add(btnConfirmar);

        return p;
    }

    private JPanel crearPanelExitoRegistro() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel pEncabezado = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pEncabezado.setBackground(Estilos.COLOR_FONDO_BLANCO);
        pEncabezado.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        pEncabezado.add(crearBotonVolver());
        panel.add(pEncabezado, BorderLayout.NORTH);

        JPanel pCentro = new JPanel();
        pCentro.setLayout(new BoxLayout(pCentro, BoxLayout.Y_AXIS));
        pCentro.setOpaque(false);
        pCentro.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel lblMensaje = new JLabel("Registro de la cuenta exitoso");
        lblMensaje.setFont(Estilos.FUENTE_TITULO_H2);
        lblMensaje.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblIcono = new JLabel("\u2714");
        lblIcono.setFont(new Font("SansSerif", Font.BOLD, 150));
        lblIcono.setForeground(Estilos.COLOR_VERDE_PRIMARIO); // Verde
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        pCentro.add(Box.createVerticalGlue());
        pCentro.add(lblMensaje);
        pCentro.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        pCentro.add(lblIcono);
        pCentro.add(Box.createVerticalGlue());

        panel.add(pCentro, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelExitoCambio() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel pEncabezado = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pEncabezado.setBackground(Estilos.COLOR_FONDO_BLANCO);
        pEncabezado.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        pEncabezado.add(crearBotonVolver());

        panel.add(pEncabezado, BorderLayout.NORTH);

        JPanel pCentro = new JPanel();
        pCentro.setLayout(new BoxLayout(pCentro, BoxLayout.Y_AXIS));
        pCentro.setOpaque(false);
        pCentro.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        JLabel lblMensaje = new JLabel("Cambio de contraseña exitoso");
        lblMensaje.setFont(Estilos.FUENTE_TITULO_H2);
        lblMensaje.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblIcono = new JLabel("\u2714");
        lblIcono.setFont(new Font("SansSerif", Font.BOLD, 150));
        lblIcono.setForeground(Estilos.COLOR_VERDE_PRIMARIO); // Verde
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        pCentro.add(Box.createVerticalGlue());
        pCentro.add(lblMensaje);
        pCentro.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        pCentro.add(lblIcono);
        pCentro.add(Box.createVerticalGlue());

        panel.add(pCentro, BorderLayout.CENTER);

        return panel;
    }

    private void abrirDashboard(modelos.Usuario user) {
        String tipo = user.getTipo();
        SwingUtilities.invokeLater(() -> {
            switch (tipo) {
                case "cliente":
                    new Panel_Cliente(user);
                    break;
                case "ilustrador":
                    new Panel_Ilustrador(user);
                    break;
                case "admin":
                    new Panel_Administrador(user);
                    break;
                case "usuario_nuevo":
                    new Panel_Roll_Registro(user);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Tipo de usuario desconocido.", "Error",
                            JOptionPane.ERROR_MESSAGE);
            }
        });
    }
/**
     * Muestra una tarjeta de éxito y programa un temporizador para volver
     * a la pantalla de inicio automáticamente después de 2 segundos.
     * * @param tarjetaExito El nombre de la tarjeta a mostrar (ej: EXITO_REGISTRO)
     */
    private void mostrarExitoYVolver(String tarjetaExito) {
        // 1. Mostrar la pantalla de éxito indicada
        cardLayout.show(derechoCards, tarjetaExito);

        // 2. Iniciar el timer de 2 segundos (2000 ms)
        Timer timerRedireccion = new Timer(2000, e -> {
            cardLayout.show(derechoCards, INICIO);
        });
        
        timerRedireccion.setRepeats(false); // Asegurar que solo corra una vez
        timerRedireccion.start();
    }
}