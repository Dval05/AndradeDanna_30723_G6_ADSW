<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Acceso restringido | SNAAR</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css?v=contrast-darkmode-20260711">
</head>
<body class="app-page error-page">
    <jsp:include page="/vistas/fragments/navbar.jsp" />
    <main class="error-state">
        <div class="error-code">403</div>
        <p class="eyebrow">Permisos insuficientes</p>
        <h1>Esta area requiere otro nivel de acceso</h1>
        <p>Tu sesion esta activa, pero tu rol no puede operar en <strong>${recursoSolicitado}</strong>.</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/dashboard">Volver al resumen</a>
    </main>
</body>
</html>
