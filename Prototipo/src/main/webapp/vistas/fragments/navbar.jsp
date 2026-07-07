<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.tekmess.snaar.modelo.entidad.Rol" %>
<%
    String currentUri = request.getRequestURI();
    Object currentRoleObject = session.getAttribute("rol");
    String currentRole = currentRoleObject != null ? currentRoleObject.toString() : "";
    String currentUser = String.valueOf(session.getAttribute("usuario"));
    boolean currentIsBoss = "JEFE_LOGISTICA".equals(currentRole);
    String roleLabel = "JEFE_LOGISTICA".equals(currentRole) ? "Jefe de Logistica"
            : "SUPERVISOR".equals(currentRole) ? "Supervisor"
            : "CENTRALISTA".equals(currentRole) ? "Centralista" : "Guardia";
%>
<nav class="navbar app-navbar" aria-label="Navegacion principal">
    <a class="brand-lockup" href="${pageContext.request.contextPath}/dashboard" aria-label="SNAAR - Inicio">
        <span class="brand-symbol">S</span>
        <span class="brand-words"><strong>SNAAR</strong><small>TEKMESS</small></span>
    </a>
    <div class="nav-links">
        <a class="<%= currentUri.endsWith("/dashboard") ? "active" : "" %>" href="${pageContext.request.contextPath}/dashboard">Resumen</a>
        <a class="<%= currentUri.contains("/empleados/") ? "active" : "" %>" href="${pageContext.request.contextPath}/empleados/listar">Personal</a>
        <a class="<%= currentUri.contains("/locaciones/") ? "active" : "" %>" href="${pageContext.request.contextPath}/locaciones/listar">Locaciones</a>
        <a class="<%= currentUri.contains("/reportes") ? "active" : "" %>" href="${pageContext.request.contextPath}/reportes">Reportes</a>
        <% if (currentIsBoss) { %>
            <a class="<%= currentUri.contains("/roles/") ? "active" : "" %>" href="${pageContext.request.contextPath}/roles/manage">Roles</a>
        <% } %>
        <a class="<%= currentUri.contains("cambiar-contrasena") ? "active" : "" %>" href="${pageContext.request.contextPath}/auth/cambiar-contrasena">Seguridad</a>
    </div>
    <div class="nav-account">
        <div class="nav-avatar"><%= currentUser.isBlank() ? "U" : currentUser.substring(0, 1).toUpperCase() %></div>
        <div class="nav-identity"><strong><%= currentUser %></strong><small><%= roleLabel %></small></div>
        <a class="nav-logout" href="${pageContext.request.contextPath}/auth/logout" title="Cerrar sesion">Salir</a>
    </div>
</nav>
