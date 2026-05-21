package ec.edu.espe.estudiantecrud.service;

import ec.edu.espe.estudiantecrud.command.*;
import ec.edu.espe.estudiantecrud.model.Estudiante;
import ec.edu.espe.estudiantecrud.observer.StudentObserver;
import ec.edu.espe.estudiantecrud.repository.StudentRepository;
import ec.edu.espe.estudiantecrud.strategy.*;
import java.util.List;

/**
 * Servicio de estudiantes que orquesta todos los patrones de diseño:
 * - Observer: para notificaciones de cambios
 * - Strategy: para diferentes tipos de búsqueda
 * - Command: para encapsular acciones CRUD
 *
 * Este servicio es el punto de entrada principal para todas las operaciones
 * relacionadas con estudiantes, proporcionando una interfaz limpia y desacoplada.
 */
public class StudentService {
    private final StudentRepository repository;
    private final CommandInvoker commandInvoker;
    private final SearchContext searchContext;

    public StudentService() {
        this.repository = new StudentRepository();
        this.commandInvoker = new CommandInvoker();
        this.searchContext = new SearchContext(new SearchByIdStrategy());
    }

    /**
     * Registra un observador para recibir notificaciones de cambios.
     * @param observer el observador a registrar
     */
    public void registerObserver(StudentObserver observer) {
        repository.attach(observer);
    }

    /**
     * Desregistra un observador.
     * @param observer el observador a desregistrar
     */
    public void unregisterObserver(StudentObserver observer) {
        repository.detach(observer);
    }

    /**
     * Agrega un nuevo estudiante utilizando el patrón Command.
     * @param id el ID del estudiante
     * @param nombre el nombre del estudiante
     * @param edad la edad del estudiante
     * @param carrera la carrera del estudiante
     * @return mensaje del resultado
     */
    public String addStudent(String id, String nombre, int edad, String carrera) {
        if (!validarDatos(id, nombre, edad, carrera)) {
            return "Error: Datos inválidos.";
        }

        Estudiante estudiante = new Estudiante(id, nombre, edad, carrera);
        Command command = new AddStudentCommand(repository, estudiante);
        return commandInvoker.executeCommand(command);
    }

    /**
     * Actualiza un estudiante existente utilizando el patrón Command.
     * @param id el ID del estudiante
     * @param nombre el nuevo nombre
     * @param edad la nueva edad
     * @param carrera la nueva carrera
     * @return mensaje del resultado
     */
    public String updateStudent(String id, String nombre, int edad, String carrera) {
        if (!validarDatos(id, nombre, edad, carrera)) {
            return "Error: Datos inválidos.";
        }

        Estudiante estudiante = new Estudiante(id, nombre, edad, carrera);
        Command command = new UpdateStudentCommand(repository, estudiante);
        return commandInvoker.executeCommand(command);
    }

    /**
     * Elimina un estudiante utilizando el patrón Command.
     * @param id el ID del estudiante a eliminar
     * @return mensaje del resultado
     */
    public String deleteStudent(String id) {
        Command command = new DeleteStudentCommand(repository, id);
        return commandInvoker.executeCommand(command);
    }

    /**
     * Busca estudiantes utilizando la estrategia actual.
     * @param criterio el criterio de búsqueda
     * @return lista de estudiantes encontrados
     */
    public List<Estudiante> search(String criterio) {
        return searchContext.executeSearch(criterio, repository.findAll());
    }

    /**
     * Busca estudiantes por ID.
     * @param id el ID a buscar
     * @return lista con el estudiante encontrado o vacía
     */
    public List<Estudiante> searchById(String id) {
        searchContext.setStrategy(new SearchByIdStrategy());
        return search(id);
    }

    /**
     * Busca estudiantes por nombre.
     * @param nombre el nombre a buscar
     * @return lista de estudiantes que coinciden
     */
    public List<Estudiante> searchByName(String nombre) {
        searchContext.setStrategy(new SearchByNameStrategy());
        return search(nombre);
    }

    /**
     * Busca estudiantes por carrera.
     * @param carrera la carrera a buscar
     * @return lista de estudiantes de esa carrera
     */
    public List<Estudiante> searchByCareer(String carrera) {
        searchContext.setStrategy(new SearchByCareerStrategy());
        return search(carrera);
    }

    /**
     * Obtiene todos los estudiantes.
     * @return lista de todos los estudiantes
     */
    public List<Estudiante> getAllStudents() {
        return repository.findAll();
    }

    /**
     * Obtiene un estudiante por su ID.
     * @param id el ID del estudiante
     * @return el estudiante o null si no existe
     */
    public Estudiante getStudentById(String id) {
        return repository.findById(id);
    }

    /**
     * Deshace la última operación.
     * @return mensaje del resultado
     */
    public String undo() {
        return commandInvoker.undo();
    }

    /**
     * Rehace la última operación desecha.
     * @return mensaje del resultado
     */
    public String redo() {
        return commandInvoker.redo();
    }

    /**
     * Valida los datos de un estudiante.
     * @param id el ID del estudiante
     * @param nombre el nombre del estudiante
     * @param edad la edad del estudiante
     * @param carrera la carrera del estudiante
     * @return true si los datos son válidos, false en caso contrario
     */
    private boolean validarDatos(String id, String nombre, int edad, String carrera) {
        return !id.trim().isEmpty() &&
               !nombre.trim().isEmpty() &&
               edad > 0 &&
               !carrera.trim().isEmpty();
    }

    /**
     * Obtiene el número de operaciones en el historial.
     * @return tamaño del historial de comandos
     */
    public int getHistorySize() {
        return commandInvoker.getHistorySize();
    }
}
