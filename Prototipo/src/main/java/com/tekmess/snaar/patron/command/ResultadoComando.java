package com.tekmess.snaar.patron.command;

/**
 * Resultado de la ejecución de un comando.
 * Encapsula el estado (éxito/fallo), mensaje y datos retornados.
 */
public class ResultadoComando {

    private final boolean exitoso;
    private final String mensaje;
    private final Object datos;

    public ResultadoComando(boolean exitoso, String mensaje) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.datos = null;
    }

    public ResultadoComando(boolean exitoso, String mensaje, Object datos) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Object getDatos() {
        return datos;
    }

    @Override
    public String toString() {
        return (exitoso ? "[OK] " : "[ERROR] ") + mensaje;
    }
}
