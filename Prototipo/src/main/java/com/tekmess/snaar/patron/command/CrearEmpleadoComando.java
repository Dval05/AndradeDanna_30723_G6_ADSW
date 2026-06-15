package com.tekmess.snaar.patron.command;

import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.controlador.servicio.EmpleadoServicio;

/**
 * Comando Concreto – Crear Empleado (RF-SNAAR-01.01).
 * Patrón Command: encapsula la operación de creación de empleado.
 * Soporta deshacer (elimina el empleado recién creado).
 */
public class CrearEmpleadoComando implements IComando {

    private final EmpleadoServicio receptor;
    private final Empleado empleado;
    private String credencialesGeneradas;

    public CrearEmpleadoComando(EmpleadoServicio receptor, Empleado empleado) {
        this.receptor = receptor;
        this.empleado = empleado;
    }

    @Override
    public ResultadoComando ejecutar() {
        String resultado = receptor.crearEmpleado(empleado);
        boolean exito = resultado.startsWith("Empleado registrado");

        if (exito) {
            this.credencialesGeneradas = resultado;
        }

        return new ResultadoComando(exito, resultado);
    }

    @Override
    public ResultadoComando deshacer() {
        String resultado = receptor.eliminarEmpleado(empleado.getCedula());
        boolean exito = resultado.contains("eliminado");
        return new ResultadoComando(exito,
                exito ? "Creación de empleado deshecha." : "No se pudo deshacer: " + resultado);
    }

    @Override
    public String getDescripcion() {
        return "Crear Empleado: " + empleado.getNombres() + " (" + empleado.getCedula() + ")";
    }

    public String getCredencialesGeneradas() {
        return credencialesGeneradas;
    }
}
