package com.tekmess.snaar.util;

import com.tekmess.snaar.modelo.entidad.Empleado;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validador de datos del empleado (RF-SNAAR-01.05).
 * Proceso interno reutilizado por RF-SNAAR-01.01 (Crear) y RF-SNAAR-01.02 (Editar).
 *
 * Reglas de validación:
 * - Cédula: exactamente 10 dígitos numéricos
 * - Rol: debe ser uno de los roles válidos del sistema
 * - Correo: formato institucional válido
 */
public class ValidadorDatos {

    private static final Pattern PATRON_CEDULA = Pattern.compile("^\\d{10}$");
    private static final Pattern PATRON_CORREO = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    private static final Pattern PATRON_CONTRASENA = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d.*\\d)(?=.*[!@#$%^&*()_+\\-={}|;:',.<>?/]).{8,}$"
    );

    /**
     * Valida todos los datos del empleado.
     * Retorna lista vacía si todos los datos son válidos.
     * @param empleado datos a validar
     * @return lista de mensajes de error (vacía = válido)
     */
    public List<String> validarDatosEmpleado(Empleado empleado) {
        List<String> errores = new ArrayList<>();

        String errorCedula = validarCedula(empleado.getCedula());
        if (errorCedula != null) errores.add(errorCedula);

        String errorRol = validarRol(empleado.getRol() != null ? empleado.getRol().name() : null);
        if (errorRol != null) errores.add(errorRol);

        String errorCorreo = validarCorreo(empleado.getCorreo());
        if (errorCorreo != null) errores.add(errorCorreo);

        return errores;
    }

    /**
     * Valida que la cédula tenga exactamente 10 dígitos numéricos.
     * @return mensaje de error o null si es válida
     */
    public String validarCedula(String cedula) {
        if (cedula == null || !PATRON_CEDULA.matcher(cedula).matches()) {
            return "La cédula debe contener exactamente 10 dígitos numéricos.";
        }
        return null;
    }

    /**
     * Valida que el rol sea uno de los roles válidos del sistema.
     * @return mensaje de error o null si es válido
     */
    public String validarRol(String rol) {
        if (rol == null || rol.isEmpty()) {
            return "El rol seleccionado no es válido.";
        }
        try {
            com.tekmess.snaar.modelo.entidad.Rol.valueOf(rol);
            return null;
        } catch (IllegalArgumentException e) {
            return "El rol seleccionado no es válido.";
        }
    }

    /**
     * Valida que el correo tenga un formato institucional válido.
     * @return mensaje de error o null si es válido
     */
    public String validarCorreo(String correo) {
        if (correo == null || !PATRON_CORREO.matcher(correo).matches()) {
            return "El correo ingresado no tiene un formato válido.";
        }
        return null;
    }

    /**
     * Valida la política de contraseñas (RF-SNAAR-02.02):
     * - Mínimo 8 caracteres
     * - Al menos 1 mayúscula, 1 minúscula, 2 números, 1 carácter especial
     * @return mensaje de error o null si cumple la política
     */
    public String validarContrasena(String contrasena) {
        if (contrasena == null || !PATRON_CONTRASENA.matcher(contrasena).matches()) {
            return "La contraseña debe tener mínimo 8 caracteres, 1 mayúscula, 1 minúscula, 2 números y 1 carácter especial.";
        }
        return null;
    }
}
