package com.tekmess.snaar.controlador.http;

import com.tekmess.snaar.controlador.servicio.ReporteServicio;
import com.tekmess.snaar.modelo.dao.EmpleadoDAO;
import com.tekmess.snaar.modelo.dao.ReporteDAO;
import com.tekmess.snaar.modelo.dao.UsuarioDAO;
import com.tekmess.snaar.modelo.entidad.Reporte;
import com.tekmess.snaar.modelo.entidad.Sesion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ReportesController", urlPatterns = { "/reportes/*" })
public class ReportesController extends HttpServlet {

    private ReporteServicio reporteServicio;
    private static final String FORMATO_FECHA = "yyyy-MM-dd";

    @Override
    public void init() throws ServletException {
        this.reporteServicio = new ReporteServicio(
                new ReporteDAO(),
                new EmpleadoDAO(),
                new UsuarioDAO());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!isUsuarioAutenticado(req, resp)) {
            return;
        }

        String path = req.getPathInfo();
        if (path == null || "/".equals(path) || "/listar".equals(path)) {
            listarReportes(req, resp);
            return;
        }

        if ("/ver".equals(path)) {
            mostrarDetalle(req, resp);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/reportes");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!isUsuarioAutenticado(req, resp)) {
            return;
        }

        String path = req.getPathInfo();
        if (path == null) {
            resp.sendRedirect(req.getContextPath() + "/reportes");
            return;
        }

        switch (path) {
            case "/generar":
                generarReporte(req, resp);
                break;
            case "/anotar":
                agregarAnotacion(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/reportes");
        }
    }

    private void listarReportes(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fechaInicioParam = req.getParameter("fechaInicio");
        String fechaFinParam = req.getParameter("fechaFin");
        Date fechaInicio = null;
        Date fechaFin = null;

        if (fechaInicioParam != null && !fechaInicioParam.isBlank() &&
                fechaFinParam != null && !fechaFinParam.isBlank()) {
            fechaInicio = parseFecha(fechaInicioParam);
            fechaFin = parseFecha(fechaFinParam);
            if (fechaInicio == null || fechaFin == null) {
                req.setAttribute("error", "Debe ingresar fechas válidas en formato YYYY-MM-DD.");
            }
        }

        Object[] resultado = reporteServicio.consultarHistorial(fechaInicio, fechaFin);
        if (resultado[0] != null) {
            @SuppressWarnings("unchecked")
            List<Reporte> reportes = (List<Reporte>) resultado[0];
            req.setAttribute("reportes", reportes);
        } else {
            req.setAttribute("reportes", List.of());
            req.setAttribute("error", resultado[1]);
        }

        req.setAttribute("fechaInicio", fechaInicioParam);
        req.setAttribute("fechaFin", fechaFinParam);
        req.getRequestDispatcher("/vistas/reportes/listar.jsp").forward(req, resp);
    }

    private void mostrarDetalle(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/reportes");
            return;
        }

        try {
            int idReporte = Integer.parseInt(idParam);
            Reporte reporte = reporteServicio.obtenerReporte(idReporte);
            if (reporte == null) {
                req.setAttribute("error", "No se encontró el reporte solicitado.");
                listarReportes(req, resp);
                return;
            }
            req.setAttribute("reporte", reporte);
            req.getRequestDispatcher("/vistas/reportes/detalle.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID de reporte inválido.");
            listarReportes(req, resp);
        }
    }

    private void generarReporte(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String fechaInicioParam = req.getParameter("fechaInicio");
        String fechaFinParam = req.getParameter("fechaFin");
        Date fechaInicio = parseFecha(fechaInicioParam);
        Date fechaFin = parseFecha(fechaFinParam);

        if (fechaInicio == null || fechaFin == null) {
            req.setAttribute("error", "Ambas fechas deben ser válidas y no estar vacías.");
            listarReportes(req, resp);
            return;
        }

        HttpSession session = req.getSession(false);
        String usuario = (String) session.getAttribute("usuario");

        Object[] resultado = reporteServicio.generarReporte(fechaInicio, fechaFin, usuario);
        Reporte reporte = (Reporte) resultado[0];
        String mensaje = (String) resultado[1];

        if (reporte != null) {
            req.setAttribute("exito", mensaje);
            req.setAttribute("reporteGenerado", reporte);
        } else {
            req.setAttribute("error", mensaje);
        }

        listarReportes(req, resp);
    }

    private void agregarAnotacion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idParam = req.getParameter("idReporte");
        String contenido = req.getParameter("contenido");
        HttpSession session = req.getSession(false);
        String autor = (String) session.getAttribute("usuario");

        if (idParam == null || idParam.isBlank() || contenido == null || contenido.isBlank()) {
            req.setAttribute("error", "El contenido de la anotación no puede estar vacío.");
            mostrarDetalle(req, resp);
            return;
        }

        try {
            int idReporte = Integer.parseInt(idParam);
            boolean agregado = reporteServicio.agregarAnotacion(idReporte, contenido, autor);
            if (agregado) {
                req.setAttribute("exito", "Anotación agregada correctamente.");
            } else {
                req.setAttribute("error", "No se pudo guardar la anotación.");
            }
            mostrarDetalle(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID de reporte inválido.");
            mostrarDetalle(req, resp);
        }
    }

    private boolean isUsuarioAutenticado(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return false;
        }
        return true;
    }

    private Date parseFecha(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return new SimpleDateFormat(FORMATO_FECHA).parse(valor);
        } catch (ParseException e) {
            return null;
        }
    }
}
