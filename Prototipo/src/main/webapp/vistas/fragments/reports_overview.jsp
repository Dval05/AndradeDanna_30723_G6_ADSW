<div id="tab-reports" class="tab-pane" style="display:none;">
    <div class="card">
        <h4>Reportes</h4>
        <div class="muted" style="margin-top:6px;">Total reportes: <strong><%= request.getAttribute("totalReportes") != null ? request.getAttribute("totalReportes") : 0 %></strong></div>
        <div style="margin-top:12px;">
            <canvas id="reportsChart" width="600" height="260"></canvas>
        </div>
    </div>
</div>
