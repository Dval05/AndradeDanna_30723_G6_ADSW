-- ============================================================
-- Base de Datos SNAAR - TekMess
-- Sistema de Notificación Automática y Auditoría de Reportes
-- PostgreSQL
-- ============================================================

-- Crear base de datos
-- CREATE DATABASE snaar_tekmess;

-- ============================================================
-- TABLA: empleados (RF-SNAAR-01)
-- ============================================================
CREATE TABLE IF NOT EXISTS empleados (
    cedula          VARCHAR(10) PRIMARY KEY,
    nombres         VARCHAR(200) NOT NULL,
    correo          VARCHAR(150) NOT NULL,
    rol             VARCHAR(20)  NOT NULL CHECK (rol IN ('GUARDIA', 'SUPERVISOR', 'CENTRALISTA', 'JEFE_LOGISTICA')),
    fecha_creacion  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLA: locaciones operativas
-- ============================================================
CREATE TABLE IF NOT EXISTS locaciones (
    id_locacion     SERIAL PRIMARY KEY,
    nombre          VARCHAR(120) NOT NULL UNIQUE,
    ciudad          VARCHAR(80) NOT NULL,
    direccion       VARCHAR(180) NOT NULL,
    responsable     VARCHAR(120) NOT NULL,
    capacidad       INTEGER NOT NULL DEFAULT 0,
    activa          BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE empleados
    ADD COLUMN IF NOT EXISTS id_locacion INTEGER REFERENCES locaciones(id_locacion);

-- ============================================================
-- TABLA: usuarios (RF-SNAAR-02)
-- ============================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario       SERIAL PRIMARY KEY,
    cedula           VARCHAR(10) NOT NULL UNIQUE REFERENCES empleados(cedula) ON DELETE CASCADE,
    nombre_usuario   VARCHAR(50) NOT NULL UNIQUE,
    contrasena_hash  VARCHAR(255) NOT NULL,
    contrasena_temporal VARCHAR(100),
    estado_cuenta    VARCHAR(15) NOT NULL DEFAULT 'ACTIVO' CHECK (estado_cuenta IN ('ACTIVO', 'BLOQUEADO', 'INACTIVO')),
    intentos_fallidos INTEGER NOT NULL DEFAULT 0,
    primer_acceso    BOOLEAN NOT NULL DEFAULT TRUE,
    ultimo_acceso    TIMESTAMP
);

-- ============================================================
-- TABLA: reportes (RF-SNAAR-03V2)
-- ============================================================
CREATE TABLE IF NOT EXISTS reportes (
    id_reporte              SERIAL PRIMARY KEY,
    fecha_generacion        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_inicio            TIMESTAMP NOT NULL,
    fecha_fin               TIMESTAMP NOT NULL,
    total_empleados_creados INTEGER NOT NULL DEFAULT 0,
    total_empleados_editados INTEGER NOT NULL DEFAULT 0,
    total_empleados_eliminados INTEGER NOT NULL DEFAULT 0,
    total_accesos_fallidos  INTEGER NOT NULL DEFAULT 0,
    generado_por            VARCHAR(100) NOT NULL
);

-- ============================================================
-- TABLA: anotaciones (RF-SNAAR-03V2)
-- ============================================================
CREATE TABLE IF NOT EXISTS anotaciones (
    id_anotacion    SERIAL PRIMARY KEY,
    id_reporte      INTEGER NOT NULL REFERENCES reportes(id_reporte) ON DELETE CASCADE,
    contenido       TEXT NOT NULL,
    fecha_creacion  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    autor           VARCHAR(100) NOT NULL
);

-- ============================================================
-- TABLA: auditoria_empleados (Observer – ObservadorAuditoria)
-- ============================================================
CREATE TABLE IF NOT EXISTS auditoria_empleados (
    id_auditoria    SERIAL PRIMARY KEY,
    tipo_evento     VARCHAR(30) NOT NULL,
    cedula_empleado VARCHAR(10),
    actor           VARCHAR(100) NOT NULL,
    datos           TEXT,
    fecha_evento    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLA: auditoria_accesos (Observer – ObservadorAuditoria)
-- ============================================================
CREATE TABLE IF NOT EXISTS auditoria_accesos (
    id_auditoria    SERIAL PRIMARY KEY,
    tipo_evento     VARCHAR(30) NOT NULL,
    nombre_usuario  VARCHAR(50),
    ip_origen       VARCHAR(50),
    datos           TEXT,
    fecha_evento    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- ÍNDICES
-- ============================================================
CREATE INDEX IF NOT EXISTS idx_empleados_rol ON empleados(rol);
CREATE INDEX IF NOT EXISTS idx_usuarios_estado ON usuarios(estado_cuenta);
CREATE INDEX IF NOT EXISTS idx_reportes_fechas ON reportes(fecha_inicio, fecha_fin);
CREATE INDEX IF NOT EXISTS idx_auditoria_emp_fecha ON auditoria_empleados(fecha_evento);
CREATE INDEX IF NOT EXISTS idx_auditoria_acc_fecha ON auditoria_accesos(fecha_evento);

-- ============================================================
-- DATOS INICIALES: Jefe Logística/Operación por defecto
-- ============================================================
INSERT INTO empleados (cedula, nombres, correo, rol)
VALUES ('1700000001', 'Administrador SNAAR', 'admin@tekmess.com', 'JEFE_LOGISTICA')
ON CONFLICT (cedula) DO NOTHING;

-- Contraseña: Admin@2026 (hash BCrypt)
INSERT INTO usuarios (cedula, nombre_usuario, contrasena_hash, contrasena_temporal, estado_cuenta, primer_acceso)
VALUES ('1700000001', 'admin', '$2a$12$uwku1iMBrTarT5Dpd.4wq.MMbfl0LorH8U4dKfljiN.WbJ/S/z50O', 'Admin@2026', 'ACTIVO', TRUE)
ON CONFLICT (cedula) DO NOTHING;
