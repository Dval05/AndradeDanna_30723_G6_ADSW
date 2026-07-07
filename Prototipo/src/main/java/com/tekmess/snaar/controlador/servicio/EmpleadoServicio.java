package com.tekmess.snaar.controlador.servicio;

import com.tekmess.snaar.modelo.dao.IEmpleadoDAO;
import com.tekmess.snaar.modelo.dao.IUsuarioDAO;
import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.modelo.entidad.EstadoCuenta;
import com.tekmess.snaar.modelo.entidad.Usuario;
import com.tekmess.snaar.util.CifradorContrasena;
import com.tekmess.snaar.util.GeneradorCredenciales;
import com.tekmess.snaar.util.ValidadorDatos;

import java.util.List;

/**
 * Servicio de Gestión de Empleados (RF-SNAAR-01).
 * Capa de Lógica de Negocio – Receiver en el Patrón Command.
 * Contiene la lógica CRUD para empleados, validación y generación de credenciales.
 */
public class EmpleadoServicio {

    private final IEmpleadoDAO empleadoDAO;
    private final IUsuarioDAO usuarioDAO;
    private final ValidadorDatos validador;
    private final GeneradorCredenciales generadorCredenciales;
    private final CifradorContrasena cifrador;

    public EmpleadoServicio(IEmpleadoDAO empleadoDAO, IUsuarioDAO usuarioDAO) {
        this.empleadoDAO = empleadoDAO;
        this.usuarioDAO = usuarioDAO;
        this.validador = new ValidadorDatos();
        this.generadorCredenciales = new GeneradorCredenciales();
        this.cifrador = new CifradorContrasena();
    }

    /**
     * RF-SNAAR-01.01: Crear Empleado.
     * Valida datos, verifica cédula no duplicada, crea registro y genera credenciales.
     *
     * @param empleado datos del nuevo empleado
     * @return mensaje de resultado con credenciales generadas
     */
    public String crearEmpleado(Empleado empleado) {
        normalizarEmpleado(empleado);

        // Paso 9: Ejecuta RF-SNAAR-01.05 (Validar Datos)
        List<String> errores = validador.validarDatosEmpleado(empleado);
        if (!errores.isEmpty()) {
            return errores.get(0); // Retorna el primer error
        }

        // Paso 11: Verifica cédula no duplicada
        if (empleadoDAO.existeCedula(empleado.getCedula())) {
            return "La cédula ingresada ya se encuentra registrada en el sistema.";
        }

        // Paso 13: Crea el registro del empleado
        boolean creado = empleadoDAO.crear(empleado);
        if (!creado) {
            return "Error al registrar el empleado en la base de datos.";
        }

        // Paso 15-16: Ejecuta RF-SNAAR-01.06 (Generar Credenciales)
        String nombreUsuario = generadorCredenciales.generarNombreUsuario(empleado);

        // Verificar unicidad del nombre de usuario (excepción 2 de RF-SNAAR-01.06)
        int sufijo = 1;
        String nombreBase = nombreUsuario;
        while (usuarioDAO.existeNombreUsuario(nombreUsuario)) {
            nombreUsuario = generadorCredenciales.aplicarSufijo(nombreBase, sufijo++);
        }

        String contrasenaInicial = generadorCredenciales.generarContrasenaInicial();
        String contrasenaHash = cifrador.cifrar(contrasenaInicial);

        // Crear usuario con primer acceso obligatorio
        Usuario usuario = new Usuario(empleado.getCedula(), nombreUsuario, contrasenaHash);
        usuario.setContrasenaTemporal(contrasenaInicial);
        usuario.setPrimerAcceso(true);
        usuarioDAO.crear(usuario);

        return String.format("Empleado registrado exitosamente.\nUsuario: %s\nContraseña: %s",
                nombreUsuario, contrasenaInicial);
    }

    /**
     * RF-SNAAR-01.02: Editar Empleado.
     * Valida nuevos datos, localiza por cédula y actualiza.
     */
    public String editarEmpleado(Empleado empleado) {
        normalizarEmpleado(empleado);

        // Paso 6: Validar datos
        List<String> errores = validador.validarDatosEmpleado(empleado);
        if (!errores.isEmpty()) {
            return errores.get(0);
        }

        // Paso 8: Buscar por cédula
        Empleado existente = empleadoDAO.buscarPorCedula(empleado.getCedula());
        if (existente == null) {
            return "El empleado seleccionado no fue encontrado en el sistema.";
        }

        // Paso 10-11: Actualizar y guardar
        boolean actualizado = empleadoDAO.editar(empleado);
        if (!actualizado) {
            return "Error al actualizar los datos del empleado.";
        }

        return "Datos del empleado actualizados exitosamente.";
    }

    /**
     * RF-SNAAR-01.03: Eliminar Empleado.
     * Localiza por cédula y elimina permanentemente.
     */
    public String eliminarEmpleado(String cedula) {
        // Paso 7: Buscar registro
        Empleado empleado = empleadoDAO.buscarPorCedula(cedula);
        if (empleado == null) {
            return "El empleado seleccionado no fue encontrado en el sistema.";
        }

        // Eliminar usuario asociado
        usuarioDAO.eliminarPorCedula(cedula);

        // Paso 9: Eliminar empleado
        boolean eliminado = empleadoDAO.eliminar(cedula);
        if (!eliminado) {
            return "Error al eliminar el empleado del sistema.";
        }

        return "Empleado eliminado del sistema exitosamente.";
    }

    /**
     * RF-SNAAR-01.04: Consultar Empleados.
     * Retorna listado completo del personal.
     */
    public List<Empleado> consultarEmpleados() {
        return empleadoDAO.listarTodos();
    }

    /**
     * Busca un empleado por cédula.
     */
    public Empleado buscarEmpleado(String cedula) {
        return empleadoDAO.buscarPorCedula(cedula);
    }

    public String regenerarContrasenaTemporal(String cedula) {
        String errorCedula = validador.validarCedula(cedula);
        if (errorCedula != null) {
            return errorCedula;
        }

        Empleado empleado = empleadoDAO.buscarPorCedula(cedula.trim());
        if (empleado == null) {
            return "El empleado seleccionado no fue encontrado en el sistema.";
        }

        Usuario usuario = usuarioDAO.buscarPorCedula(cedula.trim());
        if (usuario == null) {
            return "El empleado no tiene una cuenta de usuario asociada.";
        }

        String temporal = generadorCredenciales.generarContrasenaInicial();
        String hash = cifrador.cifrar(temporal);
        boolean actualizado = usuarioDAO.actualizarCredencialesTemporales(usuario.getIdUsuario(), hash, temporal);

        if (!actualizado) {
            return "No se pudo regenerar la contraseña temporal.";
        }

        return "Contraseña temporal regenerada para " + usuario.getNombreUsuario() + ".";
    }

    public String desbloquearCuenta(String cedula) {
        String errorCedula = validador.validarCedula(cedula);
        if (errorCedula != null) {
            return errorCedula;
        }

        Usuario usuario = usuarioDAO.buscarPorCedula(cedula.trim());
        if (usuario == null) {
            return "El empleado no tiene una cuenta de usuario asociada.";
        }

        boolean estadoOk = usuarioDAO.actualizarEstado(usuario.getIdUsuario(), EstadoCuenta.ACTIVO);
        boolean intentosOk = usuarioDAO.actualizarIntentos(usuario.getIdUsuario(), 0);

        return estadoOk && intentosOk
                ? "Cuenta desbloqueada para " + usuario.getNombreUsuario() + "."
                : "No se pudo desbloquear la cuenta.";
    }

    private void normalizarEmpleado(Empleado empleado) {
        if (empleado == null) {
            return;
        }
        empleado.setCedula(empleado.getCedula() == null ? null : empleado.getCedula().trim());
        empleado.setNombres(validador.normalizarEspacios(empleado.getNombres()));
        empleado.setCorreo(empleado.getCorreo() == null ? null : empleado.getCorreo().trim().toLowerCase());
    }
}
