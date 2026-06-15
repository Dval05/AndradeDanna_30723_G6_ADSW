package com.tekmess.snaar.patron.observer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Evento del sistema SNAAR.
 * Encapsula la información de un evento que será notificado
 * a los observadores registrados (Patrón Observer).
 */
public class EventoSistema {

    /**
     * Tipos de evento del sistema SNAAR.
     */
    public enum TipoEvento {
        EMPLEADO_CREADO,
        EMPLEADO_EDITADO,
        EMPLEADO_ELIMINADO,
        LOGIN_EXITOSO,
        LOGIN_FALLIDO,
        CUENTA_BLOQUEADA,
        CONTRASENA_CAMBIADA,
        REPORTE_GENERADO
    }

    private final TipoEvento tipoEvento;
    private final Map<String, Object> datos;
    private final Date fechaHora;
    private final String actor;

    public EventoSistema(TipoEvento tipoEvento, String actor) {
        this.tipoEvento = tipoEvento;
        this.actor = actor;
        this.fechaHora = new Date();
        this.datos = new HashMap<>();
    }

    public EventoSistema(TipoEvento tipoEvento, String actor, Map<String, Object> datos) {
        this.tipoEvento = tipoEvento;
        this.actor = actor;
        this.fechaHora = new Date();
        this.datos = datos != null ? datos : new HashMap<>();
    }

    // ── Getters ────────────────────────────────────────────────

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public Map<String, Object> getDatos() {
        return datos;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public String getActor() {
        return actor;
    }

    public void agregarDato(String clave, Object valor) {
        this.datos.put(clave, valor);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s por %s - datos: %s",
                fechaHora, tipoEvento, actor, datos);
    }
}
