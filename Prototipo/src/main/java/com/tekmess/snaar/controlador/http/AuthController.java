package com.tekmess.snaar.controlador.http;

import com.tekmess.snaar.controlador.servicio.AuthServicio;
import com.tekmess.snaar.modelo.dao.UsuarioDAO;
import com.tekmess.snaar.modelo.entidad.Sesion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controlador HTTP para Autenticación (RF-SNAAR-02).
 * Capa de Presentación – Servlet que gestiona login, cambio y recuperación de contraseña.
 */
@WebServlet(name = "AuthController", urlPatterns = {"/auth/*"})
public class AuthController extends HttpServlet {

    private AuthServicio authServicio;

    @Override
    public void init() throws ServletException {
        this.authServicio = new AuthServicio(new UsuarioDAO());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/login";

        switch (path) {
            case "/login":
                try {
                    req.getRequestDispatcher("/vistas/login.jsp").forward(req, resp);
                } catch (Exception ex) {
                    // Fallback: render a minimal login form if JSP fails to compile/load
                    resp.setContentType("text/html;charset=UTF-8");
                    java.io.PrintWriter out = resp.getWriter();
                    String error = req.getParameter("error") != null ? req.getParameter("error") : (String) req.getAttribute("error");
                    out.println("<!doctype html><html><head><meta charset=\"utf-8\"><title>Login</title></head><body style=\"font-family:Arial,sans-serif;background:#0f172a;color:#e2e8f0;display:flex;align-items:center;justify-content:center;height:100vh;\">");
                    out.println("<form method=\"post\" action=\"" + req.getContextPath() + "/auth/login\" style=\"background:#112233;padding:24px;border-radius:8px;\">\n<h2 style=\"margin:0 0 8px;color:#fff\">SNAAR - Iniciar Sesión</h2>");
                    if (error != null) out.println("<div style=\"color:#fca5a5;margin-bottom:8px;\">" + error + "</div>");
                    out.println("<div><label>Usuario</label><br/><input name=\"usuario\" required style=\"padding:8px;margin-top:4px;width:100%\"/></div>");
                    out.println("<div style=\"margin-top:8px\"><label>Contraseña</label><br/><input type=\"password\" name=\"contrasena\" required style=\"padding:8px;margin-top:4px;width:100%\"/></div>");
                    out.println("<div style=\"margin-top:12px;text-align:right\"><button style=\"padding:8px 12px;background:#6366f1;color:#fff;border:none;border-radius:6px;\">Entrar</button></div>");
                    out.println("</form></body></html>");
                    out.flush();
                }
                break;
            case "/cambiar-contrasena":
                req.getRequestDispatcher("/vistas/cambiarContrasena.jsp").forward(req, resp);
                break;
            case "/recuperar":
                req.getRequestDispatcher("/vistas/recuperarContrasena.jsp").forward(req, resp);
                break;
            case "/logout":
                cerrarSesion(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/auth/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/login";

        switch (path) {
            case "/login":
                procesarLogin(req, resp);
                break;
            case "/cambiar-contrasena":
                procesarCambioContrasena(req, resp);
                break;
            case "/recuperar":
                procesarRecuperacion(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/auth/login");
        }
    }

    /**
     * Procesa el inicio de sesión (RF-SNAAR-02.01).
     */
    private void procesarLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String usuario = req.getParameter("usuario");
        String contrasena = req.getParameter("contrasena");

        Object[] resultado = authServicio.iniciarSesion(usuario, contrasena);
        Sesion sesion = (Sesion) resultado[0];
        String mensaje = (String) resultado[1];

        if (sesion == null) {
            // Login fallido
            req.setAttribute("error", mensaje);
            req.getRequestDispatcher("/vistas/login.jsp").forward(req, resp);
            return;
        }

        // Login exitoso: almacenar sesión en HttpSession
        HttpSession httpSession = req.getSession(true);
        httpSession.setAttribute("sesionSNAAR", sesion);
        httpSession.setAttribute("usuario", sesion.getNombreUsuario());
        httpSession.setAttribute("rol", sesion.getRolUsuario());
        httpSession.setMaxInactiveInterval(120 * 60); // 120 minutos

        if ("PRIMER_ACCESO".equals(mensaje)) {
            // Redirigir a cambio de contraseña obligatorio
            resp.sendRedirect(req.getContextPath() + "/auth/cambiar-contrasena?primer=true");
        } else {
            // Redirigir según rol
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        }
    }

    /**
     * Procesa el cambio de contraseña (RF-SNAAR-02.02).
     */
    private void procesarCambioContrasena(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession httpSession = req.getSession(false);
        if (httpSession == null || httpSession.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String usuario = (String) httpSession.getAttribute("usuario");
        String actual = req.getParameter("contrasenaActual");
        String nueva = req.getParameter("contrasenaNueva");
        String confirmacion = req.getParameter("confirmacion");

        String resultado = authServicio.cambiarContrasena(usuario, actual, nueva, confirmacion);

        if (resultado.contains("exitosamente")) {
            req.setAttribute("exito", resultado);
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            req.setAttribute("error", resultado);
            req.getRequestDispatcher("/vistas/cambiarContrasena.jsp").forward(req, resp);
        }
    }

    /**
     * Procesa la recuperación de contraseña (RF-SNAAR-02.03).
     */
    private void procesarRecuperacion(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String correo = req.getParameter("correo");
        String usuario = req.getParameter("usuario");
        String nueva = req.getParameter("contrasenaNueva");
        String confirmacion = req.getParameter("confirmacion");

        String resultado = authServicio.recuperarContrasena(correo, usuario, nueva, confirmacion);

        if (resultado.contains("exitosamente")) {
            req.setAttribute("exito", resultado);
            req.getRequestDispatcher("/vistas/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", resultado);
            req.getRequestDispatcher("/vistas/recuperarContrasena.jsp").forward(req, resp);
        }
    }

    /**
     * Cierra la sesión activa.
     */
    private void cerrarSesion(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            Sesion sesion = (Sesion) httpSession.getAttribute("sesionSNAAR");
            authServicio.cerrarSesion(sesion);
            httpSession.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/auth/login");
    }
}
