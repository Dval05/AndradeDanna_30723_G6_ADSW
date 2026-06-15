package com.tekmess.snaar.modelo.entidad;

/**
 * Enumeración de estados de cuenta del sistema SNAAR.
 * Controla el acceso según RF-SNAAR-02.04.
 */
public enum EstadoCuenta {
    ACTIVO("Activo"),
    BLOQUEADO("Bloqueado"),
    INACTIVO("Inactivo");

    private final String descripcion;

    EstadoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
