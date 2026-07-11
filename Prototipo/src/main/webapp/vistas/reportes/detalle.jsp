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
        /* --- Resets globales --- */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #0b0f19; color: #e2e8f0; line-height: 1.5; }

        /* --- Navbar mejorada --- */
        .navbar {
            background: rgba(11, 15, 25, 0.85);
            backdrop-filter: blur(12px);
            -webkit-backdrop-filter: blur(12px);
            padding: 16px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid rgba(99, 102, 241, 0.12);
            position: sticky;
            top: 0;
            z-index: 100;
        }
        .nav-brand { font-size: 18px; font-weight: 700; color: #111827; }
        .nav-links a { margin-left: 24px; color: #94a3b8; text-decoration: none; font-size: 14px; font-weight: 500; transition: color 0.2s ease; }
        .nav-links a:hover { color: #1f6feb; }
        .nav-links .active { color: #1f6feb; }

        /* --- Layout general --- */
        .container { max-width: 1100px; margin: 40px auto; padding: 0 24px; }
        
        /* --- Alertas estilizadas --- */
        .alert { padding: 16px 20px; border-radius: 12px; margin-bottom: 24px; font-size: 14px; font-weight: 500; display: flex; align-items: center; }
        .alert-error { background: rgba(239, 68, 68, 0.1); border: 1px solid rgba(239, 68, 68, 0.25); color: #fca5a5; }
        .alert-success { background: rgba(34, 197, 94, 0.1); border: 1px solid rgba(34, 197, 94, 0.25); color: #86efac; }

        /* --- Tarjetas (Cards) de UI --- */
        .card { background: rgba(17, 24, 39, 0.6); border: 1px solid rgba(255, 255, 255, 0.05); border-radius: 18px; padding: 32px; margin-bottom: 28px; backdrop-filter: blur(8px); }
        
        /* --- Encabezado del reporte --- */
        .header { display: flex; justify-content: space-between; align-items: center; gap: 20px; flex-wrap: wrap; margin-bottom: 32px; }
        .header h1 { font-size: 28px; font-weight: 700; color: #fff; letter-spacing: -0.02em; }
        .badge-period { display: inline-block; background: #eff6ff; border: 1px solid #bfdbfe; color: #1f6feb; padding: 6px 14px; border-radius: 99px; font-size: 13px; font-weight: 500; margin-top: 8px; }

        /* --- Botones --- */
        .btn { display: inline-flex; align-items: center; justify-content: center; padding: 10px 20px; border-radius: 10px; font-size: 14px; font-weight: 600; cursor: pointer; border: none; transition: all 0.2s ease; text-decoration: none; }
        .btn-primary { background: #1f6feb; color: #fff; box-shadow: none; }
        .btn-primary:hover { opacity: 0.95; transform: translateY(-1px); }
        .btn-secondary { background: rgba(255, 255, 255, 0.05); color: #cbd5e1; border: 1px solid rgba(255, 255, 255, 0.08); }
        .btn-secondary:hover { background: rgba(255, 255, 255, 0.1); color: #fff; }

        /* --- Métrica Dashboard Grid --- */
        .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; margin-bottom: 36px; }
        .stat-card { background: #ffffff; border: 1px solid #e5eaf0; border-radius: 12px; padding: 24px; transition: transform 0.2s ease, border-color 0.2s ease; border-left: 4px solid #1f6feb; }
        .stat-card:hover { transform: translateY(-2px); border-color: rgba(99, 102, 241, 0.3); }
        .stat-card.danger { border-left-color: #ef4444; }
        .stat-card h3 { font-size: 13px; font-weight: 500; color: #94a3b8; text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 8px; }
        .stat-card .value { font-size: 32px; font-weight: 700; color: #fff; }

        /* --- Sección Detalle / Tablas --- */
        .section-title { font-size: 18px; font-weight: 600; color: #f8fafc; margin-bottom: 20px; display: flex; align-items: center; gap: 8px; }
        
        .meta-table { width: 100%; border-collapse: collapse; text-align: left; font-size: 14px; margin-top: 10px; }
        .meta-table tr { border-bottom: 1px solid rgba(255, 255, 255, 0.04); }
        .meta-table tr:last-child { border-bottom: none; }
        .meta-table td { padding: 14px 0; }
        .meta-table td.label { color: #94a3b8; width: 40%; font-weight: 400; }
        .meta-table td.value { color: #f1f5f9; font-weight: 500; }

        .info-box { background: rgba(99, 102, 241, 0.03); border: 1px solid rgba(99, 102, 241, 0.1); border-radius: 14px; padding: 20px; height: 100%; }
        .info-box p { color: #94a3b8; font-size: 14px; line-height: 1.6; }

        .grid-split { display: grid; grid-template-columns: 1.2fr 0.8fr; gap: 28px; }
        @media(max-width: 768px) { .grid-split { grid-template-columns: 1fr; } }

        /* --- Línea de tiempo de Anotaciones (Timeline) --- */
        .timeline { position: relative; padding-left: 24px; list-style: none; margin-top: 16px; }
        .timeline::before { content: ''; position: absolute; left: 6px; top: 8px; bottom: 8px; width: 2px; background: rgba(99, 102, 241, 0.2); }
        .timeline-item { position: relative; margin-bottom: 24px; }
        .timeline-item::before { content: ''; position: absolute; left: -22px; top: 6px; width: 10px; height: 10px; border-radius: 50%; background: #1f6feb; box-shadow: 0 0 0 4px rgba(31, 111, 235, 0.12); }
        .timeline-content { background: rgba(30, 41, 59, 0.3); border: 1px solid rgba(255, 255, 255, 0.02); padding: 16px; border-radius: 12px; }
        .timeline-text { font-size: 14px; color: #e2e8f0; line-height: 1.6; }
        .timeline-meta { font-size: 12px; color: #64748b; margin-top: 8px; display: flex; gap: 8px; }

        /* --- Formularios --- */
        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; margin-bottom: 8px; color: #94a3b8; font-size: 13px; font-weight: 500; }
        .form-group textarea { width: 100%; min-height: 110px; border-radius: 12px; border: 1px solid rgba(255, 255, 255, 0.08); background: #0f1422; color: #e2e8f0; padding: 14px; font-size: 14px; font-family: inherit; resize: vertical; transition: all 0.2s ease; }
        .form-group textarea:focus { outline: none; border-color: #1f6feb; box-shadow: 0 0 0 3px rgba(31, 111, 235, 0.14); background: #ffffff; }
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=contrast-darkmode-20260711">
</head>
<body class="app-page report-detail-page">

    <!-- Navbar -->
    <jsp:include page="/vistas/fragments/navbar.jsp" />
    <nav class="navbar legacy-navbar">
        <div class="nav-brand">SNAAR / Control Panel</div>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/empleados/listar">Empleados</a>
            <a href="${pageContext.request.contextPath}/reportes" class="active">Reportes</a>
            <a href="${pageContext.request.contextPath}/auth/logout" style="color: #ef4444;">Cerrar Sesión</a>
        </div>
    </nav>

    <div class="container">
        <!-- Notificaciones de feedback -->
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <span>${error}</span>
            </div>
        </c:if>
        <c:if test="${not empty exito}">
            <div class="alert alert-success">
                <span>${exito}</span>
            </div>
        </c:if>

        <!-- Bloque principal de Información -->
        <div class="card">
            <div class="header">
                <div>
                    <p class="eyebrow">Informe consolidado</p>
                    <h1>Reporte operativo #${reporte.idReporte}</h1>
                    <div class="badge-period">
                        Frecuencia: ${reporte.fechaInicio} — ${reporte.fechaFin}
                    </div>
                </div>
                <button type="button" class="btn btn-primary" onclick="window.print()">Imprimir reporte</button>
                <a href="${pageContext.request.contextPath}/reportes" class="btn btn-secondary">
                    ← Volver al Listado
                </a>
            </div>

            <!-- Panel de Métricas / KPIs -->
            <div class="stats-grid">
                <div class="stat-card">
                    <h3>Empleados Creados</h3>
                    <div class="value">${reporte.totalEmpleadosCreados}</div>
                </div>
                <div class="stat-card">
                    <h3>Empleados Editados</h3>
                    <div class="value">${reporte.totalEmpleadosEditados}</div>
                </div>
                <div class="stat-card">
                    <h3>Empleados Eliminados</h3>
                    <div class="value">${reporte.totalEmpleadosEliminados}</div>
                </div>
                <div class="stat-card danger">
                    <h3>Accesos Fallidos</h3>
                    <div class="value" style="color: #b42318;">${reporte.totalAccesosFallidos}</div>
                </div>
            </div>

            <!-- División de Detalles de Auditoría -->
            <div class="grid-split">
                <div>
                    <h2 class="section-title">Datos de Auditoría de Sistema</h2>
                    <table class="meta-table">
                        <tr>
                            <td class="label">Operador responsable</td>
                            <td class="value">${reporte.generadoPor}</td>
                        </tr>
                        <tr>
                            <td class="label">Fecha/Hora de emisión</td>
                            <td class="value">${reporte.fechaGeneracion}</td>
                        </tr>
                        <tr>
                            <td class="label">Fecha límite inferior (Inicio)</td>
                            <td class="value">${reporte.fechaInicio}</td>
                        </tr>
                        <tr>
                            <td class="label">Fecha límite superior (Fin)</td>
                            <td class="value">${reporte.fechaFin}</td>
                        </tr>
                    </table>
                </div>
                <div>
                    <div class="info-box">
                        <h2 class="section-title" style="font-size: 15px; margin-bottom: 12px;">Resumen Ejecutivo</h2>
                        <p>Este informe consolida las métricas del personal asociadas a transacciones CRUD de cuentas y trazas de fallos de autenticación de seguridad perimetral dentro del rango de tiempo establecido.</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bloque de Bitácora / Anotaciones -->
        <div class="card">
            <h2 class="section-title">Bitácora de Observaciones</h2>
            
            <c:if test="${empty reporte.anotaciones}">
                <p style="color:#64748b; font-size: 14px; padding: 8px 0;">No se han registrado anotaciones de control en este reporte.</p>
            </c:if>
            
            <ul class="timeline">
                <c:forEach var="anotacion" items="${reporte.anotaciones}">
                    <li class="timeline-item">
                        <div class="timeline-content">
                            <p class="timeline-text">${anotacion.contenido}</p>
                            <div class="timeline-meta">
                                <span>Por: <strong>${anotacion.autor}</strong></span>
                                <span>•</span>
                                <span>${anotacion.fechaCreacion}</span>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>

            <!-- Formulario de inserción -->
            <div style="margin-top: 36px; padding-top: 28px; border-top: 1px solid rgba(255, 255, 255, 0.05);">
                <h3 class="section-title" style="font-size: 16px;">Asentar nueva anotación en bitácora</h3>
                <form action="${pageContext.request.contextPath}/reportes/anotar" method="POST" data-validate>
                    <input type="hidden" name="idReporte" value="${reporte.idReporte}">
                    <div class="form-group">
                        <label for="contenido">Observación o Nota Técnica</label>
                        <textarea id="contenido" name="contenido" placeholder="Añada aquí comentarios sobre fallos registrados, incidencias detectadas o firmas de control..." required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Guardar Registro en Bitácora</button>
                </form>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/validaciones.js"></script>
</body>
</html>
