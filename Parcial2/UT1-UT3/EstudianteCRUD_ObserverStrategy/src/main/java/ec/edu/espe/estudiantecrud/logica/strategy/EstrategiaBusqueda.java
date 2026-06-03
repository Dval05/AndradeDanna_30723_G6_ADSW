package ec.edu.espe.estudiantecrud.logica.strategy;

import ec.edu.espe.estudiantecrud.logica.Estudiante;
import java.util.List;

/**
 * Interfaz Strategy para búsqueda de estudiantes.
 * Define el contrato común que deben implementar todas las
 * estrategias de búsqueda, permitiendo cambiar dinámicamente
 * el algoritmo sin modificar el cliente principal.
 */
public interface EstrategiaBusqueda {

    /**
     * Busca estudiantes en la lista según el criterio específico de la estrategia.
     *
     * @param estudiantes lista de estudiantes donde buscar
     * @param criterio    valor de búsqueda (puede ser ID, nombre, etc.)
     * @return lista de estudiantes que coinciden con el criterio
     */
    List<Estudiante> buscar(List<Estudiante> estudiantes, String criterio);

    /**
     * Retorna el nombre descriptivo de la estrategia.
     *
     * @return nombre de la estrategia
     */
    String getNombreEstrategia();
}
