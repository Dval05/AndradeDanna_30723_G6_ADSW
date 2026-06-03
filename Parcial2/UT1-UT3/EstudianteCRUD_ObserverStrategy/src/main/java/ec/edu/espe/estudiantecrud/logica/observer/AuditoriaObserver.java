package ec.edu.espe.estudiantecrud.logica.observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Observador concreto: Auditoría.
 * Registra en una bitácora interna todos los eventos del CRUD del estudiante,
 * con marca de tiempo para trazabilidad.
 */
public class AuditoriaObserver implements ObservadorEstudiante {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<String> registrosAuditoria = new ArrayList<>();

    @Override
    public void actualizar(String tipoEvento, String detalle) {
        String marca = LocalDateTime.now().format(FORMATO);
        String registro = "[AUDITORÍA " + marca + "] Evento: " + tipoEvento + " | Detalle: " + detalle;
        registrosAuditoria.add(registro);
        System.out.println(registro);
    }

    /**
     * Retorna todos los registros de auditoría almacenados.
     *
     * @return lista de registros de auditoría
     */
    public List<String> getRegistrosAuditoria() {
        return new ArrayList<>(registrosAuditoria);
    }

    /**
     * Limpia la bitácora de auditoría.
     */
    public void limpiarRegistros() {
        registrosAuditoria.clear();
    }
}
