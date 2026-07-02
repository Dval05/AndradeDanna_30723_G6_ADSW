<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SNAAR - Recuperar Contraseña | TekMess</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #0f172a 100%);
            display: flex; justify-content: center; align-items: center;
            min-height: 100vh; color: #e2e8f0;
        }
        .container {
            width: 100%; max-width: 420px;
            padding: 40px 36px;
            background: rgba(15, 23, 42, 0.92);
            border: 1px solid rgba(99, 102, 241, 0.3);
            border-radius: 20px;
            box-shadow: 0 25px 60px rgba(0,0,0,0.35);
        }
        h1 { font-size: 28px; margin-bottom: 20px; text-align: center; }
        p.subtitle { color: #94a3b8; font-size: 14px; margin-bottom: 24px; text-align: center; }
        .form-group { margin-bottom: 18px; }
        .form-group label { display: block; color: #cbd5e1; margin-bottom: 8px; font-size: 13px; }
        .form-group input { width: 100%; padding: 14px 16px; border-radius: 12px; border: 1px solid rgba(148, 163, 184, 0.25);
            background: rgba(15, 23, 42, 0.7); color: #e2e8f0; font-size: 14px; }
        .form-group input:focus { outline: none; border-color: #6366f1; box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.18); }
        .btn-primary { width: 100%; padding: 14px; border: none; border-radius: 12px;
            background: linear-gradient(135deg, #6366f1, #818cf8); color: white; font-size: 15px;
            font-weight: 600; cursor: pointer; transition: transform 0.2s ease; }
        .btn-primary:hover { transform: translateY(-2px); }
        .links { margin-top: 22px; text-align: center; color: #94a3b8; font-size: 13px; }
        .links a { color: #818cf8; text-decoration: none; }
        .message { margin-bottom: 18px; padding: 14px 16px; border-radius: 12px; font-size: 13px; }
        .message.error { background: rgba(248, 113, 113, 0.15); border: 1px solid rgba(248, 113, 113, 0.3); color: #fecaca; }
        .message.success { background: rgba(34, 197, 94, 0.15); border: 1px solid rgba(34, 197, 94, 0.3); color: #bbf7d0; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Recuperar Contraseña</h1>
        <p class="subtitle">Ingrese su usuario, correo institucional y nueva contraseña.</p>

        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>
        <c:if test="${not empty exito}">
            <div class="message success">${exito}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth/recuperar" method="POST">
            <div class="form-group">
                <label for="usuario">Usuario</label>
                <input type="text" id="usuario" name="usuario" placeholder="Nombre de usuario" required>
            </div>
            <div class="form-group">
                <label for="correo">Correo institucional</label>
                <input type="email" id="correo" name="correo" placeholder="correo@tekmess.com" required>
            </div>
            <div class="form-group">
                <label for="contrasenaNueva">Nueva contraseña</label>
                <input type="password" id="contrasenaNueva" name="contrasenaNueva" placeholder="Mínimo 8 caracteres" required>
            </div>
            <div class="form-group">
                <label for="confirmacion">Confirmar contraseña</label>
                <input type="password" id="confirmacion" name="confirmacion" placeholder="Confirmar contraseña" required>
            </div>
            <button type="submit" class="btn-primary">Restablecer</button>
        </form>

        <div class="links">
            <a href="${pageContext.request.contextPath}/auth/login">Volver al inicio de sesión</a>
        </div>
    </div>
</body>
</html>
