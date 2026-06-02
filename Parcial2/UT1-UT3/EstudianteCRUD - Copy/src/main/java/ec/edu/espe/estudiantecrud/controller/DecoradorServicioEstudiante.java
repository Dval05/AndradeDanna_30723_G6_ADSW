package ec.edu.espe.estudiantecrud.controller;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import java.util.List;

public abstract class DecoradorServicioEstudiante implements ServicioEstudiante {
    protected final ServicioEstudiante servicioBase;

    protected DecoradorServicioEstudiante(ServicioEstudiante servicioBase) {
        this.servicioBase = servicioBase;
    }

    @Override
    public String agregarEstudiante(String id, String nombre, int edad) {
        return servicioBase.agregarEstudiante(id, nombre, edad);
    }

    @Override
    public String actualizarEstudiante(String id, String nombre, int edad) {
        return servicioBase.actualizarEstudiante(id, nombre, edad);
    }

    @Override
    public String eliminarEstudiante(String id) {
        return servicioBase.eliminarEstudiante(id);
    }

    @Override
    public List<Estudiante> mostrarTodos() {
        return servicioBase.mostrarTodos();
    }
}
