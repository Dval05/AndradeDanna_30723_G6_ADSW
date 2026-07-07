<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SNAAR - Iniciar Sesión | TekMess</title>
    <meta name="description" content="Inicio de sesión del Sistema SNAAR - TekMess">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #0f172a 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #e2e8f0;
        }
        .login-container {
            background: rgba(30, 41, 59, 0.85);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(99, 102, 241, 0.2);
            border-radius: 20px;
            padding: 48px 40px;
            width: 100%;
            max-width: 420px;
            box-shadow: 0 25px 60px rgba(0, 0, 0, 0.4);
        }
        .logo { text-align: center; margin-bottom: 32px; }
        .logo h1 {
            font-size: 28px; font-weight: 700;
            background: linear-gradient(135deg, #818cf8, #6366f1);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        .logo p { color: #94a3b8; font-size: 13px; margin-top: 4px; }
        .form-group { margin-bottom: 20px; }
        .form-group label {
            display: block; font-size: 13px; font-weight: 500;
            color: #94a3b8; margin-bottom: 6px;
        }
        .form-group input {
            width: 100%; padding: 12px 16px; border-radius: 10px;
            border: 1px solid rgba(99, 102, 241, 0.3);
            background: rgba(15, 23, 42, 0.6);
            color: #e2e8f0; font-size: 14px; font-family: 'Inter', sans-serif;
            transition: all 0.3s ease;
        }
        .form-group input:focus {
            outline: none; border-color: #6366f1;
            box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.15);
        }
        .btn-login {
            width: 100%; padding: 14px; border: none; border-radius: 10px;
            background: linear-gradient(135deg, #6366f1, #818cf8);
            color: white; font-size: 15px; font-weight: 600;
            cursor: pointer; transition: all 0.3s ease;
            font-family: 'Inter', sans-serif;
        }
        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(99, 102, 241, 0.4);
        }
        .error-msg {
            background: rgba(239, 68, 68, 0.15);
            border: 1px solid rgba(239, 68, 68, 0.3);
            color: #fca5a5; padding: 12px 16px;
            border-radius: 10px; font-size: 13px; margin-bottom: 20px;
        }
        .success-msg {
            background: rgba(34, 197, 94, 0.15);
            border: 1px solid rgba(34, 197, 94, 0.3);
            color: #86efac; padding: 12px 16px;
            border-radius: 10px; font-size: 13px; margin-bottom: 20px;
        }
        .recuperar-link {
            text-align: center; margin-top: 16px;
        }
        .recuperar-link a {
            color: #818cf8; text-decoration: none; font-size: 13px;
            transition: color 0.2s ease;
        }
        .recuperar-link a:hover { color: #a5b4fc; }
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="auth-page login-page">
    <main class="auth-shell">
        <section class="brand-panel" aria-label="Presentacion SNAAR">
            <div class="brand-mark"><span>S</span> SNAAR · TEKMESS</div>
            <div class="brand-copy">
                <p class="brand-kicker">Operaciones y auditoría</p>
                <h1>Control claro.<br>Decisiones seguras.</h1>
                <p>Centraliza la gestión del personal, supervisa accesos y convierte la actividad operativa en información útil.</p>
            </div>
            <div class="brand-status">
                <span><i></i>Sistema disponible</span>
                <span>Acceso protegido</span>
            </div>
        </section>
        <section class="auth-panel">
    <div class="login-container">
        <div class="logo">
            <h1>Bienvenido de nuevo</h1>
            <p>TekMess — Sistema de Notificación y Auditoría</p>
        </div>

        <c:if test="${not empty error}">
            <div class="error-msg" id="error-message">${error}</div>
        </c:if>
        <c:if test="${not empty exito}">
            <div class="success-msg" id="success-message">${exito}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth/login" method="POST" id="login-form" data-validate>
            <div class="form-group">
                <label for="usuario">Nombre de usuario</label>
                <input type="text" id="usuario" name="usuario" required
                       minlength="3" maxlength="30" pattern="[A-Za-z0-9._-]{3,30}"
                       title="Use de 3 a 30 caracteres: letras, números, punto, guion o guion bajo."
                       placeholder="Ingrese su usuario" autocomplete="username">
            </div>

            <div class="form-group">
                <label for="contrasena">Contraseña</label>
                <input type="password" id="contrasena" name="contrasena" required
                       minlength="8" maxlength="72"
                       placeholder="Ingrese su contraseña" autocomplete="current-password">
            </div>

            <button type="submit" class="btn-login" id="btn-iniciar-sesion">Iniciar Sesión</button>
        </form>

        <div class="recuperar-link">
            <a href="${pageContext.request.contextPath}/auth/recuperar" id="link-recuperar">
                ¿Olvidó su contraseña?
            </a>
        </div>
    </div>
        </section>
    </main>
    <script src="${pageContext.request.contextPath}/assets/js/validaciones.js"></script>
</body>
</html>
