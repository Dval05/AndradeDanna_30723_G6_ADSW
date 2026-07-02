package com.tekmess.snaar.modelo.dao;

import com.tekmess.snaar.modelo.entidad.Anotacion;
import com.tekmess.snaar.modelo.entidad.Reporte;
import com.tekmess.snaar.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación DAO para Reporte con PostgreSQL.
 * Capa de Datos – persistencia de reportes analíticos y anotaciones.
 */
public class ReporteDAO implements IReporteDAO {

    @Override
    public boolean guardar(Reporte reporte) {
        String sql = "INSERT INTO reportes (fecha_inicio, fecha_fin, total_empleados_creados, " +
                "total_empleados_editados, total_empleados_eliminados, total_accesos_fallidos, generado_por) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, new Timestamp(reporte.getFechaInicio().getTime()));
            ps.setTimestamp(2, new Timestamp(reporte.getFechaFin().getTime()));
            ps.setInt(3, reporte.getTotalEmpleadosCreados());
            ps.setInt(4, reporte.getTotalEmpleadosEditados());
            ps.setInt(5, reporte.getTotalEmpleadosEliminados());
            ps.setInt(6, reporte.getTotalAccesosFallidos());
            ps.setString(7, reporte.getGeneradoPor());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        reporte.setIdReporte(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar reporte: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Reporte buscarPorId(int id) {
        String sql = "SELECT * FROM reportes WHERE id_reporte = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reporte reporte = mapearReporte(rs);
                    reporte.setAnotaciones(obtenerAnotaciones(id));
                    return reporte;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reporte por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Reporte> listarPorFechas(Date inicio, Date fin) {
        List<Reporte> reportes = new ArrayList<>();
        String sql = "SELECT * FROM reportes WHERE fecha_generacion BETWEEN ? AND ? ORDER BY fecha_generacion DESC";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(inicio.getTime()));
            ps.setTimestamp(2, new Timestamp(fin.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reportes.add(mapearReporte(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reportes por fechas: " + e.getMessage());
        }
        return reportes;
    }

    @Override
    public List<Reporte> listarTodos() {
        List<Reporte> reportes = new ArrayList<>();
        String sql = "SELECT * FROM reportes ORDER BY fecha_generacion DESC";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                reportes.add(mapearReporte(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar reportes: " + e.getMessage());
        }
        return reportes;
    }

    @Override
    public boolean agregarAnotacion(int idReporte, Anotacion anotacion) {
        String sql = "INSERT INTO anotaciones (id_reporte, contenido, autor) VALUES (?, ?, ?)";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReporte);
            ps.setString(2, anotacion.getContenido());
            ps.setString(3, anotacion.getAutor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar anotación: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Anotacion> obtenerAnotaciones(int idReporte) {
        List<Anotacion> anotaciones = new ArrayList<>();
        String sql = "SELECT * FROM anotaciones WHERE id_reporte = ? ORDER BY fecha_creacion DESC";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReporte);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Anotacion anotacion = new Anotacion();
                    anotacion.setIdAnotacion(rs.getInt("id_anotacion"));
                    anotacion.setIdReporte(rs.getInt("id_reporte"));
                    anotacion.setContenido(rs.getString("contenido"));
                    anotacion.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    anotacion.setAutor(rs.getString("autor"));
                    anotaciones.add(anotacion);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener anotaciones: " + e.getMessage());
        }
        return anotaciones;
    }

    private Reporte mapearReporte(ResultSet rs) throws SQLException {
        Reporte reporte = new Reporte();
        reporte.setIdReporte(rs.getInt("id_reporte"));
        reporte.setFechaGeneracion(rs.getTimestamp("fecha_generacion"));
        reporte.setFechaInicio(rs.getTimestamp("fecha_inicio"));
        reporte.setFechaFin(rs.getTimestamp("fecha_fin"));
        reporte.setTotalEmpleadosCreados(rs.getInt("total_empleados_creados"));
        reporte.setTotalEmpleadosEditados(rs.getInt("total_empleados_editados"));
        reporte.setTotalEmpleadosEliminados(rs.getInt("total_empleados_eliminados"));
        reporte.setTotalAccesosFallidos(rs.getInt("total_accesos_fallidos"));
        reporte.setGeneradoPor(rs.getString("generado_por"));
        reporte.setAnotaciones(new ArrayList<>());
        return reporte;
    }
}
