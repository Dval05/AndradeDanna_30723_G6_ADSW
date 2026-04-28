package ec.edu.espe.estudiantecrud.repository;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEstudiante {
    private final List<Estudiante> estudiantes = new ArrayList<>();

    public boolean existeId(String id) {
        return estudiantes.stream().anyMatch(e -> e.getId().equals(id));
    }

    public void guardar(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    public Estudiante buscarPorId(String id) {
        return estudiantes.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void actualizar(Estudiante estudiante) {
        eliminar(estudiante.getId());
        guardar(estudiante);
    }

    public void eliminar(String id) {
        estudiantes.removeIf(e -> e.getId().equals(id));
    }

    public List<Estudiante> listarTodos() {
        return new ArrayList<>(estudiantes);
    }
}