package ec.edu.espe.estudiantecrud.logica.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Observador concreto: Historial.
 * Mantiene un registro histórico de todas las operaciones realizadas
 * sobre la entidad Estudiante, útil para consultas de trazabilidad.
 */
public class HistorialObserver implements ObservadorEstudiante {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<String> historial = new ArrayList<>();

    @Override
    public void actualizar(String tipoEvento, String detalle) {
        String marca = LocalDateTime.now().format(FORMATO);
        String entrada = "[HISTORIAL " + marca + "] " + tipoEvento + ": " + detalle;
        historial.add(entrada);
        System.out.println(entrada);
    }

    /**
     * Retorna todo el historial de operaciones.
     *
     * @return lista del historial
     */
    public List<String> getHistorial() {
        return new ArrayList<>(historial);
    }

    /**
     * Retorna la última entrada del historial, o null si está vacío.
     *
     * @return última entrada del historial
     */
    public String getUltimaEntrada() {
        if (historial.isEmpty()) {
            return null;
        }
        return historial.get(historial.size() - 1);
    }

    /**
     * Limpia el historial completo.
     */
    public void limpiarHistorial() {
        historial.clear();
    }
}
