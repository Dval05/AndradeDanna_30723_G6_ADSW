# Prototipo TekMess — SNAAR
## Especificación de Pantallas para Figma
### RF1 · RF2V2 · RF3V2 — Aplicación Móvil Flutter

---

## 1. Componentes Base Reutilizables

| Componente | Descripción |
|---|---|
| `CampoTexto` | Etiqueta superior + input + mensaje de error debajo |
| `BotonPrimario` | Fondo color primario, texto blanco, ancho completo |
| `BotonSecundario` | Borde color primario, texto primario, ancho completo |
| `BotonPeligro` | Fondo rojo, texto blanco |
| `MensajeError` | Banner rojo con texto de error |
| `MensajeExito` | Banner verde con texto de confirmación |
| `ItemLista` | Tarjeta con nombre + cédula + rol + íconos de acción |
| `TarjetaMenu` | Tarjeta grande con ícono y etiqueta, navegable |
| `TarjetaMetrica` | Tarjeta con etiqueta de métrica y valor numérico destacado |
| `TarjetaReporte` | Resumen de reporte con período y métricas principales |
| `ModalConfirmacion` | Overlay oscuro con cuadro de diálogo centrado |

---

## 2. Mapa de Navegación

```
[S01 Inicio de Sesión]
  ├── Login exitoso ─────────────────────────────→ [S05 Dashboard]
  ├── Primer acceso ─────────────────────────────→ [S02 Cambiar Contraseña]
  └── Olvidé mi contraseña ──────────────────────→ [S03 Recuperar — Verificación]

[S02 Cambiar Contraseña]
  └── Cambio exitoso ────────────────────────────→ [S05 Dashboard]

[S03 Recuperar — Verificación]
  └── Datos válidos ─────────────────────────────→ [S04 Recuperar — Nueva Contraseña]

[S04 Recuperar — Nueva Contraseña]
  └── Restablecimiento exitoso ──────────────────→ [S01 Inicio de Sesión]

[S05 Dashboard]
  ├── (Jefe Logística) Gestionar Empleados ──────→ [S06 Listado de Empleados]
  ├── (Jefe Logística / Supervisor) Reportes ────→ [S11 Módulo Reportes]
  ├── Configuración ─────────────────────────────→ [S02 Cambiar Contraseña]
  └── Cerrar Sesión ─────────────────────────────→ [S01 Inicio de Sesión]

[S06 Listado de Empleados]
  ├── Botón "+" Nuevo ────────────────────────────→ [S07 Crear Empleado]
  ├── Ícono Editar ───────────────────────────────→ [S08 Editar Empleado]
  └── Ícono Eliminar ─────────────────────────────→ [S09 Modal Confirmar Eliminación]

[S07 Crear Empleado]
  ├── Registro exitoso ──────────────────────────→ [S10 Modal Credenciales Generadas]
  └── Cancelar ──────────────────────────────────→ [S06 Listado de Empleados]

[S08 Editar Empleado]
  ├── Actualización exitosa ─────────────────────→ [S06 Listado de Empleados]
  └── Cancelar ──────────────────────────────────→ [S06 Listado de Empleados]

[S09 Modal Confirmar Eliminación]
  ├── Confirmar ──────────────────────────────────→ [S06 Listado de Empleados]
  └── Cancelar ──────────────────────────────────→ [S06 Listado de Empleados]

[S10 Modal Credenciales Generadas]
  └── Aceptar ────────────────────────────────────→ [S06 Listado de Empleados]

[S11 Módulo Reportes]
  ├── Generar Reporte ───────────────────────────→ [S12 Formulario Generar Reporte]
  ├── Historial ─────────────────────────────────→ [S14 Historial de Reportes]
  └── Volver ────────────────────────────────────→ [S05 Dashboard]

[S12 Formulario Generar Reporte]
  ├── Reporte generado ──────────────────────────→ [S13 Ver Reporte]
  └── Cancelar ──────────────────────────────────→ [S11 Módulo Reportes]

[S13 Ver Reporte]
  ├── Exportar PDF ──────────────────────────────→ (descarga, feedback en mismo frame)
  └── Volver ────────────────────────────────────→ [S11 Módulo Reportes]

[S14 Historial de Reportes]
  ├── Seleccionar reporte ───────────────────────→ [S15 Detalle de Reporte]
  └── Volver ────────────────────────────────────→ [S11 Módulo Reportes]

[S15 Detalle de Reporte]
  ├── Exportar PDF ──────────────────────────────→ (descarga, feedback en mismo frame)
  └── Volver ────────────────────────────────────→ [S14 Historial de Reportes]
```

---

## 3. Especificación de Pantallas

---

### RF2V2 — Autenticación

---

#### S01 — Inicio de Sesión
**RF:** RF-SNAAR-02.01 | **Actor:** Usuario (todos los roles)

```
┌─────────────────────────┐
│                         │
│      [Logo TekMess]     │
│       TEKMESS           │
│                         │
│  Nombre de usuario      │
│  [                   ]  │
│                         │
│  Contraseña         [👁] │
│  [                   ]  │
│                         │
│  [   INICIAR SESIÓN   ] │
│                         │
│   ¿Olvidó su contraseña?│
│                         │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | Image | Logo TekMess | Estático |
| 2 | CampoTexto | Nombre de usuario | Teclado alfanumérico |
| 3 | CampoTexto | Contraseña | Tipo password, ícono mostrar/ocultar |
| 4 | BotonPrimario | INICIAR SESIÓN | Valida → S05 o S02 según primer acceso |
| 5 | TextLink | ¿Olvidó su contraseña? | → S03 |

**Estados:**

| Estado | Descripción visual |
|---|---|
| Normal | Sin mensajes adicionales |
| Error — Credenciales incorrectas | `MensajeError` "Usuario o contraseña incorrectos." Campos con borde rojo |
| Error — Cuenta bloqueada | `MensajeError` "Cuenta bloqueada. Contacte al administrador del sistema." Botón deshabilitado |
| Error — Sesión expirada | `MensajeError` "Sesión expirada. Inicie sesión nuevamente." |

---

#### S02 — Cambiar Contraseña
**RF:** RF-SNAAR-02.02 | **Actor:** Usuario (todos los roles)
> Se usa en dos contextos: **primer acceso** (sin campo contraseña actual, con banner informativo) y **cambio voluntario** desde configuración (con campo contraseña actual).

```
┌─────────────────────────┐
│ ←  Cambiar Contraseña   │
│─────────────────────────│
│ [Banner azul: "Debe     │
│  cambiar su contraseña  │
│  para continuar"]       │
│ (solo en primer acceso) │
│─────────────────────────│
│  Contraseña actual  [👁] │
│  [                   ]  │
│  (oculto en 1er acceso) │
│                         │
│  Nueva contraseña   [👁] │
│  [                   ]  │
│                         │
│  Confirmar contraseña   │
│  [                   ]  │
│                         │
│  Mín. 8 caracteres,     │
│  1 mayúscula, 1 minús., │
│  2 números, 1 especial  │
│                         │
│  [ CONFIRMAR CAMBIO  ]  │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | AppBar | ← Cambiar Contraseña | Ocultar flecha si es primer acceso |
| 2 | Banner (condicional) | "Debe cambiar su contraseña para continuar" | Solo visible en primer acceso |
| 3 | CampoTexto (condicional) | Contraseña actual | Oculto en primer acceso; tipo password |
| 4 | CampoTexto | Nueva contraseña | Tipo password, ícono mostrar/ocultar |
| 5 | CampoTexto | Confirmar nueva contraseña | Tipo password |
| 6 | TextInfo | Política de contraseñas | Texto descriptivo pequeño |
| 7 | BotonPrimario | CONFIRMAR CAMBIO | Valida → S05 (primer acceso) o permanece |

**Estados:**

| Estado | Descripción visual |
|---|---|
| Error — Contraseña actual incorrecta | Campo 3 borde rojo, "La contraseña actual no es correcta." |
| Error — No coinciden | Campos 4 y 5 borde rojo, "Las contraseñas no coinciden." |
| Error — No cumple política | Campo 4 borde rojo, "La contraseña debe tener mínimo 8 caracteres, 1 mayúscula, 1 minúscula, 2 números y 1 carácter especial." |
| Error — Igual a la actual | Campo 4 borde rojo, "La nueva contraseña no puede ser igual a la contraseña actual." |
| Éxito | `MensajeExito` "Contraseña actualizada exitosamente." |

**Variantes en Figma:** `S02_PrimerAcceso` / `S02_CambioVoluntario`

---

#### S03 — Recuperar Contraseña — Verificación de Identidad
**RF:** RF-SNAAR-02.03 (pasos 1–8) | **Actor:** Usuario

```
┌─────────────────────────┐
│ ← Recuperar Contraseña  │
│─────────────────────────│
│  Ingrese sus datos para │
│  verificar su identidad │
│                         │
│  Correo institucional   │
│  [                   ]  │
│                         │
│  Nombre de usuario      │
│  [                   ]  │
│                         │
│  [  CONFIRMAR DATOS  ]  │
│                         │
│       Volver al inicio  │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | AppBar | ← Recuperar Contraseña | → S01 |
| 2 | TextBody | Instrucción de verificación | Estático |
| 3 | CampoTexto | Correo institucional | Teclado email |
| 4 | CampoTexto | Nombre de usuario | Teclado alfanumérico |
| 5 | BotonPrimario | CONFIRMAR DATOS | Valida → S04 si correcto |
| 6 | TextLink | Volver al inicio | → S01 |

**Estados:**

| Estado | Descripción visual |
|---|---|
| Error — Datos no válidos | `MensajeError` "Los datos ingresados no corresponden a un usuario registrado." (mensaje genérico, sin indicar qué campo falla) |
| Error — Cuenta bloqueada | `MensajeError` "Cuenta bloqueada. Contacte al administrador del sistema." |

---

#### S04 — Recuperar Contraseña — Nueva Contraseña
**RF:** RF-SNAAR-02.03 (pasos 9–14) | **Actor:** Usuario

```
┌─────────────────────────┐
│ ←   Nueva Contraseña    │
│─────────────────────────│
│  Establezca su nueva    │
│  contraseña de acceso   │
│                         │
│  Nueva contraseña   [👁] │
│  [                   ]  │
│                         │
│  Confirmar contraseña   │
│  [                   ]  │
│                         │
│  Mín. 8 caracteres,     │
│  1 mayúscula, 1 minús., │
│  2 números, 1 especial  │
│                         │
│  [    RESTABLECER    ]  │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | AppBar | ← Nueva Contraseña | Deshabilitado (no puede retroceder) |
| 2 | TextBody | Instrucción | Estático |
| 3 | CampoTexto | Nueva contraseña | Tipo password, ícono mostrar/ocultar |
| 4 | CampoTexto | Confirmar contraseña | Tipo password |
| 5 | TextInfo | Política de contraseñas | Texto descriptivo pequeño |
| 6 | BotonPrimario | RESTABLECER | Valida → S01 con mensaje de éxito |

**Estados:** Mismos patrones de error que S02 para política y coincidencia.

---

### RF1 — Gestionar Empleados

---

#### S05 — Dashboard (Pantalla Principal)
**RF:** Punto de entrada post-login | **Actor:** Todos los roles

```
┌─────────────────────────┐
│  TekMess SNAAR    [⚙️]  │
│─────────────────────────│
│  Bienvenido,            │
│  [Nombre del usuario]   │
│  Rol: [Rol asignado]    │
│─────────────────────────│
│                         │
│  ┌─────────────────────┐│
│  │  👥  Gestionar      ││
│  │      Empleados      ││
│  └─────────────────────┘│
│  (solo Jefe Logística)  │
│                         │
│  ┌─────────────────────┐│
│  │  📊  Reportes       ││
│  │      Analíticos     ││
│  └─────────────────────┘│
│  (Jefe Logística y      │
│   Supervisor)           │
│                         │
│  [   CERRAR SESIÓN   ]  │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | AppBar | TekMess SNAAR + [⚙️] | ⚙️ → S02 (cambio voluntario) |
| 2 | TextTitle | Bienvenido, [Nombre] | Dinámico |
| 3 | TextSubtitle | Rol: [Rol del usuario] | Dinámico |
| 4 | TarjetaMenu | 👥 Gestionar Empleados | Solo Jefe Logística → S06 |
| 5 | TarjetaMenu | 📊 Reportes Analíticos | Jefe Logística y Supervisor → S11 |
| 6 | BotonSecundario | CERRAR SESIÓN | Invalida sesión → S01 |

**Variantes en Figma:** `S05_JefeLogistica` (tarjetas 4 y 5) / `S05_Supervisor` (solo tarjeta 5)

---

#### S06 — Listado de Empleados
**RF:** RF-SNAAR-01.04 | **Actor:** Jefe Logística/Operación

```
┌─────────────────────────┐
│ ←  Empleados      [+]  │
│─────────────────────────│
│  [🔍 Buscar por nombre  │
│      o cédula...     ]  │
│─────────────────────────│
│  Juan Pérez             │
│  Cédula: 1234567890     │
│  Rol: Guardia  [✏️] [🗑️] │
│─────────────────────────│
│  María López            │
│  Cédula: 0987654321     │
│  Rol: Supervisor [✏️][🗑️]│
│─────────────────────────│
│  Carlos Vega            │
│  Cédula: 1122334455     │
│  Rol: Centralista [✏️][🗑️]│
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | AppBar | ← Empleados + [+] | ← → S05; [+] → S07 |
| 2 | CampoBusqueda | Buscar por nombre o cédula... | Filtra la lista en tiempo real |
| 3 | ListView | Lista de ItemLista | Scroll vertical |
| 4 | ItemLista | Nombre + Cédula + Rol | ✏️ → S08; 🗑️ → S09 |

**Estados:**

| Estado | Descripción visual |
|---|---|
| Normal | Lista con empleados registrados |
| Vacío | "No hay empleados registrados en el sistema." en lugar de la lista |
| Búsqueda sin resultados | "No se encontraron empleados con ese criterio." |
| Tras eliminar | `MensajeExito` "Empleado dado de baja exitosamente." (banner superior) |
| Tras editar | `MensajeExito` "Empleado actualizado exitosamente." (banner superior) |

---

#### S07 — Crear Empleado
**RF:** RF-SNAAR-01.01 | **Actor:** Jefe Logística/Operación

```
┌─────────────────────────┐
│ ←  Nuevo Empleado       │
│─────────────────────────│
│  Cédula                 │
│  [                   ]  │
│                         │
│  Nombres completos      │
│  [                   ]  │
│                         │
│  Rol                    │
│  [▼ Seleccionar rol   ] │
│                         │
│  Correo institucional   │
│  [                   ]  │
│                         │
│  [      REGISTRAR    ]  │
│  [      CANCELAR     ]  │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | AppBar | ← Nuevo Empleado | → S06 sin guardar |
| 2 | CampoTexto | Cédula | Teclado numérico, máximo 10 dígitos |
| 3 | CampoTexto | Nombres completos | Teclado texto |
| 4 | Dropdown | Rol | Opciones: Guardia / Supervisor / Centralista / Jefe Logística/Operación |
| 5 | CampoTexto | Correo institucional | Teclado email |
| 6 | BotonPrimario | REGISTRAR | Valida datos → S10 si éxito |
| 7 | BotonSecundario | CANCELAR | → S06 sin guardar |

**Estados:**

| Estado | Descripción visual |
|---|---|
| Error — Cédula inválida | Campo 2 borde rojo, "La cédula debe contener exactamente 10 dígitos numéricos." |
| Error — Cédula duplicada | Campo 2 borde rojo, "La cédula ingresada ya se encuentra registrada en el sistema." |
| Error — Correo inválido | Campo 5 borde rojo, "El correo ingresado no tiene un formato válido." |

---

#### S08 — Editar Empleado
**RF:** RF-SNAAR-01.02 | **Actor:** Jefe Logística/Operación

> Misma estructura que S07 con los siguientes cambios:
> - Título: "Editar Empleado"
> - Campo Cédula: deshabilitado (solo lectura), muestra la cédula actual
> - Campos Nombres, Rol y Correo: precargados con datos actuales, editables
> - Botón primario: "ACTUALIZAR"

```
┌─────────────────────────┐
│ ←  Editar Empleado      │
│─────────────────────────│
│  Cédula (no editable)   │
│  [  1234567890       ]  │
│                         │
│  Nombres completos      │
│  [  Juan Pérez       ]  │
│                         │
│  Rol                    │
│  [▼ Guardia          ]  │
│                         │
│  Correo institucional   │
│  [  j.perez@...      ]  │
│                         │
│  [      ACTUALIZAR   ]  │
│  [      CANCELAR     ]  │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | AppBar | ← Editar Empleado | → S06 sin guardar |
| 2 | CampoTexto (solo lectura) | Cédula | No editable, fondo gris |
| 3 | CampoTexto | Nombres completos | Precargado, editable |
| 4 | Dropdown | Rol | Precargado con rol actual, editable |
| 5 | CampoTexto | Correo institucional | Precargado, editable |
| 6 | BotonPrimario | ACTUALIZAR | Valida → S06 con MensajeExito |
| 7 | BotonSecundario | CANCELAR | → S06 sin guardar |

**Estados:** Mismos errores de validación que S07 (excepto cédula duplicada).

---

#### S09 — Modal Confirmar Eliminación
**RF:** RF-SNAAR-01.03 | **Actor:** Jefe Logística/Operación

```
┌─────────────────────────┐
│                         │
│  ╔═══════════════════╗  │
│  ║  ⚠️ Dar de baja   ║  │
│  ╠═══════════════════╣  │
│  ║ ¿Está seguro que  ║  │
│  ║ desea dar de baja ║  │
│  ║ a:                ║  │
│  ║                   ║  │
│  ║  Juan Pérez       ║  │
│  ║  Cédula:          ║  │
│  ║  1234567890       ║  │
│  ║                   ║  │
│  ║ Esta acción no    ║  │
│  ║ puede deshacerse. ║  │
│  ║                   ║  │
│  ║[CANCELAR][CONFIRMAR]║ │
│  ╚═══════════════════╝  │
│                         │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | Overlay | Fondo semitransparente | Bloquea interacción con S06 |
| 2 | ModalTitle | ⚠️ Dar de baja | Estático |
| 3 | TextBody | Nombre y cédula del empleado (dinámico) | Estático |
| 4 | TextWarning | "Esta acción no puede deshacerse." | Texto en rojo |
| 5 | BotonSecundario | CANCELAR | Cierra modal → S06 sin cambios |
| 6 | BotonPeligro | CONFIRMAR | Elimina → S06 con MensajeExito |

---

#### S10 — Modal Credenciales Generadas
**RF:** RF-SNAAR-01.06 | **Actor:** Jefe Logística/Operación

```
┌─────────────────────────┐
│                         │
│  ╔═══════════════════╗  │
│  ║ ✅ Empleado       ║  │
│  ║    Registrado     ║  │
│  ╠═══════════════════╣  │
│  ║ Las credenciales  ║  │
│  ║ fueron generadas  ║  │
│  ║ exitosamente.     ║  │
│  ║                   ║  │
│  ║ Usuario:          ║  │
│  ║ [juan.perez]      ║  │
│  ║                   ║  │
│  ║ Contraseña:       ║  │
│  ║ [••••••••]  [👁️]  ║  │
│  ║                   ║  │
│  ║ ⚠️ Entregue estas ║  │
│  ║ credenciales de   ║  │
│  ║ forma segura.     ║  │
│  ║                   ║  │
│  ║   [   ACEPTAR  ]  ║  │
│  ╚═══════════════════╝  │
│                         │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | Overlay | Fondo semitransparente | Bloquea interacción |
| 2 | ModalTitle | ✅ Empleado Registrado | Estático |
| 3 | TextDisplay | Nombre de usuario generado | Solo lectura, copiable al portapapeles |
| 4 | TextDisplay | Contraseña temporal | Oculta por defecto; ícono 👁️ para revelar |
| 5 | TextWarning | ⚠️ Entregue estas credenciales de forma segura | Texto pequeño color naranja |
| 6 | BotonPrimario | ACEPTAR | Cierra modal → S06 |

---

### RF3V2 — Reportes Analíticos

---

#### S11 — Módulo Reportes
**RF:** RF-SNAAR-03V2 (pantalla de entrada) | **Actor:** Jefe Logística/Operación, Supervisor

```
┌─────────────────────────┐
│ ← Reportes Analíticos   │
│─────────────────────────│
│                         │
│  ┌─────────────────────┐│
│  │  📋  Generar        ││
│  │      Reporte        ││
│  └─────────────────────┘│
│                         │
│  ┌─────────────────────┐│
│  │  🕐  Historial de   ││
│  │      Reportes       ││
│  └─────────────────────┘│
│                         │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | AppBar | ← Reportes Analíticos | → S05 |
| 2 | TarjetaMenu | 📋 Generar Reporte | → S12 |
| 3 | TarjetaMenu | 🕐 Historial de Reportes | → S14 |

---

#### S12 — Formulario Generar Reporte
**RF:** RF-SNAAR-03V2.01 | **Actor:** Jefe Logística/Operación, Supervisor

```
┌─────────────────────────┐
│ ←  Generar Reporte      │
│─────────────────────────│
│  Seleccione el período  │
│  a analizar:            │
│                         │
│  Fecha de inicio        │
│  [📅  DD / MM / AAAA ]  │
│                         │
│  Fecha de fin           │
│  [📅  DD / MM / AAAA ]  │
│                         │
│  [      GENERAR      ]  │
│  [      CANCELAR     ]  │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | AppBar | ← Generar Reporte | → S11 |
| 2 | TextBody | Instrucción de período | Estático |
| 3 | DatePicker | Fecha de inicio | Selector de fecha, obligatorio |
| 4 | DatePicker | Fecha de fin | Selector de fecha, obligatorio |
| 5 | BotonPrimario | GENERAR | Valida fechas y genera → S13 |
| 6 | BotonSecundario | CANCELAR | → S11 |

**Estados:**

| Estado | Descripción visual |
|---|---|
| Error — Rango inválido | `MensajeError` "El período de consulta no es válido. La fecha de fin debe ser posterior a la fecha de inicio." Campos con borde rojo |
| Error — Sin datos | `MensajeError` "No existen datos registrados para el período seleccionado." |
| Cargando | Indicador de progreso circular en lugar del botón |

---

#### S13 — Ver Reporte Generado
**RF:** RF-SNAAR-03V2.01 (resultado) | **Actor:** Jefe Logística/Operación, Supervisor

```
┌─────────────────────────┐
│ ←  Reporte Analítico    │
│─────────────────────────│
│  Período:               │
│  01/01/2026 – 31/01/2026│
│─────────────────────────│
│  ┌─────────────────────┐│
│  │ Empleados           ││
│  │ Gestionados         ││
│  │          [  12  ]   ││
│  └─────────────────────┘│
│  ┌─────────────────────┐│
│  │ Empleados Editados  ││
│  │          [   5  ]   ││
│  └─────────────────────┘│
│  ┌─────────────────────┐│
│  │ Accesos Fallidos    ││
│  │          [   3  ]   ││
│  └─────────────────────┘│
│─────────────────────────│
│  Anotaciones            │
│  ┌─────────────────────┐│
│  │                     ││
│  └─────────────────────┘│
│  [ GUARDAR ANOTACIÓN ]  │
│─────────────────────────│
│  [   EXPORTAR PDF    ]  │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | AppBar | ← Reporte Analítico | → S11 |
| 2 | TextSubtitle | Período: [fecha inicio] – [fecha fin] | Dinámico, solo lectura |
| 3 | TarjetaMetrica | Empleados Gestionados + [número] | Solo lectura |
| 4 | TarjetaMetrica | Empleados Editados + [número] | Solo lectura |
| 5 | TarjetaMetrica | Accesos Fallidos + [número] | Solo lectura |
| 6 | TextArea | Anotaciones | Editable por el actor |
| 7 | BotonSecundario | GUARDAR ANOTACIÓN | Persiste anotación, muestra MensajeExito |
| 8 | BotonPrimario | EXPORTAR PDF | Genera PDF → MensajeExito o MensajeError |

**Estados:**

| Estado | Descripción visual |
|---|---|
| Exportar — éxito | `MensajeExito` "PDF generado exitosamente." |
| Exportar — error | `MensajeError` "No se pudo generar el archivo PDF. Intente nuevamente." |

---

#### S14 — Historial de Reportes
**RF:** RF-SNAAR-03V2.02 | **Actor:** Jefe Logística/Operación, Supervisor

```
┌─────────────────────────┐
│ ←  Historial Reportes   │
│─────────────────────────│
│  Filtrar por período:   │
│  Desde [📅 DD/MM/AAAA]  │
│  Hasta [📅 DD/MM/AAAA]  │
│  [      CONSULTAR    ]  │
│─────────────────────────│
│  01/01/2026–31/01/2026  │
│  Gestionados: 12        │
│  Accesos fallidos: 3 [>]│
│─────────────────────────│
│  01/12/2025–31/12/2025  │
│  Gestionados: 7         │
│  Accesos fallidos: 1 [>]│
│─────────────────────────│
│  ...                    │
└─────────────────────────┘
```

**Componentes:**

| # | Tipo | Etiqueta | Comportamiento |
|---|---|---|---|
| 1 | AppBar | ← Historial Reportes | → S11 |
| 2 | DatePicker | Desde (opcional) | Filtro de inicio; vacío = sin límite |
| 3 | DatePicker | Hasta (opcional) | Filtro de fin; vacío = sin límite |
| 4 | BotonPrimario | CONSULTAR | Aplica filtros y recarga lista |
| 5 | ListView | Lista de TarjetaReporte | Scroll vertical; cada item [>] → S15 |

**Estados:**

| Estado | Descripción visual |
|---|---|
| Sin resultados | "No se encontraron reportes para el período seleccionado." |
| Error — Rango inválido | `MensajeError` "El período de consulta no es válido." |

---

#### S15 — Detalle de Reporte
**RF:** RF-SNAAR-03V2.02 (selección del historial) | **Actor:** Jefe Logística/Operación, Supervisor

> Misma estructura que S13, con los siguientes cambios:
> - AppBar: "← Detalle de Reporte" → retrocede a S14
> - Todos los datos del reporte son de solo lectura (incluyendo métricas)
> - Las anotaciones previas del reporte aparecen precargadas y son editables

**Componentes:** Idénticos a S13.

---

## 4. Resumen de Frames en Figma

| ID | Pantalla | RF | Variantes sugeridas |
|---|---|---|---|
| S01 | Inicio de Sesión | RF-SNAAR-02.01 | Normal / Error credenciales / Cuenta bloqueada / Sesión expirada |
| S02 | Cambiar Contraseña | RF-SNAAR-02.02 | Primer acceso / Cambio voluntario / Estados de error |
| S03 | Recuperar — Verificación | RF-SNAAR-02.03 | Normal / Error datos / Cuenta bloqueada |
| S04 | Recuperar — Nueva Contraseña | RF-SNAAR-02.03 | Normal / Error validación |
| S05 | Dashboard | — | Jefe Logística / Supervisor |
| S06 | Listado de Empleados | RF-SNAAR-01.04 | Con empleados / Vacío / Tras eliminar / Tras editar |
| S07 | Crear Empleado | RF-SNAAR-01.01 | Normal / Errores de validación |
| S08 | Editar Empleado | RF-SNAAR-01.02 | Normal / Errores de validación |
| S09 | Modal Confirmar Eliminación | RF-SNAAR-01.03 | Normal |
| S10 | Modal Credenciales Generadas | RF-SNAAR-01.06 | Contraseña oculta / Contraseña visible |
| S11 | Módulo Reportes | RF-SNAAR-03V2 | Normal |
| S12 | Formulario Generar Reporte | RF-SNAAR-03V2.01 | Normal / Error rango / Error sin datos / Cargando |
| S13 | Ver Reporte Generado | RF-SNAAR-03V2.01 | Normal / Exportar éxito / Exportar error |
| S14 | Historial de Reportes | RF-SNAAR-03V2.02 | Con reportes / Vacío / Error rango |
| S15 | Detalle de Reporte | RF-SNAAR-03V2.02 | Normal / Exportar éxito / Exportar error |

**Total: 15 pantallas — ~35 variantes/estados**
