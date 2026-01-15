package Vista;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelos.Usuario;
import database.GestorDeDatos;

public class Panel_Administrador extends JFrame {

    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JButton btnHabilitar;
    private JButton btnDeshabilitar;
    private JButton btnExportar;
    private JButton btnCerrarSesion;

    public Panel_Administrador(Usuario user) {
        initComponents(user);
    }

    private void initComponents(Usuario user) {
        setTitle("Panel del Administrador - " + user.getNombreUsuario());
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = { "Nombre de Usuario", "Correo", "Tipo de Usuario", "Status" };

        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaUsuarios);

        cargarUsuarios();

        JPanel panelBotones = new JPanel();
        btnHabilitar = new JButton("Habilitar");
        btnDeshabilitar = new JButton("Deshabilitar");
        btnExportar = new JButton("Exportar Tabla");
        btnCerrarSesion = new JButton("Cerrar sesión");

        btnHabilitar.setBackground(new Color(40, 167, 69)); // Verde más visible
        btnHabilitar.setForeground(Color.WHITE);
        btnHabilitar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHabilitar.setOpaque(true);
        btnHabilitar.setBorderPainted(false);

        btnDeshabilitar.setBackground(new Color(220, 53, 69)); // Rojo más visible
        btnDeshabilitar.setForeground(Color.WHITE);
        btnDeshabilitar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDeshabilitar.setOpaque(true);
        btnDeshabilitar.setBorderPainted(false);

        btnExportar.setBackground(new Color(255, 193, 7)); // Amarillo/Naranja
        btnExportar.setForeground(Color.BLACK);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.setOpaque(true);
        btnExportar.setBorderPainted(false);

        btnCerrarSesion.setBackground(new Color(23, 162, 184)); // Azul más visible
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setOpaque(true);
        btnCerrarSesion.setBorderPainted(false);

        btnHabilitar.setFocusPainted(false);
        btnDeshabilitar.setFocusPainted(false);
        btnExportar.setFocusPainted(false);
        btnCerrarSesion.setFocusPainted(false);

        panelBotones.add(btnHabilitar);
        panelBotones.add(btnDeshabilitar);
        panelBotones.add(btnExportar);
        panelBotones.add(Box.createHorizontalStrut(20)); // separador
        panelBotones.add(btnCerrarSesion);

        btnHabilitar.addActionListener(e -> cambiarEstadoUsuario(true));
        btnDeshabilitar.addActionListener(e -> cambiarEstadoUsuario(false));
        btnExportar.addActionListener(e -> exportarTablaExcel());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        setLayout(new BorderLayout());
        add(titulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarUsuarios() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Usuario> usuarios = GestorDeDatos.listarTodosLosUsuarios();
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[] {
                    u.getNombreUsuario(),
                    u.getCorreo(),
                    u.getTipo(),
                    u.isActivo() ? "Habilitado" : "Deshabilitado"
            });
        }
    }

    private void cambiarEstadoUsuario(boolean habilitar) {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario de la tabla.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreUsuario = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        String estadoActualStr = (String) modeloTabla.getValueAt(filaSeleccionada, 3);
        boolean estadoActual = "Habilitado".equals(estadoActualStr);

        if (habilitar == estadoActual) {
            JOptionPane.showMessageDialog(this,
                    "El usuario ya está " + (habilitar ? "habilitado" : "deshabilitado") + ".");
            return;
        }

        boolean exito = GestorDeDatos.actualizarEstatusUsuario(nombreUsuario, habilitar);
        if (exito) {
            modeloTabla.setValueAt(habilitar ? "Habilitado" : "Deshabilitado", filaSeleccionada, 3);
            JOptionPane.showMessageDialog(this, "Estado cambiado a: " + (habilitar ? "Habilitado" : "Deshabilitado"));
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado en la base de datos.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> new Autenticador().setVisible(true)); // abrir login
        }
    }

    private void exportarTablaExcel() {
        // Generar nombre de archivo con fecha
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String fecha = sdf.format(new java.util.Date());
        String nombreArchivo = "TablaDeUsuarios_" + fecha + ".csv";

        // Obtener ruta de la carpeta admin
        String rutaCarpeta = utils.GestorDeArchivos.getRutaAdmin();
        java.io.File file = new java.io.File(rutaCarpeta + java.io.File.separator + nombreArchivo);

        try (java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(file))) {
            // Escribir encabezados
            for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
                pw.print(modeloTabla.getColumnName(i));
                if (i < modeloTabla.getColumnCount() - 1)
                    pw.print(",");
            }
            pw.println();

            // Escribir datos
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                    Object value = modeloTabla.getValueAt(i, j);
                    pw.print(value != null ? value.toString() : "");
                    if (j < modeloTabla.getColumnCount() - 1)
                        pw.print(",");
                }
                pw.println();
            }

            // Preguntar si desea abrir el archivo
            int opcion = JOptionPane.showConfirmDialog(this,
                    "Tabla exportada exitosamente a:\n" + file.getAbsolutePath()
                            + "\n\n¿Deseas abrir el archivo ahora?",
                    "Exportación Exitosa",
                    JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "No se pudo abrir el archivo automáticamente.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar la tabla: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}