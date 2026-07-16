# 🏗️ DIAGRAMA DE CLASES - ARQUITECTURA REFACTORIZADA

## DIAGRAMA UML SIMPLIFICADO

```
╔════════════════════════════════════════════════════════════════════════════╗
║                        CAPA DE APLICACIÓN (VIEW)                          ║
╟────────────────────────────────────────────────────────────────────────────╢
║ FormularioCrudEstudiante implements ViewStudentObserver                   ║
║ ────────────────────────────────────────────────────────────────────────  ║
║ - controlador: ControlEstudiante                                          ║
║ - txtId, txtNombre, txtEdad, txtCarrera: JTextField                       ║
║ - tablaEstudiantes: JTable                                               ║
║ + clickAgregar()                                                          ║
║ + clickActualizar()                                                       ║
║ + clickEliminar()                                                         ║
║ + clickDeshacer()                                                         ║
║ + clickRehacer()                                                          ║
║ + clickBuscar()                                          ┌─────────────┐ ║
║ + onStudentAdded(Estudiante)                           │   App.main()│ ║
║ + onStudentUpdated(Estudiante)                         └─────────────┘ ║
║ + onStudentDeleted(String id)                                           ║
║ + refreshTable()                                                        ║
╚════════════════════════════════════════════════════════════════════════════╝
                                    ▲
                                    │ usa
                                    │
╔════════════════════════════════════════════════════════════════════════════╗
║                      CAPA DE CONTROLADOR (CONTROLLER)                     ║
╟────────────────────────────────────────────────────────────────────────────╢
║ ControlEstudiante                                                        ║
║ ────────────────────────────────────────────────────────────────────────  ║
║ - service: StudentService                                               ║
║ + agregarEstudiante(id, nombre, edad[, carrera])                        ║
║ + actualizarEstudiante(id, nombre, edad[, carrera])                     ║
║ + eliminarEstudiante(id)                                                ║
║ + mostrarTodos(): List<Estudiante>                                      ║
║ + buscarPorId(id)                                                       ║
║ + buscarPorNombre(nombre)                                               ║
║ + buscarPorCarrera(carrera)                                             ║
║ + deshacer()                                                            ║
║ + rehacer()                                                             ║
║ + getService(): StudentService                                         ║
╚════════════════════════════════════════════════════════════════════════════╝
                                    ▲
                                    │ delega
                                    │
╔════════════════════════════════════════════════════════════════════════════╗
║                    CAPA DE SERVICIO (SERVICE) - ORQUESTADOR              ║
╟────────────────────────────────────────────────────────────────────────────╢
║ StudentService                                                           ║
║ ────────────────────────────────────────────────────────────────────────  ║
║ - repository: StudentRepository                                         ║
║ - commandInvoker: CommandInvoker                                        ║
║ - searchContext: SearchContext                                          ║
║ + registerObserver(observer: StudentObserver)                           ║
║ + unregisterObserver(observer: StudentObserver)                         ║
║ + addStudent(id, nombre, edad, carrera)                                ║
║ + updateStudent(id, nombre, edad, carrera)                             ║
║ + deleteStudent(id)                                                    ║
║ + search(criterio): List<Estudiante>                                   ║
║ + searchById(id)                                                       ║
║ + searchByName(nombre)                                                 ║
║ + searchByCareer(carrera)                                              ║
║ + getAllStudents(): List<Estudiante>                                   ║
║ + undo()                                                               ║
║ + redo()                                                               ║
╚════════════════════════════════════════════════════════════════════════════╝
                 ▲                          ▲                       ▲
                 │ usa                      │ usa                   │ usa
                 │                          │                       │
  ┌──────────────┴──────────┐  ┌────────────┴──────────┐  ┌────────┴────────┐
  │                         │  │                       │  │                 │
  ▼                         ▼  ▼                       ▼  ▼                 ▼
┌─────────────────┐ ┌────────────────┐ ┌────────────────────┐ ┌──────────────┐
│ REPOSITORY      │ │ COMMAND PATTERN│ │ STRATEGY PATTERN   │ │ OBSERVABLE   │
├─────────────────┤ ├────────────────┤ ├────────────────────┤ ├──────────────┤
│StudentRepository│ │CommandInvoker  │ │ SearchContext      │ │ Observers    │
│implements       │ │────────────────│ │────────────────────│ │──────────────│
│StudentObservable│ │- historial     │ │- strategy          │ │ Attached:    │
│                 │ │+ execute()     │ │+ setStrategy()     │ │ • Console    │
│+ save()         │ │+ undo()        │ │+ executeSearch()   │ │   Observer   │
│+ update()       │ │+ redo()        │ │                    │ │ • View       │
│+ delete()       │ │                │ │                    │ │   Observer   │
│+ findById()     │ │                │ │ SearchStrategy <<i>│ │              │
│+ findAll()      │ │                │ │────────────────────│ │ + attach()   │
│                 │ │ Command <<i>   │ │ + SearchByIdStrategy
│+ attach()       │ │────────────────│ │ + SearchByNameStrat.
│+ detach()       │ │+ execute()     │ │ + SearchByCareerStrat│
│+ notify...()    │ │+ undo()        │ │                    │ │              │
│                 │ │+ getDescription
│                 │ │                │ │ Concrete Commands:
└─────────────────┘ │ Concrete:      │ │────────────────────┤ └──────────────┘
                    │ + AddStudent   │ │                    │
                    │ + UpdateStudent
                    │ + DeleteStudent│ │                    │
                    └────────────────┘ └────────────────────┘
```

---

## PATRONES IMPLEMENTADOS

### 1. OBSERVER PATTERN
```
┌─────────────────────────┐
│ StudentObservable <<i>> │
├─────────────────────────┤
│ + attach()              │
│ + detach()              │
│ + notifyStudentAdded()  │
│ + notifyStudentUpdated()│
│ + notifyStudentDeleted()│
└────────────────┬────────┘
                 △
                 │ implements
                 │
    ┌────────────┴──────────────┐
    │                           │
    ▼                           ▼
┌──────────────────┐  ┌──────────────────────┐
│ StudentRepository│  │ StudentObserver <<i>>│
└──────────────────┘  ├──────────────────────┤
                      │ + onStudentAdded()   │
                      │ + onStudentUpdated() │
                      │ + onStudentDeleted() │
                      └──────────────────────┘
                                △
                                │ implements
                      ┌─────────┴─────────┐
                      │                   │
                      ▼                   ▼
                ┌───────────────┐  ┌────────────────┐
                │ ConsoleStudent│  │ ViewStudentObs.│
                │  Observer     │  │                │
                └───────────────┘  ├────────────────┤
                                   │ + refreshTable()
                                   └────────────────┘
```

### 2. STRATEGY PATTERN
```
┌──────────────────┐
│ SearchStrategy   │ <<interface>>
│ <<interface>>    │
├──────────────────┤
│ + search()       │
└────────┬─────────┘
         △
         │ implements
    ┌────┼────┬─────────────┐
    │    │    │             │
    ▼    ▼    ▼             ▼
  ┌──┐ ┌──┐ ┌──┐         ┌──────────┐
  │By│ │By│ │By│         │ (Custom) │
  │ID│ │Na│ │Ca│         │Strategy  │
  └──┘ └──┘ └──┘         └──────────┘
    │    │    │             │
    └────┼────┴─────────────┘
         │
         △ used by
         │
    ┌────────────────┐
    │ SearchContext  │
    ├────────────────┤
    │- strategy      │
    │+ setStrategy() │
    │+ executeSearch()
    └────────────────┘
```

### 3. COMMAND PATTERN
```
┌──────────────────┐
│    Command       │ <<interface>>
│ <<interface>>    │
├──────────────────┤
│ + execute()      │
│ + undo()         │
│ + getDescription()
└────────┬─────────┘
         △
         │ implements
    ┌────┼────┬──────────────┐
    │    │    │              │
    ▼    ▼    ▼              ▼
┌──────┐┌──────┐┌─────┐  ┌───────┐
│ Add  ││Update││Delete│  │(Custom)
│ Cmd  ││ Cmd  ││ Cmd  │  │Command
└──────┘└──────┘└─────┘  └───────┘
    │    │    │         │
    └────┼────┴─────────┘
         │
         △ executed by
         │
    ┌──────────────────┐
    │ CommandInvoker   │
    ├──────────────────┤
    │- historial (Stack
    │+ executeCommand()│
    │+ undo()          │
    │+ redo()          │
    └──────────────────┘
```

---

## FLUJO DE DATOS

### Caso: Agregar Estudiante

```
Usuario              Vista               Controller          Service
  │                   │                    │                   │
  │ Click Agregar     │                    │                   │
  ├──────────────────►│                    │                   │
  │                   │ clickAgregar()     │                   │
  │                   ├───────────────────►│                   │
  │                   │                    │ agregarEstudiante()
  │                   │                    ├──────────────────►│
  │                   │                    │                   │
  │                   │                    │  new AddStudentCmd
  │                   │                    │◄──────────────────┤
  │                   │                    │                   │
  │                   │                    │ CommandInvoker
  │                   │                    │   .execute(cmd)
  │                   │                    ├───┐               │
  │                   │                    │   │               │
  │                   │                    │   └──────────────►│
  │                   │                    │       Repository
  │                   │                    │        .save()
  │                   │                    │◄──────────────────┤
  │                   │                    │                   │
  │                   │                    │    Notify All
  │                   │                    │    Observers
  │                   │                    ├──────────────────►│
  │                   │                    │◄──────────────────┤
  │                   │                    │                   │
  │                   │◄───────────────────┤                   │
  │◄──────────────────┤ Mensaje éxito      │                   │
  │                   │                    │                   │
  │ Tabla actualiza   │ refreshTable()     │                   │
  │ (automático)      │◄───────────────────┤                   │
  │                   │ (del Observer)     │                   │
  │                   │                    │                   │
```

### Caso: Buscar por Nombre

```
Usuario              Vista               Controller       Service
  │                   │                    │                │
  │ Ingresa criterio  │                    │                │
  │ y selecciona tipo │                    │                │
  │                   │                    │                │
  │ Click Buscar      │                    │                │
  ├──────────────────►│                    │                │
  │                   │ clickBuscar()      │                │
  │                   ├───────────────────►│                │
  │                   │                    │                │
  │                   │                    │ buscarPorNombre()
  │                   │                    ├───────────────►│
  │                   │                    │                │
  │                   │                    │ SearchContext
  │                   │                    │ .setStrategy()
  │                   │                    ├───────────────►│
  │                   │                    │◄───────────────┤
  │                   │                    │                │
  │                   │                    │ .executeSearch()
  │                   │                    ├───────────────►│
  │                   │                    │◄───────────────┤
  │                   │◄───────────────────┤ List<Results>   │
  │◄──────────────────┤ mostrarTabla()     │                │
  │                   │                    │                │
  │ Ver resultados    │                    │                │
  │                   │                    │                │
```

---

## RELACIONES ENTRE CLASES

### Composición (Has-a)
- `StudentService` HAS-A `StudentRepository`
- `StudentService` HAS-A `CommandInvoker`
- `StudentService` HAS-A `SearchContext`
- `StudentRepository` HAS-A `List<StudentObserver>`
- `CommandInvoker` HAS-A `Stack<Command>` (historial)
- `SearchContext` HAS-A `SearchStrategy`
- `FormularioCrudEstudiante` HAS-A `ControlEstudiante`
- `ControlEstudiante` HAS-A `StudentService`

### Asociación (Uses)
- `StudentRepository` USES `Estudiante` (modelo)
- `Command` USES `StudentRepository`
- `SearchStrategy` USES `Estudiante`
- `StudentObserver` USES `Estudiante`

### Implementación (Implements)
- `StudentRepository` IMPLEMENTS `StudentObservable`
- `ConsoleStudentObserver` IMPLEMENTS `StudentObserver`
- `FormularioCrudEstudiante` IMPLEMENTS `ViewStudentObserver`
- `SearchByIdStrategy` IMPLEMENTS `SearchStrategy`
- `SearchByNameStrategy` IMPLEMENTS `SearchStrategy`
- `SearchByCareerStrategy` IMPLEMENTS `SearchStrategy`
- `AddStudentCommand` IMPLEMENTS `Command`
- `UpdateStudentCommand` IMPLEMENTS `Command`
- `DeleteStudentCommand` IMPLEMENTS `Command`

---

## PAQUETES Y RESPONSABILIDADES

```
┌─────────────────────────────────────────────────────────────────┐
│                          MODEL                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ Estudiante                                               │  │
│  │ - Entidad de dominio que representa un estudiante        │  │
│  │ - Contiene datos: id, nombre, edad, carrera            │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                       REPOSITORY                                │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ StudentRepository (implements StudentObservable)         │  │
│  │ - Gestiona persistencia en memoria                       │  │
│  │ - Notifica observadores en cambios                       │  │
│  │ - CRUD: save, update, delete, findById, findAll         │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                       OBSERVER                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────────┐   │
│  │StudentObserver   │StudentObsrvbl│  │ConsoleStudentObs   │   │
│  │<<interface>>      │<<interface>>  │                    │   │
│  │ - onStudentAddd   │ - attach     │                    │   │
│  │ - onStudentUpd    │ - detach     │                    │   │
│  │ - onStudentDel    │ - notify...  │                    │   │
│  └──────────────┘  └──────────────┘  └────────────────────┘   │
│  ┌────────────────────────────────────────────────────────┐   │
│  │ ViewStudentObserver (extends ViewObserver)             │   │
│  │ - Actualiza tabla cuando hay cambios                   │   │
│  └────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                       STRATEGY                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ SearchStrategy <<interface>>                             │  │
│  │ + search(criterio, estudiantes): List                    │  │
│  └──────────────────────────────────────────────────────────┘  │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────────────┐   │
│  │ By ID        │ │ By Name      │ │ By Career            │   │
│  │ Strategy     │ │ Strategy     │ │ Strategy             │   │
│  └──────────────┘ └──────────────┘ └──────────────────────┘   │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ SearchContext                                            │  │
│  │ - strategy: SearchStrategy                               │  │
│  │ + setStrategy(new Strategy)                              │  │
│  │ + executeSearch()                                        │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                       COMMAND                                   │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ Command <<interface>>                                    │  │
│  │ + execute()                                              │  │
│  │ + undo()                                                 │  │
│  │ + getDescription()                                       │  │
│  └──────────────────────────────────────────────────────────┘  │
│  ┌──────────┐  ┌──────────┐  ┌────────────┐                    │
│  │ Add Cmd  │  │Update Cmd│  │ Delete Cmd │                    │
│  └──────────┘  └──────────┘  └────────────┘                    │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ CommandInvoker                                           │  │
│  │ - historial: Stack<Command>                              │  │
│  │ + executeCommand(cmd)                                    │  │
│  │ + undo()                                                 │  │
│  │ + redo()                                                 │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                       SERVICE                                   │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ StudentService (Orquestador)                             │  │
│  │ - Integra Repository + Observer + Strategy + Command     │  │
│  │ - Interface unificada para toda la lógica de negocio     │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                       CONTROLLER                                │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ ControlEstudiante                                        │  │
│  │ - Intermediario entre Vista y Service                    │  │
│  │ - Proporciona métodos amigables para la vista            │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                         VIEW                                    │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │ FormularioCrudEstudiante (implements ViewObserver)       │  │
│  │ - Interfaz gráfica Swing                                 │  │
│  │ - Recibe notificaciones automáticas de cambios           │  │
│  │ - Permite CRUD, búsqueda, undo/redo                      │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

---

## CAPAS ARQUITECTÓNICAS

```
┌─────────────────────────────────────────────────────────┐
│              CAPA DE PRESENTACIÓN (View)               │
│  FormularioCrudEstudiante - Interfaz Gráfica Swing    │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│           CAPA DE CONTROL (Controller)                 │
│    ControlEstudiante - Lógica de presentación         │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│              CAPA DE SERVICIO (Service)                │
│  StudentService - Orquestación de patrones            │
└─┬───────────────────────────┬──────────────────────┬───┘
  │                           │                      │
┌─▼──────────────┐ ┌──────────▼──────┐ ┌────────────▼──┐
│  Command       │ │ Strategy        │ │ Observer     │
│  Pattern       │ │ Pattern         │ │ Pattern      │
│                │ │                 │ │              │
│ CommandInvoker │ │ SearchContext   │ │ Notifiers    │
│ Undo/Redo      │ │ + 3 Strategies  │ │ + Console    │
│                │ │                 │ │ + View       │
└─┬──────────────┘ └────────┬────────┘ └────────┬─────┘
  │                        │                    │
  └────────────┬───────────┴────────────────────┘
               │
┌──────────────▼──────────────────────────────────────────┐
│         CAPA DE PERSISTENCIA (Repository)              │
│  StudentRepository - Almacenamiento en memoria         │
│  Implementa Observable para notificaciones             │
└──────────────────────────────────────────────────────────┘
               │
┌──────────────▼──────────────────────────────────────────┐
│            CAPA DE DATOS (Model)                        │
│  Estudiante - Entidad de dominio                       │
└──────────────────────────────────────────────────────────┘
```

---

## CONCLUSIÓN

Esta arquitectura refactorizada proporciona:
- ✅ Separación clara de capas
- ✅ Implementación correcta de patrones de diseño
- ✅ Alto grado de desacoplamiento
- ✅ Fácil extensibilidad
- ✅ Mantenibilidad mejorada
- ✅ Testabilidad aumentada