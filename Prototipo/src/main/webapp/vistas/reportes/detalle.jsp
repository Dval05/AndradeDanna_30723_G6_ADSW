<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SNAAR - Detalle de Reporte | TekMess</title>
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
        .nav-links a { margin-left: 18px; color: #94a3b8; text-decoration: none; font-size: 14px; }
        .nav-links a:hover { color: #818cf8; }
        .container { max-width: 1000px; margin: 30px auto; padding: 0 24px; }
        .card { background: rgba(15, 23, 42, 0.8); border: 1px solid rgba(99, 102, 241, 0.15); border-radius: 20px; padding: 28px; margin-bottom: 24px; }
        .header { display: flex; justify-content: space-between; align-items: flex-start; gap: 16px; flex-wrap: wrap; }
        .header h1 { font-size: 24px; }
        .btn { display: inline-flex; align-items: center; justify-content: center; padding: 12px 18px; border-radius: 12px; font-size: 14px; font-weight: 600; cursor: pointer; border: none; }
        .btn-secondary { background: rgba(148, 163, 184, 0.12); color: #cbd5e1; }
        .stats { display: grid; grid-template-columns: repeat(auto-fit,minmax(220px,1fr)); gap: 16px; margin-top: 24px; }
        .stat { background: rgba(30, 41, 59, 0.7); border-radius: 16px; padding: 20px; }
        .stat h3 { font-size: 14px; color: #94a3b8; margin-bottom: 12px; }
        .stat p { font-size: 28px; font-weight: 700; color: #fff; }
        .section { margin-top: 24px; }
        .section h2 { font-size: 18px; margin-bottom: 16px; }
        .detail-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
        .info-card { background: rgba(30, 41, 59, 0.7); border-radius: 16px; padding: 18px; }
        .info-card p { color: #cbd5e1; font-size: 14px; line-height: 1.7; }
        .list { list-style: none; padding: 0; }
        .list li { padding: 14px 18px; border-bottom: 1px solid rgba(148, 163, 184, 0.08); }
        .list li:last-child { border-bottom: none; }
        .form-group { margin-bottom: 16px; }
        .form-group label { display: block; margin-bottom: 8px; color: #94a3b8; font-size: 13px; }
        .form-group textarea { width: 100%; min-height: 120px; border-radius: 14px; border: 1px solid rgba(148, 163, 184, 0.2); background: #111827; color: #e2e8f0; padding: 14px; font-size: 14px; }
        .form-group textarea:focus { outline: none; border-color: #6366f1; box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.18); }
        .alert { padding: 14px 18px; border-radius: 12px; margin-bottom: 18px; font-size: 13px; }
        .alert-error { background: rgba(248, 113, 113, 0.12); border: 1px solid rgba(248, 113, 113, 0.35); color: #fecaca; }
        .alert-success { background: rgba(34, 197, 94, 0.12); border: 1px solid rgba(34, 197, 94, 0.35); color: #bbf7d0; }
    </style>
</head>
<body>
    <nav class="navbar">
        <div>
            <span style="font-size:20px; font-weight:700; background: linear-gradient(135deg,#818cf8,#6366f1); -webkit-background-clip: text; color: transparent;">Detalle de Reporte</span>
        </div>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/empleados/listar">Empleados</a>
            <a href="${pageContext.request.contextPath}/reportes">Volver</a>
            <a href="${pageContext.request.contextPath}/auth/logout">Cerrar Sesión</a>
        </div>
    </nav>

    <div class="container">
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${not empty exito}">
            <div class="alert alert-success">${exito}</div>
        </c:if>

        <div class="card">
            <div class="header">
                <div>
                    <h1>Reporte #${reporte.idReporte}</h1>
                    <p style="color:#94a3b8; margin-top:6px;">Período: ${reporte.fechaInicio} - ${reporte.fechaFin}</p>
                </div>
                <a href="${pageContext.request.contextPath}/reportes" class="btn btn-secondary">Regresar</a>
            </div>

            <div class="stats">
                <div class="stat"><h3>Total empleados creados</h3><p>${reporte.totalEmpleadosCreados}</p></div>
                <div class="stat"><h3>Total empleados editados</h3><p>${reporte.totalEmpleadosEditados}</p></div>
                <div class="stat"><h3>Total empleados eliminados</h3><p>${reporte.totalEmpleadosEliminados}</p></div>
                <div class="stat"><h3>Accesos fallidos</h3><p>${reporte.totalAccesosFallidos}</p></div>
            </div>

            <div class="section">
                <div class="detail-grid">
                    <div class="info-card">
                        <h2>Información general</h2>
                        <p><strong>Generado por:</strong> ${reporte.generadoPor}</p>
                        <p><strong>Fecha de generación:</strong> ${reporte.fechaGeneracion}</p>
                        <p><strong>Fecha inicio:</strong> ${reporte.fechaInicio}</p>
                        <p><strong>Fecha fin:</strong> ${reporte.fechaFin}</p>
                    </div>
                    <div class="info-card">
                        <h2>Resumen rápido</h2>
                        <p>Este reporte muestra la actividad del período seleccionado y sirve como base para seguimiento y auditoría.</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <h2 style="margin-bottom:16px;">Anotaciones</h2>
            <c:if test="${empty reporte.anotaciones}">
                <p style="color:#94a3b8;">Aún no hay anotaciones para este reporte.</p>
            </c:if>
            <ul class="list">
                <c:forEach var="anotacion" items="${reporte.anotaciones}">
                    <li>
                        <p style="font-size:14px; color:#e2e8f0;">${anotacion.contenido}</p>
                        <p style="margin-top:8px; color:#94a3b8; font-size:12px;">${anotacion.autor} · ${anotacion.fechaCreacion}</p>
                    </li>
                </c:forEach>
            </ul>

            <div class="section" style="margin-top:28px;">
                <h2>Agregar anotación</h2>
                <form action="${pageContext.request.contextPath}/reportes/anotar" method="POST">
                    <input type="hidden" name="idReporte" value="${reporte.idReporte}">
                    <div class="form-group">
                        <label for="contenido">Contenido</label>
                        <textarea id="contenido" name="contenido" placeholder="Escribe una observación sobre el informe..." required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Guardar anotación</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>
