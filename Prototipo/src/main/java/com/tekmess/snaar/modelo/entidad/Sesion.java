package com.tekmess.snaar.modelo.entidad;

import java.util.Date;
import java.util.UUID;

/**
 * Entidad Sesion del sistema SNAAR.
 * Gestiona la sesión activa con timeout de 120 minutos (RF-SNAAR-02.01).
 */
public class Sesion {

    private String idSesion;
    private int idUsuario;
    private String nombreUsuario;
    private Rol rolUsuario;
    private Date fechaInicio;
    private Date fechaExpiracion;
    private boolean activa;

    private static final long TIMEOUT_MILLIS = 120 * 60 * 1000; // 120 minutos

    // ── Constructores ──────────────────────────────────────────

    public Sesion() {
        this.idSesion = UUID.randomUUID().toString();
        this.fechaInicio = new Date();
        this.fechaExpiracion = new Date(System.currentTimeMillis() + TIMEOUT_MILLIS);
        this.activa = true;
    }

    public Sesion(int idUsuario, String nombreUsuario, Rol rolUsuario) {
        this();
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.rolUsuario = rolUsuario;
    }

    // ── Métodos de negocio ─────────────────────────────────────

    /**
     * Verifica si la sesión ha expirado por inactividad.
     */
    public boolean isExpirada() {
        return new Date().after(fechaExpiracion);
    }

    /**
     * Invalida la sesión (cierre de sesión o expiración).
     */
    public void invalidar() {
        this.activa = false;
    }

    /**
     * Renueva el temporizador de sesión tras actividad del usuario.
     */
    public void renovar() {
        if (this.activa) {
            this.fechaExpiracion = new Date(System.currentTimeMillis() + TIMEOUT_MILLIS);
        }
    }

    // ── Getters y Setters ──────────────────────────────────────

    public String getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Rol getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(Rol rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public boolean isActiva() {
        return activa && !isExpirada();
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
