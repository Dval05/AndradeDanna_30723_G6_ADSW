package com.tekmess.snaar.util;

import com.tekmess.snaar.modelo.entidad.Empleado;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validador reutilizable para entradas del sistema SNAAR.
 */
public class ValidadorDatos {

    private static final Pattern PATRON_CEDULA = Pattern.compile("^\\d{10}$");
    private static final Pattern PATRON_NOMBRES = Pattern.compile("^[\\p{L}ÁÉÍÓÚÜÑáéíóúüñ'´`.-]+(?:\\s+[\\p{L}ÁÉÍÓÚÜÑáéíóúüñ'´`.-]+)+$");
    private static final Pattern PATRON_USUARIO = Pattern.compile("^[a-zA-Z0-9._-]{3,30}$");
    private static final Pattern PATRON_CORREO = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    private static final Pattern PATRON_CONTRASENA = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d.*\\d)(?=.*[!@#$%^&*()_+\\-={}|;:',.<>?/]).{8,}$"
    );

    public List<String> validarDatosEmpleado(Empleado empleado) {
        List<String> errores = new ArrayList<>();

        if (empleado == null) {
            errores.add("Los datos del empleado son obligatorios.");
            return errores;
        }

        String errorCedula = validarCedula(empleado.getCedula());
        if (errorCedula != null) errores.add(errorCedula);

        String errorNombres = validarNombres(empleado.getNombres());
        if (errorNombres != null) errores.add(errorNombres);

        String errorRol = validarRol(empleado.getRol() != null ? empleado.getRol().name() : null);
        if (errorRol != null) errores.add(errorRol);

        String errorCorreo = validarCorreo(empleado.getCorreo());
        if (errorCorreo != null) errores.add(errorCorreo);

        return errores;
    }

    public String validarCedula(String cedula) {
        if (cedula == null || !PATRON_CEDULA.matcher(cedula.trim()).matches()) {
            return "La cédula debe contener exactamente 10 dígitos numéricos.";
        }
        return null;
    }

    public String validarNombres(String nombres) {
        if (nombres == null || nombres.trim().isEmpty()) {
            return "Los nombres completos son obligatorios.";
        }
        String valor = normalizarEspacios(nombres);
        if (valor.length() < 7 || valor.length() > 80) {
            return "Los nombres completos deben tener entre 7 y 80 caracteres.";
        }
        if (!PATRON_NOMBRES.matcher(valor).matches()) {
            return "Ingrese nombre y apellido usando solo letras y espacios.";
        }
        return null;
    }

    public String validarRol(String rol) {
        if (rol == null || rol.isBlank()) {
            return "El rol seleccionado no es válido.";
        }
        try {
            com.tekmess.snaar.modelo.entidad.Rol.valueOf(rol.trim());
            return null;
        } catch (IllegalArgumentException e) {
            return "El rol seleccionado no es válido.";
        }
    }

    public String validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return "El correo institucional es obligatorio.";
        }
        String valor = correo.trim();
        if (valor.length() > 120 || !PATRON_CORREO.matcher(valor).matches()) {
            return "El correo ingresado no tiene un formato válido.";
        }
        return null;
    }

    public String validarUsuario(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return "El usuario es obligatorio.";
        }
        if (!PATRON_USUARIO.matcher(usuario.trim()).matches()) {
            return "El usuario debe tener de 3 a 30 caracteres y solo puede usar letras, números, punto, guion o guion bajo.";
        }
        return null;
    }

    public String validarTexto(String valor, String campo, int minimo, int maximo) {
        if (valor == null || valor.trim().isEmpty()) {
            return campo + " es obligatorio.";
        }
        String texto = normalizarEspacios(valor);
        if (texto.length() < minimo || texto.length() > maximo) {
            return campo + " debe tener entre " + minimo + " y " + maximo + " caracteres.";
        }
        return null;
    }

    public String validarContrasena(String contrasena) {
        if (contrasena == null || !PATRON_CONTRASENA.matcher(contrasena).matches()) {
            return "La contraseña debe tener mínimo 8 caracteres, 1 mayúscula, 1 minúscula, 2 números y 1 carácter especial.";
        }
        return null;
    }

    public String normalizarEspacios(String valor) {
        return valor == null ? null : valor.trim().replaceAll("\\s+", " ");
    }
}
