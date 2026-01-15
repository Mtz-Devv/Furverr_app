package database;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import utils.GestorDeArchivos;

public class GeneradorDeDatos {

    // --- DESCRIPCIONES ---
    private static final String DESC_LAURA = "¡Hola! Soy Laura, diseñadora gráfica e ilustradora con enfoque en identidad visual. Mi especialidad es transformar ideas abstractas en logotipos y marcas memorables.";
    private static final String DESC_GRIM = "Desde las sombras emergen las marcas más impactantes. Soy Dante 'Grim', diseñador editorial y de portadas. Me especializo en el nicho de fantasía oscura.";
    private static final String DESC_VECTOR = "La geometría es el lenguaje universal. Soy Sarah, experta en diseño vectorial minimalista. Mi obsesión es la limpieza visual: iconos perfectos e infografías.";
    private static final String DESC_PANDA = "¡Dale vida a tus píxeles! Soy PixelPanda, animador 2D especializado en la estética retro de los 8 y 16 bits. No solo dibujo sprites; los hago caminar y saltar.";
    private static final String DESC_SAKURA = "Konnichiwa! Soy Yumi, editora de video y animadora con estilo Anime/Motion Graphics. Transformo grabaciones aburridas en AMVs dinámicos.";
    private static final String DESC_NEON = "El futuro del marketing es digital. Soy Kaito, estratega de redes sociales. Corto a través del ruido de internet con campañas agresivas y visualmente impactantes.";
    private static final String DESC_VISTA = "Haz que te vean. Soy Marco, consultor SEO y experto en visibilidad online. Mi trabajo consiste en posicionar tu sitio web en la primera página de Google.";
    private static final String DESC_REALISM = "Negocios reales, resultados reales. Soy Elena 'RealismPro', analista de negocios. Ayudo a emprendedores a aterrizar sus ideas con planes de negocio sólidos.";
    private static final String DESC_FUZZY = "¡Tu mano derecha en la oficina virtual! Soy Alex 'Fuzzy', asistente virtual. Me encargo de esas tareas administrativas que te quitan tiempo.";

    public static void inicializarIlustradoresDetallados() {
        System.out.println("[Generador] Inicializando perfiles, Gigs, Tags y FAQs...");
        
        // ==================================================
        // --- 1. ARTBYLAURA (Ilustración General) ---
        // ==================================================
        // Gig Principal (Logo)
        procesarIlustrador("ArtByLaura", "Laura Design", DESC_LAURA, "Inglés", "Nativo",
            "Estados Unidos", "Parsons", "Diseño Gráfico", "2021", "Best Logo", "Behance", "2022", "Illustrator (Exp)",
            "Logo Minimalista", "Diseño de logotipo vectorial limpio y moderno.", 50.0, "GRÁFICOS Y DISEÑO/Diseño de Logo",
            "logo, minimalist, branding, vector, identity",
            new String[]{"¿Entregas el archivo editable?", "¿Haces revisiones ilimitadas?"},
            new String[]{"Sí, incluyo archivos AI y EPS.", "El paquete básico incluye 2 revisiones, el premium es ilimitado."});
        
        // Gig Extra 1: Retrato
        agregarGigAdicional("ArtByLaura", "Retrato Semirealista", "Dibujaré un retrato digital con estilo semirealista suave.", 
            45.0, "GRÁFICOS Y DISEÑO/Ilustración", "retratos, portrait, face, digital art, realistic",
            new String[]{"¿Necesitas foto de referencia?", "¿Es a color?"}, new String[]{"Sí, una foto clara ayuda mucho.", "Sí, a todo color y sombreado."});
        
        // Gig Extra 2: Furry
        agregarGigAdicional("ArtByLaura", "Arte Furry Cute", "Ilustración de tu fursona en un estilo tierno y amigable.", 
            60.0, "GRÁFICOS Y DISEÑO/Ilustración", "furry, anthro, fursona, cute, animal",
            new String[]{"¿Dibujas cualquier especie?", "¿Incluye fondo?"}, new String[]{"Sí, caninos, felinos, dragones, etc.", "Fondo simple de color sólido."});
        
        // Gig Extra 3: Paisaje
        agregarGigAdicional("ArtByLaura", "Paisaje de Fantasía", "Concept art de entornos naturales y mágicos.", 
            80.0, "GRÁFICOS Y DISEÑO/Ilustración", "paisajes, landscape, environment, fantasy, background",
            new String[]{"¿Qué resolución tiene?", "¿Sirve para videojuegos?"}, new String[]{"4K 300dpi.", "Sí, perfecto para fondos o concept art."});


        // ==================================================
        // --- 2. GRIMSHADOW (Arte Oscuro/Terror) ---
        // ==================================================
        // Gig Principal (Portada)
        procesarIlustrador("GrimShadow", "Dante Void", DESC_GRIM, "Español", "Nativo",
            "México", "UNAM", "Artes Visuales", "2019", "Editorial Gold", "Dark Arts", "2021", "Photoshop (Exp)",
            "Portada de Terror", "Ilustración oscura para portadas de libros o discos.", 90.0, "GRÁFICOS Y DISEÑO/Ilustración",
            "horror, book cover, dark, fantasy, illustration",
            new String[]{"¿Puedo usarlo comercialmente?", "¿Haces bocetos previos?"},
            new String[]{"Sí, todos los derechos comerciales están incluidos.", "Envío 2 bocetos iniciales antes de renderizar."});

        // Gig Extra 1: Retrato
        agregarGigAdicional("GrimShadow", "Retrato Gótico", "Retrato oscuro con estética de vampiro o terror.", 
            55.0, "GRÁFICOS Y DISEÑO/Ilustración", "retratos, portrait, gothic, dark, vampire",
            new String[]{"¿Puedes hacerme ver como zombie?", "¿Es a color?"}, new String[]{"Claro, me especializo en transformaciones oscuras.", "Sí, paleta de colores oscuros y rojos."});
        
        // Gig Extra 2: Furry
        agregarGigAdicional("GrimShadow", "Furry Monstruoso", "Diseño de criaturas y fursonas con toque de horror lovecraftiano.", 
            70.0, "GRÁFICOS Y DISEÑO/Ilustración", "furry, monster, beast, horror, creature",
            new String[]{"¿Dibujas gore?", "¿Qué estilo usas?"}, new String[]{"Gore ligero artístico.", "Estilo pictórico denso y texturizado."});
        
        // Gig Extra 3: Paisaje
        agregarGigAdicional("GrimShadow", "Paisaje Embrujado", "Atmósferas de cementerios, castillos y bosques oscuros.", 
            95.0, "GRÁFICOS Y DISEÑO/Ilustración", "paisajes, landscape, spooky, haunted, dark",
            new String[]{"¿Incluye personajes?", "¿Formato?"}, new String[]{"Solo siluetas lejanas.", "PNG de alta calidad."});


        // ==================================================
        // --- 3. VECTORVIBE (Diseño Vectorial) ---
        // ==================================================
        // Gig Principal (Iconos)
        procesarIlustrador("VectorVibe", "Sarah Line", DESC_VECTOR, "Francés", "Nativo",
            "Francia", "Gobelins", "Visual Comm", "2022", "Vector Award", "Dribbble", "2023", "CorelDraw (Adv)",
            "Iconos Vectoriales", "Set de iconos personalizados para tu app o web.", 45.0, "GRÁFICOS Y DISEÑO/Diseño de Logo",
            "icons, ui, flat design, app, web",
            new String[]{"¿Cuántos iconos incluye?", "¿En qué formato se entregan?"},
            new String[]{"El precio base incluye hasta 5 iconos.", "Entrego en SVG, PNG y PDF."});

        // Gig Extra 1: Retrato
        agregarGigAdicional("VectorVibe", "Retrato Vectorial", "Avatar minimalista plano (Flat Design) de tu rostro.", 
            35.0, "GRÁFICOS Y DISEÑO/Ilustración", "retratos, portrait, vector, flat, minimalist",
            new String[]{"¿Se parece a mí?", "¿Es escalable?"}, new String[]{"Es una estilización minimalista.", "Totalmente, es vector infinito."});
        
        // Gig Extra 2: Furry
        agregarGigAdicional("VectorVibe", "Mascota Vectorial", "Diseño de mascota o personaje furry para marcas (estilo e-sports o logo).", 
            65.0, "GRÁFICOS Y DISEÑO/Ilustración", "furry, mascot, logo, vector, esports",
            new String[]{"¿Incluye archivo fuente?", "¿Sirve para Twitch?"}, new String[]{"Sí, archivo .AI incluido.", "Perfecto para emotes o logos."});
        
        // Gig Extra 3: Paisaje
        agregarGigAdicional("VectorVibe", "Paisaje Geométrico", "Wallpaper de paisaje estilo low-poly o vectorial plano.", 
            50.0, "GRÁFICOS Y DISEÑO/Ilustración", "paisajes, landscape, vector, geometric, wallpaper",
            new String[]{"¿Tamaño?", "¿Colores?"}, new String[]{"1920x1080 o 4k.", "Paleta de colores a tu elección."});


        // ==================================================
        // --- OTROS CATEGORÍAS (Se mantienen igual con 1 Gig principal) ---
        // ==================================================

        procesarIlustrador("PixelPanda", "Panda Motion", DESC_PANDA, "Español", "Alto",
            "España", "U-tad", "Animación", "2023", "Unity Cert", "Unity", "2023", "Aseprite (Exp)",
            "Sprite Animado", "Personaje pixel art con animación de caminata.", 30.0, "VIDEO Y ANIMACIÓN/Animación 2D",
            "pixel art, animation, sprite, game dev, retro",
            new String[]{"¿Haces fondos también?", "¿Qué tamaño tiene el sprite?"}, new String[]{"Sí, puedo añadir fondos por un costo extra.", "Normalmente 64x64 o 128x128 píxeles."});

        procesarIlustrador("SakuraInk", "Yumi Motion", DESC_SAKURA, "Japonés", "Nativo",
            "Japón", "Tokyo Designer", "VFX", "2021", "VFX Rookie", "Adobe", "2022", "Premiere (Exp)",
            "Intro Anime", "Opening estilo anime para tu canal de YouTube.", 60.0, "VIDEO Y ANIMACIÓN/Intros",
            "anime, intro, opening, youtube, motion graphics",
            new String[]{"¿Incluyes música?", "¿Cuánto dura la intro?"}, new String[]{"Uso música sin copyright o la que tú me envíes.", "Entre 10 y 15 segundos."});

        procesarIlustrador("NeonSamurai", "Kaito Marketing", DESC_NEON, "Inglés", "Alto",
            "Japón", "Keio", "Marketing", "2020", "Ads Cert", "Google", "2024", "FB Ads (Exp)",
            "Campaña Cyberpunk", "Diseño publicitario futurista para redes sociales.", 85.0, "MARKETING DIGITAL/Publicidad",
            "cyberpunk, ads, social media, futuristic, marketing",
            new String[]{"¿Para qué plataformas sirve?", "¿Incluyes el texto (copy)?"}, new String[]{"Optimizado para Instagram y TikTok.", "Sí, redacto un texto persuasivo breve."});

        procesarIlustrador("VistaMaster", "Marco SEO", DESC_VISTA, "Italiano", "Alto",
            "Italia", "Bocconi", "Analytics", "2020", "SEO Spec", "HubSpot", "2023", "SEMrush (Adv)",
            "Auditoría SEO", "Análisis completo de tu sitio web para posicionamiento.", 100.0, "MARKETING DIGITAL/SEO",
            "seo, audit, google, website, analytics",
            new String[]{"¿Qué herramientas usas?", "¿Entregas un reporte?"}, new String[]{"Uso SEMrush y Ahrefs.", "Sí, recibirás un PDF detallado con acciones a tomar."});

        procesarIlustrador("RealismPro", "Elena Strategy", DESC_REALISM, "Ruso", "Alto",
            "Rusia", "Moscow State", "Economía", "2018", "PMP", "PMI", "2020", "Excel (Exp)",
            "Plan de Negocios", "Estructuración financiera y proyección a 5 años.", 150.0, "NEGOCIOS/Gestión",
            "business plan, finance, startup, strategy, excel",
            new String[]{"¿Incluye investigación de mercado?", "¿Está listo para inversores?"}, new String[]{"Sí, analizo a tus 3 competidores principales.", "El formato es profesional y apto para presentar."});

        procesarIlustrador("FuzzyPaws", "Alex Support", DESC_FUZZY, "Inglés", "Nativo",
            "Canadá", "Toronto U", "Admin", "2022", "CS Excel", "Zendesk", "2023", "Office (Exp)",
            "Asistente Virtual", "Gestión de correos y agenda por 5 horas.", 25.0, "NEGOCIOS/Asistencia",
            "virtual assistant, admin, email, schedule, support",
            new String[]{"¿Trabajas fines de semana?", "¿Qué horario manejas?"}, new String[]{"Solo emergencias, costo extra.", "9 AM a 5 PM EST."});
    }

    // --- MÉTODO AUXILIAR PARA AGREGAR MÁS GIGS A UN USUARIO ---
    private static void agregarGigAdicional(String username, String titulo, String desc, Double precio, 
                                            String categoria, String tags, String[] qs, String[] as) {
        // Generamos el Gig en BD y sus carpetas
        garantizarGigCompleto(username, titulo, desc, precio, categoria, tags, qs, as);
        
        // Generar las carpetas físicas e imágenes para este Gig nuevo
        // (Reutilizamos generarArchivosGigs que busca TODOS los gigs del usuario y genera lo que falte)
        String rutaUsuario = System.getProperty("user.home") + File.separator + "Documents"
                + File.separator + "usuarios" + File.separator + GestorDeArchivos.CARPETA_ILUSTRADORES
                + File.separator + username;
        generarArchivosGigs(username, rutaUsuario);
    }

    private static void procesarIlustrador(
            String username, String displayName, String desc, 
            String idioma, String nivelIdioma,
            String paisEdu, String uniEdu, String tituloEdu, String anioEdu,
            String certNombre, String certEmisor, String certAnio,
            String habilidad,
            // Datos del Gig por defecto (Gig #1)
            String gigTitulo, String gigDesc, Double gigPrecio, String gigCat,
            String gigTags, String[] faqQs, String[] faqAs) {

        // 1. CREAR ESTRUCTURA DE CARPETAS
        String rutaUsuario = System.getProperty("user.home") + File.separator + "Documents"
                + File.separator + "usuarios" + File.separator + GestorDeArchivos.CARPETA_ILUSTRADORES
                + File.separator + username;
        
        crearDirectorio(rutaUsuario);
        crearDirectorio(rutaUsuario + File.separator + "gigs");

        // 2. GENERAR FOTO PERFIL
        String rutaPerfil = rutaUsuario + File.separator + "perfil.png";
        if (!new File(rutaPerfil).exists()) {
            crearImagenDummy(new File(rutaPerfil), username.substring(0,1), 200, 200);
        }

        // 3. ACTUALIZAR PERFIL BD
        String educacion = tituloEdu + " en " + uniEdu + " (" + paisEdu + ", " + anioEdu + ")";
        String cert = certNombre + " - " + certEmisor + " (" + certAnio + ")";
        
        String sqlUpdate = "UPDATE Usuarios SET nombreMostrado=?, descripcion=?, idioma=?, nivelIdioma=?, "
                + "educacion=?, certificaciones=?, habilidades=?, rutaFotoPerfil=? WHERE nombreUsuario=?";

        try (Connection conn = DatabaseConnector.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
            pstmt.setString(1, displayName); pstmt.setString(2, desc);
            pstmt.setString(3, idioma); pstmt.setString(4, nivelIdioma);
            pstmt.setString(5, educacion); pstmt.setString(6, cert);
            pstmt.setString(7, habilidad); pstmt.setString(8, rutaPerfil);
            pstmt.setString(9, username);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }

        // 4. GARANTIZAR GIG EN BD (Crear o Actualizar tags)
        garantizarGigCompleto(username, gigTitulo, gigDesc, gigPrecio, gigCat, gigTags, faqQs, faqAs);

        // 5. GENERAR CARPETAS E IMÁGENES PARA SUS GIGS
        generarArchivosGigs(username, rutaUsuario);
    }

    private static void garantizarGigCompleto(String autor, String titulo, String desc, Double precio, String categoria, 
                                              String tags, String[] qs, String[] as) {
        Connection conn = null;
        try {
            conn = DatabaseConnector.connect();
            int gigId = -1;

            // 1. Buscar si existe
            String sqlCheck = "SELECT id FROM Ilustraciones WHERE nombreAutor = ? AND titulo = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
                ps.setString(1, autor);
                ps.setString(2, titulo);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    gigId = rs.getInt("id");
                    // Actualizar Tags si ya existe
                    try(PreparedStatement up = conn.prepareStatement("UPDATE Ilustraciones SET etiquetas = ? WHERE id = ?")) {
                        up.setString(1, tags);
                        up.setInt(2, gigId);
                        up.executeUpdate();
                    }
                }
            }

            // 2. Si no existe, Insertar
            if (gigId == -1) {
                String sqlInsert = "INSERT INTO Ilustraciones(titulo, descripcion, precio, nombreAutor, categoria, estado, etiquetas, rutaImagen) "
                        + "VALUES (?, ?, ?, ?, ?, 'ACTIVO', ?, '')"; 
                try (PreparedStatement ins = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                    ins.setString(1, titulo);
                    ins.setString(2, desc);
                    ins.setDouble(3, precio);
                    ins.setString(4, autor);
                    ins.setString(5, categoria);
                    ins.setString(6, tags);
                    ins.executeUpdate();
                    ResultSet rs = ins.getGeneratedKeys();
                    if(rs.next()) gigId = rs.getInt(1);
                    System.out.println("[Generador] Gig creado: " + titulo + " (" + autor + ")");
                }
            }

            // 3. Insertar/Actualizar FAQs (Borrar previas para evitar duplicados y reinsertar)
            if (gigId != -1 && qs != null && as != null && qs.length == as.length) {
                try (PreparedStatement del = conn.prepareStatement("DELETE FROM Faqs WHERE gigId = ?")) {
                    del.setInt(1, gigId);
                    del.executeUpdate();
                }
                String sqlFaq = "INSERT INTO Faqs (gigId, pregunta, respuesta) VALUES (?, ?, ?)";
                try (PreparedStatement insFaq = conn.prepareStatement(sqlFaq)) {
                    for (int i = 0; i < qs.length; i++) {
                        insFaq.setInt(1, gigId);
                        insFaq.setString(2, qs[i]);
                        insFaq.setString(3, as[i]);
                        insFaq.addBatch();
                    }
                    insFaq.executeBatch();
                }
            }

        } catch (SQLException e) { e.printStackTrace(); }
        finally {
            if(conn != null) try { conn.close(); } catch (Exception e){}
        }
    }

    private static void generarArchivosGigs(String username, String rutaUsuario) {
        String sqlGigs = "SELECT id, titulo FROM Ilustraciones WHERE nombreAutor = ?";
        
        try (Connection conn = DatabaseConnector.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sqlGigs)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int gigId = rs.getInt("id");
                String tituloGig = rs.getString("titulo");
                
                // Limpiar nombre para carpeta
                String nombreCarpetaGig = tituloGig.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                String rutaCarpetaGig = rutaUsuario + File.separator + "gigs" + File.separator + nombreCarpetaGig;
                
                crearDirectorio(rutaCarpetaGig);
                
                List<String> rutasGeneradas = new ArrayList<>();

                // Generar 3 imágenes dummy
                for (int i = 0; i < 3; i++) {
                    String nombreImg = "img_" + i + ".png";
                    String rutaImgAbsoluta = rutaCarpetaGig + File.separator + nombreImg;
                    File archivoImg = new File(rutaImgAbsoluta);
                    
                    if (!archivoImg.exists()) {
                        String iniciales = obtenerIniciales(tituloGig) + " " + (i+1);
                        crearImagenDummy(archivoImg, iniciales, 700, 450);
                    }
                    rutasGeneradas.add(rutaImgAbsoluta);
                }

                // Actualizar rutas en BD
                actualizarPortadaGig(gigId, rutasGeneradas.get(0));
                actualizarGaleriaGig(gigId, rutasGeneradas);
            }
            
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private static void actualizarPortadaGig(int gigId, String ruta) {
        try (Connection conn = DatabaseConnector.connect(); 
             PreparedStatement ps = conn.prepareStatement("UPDATE Ilustraciones SET rutaImagen = ? WHERE id = ?")) {
            ps.setString(1, ruta);
            ps.setInt(2, gigId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private static void actualizarGaleriaGig(int gigId, List<String> rutas) {
        try (Connection conn = DatabaseConnector.connect()) {
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM GigGaleria WHERE gigId = ?")) {
                del.setInt(1, gigId);
                del.executeUpdate();
            }
            try (PreparedStatement ins = conn.prepareStatement("INSERT INTO GigGaleria(gigId, rutaImagen) VALUES(?,?)")) {
                for (String ruta : rutas) {
                    ins.setInt(1, gigId);
                    ins.setString(2, ruta);
                    ins.addBatch();
                }
                ins.executeBatch();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // --- UTILS ---

    private static void crearDirectorio(String ruta) {
        File f = new File(ruta);
        if (!f.exists()) f.mkdirs();
    }

    private static String obtenerIniciales(String texto) {
        String[] partes = texto.split(" ");
        String res = "";
        for (int i = 0; i < Math.min(2, partes.length); i++) {
            if(partes[i].length() > 0) res += partes[i].substring(0, 1).toUpperCase();
        }
        return res;
    }

    private static void crearImagenDummy(File archivoDestino, String texto, int w, int h) {
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        int r = (int)(Math.random() * 100) + 100; 
        int g = (int)(Math.random() * 100) + 50;
        int b = (int)(Math.random() * 100) + 50;
        g2d.setColor(new Color(r, g, b));
        g2d.fillRect(0, 0, w, h);

        g2d.setColor(Color.WHITE);
        int fontSize = Math.min(w, h) / 3;
        g2d.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        
        FontMetrics fm = g2d.getFontMetrics();
        int x = (w - fm.stringWidth(texto)) / 2;
        int y = ((h - fm.getHeight()) / 2) + fm.getAscent();
        
        g2d.drawString(texto, x, y);
        g2d.dispose();

        try {
            ImageIO.write(bufferedImage, "png", archivoDestino);
        } catch (IOException e) { e.printStackTrace(); }
    }
}