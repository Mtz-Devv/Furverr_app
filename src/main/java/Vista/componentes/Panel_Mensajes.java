package Vista.componentes;

import database.GestorDeDatos;
import modelos.Usuario;
import Vista.Estilos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class Panel_Mensajes extends JDialog {

    private Usuario usuarioActual;
    private JPanel listaConversaciones;

    public Panel_Mensajes(Window parent, Usuario usuario) {
        super(parent, "Mis Mensajes", ModalityType.APPLICATION_MODAL);
        this.usuarioActual = usuario;

        setSize(400, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Bandeja de Entrada");
        lblTitulo.setFont(Estilos.FUENTE_TITULO_H2);
        lblTitulo.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(lblTitulo, BorderLayout.NORTH);

        listaConversaciones = new JPanel();
        listaConversaciones.setLayout(new BoxLayout(listaConversaciones, BoxLayout.Y_AXIS));
        listaConversaciones.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(listaConversaciones);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        cargarConversaciones();
    }

    private void cargarConversaciones() {
        List<String> usuarios = GestorDeDatos.obtenerConversaciones(usuarioActual.getNombreUsuario());

        if (usuarios.isEmpty()) {
            JLabel l = new JLabel("No tienes conversaciones.");
            l.setBorder(new EmptyBorder(20, 20, 20, 20));
            listaConversaciones.add(l);
        } else {
            for (String otroUser : usuarios) {
                Usuario u = GestorDeDatos.obtenerUsuario(otroUser);
                if (u != null) {
                    agregarItemConversacion(u);
                }
            }
        }
    }

    private void agregarItemConversacion(Usuario otroUsuario) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(15, 20, 15, 20)));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        // Foto
        JLabel lblFoto = new JLabel();
        if (otroUsuario.getRutaFotoPerfil() != null && !otroUsuario.getRutaFotoPerfil().isEmpty()) {
            ImageIcon icon = new ImageIcon(otroUsuario.getRutaFotoPerfil());
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(img));
        } else {
            String initial = "?";
            String nombre = (otroUsuario.getNombreMostrado() != null && !otroUsuario.getNombreMostrado().isEmpty())
                    ? otroUsuario.getNombreMostrado()
                    : otroUsuario.getNombreUsuario();
            if (nombre != null && !nombre.isEmpty()) {
                initial = nombre.substring(0, 1).toUpperCase();
            }
            lblFoto.setText(initial);
            lblFoto.setPreferredSize(new Dimension(40, 40));
            lblFoto.setOpaque(true);
            lblFoto.setBackground(Color.LIGHT_GRAY);
            lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
            lblFoto.setForeground(Color.WHITE);
            lblFoto.setFont(Estilos.FUENTE_TEXTO_BOLD);
        }

        String nombre = (otroUsuario.getNombreMostrado() != null && !otroUsuario.getNombreMostrado().isEmpty())
                ? otroUsuario.getNombreMostrado()
                : otroUsuario.getNombreUsuario();
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(Estilos.FUENTE_TEXTO_BOLD);

        item.add(lblFoto, BorderLayout.WEST);
        item.add(lblNombre, BorderLayout.CENTER);

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new VentanaChat(Panel_Mensajes.this, usuarioActual, otroUsuario).setVisible(true);
                // Al volver, podríamos recargar, pero por ahora simple.
            }
        });

        listaConversaciones.add(item);
    }
}
