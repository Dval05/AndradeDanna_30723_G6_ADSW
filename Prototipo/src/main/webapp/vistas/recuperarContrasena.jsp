<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SNAAR - Recuperar Contraseña | TekMess</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body class="auth-page recovery-page">
    <div class="container">
        <p class="eyebrow">Acceso seguro</p>
        <h1>Recuperar contraseña</h1>
        <p class="subtitle">Valida tu usuario, correo institucional y define una clave nueva.</p>

        <c:if test="${not empty error}">
            <div class="message error">${error}</div>
        </c:if>
        <c:if test="${not empty exito}">
            <div class="message success">${exito}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth/recuperar" method="POST" data-validate>
            <div class="form-group">
                <label for="usuario">Usuario</label>
                <input type="text" id="usuario" name="usuario" placeholder="Nombre de usuario" required
                       minlength="3" maxlength="30" pattern="[A-Za-z0-9._-]{3,30}"
                       title="Use de 3 a 30 caracteres: letras, números, punto, guion o guion bajo.">
            </div>
            <div class="form-group">
                <label for="correo">Correo institucional</label>
                <input type="email" id="correo" name="correo" placeholder="correo@tekmess.com" required
                       maxlength="120"
                       pattern="^[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}$"
                       title="Ingrese un correo válido.">
            </div>
            <div class="form-group">
                <label for="contrasenaNueva">Nueva contraseña</label>
                <input type="password" id="contrasenaNueva" name="contrasenaNueva" placeholder="Mínimo 8 caracteres" required
                       minlength="8" maxlength="72"
                       pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d.*\d)(?=.*[!@#$%^&*()_+\-={}|;:',.<>?/]).{8,}$"
                       title="Mínimo 8 caracteres, 1 mayúscula, 1 minúscula, 2 números y 1 carácter especial.">
            </div>
            <div class="form-group">
                <label for="confirmacion">Confirmar contraseña</label>
                <input type="password" id="confirmacion" name="confirmacion" placeholder="Confirmar contraseña" required
                       minlength="8" maxlength="72">
            </div>
            <button type="submit" class="btn-primary">Restablecer contraseña</button>
        </form>

        <div class="links">
            <a href="${pageContext.request.contextPath}/auth/login">Volver al inicio de sesión</a>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/validaciones.js"></script>
</body>
</html>
