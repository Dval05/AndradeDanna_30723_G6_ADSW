package ec.edu.espe.estudiantecrud.command;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import ec.edu.espe.estudiantecrud.repository.StudentRepository;

/**
 * Comando para agregar un estudiante.
 * Encapsula la lógica de adición y permite su ejecución desacoplada.
 */
public class AddStudentCommand implements Command {
    private final StudentRepository repository;
    private final Estudiante estudiante;
    private boolean executed;

    public AddStudentCommand(StudentRepository repository, Estudiante estudiante) {
        this.repository = repository;
        this.estudiante = estudiante;
        this.executed = false;
    }

    @Override
    public String execute() {
        if (executed) {
            return "Advertencia: Este comando ya fue ejecutado.";
        }

        if (repository.existsById(estudiante.getId())) {
            return "Error: El ID " + estudiante.getId() + " ya existe.";
        }

        repository.save(estudiante);
        executed = true;
        return "Éxito: Estudiante " + estudiante.getNombre() + " agregado.";
    }

    @Override
    public String undo() {
        if (!executed) {
            return "Advertencia: No hay nada que deshacer.";
        }

        repository.delete(estudiante.getId());
        executed = false;
        return "Éxito: Adición desecha - Estudiante eliminado.";
    }

    @Override
    public String getDescription() {
        return "Agregar estudiante con ID: " + estudiante.getId();
    }
}
