package Vista;

// 1. Imports de las nuevas clases de componentes
import Vista.componentes.AccountViews;
import Vista.componentes.AnalyticsView;
import Vista.componentes.DashboardView;
import Vista.componentes.EarningsView;
import Vista.componentes.GigsView;
import Vista.componentes.HeaderPanel;
import Vista.componentes.OrdersView;
import Vista.componentes.PerfilView;

import modelos.Ilustrador;
import database.GestorDeDatos;

import javax.swing.*;
import java.awt.*;

public class Panel_Ilustrador extends JFrame {

    // --- Variables principales ---
    private Ilustrador ilustrador;
    private CardLayout mainCardLayout;
    private JPanel mainCardsPanel;

    // --- Variables del panel lateral ---
    private CardLayout lateralCardLayout;
    private JPanel panelLateral;

    // --- Referencias a Vistas ---
    private EarningsView earningsView;

    // --- Nombres de Vistas (CONSTANTES PÚBLICAS) ---
    public static final String VISTA_PERFIL = "Perfil";
    public static final String VISTA_DASHBOARD = "Dashboard";
    public static final String VISTA_ORDERS = "Orders";
    public static final String VISTA_GIGS = "Gigs";
    public static final String VISTA_GIGS_CREAR = "CrearGig";
    public static final String VISTA_EARNINGS = "Earnings";
    public static final String VISTA_ANALYTICS_OVERVIEW = "AnalyticsOverview";
    public static final String VISTA_ANALYTICS_REPEAT = "AnalyticsRepeat";
    public static final String VISTA_ACCOUNT_SETTINGS = "AccountSettings";
    public static final String VISTA_BILLING_PAYMENTS = "BillingPayments";
    public static final String VISTA_LEVEL_OVERVIEW = "LevelOverview";

    // Constantes para el panel lateral de edición
    public static final String VISTA_LATERAL_LINKS = "Links";
    public static final String VISTA_LATERAL_ABOUT = "About";
    public static final String VISTA_LATERAL_EDU = "Educacion";
    public static final String VISTA_LATERAL_CERT = "Certificaciones";
    public static final String VISTA_LATERAL_SKILLS = "Skills";

    public Panel_Ilustrador(modelos.Usuario user) {
        // Aseguramos que el usuario sea tratado como Ilustrador
        if (user instanceof Ilustrador) {
            this.ilustrador = (Ilustrador) user;
        } else {
            this.ilustrador = (Ilustrador) GestorDeDatos.obtenerUsuario(user.getNombreUsuario());
        }

        // Configuración de la ventana
        setTitle("Panel de Ilustrador - " + ilustrador.getNombreUsuario());
        setSize(1100, 750);
        setMinimumSize(new Dimension(1100, 750));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Estilos.COLOR_FONDO_GRIS);

        // ================================================================
        // --- CONSTRUCCIÓN DE VISTAS (AHORA SEPARADO) ---
        // ================================================================

        // 1. Crear el Header
        HeaderPanel header = new HeaderPanel(this);
        add(header.getPanel(), BorderLayout.NORTH);

        // 2. Crear los objetos de las Vistas
        PerfilView perfilView = new PerfilView(this);
        DashboardView dashboardView = new DashboardView(this);
        GigsView gigsView = new GigsView(this);
        OrdersView ordersView = new OrdersView();
        // Inicializamos EarningsView con la referencia al ilustrador y lo guardamos en
        // la variable de instancia
        this.earningsView = new EarningsView(this.ilustrador);
        AnalyticsView analyticsView = new AnalyticsView();
        AccountViews accountViews = new AccountViews(this);

        // 3. Configuración del panel cambiante (CardLayout)
        mainCardLayout = new CardLayout();
        mainCardsPanel = new JPanel(mainCardLayout);
        mainCardsPanel.setOpaque(false);

        // 4. Añadimos todas las vistas al CardLayout
        mainCardsPanel.add(perfilView.getPanel(), VISTA_PERFIL);
        mainCardsPanel.add(dashboardView.getPanel(), VISTA_DASHBOARD);
        mainCardsPanel.add(ordersView.getPanel(), VISTA_ORDERS);
        mainCardsPanel.add(gigsView.getPanelListaGigs(), VISTA_GIGS);
        mainCardsPanel.add(gigsView.getPanelCrearGig(), VISTA_GIGS_CREAR);
        mainCardsPanel.add(earningsView.getPanel(), VISTA_EARNINGS);
        mainCardsPanel.add(analyticsView.getPanelOverview(), VISTA_ANALYTICS_OVERVIEW);
        mainCardsPanel.add(analyticsView.getPanelRepeat(), VISTA_ANALYTICS_REPEAT);
        mainCardsPanel.add(accountViews.getPanelAccountSettings(), VISTA_ACCOUNT_SETTINGS);
        mainCardsPanel.add(accountViews.getPanelBillingAndPayments(), VISTA_BILLING_PAYMENTS);
        mainCardsPanel.add(accountViews.getPanelLevelOverview(), VISTA_LEVEL_OVERVIEW);

        add(mainCardsPanel, BorderLayout.CENTER);

        // Iniciamos mostrando el Dashboard
        mainCardLayout.show(mainCardsPanel, VISTA_DASHBOARD);
        setVisible(true);
    }

    // ================================================================
    // --- MÉTODOS DE ACCESO (GETTERS Y SETTERS) ---
    // (Estos permiten a las vistas comunicarse con el panel principal)
    // ================================================================

    public Ilustrador getIlustrador() {
        return this.ilustrador;
    }

    public CardLayout getMainCardLayout() {
        return this.mainCardLayout;
    }

    public JPanel getMainCardsPanel() {
        return this.mainCardsPanel;
    }

    public CardLayout getLateralCardLayout() {
        return this.lateralCardLayout;
    }

    public void setLateralCardLayout(CardLayout layout) {
        this.lateralCardLayout = layout;
    }

    public JPanel getPanelLateral() {
        return this.panelLateral;
    }

    public void setPanelLateral(JPanel panel) {
        this.panelLateral = panel;
    }

    /**
     * Método especial para recargar el perfil cuando guardas cambios
     * (Ahora borra y crea la vista de PerfilView)
     */
    public void refrescarPanelPerfil() {
        Component c = null;
        // Buscamos el panel viejo
        for (Component comp : mainCardsPanel.getComponents()) {
            if (VISTA_PERFIL.equals(comp.getName())) {
                c = comp;
                break;
            }
        }
        // Lo borramos
        if (c != null)
            mainCardsPanel.remove(c);

        // Creamos uno nuevo con los datos actualizados
        JPanel nuevoPanelPerfil = new PerfilView(this).getPanel();
        // (El .setName() ya se hace dentro de PerfilView)
        mainCardsPanel.add(nuevoPanelPerfil, VISTA_PERFIL);

        // Lo mostramos
        mainCardLayout.show(mainCardsPanel, VISTA_PERFIL);
        mainCardsPanel.revalidate();
        mainCardsPanel.repaint();
    }

    /**
     * Método para actualizar la vista de ganancias desde otras vistas (ej.
     * Dashboard).
     */
    public void actualizarEarningsView() {
        if (this.earningsView != null) {
            this.earningsView.actualizarEarnings();
        }
    }
}