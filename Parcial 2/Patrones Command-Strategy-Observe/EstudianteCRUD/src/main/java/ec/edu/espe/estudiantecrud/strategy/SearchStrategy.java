package ec.edu.espe.estudiantecrud.strategy;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import java.util.List;

/**
 * Interfaz que define la estrategia para buscar estudiantes.
 * Permite implementar diferentes tipos de búsqueda.
 */
public interface SearchStrategy {
    /**
     * Busca estudiantes según la estrategia implementada.
     * @param criterio el criterio de búsqueda (ID, nombre, carrera, etc)
     * @param estudiantes la lista de estudiantes donde buscar
     * @return lista de estudiantes que coinciden con el criterio
     */
    List<Estudiante> search(String criterio, List<Estudiante> estudiantes);
}
