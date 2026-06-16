package ec.edu.espe.estudiantecrud.repository;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import ec.edu.espe.estudiantecrud.observer.ObservadorEstudiante;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEstudiante {

    private final List<Estudiante> estudiantes = new ArrayList<>();
    private final List<ObservadorEstudiante> observadores = new ArrayList<>();

    public void registrarObservador(ObservadorEstudiante observador) {
        observadores.add(observador);
    }

    private void notificarObservadores() {
        for (ObservadorEstudiante o : observadores) {
            o.actualizar();
        }
    }

    public boolean existeId(String id) {
        return estudiantes.stream().anyMatch(e -> e.getId().equals(id));
    }

    public void guardar(Estudiante estudiante) {
        estudiantes.add(estudiante);
        notificarObservadores();
    }

    public Estudiante buscarPorId(String id) {
        return estudiantes.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void actualizar(Estudiante estudiante) {
        eliminar(estudiante.getId());
        estudiantes.add(estudiante);
        notificarObservadores();
    }

    public void eliminar(String id) {
        estudiantes.removeIf(e -> e.getId().equals(id));
        notificarObservadores();
    }

    public List<Estudiante> listarTodos() {
        return new ArrayList<>(estudiantes);
    }
}
