package com.tekmess.snaar.patron.observer;

/**
 * Interfaz Observer (Observador) – Patrón Observer.
 * Los observadores concretos implementan esta interfaz para
 * recibir notificaciones de eventos del sistema SNAAR.
 */
public interface IObservador {

    /**
     * Método invocado cuando el sujeto observable notifica un evento.
     * @param evento información del evento ocurrido
     */
    void actualizar(EventoSistema evento);
}
