package com.tekmess.snaar.patron.command;

/**
 * Interfaz Command (Comando) – Patrón Command.
 * Todas las operaciones del sistema SNAAR se encapsulan
 * como comandos con ejecutar() y deshacer().
 */
public interface IComando {

    /**
     * Ejecuta la operación encapsulada.
     * @return resultado de la ejecución
     */
    ResultadoComando ejecutar();

    /**
     * Deshace la operación ejecutada (si es posible).
     * @return resultado de la operación de deshacer
     */
    ResultadoComando deshacer();

    /**
     * Obtiene la descripción legible del comando.
     * @return descripción del comando
     */
    String getDescripcion();
}
