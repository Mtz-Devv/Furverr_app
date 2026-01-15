package database;

import modelos.Usuario;
import modelos.Ilustrador;
import modelos.Administrador;
import modelos.Ilustracion;
import modelos.Mensaje;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorDeDatos {

    // ==========================================
    // MÉTODOS DE LECTURA (SELECT)
    // ==========================================
    public static synchronized Usuario validarLogin(String nombreOCorreo, String contraseña) {
        String sql = "SELECT * FROM Usuarios WHERE (nombreUsuario = ? OR correo = ?) AND contraseña = ? AND estatus = 1";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreOCorreo);
            pstmt.setString(2, nombreOCorreo);
            pstmt.setString(3, contraseña);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return construirUsuario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized int validarLoginConCodigo(String nombreOCorreo, String contraseña) {
        // Retorna: 1=OK, 0=No existe, -1=Inactivo, 2=Pass incorrecta
        String sql = "SELECT estatus, contraseña FROM Usuarios WHERE nombreUsuario = ? OR correo = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreOCorreo);
            pstmt.setString(2, nombreOCorreo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    return 0;
                }
                if (rs.getInt("estatus") != 1) {
                    return -1;
                }
                if (!rs.getString("contraseña").equals(contraseña)) {
                    return 2;
                }
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static synchronized Usuario obtenerUsuario(String nombre) {
        String sql = "SELECT * FROM Usuarios WHERE nombreUsuario = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return construirUsuario(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized boolean existeUsuario(String nombre) {
        return checkExistencia("SELECT 1 FROM Usuarios WHERE nombreUsuario = ?", nombre);
    }

    public static synchronized boolean existeCorreo(String correo) {
        return checkExistencia("SELECT 1 FROM Usuarios WHERE correo = ?", correo);
    }

    // --- MODIFICADO: Lee ID y ESTADO ---
    public static synchronized List<Ilustracion> obtenerIlustracionesPorIlustrador(String autor) {
        List<Ilustracion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ilustraciones WHERE nombreAutor = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, autor);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {

                    String estado = "ACTIVO";
                    try {
                        estado = rs.getString("estado");
                    } catch (SQLException e) {
                    }
                    if (estado == null)
                        estado = "ACTIVO";

                    lista.add(new Ilustracion(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getString("rutaImagen"),
                            rs.getString("nombreAutor"),
                            rs.getString("categoria"),
                            rs.getString("etiquetas"),
                            estado));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static synchronized List<Ilustracion> listarIlustraciones() {
        List<Ilustracion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ilustraciones";

        try (Connection conn = DatabaseConnector.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(new Ilustracion(
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getString("rutaImagen"),
                        rs.getString("nombreAutor"),
                        rs.getString("categoria"),
                        rs.getString("etiquetas")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static synchronized List<Ilustrador> listarIlustradoresActivos() {
        List<Ilustrador> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios WHERE tipo = 'ilustrador' AND estatus = 1";
        try (Connection conn = DatabaseConnector.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add((Ilustrador) construirUsuario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static synchronized List<Usuario> listarTodosLosUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (Connection conn = DatabaseConnector.connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(construirUsuario(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ==========================================
    // MÉTODOS DE ESCRITURA (INSERT/UPDATE)
    // ==========================================
    public static synchronized boolean registrarUsuario(Usuario u) {
        String sql = "INSERT INTO Usuarios(nombreUsuario, correo, contraseña, tipo, estatus) VALUES(?,?,?,?,1)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, u.getNombreUsuario());
            pstmt.setString(2, u.getCorreo());
            pstmt.setString(3, u.getContraseña());
            pstmt.setString(4, u.getTipo());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static synchronized boolean registrarIlustracion(Ilustracion i) {
        String sql = "INSERT INTO Ilustraciones(titulo, descripcion, precio, rutaImagen, nombreAutor) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, i.getTitulo());
            pstmt.setString(2, i.getDescripcion());
            pstmt.setDouble(3, i.getPrecio());
            pstmt.setString(4, i.getRutaImagen());
            pstmt.setString(5, i.getNombreAutor());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static synchronized Usuario actualizarRolUsuario(String nombre, String rol) {
        ejecutarUpdate("UPDATE Usuarios SET tipo = ? WHERE nombreUsuario = ?", rol, nombre);
        return obtenerUsuario(nombre);
    }

    public static synchronized boolean actualizarPerfilIlustrador(String usuario, String nombreMostrado, String desc,
            String idioma, String nivel, String ruta) {
        String sql = "UPDATE Usuarios SET nombreMostrado=?, descripcion=?, idioma=?, nivelIdioma=?, rutaFotoPerfil=? WHERE nombreUsuario=?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreMostrado);
            pstmt.setString(2, desc);
            pstmt.setString(3, idioma);
            pstmt.setString(4, nivel);
            pstmt.setString(5, ruta);
            pstmt.setString(6, usuario);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static synchronized boolean actualizarIlustrador(Ilustrador i) {
        String sql = "UPDATE Usuarios SET descripcion=?, educacion=?, certificaciones=?, habilidades=? WHERE nombreUsuario=?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, i.getDescripcion());
            pstmt.setString(2, i.getEducacion());
            pstmt.setString(3, i.getCertificaciones());
            pstmt.setString(4, i.getHabilidades());
            pstmt.setString(5, i.getNombreUsuario());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static synchronized boolean actualizarContraseña(String usuario, String pass) {
        return ejecutarUpdate("UPDATE Usuarios SET contraseña = ? WHERE nombreUsuario = ?", pass, usuario);
    }

    public static synchronized boolean verificarIdentidad(String usuario, String correo) {
        return checkExistencia("SELECT 1 FROM Usuarios WHERE nombreUsuario = ? AND correo = ?", usuario, correo);
    }

    public static synchronized boolean actualizarEstatusUsuario(String nombreUsuario, boolean nuevoEstatus) {
        int estatusInt = nuevoEstatus ? 1 : 0;
        String sql = "UPDATE Usuarios SET estatus = ? WHERE nombreUsuario = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, estatusInt);
            pstmt.setString(2, nombreUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // MÉTODOS PARA DASHBOARD
    // ==========================================
    public static Object[][] getPedidosActivos(String nombreIlustrador) {
        List<Object[]> lista = new ArrayList<>();
        // MODIFICADO: Se incluye el ID y se filtra por estado != 'TERMINADO'
        String sql = "SELECT id, comprador, servicio, fechaVence, total, estado FROM Pedidos WHERE ilustrador = ? AND estado != 'TERMINADO'";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreIlustrador);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[] {
                            rs.getInt("id"), // ID oculto
                            rs.getString("comprador"),
                            rs.getString("servicio"),
                            rs.getString("fechaVence"),
                            rs.getDouble("total"),
                            rs.getString("estado")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Object[][] data = new Object[lista.size()][6]; // Ahora son 6 columnas
        for (int i = 0; i < lista.size(); i++) {
            data[i] = lista.get(i);
        }
        return data;
    }

    public static synchronized boolean actualizarEstadoPedido(int idPedido, String nuevoEstado) {
        String sql = "UPDATE Pedidos SET estado = ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idPedido);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static synchronized double obtenerGananciasTotales(String nombreIlustrador) {
        String sql = "SELECT SUM(total) FROM Pedidos WHERE ilustrador = ? AND estado = 'TERMINADO'";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreIlustrador);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static synchronized double obtenerTotalPedidosActivos(String nombreIlustrador) {
        String sql = "SELECT SUM(total) FROM Pedidos WHERE ilustrador = ? AND estado != 'TERMINADO'";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreIlustrador);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static void insertarDatosDePruebaSiVacio() {
        if (checkExistencia("SELECT 1 FROM Usuarios LIMIT 1")) {
            return;
        }
        System.out.println("Insertando datos de prueba...");
        try (Connection conn = DatabaseConnector.connect(); Statement stmt = conn.createStatement()) {

            // 1. Usuarios (Cliente y Admin)
            stmt.addBatch(
                    "INSERT INTO Usuarios(nombreUsuario, correo, contraseña, tipo) VALUES ('cliente1', 'cliente1@ex.com', 'pass123', 'cliente')");
            stmt.addBatch(
                    "INSERT INTO Usuarios(nombreUsuario, correo, contraseña, tipo) VALUES ('admin', 'admin@ex.com', 'adminpass', 'admin')");

            // 2. Ilustradores
            String baseInsert = "INSERT INTO Usuarios(nombreUsuario, correo, contraseña, tipo, nombreMostrado, descripcion, precioBase, rutaFotoPerfil, idioma) VALUES ";
            stmt.addBatch(baseInsert
                    + "('ArtByLaura', 'laura@ex.com', 'artpass', 'ilustrador', 'ArtByLaura', 'Especialista en retratos.', 50.0, 'foto_perfil/ArtByLaura.png', 'Inglés')");
            stmt.addBatch(baseInsert
                    + "('PixelPanda', 'panda@ex.com', 'artpass2', 'ilustrador', 'PixelPanda', 'Pixel Art adorable.', 25.0, 'foto_perfil/PixelPanda.png', 'Español')");

            // 3. Ilustraciones
            String gigInsert = "INSERT INTO Ilustraciones(titulo, descripcion, precio, rutaImagen, nombreAutor, estado) VALUES ";
            stmt.addBatch(gigInsert
                    + "('Guerrero Lunar', 'Retrato épico.', 65.0, '/img/guerrero.png', 'ArtByLaura', 'ACTIVO')");
            stmt.addBatch(gigInsert
                    + "('Bosque Encantado', 'Paisaje mágico.', 80.0, '/img/bosque.png', 'ArtByLaura', 'ACTIVO')");

            stmt.executeBatch();
            System.out.println("Datos de prueba cargados en MySQL.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // HELPERS PRIVADOS
    // ==========================================
    private static Usuario construirUsuario(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        Usuario u;

        if ("ilustrador".equals(tipo)) {
            Ilustrador i = new Ilustrador(rs.getString("nombreUsuario"), rs.getString("correo"),
                    rs.getString("contraseña"));

            i.setNombreMostrado(rs.getString("nombreMostrado"));

            i.setDescripcion(rs.getString("descripcion"));
            i.setPrecioBase(rs.getDouble("precioBase"));

            i.setRutaFotoPerfil(rs.getString("rutaFotoPerfil"));
            i.setIdioma(rs.getString("idioma"));
            i.setNivelIdioma(rs.getString("nivelIdioma"));
            i.setEducacion(rs.getString("educacion"));
            i.setCertificaciones(rs.getString("certificaciones"));
            i.setHabilidades(rs.getString("habilidades"));
            u = i;
        } else if ("admin".equals(tipo)) {
            u = new Administrador(rs.getString("nombreUsuario"), rs.getString("correo"), rs.getString("contraseña"));
        } else {
            u = new Usuario(rs.getString("nombreUsuario"), rs.getString("correo"), rs.getString("contraseña"));
        }
        u.setTipo(tipo);
        u.setEstatus(rs.getInt("estatus") == 1);
        u.setNombreMostrado(rs.getString("nombreMostrado"));
        u.setDescripcion(rs.getString("descripcion"));
        u.setRutaFotoPerfil(rs.getString("rutaFotoPerfil"));

        return u;
    }

    private static boolean checkExistencia(String sql, String... params) {
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setString(i + 1, params[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private static boolean ejecutarUpdate(String sql, String val, String key) {
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, val);
            pstmt.setString(2, key);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static synchronized List<Ilustracion> buscarIlustraciones(String busqueda) {
        List<Ilustracion> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ilustraciones WHERE titulo LIKE ? OR descripcion LIKE ? OR categoria LIKE ? OR etiquetas LIKE ?";

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String termino = "%" + busqueda + "%";
            pstmt.setString(1, termino);
            pstmt.setString(2, termino);
            pstmt.setString(3, termino);
            pstmt.setString(4, termino);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Ilustracion(
                            rs.getString("titulo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getString("rutaImagen"),
                            rs.getString("nombreAutor"),
                            rs.getString("categoria"),
                            rs.getString("etiquetas")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static synchronized List<String> obtenerCategoriasDisponibles() {
        return List.of(
                "AI Artists", "Logo Design", "Game Art", "Illustration",
                "Character Modeling", "Portraits & Caricatures", "Web Design");
    }

    // --- MÉTODO MAESTRO ACTUALIZADO PARA GUARDAR UN GIG COMPLETO ---
    public static synchronized boolean publicarServicioCompleto(
            Ilustracion gigBase,
            List<modelos.Paquete> paquetes,
            List<modelos.GigExtra> extras,
            List<modelos.Faq> faqs,
            List<String> rutasImagenes) {

        Connection conn = null;
        try {
            conn = DatabaseConnector.connect();
            conn.setAutoCommit(false);

            // Insertar con estado ACTIVO por defecto
            String sqlGig = "INSERT INTO Ilustraciones(titulo, descripcion, precio, rutaImagen, nombreAutor, categoria, etiquetas, estado) VALUES(?,?,?,?,?,?,?,'ACTIVO')";
            int gigId = -1;

            try (PreparedStatement psGig = conn.prepareStatement(sqlGig, Statement.RETURN_GENERATED_KEYS)) {
                psGig.setString(1, gigBase.getTitulo());
                psGig.setString(2, gigBase.getDescripcion());
                psGig.setDouble(3, gigBase.getPrecio());
                psGig.setString(4, rutasImagenes.get(0)); // Portada
                psGig.setString(5, gigBase.getNombreAutor());
                psGig.setString(6, gigBase.getCategoria());
                psGig.setString(7, gigBase.getEtiquetas());
                psGig.executeUpdate();

                try (ResultSet rs = psGig.getGeneratedKeys()) {
                    if (rs.next())
                        gigId = rs.getInt(1);
                }
            }

            if (gigId == -1)
                throw new SQLException("No se pudo obtener el ID del Gig.");

            // 2. Insertar Paquetes
            String sqlPack = "INSERT INTO Paquetes(gigId, tipo, nombre, descripcion, tiempoEntrega, revisiones, precio) VALUES(?,?,?,?,?,?,?)";
            try (PreparedStatement psPack = conn.prepareStatement(sqlPack)) {
                for (modelos.Paquete p : paquetes) {
                    psPack.setInt(1, gigId);
                    psPack.setString(2, p.tipo);
                    psPack.setString(3, p.nombre);
                    psPack.setString(4, p.descripcion);
                    psPack.setInt(5, p.entregaDias);
                    psPack.setInt(6, p.revisiones);
                    psPack.setDouble(7, p.precio);
                    psPack.addBatch();
                }
                psPack.executeBatch();
            }

            // 3. Insertar Extras
            if (!extras.isEmpty()) {
                String sqlExtra = "INSERT INTO GigExtras(gigId, titulo, descripcion, precio, tiempoAdicional) VALUES(?,?,?,?,?)";
                try (PreparedStatement psExtra = conn.prepareStatement(sqlExtra)) {
                    for (modelos.GigExtra ex : extras) {
                        psExtra.setInt(1, gigId);
                        psExtra.setString(2, ex.titulo);
                        psExtra.setString(3, ex.descripcion);
                        psExtra.setDouble(4, ex.precio);
                        psExtra.setInt(5, ex.diasAdicionales);
                        psExtra.addBatch();
                    }
                    psExtra.executeBatch();
                }
            }

            // 4. Insertar FAQs
            if (!faqs.isEmpty()) {
                String sqlFaq = "INSERT INTO Faqs(gigId, pregunta, respuesta) VALUES(?,?,?)";
                try (PreparedStatement psFaq = conn.prepareStatement(sqlFaq)) {
                    for (modelos.Faq f : faqs) {
                        psFaq.setInt(1, gigId);
                        psFaq.setString(2, f.pregunta);
                        psFaq.setString(3, f.respuesta);
                        psFaq.addBatch();
                    }
                    psFaq.executeBatch();
                }
            }

            // 5. Insertar Galería
            if (!rutasImagenes.isEmpty()) {
                String sqlGal = "INSERT INTO GigGaleria(gigId, rutaImagen) VALUES(?,?)";
                try (PreparedStatement psGal = conn.prepareStatement(sqlGal)) {
                    for (String ruta : rutasImagenes) {
                        psGal.setInt(1, gigId);
                        psGal.setString(2, ruta);
                        psGal.addBatch();
                    }
                    psGal.executeBatch();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null)
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            return false;
        } finally {
            if (conn != null)
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                }
        }
    }

    public static List<modelos.Paquete> obtenerPaquetesGig(String tituloGig) {
        return new ArrayList<>();
    }

    // ==========================================
    // MÉTODOS PARA RECUPERAR DETALLES DE UN GIG (PREVIEW)
    // ==========================================

    public static synchronized List<String> obtenerGaleriaGig(int gigId) {
        List<String> rutas = new ArrayList<>();
        // Primero obtenemos la portada de la tabla principal
        String sqlPortada = "SELECT rutaImagen FROM Ilustraciones WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect();
                PreparedStatement pstmt = conn.prepareStatement(sqlPortada)) {
            pstmt.setInt(1, gigId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    rutas.add(rs.getString("rutaImagen"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Luego las adicionales de la galería
        String sqlGal = "SELECT rutaImagen FROM GigGaleria WHERE gigId = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sqlGal)) {
            pstmt.setInt(1, gigId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String r = rs.getString("rutaImagen");
                    // Evitar duplicar la portada si se guardó doble
                    if (!rutas.contains(r))
                        rutas.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rutas;
    }

    public static synchronized List<modelos.Paquete> obtenerPaquetesGig(int gigId) {
        List<modelos.Paquete> lista = new ArrayList<>();
        String sql = "SELECT * FROM Paquetes WHERE gigId = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gigId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new modelos.Paquete(
                            rs.getString("tipo"), rs.getString("nombre"), rs.getString("descripcion"),
                            rs.getInt("tiempoEntrega"), rs.getInt("revisiones"), rs.getDouble("precio")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static synchronized List<modelos.GigExtra> obtenerExtrasGig(int gigId) {
        List<modelos.GigExtra> lista = new ArrayList<>();
        String sql = "SELECT * FROM GigExtras WHERE gigId = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gigId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new modelos.GigExtra(
                            rs.getString("titulo"), rs.getString("descripcion"),
                            rs.getDouble("precio"), rs.getInt("tiempoAdicional")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static synchronized List<modelos.Faq> obtenerFaqsGig(int gigId) {
        List<modelos.Faq> lista = new ArrayList<>();
        String sql = "SELECT * FROM Faqs WHERE gigId = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gigId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new modelos.Faq(rs.getString("pregunta"), rs.getString("respuesta")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Cambia el estado entre 'ACTIVO' y 'BORRADOR'
    public static synchronized boolean actualizarEstadoGig(int gigId, String nuevoEstado) {
        String sql = "UPDATE Ilustraciones SET estado = ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, gigId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Elimina el Gig y toda su información relacionada (Cascada manual)
    public static synchronized boolean eliminarGig(int gigId) {
        Connection conn = null;
        try {
            conn = DatabaseConnector.connect();
            conn.setAutoCommit(false); // Transacción para seguridad

            // 1. Borrar datos dependientes primero
            eliminarHijos(conn, "GigGaleria", gigId);
            eliminarHijos(conn, "Faqs", gigId);
            eliminarHijos(conn, "GigExtras", gigId);
            eliminarHijos(conn, "Paquetes", gigId);

            // 2. Borrar el Gig principal
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Ilustraciones WHERE id = ?")) {
                pstmt.setInt(1, gigId);
                pstmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null)
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            return false;
        } finally {
            if (conn != null)
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                }
        }
    }

    // Helper privado para borrar tablas hijas
    private static void eliminarHijos(Connection conn, String tabla, int gigId) throws SQLException {
        String sql = "DELETE FROM " + tabla + " WHERE gigId = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, gigId);
            pstmt.executeUpdate();
        }
    }

    // ==========================================
    // MÉTODO PARA EL FEED DEL CLIENTE
    // ==========================================

    /**
     * Obtiene todos los Gigs activos que coincidan con el filtro.
     * Si el filtro es "todo" o vacío, trae todos.
     */
    public static synchronized List<Ilustracion> buscarGigsActivos(String filtro) {
        List<Ilustracion> lista = new ArrayList<>();
        String sql;

        if (filtro == null || filtro.isEmpty() || filtro.equalsIgnoreCase("todo")) {
            sql = "SELECT * FROM Ilustraciones WHERE estado = 'ACTIVO'";
        } else {
            // Búsqueda flexible en título, descripción, categoría o etiquetas
            sql = "SELECT * FROM Ilustraciones WHERE estado = 'ACTIVO' AND " +
                    "(titulo LIKE ? OR descripcion LIKE ? OR categoria LIKE ? OR etiquetas LIKE ?)";
        }

        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (!(filtro == null || filtro.isEmpty() || filtro.equalsIgnoreCase("todo"))) {
                String termino = "%" + filtro + "%";
                pstmt.setString(1, termino);
                pstmt.setString(2, termino);
                pstmt.setString(3, termino);
                pstmt.setString(4, termino);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Ilustracion(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getString("rutaImagen"),
                            rs.getString("nombreAutor"),
                            rs.getString("categoria"),
                            rs.getString("etiquetas"),
                            rs.getString("estado")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Actualiza solo la descripción (Usado por Panel_Cuenta)
     */
    public static synchronized boolean actualizarDescripcion(String username, String nuevaDescripcion) {
        String sql = "UPDATE Usuarios SET descripcion = ? WHERE nombreUsuario = ?";
        try (Connection conn = DatabaseConnector.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevaDescripcion);
            pstmt.setString(2, username);

            // executeUpdate devuelve el número de filas cambiadas.
            // Si devuelve > 0, es que funcionó.
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error actualizando descripción: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza solo el Nombre Mostrado (Usado por Panel_Cuenta)
     */
    public static synchronized boolean actualizarNombreMostrado(String username, String nuevoNombre) {
        String sql = "UPDATE Usuarios SET nombreMostrado = ? WHERE nombreUsuario = ?";
        try (Connection conn = DatabaseConnector.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoNombre);
            pstmt.setString(2, username);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error actualizando nombre mostrado: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // MÉTODOS PARA EL CLIENTE (foto de perfil)
    // ==========================================

    /**
     * Actualiza la ruta de la foto de perfil en la BD.
     */
    public static synchronized boolean actualizarFotoPerfil(String username, String rutaImagen) {
        String sql = "UPDATE Usuarios SET rutaFotoPerfil = ? WHERE nombreUsuario = ?";
        try (Connection conn = DatabaseConnector.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rutaImagen);
            pstmt.setString(2, username);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error actualizando foto: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // ELIMINAR PP
    // ==========================================

    public static synchronized boolean eliminarFotoPerfil(String username) {
        String sql = "UPDATE Usuarios SET rutaFotoPerfil = NULL WHERE nombreUsuario = ?";
        try (Connection conn = DatabaseConnector.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // ==========================================
    // MÉTODOS PARA EL CLIENTE (PEDIDOS)
    // ==========================================

    /**
     * Obtiene el historial de pedidos de un cliente específico.
     */
    public static synchronized Object[][] obtenerPedidosPorCliente(String nombreCliente) {
        List<Object[]> lista = new ArrayList<>();

        // AGREGAMOS LA NUEVA COLUMNA AL SELECT
        String sql = "SELECT id, ilustrador, servicio, fechaVence, total, estado, fecha_creacion_pedido FROM Pedidos WHERE comprador = ?";

        try (Connection conn = DatabaseConnector.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreCliente);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Validamos que la fecha no sea null para evitar errores
                    String fechaCreacion = rs.getString("fecha_creacion_pedido");
                    if (fechaCreacion == null)
                        fechaCreacion = "Desconocida";

                    lista.add(new Object[] {
                            rs.getInt("id"), // 0: ID (Oculto en tabla)
                            rs.getString("servicio"), // 1: Servicio (Visible)
                            rs.getString("ilustrador"), // 2: Ilustrador (Visible)
                            rs.getDouble("total"), // 3: Total (Oculto en tabla)
                            rs.getString("fechaVence"), // 4: Entrega (Visible)
                            rs.getString("estado"), // 5: Estado (Visible)
                            fechaCreacion // 6: Fecha Creación (Oculto - Nuevo)
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Convertir lista a matriz
        Object[][] data = new Object[lista.size()][7]; // Ahora son 7 columnas de datos
        for (int i = 0; i < lista.size(); i++) {
            data[i] = lista.get(i);
        }
        return data;
    }

    /**
     * Crea un nuevo pedido en la base de datos.
     * Se usa cuando el cliente contacta/contrata un servicio.
     */
    public static synchronized boolean crearNuevoPedido(String comprador, String ilustrador, String servicio,
            double total, String fechaEntrega) {
        String sql = "INSERT INTO Pedidos (comprador, ilustrador, servicio, fechaVence, total, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, comprador);
            pstmt.setString(2, ilustrador);
            pstmt.setString(3, servicio);
            pstmt.setString(4, fechaEntrega); // En una app real, calcular fecha actual + días entrega
            pstmt.setDouble(5, total);
            pstmt.setString(6, "PENDIENTE"); // Estado inicial: El ilustrador debe aceptar

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error creando pedido: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // MÉTODOS DE MENSAJERÍA (CHAT)
    // ==========================================

    public static synchronized boolean enviarMensaje(String remitente, String destinatario, String mensaje,
            String archivo) {
        String sql = "INSERT INTO Mensajes(remitente, destinatario, mensaje, archivo, leido) VALUES(?,?,?,?,0)";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, remitente);
            pstmt.setString(2, destinatario);
            pstmt.setString(3, mensaje);
            pstmt.setString(4, archivo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static synchronized List<Mensaje> obtenerMensajes(String usuario1, String usuario2) {
        List<Mensaje> lista = new ArrayList<>();
        String sql = "SELECT * FROM Mensajes WHERE (remitente = ? AND destinatario = ?) OR (remitente = ? AND destinatario = ?) ORDER BY fecha ASC";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario1);
            pstmt.setString(2, usuario2);
            pstmt.setString(3, usuario2);
            pstmt.setString(4, usuario1);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Mensaje(
                            rs.getInt("id"),
                            rs.getString("remitente"),
                            rs.getString("destinatario"),
                            rs.getString("mensaje"),
                            rs.getTimestamp("fecha"),
                            rs.getString("archivo"),
                            rs.getBoolean("leido")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Obtiene la lista de usuarios con los que 'usuario' ha hablado
    public static synchronized List<String> obtenerConversaciones(String usuario) {
        List<String> lista = new ArrayList<>();
        // Seleccionamos los usuarios distintos que han enviado o recibido mensajes con
        // 'usuario'
        String sql = "SELECT DISTINCT other_user FROM (" +
                "SELECT remitente AS other_user FROM Mensajes WHERE destinatario = ? " +
                "UNION " +
                "SELECT destinatario AS other_user FROM Mensajes WHERE remitente = ?" +
                ") AS conversations";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setString(2, usuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getString("other_user"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static synchronized boolean marcarMensajesComoLeidos(String remitente, String destinatario) {
        String sql = "UPDATE Mensajes SET leido = 1 WHERE remitente = ? AND destinatario = ? AND leido = 0";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, remitente);
            pstmt.setString(2, destinatario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static synchronized int contarMensajesNoLeidos(String destinatario) {
        String sql = "SELECT COUNT(*) FROM Mensajes WHERE destinatario = ? AND leido = 0";
        try (Connection conn = DatabaseConnector.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, destinatario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}