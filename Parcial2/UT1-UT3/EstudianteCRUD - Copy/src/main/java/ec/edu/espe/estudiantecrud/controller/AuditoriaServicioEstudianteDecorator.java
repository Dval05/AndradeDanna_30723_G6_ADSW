package ec.edu.espe.estudiantecrud.controller;

public class AuditoriaServicioEstudianteDecorator extends DecoradorServicioEstudiante {
    public AuditoriaServicioEstudianteDecorator(ServicioEstudiante servicioBase) {
        super(servicioBase);
    }

    @Override
    public String agregarEstudiante(String id, String nombre, int edad) {
        System.out.println("[AUDITORIA] agregarEstudiante id=" + id + ", nombre=" + nombre + ", edad=" + edad);
        String resultado = super.agregarEstudiante(id, nombre, edad);
        System.out.println("[AUDITORIA] resultado=" + resultado);
        return resultado;
    }

    @Override
    public String actualizarEstudiante(String id, String nombre, int edad) {
        System.out.println("[AUDITORIA] actualizarEstudiante id=" + id + ", nombre=" + nombre + ", edad=" + edad);
        String resultado = super.actualizarEstudiante(id, nombre, edad);
        System.out.println("[AUDITORIA] resultado=" + resultado);
        return resultado;
    }

    @Override
    public String eliminarEstudiante(String id) {
        System.out.println("[AUDITORIA] eliminarEstudiante id=" + id);
        String resultado = super.eliminarEstudiante(id);
        System.out.println("[AUDITORIA] resultado=" + resultado);
        return resultado;
    }
}
