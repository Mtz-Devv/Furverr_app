package Vista.componentes;

import Vista.Estilos;
import Vista.ModernScrollBarUI;
import Vista.Panel_Ilustrador;
import database.GestorDeDatos;
import modelos.Ilustracion;
import modelos.Paquete;
import modelos.GigExtra;
import modelos.Faq;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GigsView {

    private Panel_Ilustrador panelPrincipal;
    private CardLayout wizardLayout;
    private JPanel wizardPanel;

    // --- Datos Temporales del Wizard ---
    private String tempTitulo;
    private String tempCategoria;
    private String tempEtiquetas;
    private List<Paquete> tempPaquetes = new ArrayList<>();
    private List<GigExtra> tempExtras = new ArrayList<>();
    private String tempDescripcion;
    private List<Faq> tempFaqs = new ArrayList<>();
    private List<String> tempRutasGaleria = new ArrayList<>();

    // --- Componentes UI Wizard ---
    private JTextArea txtTituloGig, txtDescripcionFinal, txtRequerimientos;
    private JComboBox<String> comboCategoria1, comboCategoria2;
    private TagsInputField tagsInput;
    private Map<String, String[]> categoriasMap;
    private JPanel panelMetadataContainer, panelPreviewGaleria, panelExtrasContainer, panelFaqsContainer;
    
    // Componentes Paso 2 (Precios)
    private JCheckBox chkOfferPackages, chkFastDelivery, chkAddRevision, chkSourceFile;
    private JTextField txtBasicName, txtStdName, txtPremName;
    private JTextArea txtBasicDesc, txtStdDesc, txtPremDesc;
    private JComboBox<String> cmbBasicDel, cmbStdDel, cmbPremDel;
    private JComboBox<String> cmbBasicRev, cmbStdRev, cmbPremRev;
    private JSpinner spinBasicPrice, spinStdPrice, spinPremPrice;
    private JSpinner spinFastPrice, spinRevPrice, spinSourcePrice;

    public GigsView(Panel_Ilustrador panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        inicializarCategorias();
    }

    // --- MÉTODOS PÚBLICOS ---
    public JScrollPane getPanelListaGigs() {
        return crearPanelGigs();
    }

    public JScrollPane getPanelCrearGig() {
        return crearPanelWizard();
    }

    // ==================================================================
    // --- LISTA DE GIGS (TABLA ESTILO FIVERR) ---
    // ==================================================================
    private JScrollPane crearPanelGigs() {
        JPanel panelVistaGigs = new JPanel(new BorderLayout(20, 20));
        panelVistaGigs.setBackground(Estilos.COLOR_FONDO_GRIS);
        panelVistaGigs.setBorder(Estilos.PADDING_PANEL_GRIS);

        // Header
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        JLabel lblTitle = new JLabel("Servicios");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H1);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        panelHeader.add(lblTitle, BorderLayout.WEST);

        JButton btnCrearGig = new JButton("Crear un nuevo Servicio");
        Estilos.estilizarBotonPrimario(btnCrearGig);
        btnCrearGig.addActionListener(e -> {
            reiniciarWizard();
            panelPrincipal.getMainCardLayout().show(panelPrincipal.getMainCardsPanel(), Panel_Ilustrador.VISTA_GIGS_CREAR);
        });
        panelHeader.add(btnCrearGig, BorderLayout.EAST);
        panelVistaGigs.add(panelHeader, BorderLayout.NORTH);

        // Contenido (Tabs + Tablas)
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.setOpaque(false);
        panelContenido.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, 0, 0));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Estilos.FUENTE_TEXTO_BOLD);
        tabbedPane.setBackground(Estilos.COLOR_FONDO_GRIS);

        // Obtener todos los Gigs
        List<Ilustracion> todosLosGigs = GestorDeDatos.obtenerIlustracionesPorIlustrador(panelPrincipal.getIlustrador().getNombreUsuario());

        // Filtrar Activos y Borradores
        List<Ilustracion> activos = todosLosGigs.stream()
                .filter(g -> "ACTIVO".equalsIgnoreCase(g.getEstado()))
                .collect(Collectors.toList());

        List<Ilustracion> borradores = todosLosGigs.stream()
                .filter(g -> "BORRADOR".equalsIgnoreCase(g.getEstado()))
                .collect(Collectors.toList());

        // Tab Activos
        tabbedPane.addTab("ACTIVOS (" + activos.size() + ")", crearTablaGigs(activos, false));
        // Tab Borradores
        tabbedPane.addTab("BORRADOR (" + borradores.size() + ")", crearTablaGigs(borradores, true));

        panelContenido.add(tabbedPane, BorderLayout.CENTER);
        panelVistaGigs.add(panelContenido, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(panelVistaGigs);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getViewport().setBackground(Estilos.COLOR_FONDO_GRIS);
        return scrollPane;
    }

    private JComponent crearTablaGigs(List<Ilustracion> gigs, boolean esBorrador) {
        if (gigs.isEmpty()) {
            return ComponentUtils.crearPanelPlaceholderWIP(esBorrador ? "No tienes borradores" : "No tienes servicios activos");
        }

        // Columnas
        String[] columnNames = {"GIG", "IMPRESIONES", "CLICKS", "PEDIDOS", "CANCELACIONES", ""}; // La última es para el menú

        // Datos
        Object[][] data = new Object[gigs.size()][6];
        for (int i = 0; i < gigs.size(); i++) {
            Ilustracion g = gigs.get(i);
            data[i][0] = g; // Guardamos el OBJETO completo en la columna 0 para el renderer
            data[i][1] = "0"; // Placeholder stats
            data[i][2] = "0";
            data[i][3] = "0";
            data[i][4] = "0%";
            data[i][5] = g; // Guardamos objeto para el botón de acción
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Solo la columna de acciones es "editable" (clicable)
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(70); // Altura para que quepa la imagen
        table.setShowVerticalLines(false);
        table.setGridColor(Estilos.COLOR_BORDE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setForeground(Color.GRAY);
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Estilos.COLOR_BORDE));
        table.setBackground(Color.WHITE);
        
        // Renderers
        table.getColumnModel().getColumn(0).setCellRenderer(new GigCellRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ActionButtonEditor(new JCheckBox(), esBorrador));
        
        // Ancho de columnas
        table.getColumnModel().getColumn(0).setPreferredWidth(400); // GIG ancho
        table.getColumnModel().getColumn(5).setPreferredWidth(50);  // Botón pequeño

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(Estilos.COLOR_BORDE));
        return scroll;
    }

    // --- MÉTODO PARA REFRESCAR LA VISTA TRAS CAMBIOS ---
    private void refrescarVista() {
        JScrollPane nuevoPanelGigs = crearPanelGigs();
        JPanel mainCards = panelPrincipal.getMainCardsPanel();
        for(Component c : mainCards.getComponents()) {
            if (Panel_Ilustrador.VISTA_GIGS.equals(c.getName())) {
                mainCards.remove(c);
                break;
            }
        }
        mainCards.add(nuevoPanelGigs, Panel_Ilustrador.VISTA_GIGS);
        panelPrincipal.getMainCardLayout().show(mainCards, Panel_Ilustrador.VISTA_GIGS);
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    // --- RENDERER PARA LA CELDA "GIG" (Imagen + Título) ---
    class GigCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new BorderLayout(10, 0));
            panel.setBackground(Color.WHITE);
            panel.setBorder(new EmptyBorder(5, 10, 5, 10));

            if (value instanceof Ilustracion) {
                Ilustracion gig = (Ilustracion) value;
                JLabel lblImg = new JLabel();
                lblImg.setPreferredSize(new Dimension(80, 50));
                lblImg.setOpaque(true);
                lblImg.setBackground(Estilos.COLOR_FONDO_GRIS);
                
                ImageIcon icon = gig.getImageIcon();
                if (icon != null) {
                     Image img = icon.getImage().getScaledInstance(80, 50, Image.SCALE_SMOOTH);
                     lblImg.setIcon(new ImageIcon(img));
                }
                
                JLabel lblTitulo = new JLabel(gig.getTitulo());
                lblTitulo.setFont(Estilos.FUENTE_TEXTO_BOLD);
                lblTitulo.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);

                panel.add(lblImg, BorderLayout.WEST);
                panel.add(lblTitulo, BorderLayout.CENTER);
            }
            return panel;
        }
    }

    // --- RENDERER PARA EL BOTÓN DE ACCIÓN (v) ---
    class ActionButtonRenderer extends JButton implements TableCellRenderer {
        public ActionButtonRenderer() {
            setText("▾");
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFont(new Font("SansSerif", Font.BOLD, 18));
            setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // --- EDITOR PARA EL BOTÓN DE ACCIÓN (Maneja el Clic y el Menú) ---
    class ActionButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean esBorrador;
        private Ilustracion currentGig;

        public ActionButtonEditor(JCheckBox checkBox, boolean esBorrador) {
            super(checkBox);
            this.esBorrador = esBorrador;
            button = new JButton("▾");
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFont(new Font("SansSerif", Font.BOLD, 18));
            button.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
            
            button.addActionListener(e -> {
                mostrarMenuAcciones(button);
                fireEditingStopped(); // Detiene la edición después del clic
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof Ilustracion) {
                this.currentGig = (Ilustracion) value;
            }
            return button;
        }

        private void mostrarMenuAcciones(Component invoker) {
            JPopupMenu menu = new JPopupMenu();
            menu.setBackground(Color.WHITE);
            menu.setBorder(new LineBorder(Estilos.COLOR_BORDE));

            // 1. Vista Previa
            JMenuItem itemPreview = new JMenuItem("Vista previa");
            // 2. Mover (Dinámico)
            JMenuItem itemMove = new JMenuItem(esBorrador ? "Enviar a activos" : "Enviar a borradores");
            // 3. Eliminar
            JMenuItem itemDelete = new JMenuItem("Eliminar");

            for(JMenuItem item : new JMenuItem[]{itemPreview, itemMove, itemDelete}) {
                item.setBackground(Color.WHITE);
                item.setFont(Estilos.FUENTE_TEXTO_REGULAR);
                menu.add(item);
            }
            
            // --- ACCIONES ---
            itemPreview.addActionListener(e -> {
                // CORRECCIÓN: Agregamos 'null' como cuarto parámetro (Cliente)
                // ya que el ilustrador solo está viendo una vista previa de su propio gig.
                PreviewGig preview = new PreviewGig(currentGig, panelPrincipal.getIlustrador(), panelPrincipal, null);
                preview.setVisible(true);
            });

            itemMove.addActionListener(e -> {
                String nuevoEstado = esBorrador ? "ACTIVO" : "BORRADOR";
                boolean ok = GestorDeDatos.actualizarEstadoGig(currentGig.getId(), nuevoEstado);
                if (ok) {
                    refrescarVista();
                    JOptionPane.showMessageDialog(panelPrincipal, "Servicio movido a " + (esBorrador ? "Activos" : "Borradores"));
                } else {
                    JOptionPane.showMessageDialog(panelPrincipal, "Error al mover el servicio.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            itemDelete.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(panelPrincipal, 
                    "¿Estás seguro de eliminar este servicio permanentemente?", 
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean ok = GestorDeDatos.eliminarGig(currentGig.getId());
                    if (ok) {
                        refrescarVista();
                        JOptionPane.showMessageDialog(panelPrincipal, "Servicio eliminado.");
                    } else {
                        JOptionPane.showMessageDialog(panelPrincipal, "Error al eliminar el servicio.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            menu.show(invoker, invoker.getWidth() - 150, invoker.getHeight());
        }

        @Override
        public Object getCellEditorValue() {
            return currentGig;
        }
    }


    // ==================================================================
    // --- WIZARD DE CREACIÓN (LÓGICA COMPLETA) ---
    // ==================================================================
    
    private void inicializarCategorias() {
        categoriasMap = new HashMap<>();
        categoriasMap.put("GRÁFICOS Y DISEÑO", new String[] { "Diseño de Logo", "Arte para Juegos", "Ilustración", "Merch", "Storyboards" });
        categoriasMap.put("MARKETING DIGITAL", new String[] { "Redes Sociales", "SEO", "Publicidad", "Email Marketing" });
        categoriasMap.put("VIDEO Y ANIMACIÓN", new String[] { "Edición", "Animación 2D/3D", "Intros", "VFX" });
        categoriasMap.put("NEGOCIOS", new String[] { "Gestión", "Legal", "Asistencia", "Traducción" });
    }
    
    public void reiniciarWizard() {
        tempTitulo = ""; tempCategoria = ""; tempEtiquetas = ""; tempDescripcion = "";
        tempPaquetes.clear(); tempExtras.clear(); tempFaqs.clear(); tempRutasGaleria.clear();
        for (int i = 0; i < 3; i++) tempRutasGaleria.add(null);
        if (txtTituloGig != null) txtTituloGig.setText("Haré algo increíble...");
        if (tagsInput != null) tagsInput.limpiar();
        if (txtBasicName != null) txtBasicName.setText("Básico");
        
        if (panelPreviewGaleria != null) {
            panelPreviewGaleria.removeAll();
            for (int i = 0; i < 3; i++) panelPreviewGaleria.add(crearSlotImagen(i));
            panelPreviewGaleria.revalidate(); panelPreviewGaleria.repaint();
        }
        if (wizardLayout != null) wizardLayout.show(wizardPanel, "PASO_1");
    }
    
    private JScrollPane crearPanelWizard() {
        wizardLayout = new CardLayout();
        wizardPanel = new JPanel(wizardLayout);
        wizardPanel.setOpaque(false);

        wizardPanel.add(crearPaso1Resumen(), "PASO_1");
        wizardPanel.add(crearPaso2Precios(), "PASO_2");
        wizardPanel.add(crearPaso3Descripcion(), "PASO_3");
        wizardPanel.add(crearPaso4Requerimientos(), "PASO_4");
        wizardPanel.add(crearPaso5Galeria(), "PASO_5");
        wizardPanel.add(crearPaso6Publicar(), "PASO_6");

        JScrollPane scrollPane = new JScrollPane(wizardPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getViewport().setBackground(Estilos.COLOR_FONDO_GRIS);
        return scrollPane;
    }

    // --- PASO 1: RESUMEN ---
    private JPanel crearPaso1Resumen() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Estilos.COLOR_FONDO_GRIS);
        panel.setBorder(Estilos.PADDING_PANEL_GRIS);

        panel.add(crearWizardHeader(1, this::irAPaso2, "Guardar y Continuar"), BorderLayout.NORTH);

        JPanel panelForm = new JPanel();
        panelForm.setOpaque(false);
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));

        txtTituloGig = new JTextArea("Haré algo increíble...", 3, 30);
        txtTituloGig.setLineWrap(true);
        txtTituloGig.setWrapStyleWord(true);
        txtTituloGig.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        panelForm.add(crearSeccionForm("Título del Servicio", new JScrollPane(txtTituloGig)));
        panelForm.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        // --- CORRECCIÓN DE ORDEN: PRIMERO EL PANEL METADATA, LUEGO LAS LLAMADAS ---
        panelMetadataContainer = new JPanel(new BorderLayout(40, 10));
        panelMetadataContainer.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panelMetadataContainer.setBorder(Estilos.BORDE_TARJETA);
        
        // Crear Combos
        JPanel pCat = new JPanel(new BorderLayout(40, 10));
        pCat.setBackground(Estilos.COLOR_FONDO_BLANCO);
        pCat.setBorder(Estilos.BORDE_TARJETA);
        pCat.add(new JLabel("Categoría"), BorderLayout.WEST);
        
        comboCategoria1 = new JComboBox<>(categoriasMap.keySet().toArray(new String[0]));
        comboCategoria2 = new JComboBox<>();
        comboCategoria1.addActionListener(e -> {
            actualizarSubcategorias();
            actualizarMetadatos(); // Ahora es seguro llamar a esto
        });
        
        JPanel cp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cp.setOpaque(false);
        cp.add(comboCategoria1);
        cp.add(comboCategoria2);
        pCat.add(cp, BorderLayout.CENTER);
        panelForm.add(pCat);
        panelForm.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        // Añadir panel metadata al formulario
        panelForm.add(panelMetadataContainer);
        panelForm.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        tagsInput = new TagsInputField();
        panelForm.add(crearSeccionForm("Etiquetas de búsqueda", tagsInput));

        // Inicializar datos iniciales AHORA que todo está creado
        if (comboCategoria1.getItemCount() > 0) comboCategoria1.setSelectedIndex(0);
        actualizarSubcategorias(); // Esto llamará a actualizarMetadatos internamente

        panel.add(panelForm, BorderLayout.CENTER);
        return panel;
    }

    private void actualizarSubcategorias() {
        String cat = (String) comboCategoria1.getSelectedItem();
        if (cat != null)
            comboCategoria2.setModel(new DefaultComboBoxModel<>(categoriasMap.get(cat)));
    }

    private void actualizarMetadatos() {
        // Protección contra NullPointerException
        if (panelMetadataContainer == null) return;
        
        String categoria = (String) comboCategoria1.getSelectedItem();
        if (categoria == null) return;
        
        panelMetadataContainer.removeAll();
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(250, 1));
        JLabel lblTitle = new JLabel("Metadatos del Gig");
        lblTitle.setFont(Estilos.FUENTE_TEXTO_BOLD);
        leftPanel.add(lblTitle);
        leftPanel.add(Box.createVerticalStrut(15));
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);

        switch (categoria) {
            case "GRÁFICOS Y DISEÑO":
                agregarLabelMetadato(leftPanel, "ESTILO");
                agregarLabelMetadato(leftPanel, "FORMATO DE ARCHIVO");
                centerPanel.add(crearCombo("Estilo Artístico", new String[] { "Anime & Manga", "Cartoon", "Realista", "Pixel Art" }));
                centerPanel.add(crearChecks("Formatos", new String[] { "JPG", "PNG", "PSD", "AI" }));
                break;
            case "VIDEO Y ANIMACIÓN":
                agregarLabelMetadato(leftPanel, "TIPO");
                agregarLabelMetadato(leftPanel, "FORMATO");
                centerPanel.add(crearCombo("Tipo de Video", new String[] { "2D", "3D", "Motion" }));
                centerPanel.add(crearChecks("Formatos", new String[] { "MP4", "MOV", "GIF" }));
                break;
            case "MARKETING DIGITAL":
                agregarLabelMetadato(leftPanel, "PLATAFORMA");
                agregarLabelMetadato(leftPanel, "TIPO");
                centerPanel.add(crearCombo("Plataforma", new String[] { "Instagram", "Facebook", "TikTok" }));
                centerPanel.add(crearChecks("Contenido", new String[] { "Post", "Story", "Reel" }));
                break;
            case "NEGOCIOS":
                agregarLabelMetadato(leftPanel, "DOCUMENTO");
                agregarLabelMetadato(leftPanel, "FORMATO");
                centerPanel.add(crearCombo("Documento", new String[] { "Plan", "Contrato", "Reporte" }));
                centerPanel.add(crearChecks("Formatos", new String[] { "PDF", "DOCX", "XLSX" }));
                break;
        }
        panelMetadataContainer.add(leftPanel, BorderLayout.WEST);
        panelMetadataContainer.add(centerPanel, BorderLayout.CENTER);
        panelMetadataContainer.revalidate();
        panelMetadataContainer.repaint();
    }

    private void irAPaso2() {
        if (txtTituloGig.getText().isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal, "Falta título");
            return;
        }
        tempTitulo = txtTituloGig.getText();
        tempEtiquetas = tagsInput.getEtiquetasString();
        tempCategoria = comboCategoria1.getSelectedItem() + "/" + comboCategoria2.getSelectedItem();
        wizardLayout.show(wizardPanel, "PASO_2");
    }

    // --- PASO 2: PRECIOS ---
    private JPanel crearPaso2Precios() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Estilos.COLOR_FONDO_GRIS);
        panel.setBorder(Estilos.PADDING_PANEL_GRIS);
        panel.add(crearWizardHeader(2, this::irAPaso3, "Guardar y Continuar"), BorderLayout.NORTH);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JPanel headerPackages = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerPackages.setOpaque(false);
        chkOfferPackages = new JCheckBox("Ofrecer Paquetes (3 Niveles)");
        chkOfferPackages.setOpaque(false);
        chkOfferPackages.setFont(Estilos.FUENTE_TEXTO_BOLD);
        chkOfferPackages.addActionListener(e -> actualizarEstadoPaquetes());
        headerPackages.add(chkOfferPackages);
        content.add(headerPackages);

        JPanel tablaPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        tablaPanel.setOpaque(false);
        JPanel colLabels = new JPanel(new GridLayout(6, 1, 0, 10));
        colLabels.setOpaque(false);
        String[] labels = { "", "Nombre", "Descripción", "Entrega", "Revisiones", "Precio ($)" };
        for (String l : labels) {
            JLabel lbl = new JLabel(l);
            if (!l.isEmpty())
                lbl.setFont(Estilos.FUENTE_TEXTO_BOLD);
            colLabels.add(lbl);
        }
        tablaPanel.add(colLabels);

        txtBasicName = new JTextField("Básico");
        txtBasicDesc = new JTextArea(3, 10);
        cmbBasicDel = crearComboDias();
        cmbBasicRev = crearComboRev();
        spinBasicPrice = crearSpinnerPrecio();
        txtStdName = new JTextField("Estándar");
        txtStdDesc = new JTextArea(3, 10);
        cmbStdDel = crearComboDias();
        cmbStdRev = crearComboRev();
        spinStdPrice = crearSpinnerPrecio();
        txtPremName = new JTextField("Premium");
        txtPremDesc = new JTextArea(3, 10);
        cmbPremDel = crearComboDias();
        cmbPremRev = crearComboRev();
        spinPremPrice = crearSpinnerPrecio();
        
        tablaPanel.add(crearColumnaPaquete("BASIC", txtBasicName, txtBasicDesc, cmbBasicDel, cmbBasicRev, spinBasicPrice));
        tablaPanel.add(crearColumnaPaquete("STANDARD", txtStdName, txtStdDesc, cmbStdDel, cmbStdRev, spinStdPrice));
        tablaPanel.add(crearColumnaPaquete("PREMIUM", txtPremName, txtPremDesc, cmbPremDel, cmbPremRev, spinPremPrice));
        content.add(tablaPanel);
        content.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        JLabel lblStdExtras = new JLabel("Añadir servicios extra");
        lblStdExtras.setFont(Estilos.FUENTE_TITULO_H3);
        content.add(lblStdExtras);
        content.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        JPanel panelStdExtras = new JPanel();
        panelStdExtras.setLayout(new BoxLayout(panelStdExtras, BoxLayout.Y_AXIS));
        panelStdExtras.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panelStdExtras.setBorder(Estilos.BORDE_TARJETA);
        chkFastDelivery = new JCheckBox("Entrega extra rápida");
        spinFastPrice = crearSpinnerPrecio();
        chkAddRevision = new JCheckBox("Revisiones adicionales");
        spinRevPrice = crearSpinnerPrecio();
        chkSourceFile = new JCheckBox("Archivo fuente");
        spinSourcePrice = crearSpinnerPrecio();
        panelStdExtras.add(crearFilaExtraEstandar(chkFastDelivery, spinFastPrice));
        panelStdExtras.add(crearFilaExtraEstandar(chkAddRevision, spinRevPrice));
        panelStdExtras.add(crearFilaExtraEstandar(chkSourceFile, spinSourcePrice));
        content.add(panelStdExtras);
        content.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));

        JPanel panelCustomHeader = new JPanel(new BorderLayout());
        panelCustomHeader.setOpaque(false);
        JLabel lblCustom = new JLabel("Extras personalizados");
        lblCustom.setFont(Estilos.FUENTE_TEXTO_BOLD);
        JButton btnAddExtra = new JButton("+ Añadir Extra");
        Estilos.estilizarBotonTexto(btnAddExtra, false);
        btnAddExtra.addActionListener(e -> agregarFilaExtra());
        panelCustomHeader.add(lblCustom, BorderLayout.WEST);
        panelCustomHeader.add(btnAddExtra, BorderLayout.EAST);
        content.add(panelCustomHeader);
        panelExtrasContainer = new JPanel();
        panelExtrasContainer.setLayout(new BoxLayout(panelExtrasContainer, BoxLayout.Y_AXIS));
        panelExtrasContainer.setOpaque(false);
        content.add(panelExtrasContainer);
        actualizarEstadoPaquetes();

        JScrollPane scrollContent = new JScrollPane(content);
        scrollContent.setBorder(null);
        scrollContent.setOpaque(false);
        scrollContent.getViewport().setOpaque(false);
        panel.add(scrollContent, BorderLayout.CENTER);
        return panel;
    }

    @SuppressWarnings("unchecked")
    private void irAPaso3() {
        tempPaquetes.clear();
        tempExtras.clear();
        tempPaquetes.add(new Paquete("Basic", txtBasicName.getText(), txtBasicDesc.getText(),
                comboIdxToDias(cmbBasicDel), comboIdxToRev(cmbBasicRev), (int) spinBasicPrice.getValue()));
        if (chkOfferPackages.isSelected()) {
            tempPaquetes.add(new Paquete("Standard", txtStdName.getText(), txtStdDesc.getText(),
                    comboIdxToDias(cmbStdDel), comboIdxToRev(cmbStdRev), (int) spinStdPrice.getValue()));
            tempPaquetes.add(new Paquete("Premium", txtPremName.getText(), txtPremDesc.getText(),
                    comboIdxToDias(cmbPremDel), comboIdxToRev(cmbPremRev), (int) spinPremPrice.getValue()));
        }
        if (chkFastDelivery.isSelected())
            tempExtras.add(new GigExtra("Entrega Rápida", "Entrega prioritaria", (int) spinFastPrice.getValue(), 0));
        if (chkAddRevision.isSelected())
            tempExtras.add(new GigExtra("Revisión Extra", "Extra rev", (int) spinRevPrice.getValue(), 0));
        if (chkSourceFile.isSelected())
            tempExtras.add(new GigExtra("Archivo Fuente", "Source", (int) spinSourcePrice.getValue(), 0));
        for (Component c : panelExtrasContainer.getComponents()) {
            if (c instanceof JPanel) {
                try {
                    JPanel p = (JPanel) c;
                    JTextField tT = (JTextField) p.getComponent(1);
                    JTextField tD = (JTextField) p.getComponent(3);
                    JPanel pP = (JPanel) p.getComponent(4);
                    JSpinner sP = (JSpinner) pP.getComponent(1);
                    JComboBox<String> cD = (JComboBox<String>) pP.getComponent(3);
                    tempExtras.add(new GigExtra(tT.getText(), tD.getText(), (int) sP.getValue(), cD.getSelectedIndex()));
                } catch (Exception e) {
                }
            }
        }
        wizardLayout.show(wizardPanel, "PASO_3");
    }

    // --- PASO 3: DESC & FAQ ---
    private JPanel crearPaso3Descripcion() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Estilos.COLOR_FONDO_GRIS);
        panel.setBorder(Estilos.PADDING_PANEL_GRIS);
        panel.add(crearWizardHeader(3, this::irAPaso4, "Guardar y Continuar"), BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(Estilos.COLOR_FONDO_GRIS);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JPanel panelDesc = new JPanel(new BorderLayout(Estilos.ESPACIO_GRANDE, 0));
        panelDesc.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panelDesc.setBorder(Estilos.BORDE_TARJETA);
        JLabel lblDesc = new JLabel("Descripción");
        lblDesc.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblDesc.setVerticalAlignment(SwingConstants.TOP);
        lblDesc.setPreferredSize(new Dimension(200, 50));
        panelDesc.add(lblDesc, BorderLayout.WEST);
        txtDescripcionFinal = new JTextArea(15, 40);
        txtDescripcionFinal.setLineWrap(true);
        txtDescripcionFinal.setWrapStyleWord(true);
        txtDescripcionFinal.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        JScrollPane scrollDesc = new JScrollPane(txtDescripcionFinal);
        scrollDesc.setBorder(null);
        scrollDesc.setPreferredSize(new Dimension(400, 250));
        panelDesc.add(scrollDesc, BorderLayout.CENTER);
        content.add(panelDesc, gbc);

        gbc.gridy = 1;
        content.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE), gbc);

        gbc.gridy = 2;
        JPanel panelFaqSection = new JPanel(new BorderLayout(Estilos.ESPACIO_GRANDE, 0));
        panelFaqSection.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panelFaqSection.setBorder(Estilos.BORDE_TARJETA);
        JPanel faqHeader = new JPanel(new BorderLayout());
        faqHeader.setOpaque(false);
        JLabel lblFaq = new JLabel("Preguntas Frecuentes");
        lblFaq.setFont(Estilos.FUENTE_TEXTO_BOLD);
        faqHeader.add(lblFaq, BorderLayout.WEST);
        JButton btnAddFaq = new JButton("+ Añadir FAQ");
        Estilos.estilizarBotonTexto(btnAddFaq, false);
        btnAddFaq.addActionListener(e -> agregarDialogoFaq());
        faqHeader.add(btnAddFaq, BorderLayout.EAST);
        panelFaqSection.add(faqHeader, BorderLayout.NORTH);
        panelFaqsContainer = new JPanel();
        panelFaqsContainer.setLayout(new BoxLayout(panelFaqsContainer, BoxLayout.Y_AXIS));
        panelFaqsContainer.setOpaque(false);
        panelFaqsContainer.setBorder(new EmptyBorder(10, 0, 10, 0));
        panelFaqSection.add(panelFaqsContainer, BorderLayout.CENTER);
        content.add(panelFaqSection, gbc);

        gbc.gridy = 3;
        gbc.weighty = 1.0;
        content.add(Box.createVerticalGlue(), gbc);

        JScrollPane mainScroll = new JScrollPane(content);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(mainScroll, BorderLayout.CENTER);
        return panel;
    }

    private void irAPaso4() {
        if (txtDescripcionFinal.getText().isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal, "Falta descripción");
            return;
        }
        tempDescripcion = txtDescripcionFinal.getText();
        wizardLayout.show(wizardPanel, "PASO_4");
    }

    // --- PASO 4: REQUERIMIENTOS ---
    private JPanel crearPaso4Requerimientos() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Estilos.COLOR_FONDO_GRIS);
        panel.setBorder(Estilos.PADDING_PANEL_GRIS);
        panel.add(crearWizardHeader(4, this::irAPaso5, "Guardar y Continuar"), BorderLayout.NORTH);

        JPanel panelContent = new JPanel(new BorderLayout(0, 15));
        panelContent.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panelContent.setBorder(Estilos.BORDE_TARJETA);

        JPanel headerReq = new JPanel(new BorderLayout());
        headerReq.setOpaque(false);
        JLabel lblTitle = new JLabel("Obtén toda la información necesaria");
        lblTitle.setFont(Estilos.FUENTE_TITULO_H3);
        JLabel lblSub = new JLabel("Define instrucciones para tu comprador.");
        lblSub.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
        lblSub.setForeground(Color.GRAY);
        JPanel titles = new JPanel(new GridLayout(2, 1));
        titles.setOpaque(false);
        titles.add(lblTitle);
        titles.add(lblSub);
        headerReq.add(titles, BorderLayout.CENTER);
        panelContent.add(headerReq, BorderLayout.NORTH);

        txtRequerimientos = new JTextArea(5, 40);
        txtRequerimientos.setText("1. Por favor describe tu idea.\n2. Adjunta referencias.");
        txtRequerimientos.setLineWrap(true);
        txtRequerimientos.setWrapStyleWord(true);
        txtRequerimientos.setBorder(Estilos.BORDE_CAMPO_TEXTO);

        panelContent.add(new JScrollPane(txtRequerimientos), BorderLayout.CENTER);
        panel.add(panelContent, BorderLayout.CENTER);
        return panel;
    }

    private void irAPaso5() {
        wizardLayout.show(wizardPanel, "PASO_5");
    }

    // --- PASO 5: GALERÍA ---
    private JPanel crearPaso5Galeria() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Estilos.COLOR_FONDO_GRIS);
        panel.setBorder(Estilos.PADDING_PANEL_GRIS);
        panel.add(crearWizardHeader(5, this::irAPaso6, "Guardar y Continuar"), BorderLayout.NORTH);

        JPanel panelContent = new JPanel(new BorderLayout(0, 20));
        panelContent.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panelContent.setBorder(Estilos.BORDE_TARJETA);

        JLabel lblTitle = new JLabel("<html><center>Muestra tu trabajo (Mínimo 3 imágenes)</center></html>",
                SwingConstants.CENTER);
        lblTitle.setFont(Estilos.FUENTE_TITULO_H3);
        panelContent.add(lblTitle, BorderLayout.NORTH);

        // --- INICIALIZAR LA LISTA DE RUTAS ---
        if (tempRutasGaleria.isEmpty()) {
            for (int i = 0; i < 3; i++)
                tempRutasGaleria.add(null);
        }

        panelPreviewGaleria = new JPanel(new GridLayout(1, 3, 20, 0));
        panelPreviewGaleria.setOpaque(false);
        panelPreviewGaleria.setBorder(new EmptyBorder(20, 20, 20, 20));
        for (int i = 0; i < 3; i++)
            panelPreviewGaleria.add(crearSlotImagen(i));

        panelContent.add(panelPreviewGaleria, BorderLayout.CENTER);
        panel.add(panelContent, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearSlotImagen(int index) {
        JPanel slot = new JPanel(new BorderLayout());
        slot.setBackground(new Color(245, 245, 250));
        slot.setBorder(BorderFactory.createDashedBorder(Color.GRAY, 2, 5));
        slot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel lblIcon = new JLabel("<html><center>+<br>Foto " + (index + 1) + "</center></html>",
                SwingConstants.CENTER);
        slot.add(lblIcon, BorderLayout.CENTER);
        slot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarImagenParaSlot(lblIcon, index);
            }
        });
        return slot;
    }

    private void seleccionarImagenParaSlot(JLabel lbl, int index) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes", "jpg", "png", "jpeg");
        chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(panelPrincipal) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String ruta = file.getAbsolutePath();
            while (tempRutasGaleria.size() <= index)
                tempRutasGaleria.add(null);
            tempRutasGaleria.set(index, ruta);
            ImageIcon icon = new ImageIcon(ruta);
            Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(img));
            lbl.setText("");
        }
    }

    private void irAPaso6() {
        int c = 0;
        for (String r : tempRutasGaleria)
            if (r != null)
                c++;
        if (c < 3) {
            JOptionPane.showMessageDialog(panelPrincipal, "Sube al menos 3 imágenes.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        wizardLayout.show(wizardPanel, "PASO_6");
    }

    // --- PASO 6: PUBLICAR ---
    private JPanel crearPaso6Publicar() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Estilos.COLOR_FONDO_GRIS);
        panel.setBorder(Estilos.PADDING_PANEL_GRIS);
        panel.add(crearWizardHeader(6, this::publicarGig, "Publicar Servicio"), BorderLayout.NORTH);

        JPanel panelContent = new JPanel(new GridBagLayout());
        panelContent.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panelContent.setBorder(Estilos.BORDE_TARJETA);
        JLabel lblCongrats = new JLabel("¡Ya casi estás ahí!");
        lblCongrats.setFont(Estilos.FUENTE_TITULO_H1);
        lblCongrats.setForeground(Estilos.COLOR_VERDE_PRIMARIO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelContent.add(lblCongrats, gbc);
        panel.add(panelContent, BorderLayout.CENTER);
        return panel;
    }

    private void publicarGig() {
        List<String> rutasPersistentes = new ArrayList<>();
        String nombreUsuario = panelPrincipal.getIlustrador().getNombreUsuario();
        
        for (int i = 0; i < tempRutasGaleria.size(); i++) {
            String rutaOriginal = tempRutasGaleria.get(i);
            if (rutaOriginal != null) {
                File archivoOrigen = new File(rutaOriginal);
                if (archivoOrigen.exists()) {
                    String nuevaRuta = utils.GestorDeArchivos.guardarImagenGig(
                            archivoOrigen, nombreUsuario, tempTitulo, i);
                    if (nuevaRuta != null) rutasPersistentes.add(nuevaRuta);
                }
            }
        }

        if (rutasPersistentes.isEmpty()) {
             JOptionPane.showMessageDialog(panelPrincipal, "Error al procesar las imágenes.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
        }

        Ilustracion gigBase = new Ilustracion(tempTitulo, tempDescripcion, tempPaquetes.get(0).precio,
                rutasPersistentes.get(0), nombreUsuario, tempCategoria,
                tempEtiquetas);
        
        boolean exito = GestorDeDatos.publicarServicioCompleto(gigBase, tempPaquetes, tempExtras, tempFaqs,
                rutasPersistentes);
        
        if (exito) {
            JOptionPane.showMessageDialog(panelPrincipal, "¡Servicio publicado correctamente!", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            refrescarVista();
        } else {
            JOptionPane.showMessageDialog(panelPrincipal, "Error al guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helpers UI
    private void agregarLabelMetadato(JPanel p, String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        p.add(l);
        p.add(Box.createVerticalStrut(30));
    }

    private JPanel crearCombo(String titulo, String[] opciones) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(titulo));
        JComboBox<String> cb = new JComboBox<>(opciones);
        p.add(cb);
        return p;
    }

    private JPanel crearChecks(String titulo, String[] opciones) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new JLabel(titulo));
        JPanel grid = new JPanel(new GridLayout(0, 2, 10, 5));
        grid.setOpaque(false);
        for (String op : opciones) {
            JCheckBox chk = new JCheckBox(op);
            chk.setOpaque(false);
            grid.add(chk);
        }
        p.add(grid);
        return p;
    }

    private JPanel crearFilaExtraEstandar(JCheckBox chk, JSpinner spin) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(5, 10, 5, 10));
        chk.setOpaque(false);
        chk.setFont(Estilos.FUENTE_TEXTO_BOLD);
        p.add(chk);
        p.add(new JLabel("Costo ($):"));
        p.add(spin);
        spin.setEnabled(false);
        chk.addActionListener(e -> spin.setEnabled(chk.isSelected()));
        return p;
    }

    private JPanel crearColumnaPaquete(String titulo, JTextField txtName, JTextArea txtDesc, JComboBox<String> cmbDel,
            JComboBox<String> cmbRev, JSpinner spinPrice) {
        JPanel col = new JPanel(new GridLayout(6, 1, 0, 10));
        col.setBackground(Estilos.COLOR_FONDO_BLANCO);
        col.setBorder(new LineBorder(Estilos.COLOR_BORDE));
        JLabel lblTit = new JLabel(titulo, SwingConstants.CENTER);
        lblTit.setFont(Estilos.FUENTE_TEXTO_BOLD);
        txtDesc.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtDesc.setLineWrap(true);
        col.add(lblTit);
        col.add(txtName);
        col.add(new JScrollPane(txtDesc));
        col.add(cmbDel);
        col.add(cmbRev);
        col.add(spinPrice);
        return col;
    }

    private void actualizarEstadoPaquetes() {
        boolean active = chkOfferPackages.isSelected();
        txtStdName.setEnabled(active);
        txtStdDesc.setEnabled(active);
        cmbStdDel.setEnabled(active);
        cmbStdRev.setEnabled(active);
        spinStdPrice.setEnabled(active);
        txtPremName.setEnabled(active);
        txtPremDesc.setEnabled(active);
        cmbPremDel.setEnabled(active);
        cmbPremRev.setEnabled(active);
        spinPremPrice.setEnabled(active);
    }

    private void agregarFilaExtra() {
        if (panelExtrasContainer.getComponentCount() >= 10)
            return;
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(Estilos.COLOR_FONDO_BLANCO);
        row.setBorder(new LineBorder(Estilos.COLOR_BORDE));
        JTextField txtTit = new JTextField("Título", 15);
        JTextField txtDesc = new JTextField("Desc", 20);
        JPanel pt = new JPanel();
        pt.setOpaque(false);
        JSpinner sP = crearSpinnerPrecio();
        JComboBox<String> cD = crearComboDias();
        pt.add(new JLabel("$"));
        pt.add(sP);
        pt.add(new JLabel("Ext"));
        pt.add(cD);
        JButton btnDel = new JButton("X");
        btnDel.setForeground(Color.RED);
        btnDel.addActionListener(e -> {
            panelExtrasContainer.remove(row);
            panelExtrasContainer.revalidate();
            panelExtrasContainer.repaint();
        });
        row.add(new JLabel("Tit:"));
        row.add(txtTit);
        row.add(new JLabel("Des:"));
        row.add(txtDesc);
        row.add(pt);
        row.add(btnDel);
        panelExtrasContainer.add(row);
        panelExtrasContainer.revalidate();
    }

    private void agregarDialogoFaq() {
        JTextField q = new JTextField();
        JTextArea a = new JTextArea(3, 20);
        Object[] msg = { "Pregunta:", q, "Respuesta:", new JScrollPane(a) };
        int res = JOptionPane.showConfirmDialog(panelPrincipal, msg, "Añadir FAQ", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION && !q.getText().isEmpty()) {
            tempFaqs.add(new Faq(q.getText(), a.getText()));
            JPanel p = new JPanel(new BorderLayout());
            p.setBackground(Estilos.COLOR_FONDO_BLANCO);
            p.setBorder(new LineBorder(Estilos.COLOR_BORDE));
            JLabel l = new JLabel("<html><b>Q: " + q.getText() + "</b><br>A: " + a.getText() + "</html>");
            l.setBorder(new EmptyBorder(10, 10, 10, 10));
            p.add(l, BorderLayout.CENTER);
            panelFaqsContainer.add(p);
            panelFaqsContainer.add(Box.createVerticalStrut(10));
            panelFaqsContainer.revalidate();
        }
    }

    private JComboBox<String> crearComboDias() {
        return new JComboBox<>(new String[] { "1 Día", "2 Días", "3 Días", "5 Días", "7 Días" });
    }

    private JComboBox<String> crearComboRev() {
        return new JComboBox<>(new String[] { "0", "1", "2", "3", "Ilimitadas" });
    }

    private JSpinner crearSpinnerPrecio() {
        return new JSpinner(new SpinnerNumberModel(5, 5, 10000, 5));
    }

    private int comboIdxToDias(JComboBox<String> cb) {
        return Integer.parseInt(cb.getSelectedItem().toString().split(" ")[0]);
    }

    private int comboIdxToRev(JComboBox<String> cb) {
        String s = cb.getSelectedItem().toString();
        return s.equals("Ilimitadas") ? -1 : Integer.parseInt(s);
    }

    private JPanel crearSeccionForm(String titulo, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(40, 10));
        p.setBackground(Estilos.COLOR_FONDO_BLANCO);
        p.setBorder(Estilos.BORDE_TARJETA);
        JLabel l = new JLabel(titulo);
        l.setFont(Estilos.FUENTE_TEXTO_BOLD);
        l.setPreferredSize(new Dimension(250, 1));
        p.add(l, BorderLayout.WEST);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearWizardHeader(int pasoActivo, Runnable accionBoton, String textoBoton) {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel panelPasos = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelPasos.setOpaque(false);

        panelPasos.add(crearLabelPaso("1", "Resumen", pasoActivo == 1));
        panelPasos.add(new JLabel(">"));
        panelPasos.add(crearLabelPaso("2", "Precios", pasoActivo == 2));
        panelPasos.add(new JLabel(">"));
        panelPasos.add(crearLabelPaso("3", "Desc. & FAQ", pasoActivo == 3));
        panelPasos.add(new JLabel(">"));
        panelPasos.add(crearLabelPaso("4", "Requerimientos", pasoActivo == 4));
        panelPasos.add(new JLabel(">"));
        panelPasos.add(crearLabelPaso("5", "Galería", pasoActivo == 5));
        panelPasos.add(new JLabel(">"));
        panelPasos.add(crearLabelPaso("6", "Publicar", pasoActivo == 6));

        JButton btnAccion = new JButton(textoBoton);
        if (textoBoton.equals("Publicar Servicio"))
            Estilos.estilizarBotonPrimario(btnAccion);
        else
            Estilos.estilizarBotonSecundario(btnAccion);

        btnAccion.addActionListener(e -> accionBoton.run());

        panelHeader.add(panelPasos, BorderLayout.CENTER);
        panelHeader.add(btnAccion, BorderLayout.EAST);
        return panelHeader;
    }

    private JLabel crearLabelPaso(String num, String texto, boolean activo) {
        JLabel lbl = new JLabel("<html><body style='text-align: left;'>" + num + " " + texto + "</body></html>");
        lbl.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lbl.setForeground(activo ? Estilos.COLOR_VERDE_PRIMARIO : Estilos.COLOR_BORDE);
        return lbl;
    }
}