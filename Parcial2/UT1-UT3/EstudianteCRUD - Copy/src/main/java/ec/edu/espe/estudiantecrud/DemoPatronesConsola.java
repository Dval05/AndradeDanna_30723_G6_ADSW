package ec.edu.espe.estudiantecrud;

import ec.edu.espe.estudiantecrud.adapter.EstudianteAdapter;
import ec.edu.espe.estudiantecrud.adapter.EstudianteExterno;
import ec.edu.espe.estudiantecrud.controller.AuditoriaServicioEstudianteDecorator;
import ec.edu.espe.estudiantecrud.controller.ControlEstudiante;
import ec.edu.espe.estudiantecrud.controller.ServicioEstudiante;
import ec.edu.espe.estudiantecrud.model.Estudiante;

public class DemoPatronesConsola {
    public static void main(String[] args) {
        System.out.println("== Demo Adapter y Decorator ==");

        EstudianteExterno externo = new EstudianteExterno("A-001", "Maria Torres", 20);
        EstudianteAdapter adapter = new EstudianteAdapter();
        Estudiante interno = adapter.adaptar(externo);
        System.out.println("[ADAPTER] externo.codigo -> interno.id: " + externo.getCodigo() + " -> " + interno.getId());
        System.out.println("[ADAPTER] externo.nombreCompleto -> interno.nombre: " + externo.getNombreCompleto() + " -> " + interno.getNombre());
        System.out.println("[ADAPTER] externo.anios -> interno.edad: " + externo.getAnios() + " -> " + interno.getEdad());

        ServicioEstudiante base = new ControlEstudiante();
        System.out.println("\n== CRUD base (sin decorator) ==");
        System.out.println(base.agregarEstudiante(interno.getId(), interno.getNombre(), interno.getEdad()));

        ServicioEstudiante conAuditoria = new AuditoriaServicioEstudianteDecorator(base);
        System.out.println("\n== CRUD con AuditoriaDecorator ==");
        System.out.println(conAuditoria.actualizarEstudiante(interno.getId(), "Maria Torres M.", interno.getEdad()));
    }
}
