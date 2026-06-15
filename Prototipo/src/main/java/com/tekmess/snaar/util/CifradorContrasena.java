package com.tekmess.snaar.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Cifrador de contraseñas usando BCrypt.
 * Almacenamiento seguro según RF-SNAAR-02.01 comentarios.
 */
public class CifradorContrasena {

    private static final int ROUNDS = 12;

    /**
     * Cifra una contraseña en texto plano con BCrypt.
     * @param contrasena texto plano
     * @return hash BCrypt
     */
    public String cifrar(String contrasena) {
        return BCrypt.hashpw(contrasena, BCrypt.gensalt(ROUNDS));
    }

    /**
     * Verifica si una contraseña en texto plano corresponde al hash almacenado.
     * @param plano contraseña en texto plano
     * @param hash hash almacenado
     * @return true si coinciden
     */
    public boolean verificar(String plano, String hash) {
        try {
            return BCrypt.checkpw(plano, hash);
        } catch (Exception e) {
            return false;
        }
    }
}
