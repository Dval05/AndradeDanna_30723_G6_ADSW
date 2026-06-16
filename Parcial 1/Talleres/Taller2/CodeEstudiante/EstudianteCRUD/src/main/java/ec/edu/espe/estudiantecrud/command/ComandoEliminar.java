package ec.edu.espe.estudiantecrud.command;

import ec.edu.espe.estudiantecrud.controller.ControlEstudiante;
import ec.edu.espe.estudiantecrud.model.Estudiante;

public class ComandoEliminar implements Comando {

    private final ControlEstudiante controlador;
    private final String id;
    private String nombreGuardado;
    private int edadGuardada;
    private String resultado;

    public ComandoEliminar(ControlEstudiante controlador, String id) {
        this.controlador = controlador;
        this.id = id;
    }

    @Override
    public void ejecutar() {
        Estudiante estudiante = controlador.buscarEstudiante(id);
        if (estudiante != null) {
            nombreGuardado = estudiante.getNombre();
            edadGuardada = estudiante.getEdad();
        }
        resultado = controlador.eliminarEstudiante(id);
    }

    @Override
    public void deshacer() {
        if (nombreGuardado != null) {
            controlador.agregarEstudiante(id, nombreGuardado, edadGuardada);
        }
    }

    @Override
    public String getResultado() {
        return resultado;
    }
}
