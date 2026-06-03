package ec.edu.espe.estudiantecrud.logica.strategy;

import ec.edu.espe.estudiantecrud.logica.Estudiante;
import java.util.Collections;
import java.util.List;

/**
 * Contexto del patrón Strategy.
 * Mantiene una referencia a la estrategia de búsqueda activa y permite
 * cambiarla dinámicamente en tiempo de ejecución, sin modificar el cliente.
 */
public class ContextoBusqueda {

    private EstrategiaBusqueda estrategiaActual;

    /**
     * Crea el contexto con una estrategia inicial.
     *
     * @param estrategia estrategia de búsqueda inicial
     */
    public ContextoBusqueda(EstrategiaBusqueda estrategia) {
        this.estrategiaActual = estrategia;
    }

    /**
     * Cambia dinámicamente la estrategia de búsqueda activa.
     *
     * @param nuevaEstrategia nueva estrategia a utilizar
     */
    public void setEstrategia(EstrategiaBusqueda nuevaEstrategia) {
        this.estrategiaActual = nuevaEstrategia;
    }

    /**
     * Retorna la estrategia actualmente configurada.
     *
     * @return estrategia de búsqueda activa
     */
    public EstrategiaBusqueda getEstrategiaActual() {
        return estrategiaActual;
    }

    /**
     * Ejecuta la búsqueda usando la estrategia activa.
     *
     * @param estudiantes lista de estudiantes donde buscar
     * @param criterio    valor de búsqueda
     * @return lista de estudiantes encontrados, o lista vacía si no hay estrategia
     */
    public List<Estudiante> ejecutarBusqueda(List<Estudiante> estudiantes, String criterio) {
        if (estrategiaActual == null) {
            System.out.println("[STRATEGY] No hay estrategia de búsqueda configurada.");
            return Collections.emptyList();
        }
        System.out.println("[STRATEGY] Ejecutando: " + estrategiaActual.getNombreEstrategia()
                + " con criterio: \"" + criterio + "\"");
        return estrategiaActual.buscar(estudiantes, criterio);
    }
}
