package com.tekmess.snaar.modelo.entidad;

import java.util.Date;

/**
 * Entidad Usuario del sistema SNAAR.
 * Representa las credenciales y estado de acceso de un empleado (RF-SNAAR-02).
 * Vinculado 1:1 con Empleado a través de la cédula.
 */
public class Usuario {

    private int idUsuario;
    private String cedula;
    private String nombreUsuario;
    private String contrasenaHash;
    private String contrasenaTemporal;
    private EstadoCuenta estadoCuenta;
    private int intentosFallidos;
    private boolean primerAcceso;
    private Date ultimoAcceso;

    // ── Constructores ──────────────────────────────────────────

    public Usuario() {
        this.estadoCuenta = EstadoCuenta.ACTIVO;
        this.intentosFallidos = 0;
        this.primerAcceso = true;
    }

    public Usuario(String cedula, String nombreUsuario, String contrasenaHash) {
        this.cedula = cedula;
        this.nombreUsuario = nombreUsuario;
        this.contrasenaHash = contrasenaHash;
        this.estadoCuenta = EstadoCuenta.ACTIVO;
        this.intentosFallidos = 0;
        this.primerAcceso = true;
    }

    // ── Getters y Setters ──────────────────────────────────────

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public String getContrasenaTemporal() {
        return contrasenaTemporal;
    }

    public void setContrasenaTemporal(String contrasenaTemporal) {
        this.contrasenaTemporal = contrasenaTemporal;
    }

    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(EstadoCuenta estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    /**
     * Incrementa el contador de intentos fallidos.
     * Si alcanza 3, bloquea la cuenta automáticamente (RF-SNAAR-02.01).
     */
    public void incrementarIntentos() {
        this.intentosFallidos++;
        if (this.intentosFallidos >= 3) {
            this.estadoCuenta = EstadoCuenta.BLOQUEADO;
        }
    }

    /**
     * Reinicia el contador de intentos tras un login exitoso.
     */
    public void reiniciarIntentos() {
        this.intentosFallidos = 0;
    }

    public boolean isPrimerAcceso() {
        return primerAcceso;
    }

    public void setPrimerAcceso(boolean primerAcceso) {
        this.primerAcceso = primerAcceso;
    }

    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", estadoCuenta=" + estadoCuenta +
                ", primerAcceso=" + primerAcceso +
                '}';
    }

    // Compatibility getters for older JSPs/views that expect different names
    public String getUsuario() {
        return this.nombreUsuario;
    }

    public String getNombre() {
        return this.nombreUsuario;
    }

    public com.tekmess.snaar.modelo.entidad.Rol getRol() {
        // Usuario does not store Rol; return null by default.
        return null;
    }
}
