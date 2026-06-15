package com.tekmess.snaar.patron.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Invoker (Invocador) – Patrón Command.
 * Los controladores HTTP usan el InvocadorOperaciones para
 * ejecutar comandos. Mantiene historial para deshacer y trazabilidad.
 */
public class InvocadorOperaciones {

    private final Stack<IComando> historialComandos;

    public InvocadorOperaciones() {
        this.historialComandos = new Stack<>();
    }

    /**
     * Ejecuta un comando y lo agrega al historial.
     * @param comando comando a ejecutar
     * @return resultado de la ejecución
     */
    public ResultadoComando ejecutarComando(IComando comando) {
        ResultadoComando resultado = comando.ejecutar();

        if (resultado.isExitoso()) {
            historialComandos.push(comando);
            System.out.println("[INVOKER] Comando ejecutado: " + comando.getDescripcion());
        } else {
            System.out.println("[INVOKER] Comando fallido: " + comando.getDescripcion()
                    + " - " + resultado.getMensaje());
        }

        return resultado;
    }

    /**
     * Deshace el último comando ejecutado exitosamente.
     * @return resultado de la operación de deshacer
     */
    public ResultadoComando deshacerUltimo() {
        if (historialComandos.isEmpty()) {
            return new ResultadoComando(false, "No hay comandos para deshacer.");
        }

        IComando ultimoComando = historialComandos.pop();
        ResultadoComando resultado = ultimoComando.deshacer();
        System.out.println("[INVOKER] Deshacer: " + ultimoComando.getDescripcion()
                + " - " + resultado.getMensaje());
        return resultado;
    }

    /**
     * Obtiene el historial de comandos ejecutados.
     */
    public List<IComando> getHistorial() {
        return new ArrayList<>(historialComandos);
    }

    /**
     * Limpia el historial de comandos.
     */
    public void limpiarHistorial() {
        historialComandos.clear();
    }

    /**
     * Obtiene la cantidad de comandos en el historial.
     */
    public int getTamanioHistorial() {
        return historialComandos.size();
    }
}
