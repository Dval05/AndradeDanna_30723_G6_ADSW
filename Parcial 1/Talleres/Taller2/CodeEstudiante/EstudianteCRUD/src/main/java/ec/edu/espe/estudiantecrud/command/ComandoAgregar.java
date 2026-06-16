package ec.edu.espe.estudiantecrud.command;

import ec.edu.espe.estudiantecrud.controller.ControlEstudiante;

public class ComandoAgregar implements Comando {

    private final ControlEstudiante controlador;
    private final String id;
    private final String nombre;
    private final int edad;
    private String resultado;

    public ComandoAgregar(ControlEstudiante controlador, String id, String nombre, int edad) {
        this.controlador = controlador;
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
    }

    @Override
    public void ejecutar() {
        resultado = controlador.agregarEstudiante(id, nombre, edad);
    }

    @Override
    public void deshacer() {
        controlador.eliminarEstudiante(id);
    }

    @Override
    public String getResultado() {
        return resultado;
    }
}
