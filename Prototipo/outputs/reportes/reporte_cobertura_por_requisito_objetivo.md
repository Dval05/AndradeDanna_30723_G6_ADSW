# Reporte de cobertura por requisito funcional - SNAAR TekMess

**Tipo de reporte:** Cobertura objetivo por requisito funcional  
**Fecha de generación:** 2026-07-30  
**Proyecto:** SNAAR - TekMess  
**Tecnología de pruebas:** JUnit 5, Maven, JaCoCo  

## Introducción

Este documento presenta una distribución de cobertura objetivo por requisito funcional del prototipo SNAAR TekMess. Los porcentajes se expresan como una meta de cobertura esperada para cada requisito evaluado mediante pruebas unitarias, considerando escenarios exitosos, validaciones, errores controlados y reglas de negocio asociadas.

La cobertura se organiza por requisito para facilitar el seguimiento de calidad del Sprint y evidenciar qué módulos cuentan con mayor respaldo de pruebas automatizadas.

> Nota: Este reporte representa una cobertura objetivo/estimada por requisito. Para resultados reales de ejecución se debe contrastar con los reportes generados por JaCoCo en `target/site/jacoco/index.html`.

## Resumen general

| Indicador | Resultado |
|---|---:|
| Requisitos evaluados | 9 |
| Cobertura mínima definida | 87% |
| Cobertura máxima definida | 100% |
| Promedio de cobertura objetivo | 92.67% |
| Estado general | Aceptable |

## Cobertura por requisito funcional

| Código | Requisito funcional | Cobertura objetivo | Estado |
|---|---|---:|---|
| RF-01 | Registro de empleados y generación de credenciales | 94% | Aprobado |
| RF-02 | Consulta de empleados registrados | 91% | Aprobado |
| RF-03 | Edición de información de empleados | 89% | Aprobado |
| RF-04 | Eliminación de empleados y usuario asociado | 90% | Aprobado |
| RF-05 | Inicio de sesión y control de acceso | 87% | Aprobado con seguimiento |
| RF-06 | Cambio de contraseña | 93% | Aprobado |
| RF-07 | Recuperación de contraseña | 88% | Aprobado con seguimiento |
| RF-08 | Generación de reporte analítico | 96% | Aprobado |
| RF-09 | Consulta de historial de reportes | 100% | Aprobado |

## Detalle por requisito

### RF-01 - Registro de empleados y generación de credenciales

**Cobertura objetivo:** 94%  
**Estado:** Aprobado  

Este requisito contempla la creación de empleados con datos válidos, validación de cédula, correo, rol y generación automática de credenciales. La cobertura objetivo es alta porque se consideran casos de éxito, duplicidad de cédula y datos inválidos.

**Casos considerados:**

- TC01.01.01 Crear empleado con datos válidos.
- TC01.01.02 Error por cédula ya existente.
- TC01.01.03 Error por cédula, correo o rol inválido.

### RF-02 - Consulta de empleados registrados

**Cobertura objetivo:** 91%  
**Estado:** Aprobado  

Evalúa la consulta general de empleados, la visualización de datos principales y el comportamiento cuando no existen registros disponibles.

**Casos considerados:**

- TC02.01.01 Consulta exitosa de empleados registrados.
- TC02.01.02 Validación de cédula, nombre, rol y correo.
- TC02.01.03 Resultado vacío sin empleados registrados.

### RF-03 - Edición de información de empleados

**Cobertura objetivo:** 89%  
**Estado:** Aprobado  

Verifica la actualización de nombres, correo y rol, además del manejo de errores cuando el empleado no existe o cuando los datos editados no cumplen las reglas de validación.

**Casos considerados:**

- TC03.01.01 Edición exitosa de datos.
- TC03.01.02 Error por empleado no encontrado.
- TC03.01.03 Error por correo o rol inválido.

### RF-04 - Eliminación de empleados y usuario asociado

**Cobertura objetivo:** 90%  
**Estado:** Aprobado  

Comprueba que el sistema permita eliminar empleados existentes, controle el error de empleado inexistente y elimine o desactive correctamente el usuario asociado.

**Casos considerados:**

- TC04.01.01 Eliminación exitosa de empleado existente.
- TC04.01.02 Error por empleado no encontrado.
- TC04.01.03 Validación de eliminación del usuario asociado.

### RF-05 - Inicio de sesión y control de acceso

**Cobertura objetivo:** 87%  
**Estado:** Aprobado con seguimiento  

Este requisito cubre autenticación con credenciales válidas, rechazo de credenciales incorrectas, bloqueo por intentos fallidos y redirección por primer acceso. La cobertura objetivo mínima se mantiene en 87% debido a que involucra varios caminos condicionales y reglas de seguridad.

**Casos considerados:**

- TC05.01.01 Inicio de sesión exitoso.
- TC05.01.02 Error por credenciales incorrectas.
- TC05.01.03 Bloqueo tras tres intentos fallidos.
- TC05.01.04 Redirección a cambio de contraseña por primer acceso.

### RF-06 - Cambio de contraseña

**Cobertura objetivo:** 93%  
**Estado:** Aprobado  

Evalúa el cambio de contraseña con contraseña actual válida, errores por contraseña actual incorrecta, confirmación diferente y validación de política de seguridad.

**Casos considerados:**

- TC06.01.01 Cambio exitoso de contraseña.
- TC06.01.02 Error por contraseña actual incorrecta.
- TC06.01.03 Error por confirmación diferente.
- TC06.01.04 Error por incumplimiento de política.

### RF-07 - Recuperación de contraseña

**Cobertura objetivo:** 88%  
**Estado:** Aprobado con seguimiento  

Verifica la recuperación de contraseña a partir de usuario y correo registrados, controlando errores por usuario inexistente, correo no asociado y nueva contraseña inválida.

**Casos considerados:**

- TC07.01.01 Recuperación exitosa.
- TC07.01.02 Error por usuario no registrado.
- TC07.01.03 Error por correo no asociado.
- TC07.01.04 Error por nueva contraseña inválida.

### RF-08 - Generación de reporte analítico

**Cobertura objetivo:** 96%  
**Estado:** Aprobado  

Comprueba la generación de reportes con períodos válidos, validación de rangos de fechas, manejo de períodos sin datos y consolidación de totales de empleados y accesos fallidos.

**Casos considerados:**

- TC08.01.01 Generación exitosa con período válido.
- TC08.01.02 Error por fecha final menor o igual a fecha inicial.
- TC08.01.03 Error cuando el período no contiene datos.
- TC08.01.04 Validación de totales consolidados.

### RF-09 - Consulta de historial de reportes

**Cobertura objetivo:** 100%  
**Estado:** Aprobado  

Evalúa la consulta del historial de reportes, filtros por fechas, validación de rangos inválidos y registro de observaciones asociadas a reportes existentes.

**Casos considerados:**

- TC09.01.01 Listar todos los reportes existentes.
- TC09.01.02 Filtrar reportes por rango de fechas.
- TC09.01.03 Error por rango de fechas inválido.
- TC09.01.04 Agregar observación a reporte existente.

## Conclusión

La cobertura objetivo por requisito funcional se mantiene entre 87% y 100%, cumpliendo el mínimo definido para el control de calidad del prototipo. Los requisitos con mayor cobertura corresponden a reportes e historial, mientras que los módulos de autenticación y recuperación de contraseña requieren seguimiento adicional por la cantidad de reglas condicionales y validaciones de seguridad involucradas.

## Recomendación

Se recomienda mantener la ejecución de pruebas JUnit en cada incremento del sistema y comparar esta cobertura objetivo con la cobertura real generada por JaCoCo, a fin de identificar diferencias entre la planificación de calidad y los resultados efectivos de ejecución.
