package com.tekmess.snaar.patron.observer;

/**
 * Observador Concreto – Notificación.
 * Patrón Observer: envía alertas al Jefe Logística ante eventos
 * críticos como bloqueo de cuenta, eliminación de empleado, etc.
 */
public class ObservadorNotificacion implements IObservador {

    @Override
    public void actualizar(EventoSistema evento) {
        switch (evento.getTipoEvento()) {
            case CUENTA_BLOQUEADA:
                notificarBloqueo(evento);
                break;
            case EMPLEADO_ELIMINADO:
                enviarAlertaJefe(evento);
                break;
            case LOGIN_FALLIDO:
                System.out.println("[NOTIFICACIÓN] Intento de acceso fallido registrado - "
                        + "Usuario: " + evento.getDatos().getOrDefault("usuario", "desconocido"));
                break;
            case EMPLEADO_CREADO:
                System.out.println("[NOTIFICACIÓN] Nuevo empleado registrado: "
                        + evento.getDatos().getOrDefault("nombres", ""));
                break;
            default:
                System.out.println("[NOTIFICACIÓN] Evento registrado: " + evento.getTipoEvento());
                break;
        }
    }

    private void enviarAlertaJefe(EventoSistema evento) {
        System.out.println("[ALERTA - JEFE LOGÍSTICA] Empleado eliminado del sistema. "
                + "Cédula: " + evento.getDatos().getOrDefault("cedula", "N/A")
                + " | Por: " + evento.getActor());
    }

    private void enviarAlertaSupervisor(EventoSistema evento) {
        System.out.println("[ALERTA - SUPERVISOR] Evento crítico: " + evento.getTipoEvento()
                + " | " + evento.getDatos());
    }

    private void notificarBloqueo(EventoSistema evento) {
        System.out.println("[ALERTA CRÍTICA] Cuenta bloqueada por intentos fallidos. "
                + "Usuario: " + evento.getDatos().getOrDefault("usuario", "N/A")
                + " | Contacte al administrador.");
    }
}
