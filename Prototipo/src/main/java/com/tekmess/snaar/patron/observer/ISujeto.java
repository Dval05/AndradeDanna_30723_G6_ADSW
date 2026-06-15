package com.tekmess.snaar.patron.observer;

/**
 * Interfaz Subject (Sujeto Observable) – Patrón Observer.
 * Los sujetos concretos implementan esta interfaz para gestionar
 * la lista de observadores y notificarles los eventos.
 */
public interface ISujeto {

    /**
     * Registra un observador para recibir notificaciones.
     * @param observador observador a registrar
     */
    void agregarObservador(IObservador observador);

    /**
     * Elimina un observador del listado de notificaciones.
     * @param observador observador a eliminar
     */
    void eliminarObservador(IObservador observador);

    /**
     * Notifica a todos los observadores registrados sobre un evento.
     * @param evento información del evento ocurrido
     */
    void notificarObservadores(EventoSistema evento);
}
