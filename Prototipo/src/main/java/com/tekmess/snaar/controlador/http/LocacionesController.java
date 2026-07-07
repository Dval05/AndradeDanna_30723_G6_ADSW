package com.tekmess.snaar.controlador.http;

import com.tekmess.snaar.modelo.dao.LocacionDAO;
import com.tekmess.snaar.modelo.entidad.Locacion;
import com.tekmess.snaar.util.ValidadorDatos;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LocacionesController", urlPatterns = {"/locaciones/*"})
public class LocacionesController extends HttpServlet {

    private LocacionDAO locacionDAO;
    private ValidadorDatos validador;

    @Override
    public void init() {
        this.locacionDAO = new LocacionDAO();
        this.validador = new ValidadorDatos();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        listar(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/guardar";

        switch (path) {
            case "/guardar":
                guardar(req, resp);
                break;
            case "/estado":
                cambiarEstado(req, resp);
                break;
            default:
                listar(req, resp);
        }
    }

    private void listar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("locaciones", locacionDAO.listarTodas());
        req.setAttribute("ocupacionPorLocacion", locacionDAO.contarEmpleadosAsignados());
        req.getRequestDispatcher("/vistas/locaciones/listar.jsp").forward(req, resp);
    }

    private void guardar(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Locacion locacion = new Locacion();
        locacion.setNombre(limpiar(req.getParameter("nombre")));
        locacion.setCiudad(limpiar(req.getParameter("ciudad")));
        locacion.setDireccion(limpiar(req.getParameter("direccion")));
        locacion.setResponsable(limpiar(req.getParameter("responsable")));
        locacion.setActiva(req.getParameter("activa") != null);

        try {
            locacion.setCapacidad(Integer.parseInt(limpiar(req.getParameter("capacidad"))));
        } catch (Exception e) {
            req.setAttribute("error", "La capacidad debe ser un número válido.");
            req.setAttribute("locacion", locacion);
            listar(req, resp);
            return;
        }

        String error = validar(locacion);
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("locacion", locacion);
            listar(req, resp);
            return;
        }

        String id = limpiar(req.getParameter("idLocacion"));
        boolean ok;
        if (id != null && !id.isBlank()) {
            locacion.setIdLocacion(Integer.parseInt(id));
            ok = locacionDAO.editar(locacion);
        } else {
            ok = locacionDAO.crear(locacion);
        }

        req.setAttribute(ok ? "exito" : "error", ok ? "Locación guardada correctamente." : "No se pudo guardar la locación.");
        listar(req, resp);
    }

    private void cambiarEstado(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("idLocacion"));
            boolean activa = Boolean.parseBoolean(req.getParameter("activa"));
            boolean ok = locacionDAO.cambiarEstado(id, activa);
            req.setAttribute(ok ? "exito" : "error", ok ? "Estado de locación actualizado." : "No se pudo actualizar el estado.");
        } catch (Exception e) {
            req.setAttribute("error", "Locación inválida.");
        }
        listar(req, resp);
    }

    private String validar(Locacion locacion) {
        String errorNombre = validador.validarTexto(locacion.getNombre(), "El nombre de la locación", 4, 120);
        if (errorNombre != null) return errorNombre;
        String errorCiudad = validador.validarTexto(locacion.getCiudad(), "La ciudad", 3, 80);
        if (errorCiudad != null) return errorCiudad;
        String errorDireccion = validador.validarTexto(locacion.getDireccion(), "La dirección", 6, 180);
        if (errorDireccion != null) return errorDireccion;
        String errorResponsable = validador.validarTexto(locacion.getResponsable(), "El responsable", 4, 120);
        if (errorResponsable != null) return errorResponsable;
        if (locacion.getCapacidad() < 1 || locacion.getCapacidad() > 500) {
            return "La capacidad debe estar entre 1 y 500.";
        }
        return null;
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
