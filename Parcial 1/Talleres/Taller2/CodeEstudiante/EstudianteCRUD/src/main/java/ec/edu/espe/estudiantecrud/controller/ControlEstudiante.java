package ec.edu.espe.estudiantecrud.controller;

import ec.edu.espe.estudiantecrud.model.Estudiante;
import ec.edu.espe.estudiantecrud.observer.ObservadorEstudiante;
import ec.edu.espe.estudiantecrud.repository.RepositorioEstudiante;
import ec.edu.espe.estudiantecrud.strategy.EstrategiaValidacion;
import ec.edu.espe.estudiantecrud.strategy.ValidacionBasica;
import java.util.List;

public class ControlEstudiante {

    private final RepositorioEstudiante repositorio = new RepositorioEstudiante();
    private EstrategiaValidacion estrategia = new ValidacionBasica();

    public void agregarObservador(ObservadorEstudiante observador) {
        repositorio.registrarObservador(observador);
    }

    public Estudiante buscarEstudiante(String id) {
        return repositorio.buscarPorId(id);
    }

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
        return estrategia.validar(id, nombre, edad);
    }

    public void setEstrategia(EstrategiaValidacion estrategia) {
        this.estrategia = estrategia;
    }
}
