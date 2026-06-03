package ec.edu.espe.estudiantecrud.logica;

import java.util.List;

public interface IControlEstudiante {
    String agregarEstudiante(String id, String nombre, int edad);
    String actualizarEstudiante(String id, String nombre, int edad);
    String eliminarEstudiante(String id);
    List<Estudiante> mostrarTodos();
}
