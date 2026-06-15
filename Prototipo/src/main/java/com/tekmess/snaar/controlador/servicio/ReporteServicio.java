package com.tekmess.snaar.controlador.servicio;

import com.tekmess.snaar.modelo.dao.IEmpleadoDAO;
import com.tekmess.snaar.modelo.dao.IReporteDAO;
import com.tekmess.snaar.modelo.dao.IUsuarioDAO;
import com.tekmess.snaar.modelo.entidad.Anotacion;
import com.tekmess.snaar.modelo.entidad.Reporte;

import java.util.Date;
import java.util.List;

/**
 * Servicio de Reportes Analíticos (RF-SNAAR-03V2).
 * Capa de Lógica de Negocio – generación, consulta y exportación de reportes.
 * Consolida datos de RF-01 (empleados) y RF-02 (accesos fallidos).
 */
public class ReporteServicio {

    private final IReporteDAO reporteDAO;
    private final IEmpleadoDAO empleadoDAO;
    private final IUsuarioDAO usuarioDAO;

    public ReporteServicio(IReporteDAO reporteDAO, IEmpleadoDAO empleadoDAO, IUsuarioDAO usuarioDAO) {
        this.reporteDAO = reporteDAO;
        this.empleadoDAO = empleadoDAO;
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * RF-SNAAR-03V2.01: Generar Reporte.
     * Consulta datos de RF-01 y RF-02, construye y almacena el reporte.
     *
     * @param fechaInicio inicio del período
     * @param fechaFin fin del período
     * @param generadoPor nombre del actor que genera el reporte
     * @return reporte generado o null si hay error
     */
    public Object[] generarReporte(Date fechaInicio, Date fechaFin, String generadoPor) {
        // Paso 6: Validar rango de fechas
        if (fechaFin.before(fechaInicio) || fechaFin.equals(fechaInicio)) {
            return new Object[]{null,
                    "El período de consulta no es válido. La fecha de fin debe ser posterior a la fecha de inicio."};
        }

        // Paso 7-8: Consultar datos de RF-01 (empleados gestionados)
        int totalCreados = empleadoDAO.contarCreadosEnPeriodo(fechaInicio, fechaFin);

        // Paso 9-10: Consultar empleados editados
        int totalEditados = empleadoDAO.contarEditadosEnPeriodo(fechaInicio, fechaFin);

        // Total eliminados
        int totalEliminados = empleadoDAO.contarEliminadosEnPeriodo(fechaInicio, fechaFin);

        // Paso 11-12: Consultar accesos fallidos de RF-02
        int totalAccesosFallidos = usuarioDAO.contarAccesosFallidosEnPeriodo(fechaInicio, fechaFin);

        // Excepción 12: Período sin datos
        if (totalCreados == 0 && totalEditados == 0 &&
            totalEliminados == 0 && totalAccesosFallidos == 0) {
            return new Object[]{null,
                    "No existen datos registrados para el período seleccionado."};
        }

        // Paso 13: Construir reporte
        Reporte reporte = new Reporte(fechaInicio, fechaFin, generadoPor);
        reporte.setTotalEmpleadosCreados(totalCreados);
        reporte.setTotalEmpleadosEditados(totalEditados);
        reporte.setTotalEmpleadosEliminados(totalEliminados);
        reporte.setTotalAccesosFallidos(totalAccesosFallidos);

        // Paso 14: Guardar en BD
        boolean guardado = reporteDAO.guardar(reporte);
        if (!guardado) {
            return new Object[]{null, "Error al almacenar el reporte."};
        }

        return new Object[]{reporte, "Reporte generado exitosamente."};
    }

    /**
     * RF-SNAAR-03V2.02: Consultar Historial de Reportes.
     * Filtro opcional por rango de fechas.
     */
    public Object[] consultarHistorial(Date fechaInicio, Date fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            // Validar rango
            if (fechaFin.before(fechaInicio) || fechaFin.equals(fechaInicio)) {
                return new Object[]{null, "El período de consulta no es válido."};
            }

            List<Reporte> reportes = reporteDAO.listarPorFechas(fechaInicio, fechaFin);
            if (reportes.isEmpty()) {
                return new Object[]{null, "No se encontraron reportes para el período seleccionado."};
            }
            return new Object[]{reportes, "OK"};
        }

        // Sin filtro: retornar todos
        List<Reporte> reportes = reporteDAO.listarTodos();
        if (reportes.isEmpty()) {
            return new Object[]{null, "No se encontraron reportes."};
        }
        return new Object[]{reportes, "OK"};
    }

    /**
     * Agregar anotación a un reporte existente.
     */
    public boolean agregarAnotacion(int idReporte, String contenido, String autor) {
        Reporte reporte = reporteDAO.buscarPorId(idReporte);
        if (reporte == null) {
            return false;
        }

        Anotacion anotacion = new Anotacion(idReporte, contenido, autor);
        return reporteDAO.agregarAnotacion(idReporte, anotacion);
    }

    /**
     * Obtener un reporte por ID.
     */
    public Reporte obtenerReporte(int idReporte) {
        return reporteDAO.buscarPorId(idReporte);
    }
}
