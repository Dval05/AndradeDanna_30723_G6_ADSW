package com.tekmess.snaar.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton para la conexión a la base de datos PostgreSQL.
 * Capa de Datos – gestión centralizada de conexiones JDBC.
 */
public class ConexionBD {

    private static ConexionBD instancia;

    private static final String URL = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "jdbc:postgresql://localhost:5432/snaar_tekmess";
    private static final String USUARIO = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "postgres";
    private static final String CONTRASENA = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "postgres";

    private ConexionBD() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver PostgreSQL no encontrado: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia única (Singleton).
     */
    public static synchronized ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }

    /**
     * Obtiene una conexión activa a la base de datos.
     */
    public Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}
