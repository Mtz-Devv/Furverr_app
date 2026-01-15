/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista.componentes;

import Vista.Estilos;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TagsInputField extends JPanel {

    private JTextField txtInput;
    private JPanel panelTags; // Donde se dibujan las burbujas
    private JPopupMenu popupSugerencias;
    private List<String> etiquetasSeleccionadas;

    // Lista de ejemplo (esto debería venir de la BD en el futuro)
    private static final String[] SUGERENCIAS_DB = {
            "anime", "animation", "animal", "animated", "abstract", "art",
            "background", "banner", "branding", "business card",
            "cartoon", "character", "comic", "concept art",
            "design", "digital art", "drawing",
            "fantasy", "flat design", "flyer",
            "game art", "gif", "graphic design",
            "illustration", "icon", "isometric",
            "logo", "landscape", "line art",
            "manga", "minimalist", "modern",
            "nft", "neon",
            "pixel art", "portrait", "poster",
            "realistic", "retro",
            "sketch", "social media", "sticker", "storyboard",
            "tattoo", "twitch", "typography",
            "ui/ux", "vector", "video editing",
            "watercolor", "web design"
    };

    public TagsInputField() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(Estilos.BORDE_CAMPO_TEXTO);
        etiquetasSeleccionadas = new ArrayList<>();

        // Panel para las etiquetas visuales (FlowLayout para que se acomoden)
        panelTags = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelTags.setOpaque(false);
        add(panelTags, BorderLayout.WEST);

        // Campo de texto
        txtInput = new JTextField();
        txtInput.setBorder(null);
        txtInput.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        add(txtInput, BorderLayout.CENTER);

        // Popup de sugerencias
        popupSugerencias = new JPopupMenu();
        popupSugerencias.setBackground(Color.WHITE);
        popupSugerencias.setBorder(new LineBorder(Estilos.COLOR_BORDE));

        // Listeners
        txtInput.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                actualizarSugerencias();
            }

            public void removeUpdate(DocumentEvent e) {
                actualizarSugerencias();
            }

            public void changedUpdate(DocumentEvent e) {
                actualizarSugerencias();
            }
        });

        txtInput.addActionListener(e -> {
            String texto = txtInput.getText().trim();
            if (!texto.isEmpty()) {
                agregarEtiqueta(texto);
                txtInput.setText("");
                popupSugerencias.setVisible(false);
            }
        });
    }

    private void actualizarSugerencias() {
        String texto = txtInput.getText().trim().toLowerCase();
        popupSugerencias.setVisible(false);
        popupSugerencias.removeAll();

        if (texto.isEmpty())
            return;

        for (String sug : SUGERENCIAS_DB) {
            if (sug.startsWith(texto) && !etiquetasSeleccionadas.contains(sug.toUpperCase())) {
                JMenuItem item = new JMenuItem(sug);
                item.setBackground(Color.WHITE);
                item.setFont(Estilos.FUENTE_TEXTO_REGULAR);

                // Efecto Hover Verde (Captura 5)
                item.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        item.setBackground(Estilos.COLOR_VERDE_PRIMARIO);
                        item.setForeground(Color.WHITE);
                    }

                    public void mouseExited(MouseEvent e) {
                        item.setBackground(Color.WHITE);
                        item.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
                    }
                });

                item.addActionListener(e -> {
                    agregarEtiqueta(sug);
                    txtInput.setText("");
                    popupSugerencias.setVisible(false);
                    txtInput.requestFocus();
                });
                popupSugerencias.add(item);
            }
        }

        if (popupSugerencias.getComponentCount() > 0) {
            popupSugerencias.show(txtInput, 0, txtInput.getHeight());
            txtInput.requestFocus();
        }
    }

    private void agregarEtiqueta(String texto) {
        if (etiquetasSeleccionadas.size() >= 5)
            return; // Máximo 5 tags

        String tagUpper = texto.toUpperCase();
        if (etiquetasSeleccionadas.contains(tagUpper))
            return; // No duplicados

        etiquetasSeleccionadas.add(tagUpper);

        // Crear la "burbuja" visual (Captura 6)
        JPanel burbuja = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        burbuja.setBackground(new Color(230, 230, 230)); // Gris claro
        burbuja.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        JLabel lblTexto = new JLabel(tagUpper);
        lblTexto.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblTexto.setForeground(Color.DARK_GRAY);

        JLabel lblX = new JLabel("✕");
        lblX.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblX.setForeground(Color.GRAY);
        lblX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Acción de eliminar al hacer clic en la X o en la burbuja
        MouseAdapter borrarTag = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                panelTags.remove(burbuja);
                etiquetasSeleccionadas.remove(tagUpper);
                revalidate();
                repaint();
            }

            public void mouseEntered(MouseEvent e) {
                lblX.setForeground(Color.RED);
            }

            public void mouseExited(MouseEvent e) {
                lblX.setForeground(Color.GRAY);
            }
        };

        lblX.addMouseListener(borrarTag);
        burbuja.addMouseListener(borrarTag); // También borra al clic en la burbuja

        burbuja.add(lblTexto);
        burbuja.add(lblX);

        panelTags.add(burbuja);
        revalidate();
        repaint();
    }

    public String getEtiquetasString() {
        return String.join(",", etiquetasSeleccionadas);
    }

    public void limpiar() {
        panelTags.removeAll();
        etiquetasSeleccionadas.clear();
        txtInput.setText("");
        revalidate();
        repaint();
    }
}
