package com.tekmess.snaar.modelo.dao;

import com.tekmess.snaar.modelo.entidad.Anotacion;
import com.tekmess.snaar.modelo.entidad.Reporte;
import java.util.Date;
import java.util.List;

/**
 * Interfaz DAO para la entidad Reporte.
 * Define operaciones de persistencia de reportes analíticos.
 */
public interface IReporteDAO {

    boolean guardar(Reporte reporte);
    Reporte buscarPorId(int id);
    List<Reporte> listarPorFechas(Date inicio, Date fin);
    List<Reporte> listarTodos();
    boolean agregarAnotacion(int idReporte, Anotacion anotacion);
    List<Anotacion> obtenerAnotaciones(int idReporte);
}
