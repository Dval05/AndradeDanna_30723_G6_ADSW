package ec.edu.espe.estudiantecrud.command;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import ec.edu.espe.estudiantecrud.repository.StudentRepository;

/**
 * Comando para eliminar un estudiante.
 * Encapsula la lógica de eliminación y permite guardar el estado para undo.
 */
public class DeleteStudentCommand implements Command {
    private final StudentRepository repository;
    private final String id;
    private Estudiante estudianteEliminado;
    private boolean executed;

    public DeleteStudentCommand(StudentRepository repository, String id) {
        this.repository = repository;
        this.id = id;
        this.executed = false;
    }

    @Override
    public String execute() {
        if (executed) {
            return "Advertencia: Este comando ya fue ejecutado.";
        }

        if (!repository.existsById(id)) {
            return "Error: Estudiante con ID " + id + " no existe.";
        }

        // Guardar el estado anterior para poder deshacer
        this.estudianteEliminado = repository.findById(id);

        repository.delete(id);
        executed = true;
        return "Éxito: Estudiante con ID " + id + " eliminado.";
    }

    @Override
    public String undo() {
        if (!executed) {
            return "Advertencia: No hay nada que deshacer.";
        }

        if (estudianteEliminado != null) {
            repository.save(estudianteEliminado);
            executed = false;
            return "Éxito: Eliminación desecha - Estudiante restaurado.";
        }

        return "Error: No se puede deshacer la eliminación.";
    }

    @Override
    public String getDescription() {
        return "Eliminar estudiante con ID: " + id;
    }
}
