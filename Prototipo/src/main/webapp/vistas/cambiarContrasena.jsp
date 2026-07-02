<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>SNAAR - Cambiar Contraseña</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f9; display: flex; justify-content: center; align-items: center; min-height: 100vh; margin: 0; padding: 20px;}
        .container { background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); width: 380px; }
        h2 { text-align: center; color: #333; margin-top: 0; }
        .form-group { margin-bottom: 15px; position: relative; }
        label { display: block; margin-bottom: 5px; color: #555; font-size: 14px; font-weight: 600; }
        input[type="password"], input[type="text"] { width: 100%; padding: 10px 40px 10px 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 14px; }
        button { width: 100%; padding: 12px; background-color: #0056b3; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; font-weight: bold; margin-top: 10px; transition: background-color 0.2s; }
        button:hover { background-color: #004494; }
        .error { color: #d9534f; font-size: 14px; text-align: center; margin-bottom: 15px; font-weight: bold; }
        
        .toggle-pwd { position: absolute; right: 10px; top: 32px; cursor: pointer; font-size: 18px; color: #888; user-select: none; }
        
        /* Progress Bar */
        .progress-container { width: 100%; background-color: #e9ecef; border-radius: 4px; height: 8px; margin: 10px 0; overflow: hidden; }
        .progress-bar { height: 100%; width: 0; background-color: #dc3545; transition: width 0.3s, background-color 0.3s; }
        
        /* Requirements List */
        .req-list { list-style: none; padding: 0; margin: 10px 0 20px; font-size: 12px; color: #6c757d; }
        .req-list li { margin-bottom: 4px; display: flex; align-items: center; }
        .req-list li::before { content: "○"; margin-right: 6px; font-size: 14px; }
        .req-list li.valid { color: #28a745; }
        .req-list li.valid::before { content: "●"; color: #28a745; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Cambiar Contraseña</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>
        <p style="text-align:center; font-size: 13px; color: #666; margin-bottom: 20px;">Por seguridad, debes actualizar tu contraseña.</p>
        <form action="${pageContext.request.contextPath}/auth/cambiar-contrasena" method="POST">
            
            <div class="form-group">
                <label for="contrasenaActual">Contraseña Actual:</label>
                <input type="password" id="contrasenaActual" name="contrasenaActual" required>
                <span class="toggle-pwd" onclick="togglePassword('contrasenaActual', this)">👁</span>
            </div>
            
            <div class="form-group">
                <label for="contrasenaNueva">Nueva Contraseña:</label>
                <input type="password" id="contrasenaNueva" name="contrasenaNueva" required onkeyup="checkPasswordStrength()">
                <span class="toggle-pwd" onclick="togglePassword('contrasenaNueva', this)">👁</span>
            </div>

            <!-- Progress Bar -->
            <div class="progress-container">
                <div class="progress-bar" id="progressBar"></div>
            </div>

            <!-- Requirements List -->
            <ul class="req-list">
                <li id="req-length">Mínimo 8 caracteres</li>
                <li id="req-upper">Al menos 1 letra mayúscula</li>
                <li id="req-lower">Al menos 1 letra minúscula</li>
                <li id="req-num">Al menos 2 números</li>
                <li id="req-special">Al menos 1 carácter especial</li>
            </ul>
            
            <div class="form-group">
                <label for="confirmacion">Confirmar Contraseña:</label>
                <input type="password" id="confirmacion" name="confirmacion" required>
                <!-- No se incluye el ojito en este campo -->
            </div>
            
            <button type="submit">Guardar Contraseña</button>
        </form>
    </div>

    <script>
        function togglePassword(inputId, iconElement) {
            const input = document.getElementById(inputId);
            if (input.type === "password") {
                input.type = "text";
                iconElement.innerHTML = "🙈"; 
            } else {
                input.type = "password";
                iconElement.innerHTML = "👁";
            }
        }

        function checkPasswordStrength() {
            const pwd = document.getElementById('contrasenaNueva').value;
            
            // Check rules
            const hasLength = pwd.length >= 8;
            const hasUpper = /[A-Z]/.test(pwd);
            const hasLower = /[a-z]/.test(pwd);
            // Count digits (needs at least 2)
            const numMatches = pwd.match(/\d/g);
            const hasNum = numMatches && numMatches.length >= 2;
            const hasSpecial = /[!@#$%^&*()_+\-={}|;:',.<>?/]/.test(pwd);

            // Update list styles
            updateReq('req-length', hasLength);
            updateReq('req-upper', hasUpper);
            updateReq('req-lower', hasLower);
            updateReq('req-num', hasNum);
            updateReq('req-special', hasSpecial);

            // Calculate progress
            let score = 0;
            if(hasLength) score += 20;
            if(hasUpper) score += 20;
            if(hasLower) score += 20;
            if(hasNum) score += 20;
            if(hasSpecial) score += 20;

            const progressBar = document.getElementById('progressBar');
            progressBar.style.width = score + '%';

            // Change color based on score
            if (score <= 40) {
                progressBar.style.backgroundColor = '#dc3545'; // Red
            } else if (score <= 80) {
                progressBar.style.backgroundColor = '#ffc107'; // Yellow
            } else {
                progressBar.style.backgroundColor = '#28a745'; // Green
            }
        }

        function updateReq(elementId, isValid) {
            const el = document.getElementById(elementId);
            if (isValid) {
                el.classList.add('valid');
            } else {
                el.classList.remove('valid');
            }
        }
    </script>
</body>
</html>
