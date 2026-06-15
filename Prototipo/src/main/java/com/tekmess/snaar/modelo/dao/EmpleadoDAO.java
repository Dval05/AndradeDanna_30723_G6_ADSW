package com.tekmess.snaar.modelo.dao;

import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.modelo.entidad.Rol;
import com.tekmess.snaar.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación DAO para Empleado con PostgreSQL.
 * Capa de Datos – persistencia CRUD de empleados.
 */
public class EmpleadoDAO implements IEmpleadoDAO {

    @Override
    public boolean crear(Empleado empleado) {
        String sql = "INSERT INTO empleados (cedula, nombres, correo, rol, fecha_creacion, fecha_modificacion) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empleado.getCedula());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getCorreo());
            ps.setString(4, empleado.getRol().name());
            ps.setTimestamp(5, new Timestamp(empleado.getFechaCreacion().getTime()));
            ps.setTimestamp(6, new Timestamp(empleado.getFechaModificacion().getTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear empleado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean editar(Empleado empleado) {
        String sql = "UPDATE empleados SET nombres = ?, correo = ?, rol = ?, fecha_modificacion = ? " +
                     "WHERE cedula = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombres());
            ps.setString(2, empleado.getCorreo());
            ps.setString(3, empleado.getRol().name());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setString(5, empleado.getCedula());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar empleado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String cedula) {
        String sql = "DELETE FROM empleados WHERE cedula = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Empleado buscarPorCedula(String cedula) {
        String sql = "SELECT * FROM empleados WHERE cedula = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapearEmpleado(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar empleado: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Empleado> listarTodos() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleados ORDER BY nombres";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar empleados: " + e.getMessage());
        }
        return empleados;
    }

    @Override
    public boolean existeCedula(String cedula) {
        String sql = "SELECT COUNT(*) FROM empleados WHERE cedula = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar cédula: " + e.getMessage());
        }
        return false;
    }

    @Override
    public int contarCreadosEnPeriodo(Date inicio, Date fin) {
        return contarPorPeriodo("fecha_creacion", inicio, fin);
    }

    @Override
    public int contarEditadosEnPeriodo(Date inicio, Date fin) {
        String sql = "SELECT COUNT(*) FROM empleados " +
                     "WHERE fecha_modificacion BETWEEN ? AND ? " +
                     "AND fecha_modificacion > fecha_creacion";
        return ejecutarConteo(sql, inicio, fin);
    }

    @Override
    public int contarEliminadosEnPeriodo(Date inicio, Date fin) {
        String sql = "SELECT COUNT(*) FROM auditoria_empleados " +
                     "WHERE tipo_evento = 'EMPLEADO_ELIMINADO' " +
                     "AND fecha_evento BETWEEN ? AND ?";
        return ejecutarConteo(sql, inicio, fin);
    }

    // ── Métodos auxiliares ─────────────────────────────────────

    private int contarPorPeriodo(String campo, Date inicio, Date fin) {
        String sql = "SELECT COUNT(*) FROM empleados WHERE " + campo + " BETWEEN ? AND ?";
        return ejecutarConteo(sql, inicio, fin);
    }

    private int ejecutarConteo(String sql, Date inicio, Date fin) {
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(inicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fin.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al contar registros: " + e.getMessage());
        }
        return 0;
    }

    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado emp = new Empleado();
        emp.setCedula(rs.getString("cedula"));
        emp.setNombres(rs.getString("nombres"));
        emp.setCorreo(rs.getString("correo"));
        emp.setRol(Rol.valueOf(rs.getString("rol")));
        emp.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        emp.setFechaModificacion(rs.getTimestamp("fecha_modificacion"));
        return emp;
    }
}
