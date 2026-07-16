package ec.edu.espe.estudiantecrud.observer;

import ec.edu.espe.estudiantecrud.model.Estudiante;

/**
 * Implementación concreta del Observer que notifica cambios en la consola.
 * Útil para logging y debugging del sistema.
 */
public class ConsoleStudentObserver implements StudentObserver {

    @Override
    public void onStudentAdded(Estudiante estudiante) {
        System.out.println("✓ [OBSERVER] Estudiante agregado: " + estudiante);
    }

    @Override
    public void onStudentUpdated(Estudiante estudiante) {
        System.out.println("✓ [OBSERVER] Estudiante actualizado: " + estudiante);
    }

    @Override
    public void onStudentDeleted(String id) {
        System.out.println("✓ [OBSERVER] Estudiante eliminado - ID: " + id);
    }
}
