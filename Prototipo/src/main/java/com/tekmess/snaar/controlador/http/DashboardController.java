package com.tekmess.snaar.controlador.http;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.tekmess.snaar.controlador.servicio.EmpleadoServicio;
import com.tekmess.snaar.modelo.dao.EmpleadoDAO;
import com.tekmess.snaar.modelo.dao.LocacionDAO;
import com.tekmess.snaar.modelo.dao.ReporteDAO;
import com.tekmess.snaar.modelo.dao.UsuarioDAO;
import com.tekmess.snaar.modelo.entidad.Empleado;
import com.tekmess.snaar.modelo.entidad.Usuario;

@WebServlet(name = "DashboardController", urlPatterns = { "/dashboard" })
public class DashboardController extends HttpServlet {

    private EmpleadoServicio empleadoServicio;
    private ReporteDAO reporteDAO;
    private UsuarioDAO usuarioDAO;
    private LocacionDAO locacionDAO;

    @Override
    public void init() throws ServletException {
        this.empleadoServicio = new EmpleadoServicio(new EmpleadoDAO(), new UsuarioDAO());
        this.reporteDAO = new ReporteDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.locacionDAO = new LocacionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        // Cargar métricas para el dashboard
        List<Empleado> empleados = empleadoServicio.consultarEmpleados();
        int totalEmpleados = empleados != null ? empleados.size() : 0;
        List<Empleado> empleadosRecientes = empleados;
        if (empleadosRecientes != null && empleadosRecientes.size() > 5) {
            empleadosRecientes = empleadosRecientes.subList(0, 5);
        }

        int totalReportes = reporteDAO.listarTodos().size();
        int totalLocacionesActivas = locacionDAO.contarActivas();

        // Accesos fallidos del día
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date inicioDia = cal.getTime();
        int accesosFallidosHoy = usuarioDAO.contarAccesosFallidosEnPeriodo(inicioDia, new Date());
        // Conteo por rol
        Map<String, Integer> rolCounts = new HashMap<>();
        rolCounts.put("GUARDIA", 0);
        rolCounts.put("SUPERVISOR", 0);
        rolCounts.put("CENTRALISTA", 0);
        rolCounts.put("JEFE_LOGISTICA", 0);
        if (empleados != null) {
            for (Empleado e : empleados) {
                if (e.getRol() != null) {
                    String key = e.getRol().name();
                    rolCounts.put(key, rolCounts.getOrDefault(key, 0) + 1);
                }
            }
        }

        // Empleados creados por día (últimos 14 días)
        int days = 14;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM");
        List<String> dayLabels = new ArrayList<>();
        List<Integer> dayCounts = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        for (int i = days - 1; i >= 0; i--) {
            Calendar temp = (Calendar) c.clone();
            temp.add(Calendar.DAY_OF_MONTH, -i);
            Date start = temp.getTime();
            temp.add(Calendar.DAY_OF_MONTH, 1);
            Date end = temp.getTime();
            int count = 0;
            if (empleados != null) {
                for (Empleado e : empleados) {
                    Date creado = e.getFechaCreacion();
                    if (creado != null && !creado.before(start) && creado.before(end))
                        count++;
                }
            }
            dayLabels.add(df.format(start));
            dayCounts.add(count);
        }

        // Reportes por día (últimos 'days' días)
        // Obtener todos los reportes en el periodo y agruparlos por día
        Calendar startPeriodCal = (Calendar) c.clone();
        startPeriodCal.add(Calendar.DAY_OF_MONTH, -(days - 1));
        Date startPeriod = startPeriodCal.getTime();
        List<com.tekmess.snaar.modelo.entidad.Reporte> recentReports = reporteDAO.listarPorFechas(startPeriod,
                new Date());
        List<Integer> reportDayCounts = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            Calendar temp = (Calendar) c.clone();
            temp.add(Calendar.DAY_OF_MONTH, -i);
            Date start = temp.getTime();
            temp.add(Calendar.DAY_OF_MONTH, 1);
            Date end = temp.getTime();
            int rc = 0;
            if (recentReports != null) {
                for (com.tekmess.snaar.modelo.entidad.Reporte r : recentReports) {
                    Date fg = r.getFechaGeneracion();
                    if (fg != null && !fg.before(start) && fg.before(end))
                        rc++;
                }
            }
            reportDayCounts.add(rc);
        }

        // Convertir a JSON simple
        String roleLabelsJson = "[\"GUARDIA\",\"SUPERVISOR\",\"CENTRALISTA\",\"JEFE_LOGISTICA\"]";
        String roleValuesJson = "[" + rolCounts.get("GUARDIA") + "," + rolCounts.get("SUPERVISOR") + ","
                + rolCounts.get("CENTRALISTA") + "," + rolCounts.get("JEFE_LOGISTICA") + "]";
        StringBuilder daysLabelsSb = new StringBuilder("[");
        StringBuilder daysValuesSb = new StringBuilder("[");
        for (int i = 0; i < dayLabels.size(); i++) {
            if (i > 0) {
                daysLabelsSb.append(',');
                daysValuesSb.append(',');
            }
            daysLabelsSb.append('"').append(dayLabels.get(i)).append('"');
            daysValuesSb.append(dayCounts.get(i));
        }
        daysLabelsSb.append(']');
        daysValuesSb.append(']');

        // report values JSON
        StringBuilder reportsValuesSb = new StringBuilder("[");
        for (int i = 0; i < reportDayCounts.size(); i++) {
            if (i > 0)
                reportsValuesSb.append(',');
            reportsValuesSb.append(reportDayCounts.get(i));
        }
        reportsValuesSb.append(']');
        req.setAttribute("totalEmpleados", totalEmpleados);
        req.setAttribute("empleadosRecientes", empleadosRecientes);
        req.setAttribute("totalReportes", totalReportes);
        req.setAttribute("totalLocacionesActivas", totalLocacionesActivas);
        req.setAttribute("accesosFallidosHoy", accesosFallidosHoy);
        req.setAttribute("roleLabelsJson", roleLabelsJson);
        req.setAttribute("roleValuesJson", roleValuesJson);
        req.setAttribute("daysLabelsJson", daysLabelsSb.toString());
        req.setAttribute("daysValuesJson", daysValuesSb.toString());

        req.setAttribute("reportDaysValuesJson", reportsValuesSb.toString());
        // Lista de usuarios para la vista de administración
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            req.setAttribute("usuariosList", usuarios);
        } catch (Exception ex) {
            req.setAttribute("usuariosList", new java.util.ArrayList<>());
        }

        req.getRequestDispatcher("/vistas/dashboard.jsp").forward(req, resp);
    }
}
