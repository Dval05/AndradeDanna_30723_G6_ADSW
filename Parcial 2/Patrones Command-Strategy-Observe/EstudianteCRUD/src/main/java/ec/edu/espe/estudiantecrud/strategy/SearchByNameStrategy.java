package ec.edu.espe.estudiantecrud.strategy;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia de búsqueda por nombre.
 * Busca estudiantes cuyo nombre contenga el criterio (búsqueda parcial).
 */
public class SearchByNameStrategy implements SearchStrategy {

    @Override
    public List<Estudiante> search(String criterio, List<Estudiante> estudiantes) {
        return estudiantes.stream()
                .filter(e -> e.getNombre().toLowerCase().contains(criterio.toLowerCase()))
                .collect(Collectors.toList());
    }
}
