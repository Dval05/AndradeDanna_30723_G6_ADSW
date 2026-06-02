package ec.edu.espe.estudiantecrud.controller;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import java.util.List;

public interface ServicioEstudiante {
    String agregarEstudiante(String id, String nombre, int edad);

    String actualizarEstudiante(String id, String nombre, int edad);

    String eliminarEstudiante(String id);

    List<Estudiante> mostrarTodos();
}
