package ec.edu.espe.estudiantecrud.repository;

import java.util.ArrayList;
import java.util.List;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import ec.edu.espe.estudiantecrud.observer.StudentObservable;
import ec.edu.espe.estudiantecrud.observer.StudentObserver;

/**
 * Repositorio refactorizado que implementa el patrón Observable.
 * Gestiona el almacenamiento en memoria y notifica a los observadores
 * sobre cambios en los estudiantes.
 */
public class StudentRepository implements StudentObservable {
    private final List<Estudiante> estudiantes = new ArrayList<>();
    private final List<StudentObserver> observadores = new ArrayList<>();

    /**
     * Verifica si existe un estudiante con el ID especificado.
     * @param id el ID a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(String id) {
        return estudiantes.stream().anyMatch(e -> e.getId().equals(id));
    }

    /**
     * Guarda un nuevo estudiante en el repositorio.
     * @param estudiante el estudiante a guardar
     */
    public void save(Estudiante estudiante) {
        estudiantes.add(estudiante);
        notifyStudentAdded(estudiante);
    }

    /**
     * Busca un estudiante por su ID.
     * @param id el ID del estudiante
     * @return el estudiante encontrado o null
     */
    public Estudiante findById(String id) {
        return estudiantes.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst().orElse(null);
    }

    /**
     * Actualiza un estudiante existente.
     * @param estudiante el estudiante con datos actualizados
     */
    public void update(Estudiante estudiante) {
        Estudiante existente = findById(estudiante.getId());
        if (existente != null) {
            int index = estudiantes.indexOf(existente);
            estudiantes.set(index, estudiante);
            notifyStudentUpdated(estudiante);
        }
    }

    /**
     * Elimina un estudiante por su ID.
     * @param id el ID del estudiante a eliminar
     */
    public void delete(String id) {
        Estudiante estudiante = findById(id);
        if (estudiante != null) {
            estudiantes.remove(estudiante);
            notifyStudentDeleted(id);
        }
    }

    /**
     * Obtiene la lista de todos los estudiantes.
     * @return copia de la lista de estudiantes
     */
    public List<Estudiante> findAll() {
        return new ArrayList<>(estudiantes);
    }

    // === IMPLEMENTACIÓN DEL PATRÓN OBSERVER ===

    @Override
    public void attach(StudentObserver observer) {
        if (!observadores.contains(observer)) {
            observadores.add(observer);
            System.out.println("[OBSERVER] Registrado: " + observer.getClass().getSimpleName()
                    + " | total=" + observadores.size());
        }
    }

    @Override
    public void detach(StudentObserver observer) {
        observadores.remove(observer);
        System.out.println("[OBSERVER] Desregistrado: " + observer.getClass().getSimpleName()
                + " | total=" + observadores.size());
    }

    @Override
    public void notifyStudentAdded(Estudiante estudiante) {
        System.out.println("[OBSERVER] Notificando agregado | total=" + observadores.size());
        for (StudentObserver observer : observadores) {
            observer.onStudentAdded(estudiante);
        }
    }

    @Override
    public void notifyStudentUpdated(Estudiante estudiante) {
        System.out.println("[OBSERVER] Notificando actualizado | total=" + observadores.size());
        for (StudentObserver observer : observadores) {
            observer.onStudentUpdated(estudiante);
        }
    }

    @Override
    public void notifyStudentDeleted(String id) {
        System.out.println("[OBSERVER] Notificando eliminado | total=" + observadores.size());
        for (StudentObserver observer : observadores) {
            observer.onStudentDeleted(id);
        }
    }

    /**
     * Obtiene el número de observadores registrados.
     * @return cantidad de observadores
     */
    public int getObserverCount() {
        return observadores.size();
    }
}
