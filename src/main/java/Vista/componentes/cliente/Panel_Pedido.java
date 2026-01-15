package Vista.componentes.cliente;

import database.GestorDeDatos;
import modelos.Usuario;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Panel_Pedido extends JFrame {

    private Usuario usuario;
    private JTable tablaPedidos;
    private DefaultTableModel modeloTabla;
    
    // Lista Maestra: Contiene TODOS los datos (ID, Precio, Fechas) aunque no se vean
    // Estructura del Object[]: 
    // [0]ID, [1]Servicio, [2]Ilustrador, [3]Total, [4]Entrega, [5]Estado, [6]FechaCreacion
    private List<Object[]> listaDatosCompleta; 

    public Panel_Pedido(Usuario user) {
        this.usuario = user;
        this.listaDatosCompleta = new ArrayList<>();
        
        setTitle("Mis Pedidos - " + user.getNombreUsuario());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // 1. HEADER CON BOTONES
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitulo = new JLabel("Historial de Pedidos");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        
        // Panel de Botones (Derecha)
        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBotones.setOpaque(false);
        
        JButton btnVerMas = new JButton("Ver Detalles");
        estilizarBoton(btnVerMas, new Color(50, 150, 150));
        btnVerMas.setForeground(Color.BLACK);
        
        JButton btnOrdenar = new JButton("⇅ Ordenar por...");
        estilizarBoton(btnOrdenar, new Color(240, 240, 240));
        btnOrdenar.setForeground(Color.BLACK);
        
        pBotones.add(btnOrdenar);
        pBotones.add(btnVerMas);
        
        header.add(lblTitulo, BorderLayout.WEST);
        header.add(pBotones, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // 2. CONFIGURACIÓN DE LA TABLA (Solo columnas visibles)
        // OJO: Solo ponemos las columnas que el usuario pidió ver en la lista principal
        String[] columnasVisibles = {"Servicio", "Ilustrador", "Entrega Estimada", "Estado"};
        
        modeloTabla = new DefaultTableModel(columnasVisibles, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaPedidos = new JTable(modeloTabla);
        tablaPedidos.setRowHeight(40);
        tablaPedidos.setShowVerticalLines(false);
        tablaPedidos.setGridColor(new Color(230, 230, 230));
        tablaPedidos.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaPedidos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPedidos.setSelectionBackground(new Color(236, 255, 240));
        tablaPedidos.setSelectionForeground(Color.BLACK);

        JTableHeader th = tablaPedidos.getTableHeader();
        th.setBackground(Color.WHITE);
        th.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Centrar columnas
        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        centro.setHorizontalAlignment(JLabel.CENTER);
        // Centramos Entrega (col 2) y Estado (col 3)
        tablaPedidos.getColumnModel().getColumn(2).setCellRenderer(centro);
        tablaPedidos.getColumnModel().getColumn(3).setCellRenderer(centro);

        JScrollPane scroll = new JScrollPane(tablaPedidos);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        add(scroll, BorderLayout.CENTER);

        // 3. LÓGICA DE BOTONES
        btnVerMas.addActionListener(e -> mostrarDetallesPedido());
        btnOrdenar.addActionListener(e -> mostrarMenuOrdenar(btnOrdenar));

        // 4. CARGAR DATOS INICIALES
        cargarDatosDesdeBD();
    }

    private void cargarDatosDesdeBD() {
        Object[][] rawData = GestorDeDatos.obtenerPedidosPorCliente(usuario.getNombreUsuario());
        listaDatosCompleta.clear();
        
        listaDatosCompleta.addAll(Arrays.asList(rawData));
        
        refrescarTabla();
    }

    /**
     * Vuelve a pintar la tabla basándose en el orden actual de listaDatosCompleta
     */
    private void refrescarTabla() {
        modeloTabla.setRowCount(0);
        
        for (Object[] row : listaDatosCompleta) {
            // Solo agregamos al modelo las columnas VISIBLES
            // [1]Servicio, [2]Ilustrador, [4]Entrega, [5]Estado
            modeloTabla.addRow(new Object[]{
                row[1], 
                row[2], 
                row[4], 
                row[5]
            });
        }
        
        if (listaDatosCompleta.isEmpty()) {
            // Opcional: Mostrar mensaje en la tabla vacía
        }
    }

    private void mostrarDetallesPedido() {
        int row = tablaPedidos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un pedido de la lista primero.");
            return;
        }

        // Recuperamos los datos ocultos usando el índice de la fila seleccionada
        Object[] datosCompletos = listaDatosCompleta.get(row);
        
        // Extraemos los datos ocultos
        int id = (int) datosCompletos[0];
        double total = (double) datosCompletos[3];
        String fechaCreacion = (String) datosCompletos[6];
        String servicio = (String) datosCompletos[1];

        // Mostramos el Popup
        String mensaje = "<html><h3>Detalles del Pedido #" + id + "</h3>" +
                         "<b>Servicio:</b> " + servicio + "<br>" +
                         "<b>Fecha de Creación:</b> " + fechaCreacion + "<br>" +
                         "<b>Total a Pagar:</b> <font color='green'>$" + total + "</font></html>";

        JOptionPane.showMessageDialog(this, mensaje, "Información Extendida", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarMenuOrdenar(JButton botonInvoker) {
        JPopupMenu menu = new JPopupMenu();
        
        JMenuItem itemPrecioMayor = new JMenuItem("Precio: Mayor a Menor");
        JMenuItem itemPrecioMenor = new JMenuItem("Precio: Menor a Mayor");
        JMenuItem itemReciente = new JMenuItem("Más Reciente (Creación)");
        JMenuItem itemEstado = new JMenuItem("Estado");

        // Lógica de Ordenamiento
        itemPrecioMayor.addActionListener(e -> ordenarPor((o1, o2) -> Double.compare((double)o2[3], (double)o1[3])));
        itemPrecioMenor.addActionListener(e -> ordenarPor((o1, o2) -> Double.compare((double)o1[3], (double)o2[3])));
        
        // Ordenar por fecha (String SQL formato YYYY-MM-DD HH:MM:SS se ordena bien alfabéticamente)
        itemReciente.addActionListener(e -> ordenarPor((o1, o2) -> ((String)o2[6]).compareTo((String)o1[6])));
        
        itemEstado.addActionListener(e -> ordenarPor((o1, o2) -> ((String)o1[5]).compareTo((String)o2[5])));

        menu.add(itemPrecioMayor);
        menu.add(itemPrecioMenor);
        menu.add(itemReciente);
        menu.addSeparator();
        menu.add(itemEstado);
        
        menu.show(botonInvoker, 0, botonInvoker.getHeight());
    }

    private void ordenarPor(Comparator<Object[]> comparador) {
        listaDatosCompleta.sort(comparador);
        refrescarTabla();
    }

    private void estilizarBoton(JButton btn, Color bg) {
        btn.setFocusPainted(false);
        btn.setBackground(bg);
        if(bg.equals(Color.WHITE) || bg.getRed() > 200) btn.setForeground(Color.BLACK);
        else btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        btn.setBorder(new LineBorder(Color.LIGHT_GRAY, 1, true));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 30));
    }
}