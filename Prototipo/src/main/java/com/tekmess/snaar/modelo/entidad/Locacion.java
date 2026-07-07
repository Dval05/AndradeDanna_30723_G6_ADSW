package com.tekmess.snaar.modelo.entidad;

public class Locacion {
    private int idLocacion;
    private String nombre;
    private String ciudad;
    private String direccion;
    private String responsable;
    private int capacidad;
    private boolean activa;

    public int getIdLocacion() { return idLocacion; }
    public void setIdLocacion(int idLocacion) { this.idLocacion = idLocacion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
