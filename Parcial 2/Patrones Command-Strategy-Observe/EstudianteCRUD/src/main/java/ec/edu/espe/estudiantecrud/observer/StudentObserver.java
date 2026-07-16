package ec.edu.espe.estudiantecrud.observer;

import ec.edu.espe.estudiantecrud.model.Estudiante;

/**
 * Interfaz para implementar el patrón Observer.
 * Define los métodos que deben implementar los observadores
 * para recibir notificaciones de cambios en estudiantes.
 */
public interface StudentObserver {
    /**
     * Se invoca cuando se agrega un nuevo estudiante.
     * @param estudiante el estudiante agregado
     */
    void onStudentAdded(Estudiante estudiante);

    /**
     * Se invoca cuando se actualiza un estudiante.
     * @param estudiante el estudiante actualizado
     */
    void onStudentUpdated(Estudiante estudiante);

    /**
     * Se invoca cuando se elimina un estudiante.
     * @param id el ID del estudiante eliminado
     */
    void onStudentDeleted(String id);
}
