package ec.edu.espe.estudiantecrud.command;

import java.util.Stack;

public class InvocadorComando {

    private final Stack<Comando> historial = new Stack<>();

    public void ejecutar(Comando comando) {
        comando.ejecutar();
        historial.push(comando);
    }

    public void deshacer() {
        if (!historial.isEmpty()) {
            historial.pop().deshacer();
        }
    }

    public boolean tieneHistorial() {
        return !historial.isEmpty();
    }
}
