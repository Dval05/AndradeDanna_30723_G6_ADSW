package ec.edu.espe.estudiantecrud.strategy;

import java.util.List;

import ec.edu.espe.estudiantecrud.model.Estudiante;

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
        String nombre = strategy == null ? "null" : strategy.getClass().getSimpleName();
        System.out.println("[STRATEGY] Estrategia actual: " + nombre);
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
        System.out.println("[STRATEGY] Ejecutando busqueda con "
                + strategy.getClass().getSimpleName()
                + " | criterio='" + criterio + "' | total=" + estudiantes.size());
        return strategy.search(criterio, estudiantes);
    }
}
