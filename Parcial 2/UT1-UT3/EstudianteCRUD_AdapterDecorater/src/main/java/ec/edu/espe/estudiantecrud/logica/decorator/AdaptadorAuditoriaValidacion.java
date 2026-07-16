package ec.edu.espe.estudiantecrud.logica.decorator;

import ec.edu.espe.estudiantecrud.logica.Estudiante;
import ec.edu.espe.estudiantecrud.logica.IControlEstudiante;
import java.util.List;

public class AdaptadorAuditoriaValidacion implements IControlEstudiante {
    private final IControlEstudiante servicioBase;

    public AdaptadorAuditoriaValidacion(IControlEstudiante servicioBase) {
        this.servicioBase = servicioBase;
    }

    @Override
    public String agregarEstudiante(String id, String nombre, int edad) {
        System.out.println("[AUDITORÍA] Intentando registrar estudiante con ID: " + id);
        if (edad <= 0) {
            return "Error (Validación Extra): La edad debe ser mayor a cero.";
        }
        String resultado = servicioBase.agregarEstudiante(id, nombre, edad);
        System.out.println("[AUDITORÍA] Resultado del registro: " + resultado);
        return resultado;
    }

    @Override
    public String actualizarEstudiante(String id, String nombre, int edad) {
        System.out.println("[AUDITORÍA] Intentando actualizar estudiante con ID: " + id);
        if (edad <= 0) {
            return "Error (Validación Extra): La edad debe ser mayor a cero para actualizar.";
        }
        String resultado = servicioBase.actualizarEstudiante(id, nombre, edad);
        System.out.println("[AUDITORÍA] Resultado de la actualización: " + resultado);
        return resultado;
    }

    @Override
    public String eliminarEstudiante(String id) {
        System.out.println("[AUDITORÍA] Intentando eliminar estudiante con ID: " + id);
        String resultado = servicioBase.eliminarEstudiante(id);
        System.out.println("[AUDITORÍA] Resultado de la eliminación: " + resultado);
        return resultado;
    }

    @Override
    public List<Estudiante> mostrarTodos() {
        System.out.println("[AUDITORÍA] Consultando la lista de todos los estudiantes.");
        return servicioBase.mostrarTodos();
    }
}
