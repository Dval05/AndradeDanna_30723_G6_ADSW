<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SNAAR - ${editar ? 'Editar' : 'Registrar'} Empleado | TekMess</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Inter', sans-serif;
            background: #0f172a; color: #e2e8f0; min-height: 100vh;
        }
        .navbar {
            background: rgba(30, 41, 59, 0.95); backdrop-filter: blur(10px);
            padding: 16px 32px; display: flex;
            justify-content: space-between; align-items: center;
            border-bottom: 1px solid rgba(99, 102, 241, 0.2);
        }
        .navbar h2 {
            font-size: 20px;
            background: linear-gradient(135deg, #818cf8, #6366f1);
            -webkit-background-clip: text; -webkit-text-fill-color: transparent;
        }
        .container { max-width: 600px; margin: 48px auto; padding: 0 24px; }
        .form-card {
            background: rgba(30, 41, 59, 0.85); backdrop-filter: blur(20px);
            border: 1px solid rgba(99, 102, 241, 0.2);
            border-radius: 20px; padding: 40px;
        }
        .form-card h1 { font-size: 22px; margin-bottom: 28px; }
        .form-group { margin-bottom: 20px; }
        .form-group label {
            display: block; font-size: 13px; font-weight: 500;
            color: #94a3b8; margin-bottom: 6px;
        }
        .form-group input, .form-group select {
            width: 100%; padding: 12px 16px; border-radius: 10px;
            border: 1px solid rgba(99, 102, 241, 0.3);
            background: rgba(15, 23, 42, 0.6); color: #e2e8f0;
            font-size: 14px; font-family: 'Inter', sans-serif;
            transition: all 0.3s ease;
        }
        .form-group input:focus, .form-group select:focus {
            outline: none; border-color: #6366f1;
            box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.15);
        }
        .form-group input:disabled {
            opacity: 0.5; cursor: not-allowed;
        }
        .form-group select option { background: #1e293b; color: #e2e8f0; }
        .form-actions { display: flex; gap: 12px; margin-top: 28px; }
        .btn {
            padding: 12px 24px; border: none; border-radius: 10px;
            font-size: 14px; font-weight: 600; cursor: pointer;
            transition: all 0.3s ease; font-family: 'Inter', sans-serif;
        }
        .btn-primary {
            background: linear-gradient(135deg, #6366f1, #818cf8);
            color: white; flex: 1;
        }
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(99, 102, 241, 0.4);
        }
        .btn-cancel {
            background: rgba(100, 116, 139, 0.2); color: #cbd5e1;
            border: 1px solid rgba(100, 116, 139, 0.3);
        }
        .btn-cancel:hover { background: rgba(100, 116, 139, 0.35); }
        .alert {
            padding: 14px 20px; border-radius: 10px;
            margin-bottom: 20px; font-size: 13px;
        }
        .alert-error {
            background: rgba(239, 68, 68, 0.12);
            border: 1px solid rgba(239, 68, 68, 0.25); color: #fca5a5;
        }
        .hint { font-size: 11px; color: #64748b; margin-top: 4px; }
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="app-page employee-form-page">
    <jsp:include page="/vistas/fragments/navbar.jsp" />
    <nav class="navbar legacy-navbar">
        <h2>SNAAR — TekMess</h2>
    </nav>

    <div class="container">
        <div class="form-card">
            <div class="page-heading">
                <p class="eyebrow">${editar ? 'Actualizacion de perfil' : 'Alta de personal'}</p>
                <h1>${editar ? 'Editar empleado' : 'Registrar empleado'}</h1>
                <p class="page-subtitle">Completa la informacion institucional y asigna el nivel operativo correspondiente.</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-error" id="msg-error">${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/empleados/${editar ? 'editar' : 'crear'}"
                  method="POST" id="form-empleado" data-validate>

                <!-- Cédula (RF-SNAAR-01.05: 10 dígitos) -->
                <div class="form-group">
                    <label for="cedula">Cédula</label>
                    <input type="text" id="cedula" name="cedula"
                           value="${empleado.cedula}" required
                           inputmode="numeric" pattern="\d{10}" minlength="10" maxlength="10"
                           title="La cédula debe tener exactamente 10 dígitos numéricos."
                           placeholder="Ingrese 10 dígitos"
                           ${editar ? 'disabled' : ''}>
                    <c:if test="${editar}">
                        <input type="hidden" name="cedula" value="${empleado.cedula}">
                    </c:if>
                    <p class="hint">Exactamente 10 dígitos numéricos</p>
                </div>

                <!-- Nombres -->
                <div class="form-group">
                    <label for="nombres">Nombres completos</label>
                    <input type="text" id="nombres" name="nombres"
                           value="${empleado.nombres}" required
                           minlength="7" maxlength="80"
                           pattern="^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ'´`.\-]+( [A-Za-zÁÉÍÓÚÜÑáéíóúüñ'´`.\-]+)+$"
                           title="Ingrese nombre y apellido usando solo letras y espacios."
                           placeholder="Nombres y apellidos">
                    <p class="hint">Debe incluir al menos nombre y apellido</p>
                </div>

                <!-- Rol (RF-SNAAR-01.05: rol válido) -->
                <div class="form-group">
                    <label for="rol">Rol</label>
                    <select id="rol" name="rol" required>
                        <option value="">Seleccione un rol</option>
                        <option value="GUARDIA" ${empleado.rol == 'GUARDIA' ? 'selected' : ''}>Guardia</option>
                        <option value="SUPERVISOR" ${empleado.rol == 'SUPERVISOR' ? 'selected' : ''}>Supervisor</option>
                        <option value="CENTRALISTA" ${empleado.rol == 'CENTRALISTA' ? 'selected' : ''}>Centralista</option>
                        <option value="JEFE_LOGISTICA" ${empleado.rol == 'JEFE_LOGISTICA' ? 'selected' : ''}>Jefe Logística/Operación</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="idLocacion">Locación / puesto asignado</label>
                    <select id="idLocacion" name="idLocacion" required>
                        <option value="">Seleccione una locación</option>
                        <c:forEach var="loc" items="${locaciones}">
                            <option value="${loc.idLocacion}" ${empleado.idLocacion == loc.idLocacion ? 'selected' : ''}>
                                ${loc.nombre} — ${loc.ciudad}
                            </option>
                        </c:forEach>
                    </select>
                    <p class="hint">Define dónde prestará servicio o a qué sede pertenece.</p>
                </div>

                <!-- Correo (RF-SNAAR-01.05: formato institucional) -->
                <div class="form-group">
                    <label for="correo">Correo institucional</label>
                    <input type="email" id="correo" name="correo"
                           value="${empleado.correo}" required maxlength="120"
                           pattern="^[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}$"
                           title="Ingrese un correo válido, por ejemplo nombre@tekmess.com."
                           placeholder="ejemplo@tekmess.com">
                    <p class="hint">Formato de correo institucional válido</p>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/empleados/listar"
                       class="btn btn-cancel" id="btn-cancelar">Cancelar</a>
                    <button type="submit" class="btn btn-primary" id="btn-guardar">
                        ${editar ? 'Actualizar' : 'Registrar'}
                    </button>
                </div>
            </form>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/validaciones.js"></script>
</body>
</html>
