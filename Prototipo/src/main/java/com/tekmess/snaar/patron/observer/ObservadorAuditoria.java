package com.tekmess.snaar.patron.observer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Observador Concreto – Auditoría.
 * Patrón Observer: registra TODAS las operaciones en bitácora
 * para trazabilidad del sistema (quién, cuándo, qué operación).
 * Alimenta los datos para RF-SNAAR-03V2 (Reportes Analíticos).
 */
public class ObservadorAuditoria implements IObservador {

    private final List<EventoSistema> registroAuditoria;
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ObservadorAuditoria() {
        this.registroAuditoria = new ArrayList<>();
    }

    @Override
    public void actualizar(EventoSistema evento) {
        registrarEnBitacora(evento);
    }

    /**
     * Registra el evento en la bitácora de auditoría.
     */
    private void registrarEnBitacora(EventoSistema evento) {
        registroAuditoria.add(evento);
        System.out.println("[AUDITORÍA] " + FORMAT.format(evento.getFechaHora())
                + " | Evento: " + evento.getTipoEvento()
                + " | Actor: " + evento.getActor()
                + " | Datos: " + evento.getDatos());
    }

    /**
     * Obtiene el historial completo de auditoría.
     */
    public List<EventoSistema> getRegistros() {
        return new ArrayList<>(registroAuditoria);
    }

    /**
     * Obtiene registros filtrados por tipo de evento.
     */
    public List<EventoSistema> getRegistrosPorTipo(EventoSistema.TipoEvento tipo) {
        List<EventoSistema> filtrados = new ArrayList<>();
        for (EventoSistema evento : registroAuditoria) {
            if (evento.getTipoEvento() == tipo) {
                filtrados.add(evento);
            }
        }
        return filtrados;
    }
}
