package com.tekmess.snaar.modelo.entidad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entidad Reporte del sistema SNAAR.
 * Representa un reporte analítico generado (RF-SNAAR-03V2.01).
 * Consolida datos de RF-01 (empleados) y RF-02 (accesos fallidos).
 */
public class Reporte {

    private int idReporte;
    private Date fechaGeneracion;
    private Date fechaInicio;
    private Date fechaFin;
    private int totalEmpleadosCreados;
    private int totalEmpleadosEditados;
    private int totalEmpleadosEliminados;
    private int totalAccesosFallidos;
    private String generadoPor;
    private List<Anotacion> anotaciones;

    // ── Constructores ──────────────────────────────────────────

    public Reporte() {
        this.fechaGeneracion = new Date();
        this.anotaciones = new ArrayList<>();
    }

    public Reporte(Date fechaInicio, Date fechaFin, String generadoPor) {
        this();
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.generadoPor = generadoPor;
    }

    // ── Getters y Setters ──────────────────────────────────────

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getTotalEmpleadosCreados() {
        return totalEmpleadosCreados;
    }

    public void setTotalEmpleadosCreados(int totalEmpleadosCreados) {
        this.totalEmpleadosCreados = totalEmpleadosCreados;
    }

    public int getTotalEmpleadosEditados() {
        return totalEmpleadosEditados;
    }

    public void setTotalEmpleadosEditados(int totalEmpleadosEditados) {
        this.totalEmpleadosEditados = totalEmpleadosEditados;
    }

    public int getTotalEmpleadosEliminados() {
        return totalEmpleadosEliminados;
    }

    public void setTotalEmpleadosEliminados(int totalEmpleadosEliminados) {
        this.totalEmpleadosEliminados = totalEmpleadosEliminados;
    }

    public int getTotalAccesosFallidos() {
        return totalAccesosFallidos;
    }

    public void setTotalAccesosFallidos(int totalAccesosFallidos) {
        this.totalAccesosFallidos = totalAccesosFallidos;
    }

    public String getGeneradoPor() {
        return generadoPor;
    }

    public void setGeneradoPor(String generadoPor) {
        this.generadoPor = generadoPor;
    }

    public List<Anotacion> getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(List<Anotacion> anotaciones) {
        this.anotaciones = anotaciones;
    }

    public void agregarAnotacion(Anotacion anotacion) {
        this.anotaciones.add(anotacion);
    }

    /**
     * Verifica si el reporte tiene datos (RF-SNAAR-03V2.01 excepción 12).
     */
    public boolean tieneDatos() {
        return totalEmpleadosCreados > 0 ||
               totalEmpleadosEditados > 0 ||
               totalEmpleadosEliminados > 0 ||
               totalAccesosFallidos > 0;
    }
}
