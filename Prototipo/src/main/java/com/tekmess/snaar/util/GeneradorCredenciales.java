package com.tekmess.snaar.util;

import com.tekmess.snaar.modelo.entidad.Empleado;
import java.security.SecureRandom;

/**
 * Generador de credenciales de acceso (RF-SNAAR-01.06).
 * Proceso interno que genera nombre de usuario y contraseña inicial
 * para empleados recién registrados.
 */
public class GeneradorCredenciales {

    private static final String CARACTERES = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789!@#$";
    private static final SecureRandom random = new SecureRandom();

    /**
     * Genera nombre de usuario basado en los datos del empleado.
     * Formato: primera letra del nombre + apellido (en minúsculas).
     * Si ya existe, aplica sufijo numérico (RF-SNAAR-01.06 excepción 2).
     *
     * @param empleado datos del empleado
     * @return nombre de usuario generado
     */
    public String generarNombreUsuario(Empleado empleado) {
        String[] partes = empleado.getNombres().trim().split("\\s+");
        String usuario;

        if (partes.length >= 2) {
            // Primera letra del primer nombre + apellido
            usuario = (partes[0].charAt(0) + partes[partes.length - 1]).toLowerCase();
        } else {
            usuario = partes[0].toLowerCase();
        }

        // Limpiar caracteres especiales
        usuario = usuario.replaceAll("[^a-z0-9]", "");

        return usuario;
    }

    /**
     * Aplica sufijo numérico si el nombre de usuario ya existe.
     *
     * @param baseUsuario nombre de usuario base
     * @param sufijo número de sufijo
     * @return nombre de usuario con sufijo
     */
    public String aplicarSufijo(String baseUsuario, int sufijo) {
        return baseUsuario + sufijo;
    }

    /**
     * Genera contraseña inicial temporal de 10 caracteres.
     * Cumple política: 1 mayúscula, 1 minúscula, 2 números, 1 especial.
     *
     * @return contraseña generada
     */
    public String generarContrasenaInicial() {
        StringBuilder contrasena = new StringBuilder();

        // Garantizar cumplimiento de la política
        contrasena.append((char) ('A' + random.nextInt(26)));     // 1 mayúscula
        contrasena.append((char) ('a' + random.nextInt(26)));     // 1 minúscula
        contrasena.append(random.nextInt(10));                     // 1er número
        contrasena.append(random.nextInt(10));                     // 2do número
        contrasena.append("!@#$%".charAt(random.nextInt(5)));     // 1 especial

        // Completar hasta 10 caracteres
        for (int i = 5; i < 10; i++) {
            contrasena.append(CARACTERES.charAt(random.nextInt(CARACTERES.length())));
        }

        // Mezclar los caracteres
        char[] chars = contrasena.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }
}
