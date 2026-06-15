package com.tekmess.snaar.modelo.entidad;

import java.util.Date;

/**
 * Entidad Empleado del sistema SNAAR.
 * Representa un empleado registrado en TekMess (RF-SNAAR-01).
 * Atributos: cédula (PK), nombres, correo institucional, rol.
 */
public class Empleado {

    private String cedula;
    private String nombres;
    private String correo;
    private Rol rol;
    private Date fechaCreacion;
    private Date fechaModificacion;

    // ── Constructores ──────────────────────────────────────────

    public Empleado() {
        this.fechaCreacion = new Date();
        this.fechaModificacion = new Date();
    }

    public Empleado(String cedula, String nombres, String correo, Rol rol) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.correo = correo;
        this.rol = rol;
        this.fechaCreacion = new Date();
        this.fechaModificacion = new Date();
    }

    // ── Getters y Setters ──────────────────────────────────────

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
        this.fechaModificacion = new Date();
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
        this.fechaModificacion = new Date();
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
        this.fechaModificacion = new Date();
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    // ── toString ───────────────────────────────────────────────

    @Override
    public String toString() {
        return "Empleado{" +
                "cedula='" + cedula + '\'' +
                ", nombres='" + nombres + '\'' +
                ", correo='" + correo + '\'' +
                ", rol=" + rol +
                '}';
    }
}
