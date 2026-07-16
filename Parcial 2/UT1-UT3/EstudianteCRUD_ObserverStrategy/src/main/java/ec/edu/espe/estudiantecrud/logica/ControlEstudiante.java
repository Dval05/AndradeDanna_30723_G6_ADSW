package ec.edu.espe.estudiantecrud.logica;

import ec.edu.espe.estudiantecrud.datos.RepositorioEstudiante;
import ec.edu.espe.estudiantecrud.logica.observer.GestorEventosEstudiante;
import ec.edu.espe.estudiantecrud.logica.strategy.ContextoBusqueda;
import ec.edu.espe.estudiantecrud.logica.strategy.BuscarPorID;
import java.util.List;

/**
 * Servicio base del CRUD del estudiante.
 * Integra los patrones Observer y Strategy:
 * - Observer: notifica eventos al crear, actualizar o eliminar estudiantes.
 * - Strategy: permite cambiar dinámicamente la estrategia de búsqueda.
 */
public class ControlEstudiante implements IControlEstudiante {
    private final RepositorioEstudiante repositorio = new RepositorioEstudiante();
    private final GestorEventosEstudiante gestorEventos = new GestorEventosEstudiante();
    private final ContextoBusqueda contextoBusqueda;

    public ControlEstudiante() {
        // Estrategia de búsqueda por defecto: por ID
        this.contextoBusqueda = new ContextoBusqueda(new BuscarPorID());
    }

    @Override
    public String agregarEstudiante(String id, String nombre, int edad) {
        if (!validarDatos(id, nombre, edad)) return "Error: Datos inválidos.";
        if (repositorio.existeId(id)) return "Error: El ID ya existe.";
        
        repositorio.guardar(new Estudiante(id, nombre, edad));
        // Observer: notificar evento de creación
        gestorEventos.notificar("CREAR", "Estudiante registrado - ID: " + id + ", Nombre: " + nombre + ", Edad: " + edad);
        return "Éxito: Estudiante agregado.";
    }

    @Override
    public String actualizarEstudiante(String id, String nombre, int edad) {
        if (!repositorio.existeId(id)) return "Error: Estudiante no encontrado.";
        repositorio.actualizar(new Estudiante(id, nombre, edad));
        // Observer: notificar evento de actualización
        gestorEventos.notificar("ACTUALIZAR", "Estudiante actualizado - ID: " + id + ", Nombre: " + nombre + ", Edad: " + edad);
        return "Éxito: Datos actualizados.";
    }

    @Override
    public String eliminarEstudiante(String id) {
        if (!repositorio.existeId(id)) return "Error: ID no existe.";
        repositorio.eliminar(id);
        // Observer: notificar evento de eliminación
        gestorEventos.notificar("ELIMINAR", "Estudiante eliminado - ID: " + id);
        return "Éxito: Estudiante eliminado.";
    }

    @Override
    public List<Estudiante> mostrarTodos() {
        return repositorio.listarTodos();
    }

    /**
     * Strategy: buscar estudiantes usando la estrategia activa del contexto.
     *
     * @param criterio valor de búsqueda
     * @return lista de estudiantes encontrados
     */
    public List<Estudiante> buscarEstudiante(String criterio) {
        return contextoBusqueda.ejecutarBusqueda(repositorio.listarTodos(), criterio);
    }

    /**
     * Retorna el contexto de búsqueda para permitir el cambio dinámico de estrategia.
     *
     * @return contexto de búsqueda
     */
    public ContextoBusqueda getContextoBusqueda() {
        return contextoBusqueda;
    }

    /**
     * Retorna el gestor de eventos para registrar observadores.
     *
     * @return gestor de eventos
     */
    public GestorEventosEstudiante getGestorEventos() {
        return gestorEventos;
    }

    public boolean validarDatos(String id, String nombre, int edad) {
        return !id.trim().isEmpty() && !nombre.trim().isEmpty() && edad > 0;
    }
}
