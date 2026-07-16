package ec.edu.espe.estudiantecrud.logica.strategy;

import ec.edu.espe.estudiantecrud.logica.Estudiante;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Estrategia concreta: Búsqueda por Edad.
 * Filtra estudiantes cuya edad coincida exactamente con el criterio proporcionado.
 * El criterio debe ser un valor numérico entero que representa la edad a buscar.
 */
public class BuscarPorEdad implements EstrategiaBusqueda {

    @Override
    public List<Estudiante> buscar(List<Estudiante> estudiantes, String criterio) {
        try {
            int edadBuscada = Integer.parseInt(criterio.trim());
            return estudiantes.stream()
                    .filter(e -> e.getEdad() == edadBuscada)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.out.println("[STRATEGY] Error: El criterio de edad debe ser un número entero.");
            return List.of();
        }
    }

    @Override
    public String getNombreEstrategia() {
        return "Búsqueda por Edad";
    }
}
