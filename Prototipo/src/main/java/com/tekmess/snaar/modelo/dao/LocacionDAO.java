package com.tekmess.snaar.modelo.dao;

import com.tekmess.snaar.modelo.entidad.Locacion;
import com.tekmess.snaar.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocacionDAO {

    public LocacionDAO() {
        asegurarEstructura();
    }

    public List<Locacion> listarTodas() {
        List<Locacion> locaciones = new ArrayList<>();
        String sql = "SELECT * FROM locaciones ORDER BY activa DESC, nombre";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) locaciones.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar locaciones: " + e.getMessage());
        }
        return locaciones;
    }

    public List<Locacion> listarActivas() {
        List<Locacion> locaciones = new ArrayList<>();
        String sql = "SELECT * FROM locaciones WHERE activa = TRUE ORDER BY nombre";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) locaciones.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar locaciones activas: " + e.getMessage());
        }
        return locaciones;
    }

    public Locacion buscarPorId(int id) {
        String sql = "SELECT * FROM locaciones WHERE id_locacion = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("Error al buscar locacion: " + e.getMessage());
        }
        return null;
    }

    public boolean crear(Locacion locacion) {
        String sql = "INSERT INTO locaciones (nombre, ciudad, direccion, responsable, capacidad, activa) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preparar(ps, locacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear locacion: " + e.getMessage());
        }
        return false;
    }

    public boolean editar(Locacion locacion) {
        String sql = "UPDATE locaciones SET nombre=?, ciudad=?, direccion=?, responsable=?, capacidad=?, activa=? WHERE id_locacion=?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preparar(ps, locacion);
            ps.setInt(7, locacion.getIdLocacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar locacion: " + e.getMessage());
        }
        return false;
    }

    public boolean cambiarEstado(int id, boolean activa) {
        String sql = "UPDATE locaciones SET activa = ? WHERE id_locacion = ?";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, activa);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al cambiar estado de locacion: " + e.getMessage());
        }
        return false;
    }

    public int contarActivas() {
        return contar("SELECT COUNT(*) FROM locaciones WHERE activa = TRUE");
    }

    public int contarTotal() {
        return contar("SELECT COUNT(*) FROM locaciones");
    }

    public Map<Integer, Integer> contarEmpleadosAsignados() {
        Map<Integer, Integer> conteos = new HashMap<>();
        String sql = "SELECT id_locacion, COUNT(*) AS total FROM empleados WHERE id_locacion IS NOT NULL GROUP BY id_locacion";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                conteos.put(rs.getInt("id_locacion"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.err.println("Error al contar empleados por locacion: " + e.getMessage());
        }
        return conteos;
    }

    private int contar(String sql) {
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error al contar locaciones: " + e.getMessage());
        }
        return 0;
    }

    private void preparar(PreparedStatement ps, Locacion locacion) throws SQLException {
        ps.setString(1, locacion.getNombre());
        ps.setString(2, locacion.getCiudad());
        ps.setString(3, locacion.getDireccion());
        ps.setString(4, locacion.getResponsable());
        ps.setInt(5, locacion.getCapacidad());
        ps.setBoolean(6, locacion.isActiva());
    }

    private Locacion mapear(ResultSet rs) throws SQLException {
        Locacion locacion = new Locacion();
        locacion.setIdLocacion(rs.getInt("id_locacion"));
        locacion.setNombre(rs.getString("nombre"));
        locacion.setCiudad(rs.getString("ciudad"));
        locacion.setDireccion(rs.getString("direccion"));
        locacion.setResponsable(rs.getString("responsable"));
        locacion.setCapacidad(rs.getInt("capacidad"));
        locacion.setActiva(rs.getBoolean("activa"));
        return locacion;
    }

    private void asegurarEstructura() {
        String crearTabla = "CREATE TABLE IF NOT EXISTS locaciones (" +
                "id_locacion SERIAL PRIMARY KEY, " +
                "nombre VARCHAR(120) NOT NULL UNIQUE, " +
                "ciudad VARCHAR(80) NOT NULL, " +
                "direccion VARCHAR(180) NOT NULL, " +
                "responsable VARCHAR(120) NOT NULL, " +
                "capacidad INTEGER NOT NULL DEFAULT 0, " +
                "activa BOOLEAN NOT NULL DEFAULT TRUE, " +
                "fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)";
        String alterarEmpleado = "ALTER TABLE empleados ADD COLUMN IF NOT EXISTS id_locacion INTEGER REFERENCES locaciones(id_locacion)";
        String seed = "INSERT INTO locaciones (nombre, ciudad, direccion, responsable, capacidad, activa) VALUES " +
                "('Centro Norte', 'Quito', 'Av. Amazonas N34-120', 'Operaciones Norte', 18, TRUE), " +
                "('Bodega Sur', 'Quito', 'Av. Maldonado S12-44', 'Logistica Sur', 14, TRUE), " +
                "('Campus ESPE', 'Sangolqui', 'Av. General Ruminahui', 'Supervisor Campus', 20, TRUE), " +
                "('Centro Historico', 'Quito', 'Garcia Moreno y Chile', 'Turno Centro', 10, TRUE) " +
                "ON CONFLICT (nombre) DO NOTHING";
        String asignar = "UPDATE empleados SET id_locacion = (SELECT id_locacion FROM locaciones ORDER BY id_locacion LIMIT 1) WHERE id_locacion IS NULL";
        try (Connection conn = ConexionBD.getInstancia().getConexion();
             Statement st = conn.createStatement()) {
            st.execute(crearTabla);
            st.execute(alterarEmpleado);
            st.execute(seed);
            st.execute(asignar);
        } catch (SQLException e) {
            System.err.println("No se pudo asegurar estructura de locaciones: " + e.getMessage());
        }
    }
}
