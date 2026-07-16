package ec.edu.espe.estudiantecrud.logica.strategy;

import ec.edu.espe.estudiantecrud.logica.Estudiante;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia concreta: Búsqueda por Nombre.
 * Filtra estudiantes cuyo nombre contenga (parcial, sin distinción de mayúsculas)
 * el criterio proporcionado.
 */
public class BuscarPorNombre implements EstrategiaBusqueda {

    @Override
    public List<Estudiante> buscar(List<Estudiante> estudiantes, String criterio) {
        String criterioBusqueda = criterio.toLowerCase();
        return estudiantes.stream()
                .filter(e -> e.getNombre().toLowerCase().contains(criterioBusqueda))
                .collect(Collectors.toList());
    }

    @Override
    public String getNombreEstrategia() {
        return "Búsqueda por Nombre";
    }
}
