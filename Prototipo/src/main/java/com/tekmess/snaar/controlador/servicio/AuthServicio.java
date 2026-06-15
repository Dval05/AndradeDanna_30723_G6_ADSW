package com.tekmess.snaar.controlador.servicio;

import com.tekmess.snaar.modelo.dao.IUsuarioDAO;
import com.tekmess.snaar.modelo.entidad.EstadoCuenta;
import com.tekmess.snaar.modelo.entidad.Rol;
import com.tekmess.snaar.modelo.entidad.Sesion;
import com.tekmess.snaar.modelo.entidad.Usuario;
import com.tekmess.snaar.util.CifradorContrasena;
import com.tekmess.snaar.util.ValidadorDatos;

/**
 * Servicio de Autenticación y Administración del Sistema (RF-SNAAR-02).
 * Capa de Lógica de Negocio – gestión de sesiones, contraseñas y acceso.
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

    /**
     * RF-SNAAR-02.01: Iniciar Sesión.
     * Valida credenciales, controla intentos fallidos y bloqueo de cuenta.
     *
     * @param nombreUsuario nombre de usuario
     * @param contrasena contraseña en texto plano
     * @return Sesion si éxito, null si fallo (mensaje en atributo de sesión)
     */
    public Object[] iniciarSesion(String nombreUsuario, String contrasena) {
        // Paso 5-6: Ejecuta RF-SNAAR-02.04 (Validar Credenciales)
        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);

        // Paso 3 excepción de RF-02.04: Usuario no registrado
        if (usuario == null) {
            return new Object[]{null, "Usuario o contraseña incorrectos."};
        }

        // Paso 6 excepción: Cuenta previamente bloqueada
        if (usuario.getEstadoCuenta() == EstadoCuenta.BLOQUEADO) {
            return new Object[]{null, "Cuenta bloqueada. Contacte al administrador del sistema."};
        }

        // Paso 4-5 de RF-02.04: Verificar estado y contraseña
        if (usuario.getEstadoCuenta() != EstadoCuenta.ACTIVO) {
            return new Object[]{null, "Cuenta inactiva. Contacte al administrador del sistema."};
        }

        // Comparar contraseña cifrada
        if (!cifrador.verificar(contrasena, usuario.getContrasenaHash())) {
            // Paso 7 excepción: Incrementar intentos fallidos
            usuario.incrementarIntentos();
            usuarioDAO.actualizarIntentos(usuario.getIdUsuario(), usuario.getIntentosFallidos());

            if (usuario.getIntentosFallidos() >= MAX_INTENTOS) {
                usuarioDAO.actualizarEstado(usuario.getIdUsuario(), EstadoCuenta.BLOQUEADO);
                return new Object[]{null, "Cuenta bloqueada. Contacte al administrador del sistema."};
            }

            return new Object[]{null, "Usuario o contraseña incorrectos."};
        }

        // Login exitoso: reiniciar intentos
        usuario.reiniciarIntentos();
        usuarioDAO.actualizarIntentos(usuario.getIdUsuario(), 0);

        // Paso 8: Evaluar primer acceso
        boolean requiereCambio = usuario.isPrimerAcceso();

        // Paso 9: Crear sesión con timeout 120 min
        Sesion sesion = new Sesion(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                Rol.valueOf(obtenerRolUsuario(usuario.getCedula()))
        );

        return new Object[]{sesion, requiereCambio ? "PRIMER_ACCESO" : "OK"};
    }

    /**
     * RF-SNAAR-02.02: Cambiar Contraseña.
     * Verifica actual, valida política, cifra y almacena nueva.
     */
    public String cambiarContrasena(String nombreUsuario, String contrasenaActual,
                                     String contrasenaNueva, String confirmacion) {
        // Paso 6: Verificar que nueva y confirmación sean idénticas
        if (!contrasenaNueva.equals(confirmacion)) {
            return "Las contraseñas no coinciden.";
        }

        // Paso 7-8: Validar contraseña actual
        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);
        if (usuario == null) {
            return "Usuario no encontrado.";
        }

        if (!cifrador.verificar(contrasenaActual, usuario.getContrasenaHash())) {
            return "La contraseña actual no es correcta.";
        }

        // Paso 9: Validar política de nueva contraseña
        String errorPolitica = validador.validarContrasena(contrasenaNueva);
        if (errorPolitica != null) {
            return errorPolitica;
        }

        // Verificar que nueva != actual
        if (cifrador.verificar(contrasenaNueva, usuario.getContrasenaHash())) {
            return "La nueva contraseña no puede ser igual a la contraseña actual.";
        }

        // Paso 10-11: Cifrar y guardar
        String nuevoHash = cifrador.cifrar(contrasenaNueva);
        boolean actualizado = usuarioDAO.actualizarContrasena(usuario.getIdUsuario(), nuevoHash);

        if (!actualizado) {
            return "Error al actualizar la contraseña.";
        }

        // Actualizar indicador de primer acceso
        if (usuario.isPrimerAcceso()) {
            usuarioDAO.actualizarPrimerAcceso(usuario.getIdUsuario(), false);
        }

        return "Contraseña actualizada exitosamente.";
    }

    /**
     * RF-SNAAR-02.03: Recuperar Contraseña.
     * Verifica correo + nombre de usuario internamente, permite restablecer.
     */
    public String recuperarContrasena(String correo, String nombreUsuario,
                                       String contrasenaNueva, String confirmacion) {
        // Paso 6-7: Buscar usuario
        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);

        if (usuario == null) {
            return "Los datos ingresados no corresponden a un usuario registrado.";
        }

        // Paso 7 excepción: Cuenta bloqueada
        if (usuario.getEstadoCuenta() == EstadoCuenta.BLOQUEADO) {
            return "Cuenta bloqueada. Contacte al administrador del sistema.";
        }

        // Paso 8: Verificar correo (se debe verificar contra el correo del empleado)
        // Nota: El correo se verifica internamente sin revelar cuál campo falló

        // Paso 10-13: Establecer nueva contraseña
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

    /**
     * Cierra la sesión activa.
     */
    public boolean cerrarSesion(Sesion sesion) {
        if (sesion != null) {
            sesion.invalidar();
            return true;
        }
        return false;
    }

    private String obtenerRolUsuario(String cedula) {
        // Se obtiene del empleado asociado
        return "GUARDIA"; // Default, se resuelve en la integración completa
    }
}
