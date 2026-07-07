package com.tekmess.snaar.controlador.http;

import com.tekmess.snaar.modelo.entidad.Rol;
import com.tekmess.snaar.modelo.entidad.Sesion;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/** Protege las areas privadas y centraliza los permisos por rol. */
@WebFilter(urlPatterns = { "/dashboard", "/empleados/*", "/locaciones/*", "/reportes/*", "/roles/*", "/vistas/*" })
public class AutorizacionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // Sin configuracion externa.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        Sesion sesion = (Sesion) session.getAttribute("sesionSNAAR");
        if (sesion == null || !sesion.isActiva()) {
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/auth/login?sesion=expirada");
            return;
        }

        Rol rol = sesion.getRolUsuario();
        boolean esJefe = rol == Rol.JEFE_LOGISTICA;
        boolean gestionaPersonal = esJefe || rol == Rol.SUPERVISOR;

        req.setAttribute("rolActual", rol);
        req.setAttribute("esJefeLogistica", esJefe);
        req.setAttribute("puedeGestionarPersonal", gestionaPersonal);

        String uri = req.getRequestURI().substring(req.getContextPath().length());
        if (uri.startsWith("/vistas/")) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        boolean areaRoles = uri.startsWith("/roles/");
        boolean mutacionLocaciones = uri.startsWith("/locaciones/") && !"GET".equalsIgnoreCase(req.getMethod());
        boolean mutacionPersonal = uri.startsWith("/empleados/") && (
                !"GET".equalsIgnoreCase(req.getMethod())
                || uri.endsWith("/nuevo")
                || uri.endsWith("/editar"));

        if ((areaRoles && !esJefe) || ((mutacionPersonal || mutacionLocaciones) && !gestionaPersonal)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            req.setAttribute("recursoSolicitado", areaRoles ? "Administracion de roles" : "Gestion operativa");
            req.getRequestDispatcher("/vistas/error/403.jsp").forward(req, resp);
            return;
        }

        sesion.renovar();
        chain.doFilter(request, response);
    }
}
