package ec.edu.espe.estudiantecrud.controller;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import ec.edu.espe.estudiantecrud.service.StudentService;
import ec.edu.espe.estudiantecrud.observer.ConsoleStudentObserver;
import java.util.List;

/**
 * Controlador refactorizado que actúa como intermediario entre
 * la vista y la capa de servicio.
 * Delega toda la lógica de negocio al StudentService.
 */
public class ControlEstudiante {
    private final StudentService service;

    public ControlEstudiante() {
        this.service = new StudentService();
        // Registrar observer de consola para logging
        service.registerObserver(new ConsoleStudentObserver());
    }

    /**
     * Agrega un nuevo estudiante.
     * @param id el ID del estudiante
     * @param nombre el nombre del estudiante
     * @param edad la edad del estudiante
     * @return mensaje del resultado
     */
    public String agregarEstudiante(String id, String nombre, int edad) {
        return agregarEstudiante(id, nombre, edad, "Sin carrera");
    }

    /**
     * Agrega un nuevo estudiante con carrera.
     * @param id el ID del estudiante
     * @param nombre el nombre del estudiante
     * @param edad la edad del estudiante
     * @param carrera la carrera del estudiante
     * @return mensaje del resultado
     */
    public String agregarEstudiante(String id, String nombre, int edad, String carrera) {
        return service.addStudent(id, nombre, edad, carrera);
    }

    /**
     * Actualiza un estudiante existente.
     * @param id el ID del estudiante
     * @param nombre el nuevo nombre
     * @param edad la nueva edad
     * @return mensaje del resultado
     */
    public String actualizarEstudiante(String id, String nombre, int edad) {
        return actualizarEstudiante(id, nombre, edad, "Sin carrera");
    }

    /**
     * Actualiza un estudiante existente con carrera.
     * @param id el ID del estudiante
     * @param nombre el nuevo nombre
     * @param edad la nueva edad
     * @param carrera la nueva carrera
     * @return mensaje del resultado
     */
    public String actualizarEstudiante(String id, String nombre, int edad, String carrera) {
        return service.updateStudent(id, nombre, edad, carrera);
    }

    /**
     * Elimina un estudiante.
     * @param id el ID del estudiante a eliminar
     * @return mensaje del resultado
     */
    public String eliminarEstudiante(String id) {
        return service.deleteStudent(id);
    }

    /**
     * Obtiene todos los estudiantes.
     * @return lista de todos los estudiantes
     */
    public List<Estudiante> mostrarTodos() {
        return service.getAllStudents();
    }

    /**
     * Obtiene un estudiante por su ID.
     * @param id el ID del estudiante
     * @return el estudiante o null si no existe
     */
    public Estudiante obtenerPorId(String id) {
        return service.getStudentById(id);
    }

    /**
     * Busca estudiantes por ID.
     * @param id el ID a buscar
     * @return lista de estudiantes encontrados
     */
    public List<Estudiante> buscarPorId(String id) {
        return service.searchById(id);
    }

    /**
     * Busca estudiantes por nombre.
     * @param nombre el nombre a buscar
     * @return lista de estudiantes encontrados
     */
    public List<Estudiante> buscarPorNombre(String nombre) {
        return service.searchByName(nombre);
    }

    /**
     * Busca estudiantes por carrera.
     * @param carrera la carrera a buscar
     * @return lista de estudiantes encontrados
     */
    public List<Estudiante> buscarPorCarrera(String carrera) {
        return service.searchByCareer(carrera);
    }

    /**
     * Deshace la última operación.
     * @return mensaje del resultado
     */
    public String deshacer() {
        return service.undo();
    }

    /**
     * Rehace la última operación desecha.
     * @return mensaje del resultado
     */
    public String rehacer() {
        return service.redo();
    }

    /**
     * Obtiene el acceso directo al servicio para casos especiales.
     * @return el StudentService
     */
    public StudentService getService() {
        return service;
    }
}