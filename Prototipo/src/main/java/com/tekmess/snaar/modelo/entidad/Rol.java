package com.tekmess.snaar.modelo.entidad;

/**
 * Enumeración de roles del sistema SNAAR.
 * Define los roles válidos según RF-SNAAR-01.05.
 */
public enum Rol {
    GUARDIA("Guardia"),
    SUPERVISOR("Supervisor"),
    CENTRALISTA("Centralista"),
    JEFE_LOGISTICA("Jefe Logística/Operación");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene el Rol a partir de su descripción.
     * @param descripcion texto del rol
     * @return Rol correspondiente
     * @throws IllegalArgumentException si no se encuentra el rol
     */
    public static Rol fromDescripcion(String descripcion) {
        for (Rol rol : values()) {
            if (rol.descripcion.equalsIgnoreCase(descripcion)) {
                return rol;
            }
        }
        throw new IllegalArgumentException("El rol seleccionado no es válido: " + descripcion);
    }
}
