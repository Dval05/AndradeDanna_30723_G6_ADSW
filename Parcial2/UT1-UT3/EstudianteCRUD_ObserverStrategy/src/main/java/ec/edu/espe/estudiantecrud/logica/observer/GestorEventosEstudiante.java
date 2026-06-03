package ec.edu.espe.estudiantecrud.logica.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject (Sujeto) del patrón Observer.
 * Gestiona la lista de observadores y los notifica cuando ocurre
 * un evento relevante en el CRUD del estudiante (crear, actualizar, eliminar).
 */
public class GestorEventosEstudiante {

    private final List<ObservadorEstudiante> observadores = new ArrayList<>();

    /**
     * Registra un nuevo observador para recibir notificaciones.
     *
     * @param observador observador a agregar
     */
    public void agregarObservador(ObservadorEstudiante observador) {
        observadores.add(observador);
    }

    /**
     * Elimina un observador registrado.
     *
     * @param observador observador a remover
     */
    public void removerObservador(ObservadorEstudiante observador) {
        observadores.remove(observador);
    }

    /**
     * Notifica a todos los observadores registrados sobre un evento.
     *
     * @param tipoEvento tipo de evento: CREAR, ACTUALIZAR, ELIMINAR
     * @param detalle    descripción del evento ocurrido
     */
    public void notificar(String tipoEvento, String detalle) {
        for (ObservadorEstudiante obs : observadores) {
            obs.actualizar(tipoEvento, detalle);
        }
    }

    /**
     * Retorna la cantidad de observadores registrados.
     *
     * @return número de observadores
     */
    public int cantidadObservadores() {
        return observadores.size();
    }
}
