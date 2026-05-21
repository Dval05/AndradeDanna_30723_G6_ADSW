package ec.edu.espe.estudiantecrud.strategy;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import java.util.List;

/**
 * Contexto para aplicar diferentes estrategias de búsqueda.
 * Encapsula la lógica de selección de estrategia.
 */
public class SearchContext {
    private SearchStrategy strategy;

    /**
     * Constructor con estrategia inicial.
     * @param strategy la estrategia de búsqueda a usar
     */
    public SearchContext(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Cambia la estrategia de búsqueda dinámicamente.
     * @param strategy la nueva estrategia
     */
    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Ejecuta la búsqueda con la estrategia actual.
     * @param criterio el criterio de búsqueda
     * @param estudiantes la lista de estudiantes
     * @return resultado de la búsqueda
     */
    public List<Estudiante> executeSearch(String criterio, List<Estudiante> estudiantes) {
        if (strategy == null) {
            throw new IllegalStateException("Estrategia de búsqueda no establecida");
        }
        return strategy.search(criterio, estudiantes);
    }
}
