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
</head>
<body>
    <div class="card">
        <h3>Gestión de Roles</h3>
        <p class="muted">Edita descripciones y activa/desactiva roles del sistema.</p>
        <form method="post">
            <div style="display:flex;flex-direction:column;gap:12px;margin-top:12px;">
                <%
                    List<Map<String,Object>> cfg = (List<Map<String,Object>>) request.getAttribute("configList");
                    if (cfg != null) {
                        for (Map<String,Object> m : cfg) {
                            String name = (String) m.get("name");
                            String desc = (String) m.get("description");
                            boolean active = Boolean.TRUE.equals(m.get("active"));
                %>
                <div style="display:flex;gap:10px;align-items:center;">
                    <div style="width:160px;"><strong><%= name %></strong></div>
                    <div style="flex:1;"><input type="text" name="desc_<%= name %>" value="<%= desc %>"/></div>
                    <div><label><input type="checkbox" name="active_<%= name %>" <%= active?"checked":"" %> /> activo</label></div>
                </div>
                <%      }
                    }
                %>
            </div>
            <div class="actions"><button class="btn" type="submit">Guardar cambios</button></div>
        </form>
    </div>
</body>
</html>
