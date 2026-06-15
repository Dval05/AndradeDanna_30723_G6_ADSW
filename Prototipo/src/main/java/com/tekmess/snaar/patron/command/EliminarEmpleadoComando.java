package com.tekmess.snaar.patron.command;

import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.controlador.servicio.EmpleadoServicio;

/**
 * Comando Concreto – Eliminar Empleado (RF-SNAAR-01.03).
 * Patrón Command: encapsula la eliminación con respaldo.
 * Guarda copia del empleado antes de eliminar para soportar deshacer.
 */
public class EliminarEmpleadoComando implements IComando {

    private final EmpleadoServicio receptor;
    private final String cedula;
    private Empleado empleadoRespaldo;

    public EliminarEmpleadoComando(EmpleadoServicio receptor, String cedula) {
        this.receptor = receptor;
        this.cedula = cedula;
    }

    @Override
    public ResultadoComando ejecutar() {
        // Respaldar empleado antes de eliminar
        empleadoRespaldo = receptor.buscarEmpleado(cedula);
        if (empleadoRespaldo == null) {
            return new ResultadoComando(false,
                    "El empleado seleccionado no fue encontrado en el sistema.");
        }

        String resultado = receptor.eliminarEmpleado(cedula);
        boolean exito = resultado.contains("eliminado");
        return new ResultadoComando(exito, resultado);
    }

    @Override
    public ResultadoComando deshacer() {
        if (empleadoRespaldo == null) {
            return new ResultadoComando(false, "No hay respaldo para restaurar.");
        }

        String resultado = receptor.crearEmpleado(empleadoRespaldo);
        boolean exito = resultado.startsWith("Empleado registrado");
        return new ResultadoComando(exito,
                exito ? "Eliminación deshecha. Empleado restaurado." : "No se pudo restaurar: " + resultado);
    }

    @Override
    public String getDescripcion() {
        return "Eliminar Empleado: " + cedula;
    }
}
