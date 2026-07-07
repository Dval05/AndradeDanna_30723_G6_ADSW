<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Gestión de Roles</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        body { font-family:Inter, sans-serif; background:#071029; color:#e6eef8; padding:20px; }
        .card{ background:rgba(12,22,42,0.7); padding:18px; border-radius:10px; max-width:900px; margin:auto; }
        .row{ display:flex; gap:8px; align-items:center; }
        label{ font-size:13px; color:#9fb4c5; }
        input[type=text]{ width:100%; padding:8px; border-radius:8px; border:1px solid rgba(255,255,255,0.04); background:transparent; color:#e6eef8; }
        .actions{ margin-top:12px; text-align:right; }
        .btn{ padding:8px 12px; border-radius:8px; background:linear-gradient(90deg,#06b6d4,#6366f1); color:white; text-decoration:none; }
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="app-page roles-page">
    <jsp:include page="/vistas/fragments/navbar.jsp" />
    <div class="card">
        <div class="page-heading">
            <p class="eyebrow">Gobierno de acceso</p>
            <h1>Roles del sistema</h1>
            <p class="page-subtitle">Administra el catalogo operativo. Los cambios se aplican a la descripcion y disponibilidad de cada rol.</p>
        </div>
        <% if (request.getAttribute("exito") != null) { %>
            <div class="alert alert-success" style="margin-top:20px;"><%= request.getAttribute("exito") %></div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error" style="margin-top:20px;"><%= request.getAttribute("error") %></div>
        <% } %>
        <form method="post" data-validate>
            <div style="display:flex;flex-direction:column;gap:12px;margin-top:26px;">
                <%
                    List<Map<String,Object>> cfg = (List<Map<String,Object>>) request.getAttribute("configList");
                    if (cfg != null) {
                        for (Map<String,Object> m : cfg) {
                            String name = (String) m.get("name");
                            String desc = (String) m.get("description");
                            boolean active = Boolean.TRUE.equals(m.get("active"));
                %>
                <div class="role-row">
                    <div class="role-name"><strong><%= name.replace('_', ' ') %></strong><small>Nivel operativo</small></div>
                    <div><input type="text" name="desc_<%= name %>" value="<%= desc %>" aria-label="Descripcion de <%= name %>" required minlength="8" maxlength="120"/></div>
                    <label class="role-switch"><input type="checkbox" name="active_<%= name %>" <%= active?"checked":"" %> /> Activo</label>
                </div>
                <%      }
                    }
                %>
            </div>
            <div class="actions"><button class="btn" type="submit">Guardar cambios</button></div>
        </form>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/validaciones.js"></script>
</body>
</html>
