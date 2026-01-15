/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Archivo nuevo: Vista/ModernScrollBarUI.java
package Vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ModernScrollBarUI extends BasicScrollBarUI {

    // Colores para el scroll
    private final Color trackColor = new Color(245, 245, 245); // Color de fondo (igual al panel)
    private final Color thumbColor = new Color(180, 180, 180, 150); // Color del "pulgar" (semitransparente)
    private final int scrollBarWidth = 8; // Ancho del scroll

  @Override
    public Dimension getPreferredSize(JComponent c) {
        // Establece el ancho de la barra
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            return new Dimension(scrollBarWidth, super.getPreferredSize(c).height);
        } else {
            return new Dimension(super.getPreferredSize(c).width, scrollBarWidth);
        }
    }

    // --- Oculta los botones de flecha ---
    private JButton createZeroButton() {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(0, 0));
        btn.setMinimumSize(new Dimension(0, 0));
        btn.setMaximumSize(new Dimension(0, 0));
        return btn;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    // --- Dibuja el fondo (el "track") ---
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(trackColor);
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2.dispose();
    }

    // --- Dibuja la barra deslizable (el "thumb") ---
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(thumbColor);
        
        // Dibuja un rectángulo redondeado
        int arc = scrollBarWidth - 4; // Hace que sea bien redondeado
        g2.fillRoundRect(thumbBounds.x + 1, thumbBounds.y + 1, thumbBounds.width - 2, thumbBounds.height - 2, arc, arc);
        
        g2.dispose();
    }
}