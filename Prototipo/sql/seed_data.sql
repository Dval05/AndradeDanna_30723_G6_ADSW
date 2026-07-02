-- Seed datos realistas para SNAAR
-- Empleados
INSERT INTO empleados (cedula, nombres, correo, rol, fecha_creacion, fecha_modificacion) VALUES
('0102030405', 'Luis Enrique Gómez', 'luis.gomez@tekmess.com', 'GUARDIA', NOW() - INTERVAL '25 days', NOW() - INTERVAL '2 days'),
('0102030406', 'María Fernanda López', 'maria.lopez@tekmess.com', 'CENTRALISTA', NOW() - INTERVAL '40 days', NOW() - INTERVAL '10 days'),
('0102030407', 'Carlos Alberto Ruiz', 'carlos.ruiz@tekmess.com', 'SUPERVISOR', NOW() - INTERVAL '70 days', NOW() - INTERVAL '1 day'),
('0102030408', 'Ana Sofía Martínez', 'ana.martinez@tekmess.com', 'GUARDIA', NOW() - INTERVAL '10 days', NOW() - INTERVAL '3 days'),
('0102030409', 'Jorge Andrés Castillo', 'jorge.castillo@tekmess.com', 'CENTRALISTA', NOW() - INTERVAL '5 days', NOW() - INTERVAL '1 day')
ON CONFLICT (cedula) DO NOTHING;

-- Usuarios (misma contraseña inicial: Admin@2026 -> hash reutilizado)
INSERT INTO usuarios (cedula, nombre_usuario, contrasena_hash, estado_cuenta, primer_acceso, ultimo_acceso) VALUES
('0102030405', 'lgomez', '$2a$12$uwku1iMBrTarT5Dpd.4wq.MMbfl0LorH8U4dKfljiN.WbJ/S/z50O', 'ACTIVO', false, NOW() - INTERVAL '1 day'),
('0102030406', 'mlopez', '$2a$12$uwku1iMBrTarT5Dpd.4wq.MMbfl0LorH8U4dKfljiN.WbJ/S/z50O', 'ACTIVO', false, NOW() - INTERVAL '3 days'),
('0102030407', 'cruiz', '$2a$12$uwku1iMBrTarT5Dpd.4wq.MMbfl0LorH8U4dKfljiN.WbJ/S/z50O', 'ACTIVO', false, NOW() - INTERVAL '5 days'),
('0102030408', 'amartinez', '$2a$12$uwku1iMBrTarT5Dpd.4wq.MMbfl0LorH8U4dKfljiN.WbJ/S/z50O', 'ACTIVO', true, NOW() - INTERVAL '2 days'),
('0102030409', 'jcastillo', '$2a$12$uwku1iMBrTarT5Dpd.4wq.MMbfl0LorH8U4dKfljiN.WbJ/S/z50O', 'ACTIVO', false, NOW() - INTERVAL '1 hour')
ON CONFLICT (cedula) DO NOTHING;

-- Reportes de ejemplo
INSERT INTO reportes (fecha_inicio, fecha_fin, total_empleados_creados, total_empleados_editados, total_empleados_eliminados, total_accesos_fallidos, generado_por)
VALUES
(NOW()-INTERVAL '30 days', NOW()-INTERVAL '1 days', 5, 2, 0, 3, 'Administrador SNAAR') ON CONFLICT DO NOTHING;

-- Auditoría ejemplo
INSERT INTO auditoria_empleados (tipo_evento, cedula_empleado, actor, datos)
VALUES
('EMPLEADO_CREADO', '0102030405', 'Administrador SNAAR', 'Creación inicial del empleado Luis Enrique Gómez'),
('EMPLEADO_EDITADO', '0102030407', 'cruiz', 'Actualización de correo y rol');

INSERT INTO auditoria_accesos (tipo_evento, nombre_usuario, ip_origen, datos)
VALUES
('LOGIN_EXITOSO', 'lgomez', '192.168.1.10', 'Inicio de sesión exitoso desde web'),
('LOGIN_FALLIDO', 'unknown', '203.0.113.5', 'Intento fallido con credenciales inválidas');

-- Fin seed
