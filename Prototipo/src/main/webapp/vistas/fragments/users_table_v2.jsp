<%@ page import="java.util.List, com.tekmess.snaar.modelo.entidad.Usuario" %>
<div id="tab-users" class="tab-pane" style="display:none;">
    <div class="card">
        <h4>Usuarios del sistema</h4>
        <div style="margin-top:10px;overflow:auto;">
            <table style="width:100%;border-collapse:collapse;color:#cbd5e1;font-size:14px;">
                <thead>
                    <tr style="text-align:left;color:#94a3b8;font-size:13px;">
                        <th style="padding:8px 6px;">Usuario</th>
                        <th style="padding:8px 6px;">Cédula</th>
                        <th style="padding:8px 6px;">Estado</th>
                        <th style="padding:8px 6px;">Último acceso</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuariosList");
                    if (usuarios == null || usuarios.isEmpty()) {
                %>
                    <tr><td colspan="4" style="padding:12px;color:#64748b;">No hay usuarios registrados.</td></tr>
                <% } else {
                        for (Usuario u : usuarios) {
                %>
                    <tr style="border-top:1px solid rgba(255,255,255,0.02);">
                        <td style="padding:8px 6px;"><%= u.getNombreUsuario() %></td>
                        <td style="padding:8px 6px;"><%= u.getCedula() != null ? u.getCedula() : "-" %></td>
                        <td style="padding:8px 6px;"><%= u.getEstadoCuenta() != null ? u.getEstadoCuenta().name() : "-" %></td>
                        <td style="padding:8px 6px;"><%= u.getUltimoAcceso() != null ? u.getUltimoAcceso() : "-" %></td>
                    </tr>
                <%  }
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>
