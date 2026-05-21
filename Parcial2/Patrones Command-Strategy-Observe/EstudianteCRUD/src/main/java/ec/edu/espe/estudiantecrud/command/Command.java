package ec.edu.espe.estudiantecrud.command;

/**
 * Interfaz para implementar el patrón Command.
 * Define el contrato que deben cumplir todos los comandos ejecutables.
 */
public interface Command {
    /**
     * Ejecuta el comando.
     * @return mensaje descriptivo del resultado
     */
    String execute();

    /**
     * Deshace la ejecución del comando (si es aplicable).
     * @return mensaje descriptivo del resultado
     */
    String undo();

    /**
     * Obtiene la descripción del comando.
     * @return descripción del comando
     */
    String getDescription();
}
