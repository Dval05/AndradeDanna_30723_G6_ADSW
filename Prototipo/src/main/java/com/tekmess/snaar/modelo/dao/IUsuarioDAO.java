package com.tekmess.snaar.modelo.dao;

import com.tekmess.snaar.modelo.entidad.EstadoCuenta;
import com.tekmess.snaar.modelo.entidad.Usuario;
import java.util.Date;

/**
 * Interfaz DAO para la entidad Usuario.
 * Define operaciones de persistencia de credenciales y estado de cuenta.
 */
public interface IUsuarioDAO {

    boolean crear(Usuario usuario);

    Usuario buscarPorNombreUsuario(String nombreUsuario);

    Usuario buscarPorCedula(String cedula);

    boolean actualizarContrasena(int idUsuario, String hash);

    boolean actualizarEstado(int idUsuario, EstadoCuenta estado);

    boolean actualizarIntentos(int idUsuario, int intentos);

    boolean actualizarPrimerAcceso(int idUsuario, boolean primerAcceso);

    boolean eliminarPorCedula(String cedula);

    boolean existeNombreUsuario(String nombreUsuario);

    int contarAccesosFallidosEnPeriodo(Date inicio, Date fin);

    java.util.List<com.tekmess.snaar.modelo.entidad.Usuario> listarTodos();
}
