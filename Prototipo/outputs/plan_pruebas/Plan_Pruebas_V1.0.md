# Plan de Pruebas Funcionales

# Sistema SNAAR - TekMess

### Danna Andrade Ariel Llumiquinga David Pilaguano

### 9 de julio de 2026

Documento Plan de pruebas funcionales del prototipo Tek-
Mess
Versión 1.
Fecha de elaboración 9 de julio de 2026
Proyecto Sistema SNAAR - TekMess
Tipo de pruebas Pruebas funcionales automatizadas con JUnit
Responsables Danna Andrade, Ariel Llumiquinga, David Pi-
laguano


## Índice


- 1. Control del Documento
   - 1.1. Historial de Versiones
   - 1.2. Propósito del Documento
- 2. Introducción
- 3. Objetivos
   - 3.1. Objetivo General
   - 3.2. Objetivos Específicos
- 4. Datos Generales del Proyecto
- 5. Involucrados
- 6. Alcance
   - 6.1. Funcionalidades Incluidas
   - 6.2. Funcionalidades No Incluidas
- 7. Estrategia de Pruebas
- 8. Ambiente de Pruebas
- 9. Criterios de Entrada y Salida
   - 9.1. Criterios de Entrada
   - 9.2. Criterios de Salida
- 10.Datos de Prueba
- 11.Matriz de Trazabilidad
- 12.Desglose Detallado de Pruebas Funcionales
   - 12.1. TC-001: Registro correcto de empleado y generación de credenciales
   - 12.2. TC-002: Rechazo de registro con cédula duplicada
   - 12.3. TC-003: Listado de personal registrado
   - 12.4. TC-004: Edición de datos de un empleado existente
   - 12.5. TC-005: Eliminación de empleado y usuario asociado
   - 12.6. TC-006: Inicio de sesión válido por rol
   - 12.7. TC-007: Bloqueo de cuenta por intentos fallidos
   - 12.8. TC-008: Cambio de contraseña válido
   - 12.9. TC-009: Recuperación de contraseña con correo y usuario registrados
   - 12.10.TC-010: Generación de reporte analítico
   - 12.11.TC-011: Consulta de historial de reportes por rango de fechas
   - 12.12.TC-012: Registro de anotación en un reporte existente
- 13.Gestión de Defectos
- 14.Riesgos y Mitigaciones
- 15.Procedimiento de Ejecución
- 16.Entregables
- 17.Observaciones Finales


## 1. Control del Documento

### 1.1. Historial de Versiones

```
Versión Fecha Descripción Responsable
1.0 09/07/2026 Elaboración inicial del plan de
pruebas funcionales, matriz de
casos y trazabilidad de requisi-
tos.
```
```
Equipo TekMess
```
### 1.2. Propósito del Documento

El propósito de este documento es establecer una guía formal para planificar, diseñar y eje-
cutar las pruebas funcionales del prototipo del sistema SNAAR - TekMess. El plan define
los objetivos, alcance, estrategia, ambiente, criterios, datos de prueba, responsabilidades
y casos que permiten verificar el cumplimiento de los requisitos funcionales identificados.
Este documento también sirve como evidencia técnica del proceso de aseguramiento de
calidad aplicado al prototipo, permitiendo relacionar cada requisito con al menos un caso
de prueba automatizado o verificable.

## 2. Introducción

El sistema SNAAR - TekMess es un prototipo web desarrollado en Java orientado a la
gestión de empleados, generación de credenciales, autenticación por rol y administración
de reportes analíticos. Debido a que el sistema maneja información de usuarios, roles y ac-
cesos, resulta necesario validar que las reglas de negocio se comporten de manera correcta
antes de avanzar hacia pruebas integrales con base de datos y servidor de aplicaciones.
Las pruebas propuestas se enfocan en las funcionalidades existentes en el prototipo y se
desarrollan con JUnit 5. Para aislar la lógica de negocio y evitar dependencia directa
de PostgreSQL durante la ejecución unitaria, se emplean objetos DAO en memoria que
simulan el comportamiento esperado de la persistencia.
La planificación considera los requisitos REQ001 a REQ009, relacionados con registro de
empleados, listado, edición, eliminación, inicio de sesión, cambio de contraseña, recupe-
ración de contraseña, generación de reportes e historial de reportes. Las pruebas fueron
diseñadas desde cero con base en los requisitos entregados, sin reutilizar pruebas previas
que pudieran existir en documentos externos.

## 3. Objetivos


### 3.1. Objetivo General

Validar de forma sistemática y automatizada que el prototipo TekMess cumpla los requisi-
tos funcionales REQ001 a REQ009, verificando los flujos principales, reglas de validación,
estados del sistema y respuestas esperadas en los módulos de empleados, autenticación,
contraseñas y reportes.

### 3.2. Objetivos Específicos

```
Verificar que el sistema permita registrar empleados válidos y genere credenciales de
acceso iniciales.
Comprobar que la validación de datos impida registros con cédulas inválidas, roles
incorrectos, correos no válidos o datos duplicados.
Confirmar que el listado de personal presente la información principal de cada empleado
registrado.
Validar la edición de datos de empleados existentes, manteniendo reglas de integridad
y consistencia.
Verificar la eliminación de empleados y la baja del usuario asociado.
Evaluar el inicio de sesión por rol, el control de intentos fallidos y el bloqueo de cuentas.
Comprobar el cambio de contraseña con política de seguridad, cifrado y actualización
de primer acceso.
Validar la recuperación de contraseña mediante usuario y correo registrados.
Verificar la generación de reportes analíticos y la consulta del historial con anotaciones.
Registrar la trazabilidad entre requisitos, casos de prueba, datos utilizados y resultados
esperados.
```
## 4. Datos Generales del Proyecto

```
Campo Descripción
Nombre del proyecto Sistema SNAAR - TekMess
Tipo de producto Prototipo web Java basado en arquitectura MVC,
servicios, DAOs y vistas JSP
Lenguaje principal Java 17
Herramienta de construc-
ción
```
```
Maven
Framework de pruebas JUnit 5
Plugin de ejecución Maven Surefire
```

```
Base de datos prevista PostgreSQL
Módulos evaluados Gestión de empleados, autenticación, cambio y re-
cuperación de contraseña, reportes analíticos
Artefactos generados Pruebas automatizadas JUnit, documento LaTeX
del plan de pruebas y tablero Kanban en Excel
Fecha del plan 9 de julio de 2026
```
## 5. Involucrados

```
Integrante Rol en el proyecto Responsabilidad en pruebas
Danna Andrade Integrante del equipo
de desarrollo
```
```
Revisión de requisitos de gestión
de empleados, validación de regis-
tro, eliminación y documentación
del tablero Kanban.
Ariel Llumiquinga Integrante del equipo
de desarrollo
```
```
Revisión de requisitos de auten-
ticación, inicio de sesión, bloqueo
por intentos y trazabilidad de ca-
sos de prueba.
David Pilaguano Integrante del equipo
de desarrollo
```
```
Revisión de requisitos de repor-
tes, historial, anotaciones y con-
sistencia técnica del documento.
```
## 6. Alcance

### 6.1. Funcionalidades Incluidas

El plan cubre las siguientes funcionalidades del prototipo:

```
Registro de empleados y generación automática de credenciales.
Listado de empleados registrados.
Edición de información de empleados.
Eliminación de empleados y usuario asociado.
Inicio de sesión por rol y bloqueo por intentos fallidos.
Cambio de contraseña con política de seguridad.
Recuperación de contraseña mediante usuario y correo.
Generación de reportes analíticos.
Consulta de historial de reportes y registro de anotaciones.
```

### 6.2. Funcionalidades No Incluidas

Quedan fuera del alcance de este plan:

```
Pruebas de rendimiento, carga o estrés.
Pruebas de seguridad avanzadas como inyección SQL, análisis de vulnerabilidades o
pruebas de penetración.
Pruebas visuales completas sobre JSP, CSS o experiencia de usuario.
Pruebas de compatibilidad entre navegadores.
Pruebas de integración real contra PostgreSQL, salvo que posteriormente se configure
un entorno de base de datos controlado.
Validación de exportación PDF con inspección visual del archivo generado.
```
## 7. Estrategia de Pruebas

La estrategia se basa en pruebas funcionales automatizadas sobre la capa de servicios y
utilidades. Esta decisión permite validar las reglas de negocio de forma rápida, repetible
y controlada, sin depender de elementos externos como servidor web, sesiones HTTP o
base de datos real.
Elemento Estrategia Aplicada
Nivel de prueba Prueba funcional unitaria sobre servicios Java.
Enfoque Validación de entradas, reglas de negocio, mensajes de
respuesta y cambios de estado.
Aislamiento Uso de DAOs en memoria para simular empleados, usua-
rios y reportes.
Automatización Casos implementados en JUnit 5 dentro de
src/test/java.
Datos de prueba Datos controlados para empleados, usuarios, roles, con-
traseñas y reportes.
Ejecución Comando esperado: mvn test.
Evidencia Resultado de ejecución JUnit, código fuente de pruebas
y trazabilidad incluida en este documento.

## 8. Ambiente de Pruebas

```
Recurso Especificación
Sistema operativo Windows o entorno compatible con Java y Maven
```

```
JDK Java 17 o superior
Gestor de dependencias Maven configurado en PATH o Maven Wrapper del
proyecto
Framework de pruebas JUnit Jupiter 5.10.
Plugin de pruebas Maven Surefire 3.2.
Base de datos No requerida para la ejecución unitaria; se simula
mediante DAOs en memoria
Comando de ejecución mvn test
```
## 9. Criterios de Entrada y Salida

### 9.1. Criterios de Entrada

```
El código fuente del prototipo debe estar disponible.
El archivo pom.xml debe incluir JUnit 5 y Maven Surefire.
Los requisitos funcionales REQ001 a REQ009 deben estar identificados.
Las clases de servicio y entidades deben compilar correctamente.
El entorno de ejecución debe contar con Java y Maven.
```
### 9.2. Criterios de Salida

```
Cada requisito funcional debe tener al menos un caso de prueba asociado.
Las pruebas automatizadas deben ejecutarse sin errores.
Los resultados esperados deben coincidir con los resultados obtenidos.
Las incidencias detectadas deben registrarse para corrección.
El plan de pruebas y el tablero Kanban deben quedar actualizados como evidencia del
trabajo realizado.
```
## 10.Datos de Prueba

```
Dato Valor de Referencia Uso Principal
Cédula válida 1712345678 Registro, edición, eliminación y
recuperación de usuario.
Nombre de emplea-
do
```
```
Danna Andrade Generación de usuario base
dandrade.
```

```
Correo institucio-
nal
```
```
danna@tekmess.com Validación de correo y recupera-
ción de contraseña.
Rol válido SUPERVISOR,
GUARDIA, JE-
FE_LOGISTICA
```
```
Validación de permisos y creación
de sesión por rol.
Contraseña tempo-
ral
```
```
Temp123! Inicio de sesión y cambio de con-
traseña.
Contraseña nueva Nueva123! / Recupe-
ra123!
```
```
Cambio y recuperación de contra-
seña cumpliendo política.
Rango de fechas vá-
lido
```
```
Fecha inicio menor que
fecha fin
```
```
Generación y consulta de repor-
tes.
```
## 11.Matriz de Trazabilidad

```
Requisito Funcionalidad Evaluada Clase Principal Casos
REQ001 Registro de empleados y ge-
neración de credenciales
```
```
EmpleadoServicio,
GeneradorCredenciales,
ValidadorDatos
```
#### TC-001, TC-

```
REQ002 Listado de personal regis-
trado
```
```
EmpleadoServicio TC-
REQ003 Edición de datos del em-
pleado
```
```
EmpleadoServicio TC-
REQ004 Eliminación de empleados EmpleadoServicio TC-
REQ005 Inicio de sesión por rol AuthServicio,
Sesion
```
#### TC-006, TC-

```
REQ006 Cambio de contraseña AuthServicio,
CifradorContrasena
```
#### TC-

```
REQ007 Recuperación de contraseña AuthServicio TC-
REQ008 Generación de reporte ana-
lítico
```
```
ReporteServicio TC-
REQ009 Consulta de historial de re-
portes
```
```
ReporteServicio TC-011, TC-
```
## 12.Desglose Detallado de Pruebas Funcionales

En esta sección se describe cada prueba de forma individual. Para cada caso se especifica el
objetivo, requisito funcional relacionado, precondiciones, datos utilizados, procedimiento,
elementos evaluados, resultado esperado y criterio de aprobación. Esta estructura permite
comprender exactamente qué se valida en cada funcionalidad sin reducir el análisis a una
matriz resumida.


### 12.1. TC-001: Registro correcto de empleado y generación de credenciales

### denciales

Requisito asociado: REQ001 - Registro de empleados y generación de credenciales.
Objetivo de la prueba: comprobar que el sistema permita registrar un empleado con
datos válidos y que, como consecuencia del registro, genere automáticamente una cuenta
de usuario con credenciales temporales.
Precondiciones:

```
No debe existir un empleado registrado con la cédula utilizada en la prueba.
El DAO de empleados debe estar disponible en memoria.
El DAO de usuarios debe estar disponible en memoria.
```
Datos de prueba:

```
Cédula: 1712345678.
Nombres: Danna Andrade.
Correo: DANNA.ANDRADE@TEKMESS.COM.
Rol: JEFE_LOGISTICA.
```
Procedimiento:

1. Crear una instancia de EmpleadoServicio con DAOs en memoria.
2. Construir un objeto Empleado con datos válidos.
3. Ejecutar el método crearEmpleado.
4. Consultar el empleado almacenado por cédula.
5. Consultar el usuario generado para la cédula registrada.

Aspectos evaluados:

```
Validación de cédula con exactamente diez dígitos numéricos.
Validación de nombres completos.
Validación de rol permitido por el sistema.
Normalización del correo institucional a minúsculas.
Generación de nombre de usuario a partir de nombres y apellidos.
Generación de contraseña temporal que cumpla política de seguridad.
Cifrado de la contraseña mediante BCrypt.
```

Marcación del usuario con primer acceso obligatorio.
Resultado esperado: el servicio debe retornar un mensaje de registro exitoso, el em-
pleado debe existir en el DAO, el usuario debe estar asociado a la cédula del empleado,
la contraseña temporal debe cumplir la política de seguridad y el campo de primer acceso
debe quedar activo.
Criterio de aprobación: la prueba se aprueba si todas las validaciones anteriores se
cumplen y la contraseña temporal coincide con el hash almacenado al verificarla con el
cifrador.

### 12.2. TC-002: Rechazo de registro con cédula duplicada

Requisito asociado: REQ001 - Registro de empleados y generación de credenciales.
Objetivo de la prueba: verificar que el sistema impida registrar un nuevo empleado
cuando la cédula ya existe en el repositorio de empleados.
Precondiciones:
Debe existir previamente un empleado registrado con la misma cédula.
El servicio debe consultar la existencia de la cédula antes de crear el nuevo registro.
Datos de prueba:
Cédula duplicada: 1712345678.
Primer empleado: Danna Andrade.
Segundo empleado: Ariel Llumiquinga.
Procedimiento:

1. Registrar un empleado inicial en el DAO en memoria.
2. Intentar registrar un segundo empleado usando la misma cédula.
3. Capturar el mensaje retornado por crearEmpleado.
Aspectos evaluados:
    Verificación de unicidad de la cédula.
    Prevención de duplicidad en el registro de personal.
    Control de flujo para evitar creación de usuario cuando el empleado no debe registrarse.
    Mensaje funcional comprensible para el usuario.
Resultado esperado: el sistema debe rechazar el registro y retornar un mensaje indi-
cando que la cédula ya se encuentra registrada.
Criterio de aprobación: la prueba se aprueba si no se crea un nuevo empleado, no
se generan credenciales adicionales y el mensaje de error corresponde al escenario de
duplicidad.


### 12.3. TC-003: Listado de personal registrado

Requisito asociado: REQ002 - Listado de personal registrado.
Objetivo de la prueba: confirmar que el servicio permita consultar el conjunto de
empleados registrados y que la información principal de cada empleado esté disponible
para la vista de listado.
Precondiciones:

```
Deben existir empleados cargados en el DAO en memoria.
Cada empleado debe tener cédula, nombres, correo y rol.
```
Datos de prueba:

```
Empleado 1: Danna Andrade, rol JEFE_LOGISTICA.
Empleado 2: Ariel Llumiquinga, rol SUPERVISOR.
```
Procedimiento:

1. Registrar dos empleados en el DAO en memoria.
2. Ejecutar el método consultarEmpleados.
3. Verificar la cantidad de elementos retornados.
4. Validar que uno de los empleados contenga cédula, rol y correo esperados.

Aspectos evaluados:

```
Recuperación del listado completo de empleados.
Conservación de datos principales del empleado.
Disponibilidad de información necesaria para edición o eliminación posterior.
```
Resultado esperado: el método debe retornar una lista con los empleados registrados
y sus datos principales.
Criterio de aprobación: la prueba se aprueba si la lista contiene la cantidad esperada
de empleados y los datos consultados coinciden con los registros cargados.

### 12.4. TC-004: Edición de datos de un empleado existente

Requisito asociado: REQ003 - Edición de datos del empleado.
Objetivo de la prueba: validar que el sistema permita modificar datos editables de un
empleado existente, conservando la cédula como identificador principal.
Precondiciones:


```
Debe existir un empleado registrado con la cédula indicada.
Los nuevos datos deben cumplir las reglas de validación.
```
Datos de prueba:

```
Cédula: 1712345678.
Correo inicial: danna@tekmess.com.
Correo actualizado: danna.andrade@tekmess.com.
Rol actualizado: SUPERVISOR.
```
Procedimiento:

1. Crear un empleado inicial en el DAO.
2. Construir un objeto Empleado con la misma cédula y datos modificados.
3. Ejecutar el método editarEmpleado.
4. Consultar el empleado por cédula.
5. Comparar los datos actualizados.

Aspectos evaluados:

```
Búsqueda de empleado existente por cédula.
Validación de correo institucional.
Validación de rol permitido.
Persistencia de cambios en el repositorio.
Mensaje de confirmación de actualización.
```
Resultado esperado: el empleado debe quedar actualizado con el nuevo correo y rol, y
el servicio debe informar que los datos fueron actualizados exitosamente.
Criterio de aprobación: la prueba se aprueba si el registro consultado después de
la edición refleja los cambios enviados y el contador de ediciones del DAO en memoria
aumenta.


### 12.5. TC-005: Eliminación de empleado y usuario asociado

Requisito asociado: REQ004 - Eliminación de empleados.
Objetivo de la prueba: verificar que al eliminar un empleado también se elimine o
invalide la cuenta de usuario asociada a su cédula.
Precondiciones:

```
Debe existir un empleado registrado.
Debe existir un usuario asociado a la misma cédula.
```
Datos de prueba:

```
Cédula: 1712345678.
Usuario asociado: dandrade.
Contraseña temporal inicial: Temp123!.
```
Procedimiento:

1. Registrar un empleado en el DAO de empleados.
2. Registrar un usuario asociado en el DAO de usuarios.
3. Ejecutar el método eliminarEmpleado.
4. Consultar nuevamente el empleado por cédula.
5. Consultar nuevamente el usuario por cédula.

Aspectos evaluados:

```
Localización del empleado antes de eliminar.
Eliminación del usuario asociado.
Eliminación del registro de empleado.
Mensaje de eliminación exitosa.
Prevención de accesos posteriores con usuario de empleado eliminado.
```
Resultado esperado: el empleado y el usuario asociado no deben existir después de
ejecutar la eliminación.
Criterio de aprobación: la prueba se aprueba si ambas consultas posteriores retornan
null y el servicio informa eliminación exitosa.


### 12.6. TC-006: Inicio de sesión válido por rol

Requisito asociado: REQ005 - Inicio de sesión por rol.
Objetivo de la prueba: comprobar que un usuario activo con credenciales correctas
pueda iniciar sesión y que el sistema construya una sesión con el rol correspondiente.
Precondiciones:

```
Debe existir un empleado con rol definido.
Debe existir un usuario activo vinculado a la cédula del empleado.
La contraseña ingresada debe coincidir con el hash almacenado.
```
Datos de prueba:

```
Usuario: dandrade.
Contraseña: Temp123!.
Rol esperado: SUPERVISOR.
```
Procedimiento:

1. Crear un empleado con rol SUPERVISOR.
2. Crear un usuario activo asociado al empleado.
3. Ejecutar iniciarSesion con usuario y contraseña correctos.
4. Verificar que el objeto retornado sea una instancia de Sesion.
5. Verificar el rol asignado a la sesión.

Aspectos evaluados:

```
Validación de formato del nombre de usuario.
Verificación de contraseña cifrada.
Revisión de estado de cuenta activo.
Reinicio de intentos fallidos tras autenticación correcta.
Creación de sesión con rol funcional.
Redirección a cambio de contraseña cuando el usuario está en primer acceso.
```
Resultado esperado: el sistema debe retornar una sesión válida con rol SUPERVISOR y
el mensaje PRIMER_ACCESO cuando corresponda.
Criterio de aprobación: la prueba se aprueba si la sesión no es nula, el rol coincide con
el empleado asociado y la respuesta funcional corresponde al estado de primer acceso.


### 12.7. TC-007: Bloqueo de cuenta por intentos fallidos

Requisito asociado: REQ005 - Inicio de sesión por rol.
Objetivo de la prueba: verificar que el sistema controle los intentos fallidos de auten-
ticación y bloquee la cuenta al alcanzar el límite permitido.
Precondiciones:

```
Debe existir un usuario activo.
La cuenta no debe estar bloqueada al inicio de la prueba.
```
Datos de prueba:

```
Usuario: dandrade.
Contraseña incorrecta: Mala123!.
Límite de intentos: 3.
```
Procedimiento:

1. Ejecutar iniciarSesion con contraseña incorrecta.
2. Repetir la operación hasta completar tres intentos fallidos.
3. Consultar el estado de la cuenta.
4. Revisar el mensaje funcional retornado por el tercer intento.

Aspectos evaluados:

```
Incremento del contador de intentos fallidos.
Persistencia del número de intentos.
Cambio de estado de cuenta a BLOQUEADO.
Respuesta funcional ante cuenta bloqueada.
Prevención de acceso con credenciales incorrectas.
```
Resultado esperado: luego del tercer intento fallido, el usuario debe quedar con estado
BLOQUEADO y el sistema debe retornar un mensaje de bloqueo.
Criterio de aprobación: la prueba se aprueba si no se crea sesión, el estado de la cuenta
cambia a bloqueado y el mensaje informa que se debe contactar al administrador.


### 12.8. TC-008: Cambio de contraseña válido

Requisito asociado: REQ006 - Cambio de contraseña.
Objetivo de la prueba: comprobar que un usuario pueda cambiar su contraseña cuando
ingresa correctamente la clave actual y la nueva contraseña cumple la política de seguridad.
Precondiciones:

```
Debe existir un usuario registrado.
La contraseña actual debe estar cifrada.
El usuario debe estar marcado inicialmente con primer acceso obligatorio.
```
Datos de prueba:

```
Usuario: dandrade.
Contraseña actual: Temp123!.
Nueva contraseña: Nueva123!.
Confirmación: Nueva123!.
```
Procedimiento:

1. Crear un usuario con contraseña temporal cifrada.
2. Ejecutar cambiarContrasena.
3. Consultar el usuario actualizado.
4. Verificar que el nuevo hash corresponda a la nueva contraseña.
5. Verificar que el indicador de primer acceso quede desactivado.

Aspectos evaluados:

```
Obligatoriedad de contraseña actual.
Coincidencia entre nueva contraseña y confirmación.
Cumplimiento de política: longitud mínima, mayúscula, minúscula, dos números y ca-
rácter especial.
Rechazo de contraseña igual a la actual.
Actualización segura mediante hash.
Desactivación del primer acceso obligatorio.
```
Resultado esperado: el sistema debe actualizar la contraseña, almacenar un nuevo hash
y retornar un mensaje de actualización exitosa.
Criterio de aprobación: la prueba se aprueba si el nuevo hash valida la contraseña
nueva y el usuario deja de estar marcado como primer acceso.


### 12.9. TC-009: Recuperación de contraseña con correo y usuario registrados

Requisito asociado: REQ007 - Recuperación de contraseña.
Objetivo de la prueba: validar que un usuario pueda restablecer su contraseña cuando
proporciona un nombre de usuario y correo electrónico registrados en el sistema.
Precondiciones:

```
Debe existir un empleado con correo registrado.
Debe existir un usuario asociado a la cédula del empleado.
La cuenta no debe estar bloqueada.
```
Datos de prueba:

```
Correo: danna@tekmess.com.
Usuario: dandrade.
Nueva contraseña: Recupera123!.
Confirmación: Recupera123!.
```
Procedimiento:

1. Registrar empleado y usuario asociado.
2. Ejecutar recuperarContrasena.
3. Consultar el usuario por nombre de usuario.
4. Verificar que el hash corresponda a la nueva contraseña.

Aspectos evaluados:

```
Validación de formato de usuario.
Validación de formato de correo.
Verificación de relación entre usuario, cédula y correo del empleado.
Bloqueo del restablecimiento cuando la cuenta está bloqueada.
Aplicación de la política de seguridad a la nueva contraseña.
Almacenamiento cifrado de la contraseña restablecida.
```
Resultado esperado: el sistema debe restablecer la contraseña y confirmar la operación
mediante un mensaje exitoso.
Criterio de aprobación: la prueba se aprueba si el hash almacenado valida la nueva
contraseña y el mensaje indica restablecimiento exitoso.


### 12.10.TC-010: Generación de reporte analítico

Requisito asociado: REQ008 - Generación de reporte analítico.
Objetivo de la prueba: verificar que el servicio consolide correctamente los datos de
empleados gestionados y accesos fallidos en un reporte analítico.
Precondiciones:

```
Debe existir un rango de fechas válido.
Los DAOs deben proporcionar totales de empleados creados, editados, eliminados y
accesos fallidos.
Debe existir un DAO de reportes capaz de guardar el reporte generado.
```
Datos de prueba:

```
Empleados creados: 2.
Empleados editados: 1.
Empleados eliminados: 1.
Accesos fallidos: 3.
Generado por: Danna Andrade.
```
Procedimiento:

1. Configurar los totales en los DAOs en memoria.
2. Ejecutar generarReporte con fecha inicial menor que fecha final.
3. Obtener el objeto Reporte retornado.
4. Validar cada total consolidado.
5. Confirmar el mensaje de generación exitosa.

Aspectos evaluados:

```
Validación de rango de fechas.
Consulta de empleados creados en el período.
Consulta de empleados editados en el período.
Consulta de empleados eliminados en el período.
Consulta de accesos fallidos.
Construcción del objeto Reporte.
```

Persistencia del reporte generado.
Resultado esperado: el servicio debe retornar un reporte con los totales esperados y el
mensaje Reporte generado exitosamente.
Criterio de aprobación: la prueba se aprueba si todos los totales del reporte coinciden
con los datos configurados y el reporte queda guardado.

### 12.11.TC-011: Consulta de historial de reportes por rango de fechas

Requisito asociado: REQ009 - Consulta de historial de reportes.
Objetivo de la prueba: comprobar que el sistema permita consultar reportes históricos
aplicando un filtro de fechas válido.
Precondiciones:
Debe existir al menos un reporte guardado.
El rango de consulta debe incluir el período del reporte.
Datos de prueba:
Fecha inicial de reporte: 1000 ms.
Fecha final de reporte: 2000 ms.
Rango de búsqueda: 500 ms a 3000 ms.
Generado por: David Pilaguano.
Procedimiento:

1. Crear y guardar un reporte en el DAO en memoria.
2. Ejecutar consultarHistorial con un rango válido.
3. Verificar que el resultado no sea nulo.
4. Validar que la lista contenga el reporte esperado.
Aspectos evaluados:
    Validación de rango de fechas para consulta.
    Recuperación de reportes históricos.
    Aplicación del filtro por período.
    Mensaje funcional OK cuando existen resultados.
Resultado esperado: el sistema debe retornar una lista con el reporte ubicado dentro
del rango de fechas y el estado de respuesta debe ser OK.
Criterio de aprobación: la prueba se aprueba si el historial contiene exactamente el
reporte esperado y la respuesta funcional indica éxito.


### 12.12.TC-012: Registro de anotación en un reporte existente

Requisito asociado: REQ009 - Consulta de historial de reportes.
Objetivo de la prueba: validar que se puedan agregar anotaciones a un reporte previa-
mente generado sin modificar los datos base del reporte.
Precondiciones:

```
Debe existir un reporte guardado.
El identificador del reporte debe ser válido.
```
Datos de prueba:

```
Autor: Ariel Llumiquinga.
Contenido de la anotación: Revision aprobada.
```
Procedimiento:

1. Guardar un reporte en el DAO en memoria.
2. Ejecutar agregarAnotacion indicando el identificador del reporte.
3. Consultar las anotaciones asociadas al reporte.
4. Verificar que la anotación haya sido almacenada.

Aspectos evaluados:

```
Búsqueda del reporte por identificador.
Creación de objeto Anotacion.
Asociación de la anotación al reporte correcto.
Conservación de los datos base del reporte.
Respuesta booleana de éxito.
```
Resultado esperado: la anotación debe quedar registrada y asociada al reporte existen-
te.
Criterio de aprobación: la prueba se aprueba si el método retorna true y la consulta
de anotaciones devuelve el registro agregado.


## 13.Gestión de Defectos

Cuando una prueba falle, se deberá registrar una incidencia con la siguiente información
mínima:

```
Identificador del defecto.
Caso de prueba asociado.
Requisito funcional afectado.
Descripción clara del comportamiento observado.
Resultado esperado.
Evidencia de ejecución.
Severidad: crítica, alta, media o baja.
Estado: abierto, en corrección, corregido, verificado o cerrado.
```
## 14.Riesgos y Mitigaciones

```
Riesgo Impacto Mitigación
Maven no disponible en
el entorno
```
```
No se pueden ejecutar las
pruebas con mvn test.
```
```
Instalar Maven, configu-
rar PATH o agregar Ma-
ven Wrapper al proyecto.
Dependencia de base de
datos real en algunas cla-
ses
```
```
Puede dificultar pruebas
unitarias puras.
```
```
Inyectar interfaces DAO
y usar dobles de prueba
en memoria.
Reglas funcionales in-
completas en el prototipo
```
```
Algunos escenarios podrían
requerir validación manual
o integración futura.
```
```
Documentar limitaciones
y ampliar pruebas con-
forme evolucione el códi-
go.
Codificación incorrecta
de caracteres
```
```
Puede afectar documenta-
ción y mensajes en español.
```
```
Guardar archivos en
UTF-8 y revisar salida
del compilador LaTeX.
```
## 15.Procedimiento de Ejecución

1. Verificar que Java esté instalado mediante java -version.
2. Verificar que Maven esté instalado mediante mvn -version.
3. Ubicarse en la raíz del prototipo, donde se encuentra el archivo pom.xml.


4. Ejecutar el comando mvn test.
5. Revisar el resumen de ejecución generado por Maven Surefire.
6. En caso de fallos, identificar el caso afectado y registrar la incidencia correspondiente.

## 16.Entregables

```
Entregable Descripción
Archivo de pruebas JUnit Clase RequisitosFuncionalesTest.java con ca-
sos automatizados para REQ001 a REQ009.
Plan de pruebas en LaTeX Documento Plan_Pruebas_TekMess.tex con pla-
nificación, estrategia y matriz de casos.
Tablero Kanban en Excel Archivo Tablero_Kanban_Pruebas_TekMess.xlsx
con tareas ejecutadas y responsables.
```
## 17.Observaciones Finales

El plan de pruebas fortalece la verificación funcional del prototipo al cubrir cada requisito
mediante casos trazables y automatizados. La estrategia aplicada permite validar reglas
de negocio sin depender de una base de datos real, lo cual mejora la rapidez de ejecución
y facilita la detección temprana de errores.
Durante la preparación del plan se identificó que el entorno local utilizado no contaba
con el comando mvn disponible en el PATH ni con compilador LaTeX instalado. Por este
motivo, la ejecución formal de mvn test y la generación del PDF deberán realizarse en un
entorno que cuente con dichas herramientas configuradas. Aun así, el documento fuente
LaTeX, las pruebas JUnit y el tablero Kanban quedan listos para su revisión y ejecución.


