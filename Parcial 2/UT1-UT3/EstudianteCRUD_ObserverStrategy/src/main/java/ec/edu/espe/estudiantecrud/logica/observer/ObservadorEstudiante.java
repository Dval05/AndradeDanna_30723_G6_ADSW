package ec.edu.espe.estudiantecrud.logica.observer;

/**
 * Interfaz Observer para el patrón Observer.
 * Define el contrato que deben cumplir todos los observadores
 * que deseen ser notificados ante eventos del CRUD del estudiante.
 */
public interface ObservadorEstudiante {

    /**
     * Método invocado cuando ocurre un evento en el CRUD del estudiante.
     *
     * @param tipoEvento tipo de evento: CREAR, ACTUALIZAR, ELIMINAR
     * @param detalle    información descriptiva del evento
     */
    void actualizar(String tipoEvento, String detalle);
}
