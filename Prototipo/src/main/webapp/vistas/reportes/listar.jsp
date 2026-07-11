<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SNAAR - Reportes | TekMess</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #0f172a; color: #e2e8f0; }
        .navbar {
            background: rgba(15, 23, 42, 0.95);
            backdrop-filter: blur(10px);
            padding: 16px 32px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid rgba(99, 102, 241, 0.15);
        }
        .navbar h2 { font-size: 20px; color: #111827; }
        .nav-links a { margin-left: 18px; color: #94a3b8; text-decoration: none; font-size: 14px; }
        .nav-links a:hover { color: #1f6feb; }
        .container { max-width: 1200px; margin: 30px auto; padding: 0 24px; }
        .card { background: rgba(15, 23, 42, 0.8); border: 1px solid rgba(99, 102, 241, 0.15); border-radius: 20px; padding: 26px; margin-bottom: 24px; }
        .header { display: flex; justify-content: space-between; align-items: center; gap: 12px; flex-wrap: wrap; }
        .header h1 { font-size: 24px; }
        .btn { display: inline-flex; align-items: center; justify-content: center; padding: 12px 20px; border-radius: 12px; font-size: 14px; font-weight: 600; cursor: pointer; border: none; }
        .btn-primary { background: #1f6feb; color: #fff; }
        .btn-secondary { background: rgba(148, 163, 184, 0.12); color: #cbd5e1; }
        .btn-secondary:hover { background: rgba(148, 163, 184, 0.2); }
        .filters { display: grid; grid-template-columns: repeat(auto-fit,minmax(200px,1fr)); gap: 16px; margin: 22px 0; }
        .filters label { display: block; color: #94a3b8; margin-bottom: 8px; font-size: 13px; }
        .filters input { width: 100%; padding: 12px 14px; border-radius: 12px; border: 1px solid rgba(148, 163, 184, 0.2); background: #111827; color: #e2e8f0; }
        .alert { padding: 14px 18px; border-radius: 12px; margin-bottom: 18px; font-size: 13px; }
        .alert-error { background: rgba(248, 113, 113, 0.12); border: 1px solid rgba(248, 113, 113, 0.35); color: #fecaca; }
        .alert-success { background: rgba(34, 197, 94, 0.12); border: 1px solid rgba(34, 197, 94, 0.35); color: #bbf7d0; }
        .table-card { overflow-x: auto; border-radius: 20px; }
        table { width: 100%; border-collapse: collapse; min-width: 920px; }
        th, td { padding: 16px 18px; text-align: left; }
        th { color: #94a3b8; font-size: 12px; text-transform: uppercase; letter-spacing: 0.08em; background: rgba(99, 102, 241, 0.08); }
        tr { border-top: 1px solid rgba(148, 163, 184, 0.1); }
        tr:hover { background: rgba(99, 102, 241, 0.08); }
        .badge { display: inline-flex; padding: 6px 10px; border-radius: 999px; font-size: 12px; font-weight: 600; }
        .badge-primary { background: rgba(99, 102, 241, 0.16); color: #c7d2fe; }
        .empty-state { text-align: center; padding: 40px 0; color: #94a3b8; }
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=contrast-darkmode-20260711">
</head>
<body class="app-page reports-page">
    <jsp:include page="/vistas/fragments/navbar.jsp" />
    <nav class="navbar legacy-navbar">
        <h2>Reportes SNAAR</h2>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/empleados/listar">Empleados</a>
            <a href="${pageContext.request.contextPath}/auth/cambiar-contrasena">Contraseña</a>
            <a href="${pageContext.request.contextPath}/auth/logout">Cerrar Sesión</a>
        </div>
    </nav>

    <div class="container">
        <div class="card">
            <div class="header">
                <div>
                    <p class="eyebrow">Inteligencia operativa</p>
                    <h1>Reportes y auditoria</h1>
                    <p style="color:#94a3b8; margin-top:6px;">Genera y consulta reportes analíticos sobre empleados y accesos fallidos.</p>
                </div>
                <div style="display:flex; gap:12px; flex-wrap:wrap;">
                    <a href="${pageContext.request.contextPath}/reportes/exportar?fechaInicio=${fechaInicio}&fechaFin=${fechaFin}" class="btn btn-secondary">Exportar CSV</a>
                    <button type="button" class="btn btn-secondary" onclick="window.print()">Imprimir reporte</button>
                    <a href="${pageContext.request.contextPath}/reportes" class="btn btn-secondary">Actualizar vista</a>
                </div>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            <c:if test="${not empty exito}">
                <div class="alert alert-success">${exito}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/reportes/generar" method="POST" data-validate>
                <div class="filters">
                    <div>
                        <label for="fechaInicio">Fecha de inicio</label>
                        <input type="date" id="fechaInicio" name="fechaInicio" value="${fechaInicio}" required>
                    </div>
                    <div>
                        <label for="fechaFin">Fecha de fin</label>
                        <input type="date" id="fechaFin" name="fechaFin" value="${fechaFin}" required>
                    </div>
                    <div style="align-self:end; display:flex; gap:12px;">
                        <button type="submit" class="btn btn-primary">Generar Reporte</button>
                    </div>
                </div>
            </form>
        </div>

        <div class="card table-card">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Generado</th>
                        <th>Período</th>
                        <th>Creados</th>
                        <th>Editados</th>
                        <th>Eliminados</th>
                        <th>Accesos fallidos</th>
                        <th>Generado por</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="reporte" items="${reportes}">
                        <tr>
                            <td>${reporte.idReporte}</td>
                            <td>${reporte.fechaGeneracion}</td>
                            <td>${reporte.fechaInicio} - ${reporte.fechaFin}</td>
                            <td>${reporte.totalEmpleadosCreados}</td>
                            <td>${reporte.totalEmpleadosEditados}</td>
                            <td>${reporte.totalEmpleadosEliminados}</td>
                            <td>${reporte.totalAccesosFallidos}</td>
                            <td><span class="badge badge-primary">${reporte.generadoPor}</span></td>
                            <td><a href="${pageContext.request.contextPath}/reportes/ver?id=${reporte.idReporte}" class="btn btn-secondary">Ver</a></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty reportes}">
                        <tr>
                            <td colspan="9" class="empty-state">No hay reportes registrados para el período seleccionado.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/validaciones.js"></script>
</body>
</html>
