package com.tekmess.snaar.controlador.http;

import com.tekmess.snaar.controlador.servicio.EmpleadoServicio;
import com.tekmess.snaar.modelo.dao.EmpleadoDAO;
import com.tekmess.snaar.modelo.dao.UsuarioDAO;
import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.modelo.entidad.Rol;
import com.tekmess.snaar.patron.command.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controlador HTTP para Gestión de Empleados (RF-SNAAR-01).
 * Capa de Presentación – Servlet que gestiona CRUD de empleados.
 * Integra el Patrón Command para encapsular operaciones.
 */
@WebServlet(name = "EmpleadoController", urlPatterns = {"/empleados/*"})
public class EmpleadoController extends HttpServlet {

    private EmpleadoServicio empleadoServicio;
    private InvocadorOperaciones invocador;

    @Override
    public void init() throws ServletException {
        this.empleadoServicio = new EmpleadoServicio(new EmpleadoDAO(), new UsuarioDAO());
        this.invocador = new InvocadorOperaciones();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/listar";

        switch (path) {
            case "/listar":
                listarEmpleados(req, resp);
                break;
            case "/nuevo":
                req.getRequestDispatcher("/vistas/empleados/formulario.jsp").forward(req, resp);
                break;
            case "/editar":
                mostrarEdicion(req, resp);
                break;
            default:
                listarEmpleados(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/crear";

        switch (path) {
            case "/crear":
                crearEmpleado(req, resp);
                break;
            case "/editar":
                editarEmpleado(req, resp);
                break;
            case "/eliminar":
                eliminarEmpleado(req, resp);
                break;
            case "/deshacer":
                deshacerUltimaOperacion(req, resp);
                break;
            default:
                listarEmpleados(req, resp);
        }
    }

    /**
     * RF-SNAAR-01.04: Listar personal.
     */
    private void listarEmpleados(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Empleado> empleados = empleadoServicio.consultarEmpleados();
        req.setAttribute("empleados", empleados);
        req.getRequestDispatcher("/vistas/empleados/listar.jsp").forward(req, resp);
    }

    /**
     * RF-SNAAR-01.01: Crear empleado usando Patrón Command.
     */
    private void crearEmpleado(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Empleado empleado = new Empleado();
        empleado.setCedula(req.getParameter("cedula"));
        empleado.setNombres(req.getParameter("nombres"));
        empleado.setCorreo(req.getParameter("correo"));
        empleado.setRol(Rol.valueOf(req.getParameter("rol")));

        // Usar patrón Command
        IComando comando = new CrearEmpleadoComando(empleadoServicio, empleado);
        ResultadoComando resultado = invocador.ejecutarComando(comando);

        if (resultado.isExitoso()) {
            req.setAttribute("exito", resultado.getMensaje());
        } else {
            req.setAttribute("error", resultado.getMensaje());
            req.setAttribute("empleado", empleado);
            req.getRequestDispatcher("/vistas/empleados/formulario.jsp").forward(req, resp);
            return;
        }

        listarEmpleados(req, resp);
    }

    /**
     * Muestra el formulario de edición con datos actuales.
     */
    private void mostrarEdicion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String cedula = req.getParameter("cedula");
        Empleado empleado = empleadoServicio.buscarEmpleado(cedula);

        if (empleado == null) {
            req.setAttribute("error", "El empleado seleccionado no fue encontrado en el sistema.");
            listarEmpleados(req, resp);
            return;
        }

        req.setAttribute("empleado", empleado);
        req.setAttribute("editar", true);
        req.getRequestDispatcher("/vistas/empleados/formulario.jsp").forward(req, resp);
    }

    /**
     * RF-SNAAR-01.02: Editar empleado usando Patrón Command.
     */
    private void editarEmpleado(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Empleado empleado = new Empleado();
        empleado.setCedula(req.getParameter("cedula"));
        empleado.setNombres(req.getParameter("nombres"));
        empleado.setCorreo(req.getParameter("correo"));
        empleado.setRol(Rol.valueOf(req.getParameter("rol")));

        IComando comando = new EditarEmpleadoComando(empleadoServicio, empleado);
        ResultadoComando resultado = invocador.ejecutarComando(comando);

        if (resultado.isExitoso()) {
            req.setAttribute("exito", resultado.getMensaje());
        } else {
            req.setAttribute("error", resultado.getMensaje());
        }

        listarEmpleados(req, resp);
    }

    /**
     * RF-SNAAR-01.03: Eliminar empleado usando Patrón Command.
     */
    private void eliminarEmpleado(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String cedula = req.getParameter("cedula");

        IComando comando = new EliminarEmpleadoComando(empleadoServicio, cedula);
        ResultadoComando resultado = invocador.ejecutarComando(comando);

        if (resultado.isExitoso()) {
            req.setAttribute("exito", resultado.getMensaje());
        } else {
            req.setAttribute("error", resultado.getMensaje());
        }

        listarEmpleados(req, resp);
    }

    /**
     * Deshacer última operación (Patrón Command – undo).
     */
    private void deshacerUltimaOperacion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ResultadoComando resultado = invocador.deshacerUltimo();
        req.setAttribute(resultado.isExitoso() ? "exito" : "error", resultado.getMensaje());
        listarEmpleados(req, resp);
    }
}
