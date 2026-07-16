package ec.edu.espe.estudiantecrud.strategy;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia de búsqueda por ID.
 * Busca un estudiante exacto por su ID.
 */
public class SearchByIdStrategy implements SearchStrategy {

    @Override
    public List<Estudiante> search(String criterio, List<Estudiante> estudiantes) {
        return estudiantes.stream()
                .filter(e -> e.getId().equalsIgnoreCase(criterio))
                .collect(Collectors.toList());
    }
}
