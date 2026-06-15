package com.tekmess.snaar.patron.observer;

import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.patron.observer.EventoSistema.TipoEvento;
import com.tekmess.snaar.controlador.servicio.EmpleadoServicio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sujeto Concreto – Gestión de Empleados (RF-01).
 * Patrón Observer: notifica a los observadores cuando se
 * crea, edita o elimina un empleado en el sistema SNAAR.
 */
public class SujetoGestionEmpleado implements ISujeto {

    private final List<IObservador> observadores;
    private final EmpleadoServicio empleadoServicio;

    public SujetoGestionEmpleado(EmpleadoServicio empleadoServicio) {
        this.observadores = new ArrayList<>();
        this.empleadoServicio = empleadoServicio;
    }

    @Override
    public void agregarObservador(IObservador observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void eliminarObservador(IObservador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores(EventoSistema evento) {
        for (IObservador observador : observadores) {
            observador.actualizar(evento);
        }
    }

    // ── Operaciones que generan eventos ────────────────────────

    /**
     * Crea un empleado y notifica el evento EMPLEADO_CREADO.
     */
    public String crearEmpleado(Empleado empleado, String actor) {
        String resultado = empleadoServicio.crearEmpleado(empleado);

        if (resultado.startsWith("Empleado registrado")) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("cedula", empleado.getCedula());
            datos.put("nombres", empleado.getNombres());
            datos.put("rol", empleado.getRol().getDescripcion());

            EventoSistema evento = new EventoSistema(
                    TipoEvento.EMPLEADO_CREADO, actor, datos);
            notificarObservadores(evento);
        }

        return resultado;
    }

    /**
     * Edita un empleado y notifica el evento EMPLEADO_EDITADO.
     */
    public String editarEmpleado(Empleado empleado, String actor) {
        String resultado = empleadoServicio.editarEmpleado(empleado);

        if (resultado.contains("actualizado")) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("cedula", empleado.getCedula());
            datos.put("nombres", empleado.getNombres());

            EventoSistema evento = new EventoSistema(
                    TipoEvento.EMPLEADO_EDITADO, actor, datos);
            notificarObservadores(evento);
        }

        return resultado;
    }

    /**
     * Elimina un empleado y notifica el evento EMPLEADO_ELIMINADO.
     */
    public String eliminarEmpleado(String cedula, String actor) {
        String resultado = empleadoServicio.eliminarEmpleado(cedula);

        if (resultado.contains("eliminado")) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("cedula", cedula);

            EventoSistema evento = new EventoSistema(
                    TipoEvento.EMPLEADO_ELIMINADO, actor, datos);
            notificarObservadores(evento);
        }

        return resultado;
    }
}
