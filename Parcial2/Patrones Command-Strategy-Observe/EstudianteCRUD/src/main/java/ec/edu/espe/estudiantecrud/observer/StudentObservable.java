package ec.edu.espe.estudiantecrud.observer;

import ec.edu.espe.estudiantecrud.model.Estudiante;

/**
 * Interfaz para implementar el Subject/Observable del patrón Observer.
 * Define los métodos para registrar, desregistrar y notificar observadores.
 */
public interface StudentObservable {
    /**
     * Registra un observador para recibir notificaciones.
     * @param observer el observador a registrar
     */
    void attach(StudentObserver observer);

    /**
     * Desregistra un observador de las notificaciones.
     * @param observer el observador a desregistrar
     */
    void detach(StudentObserver observer);

    /**
     * Notifica a todos los observadores que se agregó un estudiante.
     * @param estudiante el estudiante agregado
     */
    void notifyStudentAdded(Estudiante estudiante);

    /**
     * Notifica a todos los observadores que se actualizó un estudiante.
     * @param estudiante el estudiante actualizado
     */
    void notifyStudentUpdated(Estudiante estudiante);

    /**
     * Notifica a todos los observadores que se eliminó un estudiante.
     * @param id el ID del estudiante eliminado
     */
    void notifyStudentDeleted(String id);
}
