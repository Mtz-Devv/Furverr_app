package Vista.componentes;

import database.GestorDeDatos;
import modelos.Mensaje;
import modelos.Usuario;
import Vista.Estilos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class VentanaChat extends JDialog {

    private Usuario usuarioActual;
    private Usuario otroUsuario;
    private JPanel panelMensajes;
    private JTextField txtMensaje;
    private JScrollPane scrollPane;
    private Timer timer;

    public VentanaChat(Window parent, Usuario usuarioActual, Usuario otroUsuario) {
        super(parent, "Chat con " + otroUsuario.getNombreMostrado(), ModalityType.APPLICATION_MODAL);
        this.usuarioActual = usuarioActual;
        this.otroUsuario = otroUsuario;

        setSize(400, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Foto de perfil
        JLabel lblFoto = new JLabel();
        if (otroUsuario.getRutaFotoPerfil() != null && !otroUsuario.getRutaFotoPerfil().isEmpty()) {
            ImageIcon icon = new ImageIcon(otroUsuario.getRutaFotoPerfil());
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(img));
        } else {
            // Placeholder
            lblFoto.setPreferredSize(new Dimension(40, 40));
            lblFoto.setOpaque(true);
            lblFoto.setBackground(Color.LIGHT_GRAY);
            lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
            String nombre = (otroUsuario.getNombreMostrado() != null && !otroUsuario.getNombreMostrado().isEmpty())
                    ? otroUsuario.getNombreMostrado()
                    : otroUsuario.getNombreUsuario(); // Fallback a usuario
            String inicial = (nombre != null && nombre.length() > 0) ? nombre.substring(0, 1).toUpperCase() : "?";

            lblFoto.setText(inicial);
            lblFoto.setForeground(Color.WHITE);
            lblFoto.setFont(Estilos.FUENTE_TITULO_H3);
        }

        String nombreDisplay = (otroUsuario.getNombreMostrado() != null && !otroUsuario.getNombreMostrado().isEmpty())
                ? otroUsuario.getNombreMostrado()
                : otroUsuario.getNombreUsuario(); // Fallback a usuario
        JLabel lblNombre = new JLabel(nombreDisplay);
        lblNombre.setFont(Estilos.FUENTE_TITULO_H3);

        header.add(lblFoto, BorderLayout.WEST);
        header.add(lblNombre, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // --- AREA DE MENSAJES ---
        panelMensajes = new JPanel();
        panelMensajes.setLayout(new BoxLayout(panelMensajes, BoxLayout.Y_AXIS));
        panelMensajes.setBackground(new Color(229, 221, 213)); // Color fondo WhatsApp

        scrollPane = new JScrollPane(panelMensajes);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- INPUT AREA ---
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnAdjuntar = new JButton("+");
        Estilos.estilizarBotonSecundario(btnAdjuntar);
        btnAdjuntar.setPreferredSize(new Dimension(40, 40));

        txtMensaje = new JTextField();
        txtMensaje.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        txtMensaje.setBorder(Estilos.BORDE_CAMPO_TEXTO);

        JButton btnEnviar = new JButton("Enviar");
        Estilos.estilizarBotonPrimario(btnEnviar);
        btnEnviar.setPreferredSize(new Dimension(80, 40)); // Asegurar tamaño

        inputPanel.add(btnAdjuntar, BorderLayout.WEST);
        inputPanel.add(txtMensaje, BorderLayout.CENTER);
        inputPanel.add(btnEnviar, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // --- ACCIONES ---
        btnEnviar.addActionListener(e -> enviarMensaje(null));
        txtMensaje.addActionListener(e -> enviarMensaje(null));

        btnAdjuntar.addActionListener(e -> adjuntarArchivo());

        // Cargar mensajes iniciales
        cargarMensajes();

        // Timer para actualizar mensajes cada 3 segundos
        timer = new Timer(3000, e -> cargarMensajes());
        timer.start();

        // Marcar como leídos al abrir
        GestorDeDatos.marcarMensajesComoLeidos(otroUsuario.getNombreUsuario(), usuarioActual.getNombreUsuario());
    }

    @Override
    public void dispose() {
        if (timer != null)
            timer.stop();
        super.dispose();
    }

    private void cargarMensajes() {
        List<Mensaje> mensajes = GestorDeDatos.obtenerMensajes(usuarioActual.getNombreUsuario(),
                otroUsuario.getNombreUsuario());

        panelMensajes.removeAll();

        for (Mensaje m : mensajes) {
            boolean esMio = m.getRemitente().equals(usuarioActual.getNombreUsuario());
            agregarBurbujaMensaje(m, esMio);
        }

        panelMensajes.revalidate();
        panelMensajes.repaint();

        // Scroll to bottom
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    private void agregarBurbujaMensaje(Mensaje m, boolean esMio) {
        JPanel row = new JPanel(new FlowLayout(esMio ? FlowLayout.RIGHT : FlowLayout.LEFT));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(1, 0, 1, 0)); // Reduced vertical padding

        Color colorFondo = esMio ? new Color(225, 255, 199) : Color.WHITE;
        Color colorTexto = Color.BLACK;

        RoundedPanel burbuja = new RoundedPanel(15, colorFondo);
        burbuja.setLayout(new BoxLayout(burbuja, BoxLayout.Y_AXIS));
        burbuja.setBorder(new EmptyBorder(8, 12, 8, 12));

        if (m.getMensaje() != null && !m.getMensaje().isEmpty()) {
            JTextArea txtArea = new JTextArea(m.getMensaje());
            txtArea.setFont(Estilos.FUENTE_TEXTO_REGULAR);
            txtArea.setForeground(colorTexto);
            txtArea.setLineWrap(true);
            txtArea.setWrapStyleWord(true);
            txtArea.setOpaque(false);
            txtArea.setEditable(false);

            int maxW = 250;
            txtArea.setSize(new Dimension(maxW, Short.MAX_VALUE));
            Dimension preferredSize = txtArea.getPreferredSize();
            if (preferredSize.width > maxW) {
                preferredSize.width = maxW;
            }
            txtArea.setMaximumSize(preferredSize);

            burbuja.add(txtArea);
        }

        if (m.getArchivo() != null && !m.getArchivo().isEmpty()) {
            File f = new File(m.getArchivo());
            JButton btnArchivo = new JButton("📎 " + f.getName());
            btnArchivo.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
            btnArchivo.setForeground(new Color(0, 102, 204));
            btnArchivo.setContentAreaFilled(false);
            btnArchivo.setBorderPainted(false);
            btnArchivo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnArchivo.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnArchivo.addActionListener(e -> {
                try {
                    Desktop.getDesktop().open(f);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "No se puede abrir el archivo: " + ex.getMessage());
                }
            });
            burbuja.add(btnArchivo);
        }

        if (m.getFecha() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
            JLabel lblHora = new JLabel(sdf.format(m.getFecha()));
            lblHora.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            lblHora.setForeground(Color.GRAY);
            lblHora.setAlignmentX(Component.RIGHT_ALIGNMENT);
            burbuja.add(Box.createVerticalStrut(2));
            burbuja.add(lblHora);
        }

        row.add(burbuja);
        panelMensajes.add(row);
        panelMensajes.add(Box.createVerticalStrut(2)); // Reduced vertical strut
    }

    private void enviarMensaje(String archivo) {
        String texto = txtMensaje.getText().trim();
        if (texto.isEmpty() && archivo == null)
            return;

        boolean exito = GestorDeDatos.enviarMensaje(
                usuarioActual.getNombreUsuario(),
                otroUsuario.getNombreUsuario(),
                texto,
                archivo);

        if (exito) {
            txtMensaje.setText("");
            cargarMensajes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al enviar mensaje", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adjuntarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            enviarMensaje(selectedFile.getAbsolutePath());
        }
    }

    private static class RoundedPanel extends JPanel {
        private int radius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }
}
