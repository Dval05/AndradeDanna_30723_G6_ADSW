package com.tekmess.snaar.controlador.servicio;

import com.tekmess.snaar.modelo.dao.EmpleadoDAO;
import com.tekmess.snaar.modelo.dao.IUsuarioDAO;
import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.modelo.entidad.EstadoCuenta;
import com.tekmess.snaar.modelo.entidad.Rol;
import com.tekmess.snaar.modelo.entidad.Sesion;
import com.tekmess.snaar.modelo.entidad.Usuario;
import com.tekmess.snaar.util.CifradorContrasena;
import com.tekmess.snaar.util.ValidadorDatos;

/**
 * Servicio de Autenticación y Administración del Sistema (RF-SNAAR-02).
 */
public class AuthServicio {

    private final IUsuarioDAO usuarioDAO;
    private final CifradorContrasena cifrador;
    private final ValidadorDatos validador;

    private static final int MAX_INTENTOS = 3;

    public AuthServicio(IUsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
        this.cifrador = new CifradorContrasena();
        this.validador = new ValidadorDatos();
    }

    public Object[] iniciarSesion(String nombreUsuario, String contrasena) {
        String errorUsuario = validador.validarUsuario(nombreUsuario);
        if (errorUsuario != null || contrasena == null || contrasena.isBlank()) {
            return new Object[]{null, "Usuario o contraseña incorrectos."};
        }

        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(nombreUsuario.trim());

        if (usuario == null) {
            return new Object[]{null, "Usuario o contraseña incorrectos."};
        }

        if (usuario.getEstadoCuenta() == EstadoCuenta.BLOQUEADO) {
            return new Object[]{null, "Cuenta bloqueada. Contacte al administrador del sistema."};
        }

        if (usuario.getEstadoCuenta() != EstadoCuenta.ACTIVO) {
            return new Object[]{null, "Cuenta inactiva. Contacte al administrador del sistema."};
        }

        if (!cifrador.verificar(contrasena, usuario.getContrasenaHash())) {
            usuario.incrementarIntentos();
            usuarioDAO.actualizarIntentos(usuario.getIdUsuario(), usuario.getIntentosFallidos());

            if (usuario.getIntentosFallidos() >= MAX_INTENTOS) {
                usuarioDAO.actualizarEstado(usuario.getIdUsuario(), EstadoCuenta.BLOQUEADO);
                return new Object[]{null, "Cuenta bloqueada. Contacte al administrador del sistema."};
            }

            return new Object[]{null, "Usuario o contraseña incorrectos."};
        }

        usuario.reiniciarIntentos();
        usuarioDAO.actualizarIntentos(usuario.getIdUsuario(), 0);

        Sesion sesion = new Sesion(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                Rol.valueOf(obtenerRolUsuario(usuario.getCedula()))
        );

        return new Object[]{sesion, usuario.isPrimerAcceso() ? "PRIMER_ACCESO" : "OK"};
    }

    public String cambiarContrasena(String nombreUsuario, String contrasenaActual,
                                     String contrasenaNueva, String confirmacion) {
        if (contrasenaActual == null || contrasenaActual.isBlank()) {
            return "La contraseña actual es obligatoria.";
        }
        if (contrasenaNueva == null || confirmacion == null) {
            return "Debe ingresar y confirmar la nueva contraseña.";
        }
        if (!contrasenaNueva.equals(confirmacion)) {
            return "Las contraseñas no coinciden.";
        }

        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);
        if (usuario == null) {
            return "Usuario no encontrado.";
        }

        if (!cifrador.verificar(contrasenaActual, usuario.getContrasenaHash())) {
            return "La contraseña actual no es correcta.";
        }

        String errorPolitica = validador.validarContrasena(contrasenaNueva);
        if (errorPolitica != null) {
            return errorPolitica;
        }

        if (cifrador.verificar(contrasenaNueva, usuario.getContrasenaHash())) {
            return "La nueva contraseña no puede ser igual a la contraseña actual.";
        }

        String nuevoHash = cifrador.cifrar(contrasenaNueva);
        boolean actualizado = usuarioDAO.actualizarContrasena(usuario.getIdUsuario(), nuevoHash);

        if (!actualizado) {
            return "Error al actualizar la contraseña.";
        }

        if (usuario.isPrimerAcceso()) {
            usuarioDAO.actualizarPrimerAcceso(usuario.getIdUsuario(), false);
        }

        return "Contraseña actualizada exitosamente.";
    }

    public String recuperarContrasena(String correo, String nombreUsuario,
                                       String contrasenaNueva, String confirmacion) {
        String errorUsuario = validador.validarUsuario(nombreUsuario);
        if (errorUsuario != null) {
            return errorUsuario;
        }

        String errorCorreo = validador.validarCorreo(correo);
        if (errorCorreo != null) {
            return errorCorreo;
        }

        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(nombreUsuario.trim());
        if (usuario == null) {
            return "Los datos ingresados no corresponden a un usuario registrado.";
        }

        if (usuario.getEstadoCuenta() == EstadoCuenta.BLOQUEADO) {
            return "Cuenta bloqueada. Contacte al administrador del sistema.";
        }

        Empleado empleado = new EmpleadoDAO().buscarPorCedula(usuario.getCedula());
        if (empleado == null || empleado.getCorreo() == null ||
                !empleado.getCorreo().equalsIgnoreCase(correo.trim())) {
            return "Los datos ingresados no corresponden a un usuario registrado.";
        }

        if (contrasenaNueva == null || confirmacion == null) {
            return "Debe ingresar y confirmar la nueva contraseña.";
        }
        if (!contrasenaNueva.equals(confirmacion)) {
            return "Las contraseñas no coinciden.";
        }

        String errorPolitica = validador.validarContrasena(contrasenaNueva);
        if (errorPolitica != null) {
            return errorPolitica;
        }

        String nuevoHash = cifrador.cifrar(contrasenaNueva);
        boolean actualizado = usuarioDAO.actualizarContrasena(usuario.getIdUsuario(), nuevoHash);

        return actualizado ? "Contraseña restablecida exitosamente."
                           : "Error al restablecer la contraseña.";
    }

    public boolean cerrarSesion(Sesion sesion) {
        if (sesion != null) {
            sesion.invalidar();
            return true;
        }
        return false;
    }

    private String obtenerRolUsuario(String cedula) {
        Empleado emp = new EmpleadoDAO().buscarPorCedula(cedula);
        if (emp != null && emp.getRol() != null) {
            return emp.getRol().name();
        }
        return "GUARDIA";
    }
}
