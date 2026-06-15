package com.tekmess.snaar.patron.command;

import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.controlador.servicio.EmpleadoServicio;

/**
 * Comando Concreto – Editar Empleado (RF-SNAAR-01.02).
 * Patrón Command: encapsula la operación de edición.
 * Guarda los datos anteriores para soportar deshacer.
 */
public class EditarEmpleadoComando implements IComando {

    private final EmpleadoServicio receptor;
    private final Empleado empleadoNuevo;
    private Empleado datosAnteriores;

    public EditarEmpleadoComando(EmpleadoServicio receptor, Empleado empleadoNuevo) {
        this.receptor = receptor;
        this.empleadoNuevo = empleadoNuevo;
    }

    @Override
    public ResultadoComando ejecutar() {
        // Guardar datos anteriores para posible deshacer
        datosAnteriores = receptor.buscarEmpleado(empleadoNuevo.getCedula());
        if (datosAnteriores == null) {
            return new ResultadoComando(false, "El empleado seleccionado no fue encontrado en el sistema.");
        }

        String resultado = receptor.editarEmpleado(empleadoNuevo);
        boolean exito = resultado.contains("actualizado");
        return new ResultadoComando(exito, resultado);
    }

    @Override
    public ResultadoComando deshacer() {
        if (datosAnteriores == null) {
            return new ResultadoComando(false, "No hay datos anteriores para restaurar.");
        }

        String resultado = receptor.editarEmpleado(datosAnteriores);
        boolean exito = resultado.contains("actualizado");
        return new ResultadoComando(exito,
                exito ? "Edición deshecha. Datos restaurados." : "No se pudo deshacer: " + resultado);
    }

    @Override
    public String getDescripcion() {
        return "Editar Empleado: " + empleadoNuevo.getCedula();
    }
}
