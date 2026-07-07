<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SNAAR - Locaciones | TekMess</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="app-page locations-page">
    <jsp:include page="/vistas/fragments/navbar.jsp" />

    <div class="container">
        <div class="header-section">
            <div class="page-heading">
                <p class="eyebrow">Cobertura operativa</p>
                <h1>Locaciones y puestos</h1>
                <p class="page-subtitle">Administra sedes, puntos de vigilancia y capacidad operativa para asignar personal correctamente.</p>
            </div>
        </div>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${not empty exito}">
            <div class="alert alert-success">${exito}</div>
        </c:if>

        <div class="location-layout">
            <section class="form-card">
                <p class="eyebrow">Nueva locación</p>
                <h2>Registrar sitio</h2>
                <form action="${pageContext.request.contextPath}/locaciones/guardar" method="POST" data-validate>
                    <div class="form-group">
                        <label for="nombre">Nombre</label>
                        <input id="nombre" name="nombre" required minlength="4" maxlength="120" placeholder="Centro Norte">
                    </div>
                    <div class="form-group">
                        <label for="ciudad">Ciudad</label>
                        <input id="ciudad" name="ciudad" required minlength="3" maxlength="80" placeholder="Quito">
                    </div>
                    <div class="form-group">
                        <label for="direccion">Dirección</label>
                        <input id="direccion" name="direccion" required minlength="6" maxlength="180" placeholder="Av. Amazonas N34-120">
                    </div>
                    <div class="form-group">
                        <label for="responsable">Responsable</label>
                        <input id="responsable" name="responsable" required minlength="4" maxlength="120" placeholder="Supervisor de turno">
                    </div>
                    <div class="form-group">
                        <label for="capacidad">Capacidad operativa</label>
                        <input id="capacidad" name="capacidad" type="number" min="1" max="500" required placeholder="12">
                    </div>
                    <label class="role-switch"><input type="checkbox" name="activa" checked> Activa</label>
                    <div class="form-actions">
                        <button class="btn btn-primary" type="submit">Guardar locación</button>
                    </div>
                </form>
            </section>

            <section class="table-card">
                <table>
                    <thead>
                        <tr>
                            <th>Locación</th>
                            <th>Ciudad</th>
                            <th>Dirección</th>
                            <th>Responsable</th>
                            <th>Ocupación</th>
                            <th>Estado</th>
                            <th>Acción</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="loc" items="${locaciones}">
                            <tr>
                                <td><strong>${loc.nombre}</strong></td>
                                <td>${loc.ciudad}</td>
                                <td>${loc.direccion}</td>
                                <td>${loc.responsable}</td>
                                <td>
                                    <div class="occupancy">
                                        <strong>${empty ocupacionPorLocacion[loc.idLocacion] ? 0 : ocupacionPorLocacion[loc.idLocacion]} / ${loc.capacidad}</strong>
                                        <span>asignados</span>
                                    </div>
                                </td>
                                <td>
                                    <span class="badge ${loc.activa ? 'badge-success' : 'badge-neutral'}">
                                        ${loc.activa ? 'Activa' : 'Inactiva'}
                                    </span>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/locaciones/estado" method="POST">
                                        <input type="hidden" name="idLocacion" value="${loc.idLocacion}">
                                        <input type="hidden" name="activa" value="${!loc.activa}">
                                        <button class="btn btn-secondary btn-sm" type="submit">
                                            ${loc.activa ? 'Desactivar' : 'Activar'}
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty locaciones}">
                            <tr><td colspan="7" class="empty-state">No hay locaciones registradas.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </section>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/validaciones.js"></script>
</body>
</html>
