package ec.edu.espe.estudiantecrud.command;

import java.util.Stack;

/**
 * Invoker que ejecuta comandos y mantiene un historial para undo/redo.
 * Encapsula la lógica de ejecución y administración de comandos.
 */
public class CommandInvoker {
    private final Stack<Command> historialEjecuciones;
    private final Stack<Command> historialDeshaceres;

    public CommandInvoker() {
        this.historialEjecuciones = new Stack<>();
        this.historialDeshaceres = new Stack<>();
    }

    /**
     * Ejecuta un comando y lo agrega al historial.
     * @param command el comando a ejecutar
     * @return resultado de la ejecución
     */
    public String executeCommand(Command command) {
        String resultado = command.execute();
        if (resultado.contains("Éxito")) {
            historialEjecuciones.push(command);
            historialDeshaceres.clear(); // Limpiar historial de deshaceres
        }
        return resultado;
    }

    /**
     * Deshace el último comando ejecutado.
     * @return resultado de deshacer
     */
    public String undo() {
        if (historialEjecuciones.isEmpty()) {
            return "Advertencia: No hay comando para deshacer.";
        }

        Command command = historialEjecuciones.pop();
        String resultado = command.undo();
        historialDeshaceres.push(command);
        return resultado;
    }

    /**
     * Rehace el último comando deshecho.
     * @return resultado de rehacer
     */
    public String redo() {
        if (historialDeshaceres.isEmpty()) {
            return "Advertencia: No hay comando para rehacer.";
        }

        Command command = historialDeshaceres.pop();
        String resultado = command.execute();
        historialEjecuciones.push(command);
        return resultado;
    }

    /**
     * Obtiene el historial de comandos ejecutados.
     * @return tamaño del historial
     */
    public int getHistorySize() {
        return historialEjecuciones.size();
    }

    /**
     * Limpia el historial de comandos.
     */
    public void clearHistory() {
        historialEjecuciones.clear();
        historialDeshaceres.clear();
    }
}
