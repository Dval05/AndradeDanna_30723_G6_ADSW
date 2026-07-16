package ec.edu.espe.estudiantecrud.strategy;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia de búsqueda por carrera.
 * Busca estudiantes que estudien una carrera específica.
 */
public class SearchByCareerStrategy implements SearchStrategy {

    @Override
    public List<Estudiante> search(String criterio, List<Estudiante> estudiantes) {
        return estudiantes.stream()
                .filter(e -> e.getCarrera().toLowerCase().contains(criterio.toLowerCase()))
                .collect(Collectors.toList());
    }
}
