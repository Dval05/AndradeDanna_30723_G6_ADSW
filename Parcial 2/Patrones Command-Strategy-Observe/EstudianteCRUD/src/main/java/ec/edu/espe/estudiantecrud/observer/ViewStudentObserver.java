package ec.edu.espe.estudiantecrud.observer;

//import ec.edu.espe.estudiantecrud.model.Estudiante;

/**
 * Observer para la interfaz de usuario.
 * Actualiza la vista cuando hay cambios en los estudiantes.
 * Esta clase se implementará en la vista para actualizar dinámicamente la tabla.
 */
public interface ViewStudentObserver extends StudentObserver {
    /**
     * Solicita que se refresque la tabla de estudiantes.
     */
    void refreshTable();
}
