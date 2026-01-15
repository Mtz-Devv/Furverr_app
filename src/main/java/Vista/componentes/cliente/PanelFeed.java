package Vista.componentes.cliente;

import Vista.Estilos;
import Vista.TarjetaObra; // <--- IMPORTANTE: Usamos la tarjeta con lógica
import database.GestorDeDatos;
import modelos.Ilustracion;
import modelos.Usuario; // <--- Necesario para saber quién compra

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelFeed extends JPanel {

    private JPanel feedContainer;
    private JPanel col1, col2, col3;
    private JButton btnOrderBy;
    
    // Lista en memoria
    private List<Ilustracion> listaActualGigs;
    
    private Component parentContext;
    private Usuario clienteActual; // <--- VARIABLE NUEVA: El que va a comprar

    public PanelFeed() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(null);

        this.listaActualGigs = new ArrayList<>();

        // 1. Barra Superior (Filtros)
        add(crearTopBar(), BorderLayout.NORTH);

        // 2. Área de Scroll
        JScrollPane scroll = crearAreaFeed();
        add(scroll, BorderLayout.CENTER);

        // 3. Carga Inicial
        cargarFeed(""); 
    }
    
    public void setParentContext(Component parent) {
        this.parentContext = parent;
    }

    // --- MÉTODO NUEVO: Recibe al cliente desde Panel_Cliente ---
    public void setCliente(Usuario u) {
        this.clienteActual = u;
    }

    private JPanel crearTopBar() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(new EmptyBorder(10, 20, 0, 20));

        btnOrderBy = new JButton("Order by ▾");
        btnOrderBy.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnOrderBy.setBackground(Color.WHITE);
        btnOrderBy.setFocusPainted(false);
        btnOrderBy.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        btnOrderBy.setPreferredSize(new Dimension(180, 35));
        btnOrderBy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPopupMenu menuOrden = new JPopupMenu();
        menuOrden.setBackground(Color.WHITE);
        menuOrden.setBorder(new LineBorder(Color.LIGHT_GRAY));
        
        String[] opciones = {"Relevancia", "Lo más nuevo", "Precio: Menor a Mayor", "Precio: Mayor a Menor"};
        
        for (String op : opciones) {
            JMenuItem item = new JMenuItem(op);
            item.setBackground(Color.WHITE);
            item.setFont(new Font("SansSerif", Font.PLAIN, 12));
            
            item.addActionListener(e -> {
                btnOrderBy.setText(op);
                OrderClientFeed.aplicarOrden(listaActualGigs, op);
                renderizarGigs(); 
            });
            menuOrden.add(item);
        }

        btnOrderBy.addActionListener(e -> menuOrden.show(btnOrderBy, 0, btnOrderBy.getHeight()));
        topBar.add(btnOrderBy);
        return topBar;
    }

    private JScrollPane crearAreaFeed() {
        feedContainer = new JPanel(new GridLayout(1, 3, 20, 0)); 
        feedContainer.setBackground(Color.WHITE);
        feedContainer.setBorder(new EmptyBorder(20, 40, 20, 40)); 

        col1 = new JPanel(); col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS)); col1.setBackground(Color.WHITE);
        col2 = new JPanel(); col2.setLayout(new BoxLayout(col2, BoxLayout.Y_AXIS)); col2.setBackground(Color.WHITE);
        col3 = new JPanel(); col3.setLayout(new BoxLayout(col3, BoxLayout.Y_AXIS)); col3.setBackground(Color.WHITE);

        feedContainer.add(col1);
        feedContainer.add(col2);
        feedContainer.add(col3);

        JScrollPane scroll = new JScrollPane(feedContainer);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        return scroll;
    }

    public void cargarFeed(String busqueda) {
        this.listaActualGigs = GestorDeDatos.buscarGigsActivos(busqueda);
        btnOrderBy.setText("Order by ▾");
        renderizarGigs();
    }

    /**
     * AQUÍ ESTÁ EL CAMBIO CLAVE:
     * Usamos 'TarjetaObra' en lugar de crear paneles manuales.
     */
    private void renderizarGigs() {
        col1.removeAll();
        col2.removeAll();
        col3.removeAll();

        if (listaActualGigs.isEmpty()) {
            JLabel emptyLabel = new JLabel("No se encontraron resultados.");
            emptyLabel.setFont(Estilos.FUENTE_TITULO_H3);
            col2.add(emptyLabel); 
        } else {
            int contador = 0;
            for (Ilustracion gig : listaActualGigs) {
                
                // --- USAMOS LA TARJETA CON LÓGICA DE COMPRA ---
                // Le pasamos el gig y el cliente actual para que el botón funcione
                TarjetaObra card = new TarjetaObra(gig, clienteActual);
                
                if (contador % 3 == 0) {
                    col1.add(card); col1.add(Box.createVerticalStrut(20));
                } else if (contador % 3 == 1) {
                    col2.add(card); col2.add(Box.createVerticalStrut(20));
                } else {
                    col3.add(card); col3.add(Box.createVerticalStrut(20));
                }
                contador++;
            }
        }

        feedContainer.revalidate();
        feedContainer.repaint();
    }
}