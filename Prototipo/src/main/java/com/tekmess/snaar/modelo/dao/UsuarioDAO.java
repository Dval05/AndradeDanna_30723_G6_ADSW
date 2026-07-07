package com.tekmess.snaar.modelo.dao;

import com.tekmess.snaar.modelo.entidad.EstadoCuenta;
import com.tekmess.snaar.modelo.entidad.Usuario;
import com.tekmess.snaar.util.ConexionBD;

import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para Usuario con PostgreSQL.
 * Capa de Datos – persistencia de credenciales y estado de cuenta.
 */
public class UsuarioDAO implements IUsuarioDAO {

    public UsuarioDAO() {
        asegurarColumnaContrasenaTemporal();
    }

    @Override
    public boolean crear(Usuario usuario) {
        String sql = "INSERT INTO usuarios (cedula, nombre_usuario, contrasena_hash, contrasena_temporal, estado_cuenta, " +
                "intentos_fallidos, primer_acceso) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getCedula());
            ps.setString(2, usuario.getNombreUsuario());
            ps.setString(3, usuario.getContrasenaHash());
            ps.setString(4, usuario.getContrasenaTemporal());
            ps.setString(5, usuario.getEstadoCuenta().name());
            ps.setInt(6, usuario.getIntentosFallidos());
            ps.setBoolean(7, usuario.isPrimerAcceso());
            int filas = ps.executeUpdate();
            if (filas > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    usuario.setIdUsuario(keys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario buscarPorCedula(String cedula) {
        String sql = "SELECT * FROM usuarios WHERE cedula = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearUsuario(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por cédula: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean actualizarContrasena(int idUsuario, String hash) {
        String sql = "UPDATE usuarios SET contrasena_hash = ?, contrasena_temporal = NULL WHERE id_usuario = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean actualizarContrasenaTemporal(int idUsuario, String contrasenaTemporal) {
        String sql = "UPDATE usuarios SET contrasena_temporal = ? WHERE id_usuario = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contrasenaTemporal);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña temporal: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean actualizarCredencialesTemporales(int idUsuario, String hash, String contrasenaTemporal) {
        String sql = "UPDATE usuarios SET contrasena_hash = ?, contrasena_temporal = ?, primer_acceso = TRUE, " +
                "estado_cuenta = 'ACTIVO', intentos_fallidos = 0 WHERE id_usuario = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hash);
            ps.setString(2, contrasenaTemporal);
            ps.setInt(3, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar credenciales temporales: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean actualizarEstado(int idUsuario, EstadoCuenta estado) {
        String sql = "UPDATE usuarios SET estado_cuenta = ? WHERE id_usuario = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean actualizarIntentos(int idUsuario, int intentos) {
        String sql = "UPDATE usuarios SET intentos_fallidos = ? WHERE id_usuario = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, intentos);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar intentos: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean actualizarPrimerAcceso(int idUsuario, boolean primerAcceso) {
        String sql = "UPDATE usuarios SET primer_acceso = ? WHERE id_usuario = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, primerAcceso);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar primer acceso: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean eliminarPorCedula(String cedula) {
        String sql = "DELETE FROM usuarios WHERE cedula = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean existeNombreUsuario(String nombreUsuario) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE nombre_usuario = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error al verificar usuario: " + e.getMessage());
        }
        return false;
    }

    @Override
    public int contarAccesosFallidosEnPeriodo(Date inicio, Date fin) {
        String sql = "SELECT COUNT(*) FROM auditoria_accesos WHERE tipo_evento = 'LOGIN_FALLIDO' " +
                "AND fecha_evento BETWEEN ? AND ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(inicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fin.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error al contar accesos fallidos: " + e.getMessage());
        }
        return 0;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setCedula(rs.getString("cedula"));
        u.setNombreUsuario(rs.getString("nombre_usuario"));
        u.setContrasenaHash(rs.getString("contrasena_hash"));
        try {
            u.setContrasenaTemporal(rs.getString("contrasena_temporal"));
        } catch (SQLException ignored) {
            u.setContrasenaTemporal(null);
        }
        u.setEstadoCuenta(EstadoCuenta.valueOf(rs.getString("estado_cuenta")));
        u.setIntentosFallidos(rs.getInt("intentos_fallidos"));
        u.setPrimerAcceso(rs.getBoolean("primer_acceso"));
        Timestamp ts = rs.getTimestamp("ultimo_acceso");
        if (ts != null)
            u.setUltimoAcceso(ts);
        return u;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nombre_usuario";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    private void asegurarColumnaContrasenaTemporal() {
        String sql = "ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS contrasena_temporal VARCHAR(100)";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.println("No se pudo asegurar columna contrasena_temporal: " + e.getMessage());
        }
    }
}
