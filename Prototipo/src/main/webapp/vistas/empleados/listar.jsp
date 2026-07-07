<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SNAAR - Gestión de Empleados | TekMess</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Inter', sans-serif;
            background: #0f172a; color: #e2e8f0;
            min-height: 100vh;
        }
        .navbar {
            background: rgba(30, 41, 59, 0.95);
            backdrop-filter: blur(10px);
            padding: 16px 32px; display: flex;
            justify-content: space-between; align-items: center;
            border-bottom: 1px solid rgba(99, 102, 241, 0.2);
        }
        .navbar h2 {
            font-size: 20px;
            background: linear-gradient(135deg, #818cf8, #6366f1);
            -webkit-background-clip: text; -webkit-text-fill-color: transparent;
        }
        .navbar .nav-links a {
            color: #94a3b8; text-decoration: none; margin-left: 24px;
            font-size: 14px; transition: color 0.2s;
        }
        .navbar .nav-links a:hover { color: #818cf8; }
        .container { max-width: 1200px; margin: 32px auto; padding: 0 24px; }
        .header-section {
            display: flex; justify-content: space-between;
            align-items: center; margin-bottom: 24px;
        }
        .header-section h1 { font-size: 24px; font-weight: 600; }
        .btn {
            padding: 10px 20px; border: none; border-radius: 8px;
            font-size: 13px; font-weight: 600; cursor: pointer;
            transition: all 0.3s ease; font-family: 'Inter', sans-serif;
        }
        .btn-primary {
            background: linear-gradient(135deg, #6366f1, #818cf8);
            color: white;
        }
        .btn-primary:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
        }
        .btn-danger { background: rgba(239, 68, 68, 0.2); color: #fca5a5; border: 1px solid rgba(239, 68, 68, 0.3); }
        .btn-danger:hover { background: rgba(239, 68, 68, 0.35); }
        .btn-warning { background: rgba(245, 158, 11, 0.2); color: #fcd34d; border: 1px solid rgba(245, 158, 11, 0.3); }
        .btn-warning:hover { background: rgba(245, 158, 11, 0.35); }
        .btn-secondary { background: rgba(100, 116, 139, 0.2); color: #cbd5e1; border: 1px solid rgba(100, 116, 139, 0.3); }

        .alert {
            padding: 14px 20px; border-radius: 10px;
            margin-bottom: 20px; font-size: 13px;
        }
        .alert-error {
            background: rgba(239, 68, 68, 0.12);
            border: 1px solid rgba(239, 68, 68, 0.25); color: #fca5a5;
        }
        .alert-success {
            background: rgba(34, 197, 94, 0.12);
            border: 1px solid rgba(34, 197, 94, 0.25); color: #86efac;
        }

        .table-card {
            background: rgba(30, 41, 59, 0.7);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(99, 102, 241, 0.15);
            border-radius: 16px; overflow: hidden;
        }
        table { width: 100%; border-collapse: collapse; }
        th {
            background: rgba(99, 102, 241, 0.12);
            padding: 14px 20px; text-align: left;
            font-size: 12px; font-weight: 600;
            text-transform: uppercase; letter-spacing: 0.5px;
            color: #94a3b8;
        }
        td {
            padding: 14px 20px; font-size: 14px;
            border-top: 1px solid rgba(51, 65, 85, 0.5);
        }
        tr:hover td { background: rgba(99, 102, 241, 0.06); }
        .rol-badge {
            padding: 4px 10px; border-radius: 6px;
            font-size: 11px; font-weight: 600;
        }
        .rol-guardia { background: rgba(34, 197, 94, 0.15); color: #86efac; }
        .rol-supervisor { background: rgba(59, 130, 246, 0.15); color: #93c5fd; }
        .rol-centralista { background: rgba(245, 158, 11, 0.15); color: #fcd34d; }
        .rol-jefe { background: rgba(168, 85, 247, 0.15); color: #c4b5fd; }
        .acciones { display: flex; gap: 8px; }
        .acciones .btn { padding: 6px 12px; font-size: 12px; }

        .modal-overlay {
            display: none; position: fixed; top: 0; left: 0;
            width: 100%; height: 100%; background: rgba(0,0,0,0.6);
            z-index: 100; align-items: center; justify-content: center;
        }
        .modal-content {
            background: #1e293b; border-radius: 16px; padding: 32px;
            border: 1px solid rgba(99, 102, 241, 0.2);
            max-width: 400px; width: 90%;
        }
        .modal-content h3 { margin-bottom: 16px; }
        .modal-actions { display: flex; gap: 12px; margin-top: 20px; justify-content: flex-end; }
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="app-page employees-page">
    <!-- Barra de navegación -->
    <jsp:include page="/vistas/fragments/navbar.jsp" />
    <nav class="navbar legacy-navbar">
        <h2>SNAAR — TekMess</h2>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/empleados/listar" id="nav-empleados">Empleados</a>
            <a href="${pageContext.request.contextPath}/reportes" id="nav-reportes">Reportes</a>
            <a href="${pageContext.request.contextPath}/auth/cambiar-contrasena" id="nav-contrasena">Contraseña</a>
            <a href="${pageContext.request.contextPath}/auth/logout" id="nav-salir">Cerrar Sesión</a>
        </div>
    </nav>

    <div class="container">
        <!-- Encabezado -->
        <div class="header-section">
            <div class="page-heading">
                <p class="eyebrow">Directorio operativo</p>
                <h1>Personal</h1>
                <p class="page-subtitle">Consulta colaboradores, responsabilidades y accesos desde un único lugar.</p>
            </div>
            <c:if test="${puedeGestionarPersonal}">
            <div style="display: flex; gap: 12px;">
                <a href="${pageContext.request.contextPath}/empleados/nuevo" class="btn btn-primary" id="btn-nuevo-empleado">
                    + Registrar Empleado
                </a>
                <form action="${pageContext.request.contextPath}/empleados/deshacer" method="POST" style="display:inline;">
                    <button type="submit" class="btn btn-secondary" id="btn-deshacer">↩ Deshacer</button>
                </form>
            </div>
            </c:if>
        </div>

        <!-- Mensajes -->
        <c:if test="${not empty error}">
            <div class="alert alert-error" id="msg-error">${error}</div>
        </c:if>
        <c:if test="${not empty exito}">
            <div class="alert alert-success" id="msg-exito">${exito}</div>
        </c:if>

        <form class="filters personnel-filters personnel-filters-wide" action="${pageContext.request.contextPath}/empleados/listar" method="GET">
            <div>
                <label for="q">Buscar personal</label>
                <input type="text" id="q" name="q" value="${q}" placeholder="Cédula, nombre o correo">
            </div>
            <div>
                <label for="rol">Rol</label>
                <select id="rol" name="rol">
                    <option value="" ${empty rolFiltro ? 'selected' : ''}>Todos los roles</option>
                    <option value="GUARDIA" ${rolFiltro == 'GUARDIA' ? 'selected' : ''}>Guardia</option>
                    <option value="SUPERVISOR" ${rolFiltro == 'SUPERVISOR' ? 'selected' : ''}>Supervisor</option>
                    <option value="CENTRALISTA" ${rolFiltro == 'CENTRALISTA' ? 'selected' : ''}>Centralista</option>
                    <option value="JEFE_LOGISTICA" ${rolFiltro == 'JEFE_LOGISTICA' ? 'selected' : ''}>Jefe Logística</option>
                </select>
            </div>
            <div>
                <label for="locacion">Locación</label>
                <select id="locacion" name="locacion">
                    <option value="" ${empty locacionFiltro ? 'selected' : ''}>Todas las locaciones</option>
                    <c:forEach var="loc" items="${locaciones}">
                        <option value="${loc.idLocacion}" ${locacionFiltro == loc.idLocacion ? 'selected' : ''}>
                            ${loc.nombre}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="filter-actions">
                <button type="submit" class="btn btn-primary">Aplicar filtros</button>
                <a href="${pageContext.request.contextPath}/empleados/exportar?q=${q}&rol=${rolFiltro}&locacion=${locacionFiltro}" class="btn btn-secondary">Exportar CSV</a>
                <a href="${pageContext.request.contextPath}/empleados/listar" class="btn btn-secondary">Limpiar</a>
            </div>
        </form>

        <!-- Tabla de empleados (RF-SNAAR-01.04) -->
        <div class="table-card">
            <table id="tabla-empleados">
                <thead>
                    <tr>
                        <th>Cédula</th>
                        <th>Nombres</th>
                        <th>Rol</th>
                        <th>Locación</th>
                        <th>Correo</th>
                        <c:if test="${puedeGestionarPersonal}">
                            <th>Acceso</th>
                            <th>Estado</th>
                        </c:if>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="emp" items="${empleados}">
                        <c:set var="usuarioEmp" value="${usuariosPorCedula[emp.cedula]}" />
                        <tr>
                            <td>${emp.cedula}</td>
                            <td>${emp.nombres}</td>
                            <td>
                                <span class="rol-badge
                                    <c:choose>
                                        <c:when test="${emp.rol == 'GUARDIA'}">rol-guardia</c:when>
                                        <c:when test="${emp.rol == 'SUPERVISOR'}">rol-supervisor</c:when>
                                        <c:when test="${emp.rol == 'CENTRALISTA'}">rol-centralista</c:when>
                                        <c:when test="${emp.rol == 'JEFE_LOGISTICA'}">rol-jefe</c:when>
                                    </c:choose>
                                ">
                                    ${emp.rol.descripcion}
                                </span>
                            </td>
                            <td>
                                <span class="location-pill">${empty emp.nombreLocacion ? 'Sin asignar' : emp.nombreLocacion}</span>
                            </td>
                            <td>${emp.correo}</td>
                            <c:if test="${puedeGestionarPersonal}">
                                <td>
                                    <div class="credential-cell">
                                        <span class="credential-user">${empty usuarioEmp ? 'Sin usuario' : usuarioEmp.nombreUsuario}</span>
                                        <c:choose>
                                            <c:when test="${not empty usuarioEmp.contrasenaTemporal}">
                                                <code class="credential-key">${usuarioEmp.contrasenaTemporal}</code>
                                            </c:when>
                                            <c:when test="${not empty usuarioEmp and not usuarioEmp.primerAcceso}">
                                                <span class="badge badge-neutral">Ya cambiada</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-neutral">No disponible</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                                <td>
                                    <div class="account-state">
                                        <span class="badge ${usuarioEmp.estadoCuenta == 'BLOQUEADO' ? 'badge-danger' : 'badge-success'}">
                                            ${empty usuarioEmp ? 'Sin cuenta' : usuarioEmp.estadoCuenta}
                                        </span>
                                        <small>${empty usuarioEmp ? '' : usuarioEmp.intentosFallidos} intentos fallidos</small>
                                    </div>
                                </td>
                            </c:if>
                            <td>
                                <c:choose>
                                <c:when test="${puedeGestionarPersonal}">
                                <div class="acciones">
                                    <a href="${pageContext.request.contextPath}/empleados/editar?cedula=${emp.cedula}"
                                       class="btn btn-warning" id="btn-editar-${emp.cedula}">Editar</a>

                                    <button class="btn btn-danger" id="btn-eliminar-${emp.cedula}"
                                            onclick="confirmarEliminacion('${emp.cedula}', '${emp.nombres}')">
                                        Dar de baja
                                    </button>
                                    <form action="${pageContext.request.contextPath}/empleados/reset-password" method="POST" style="display:inline;">
                                        <input type="hidden" name="cedula" value="${emp.cedula}">
                                        <button type="submit" class="btn btn-secondary">Nueva clave</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/empleados/desbloquear" method="POST" style="display:inline;">
                                        <input type="hidden" name="cedula" value="${emp.cedula}">
                                        <button type="submit" class="btn btn-secondary">Desbloquear</button>
                                    </form>
                                </div>
                                </c:when>
                                <c:otherwise><span class="badge badge-neutral">Solo lectura</span></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty empleados}">
                        <tr>
                            <td colspan="${puedeGestionarPersonal ? 8 : 6}" style="text-align:center; padding:40px; color:#64748b;">
                                No hay empleados registrados en el sistema.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Modal de confirmación para eliminar (RF-SNAAR-01.03 paso 4) -->
    <div class="modal-overlay" id="modal-confirmar">
        <div class="modal-content">
            <h3>Confirmar eliminación</h3>
            <p>¿Está seguro de dar de baja al empleado <strong id="modal-nombre"></strong>
               (Cédula: <span id="modal-cedula"></span>)?</p>
            <p style="color: #fca5a5; font-size: 12px; margin-top: 8px;">
                Esta acción es permanente y no podrá ser revertida.
            </p>
            <div class="modal-actions">
                <button class="btn btn-secondary" onclick="cerrarModal()" id="btn-cancelar-eliminacion">Cancelar</button>
                <form id="form-eliminar" action="${pageContext.request.contextPath}/empleados/eliminar" method="POST">
                    <input type="hidden" name="cedula" id="input-cedula-eliminar">
                    <button type="submit" class="btn btn-danger" id="btn-confirmar-eliminacion">Confirmar</button>
                </form>
            </div>
        </div>
    </div>

    <script>
        function confirmarEliminacion(cedula, nombre) {
            document.getElementById('modal-nombre').textContent = nombre;
            document.getElementById('modal-cedula').textContent = cedula;
            document.getElementById('input-cedula-eliminar').value = cedula;
            document.getElementById('modal-confirmar').style.display = 'flex';
        }
        function cerrarModal() {
            document.getElementById('modal-confirmar').style.display = 'none';
        }
    </script>
</body>
</html>
