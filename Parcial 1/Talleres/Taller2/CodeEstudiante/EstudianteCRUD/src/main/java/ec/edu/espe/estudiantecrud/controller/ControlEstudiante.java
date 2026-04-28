package ec.edu.espe.estudiantecrud.controller;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import ec.edu.espe.estudiantecrud.repository.RepositorioEstudiante;
import java.util.List;

public class ControlEstudiante {
    private final RepositorioEstudiante repositorio = new RepositorioEstudiante();

    public String agregarEstudiante(String id, String nombre, int edad) {
        if (!validarDatos(id, nombre, edad)) return "Error: Datos inválidos.";
        if (repositorio.existeId(id)) return "Error: El ID ya existe.";
        
        repositorio.guardar(new Estudiante(id, nombre, edad));
        return "Éxito: Estudiante agregado.";
    }

    public String actualizarEstudiante(String id, String nombre, int edad) {
        if (!repositorio.existeId(id)) return "Error: Estudiante no encontrado.";
        repositorio.actualizar(new Estudiante(id, nombre, edad));
        return "Éxito: Datos actualizados.";
    }

    public String eliminarEstudiante(String id) {
        if (!repositorio.existeId(id)) return "Error: ID no existe.";
        repositorio.eliminar(id);
        return "Éxito: Estudiante eliminado.";
    }

    public List<Estudiante> mostrarTodos() {
        return repositorio.listarTodos();
    }

    public boolean validarDatos(String id, String nombre, int edad) {
        return !id.trim().isEmpty() && !nombre.trim().isEmpty() && edad > 0;
    }
}