package ec.edu.espe.estudiantecrud.command;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import ec.edu.espe.estudiantecrud.repository.StudentRepository;

/**
 * Comando para actualizar un estudiante.
 * Encapsula la lógica de actualización y permite guardar el estado anterior para undo.
 */
public class UpdateStudentCommand implements Command {
    private final StudentRepository repository;
    private final Estudiante estudianteNuevo;
    private Estudiante estudianteAnterior;
    private boolean executed;

    public UpdateStudentCommand(StudentRepository repository, Estudiante estudianteNuevo) {
        this.repository = repository;
        this.estudianteNuevo = estudianteNuevo;
        this.executed = false;
    }

    @Override
    public String execute() {
        if (executed) {
            return "Advertencia: Este comando ya fue ejecutado.";
        }

        if (!repository.existsById(estudianteNuevo.getId())) {
            return "Error: Estudiante con ID " + estudianteNuevo.getId() + " no encontrado.";
        }

        // Guardar el estado anterior para poder deshacer
        this.estudianteAnterior = repository.findById(estudianteNuevo.getId());

        repository.update(estudianteNuevo);
        executed = true;
        return "Éxito: Estudiante " + estudianteNuevo.getNombre() + " actualizado.";
    }

    @Override
    public String undo() {
        if (!executed) {
            return "Advertencia: No hay nada que deshacer.";
        }

        if (estudianteAnterior != null) {
            repository.update(estudianteAnterior);
            executed = false;
            return "Éxito: Actualización desecha - Datos restaurados.";
        }

        return "Error: No se puede deshacer la actualización.";
    }

    @Override
    public String getDescription() {
        return "Actualizar estudiante con ID: " + estudianteNuevo.getId();
    }
}
