/*
 en este panel se puede editar:
    - descripcion
    - foto de perfil
    - nombre de usuario
*/


package Vista.componentes.cliente;

import modelos.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
/// imports para AWS and S3 at nueva foto function.
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.regions.Region;
import java.net.URL;
/// Fin imports for AWS and S3
///import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.Locale;

/**
 * @author isaaciturralde
 */
public class Panel_Cuenta extends JPanel {

    // 1. VARIABLES GLOBALES
    private Usuario usuario;
    private Runnable accionVolver;
    private Runnable accionActualizarHeader; // Para avisar al Panel_Cliente

    // Componentes UI
    private JLabel lblAvatar;
    private JLabel lblDisplayName;
    private JTextArea txtDescripcion;

    // 2. CONSTRUCTOR
    public Panel_Cuenta(Usuario user, Runnable accionVolver, Runnable onFotoChange) {
        this.usuario = user;
        this.accionVolver = accionVolver;
        this.accionActualizarHeader = onFotoChange;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ZONA 1: NAVEGACIÓN
        add(crearBarraNavegacion(), BorderLayout.NORTH);

        // ZONA 2: CONTENIDO
        JPanel contenidoVertical = new JPanel();
        contenidoVertical.setLayout(new BoxLayout(contenidoVertical, BoxLayout.Y_AXIS));
        contenidoVertical.setBackground(new Color(245, 245, 245));
        contenidoVertical.setBorder(new EmptyBorder(20, 40, 20, 40));

        contenidoVertical.add(crearTarjetaPerfil());
        contenidoVertical.add(Box.createVerticalStrut(20));
        contenidoVertical.add(crearSeccionDescripcion());
        contenidoVertical.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(contenidoVertical);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

    // =================================================================================
    //                             MÉTODOS DE UI
    // =================================================================================

    private JPanel crearBarraNavegacion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 40, 0, 40));

        JLabel lblHome = new JLabel("Home");
        lblHome.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblHome.setForeground(Color.GRAY);
        lblHome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblHome.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { accionVolver.run(); }
        });

        JLabel lblSep = new JLabel(" / ");
        lblSep.setForeground(Color.LIGHT_GRAY);
        JLabel lblActual = new JLabel("Mi Cuenta");
        lblActual.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblActual.setForeground(Color.LIGHT_GRAY);

        panel.add(lblHome);
        panel.add(lblSep);
        panel.add(lblActual);
        return panel;
    }

    private JPanel crearTarjetaPerfil() {
        JPanel card = new JPanel(new GridBagLayout());
        estilizarTarjeta(card);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        GridBagConstraints gbc = new GridBagConstraints();

        // 1. AVATAR
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 2;
        gbc.insets = new Insets(0, 0, 0, 20);

        lblAvatar = new JLabel();
        
        // Cargar imagen inicial si existe
        Image imgInicial = null;
        if (usuario.getRutaFotoPerfil() != null && !usuario.getRutaFotoPerfil().isEmpty()) {
            try {
                File f = new File(usuario.getRutaFotoPerfil());
                if(f.exists()) imgInicial = new ImageIcon(f.getAbsolutePath()).getImage();
            } catch (Exception e) {}
        }
        actualizarDibujoAvatar(imgInicial);

        lblAvatar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblAvatar.addMouseListener(new MouseAdapter() {
            // AQUI ESTÁ EL CAMBIO: Abrimos el visor en lugar de cambiar directo
            @Override public void mouseClicked(MouseEvent e) { abiriVisorfoto(); } 
        });
        card.add(lblAvatar, gbc);

        // 2. TEXTOS
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String nombreMostrar = (usuario.getNombreMostrado() != null) ? usuario.getNombreMostrado() : usuario.getNombreUsuario();
        lblDisplayName = new JLabel(nombreMostrar);
        lblDisplayName.setFont(new Font("SansSerif", Font.BOLD, 22));
        card.add(lblDisplayName, gbc);

        gbc.gridy = 1;
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setOpaque(false);

        JLabel lblUserTag = new JLabel("@" + usuario.getNombreUsuario());
        lblUserTag.setForeground(Color.GRAY);
        JLabel lblRol = new JLabel("Rol: " + usuario.getTipoUsuario());
        lblRol.setForeground(Color.GRAY);

        // Lógica de Fecha
        /*String textoFecha = "Reciente";
        if (usuario.getFechaRegistro() != null && usuario.getFechaRegistro().length() >= 10) {
            try {
                String fechaDB = usuario.getFechaRegistro().substring(0, 10);
                LocalDate fechaObj = LocalDate.parse(fechaDB);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "ES"));
                String fechaFormateada = fechaObj.format(formatter);
                textoFecha = fechaFormateada.substring(0, 1).toUpperCase() + fechaFormateada.substring(1);
            } catch (Exception e) { textoFecha = "Desconocida"; }
        }
        JLabel lblDate = new JLabel("📅 Miembro desde " + textoFecha);
        lblDate.setForeground(new Color(100, 100, 100));
        lblDate.setFont(new Font("SansSerif", Font.PLAIN, 11));

        */
        infoPanel.add(lblUserTag);
        infoPanel.add(lblRol);
        //infoPanel.add(lblDate);
        card.add(infoPanel, gbc);

        // 3. EDITAR NOMBRE
        gbc.gridx = 2; gbc.gridy = 0; gbc.gridheight = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;

        JButton btnEdit = new JButton("Editar");
        btnEdit.setBackground(Color.WHITE);
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(e -> mostrarDialogoEditarNombre());
        card.add(btnEdit, gbc);

        return card;
    }

    private JPanel crearSeccionDescripcion() {
        JPanel card = new JPanel(new BorderLayout());
        estilizarTarjeta(card);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblTitle = new JLabel("Overview");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton btnEditDesc = new JButton("Editar descripción");
        btnEditDesc.setForeground(new Color(0, 102, 204));
        btnEditDesc.setBorderPainted(false);
        btnEditDesc.setContentAreaFilled(false);
        btnEditDesc.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEditDesc.addActionListener(e -> mostrarDialogoEditarDescripcion());

        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnEditDesc, BorderLayout.EAST);

        txtDescripcion = new JTextArea();
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setOpaque(false);
        txtDescripcion.setEditable(false);
        txtDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtDescripcion.setForeground(Color.DARK_GRAY);

        if (usuario.getDescripcion() == null || usuario.getDescripcion().isEmpty()) {
            txtDescripcion.setText("Sin descripción. ¡Cuéntanos algo sobre ti!");
            txtDescripcion.setForeground(Color.GRAY);
        } else {
            txtDescripcion.setText(usuario.getDescripcion());
        }

        card.add(header, BorderLayout.NORTH);
        card.add(txtDescripcion, BorderLayout.CENTER);
        return card;
    }

    // =================================================================================
    //                             LÓGICA DE FOTOS 
    // =================================================================================

    private void abiriVisorfoto() {
        JDialog visor = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Foto de Perfil", true);
        visor.setSize(400, 500);
        visor.setLocationRelativeTo(this);
        visor.setLayout(new BorderLayout());

        // Imagen Grande
        JLabel lblImagenGrande = new JLabel();
        lblImagenGrande.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Reutilizamos la logica para cargar la imagen grande
        if (usuario.getRutaFotoPerfil() != null && !usuario.getRutaFotoPerfil().isEmpty()) {
            try {
                File f = new File(usuario.getRutaFotoPerfil());
                if (f.exists()) {
                    ImageIcon iconoOriginal = new ImageIcon(f.getAbsolutePath());
                    // Escalar a 300x300 aprox
                    Image imgEscalada = iconoOriginal.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                    lblImagenGrande.setIcon(new ImageIcon(imgEscalada));
                } else {
                    lblImagenGrande.setText("Error cargando imagen");
                }
            } catch (Exception e) { lblImagenGrande.setText("Error"); }
        } else {
            lblImagenGrande.setText("Sin foto de perfil");
        }

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnCambiar = new JButton("Cambiar");
        JButton btnEliminar = new JButton("Eliminar");
        
        btnEliminar.setBackground(new Color(200, 50, 50));
        btnEliminar.setForeground(Color.RED);

        btnCambiar.addActionListener(e -> {
            visor.dispose();
            seleccionarNuevaFoto(); // Llama al selector
        });

        btnEliminar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(visor, "¿Seguro que quieres eliminar tu foto?", "Eliminar", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                eliminarFotoPerfil(); // Llama a la lógica de borrado
                visor.dispose();
            }
        });

        panelBotones.add(btnCambiar);
        panelBotones.add(btnEliminar);

        visor.add(lblImagenGrande, BorderLayout.CENTER);
        visor.add(panelBotones, BorderLayout.SOUTH);
        visor.setVisible(true);
    }

    private void eliminarFotoPerfil() {
        // 1. DB
        boolean exito = database.GestorDeDatos.eliminarFotoPerfil(usuario.getNombreUsuario());
        
        if (exito) {
            // 2. RAM
            usuario.setRutaFotoPerfil(null);
            
            // 3. UI Local
            actualizarDibujoAvatar(null); // Pinta el círculo verde
            
            // 4. UI Header
            if (accionActualizarHeader != null) accionActualizarHeader.run();
            
            JOptionPane.showMessageDialog(this, "Foto eliminada.");
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccionarNuevaFoto() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar Foto");
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "png", "jpeg"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = chooser.getSelectedFile();
            try {

        // DETAILS EC2
                String nombreBucket = "furverr-server";
                Region region = Region.US_EAST_1; //

        // PROCESS TO CONNECT DB with DB

                // 2. Generar nombre único para S3 (Igual que hacías antes)
                String nombreArchivoS3 = "perfil_" + usuario.getNombreUsuario() + "_" + System.currentTimeMillis() + ".png";

                //Inicializar cliente S3 (Esto buscará tus credenciales configuradas)
                S3Client s3 = S3Client.builder()
                        .region(region)
                        .build();
                //SUBIR EL ARCHIVO A LA NUBE.
                PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(nombreBucket)
                        .key(nombreArchivoS3)
                        .build();

                s3.putObject(request, archivoSeleccionado.toPath());
                s3.close(); // Cerramos conexión

                //CONSTRUIR LA URL PÚBLICA
                String rutaFinalUrl = "https://" + nombreBucket + ".s3.us-east-1.amazonaws.com/" + nombreArchivoS3;

                //Guardar la URL en DB at EC2
                boolean exito = database.GestorDeDatos.actualizarFotoPerfil(usuario.getNombreUsuario(), rutaFinalUrl);

                if (exito) {
                    usuario.setRutaFotoPerfil(rutaFinalUrl);
                    
                    ImageIcon nuevoIcono = new ImageIcon(new URL(rutaFinalUrl));
                    actualizarDibujoAvatar(nuevoIcono.getImage());
                    
                    if (accionActualizarHeader != null) accionActualizarHeader.run();
                    
                    JOptionPane.showMessageDialog(this, "¡Foto actualizada!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error BD.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error IO: " + e.getMessage());
            }
        }
    }

    // =================================================================================
    //                             MÉTODOS AUXILIARES
    // =================================================================================

    private void mostrarDialogoEditarNombre() {
        String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo Nombre:", lblDisplayName.getText());
        if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
            boolean exito = database.GestorDeDatos.actualizarNombreMostrado(usuario.getNombreUsuario(), nuevoNombre);
            if (exito) {
                lblDisplayName.setText(nuevoNombre);
                usuario.setNombreMostrado(nuevoNombre);
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar.");
            }
        }
    }

    private void mostrarDialogoEditarDescripcion() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Descripción", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JTextArea areaEdicion = new JTextArea(usuario.getDescripcion());
        areaEdicion.setLineWrap(true);
        areaEdicion.setWrapStyleWord(true);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(0, 153, 51));
        btnGuardar.setForeground(Color.WHITE);

        btnGuardar.addActionListener(e -> {
            String texto = areaEdicion.getText();
            boolean exito = database.GestorDeDatos.actualizarDescripcion(usuario.getNombreUsuario(), texto);
            if (exito) {
                usuario.setDescripcion(texto);
                txtDescripcion.setText(texto);
                txtDescripcion.setForeground(Color.DARK_GRAY);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al guardar.");
            }
        });

        dialog.add(new JScrollPane(areaEdicion), BorderLayout.CENTER);
        dialog.add(btnGuardar, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void actualizarDibujoAvatar(Image img) {
        int size = 100;
        BufferedImage imagenRedonda = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagenRedonda.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (img != null) {
            g2.setClip(new Ellipse2D.Float(0, 0, size, size));
            g2.drawImage(img, 0, 0, size, size, null);
        } else {
            g2.setColor(new Color(29, 191, 115));
            g2.fillOval(0, 0, size, size);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 40));
            String letra = (usuario.getNombreUsuario() != null) ? usuario.getNombreUsuario().substring(0, 1).toUpperCase() : "?";
            FontMetrics fm = g2.getFontMetrics();
            int x = (size - fm.stringWidth(letra)) / 2;
            int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(letra, x, y);
        }
        g2.dispose();
        lblAvatar.setIcon(new ImageIcon(imagenRedonda));
        lblAvatar.repaint();
    }

    private void estilizarTarjeta(JPanel p) {
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(20, 30, 20, 30)
        ));
    }
}