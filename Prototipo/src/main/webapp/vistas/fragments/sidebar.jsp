<div class="card" style="padding:12px;">
    <div style="display:flex;align-items:center;gap:12px;margin-bottom:14px;">
        <div style="width:40px;height:40px;border-radius:8px;background:linear-gradient(135deg,#6366f1,#818cf8);"></div>
        <div>
            <div style="font-weight:700;color:#e6eeff;">SNAAR</div>
            <div class="small">Panel de control</div>
        </div>
    </div>
    <nav style="display:flex;flex-direction:column;gap:6px;">
        <a href="#tab-overview" class="sidebar-link btn-link active">Overview</a>
        <a href="#tab-users" class="sidebar-link btn-link">Usuarios</a>
        <a href="#tab-roles" class="sidebar-link btn-link">Roles</a>
        <a href="#tab-reports" class="sidebar-link btn-link">Reportes</a>
        <a href="#tab-settings" class="sidebar-link btn-link">Configuración</a>
    </nav>
    <hr style="margin:12px 0;border:none;border-top:1px solid rgba(99,102,241,0.04);" />
    <div style="font-size:13px;color:#94a3b8;">Acciones</div>
    <div style="display:flex;gap:8px;margin-top:8px;flex-wrap:wrap;">
        <a class="btn" href="${pageContext.request.contextPath}/empleados/formulario">Nuevo empleado</a>
        <a class="btn" href="${pageContext.request.contextPath}/reportes">Generar reporte</a>
    </div>
</div>

<style>
    .btn-link { display:block; padding:10px 8px; color:#cbd5e1; text-decoration:none; border-radius:6px; }
    .btn-link:hover { background: rgba(255,255,255,0.02); color:#e2e8f0; }
    .btn-link.active { background: linear-gradient(90deg, rgba(99,102,241,0.12), rgba(129,140,248,0.06)); color:#e2e8f0; }
</style>

<script>
    // Sidebar interactions: switch visible tab sections
    document.addEventListener('click', function(e){
        var t = e.target;
        if (t.classList && t.classList.contains('sidebar-link')) {
            e.preventDefault();
            document.querySelectorAll('.sidebar-link').forEach(function(a){ a.classList.remove('active'); });
            t.classList.add('active');
            var id = t.getAttribute('href').substring(1);
            document.querySelectorAll('.tab-pane').forEach(function(p){ p.style.display = 'none'; });
            var target = document.getElementById(id);
            if (target) target.style.display = 'block';
        }
    });
    // Show overview by default
    window.addEventListener('load', function(){ var el = document.getElementById('tab-overview'); if (el) el.style.display='block'; });
</script>
