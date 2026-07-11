<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.tekmess.snaar.modelo.entidad.Empleado, com.tekmess.snaar.modelo.entidad.Usuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SNAAR - Panel Principal | TekMess</title>
    <!-- Fuente Inter para diseño moderno -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: linear-gradient(180deg,#f8fbff 0%, #eef3f8 100%); color: #152033; min-height: 100vh; }
        
        /* Navbar */
        .navbar { background: rgba(16, 24, 39, 0.96); backdrop-filter: blur(14px); padding: 14px 28px; display:flex; justify-content:space-between; align-items:center; border-bottom: 1px solid rgba(255,255,255,0.08); }
        .navbar h2 { font-size:18px; color:#f8fafc; font-weight: 700; }
        .nav-links a { color: #b9c6d8; text-decoration:none; margin-left:18px; font-size:14px; transition: color 0.2s; }
        .nav-links a:hover { color:#fff; }
        
        /* Estructura Principal */
        .container { max-width:1400px; margin:28px auto; padding: 0 20px; }
        .grid { display:grid; grid-template-columns: 260px 1fr; gap:24px; align-items:start; }
        
        /* Sidebar integrado básico por si falla el fragmento */
        .sidebar { background: #ffffff; border-radius:12px; padding:20px; border:1px solid #dbe4ee; }
        .sidebar h3 { font-size: 14px; text-transform: uppercase; letter-spacing: 1px; color: #2563eb; margin-bottom: 16px; }
        .sidebar-menu { list-style: none; }
        .sidebar-menu li { margin-bottom: 12px; }
        .sidebar-menu a { color: #637083; text-decoration: none; font-size: 14px; display: block; padding: 8px 12px; border-radius: 6px; transition: background 0.2s; }
        .sidebar-menu a:hover { background: #eef6ff; color: #2563eb; }

        /* Tarjetas */
        .card { background: #ffffff; border-radius:14px; padding:24px; border:1px solid #dbe4ee; box-shadow: 0 18px 45px rgba(15,23,42,0.08); margin-bottom: 24px; }
        .welcome { font-size:22px; font-weight:700; color:#152033; }
        .muted { color:#637083; font-size:14px; }
        
        /* Métricas */
        .metrics { display:grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap:16px; margin: 20px 0; }
        .metric { padding:20px; border-radius:12px; background: #ffffff; border:1px solid #dbe4ee; transition:transform .18s ease, box-shadow .18s ease; }
        .metric:hover { transform: translateY(-3px); box-shadow: 0 16px 34px rgba(15,23,42,0.10); border-color: rgba(37,99,235,0.22); }
        .metric h5 { font-size:12px; color:#637083; margin-bottom:8px; text-transform: uppercase; letter-spacing: 0.5px; }
        .metric .value { font-size:28px; font-weight:800; color:#0f172a; }
        
        /* Gráficos */
        .charts-container { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 24px; }
        @media (max-width: 1024px) { .charts-container { grid-template-columns: 1fr; } }
        .chart-box { background: #ffffff; border-radius: 14px; padding: 18px; border: 1px solid #dbe4ee; box-shadow: 0 14px 34px rgba(15,23,42,.07); }
        .chart-box h4 { font-size: 15px; margin-bottom: 12px; color: #152033; }

        /* Tablas y Listas */
        .table-responsive { width: 100%; overflow-x: auto; margin-top: 12px; }
        table { width: 100%; border-collapse: collapse; text-align: left; font-size: 14px; }
        th { padding: 12px; color: #475569; font-weight: 600; border-bottom: 1px solid #dbe4ee; background:#f8fafc; }
        td { padding: 12px; border-bottom: 1px solid #edf2f7; color: #334155; }
        tr:hover td { background: #f8fbff; }

        .list-card { display:flex; flex-direction:column; gap:10px; }
        .empleado-row { display:flex; justify-content:space-between; align-items:center; padding:12px; border-radius:10px; background: #f8fafc; border: 1px solid #e2e8f0; }
        .empleado-name { font-weight:600; color:#152033; }
        .small { font-size:13px; color:#637083; }
        
        /* Botones y Estados */
        .btn { padding:8px 16px; border-radius:8px; font-size: 13px; font-weight:600; background: linear-gradient(135deg,#2563eb,#1d4ed8); color:#fff; text-decoration:none; display: inline-block; border: none; cursor: pointer; transition: opacity 0.2s; }
        .btn:hover { opacity: 0.9; }
        .btn-sm { padding: 5px 10px; font-size: 12px; border-radius: 6px; }
        .badge { padding: 3px 8px; border-radius: 20px; font-size: 11px; font-weight: 600; }
        .badge-danger { background: #fee4e2; color: #b42318; border: 1px solid #fecdca; }
        .badge-success { background: #dcfae6; color: #16803c; border: 1px solid #abefc6; }
        .empty { text-align:center; color:#64748b; padding:30px; font-size: 14px; }

        /* Responsive */
        @media (max-width: 900px) {
            .grid { grid-template-columns: 1fr; }
            .sidebar { display: none; } /* En móvil se puede usar la navbar */
        }
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=contrast-darkmode-20260711">
</head>
<body class="app-page dashboard-page">

    <jsp:include page="/vistas/fragments/navbar.jsp" />
    <nav class="navbar legacy-navbar">
        <h2>SNAAR — TekMess</h2>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/empleados/listar">Empleados</a>
            <a href="${pageContext.request.contextPath}/reportes">Reportes</a>
            <a href="${pageContext.request.contextPath}/auth/cambiar-contrasena">Contraseña</a>
            <a href="${pageContext.request.contextPath}/auth/logout" style="color: #b42318;">Cerrar Sesión</a>
        </div>
    </nav>

    <div class="container">
        <div class="grid">
            
            <!-- SIDEBAR -->
            <aside class="sidebar legacy-sidebar">
                <h3>Navegación</h3>
                <ul class="sidebar-menu">
                    <li><a href="${pageContext.request.contextPath}/dashboard">🏠 Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/empleados/listar">👥 Gestión Empleados</a></li>
                    <li><a href="${pageContext.request.contextPath}/reportes">📊 Reportes del Sistema</a></li>
                    <li><a href="${pageContext.request.contextPath}/auth/cambiar-contrasena">🔒 Seguridad</a></li>
                </ul>
            </aside>
            
            <!-- PANEL CONTENIDO PRINCIPAL -->
            <main>
                <!-- Mensaje de Bienvenida -->
                <div class="card">
                    <p class="eyebrow">Centro de operaciones</p>
                    <div class="welcome">Hola, <span style="color:#2563eb;"><%= session.getAttribute("usuario") %></span></div>
                    <div class="muted">Rol actual: <strong style="color: #0f766e;"><%= session.getAttribute("rol") %></strong></div>
                    
                    <% if (request.getAttribute("exito") != null) { %>
                        <div style="color:#16803c; padding:10px 0 0 0; font-size: 14px;">✔ <%= request.getAttribute("exito") %></div>
                    <% } %>

                    <!-- MÉTRICAS INYECTADAS DESDE EL CONTROLADOR -->
                    <div class="metrics">
                        <div class="metric">
                            <h5>Total Empleados</h5>
                            <div class="value"><%= request.getAttribute("totalEmpleados") != null ? request.getAttribute("totalEmpleados") : 0 %></div>
                        </div>
                        <div class="metric">
                            <h5>Total Reportes</h5>
                            <div class="value"><%= request.getAttribute("totalReportes") != null ? request.getAttribute("totalReportes") : 0 %></div>
                        </div>
                        <div class="metric">
                            <h5>Accesos Fallidos (Hoy)</h5>
                            <div class="value" style="color: #b42318;"><%= request.getAttribute("accesosFallidosHoy") != null ? request.getAttribute("accesosFallidosHoy") : 0 %></div>
                        </div>
                    </div>
                </div>

                <!-- SECCIÓN DE GRÁFICOS (DINÁMICOS) -->
                <div class="charts-container">
                    <div class="chart-box">
                        <h4>Distribución por Roles (Empleados)</h4>
                        <div style="position: relative; height:220px;">
                            <canvas id="rolesChart"></canvas>
                        </div>
                    </div>
                    <div class="chart-box">
                        <h4>Registros y Reportes (Últimos 14 días)</h4>
                        <div style="position: relative; height:220px;">
                            <canvas id="trendsChart"></canvas>
                        </div>
                    </div>
                </div>

                <!-- SECCIÓN: DOS COLUMNAS (EMPLEADOS RECIENTES Y SEGUIMIENTO DE USUARIOS) -->
                <div class="split-panels">
                    
                    <!-- Tarjeta: Empleados Recientes -->
                    <div class="card" style="margin-bottom: 0;">
                        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px;">
                            <h4 style="font-size: 16px;">Empleados Recientes</h4>
                            <a class="btn btn-sm" href="${pageContext.request.contextPath}/empleados/listar">Ver todos</a>
                        </div>
                        <div class="list-card">
                            <%
                                List<Empleado> recientes = (List<Empleado>) request.getAttribute("empleadosRecientes");
                                if (recientes == null || recientes.isEmpty()) {
                            %>
                                <div class="empty">No hay empleados registrados.</div>
                            <% } else { %>
                                <% for (Empleado e : recientes) { %>
                                    <div class="empleado-row">
                                        <div>
                                            <div class="empleado-name"><%= e.getNombres() %></div>
                                            <div class="small"><%= e.getCedula() %> • <span style="color:#0f766e;"><%= e.getRol() %></span></div>
                                        </div>
                                        <div>
                                            <% if (Boolean.TRUE.equals(request.getAttribute("puedeGestionarPersonal"))) { %>
                                                <a class="btn btn-sm btn-secondary" href="${pageContext.request.contextPath}/empleados/editar?cedula=<%= e.getCedula() %>">Editar</a>
                                            <% } %>
                                        </div>
                                    </div>
                                <% } %>
                            <% } %>
                        </div>
                    </div>

                    <!-- Tarjeta: Tabla de Usuarios del Sistema (Para Auditoría / Control) -->
                    <% if (Boolean.TRUE.equals(request.getAttribute("puedeGestionarPersonal"))) { %>
                    <div class="card" style="margin-bottom: 0;">
                        <h4 style="font-size: 16px; margin-bottom: 14px;">Auditoría de Usuarios del Sistema</h4>
                        <div class="table-responsive">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Usuario</th>
                                        <th>Estado / Intentos</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        List<Usuario> usuariosList = (List<Usuario>) request.getAttribute("usuariosList");
                                        if (usuariosList == null || usuariosList.isEmpty()) {
                                    %>
                                        <tr>
                                            <td colspan="2" class="empty">No hay usuarios del sistema para mostrar.</td>
                                        </tr>
                                    <% } else { %>
                                        <% 
                                            // Renderizar máximo 4 para no romper la estética simétrica del panel
                                            int max = Math.min(usuariosList.size(), 4);
                                            for(int i=0; i<max; i++) { 
                                                Usuario u = usuariosList.get(i);
                                        %>
                                            <tr>
                                                <td>
                                                    <strong style="color: #fff;"><%= u.getNombreUsuario() %></strong>
                                                </td>
                                                <td>
                                                    <% if(u.getIntentosFallidos() >= 3) { %>
                                                        <span class="badge badge-danger">Bloqueado u Observado (<%= u.getIntentosFallidos() %>)</span>
                                                    <% } else { %>
                                                        <span class="badge badge-success">Activo (Intentos: <%= u.getIntentosFallidos() %>)</span>
                                                    <% } %>
                                                </td>
                                            </tr>
                                        <% } %>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <% } %>
                </div>

            </main>
        </div>
    </div>

    <!-- SCRIPTS GRÁFICOS: Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
        // Datos Inyectados desde DashboardController de forma segura
       // Datos desde el servidor convertidos correctamente a objetos JS
var roleLabels = JSON.parse('<%= request.getAttribute("roleLabelsJson") != null ? request.getAttribute("roleLabelsJson") : "[]" %>');
var roleValues = JSON.parse('<%= request.getAttribute("roleValuesJson") != null ? request.getAttribute("roleValuesJson") : "[]" %>');
var daysLabels = JSON.parse('<%= request.getAttribute("daysLabelsJson") != null ? request.getAttribute("daysLabelsJson") : "[]" %>');
var daysValues = JSON.parse('<%= request.getAttribute("daysValuesJson") != null ? request.getAttribute("daysValuesJson") : "[]" %>');
var reportDaysValues = JSON.parse('<%= request.getAttribute("reportDaysValuesJson") != null ? request.getAttribute("reportDaysValuesJson") : "[]" %>');
var chartDark = document.documentElement.classList.contains('dark-mode');
var chartText = chartDark ? '#dbe4ee' : '#344054';
var chartMuted = chartDark ? '#a7b2c3' : '#667085';
var chartGrid = chartDark ? 'rgba(219, 228, 238, 0.16)' : 'rgba(148, 163, 184, 0.22)';
        // 1. Gráfico de Pastel: Distribución de Roles
        var ctxR = document.getElementById('rolesChart').getContext('2d');
        new Chart(ctxR, {
            type: 'bar',
            data: {
                labels: roleLabels,
                datasets: [{
                    label: 'Empleados',
                    data: roleValues,
                    backgroundColor: 'rgba(31, 111, 235, 0.72)',
                    hoverBackgroundColor: '#1f6feb',
                    borderWidth: 0,
                    borderRadius: 8,
                    barThickness: 20
                }]
            },
            options: {
                responsive: true,
                indexAxis: 'y',
                maintainAspectRatio: false,
                scales: {
                    x: { beginAtZero: true, grid: { color: chartGrid }, ticks: { color: chartMuted, precision: 0 } },
                    y: { grid: { display: false }, ticks: { color: chartText, font: { family: 'Inter', size: 12, weight: '600' } } }
                },
                plugins: {
                    legend: {
                        display: false,
                        labels: {
                            color: chartText,
                            usePointStyle: true,
                            pointStyle: 'circle',
                            padding: 18,
                            font: { family: 'Inter', size: 12, weight: '600' }
                        }
                    },
                    tooltip: { backgroundColor: '#111827', titleColor: '#ffffff', bodyColor: '#eef2ff', padding: 10, cornerRadius: 8 }
                }
            }
        });

        // 2. Gráfico Combinado Lineal/Barras: Tendencias de Empleados Creados vs Reportes Generados
        var ctxT = document.getElementById('trendsChart').getContext('2d');
        new Chart(ctxT, {
            data: {
                labels: daysLabels,
                datasets: [
                    {
                        type: 'line',
                        label: 'Empleados Creados',
                        data: daysValues,
                        borderColor: '#1f6feb',
                        backgroundColor: 'rgba(31, 111, 235, 0.08)',
                        pointBackgroundColor: '#ffffff',
                        pointBorderColor: '#1f6feb',
                        pointBorderWidth: 2,
                        pointRadius: 4,
                        pointHoverRadius: 6,
                        borderWidth: 3,
                        tension: 0.38,
                        fill: true
                    },
                    {
                        type: 'bar',
                        label: 'Reportes Generados',
                        data: reportDaysValues,
                        backgroundColor: 'rgba(15, 118, 110, 0.58)',
                        hoverBackgroundColor: '#0f766e',
                        borderColor: '#0f766e',
                        borderWidth: 1,
                        borderRadius: 8,
                        barThickness: 18
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    x: { grid: { display: false }, ticks: { color: chartMuted, font: { family: 'Inter', size: 11 } } },
                    y: { grid: { color: chartGrid }, ticks: { color: chartMuted, font: { family: 'Inter', size: 11 }, precision: 0 }, beginAtZero: true }
                },
                plugins: {
                    legend: {
                        labels: {
                            color: chartText,
                            usePointStyle: true,
                            pointStyle: 'circle',
                            padding: 16,
                            font: { family: 'Inter', size: 12, weight: '600' }
                        }
                    },
                    tooltip: {
                        backgroundColor: '#0f172a',
                        titleColor: '#ffffff',
                        bodyColor: '#dbeafe',
                        padding: 12,
                        cornerRadius: 10
                    }
                }
            }
        });
    </script>
</body>
</html>
