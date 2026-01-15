package Vista;

import Vista.componentes.PreviewGig; // Asegúrate de tener este import
import modelos.Ilustracion;
import modelos.Usuario;
import modelos.Ilustrador;
import database.GestorDeDatos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TarjetaObra extends JPanel {

    private Usuario clienteActual;
    private Ilustracion ilustracion; // Guardamos la ilustración como variable global

    public TarjetaObra(Ilustracion ilustracion, Usuario cliente) {
        this.clienteActual = cliente;
        this.ilustracion = ilustracion;
        initComponent();
    }

    private void initComponent() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Estilos.COLOR_BORDE, 1),
                new EmptyBorder(8, 8, 8, 8)
        ));
        setBackground(Estilos.COLOR_FONDO_BLANCO);
        setPreferredSize(new Dimension(240, 300));
        setMaximumSize(new Dimension(240, 300));
        
        // --- 1. LISTENER PARA ABRIR PREVIEW (CLIC EN LA TARJETA) ---
        // Esto hace que al dar clic en el fondo blanco, se abra el detalle
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirPreview();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                // Efecto visual al pasar el mouse (Borde negro)
                setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Color.BLACK, 1),
                    new EmptyBorder(8, 8, 8, 8)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                // Quitar efecto
                setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Estilos.COLOR_BORDE, 1),
                    new EmptyBorder(8, 8, 8, 8)
                ));
            }
        });

        // --- IMAGEN ---
        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(220, 160));
        lblImagen.setOpaque(true);
        lblImagen.setBackground(Estilos.COLOR_FONDO_GRIS);

        ImageIcon icon = ilustracion.getImageIcon();
        if (icon != null && icon.getImage() != null) {
            Image img = icon.getImage().getScaledInstance(220, 160, Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(img));
        } else {
            lblImagen.setText("Imagen no disponible");
            lblImagen.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        }
        // IMPORTANTE: Pasar el clic de la imagen al panel padre
        lblImagen.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { abrirPreview(); }
        });
        add(lblImagen, BorderLayout.NORTH);

        // --- INFO ---
        JPanel pInfo = new JPanel();
        pInfo.setLayout(new BoxLayout(pInfo, BoxLayout.Y_AXIS));
        pInfo.setBorder(new EmptyBorder(8, 5, 5, 5));
        pInfo.setBackground(Estilos.COLOR_FONDO_BLANCO);
        
        // Para que si das click en el espacio blanco del texto tambien abra preview
        pInfo.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { abrirPreview(); }
        });

        JLabel lblTitulo = new JLabel(ilustracion.getTitulo());
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblTitulo.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblTitulo.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        pInfo.add(lblTitulo);

        String desc = ilustracion.getDescripcion();
        if (desc.length() > 70) {
            desc = desc.substring(0, 67) + "...";
        }
        JLabel lblDescripcion = new JLabel("<html><p style='width:190px;'>" + desc + "</p></html>");
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDescripcion.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
        lblDescripcion.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        pInfo.add(lblDescripcion);

        pInfo.add(Box.createVerticalGlue());

        JLabel lblPrecio = new JLabel(String.format("$%.2f", ilustracion.getPrecio()));
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPrecio.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblPrecio.setForeground(Estilos.COLOR_TEXTO_LINK);
        pInfo.add(lblPrecio);
        pInfo.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));

        // --- BOTÓN SOLICITAR (LÓGICA DE COMPRA) ---
        JButton btnSolicitar = new JButton("Solicitar");
        Estilos.estilizarBotonPrimario(btnSolicitar);
        btnSolicitar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnSolicitar.addActionListener(e -> {
            if (clienteActual == null) {
                JOptionPane.showMessageDialog(this, "Error: No se identificó al usuario.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Confirmar pedido de '" + ilustracion.getTitulo() + "'?\nPrecio: $" + ilustracion.getPrecio(),
                "Confirmar Compra", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String fechaEntrega = LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                boolean exito = GestorDeDatos.crearNuevoPedido(
                    clienteActual.getNombreUsuario(), 
                    ilustracion.getNombreAutor(), 
                    ilustracion.getTitulo(), 
                    ilustracion.getPrecio(), 
                    fechaEntrega
                );

                if (exito) {
                    JOptionPane.showMessageDialog(this, "¡Pedido realizado con éxito!");
                } else {
                    JOptionPane.showMessageDialog(this, "Hubo un error al guardar el pedido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        pInfo.add(btnSolicitar);
        add(pInfo, BorderLayout.CENTER);
    }

    // --- MÉTODO PRIVADO PARA ABRIR EL PREVIEW ---
    // --- MÉTODO PRIVADO PARA ABRIR EL PREVIEW ---
    private void abrirPreview() {
        Usuario u = GestorDeDatos.obtenerUsuario(ilustracion.getNombreAutor());
        
        if (u instanceof Ilustrador) {
            // CAMBIO: Pasamos 'this.clienteActual' como tercer parámetro
            // (Asegúrate de modificar el constructor de PreviewGig en el siguiente paso)
            PreviewGig preview = new PreviewGig(ilustracion, (Ilustrador)u, null, this.clienteActual);
            preview.setVisible(true);
        } else {
            System.out.println("No se pudo cargar el ilustrador para el preview.");
        }
    }
}