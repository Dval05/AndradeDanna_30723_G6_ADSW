package com.tekmess.snaar.controlador.http;

import com.tekmess.snaar.controlador.servicio.EmpleadoServicio;
import com.tekmess.snaar.modelo.dao.EmpleadoDAO;
import com.tekmess.snaar.modelo.dao.LocacionDAO;
import com.tekmess.snaar.modelo.dao.UsuarioDAO;
import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.modelo.entidad.Rol;
import com.tekmess.snaar.modelo.entidad.Usuario;
import com.tekmess.snaar.patron.command.*;
import com.tekmess.snaar.util.ValidadorDatos;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Locale;

/**
 * Controlador HTTP para Gestión de Empleados (RF-SNAAR-01).
 * Capa de Presentación – Servlet que gestiona CRUD de empleados.
 * Integra el Patrón Command para encapsular operaciones.
 */
@WebServlet(name = "EmpleadoController", urlPatterns = {"/empleados/*"})
public class EmpleadoController extends HttpServlet {

    private EmpleadoServicio empleadoServicio;
    private UsuarioDAO usuarioDAO;
    private LocacionDAO locacionDAO;
    private InvocadorOperaciones invocador;
    private ValidadorDatos validador;

    @Override
    public void init() throws ServletException {
        this.usuarioDAO = new UsuarioDAO();
        this.locacionDAO = new LocacionDAO();
        this.empleadoServicio = new EmpleadoServicio(new EmpleadoDAO(), usuarioDAO);
        this.invocador = new InvocadorOperaciones();
        this.validador = new ValidadorDatos();
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
                req.setAttribute("locaciones", locacionDAO.listarActivas());
                req.getRequestDispatcher("/vistas/empleados/formulario.jsp").forward(req, resp);
                break;
            case "/editar":
                mostrarEdicion(req, resp);
                break;
            case "/exportar":
                exportarCsv(req, resp);
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
            case "/reset-password":
                regenerarContrasenaTemporal(req, resp);
                break;
            case "/desbloquear":
                desbloquearCuenta(req, resp);
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
        String busqueda = limpiar(req.getParameter("q"));
        String rolFiltro = limpiar(req.getParameter("rol"));
        String locacionFiltro = limpiar(req.getParameter("locacion"));
        List<Empleado> empleados = filtrarEmpleados(empleadoServicio.consultarEmpleados(), busqueda, rolFiltro, locacionFiltro);
        req.setAttribute("empleados", empleados);
        req.setAttribute("usuariosPorCedula", obtenerUsuariosPorCedula());
        req.setAttribute("locaciones", locacionDAO.listarActivas());
        req.setAttribute("q", busqueda);
        req.setAttribute("rolFiltro", rolFiltro);
        req.setAttribute("locacionFiltro", locacionFiltro);
        req.getRequestDispatcher("/vistas/empleados/listar.jsp").forward(req, resp);
    }

    private List<Empleado> filtrarEmpleados(List<Empleado> empleados, String busqueda, String rolFiltro, String locacionFiltro) {
        List<Empleado> filtrados = new ArrayList<>();
        String q = busqueda == null ? "" : busqueda.toLowerCase(Locale.ROOT);
        String rol = rolFiltro == null ? "" : rolFiltro;
        String locacion = locacionFiltro == null ? "" : locacionFiltro;

        for (Empleado empleado : empleados) {
            boolean coincideTexto = q.isBlank()
                    || empleado.getCedula().toLowerCase(Locale.ROOT).contains(q)
                    || empleado.getNombres().toLowerCase(Locale.ROOT).contains(q)
                    || empleado.getCorreo().toLowerCase(Locale.ROOT).contains(q);
            boolean coincideRol = rol.isBlank()
                    || (empleado.getRol() != null && empleado.getRol().name().equals(rol));
            boolean coincideLocacion = locacion.isBlank()
                    || (empleado.getIdLocacion() != null && String.valueOf(empleado.getIdLocacion()).equals(locacion));

            if (coincideTexto && coincideRol && coincideLocacion) {
                filtrados.add(empleado);
            }
        }
        return filtrados;
    }

    private Map<String, Usuario> obtenerUsuariosPorCedula() {
        Map<String, Usuario> usuariosPorCedula = new HashMap<>();
        for (Usuario usuario : usuarioDAO.listarTodos()) {
            usuariosPorCedula.put(usuario.getCedula(), usuario);
        }
        return usuariosPorCedula;
    }

    private void exportarCsv(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String busqueda = limpiar(req.getParameter("q"));
        String rolFiltro = limpiar(req.getParameter("rol"));
        String locacionFiltro = limpiar(req.getParameter("locacion"));
        List<Empleado> empleados = filtrarEmpleados(empleadoServicio.consultarEmpleados(), busqueda, rolFiltro, locacionFiltro);
        Map<String, Usuario> usuarios = obtenerUsuariosPorCedula();

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/csv;charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"personal-snaar.csv\"");

        try (java.io.PrintWriter out = resp.getWriter()) {
            out.println("Cedula,Nombres,Rol,Locacion,Correo,Usuario,Estado cuenta,Intentos fallidos,Primer acceso");
            for (Empleado empleado : empleados) {
                Usuario usuario = usuarios.get(empleado.getCedula());
                out.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        csv(empleado.getCedula()),
                        csv(empleado.getNombres()),
                        csv(empleado.getRol()),
                        csv(empleado.getNombreLocacion()),
                        csv(empleado.getCorreo()),
                        csv(usuario != null ? usuario.getNombreUsuario() : ""),
                        csv(usuario != null ? usuario.getEstadoCuenta() : ""),
                        csv(usuario != null ? usuario.getIntentosFallidos() : ""),
                        csv(usuario != null && usuario.isPrimerAcceso() ? "SI" : "NO"));
            }
        }
    }

    private String csv(Object valor) {
        String texto = valor == null ? "" : String.valueOf(valor);
        return "\"" + texto.replace("\"", "\"\"") + "\"";
    }

    /**
     * RF-SNAAR-01.01: Crear empleado usando Patrón Command.
     */
    private void crearEmpleado(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Empleado empleado = new Empleado();
        String errorCarga = cargarEmpleadoDesdeRequest(req, empleado);
        if (errorCarga != null) {
            req.setAttribute("error", errorCarga);
            req.setAttribute("empleado", empleado);
            req.setAttribute("locaciones", locacionDAO.listarActivas());
            req.getRequestDispatcher("/vistas/empleados/formulario.jsp").forward(req, resp);
            return;
        }

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
        req.setAttribute("locaciones", locacionDAO.listarActivas());
        req.getRequestDispatcher("/vistas/empleados/formulario.jsp").forward(req, resp);
    }

    /**
     * RF-SNAAR-01.02: Editar empleado usando Patrón Command.
     */
    private void editarEmpleado(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Empleado empleado = new Empleado();
        String errorCarga = cargarEmpleadoDesdeRequest(req, empleado);
        if (errorCarga != null) {
            req.setAttribute("error", errorCarga);
            req.setAttribute("empleado", empleado);
            req.setAttribute("editar", true);
            req.setAttribute("locaciones", locacionDAO.listarActivas());
            req.getRequestDispatcher("/vistas/empleados/formulario.jsp").forward(req, resp);
            return;
        }

        IComando comando = new EditarEmpleadoComando(empleadoServicio, empleado);
        ResultadoComando resultado = invocador.ejecutarComando(comando);

        if (resultado.isExitoso()) {
            req.setAttribute("exito", resultado.getMensaje());
        } else {
            req.setAttribute("error", resultado.getMensaje());
        }

        if (resultado.isExitoso()) {
            listarEmpleados(req, resp);
        } else {
            req.setAttribute("empleado", empleado);
            req.setAttribute("editar", true);
            req.setAttribute("locaciones", locacionDAO.listarActivas());
            req.getRequestDispatcher("/vistas/empleados/formulario.jsp").forward(req, resp);
        }
    }

    /**
     * RF-SNAAR-01.03: Eliminar empleado usando Patrón Command.
     */
    private void eliminarEmpleado(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String cedula = req.getParameter("cedula");
        String errorCedula = validador.validarCedula(cedula);
        if (errorCedula != null) {
            req.setAttribute("error", errorCedula);
            listarEmpleados(req, resp);
            return;
        }

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

    private void regenerarContrasenaTemporal(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String resultado = empleadoServicio.regenerarContrasenaTemporal(req.getParameter("cedula"));
        req.setAttribute(resultado.startsWith("Contraseña temporal") ? "exito" : "error", resultado);
        listarEmpleados(req, resp);
    }

    private void desbloquearCuenta(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String resultado = empleadoServicio.desbloquearCuenta(req.getParameter("cedula"));
        req.setAttribute(resultado.startsWith("Cuenta desbloqueada") ? "exito" : "error", resultado);
        listarEmpleados(req, resp);
    }

    private String cargarEmpleadoDesdeRequest(HttpServletRequest req, Empleado empleado) {
        empleado.setCedula(limpiar(req.getParameter("cedula")));
        empleado.setNombres(validador.normalizarEspacios(req.getParameter("nombres")));
        empleado.setCorreo(limpiar(req.getParameter("correo")));
        String locacionParam = limpiar(req.getParameter("idLocacion"));
        if (locacionParam != null && !locacionParam.isBlank()) {
            try {
                int idLocacion = Integer.parseInt(locacionParam);
                if (locacionDAO.buscarPorId(idLocacion) == null) {
                    return "La locación seleccionada no existe.";
                }
                empleado.setIdLocacion(idLocacion);
            } catch (NumberFormatException e) {
                return "La locación seleccionada no es válida.";
            }
        }

        String rolParam = limpiar(req.getParameter("rol"));
        String errorRol = validador.validarRol(rolParam);
        if (errorRol != null) {
            return errorRol;
        }
        empleado.setRol(Rol.valueOf(rolParam));

        List<String> errores = validador.validarDatosEmpleado(empleado);
        return errores.isEmpty() ? null : errores.get(0);
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
