package com.tekmess.snaar.modelo.entidad;

import java.util.Date;

/**
 * Entidad Anotacion del sistema SNAAR.
 * Permite agregar notas a reportes generados (RF-SNAAR-03V2.01 paso 17).
 * Los datos base del reporte NO se modifican; solo se permiten anotaciones.
 */
public class Anotacion {

    private int idAnotacion;
    private int idReporte;
    private String contenido;
    private Date fechaCreacion;
    private String autor;

    // ── Constructores ──────────────────────────────────────────

    public Anotacion() {
        this.fechaCreacion = new Date();
    }

    public Anotacion(int idReporte, String contenido, String autor) {
        this();
        this.idReporte = idReporte;
        this.contenido = contenido;
        this.autor = autor;
    }

    // ── Getters y Setters ──────────────────────────────────────

    public int getIdAnotacion() {
        return idAnotacion;
    }

    public void setIdAnotacion(int idAnotacion) {
        this.idAnotacion = idAnotacion;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
