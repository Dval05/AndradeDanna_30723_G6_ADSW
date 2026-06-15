package com.tekmess.snaar.modelo.dao;

import com.tekmess.snaar.modelo.entidad.Empleado;
import java.util.List;

/**
 * Interfaz DAO para la entidad Empleado.
 * Define las operaciones CRUD de persistencia (Capa de Datos).
 */
public interface IEmpleadoDAO {

    boolean crear(Empleado empleado);
    boolean editar(Empleado empleado);
    boolean eliminar(String cedula);
    Empleado buscarPorCedula(String cedula);
    List<Empleado> listarTodos();
    boolean existeCedula(String cedula);
    int contarCreadosEnPeriodo(java.util.Date inicio, java.util.Date fin);
    int contarEditadosEnPeriodo(java.util.Date inicio, java.util.Date fin);
    int contarEliminadosEnPeriodo(java.util.Date inicio, java.util.Date fin);
}
