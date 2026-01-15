// Reemplaza tu archivo: Vista/TarjetaIlustrador.java
package Vista;

import modelos.Ilustrador;
import modelos.Ilustracion;
import database.GestorDeDatos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder; // <-- ESTA LÍNEA FALTABA
import java.awt.*;
import java.awt.image.BufferedImage; 
import java.util.List;

public class TarjetaIlustrador extends JPanel {

    private Ilustrador ilustrador;

    private final int IMG_ANCHO = 240;
    private final int IMG_ALTO = 180;
    private final Color IMG_FONDO = Estilos.COLOR_FONDO_GRIS; 

    public TarjetaIlustrador(Ilustrador ilustrador, Runnable onIlustradorClick) {
        this.ilustrador = ilustrador;
        initComponent(onIlustradorClick);
    }

    private void initComponent(Runnable onIlustradorClick) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Estilos.COLOR_BORDE, 1), // Borde de Estilos
                new EmptyBorder(10, 10, 10, 10) 
        ));
        setBackground(Estilos.COLOR_FONDO_BLANCO); 
        
        setPreferredSize(new Dimension(260, 320));
        setMaximumSize(new Dimension(260, 320)); 
        
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (onIlustradorClick != null) {
                    onIlustradorClick.run();
                }
            }
        });

        // Panel para la imagen principal (una de sus ilustraciones)
        JLabel lblImagen = new JLabel();
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(IMG_ANCHO, IMG_ALTO)); 
        lblImagen.setOpaque(true);
        lblImagen.setBackground(IMG_FONDO); 

        List<Ilustracion> obras = GestorDeDatos.obtenerIlustracionesPorIlustrador(ilustrador.getNombreUsuario());
        if (!obras.isEmpty()) {
            ImageIcon icon = obras.get(0).getImageIcon(); 
            
            if (icon != null && icon.getImage() != null) {
                lblImagen.setIcon(redimensionarImagen(icon, IMG_ANCHO, IMG_ALTO));
            } else {
                lblImagen.setText("Imagen no disponible");
                lblImagen.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO); // Estilo
            }
        } else {
            lblImagen.setText("Sin obras aún");
            lblImagen.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO); // Estilo
        }
        add(lblImagen, BorderLayout.NORTH);

        // Panel para la información del ilustrador
        JPanel pInfo = new JPanel();
        pInfo.setLayout(new BoxLayout(pInfo, BoxLayout.Y_AXIS));
        pInfo.setBorder(new EmptyBorder(10, 5, 5, 5)); 
        pInfo.setBackground(Estilos.COLOR_FONDO_BLANCO); // Estilo

        JLabel lblNombre = new JLabel(ilustrador.getNombreUsuario());
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblNombre.setFont(Estilos.FUENTE_TEXTO_BOLD); // Estilo
        lblNombre.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL); // Estilo
        pInfo.add(lblNombre);

        String desc = ilustrador.getDescripcion();
        if (desc.length() > 60) {
            desc = desc.substring(0, 57) + "..."; 
        }
        JLabel lblDescripcion = new JLabel("<html><p style='width:200px;'>" + desc + "</p></html>");
        lblDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDescripcion.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO); // Estilo
        lblDescripcion.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO); // Estilo
        pInfo.add(lblDescripcion);
        
        pInfo.add(Box.createVerticalGlue()); 

        JLabel lblPrecio = new JLabel(String.format("Desde: $%.2f", ilustrador.getPrecioBase()));
        lblPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblPrecio.setFont(Estilos.FUENTE_TEXTO_BOLD); // Estilo
        lblPrecio.setForeground(Estilos.COLOR_TEXTO_LINK); // Estilo (azul)
        pInfo.add(lblPrecio);

        add(pInfo, BorderLayout.CENTER);
    }
    
    // (Sin cambios)
    private ImageIcon redimensionarImagen(ImageIcon icon, int ancho, int alto) {
        Image imgOriginal = icon.getImage();
        int imgAncho = imgOriginal.getWidth(null);
        int imgAlto = imgOriginal.getHeight(null);

        double ratioImg = (double) imgAncho / imgAlto;
        double ratioContenedor = (double) ancho / alto;

        int nuevoAncho, nuevoAlto;
        if (ratioImg > ratioContenedor) {
            nuevoAncho = ancho;
            nuevoAlto = (int) (ancho / ratioImg);
        } else {
            nuevoAlto = alto;
            nuevoAncho = (int) (alto * ratioImg);
        }

        int x = (ancho - nuevoAncho) / 2;
        int y = (alto - nuevoAlto) / 2;

        BufferedImage imgRedimensionada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imgRedimensionada.createGraphics();

        g2d.setColor(IMG_FONDO);
        g2d.fillRect(0, 0, ancho, alto);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(imgOriginal, x, y, nuevoAncho, nuevoAlto, null);
        g2d.dispose();

        return new ImageIcon(imgRedimensionada);
    }
}