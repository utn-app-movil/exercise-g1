# Práctica de Laboratorio: TaskManager - Depuración y Extensión de Funcionalidades

**Asignatura:** Desarrollo de Aplicaciones Móviles  
**Duración Estimada:** 4 Horas  
**Modalidad:** Individual o en Parejas  
**Nivel:** Intermedio (Kotlin, Android SDK, MVVM, Architecture Components)

---

## 📋 Descripción General

En esta práctica de laboratorio, los estudiantes tomarán como base el proyecto **TaskManager** (una aplicación móvil para la gestión de tareas personales) para poner a prueba y fortalecer sus habilidades de **depuración de código (debugging)**, **arquitectura de software en Android (MVVM)**, **interacción con interfaz de usuario (UI)** y **colecciones/filtrado en Kotlin**.

---

## 🎯 Objetivos de Aprendizaje

1. **Aprender a depurar aplicaciones Android:** Utilizar las herramientas del IDE (**Logcat, Breakpoints, Inspect Variables y Stack Trace**) para diagnosticar y corregir errores en tiempo de ejecución (Runtime Exceptions) y errores de lógica.
2. **Implementar componentes de arquitectura:** Desarrollar nuevas actividades (`Activity`), manejar la navegación mediante `Intents`, conectar la capa de presentación con el `ViewModel` y uso de `AlertDialog` y `Spinner`.
3. **Manipular datos y colecciones:** Aplicar transformaciones y filtrados dinámicos sobre colecciones de Kotlin (`filter`, `count`, `map`).

---

## 🐞 Fase 0: Guía de Errores Intencionales de Código (Debugging - 45 min)

1. El proyecto base contiene **8 errores intencionales de código** que los estudiantes deben encontrar y solucionar utilizando el modo Debug de Android Studio antes de avanzar.

2. Debe mostrar un dialog con una pregunta de verificación cuando desea actualizar o eliminar en la pantalla de Task.
---

## 🚀 Fase 1: Nueva Funcionalidad 1 - Pantalla de Estadísticas y Resumen (`TaskStatsActivity`) (75 min)

Desarrollar una nueva pantalla que presente métricas y estadísticas sobre las tareas registradas.

### Requisitos Técnicos:

1. **Creación de la Activity:**
    - Crear la pantalla `TaskStatsActivity`.
    - Agregar un botón en `MainActivity` o en el menú de la aplicación que permita navegar a esta pantalla mediante un `Intent`.

2. **Diseño de la Interfaz (`activity_task_stats.xml`):**
    - Un `TextView` grande con el **Total de Tareas Registradas**.
    - Conteo de tareas desglosado por cada Estado:
        - **Pendientes** (`PENDING`)
        - **En Progreso** (`IN_PROGRESS`)
        - **Completadas** (`COMPLETED`)
        - **Canceladas** (`CANCELLED`)
    - Una barra de progreso (`ProgressBar` horizontal) que muestre visualmente el porcentaje de tareas completadas respecto al total.
    - Botón para **Volver al Menú Principal**.

3. **Lógica de Negocio en ViewModel:**
    - Obtener la lista de tareas desde `TaskViewModel`.
    - Calcular los totales utilizando funciones de orden superior de Kotlin (`count`, `filter`).

---

## 🔍 Fase 2: Nueva Funcionalidad 2 - Búsqueda Dinámica y Filtros por Estado en `TaskListActivity` (75 min)

Mejorar la pantalla de lista de tareas (`TaskListActivity`) para permitir al usuario buscar y filtrar tareas en tiempo real.

### Requisitos Técnicos:

1. **Componentes de UI en `activity_task_list.xml`:**
    - Agregar un `SearchView` (o `EditText` con listener de cambio de texto) en la parte superior del `RecyclerView`.
    - Agregar un `Spinner` o grupo de botones (`ChipGroup` / `RadioGroup`) con las opciones: `TODAS`, `PENDING`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`.

2. **Lógica de Filtrado Combinado:**
    - Implementar un método que reciba el texto de búsqueda y el estado seleccionado para filtrar en tiempo real.

---

## 🔥 Fase 3: Retos Adicional (45 min)

### 🏆 Reto A: Eliminar Tareas con Gestos (Swipe to Delete) y Opción Undo
- Implementar un `ItemTouchHelper.SimpleCallback` en el `RecyclerView` de `TaskListActivity`.
- Al eliminar, mostrar un `Snackbar` con la opción **"DESHACER"** para restaurar la tarea.
---

## 📊 Criterios de Evaluación

| Criterio |   Porcentaje    | Descripción |
| :--- |:---------------:| :--- |
| **1. Depuración (Fase 0)** |     **20%**     | Solución correcta de los 4 errores intencionales usando el Debugger. |
| **2. Pantalla de Estadísticas (Fase 1)** |     **30%**     | Diseño adecuado en XML, cálculo correcto de métricas y navegación funcional. |
| **3. Búsqueda y Filtros (Fase 2)** |     **30%**     | Filtrado combinado en tiempo real por texto y estado. |
| **4. Calidad del Código y Buenas Prácticas** |     **10%**     | Uso de arquitectura MVVM y nombres limpios. |
| **5. Reto (Fase 3)** | **10%** | Implementación correcta de Swipe-to-Delete. |
| **TOTAL** |    **100%**     | |


### Entregable:
- Deberá crear un branch de trabajo a partir de main.
- Cada persona deberá hacer sus propios commits y push.
- Debe crear un archivo readme con el siguiente nombre: Resoluciion-practica-< nombre >.md
- En el archivo de readme debe explicar los issues que encontró y cómo lo solucionó. Debe agregar screenshot como evidencia del debugging que realizó en la aplicación.
- Deberá crear un PR de su branch de trabajo hacia main.
- Debe agregar al profesor (ebarahonautn) como reviewer de su PR.
- Cada uno debe subir al campus el link del PR donde realizó su práctica.