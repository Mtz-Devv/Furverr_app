package Vista.componentes;

import Vista.Estilos;
import Vista.ModernScrollBarUI;
import Vista.Panel_Ilustrador;
import database.GestorDeDatos;
import modelos.Ilustrador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class PerfilView {

    private Panel_Ilustrador panelPrincipal;
    private Ilustrador ilustrador;
    private JPanel panel;
    private JLabel lblFotoPerfil;

    // Campos de formulario
    private JTextArea txtAbout;
    private JComboBox<String> comboPaisEdu;
    private JTextField txtUniversidadEdu;
    private JTextField txtTituloEdu;
    private JComboBox<String> comboAnioEdu;
    private JTextField txtCertificadoCert;
    private JTextField txtOtorgadoPorCert;
    private JComboBox<String> comboAnioCert;
    private JTextField txtSkill;
    private JComboBox<String> comboNivelSkill;

    public PerfilView(Panel_Ilustrador panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        this.ilustrador = panelPrincipal.getIlustrador();
        inicializarCamposDeFormulario();
        this.panel = crearPanelPerfil();
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public void inicializarCamposDeFormulario() {
        txtAbout = new JTextArea(10, 20);
        txtAbout.setLineWrap(true);
        txtAbout.setWrapStyleWord(true);
        txtAbout.setText(ilustrador.getDescripcion());
        txtAbout.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        txtAbout.setBorder(Estilos.BORDE_CAMPO_TEXTO);

        comboPaisEdu = new JComboBox<>(new String[] { "México", "Estados Unidos", "España", "Argentina" });
        comboPaisEdu.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        txtUniversidadEdu = new JTextField();
        txtUniversidadEdu.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        txtUniversidadEdu.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtTituloEdu = new JTextField();
        txtTituloEdu.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        txtTituloEdu.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        comboAnioEdu = new JComboBox<>(new String[] { "2024", "2023", "2022", "2021", "2020" });
        comboAnioEdu.setFont(Estilos.FUENTE_TEXTO_REGULAR);

        txtCertificadoCert = new JTextField();
        txtCertificadoCert.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        txtCertificadoCert.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        txtOtorgadoPorCert = new JTextField();
        txtOtorgadoPorCert.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        txtOtorgadoPorCert.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        comboAnioCert = new JComboBox<>(new String[] { "2024", "2023", "2022", "2021", "2020" });
        comboAnioCert.setFont(Estilos.FUENTE_TEXTO_REGULAR);

        txtSkill = new JTextField();
        txtSkill.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        txtSkill.setBorder(Estilos.BORDE_CAMPO_TEXTO);
        comboNivelSkill = new JComboBox<>(new String[] { "Básico", "Intermedio", "Experto" });
        comboNivelSkill.setFont(Estilos.FUENTE_TEXTO_REGULAR);
    }

    public JPanel crearPanelPerfil() {
        JPanel panelPerfil = new JPanel(new BorderLayout(Estilos.ESPACIO_GRANDE, Estilos.ESPACIO_GRANDE));
        panelPerfil.setBackground(Estilos.COLOR_FONDO_GRIS);
        panelPerfil.setBorder(Estilos.PADDING_PANEL_GRIS);
        // IMPORTANTE: El nombre debe coincidir con la constante en Panel_Ilustrador para que refrescar funcione
        panelPerfil.setName(Panel_Ilustrador.VISTA_PERFIL);

        // Panel Lateral (Quick Links)
        panelPrincipal.setLateralCardLayout(new CardLayout());
        panelPrincipal.setPanelLateral(new JPanel(panelPrincipal.getLateralCardLayout()));
        panelPrincipal.getPanelLateral().setOpaque(false);
        panelPrincipal.getPanelLateral().setPreferredSize(new Dimension(300, 1));

        JPanel panelQuickLinks = new JPanel();
        panelQuickLinks.setOpaque(false);
        panelQuickLinks.setLayout(new BoxLayout(panelQuickLinks, BoxLayout.Y_AXIS));
        JLabel lblQuickLinks = new JLabel("Accesos Rápidos");
        lblQuickLinks.setFont(Estilos.FUENTE_TITULO_H3);
        lblQuickLinks.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblQuickLinks.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnGigsLink = new JButton("Servicios");
        Estilos.estilizarBotonTexto(btnGigsLink, false);
        btnGigsLink.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        btnGigsLink.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        btnGigsLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGigsLink.addActionListener(e -> panelPrincipal.getMainCardLayout().show(panelPrincipal.getMainCardsPanel(),
                Panel_Ilustrador.VISTA_GIGS));
        panelQuickLinks.add(lblQuickLinks);
        panelQuickLinks.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        panelQuickLinks.add(btnGigsLink);
        JPanel wrapperLinks = new JPanel(new BorderLayout());
        wrapperLinks.setOpaque(false);
        wrapperLinks.add(panelQuickLinks, BorderLayout.NORTH);

        panelPrincipal.getPanelLateral().add(wrapperLinks, Panel_Ilustrador.VISTA_LATERAL_LINKS);
        panelPrincipal.getPanelLateral().add(crearPanelEditarEducacion(), Panel_Ilustrador.VISTA_LATERAL_EDU);
        panelPrincipal.getPanelLateral().add(crearPanelEditarAbout(), Panel_Ilustrador.VISTA_LATERAL_ABOUT);
        panelPrincipal.getPanelLateral().add(crearPanelEditarCertificaciones(), Panel_Ilustrador.VISTA_LATERAL_CERT);
        panelPrincipal.getPanelLateral().add(crearPanelEditarSkills(), Panel_Ilustrador.VISTA_LATERAL_SKILLS);

        panelPrincipal.getLateralCardLayout().show(panelPrincipal.getPanelLateral(),
                Panel_Ilustrador.VISTA_LATERAL_LINKS);

        panelPerfil.add(panelPrincipal.getPanelLateral(), BorderLayout.EAST);

        // Panel Central
        JPanel mainContentPanel = new JPanel(new GridBagLayout());
        mainContentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(Estilos.ESPACIO_MEDIANO, 0, Estilos.ESPACIO_MEDIANO, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        
        mainContentPanel.add(crearPanelInfoUsuario(), gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(Estilos.ESPACIO_GRANDE, 0, Estilos.ESPACIO_MEDIANO, 0);
        mainContentPanel.add(crearTarjetaPerfil("Acerca de mí",
                "Comparte algunos detalles sobre ti, tu experiencia y lo que ofreces.", ilustrador.getDescripcion(),
                "details"), gbc);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, Estilos.ESPACIO_MEDIANO, Estilos.ESPACIO_PEQUENO);
        mainContentPanel.add(
                crearTarjetaPerfil("Educación", "Respalda tus habilidades añadiendo títulos o programas educativos.",
                        ilustrador.getEducacion(), "education"),
                gbc);
        gbc.gridx = 1;
        gbc.insets = new Insets(0, Estilos.ESPACIO_PEQUENO, Estilos.ESPACIO_MEDIANO, 0);
        mainContentPanel.add(crearTarjetaPerfil("Certificaciones",
                "Demuestra tu maestría con certificaciones obtenidas en tu campo.", ilustrador.getCertificaciones(),
                "certifications"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainContentPanel.add(crearTarjetaPerfil("Habilidades y experiencia",
                "Deja que tus compradores conozcan tus habilidades. Habilidades ganadas...",
                ilustrador.getHabilidades(), "skills"), gbc);
                
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.add(mainContentPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        panelPerfil.add(scrollPane, BorderLayout.CENTER);
        return panelPerfil;
    }

    private JPanel crearPanelInfoUsuario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 0, Estilos.ESPACIO_MEDIANO);

        lblFotoPerfil = new JLabel();
        String fotoPath = ilustrador.getRutaFotoPerfil();
        if (fotoPath != null && !fotoPath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(fotoPath);
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblFotoPerfil.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                lblFotoPerfil.setText("[Sin Foto]");
            }
        } else {
            lblFotoPerfil.setText("[Sin Foto]");
        }
        lblFotoPerfil.setPreferredSize(new Dimension(100, 100));
        lblFotoPerfil.setBorder(new LineBorder(Estilos.COLOR_BORDE));
        lblFotoPerfil.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblFotoPerfil.setToolTipText("Clic para cambiar foto de perfil");
        lblFotoPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cambiarFotoPerfil();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                lblFotoPerfil.setBorder(new LineBorder(Estilos.COLOR_VERDE_PRIMARIO, 2));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblFotoPerfil.setBorder(new LineBorder(Estilos.COLOR_BORDE));
            }
        });

        panel.add(lblFotoPerfil, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        String nombreMostrado = ilustrador.getNombreMostrado();
        if (nombreMostrado == null || nombreMostrado.isEmpty()) {
            nombreMostrado = ilustrador.getNombreUsuario();
        }
        JLabel lblNombre = new JLabel(nombreMostrado);
        lblNombre.setFont(Estilos.FUENTE_TITULO_H2);
        lblNombre.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        JLabel lblUsername = new JLabel("@" + ilustrador.getNombreUsuario());
        lblUsername.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblUsername.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        JPanel infoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        infoRow.setOpaque(false);
        infoRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblIdioma = new JLabel("Habla " + ilustrador.getIdioma());
        lblIdioma.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblIdioma.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        JLabel lblMexico = new JLabel("Mexico");
        lblMexico.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblMexico.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        infoRow.add(lblMexico);
        infoRow.add(new JLabel("•"));
        infoRow.add(lblIdioma);
        textPanel.add(lblNombre);
        textPanel.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        textPanel.add(lblUsername);
        textPanel.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        textPanel.add(infoRow);
        panel.add(textPanel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(Estilos.ESPACIO_MEDIANO, 0, 0, 0);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton btnPreview = new JButton("Vista Previa");
        Estilos.estilizarBotonSecundario(btnPreview);

        btnPreview.addActionListener(e -> {
            crearDialogoVistaPrevia();
        });

        buttonPanel.add(btnPreview);
        panel.add(buttonPanel, gbc);
        return panel;
    }

    private void cambiarFotoPerfil() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "png", "jpeg");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(panelPrincipal);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = chooser.getSelectedFile();
            String rutaNueva = utils.GestorDeArchivos.guardarImagenPerfil(
                    archivoSeleccionado, 
                    utils.GestorDeArchivos.CARPETA_ILUSTRADORES, 
                    ilustrador.getNombreUsuario());
            if (rutaNueva != null) {
                ilustrador.setRutaFotoPerfil(rutaNueva);
                GestorDeDatos.actualizarPerfilIlustrador(
                        ilustrador.getNombreUsuario(),
                        ilustrador.getNombreMostrado(),
                        ilustrador.getDescripcion(),
                        ilustrador.getIdioma(),
                        ilustrador.getNivelIdioma(),
                        rutaNueva);
                panelPrincipal.refrescarPanelPerfil();
                JOptionPane.showMessageDialog(panelPrincipal, "Foto de perfil actualizada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panelPrincipal, "Error al guardar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel crearTarjetaPerfil(String titulo, String descripcion, String contenido, String tipo) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Estilos.COLOR_FONDO_BLANCO);
        card.setBorder(Estilos.BORDE_TARJETA);
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(Estilos.FUENTE_TITULO_H3);
        lblTitulo.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblDesc = new JLabel("<html><p style='width:300px;'>" + descripcion + "</p></html>");
        lblDesc.setFont(Estilos.FUENTE_TEXTO_SECUNDARIO);
        lblDesc.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(lblTitulo);
        content.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        content.add(lblDesc);
        card.add(content, BorderLayout.CENTER);
        String textoBoton;
        String tipoBoton = tipo.substring(0, 1).toUpperCase() + tipo.substring(1);
        if (tipo.equals("details")) {
            tipoBoton = "Detalles"; // Corrección: Decía "Editar Detalles" lo que resultaba en "Editar Editar Detalles"
        }
        if (tipo.equals("education"))
            tipoBoton = "Educación";
        if (tipo.equals("certifications"))
            tipoBoton = "Certificaciones";
        if (tipo.equals("skills"))
            tipoBoton = "Habilidades";
        
        if (contenido == null || contenido.isEmpty()) {
            textoBoton = "Añadir " + tipoBoton;
        } else {
            textoBoton = "Editar " + tipoBoton;
            JLabel lblContenido = new JLabel("<html><p style='width:300px;'>" + contenido + "</p></html>");
            lblContenido.setFont(Estilos.FUENTE_TEXTO_REGULAR);
            lblContenido.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
            content.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
            content.add(lblContenido);
        }
        JButton btnAccion = new JButton(textoBoton);
        Estilos.estilizarBotonSecundario(btnAccion);
        btnAccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAccion.addActionListener(e -> {
            String vistaLateral = "";
            switch (tipo) {
                case "details": vistaLateral = Panel_Ilustrador.VISTA_LATERAL_ABOUT; break;
                case "education": vistaLateral = Panel_Ilustrador.VISTA_LATERAL_EDU; break;
                case "certifications": vistaLateral = Panel_Ilustrador.VISTA_LATERAL_CERT; break;
                case "skills": vistaLateral = Panel_Ilustrador.VISTA_LATERAL_SKILLS; break;
            }
            panelPrincipal.getLateralCardLayout().show(panelPrincipal.getPanelLateral(), vistaLateral);
        });
        content.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        content.add(btnAccion);
        return card;
    }

    private JPanel crearPanelEdicionLateral(String titulo, JPanel formulario, ActionListener accionGuardar) {
        JPanel panel = new JPanel(new BorderLayout(10, Estilos.ESPACIO_MEDIANO));
        panel.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, Estilos.COLOR_BORDE),
                Estilos.PADDING_TARJETA));
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(Estilos.FUENTE_TITULO_H3);
        lblTitulo.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        JButton btnCerrar = new JButton("X");
        btnCerrar.setFont(Estilos.FUENTE_TEXTO_BOLD);
        btnCerrar.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        Estilos.estilizarBotonTexto(btnCerrar, false);
        btnCerrar.addActionListener(e -> {
            panelPrincipal.getLateralCardLayout().show(panelPrincipal.getPanelLateral(),
                    Panel_Ilustrador.VISTA_LATERAL_LINKS);
        });
        headerPanel.add(btnCerrar, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(formulario, BorderLayout.CENTER);
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        JButton btnGuardar = new JButton("Guardar");
        Estilos.estilizarBotonPrimario(btnGuardar);
        btnGuardar.addActionListener(accionGuardar);
        footerPanel.add(btnGuardar);
        panel.add(footerPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearCampoFormulario(String etiqueta, JComponent campo) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lbl.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, campo.getPreferredSize().height));
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        panel.add(campo);
        return panel;
    }

    // ==================================================================
    // --- MÉTODOS DE "GUARDAR" CON REFRESH ---
    // ==================================================================

    private JPanel crearPanelEditarAbout() {
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(txtAbout);
        scroll.setBorder(null);
        form.add(crearCampoFormulario("Tu descripción", scroll));

        ActionListener guardarAbout = e -> {
            this.ilustrador.setDescripcion(txtAbout.getText());
            GestorDeDatos.actualizarIlustrador(this.ilustrador);
            // REFRESCAR PANEL PRINCIPAL
            panelPrincipal.refrescarPanelPerfil();
            JOptionPane.showMessageDialog(panelPrincipal, "Descripción actualizada.");
        };
        return crearPanelEdicionLateral("Acerca de mí", form, guardarAbout);
    }

    private JPanel crearPanelEditarEducacion() {
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, Estilos.ESPACIO_MEDIANO, 0));
        form.add(crearCampoFormulario("País del colegio o la universidad", comboPaisEdu));
        form.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        form.add(crearCampoFormulario("Nombre de la facultad/universidad", txtUniversidadEdu));
        form.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        form.add(crearCampoFormulario("Título", txtTituloEdu));
        form.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        form.add(crearCampoFormulario("Año de graduación", comboAnioEdu));
        form.add(Box.createVerticalGlue());

        ActionListener guardarEducacion = e -> {
            String pais = (String) comboPaisEdu.getSelectedItem();
            String uni = txtUniversidadEdu.getText();
            String titulo = txtTituloEdu.getText();
            String anio = (String) comboAnioEdu.getSelectedItem();
            String educacionTexto = titulo + " en " + uni + " (" + pais + ", " + anio + ")";

            this.ilustrador.setEducacion(educacionTexto);
            GestorDeDatos.actualizarIlustrador(this.ilustrador);
            // REFRESCAR PANEL PRINCIPAL
            panelPrincipal.refrescarPanelPerfil();
            JOptionPane.showMessageDialog(panelPrincipal, "Educación actualizada.");
        };
        return crearPanelEdicionLateral("Educación", form, guardarEducacion);
    }

    private JPanel crearPanelEditarCertificaciones() {
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, Estilos.ESPACIO_MEDIANO, 0));
        form.add(crearCampoFormulario("Certificado o Premio", txtCertificadoCert));
        form.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        form.add(crearCampoFormulario("Otorgado por (ej. Adobe)", txtOtorgadoPorCert));
        form.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        form.add(crearCampoFormulario("Año", comboAnioCert));
        form.add(Box.createVerticalGlue());

        ActionListener guardarCert = e -> {
            String cert = txtCertificadoCert.getText();
            String otorgado = txtOtorgadoPorCert.getText();
            String anio = (String) comboAnioCert.getSelectedItem();
            String certTexto = cert + " - " + otorgado + " (" + anio + ")";

            this.ilustrador.setCertificaciones(certTexto);
            GestorDeDatos.actualizarIlustrador(this.ilustrador);
            // REFRESCAR PANEL PRINCIPAL
            panelPrincipal.refrescarPanelPerfil();
            JOptionPane.showMessageDialog(panelPrincipal, "Certificaciones actualizadas.");
        };
        return crearPanelEdicionLateral("Certificaciones", form, guardarCert);
    }

    private JPanel crearPanelEditarSkills() {
        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(Estilos.ESPACIO_MEDIANO, 0, Estilos.ESPACIO_MEDIANO, 0));
        form.add(crearCampoFormulario("Añadir Habilidad (ej. Photoshop)", txtSkill));
        form.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        form.add(crearCampoFormulario("Nivel de Experiencia", comboNivelSkill));
        form.add(Box.createVerticalGlue());

        ActionListener guardarSkill = e -> {
            String skill = txtSkill.getText();
            String nivel = (String) comboNivelSkill.getSelectedItem();
            String skillTexto = skill + " (" + nivel + ")";

            this.ilustrador.setHabilidades(skillTexto);
            GestorDeDatos.actualizarIlustrador(this.ilustrador);
            // REFRESCAR PANEL PRINCIPAL
            panelPrincipal.refrescarPanelPerfil();
            JOptionPane.showMessageDialog(panelPrincipal, "Habilidades actualizadas.");
        };
        return crearPanelEdicionLateral("Habilidades", form, guardarSkill);
    }

    // (El resto del archivo para VISTA PREVIA se mantiene igual, es solo para mostrar)
    private void crearDialogoVistaPrevia() {
        JDialog vistaPreviaDialog = new JDialog(panelPrincipal, "Vista Previa del Perfil", true);
        vistaPreviaDialog.setSize(900, 700);
        vistaPreviaDialog.setLocationRelativeTo(panelPrincipal);
        vistaPreviaDialog.setLayout(new BorderLayout());

        JPanel panelFondo = new JPanel(new BorderLayout());
        panelFondo.setBackground(Estilos.COLOR_FONDO_GRIS);
        panelFondo.setBorder(Estilos.PADDING_PANEL_GRIS);

        JPanel panelContenido = new JPanel(new BorderLayout(Estilos.ESPACIO_GRANDE, Estilos.ESPACIO_GRANDE));
        panelContenido.setBackground(Estilos.COLOR_FONDO_BLANCO);
        panelContenido.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Estilos.COLOR_BORDE),
                Estilos.PADDING_TARJETA));

        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));

        panelIzquierdo.add(crearPanelInfoUsuarioVistaPrevia());
        panelIzquierdo.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        panelIzquierdo.add(crearSeccionVistaPrevia("Sobre mí", ilustrador.getDescripcion()));
        panelIzquierdo.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        panelIzquierdo.add(crearSeccionVistaPrevia("Habilidades", ilustrador.getHabilidades()));
        panelIzquierdo.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        panelIzquierdo.add(crearSeccionVistaPrevia("Educación", ilustrador.getEducacion()));
        panelIzquierdo.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        panelIzquierdo.add(crearSeccionVistaPrevia("Certificaciones", ilustrador.getCertificaciones()));
        panelIzquierdo.add(Box.createVerticalGlue());

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(300, 1));
        panelDerecho.add(crearTarjetaLateralVistaPrevia(), BorderLayout.NORTH);

        JScrollPane scrollIzquierdo = new JScrollPane(panelIzquierdo);
        scrollIzquierdo.setOpaque(false);
        scrollIzquierdo.getViewport().setOpaque(false);
        scrollIzquierdo.setBorder(BorderFactory.createEmptyBorder());
        scrollIzquierdo.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        panelContenido.add(scrollIzquierdo, BorderLayout.CENTER);
        panelContenido.add(panelDerecho, BorderLayout.EAST);

        panelFondo.add(panelContenido, BorderLayout.CENTER);
        vistaPreviaDialog.add(panelFondo, BorderLayout.CENTER);
        vistaPreviaDialog.setVisible(true);
    }

    private JPanel crearPanelInfoUsuarioVistaPrevia() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, Estilos.ESPACIO_MEDIANO, 0));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblFoto = new JLabel();
        String fotoPath = ilustrador.getRutaFotoPerfil();
        if (fotoPath != null && !fotoPath.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(fotoPath);
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblFoto.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                lblFoto.setText("[Foto]");
            }
        } else {
            lblFoto.setText("[Foto]");
        }
        lblFoto.setPreferredSize(new Dimension(100, 100));
        lblFoto.setBorder(new LineBorder(Estilos.COLOR_BORDE));
        panel.add(lblFoto);
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        String nombreMostrado = ilustrador.getNombreMostrado();
        if (nombreMostrado == null || nombreMostrado.isEmpty()) {
            nombreMostrado = ilustrador.getNombreUsuario();
        }
        JLabel lblNombre = new JLabel(nombreMostrado);
        lblNombre.setFont(Estilos.FUENTE_TITULO_H2);
        lblNombre.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        JLabel lblUsername = new JLabel("@" + ilustrador.getNombreUsuario());
        lblUsername.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblUsername.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        JPanel infoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        infoRow.setOpaque(false);
        infoRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblPais = new JLabel("Mexico");
        lblPais.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblPais.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        JLabel lblIdioma = new JLabel("Habla " + ilustrador.getIdioma());
        lblIdioma.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblIdioma.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        infoRow.add(new JLabel("📍"));
        infoRow.add(lblPais);
        infoRow.add(new JLabel("•"));
        infoRow.add(lblIdioma);
        textPanel.add(lblNombre);
        textPanel.add(Box.createVerticalStrut(Estilos.ESPACIO_PEQUENO));
        textPanel.add(lblUsername);
        textPanel.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        textPanel.add(infoRow);
        panel.add(textPanel);
        return panel;
    }

    private JPanel crearSeccionVistaPrevia(String titulo, String contenido) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblTitle = new JLabel(titulo);
        lblTitle.setFont(Estilos.FUENTE_TITULO_H3);
        lblTitle.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        String textoContenido = (contenido != null && !contenido.isEmpty()) ? contenido : "No especificado";
        JLabel lblContenido = new JLabel("<html><p style='width:350px;'>" + textoContenido + "</p></html>");
        lblContenido.setFont(Estilos.FUENTE_TEXTO_REGULAR);
        lblContenido.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblContenido.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        panel.add(lblContenido);
        return panel;
    }

    private JPanel crearTarjetaLateralVistaPrevia() {
        JPanel card = new JPanel();
        card.setBackground(Estilos.COLOR_FONDO_BLANCO);
        card.setBorder(Estilos.BORDE_TARJETA);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JButton btnContactar = new JButton("Contáctame");
        Estilos.estilizarBotonPrimario(btnContactar);
        btnContactar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnContactar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(btnContactar);
        card.add(Box.createVerticalStrut(Estilos.ESPACIO_GRANDE));
        JPanel filaDesde = new JPanel(new BorderLayout());
        filaDesde.setOpaque(false);
        JLabel lblDesde = new JLabel("En Furverr desde");
        lblDesde.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblDesde.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        JLabel lblFecha = new JLabel("nov 2025");
        lblFecha.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblFecha.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        filaDesde.add(lblDesde, BorderLayout.WEST);
        filaDesde.add(lblFecha, BorderLayout.EAST);
        card.add(filaDesde);
        card.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        card.add(new JSeparator());
        card.add(Box.createVerticalStrut(Estilos.ESPACIO_MEDIANO));
        JLabel lblHablo = new JLabel("Hablo");
        lblHablo.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblHablo.setForeground(Estilos.COLOR_TEXTO_SECUNDARIO);
        card.add(lblHablo);
        JLabel lblIdiomas = new JLabel(ilustrador.getIdioma() + " (Nativo/Bilingüe)");
        lblIdiomas.setFont(Estilos.FUENTE_TEXTO_BOLD);
        lblIdiomas.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        card.add(lblIdiomas);
        return card;
    }
}