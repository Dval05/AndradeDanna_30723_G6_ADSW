# 📚 GUÍA DE USO - PATRONES DE DISEÑO EN ESTUDIANTECRUD

## 🎓 INTRODUCCIÓN

Este documento proporciona ejemplos prácticos de cómo usar cada patrón de diseño implementado en el proyecto EstudianteCRUD refactorizado.

---

## 1️⃣ OBSERVER PATTERN - Notificaciones Automáticas

### ¿Qué es?
El patrón Observer permite que múltiples objetos (observadores) reciban notificaciones automáticas cuando algo importante sucede en otro objeto (observable).

### Componentes
- **Subject/Observable**: `StudentRepository` (notifica cambios)
- **Observers**: `ConsoleStudentObserver`, `ViewStudentObserver` (escuchan cambios)
- **Events**: Agregar, actualizar, eliminar estudiante

### Ejemplo 1: Registrar un observador
```java
// Crear servicio y repositorio
StudentService service = new StudentService();

// Crear observador para consola
StudentObserver consoleObserver = new ConsoleStudentObserver();

// Registrar observador
service.registerObserver(consoleObserver);

// Cuando se agrega un estudiante:
service.addStudent("001", "Juan", 20, "Ingeniería");

// OUTPUT en consola:
// ✓ [OBSERVER] Estudiante agregado: Estudiante{id='001', nombre='Juan', edad=20, carrera='Ingeniería'}
```

### Ejemplo 2: Múltiples observadores
```java
StudentService service = new StudentService();

// Registrar múltiples observadores
service.registerObserver(new ConsoleStudentObserver());
service.registerObserver(new ViewStudentObserver() {
    @Override
    public void onStudentAdded(Estudiante est) {
        System.out.println("🔔 VISTA: Agregar " + est.getNombre() + " a tabla");
    }
    
    @Override
    public void onStudentUpdated(Estudiante est) {
        System.out.println("🔄 VISTA: Actualizar fila de " + est.getNombre());
    }
    
    @Override
    public void onStudentDeleted(String id) {
        System.out.println("❌ VISTA: Eliminar fila de " + id);
    }
    
    @Override
    public void refreshTable() {
        System.out.println("🔃 VISTA: Refrescar tabla");
    }
});

// Todas las operaciones notificarán a ambos observadores
service.addStudent("002", "María", 19, "Sistemas");
```

### Ejemplo 3: Desregistrar observador
```java
StudentObserver observer = new ConsoleStudentObserver();
service.registerObserver(observer);

// Hacer operaciones (notifica)
service.addStudent("003", "Carlos", 21, "Redes");

// Desregistrar
service.unregisterObserver(observer);

// Hacer más operaciones (NO notifica)
service.addStudent("004", "Ana", 20, "Telecomunicaciones");
```

### Flujo del Observer Pattern en la aplicación
```
┌─────────────────────────────────────────────────────┐
│ StudentService.addStudent()                         │
└────────────────┬────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────┐
│ CommandInvoker.executeCommand(AddStudentCommand)    │
└────────────────┬────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────┐
│ StudentRepository.save(estudiante)                  │
└────────────────┬────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────┐
│ repository.notifyStudentAdded(estudiante)           │
└────────────────┬────────────────────────────────────┘
                 │
        ┌────────┴────────┐
        │                 │
        ▼                 ▼
ConsoleObserver.    ViewObserver.
onStudentAdded()    onStudentAdded()
```

---

## 2️⃣ STRATEGY PATTERN - Búsquedas Dinámicas

### ¿Qué es?
El patrón Strategy encapsula diferentes algoritmos (en este caso, búsquedas) permitiendo cambiar entre ellos dinámicamente sin modificar el código cliente.

### Componentes
- **Strategy Interface**: `SearchStrategy`
- **Concrete Strategies**: 
  - `SearchByIdStrategy` (búsqueda exacta)
  - `SearchByNameStrategy` (búsqueda parcial)
  - `SearchByCareerStrategy` (búsqueda parcial)
- **Context**: `SearchContext` (ejecuta la estrategia)

### Ejemplo 1: Búsqueda por ID
```java
StudentService service = new StudentService();

// Agregar algunos estudiantes
service.addStudent("001", "Juan García", 20, "Ingeniería");
service.addStudent("002", "Juan López", 19, "Sistemas");
service.addStudent("003", "María Pérez", 21, "Redes");

// Búsqueda por ID (exacta)
List<Estudiante> resultado = service.searchById("001");

// resultado.size() = 1
// resultado[0].getNombre() = "Juan García"
```

### Ejemplo 2: Búsqueda por nombre
```java
// Búsqueda por nombre (parcial, case-insensitive)
List<Estudiante> resultado = service.searchByName("Juan");

// resultado.size() = 2
// resultado[0] = "Juan García"
// resultado[1] = "Juan López"

// También busca parcialmente
resultado = service.searchByName("PE");
// resultado.size() = 1
// resultado[0] = "María Pérez"
```

### Ejemplo 3: Búsqueda por carrera
```java
// Búsqueda por carrera (parcial)
List<Estudiante> resultado = service.searchByCareer("Ing");

// resultado.size() = 1
// resultado[0] = "Juan García" (Ingeniería)

// También parcial
resultado = service.searchByCareer("emas");
// resultado.size() = 1
// resultado[0] = "Juan López" (Sistemas)
```

### Ejemplo 4: Cambio dinámico de estrategia
```java
// Acceder al contexto de búsqueda
SearchContext context = new SearchContext(new SearchByIdStrategy());

// Buscar por ID
List<Estudiante> resultados = context.executeSearch("001", todoEstudiantes);

// CAMBIAR ESTRATEGIA SIN CREAR NUEVO CONTEXTO
context.setStrategy(new SearchByNameStrategy());
resultados = context.executeSearch("Juan", todoEstudiantes);

// CAMBIAR NUEVAMENTE
context.setStrategy(new SearchByCareerStrategy());
resultados = context.executeSearch("Ingeniería", todoEstudiantes);
```

### Ejemplo 5: Crear estrategia personalizada
```java
// Crear nueva estrategia para buscar por edad
public class SearchByAgeStrategy implements SearchStrategy {
    @Override
    public List<Estudiante> search(String criterio, List<Estudiante> estudiantes) {
        int edad = Integer.parseInt(criterio);
        return estudiantes.stream()
                .filter(e -> e.getEdad() == edad)
                .collect(Collectors.toList());
    }
}

// Usar nueva estrategia sin modificar código existente
SearchContext context = new SearchContext(new SearchByAgeStrategy());
List<Estudiante> de20Años = context.executeSearch("20", todoEstudiantes);
```

### Flujo del Strategy Pattern
```
┌──────────────────────────────────────────────┐
│ Usuario selecciona tipo de búsqueda en View  │
└────────────────┬─────────────────────────────┘
                 │
        ┌────────┴────────┐
        │                 │
        ▼                 ▼
SearchByIdStrategy   SearchByNameStrategy
        │                 │
        └────────┬────────┘
                 │
                 ▼
    ┌─────────────────────────────┐
    │ SearchContext.setStrategy() │
    └────────────┬────────────────┘
                 │
                 ▼
    ┌─────────────────────────────┐
    │ strategy.search(criterio)   │
    └─────────────────────────────┘
```

---

## 3️⃣ COMMAND PATTERN - Operaciones Encapsuladas

### ¿Qué es?
El patrón Command encapsula una solicitud como un objeto, permitiendo parametrizar clientes con diferentes solicitudes, encolar solicitudes e implementar undo/redo.

### Componentes
- **Command Interface**: `Command`
- **Concrete Commands**: 
  - `AddStudentCommand`
  - `UpdateStudentCommand`
  - `DeleteStudentCommand`
- **Invoker**: `CommandInvoker` (ejecuta y gestiona historial)

### Ejemplo 1: Ejecutar comando simple
```java
CommandInvoker invoker = new CommandInvoker();
StudentRepository repo = new StudentRepository();

// Crear comando
Estudiante nuevo = new Estudiante("001", "Juan", 20, "Ingeniería");
Command cmd = new AddStudentCommand(repo, nuevo);

// Ejecutar
String resultado = invoker.executeCommand(cmd);
// resultado = "Éxito: Estudiante Juan agregado."

// Verificar historial
System.out.println("Historial: " + invoker.getHistorySize()); // 1
```

### Ejemplo 2: Deshacer operación
```java
// Después de agregar
invoker.executeCommand(new AddStudentCommand(repo, estudiante1));
invoker.executeCommand(new AddStudentCommand(repo, estudiante2));

System.out.println("Historial: " + invoker.getHistorySize()); // 2
System.out.println("Estudiantes: " + repo.findAll().size()); // 2

// Deshacer última operación
String resultado = invoker.undo();
// resultado = "Éxito: Adición desecha - Estudiante eliminado."

System.out.println("Historial: " + invoker.getHistorySize()); // 1
System.out.println("Estudiantes: " + repo.findAll().size()); // 1
```

### Ejemplo 3: Rehacer operación
```java
// Deshacer
invoker.undo();

// Rehacer
String resultado = invoker.redo();
// resultado = "Éxito: Estudiante restaurado."

System.out.println("Estudiantes: " + repo.findAll().size()); // 2
```

### Ejemplo 4: Múltiples comandos con historial
```java
CommandInvoker invoker = new CommandInvoker();

// Operación 1: Agregar
invoker.executeCommand(new AddStudentCommand(repo, est1));

// Operación 2: Agregar
invoker.executeCommand(new AddStudentCommand(repo, est2));

// Operación 3: Actualizar
invoker.executeCommand(new UpdateStudentCommand(repo, est1Modificado));

// Operación 4: Eliminar
invoker.executeCommand(new DeleteStudentCommand(repo, "002"));

System.out.println("Historial: " + invoker.getHistorySize()); // 4

// Deshacer último (delete)
invoker.undo();
// est2 restaurado

// Deshacer penúltimo (update)
invoker.undo();
// est1 con datos anteriores

// Rehacer
invoker.redo();
// est1 actualizado nuevamente
```

### Ejemplo 5: Comando con validación
```java
// Los comandos validan antes de ejecutar
Estudiante sinId = new Estudiante("", "Juan", 20, "Ingeniería");
Command cmd = new AddStudentCommand(repo, sinId);

String resultado = invoker.executeCommand(cmd);
// resultado = "Error: El ID es inválido."

// El comando NO se agrega al historial (validación falla)
System.out.println("Historial: " + invoker.getHistorySize()); // 0
```

### Flujo del Command Pattern
```
┌─────────────────────────────────────┐
│ Usuario hace clic en botón CRUD     │
└────────────────┬────────────────────┘
                 │
    ┌────────────┴────────────┐
    │                         │
    ▼                         ▼
Agregar              UpdateCommand
    │
    ▼
┌──────────────────────────┐
│ new AddStudentCommand()  │
└────────────┬─────────────┘
             │
             ▼
┌──────────────────────────┐
│ CommandInvoker.execute() │
└────────────┬─────────────┘
             │
    ┌────────┴────────┐
    │                 │
    ▼                 ▼
Ejecutar          Guardar en
Comando           Historial
    │                 │
    └────────┬────────┘
             │
             ▼
    ┌──────────────────┐
    │ Repositorio      │
    │ actualizado      │
    └──────────────────┘
```

---

## 🎯 INTEGRACIÓN DE LOS TRES PATRONES

### Ejemplo completo: Operación desde la Vista
```java
// En FormularioCrudEstudiante.clickAgregar()

// 1. Usuario ingresa datos y hace clic en "Agregar"
String id = "001";
String nombre = "Juan";
int edad = 20;
String carrera = "Ingeniería";

// 2. Vista delega a Controller
String resultado = controlador.agregarEstudiante(id, nombre, edad, carrera);

// 3. Controller delega a Service
// ControlEstudiante.agregarEstudiante() → StudentService.addStudent()

// 4. Service crea y ejecuta comando (COMMAND PATTERN)
Command cmd = new AddStudentCommand(repository, estudiante);
String resultado = commandInvoker.executeCommand(cmd);

// 5. Comando ejecuta en Repository (OBSERVER PATTERN)
repository.save(estudiante);

// 6. Repository notifica observadores
// - ConsoleStudentObserver: imprime en consola
// - ViewStudentObserver: actualiza tabla

// 7. Usuario ve resultado en pantalla
// - Tabla se actualiza automáticamente
// - Mensaje de éxito muestra en diálogo
// - Historial se incrementa

// 8. Usuario puede deshacer (Undo)
// - Botón "Deshacer" llama → commandInvoker.undo()
// - Estudiante se elimina
// - Tabla se actualiza automáticamente
```

### Ejemplo de búsqueda: Usando STRATEGY PATTERN
```java
// En FormularioCrudEstudiante.clickBuscar()

// 1. Usuario selecciona tipo de búsqueda en combo
String tipoBusqueda = comboBusqueda.getSelectedItem(); // "Por Nombre"

// 2. Usuario ingresa criterio
String criterio = txtBusqueda.getText(); // "Juan"

// 3. Vista delega a Controller
List<Estudiante> resultado = controlador.buscarPorNombre(criterio);

// 4. Controller delega a Service
// ControlEstudiante.buscarPorNombre() → StudentService.searchByName()

// 5. Service cambia estrategia (STRATEGY PATTERN)
searchContext.setStrategy(new SearchByNameStrategy());
resultado = searchContext.executeSearch(criterio, todosEstudiantes);

// 6. Estrategia ejecuta búsqueda
// SearchByNameStrategy busca con contains (case-insensitive)

// 7. Vista recibe resultados y los muestra en tabla
mostrarTabla(resultado);
```

---

## 📊 DIAGRAMA DE FLUJO COMPLETO

```
USUARIO → VISTA (FormularioCrudEstudiante)
   │
   ├─ Agregar → CONTROLLER → SERVICE
   │               │            │
   │               └─ COMMAND ─┤
   │                    │      │
   │                    └─ REPOSITORY (Observable)
   │                         │
   │              ┌──────────┬┴──────────┐
   │              │          │          │
   │          Notifica   Notifica   Notifica
   │           Agregar   Actualizar  Eliminar
   │              │          │          │
   │         CONSOLE      VIEW       (otra)
   │         OBSERVER    OBSERVER    OBSERVER
   │
   ├─ Buscar → CONTROLLER → SERVICE → STRATEGY
   │               │            │         │
   │               │            └─────────┤
   │               │                 Ejecuta
   │           Retorna resultados
   │
   ├─ Deshacer → CONTROLLER → SERVICE → COMMAND INVOKER
   │               │            │           │
   │               │            └───────────┤
   │               │                  Undo
   │           Retorna resultado
   │
   └─ Rehacer → CONTROLLER → SERVICE → COMMAND INVOKER
                   │            │           │
                   │            └───────────┤
                   │                   Redo
               Retorna resultado
```

---

## 🧬 VENTAJAS DE ESTA ARQUITECTURA

### 1. Separación de Responsabilidades
- ✅ Cada patrón maneja un aspecto diferente
- ✅ Cada clase tiene una razón única para cambiar

### 2. Extensibilidad
```java
// Agregar nueva estrategia de búsqueda SIN modificar código existente
public class SearchByGpaStrategy implements SearchStrategy { ... }
context.setStrategy(new SearchByGpaStrategy());

// Agregar nuevo observador SIN modificar código existente
public class DatabaseObserver implements StudentObserver { ... }
service.registerObserver(new DatabaseObserver());

// Agregar nuevo comando SIN modificar código existente
public class ExportStudentsCommand implements Command { ... }
invoker.executeCommand(new ExportStudentsCommand());
```

### 3. Testabilidad
```java
// Test del Strategy Pattern
@Test
public void testSearchByNameStrategy() {
    SearchStrategy strategy = new SearchByNameStrategy();
    List<Estudiante> result = strategy.search("Juan", estudiantes);
    assertEquals(2, result.size());
}

// Test del Command Pattern
@Test
public void testUndoRedo() {
    CommandInvoker invoker = new CommandInvoker();
    invoker.executeCommand(cmd);
    invoker.undo();
    assertEquals(0, repo.findAll().size());
}

// Test del Observer Pattern
@Test
public void testObserverNotified() {
    MockObserver mock = new MockObserver();
    service.registerObserver(mock);
    service.addStudent(...);
    assertTrue(mock.wasNotified());
}
```

### 4. Flexibilidad en Runtime
```java
// Cambiar estrategia sin recompilar
searchContext.setStrategy(getStrategyFromConfig());

// Agregar/remover observadores dinámicamente
if (enableDatabaseLogging) {
    service.registerObserver(new DatabaseObserver());
}

// Rehacer operaciones en cualquier momento
commandInvoker.redo();
```

---

## ✅ CHECKLIST DE COMPRENSIÓN

- [ ] Entiendo cómo Observer notifica cambios automáticamente
- [ ] Entiendo cómo Strategy permite cambiar búsquedas dinámicamente
- [ ] Entiendo cómo Command encapsula operaciones CRUD
- [ ] Entiendo cómo los tres patrones se integran en StudentService
- [ ] Puedo crear una nueva estrategia de búsqueda
- [ ] Puedo agregar un nuevo observador
- [ ] Puedo crear un nuevo comando
- [ ] Entiendo el flujo completo desde Vista → Controlador → Servicio → Patrones

---

**¡Felicidades! Has aprendido cómo se integran los patrones en una aplicación real.** 🎉