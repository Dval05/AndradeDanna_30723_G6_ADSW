package ec.edu.espe.estudiantecrud.logica.strategy;

import ec.edu.espe.estudiantecrud.logica.Estudiante;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia concreta: Búsqueda por ID.
 * Filtra estudiantes cuyo ID coincida exactamente con el criterio proporcionado.
 */
public class BuscarPorID implements EstrategiaBusqueda {

    @Override
    public List<Estudiante> buscar(List<Estudiante> estudiantes, String criterio) {
        return estudiantes.stream()
                .filter(e -> e.getId().equals(criterio))
                .collect(Collectors.toList());
    }

    @Override
    public String getNombreEstrategia() {
        return "Búsqueda por ID";
    }
}
