package ec.edu.espe.estudiantecrud.command;

import ec.edu.espe.estudiantecrud.controller.ControlEstudiante;
import ec.edu.espe.estudiantecrud.model.Estudiante;

public class ComandoActualizar implements Comando {

    private final ControlEstudiante controlador;
    private final String id;
    private final String nombreNuevo;
    private final int edadNueva;
    private String nombreAnterior;
    private int edadAnterior;
    private String resultado;

    public ComandoActualizar(ControlEstudiante controlador, String id, String nombreNuevo, int edadNueva) {
        this.controlador = controlador;
        this.id = id;
        this.nombreNuevo = nombreNuevo;
        this.edadNueva = edadNueva;
    }

    @Override
    public void ejecutar() {
        Estudiante anterior = controlador.buscarEstudiante(id);
        if (anterior != null) {
            nombreAnterior = anterior.getNombre();
            edadAnterior = anterior.getEdad();
        }
        resultado = controlador.actualizarEstudiante(id, nombreNuevo, edadNueva);
    }

    @Override
    public void deshacer() {
        if (nombreAnterior != null) {
            controlador.actualizarEstudiante(id, nombreAnterior, edadAnterior);
        }
    }

    @Override
    public String getResultado() {
        return resultado;
    }
}
