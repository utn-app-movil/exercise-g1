\# Resolución práctica – TaskManager (Depuración y Extensión)



\*\*Estudiante:\*\* Allen Vargas Cousin

\*\*Rama:\*\* AllenVC



\---



\## Fase 0: Problemas encontrados y cómo los solucioné



\### 1. La app se cerraba al tocar "Task List"

\- \*\*Cómo lo detecté:\*\* al tocar el botón la app se cerraba. En Logcat apareció `ActivityNotFoundException` ("have you declared this activity in your AndroidManifest.xml?").

\- \*\*Causa:\*\* `TaskListActivity` no estaba declarada en `AndroidManifest.xml`.

\- \*\*Solución:\*\* agregué la etiqueta `<activity android:name=".TaskListActivity">` en el manifiesto.

\- \*\*Evidencia:\*\* !\[La app se cierra](capturas/2\_la\_app\_se\_cierra.png)



\### 2. La fecha no se guardaba (por ejemplo, con el día 25)

\- \*\*Cómo lo detecté:\*\* puse un breakpoint en `saveTask()` y con Debug revisé el valor de la fecha: al parsear devolvía `null`.

\- \*\*Causa:\*\* en `util.kt` (línea 27) el formato estaba escrito a mano como `"MM/dd/yyyy"` e ignoraba el parámetro `pattern`.

\- \*\*Solución:\*\* cambié a `DateTimeFormatter.ofPattern(pattern, Locale.getDefault())`.



\### 3. El mes se mostraba uno menos

\- \*\*Causa:\*\* `DatePickerDialog` cuenta los meses de 0 a 11 y en `onDateSet` se mostraba el valor sin ajustar.

\- \*\*Solución:\*\* mostrar `month + 1` en pantalla y dejar `dueMonth` en base 0 para el calendario.



\### 4. Al editar, el calendario abría un mes corrido

\- \*\*Causa:\*\* en `searchTask`, `dueMonth = task.dueDate.monthValue` guardaba el mes en base 1 (1 a 12).

\- \*\*Solución:\*\* `dueMonth = task.dueDate.monthValue - 1`.



\### 5. No avisaba cuando el ID ya existía y quedaba visible el ícono de eliminar

\- \*\*Causa:\*\* `cleanScreen()` dejaba `isEditMode = true`.

\- \*\*Solución:\*\* `isEditMode = false`.



\### 6. La lista repetía la primera tarea

\- \*\*Causa:\*\* en `TaskListAdapter.onBindViewHolder` se usaba `tasks\[position - 1]`.

\- \*\*Solución:\*\* `tasks\[position]`.

\- \*\*Evidencia:\*\* !\[Lista con tarea repetida](capturas/4\_lista\_tarea\_repetida.png)



\### 7. "La información no existe" al abrir una tarea desde la lista

\- \*\*Causa:\*\* `TaskListActivity.onItemClick` enviaba `task.title` y `TaskActivity` busca por ID.

\- \*\*Solución:\*\* enviar `task.id`.

\- \*\*Evidencia:\*\* !\[Información no encontrada](capturas/3\_informacion\_no\_encontrada.png)



\### Diálogos y navegación (requeridos por la práctica)

\- Diálogo de confirmación antes de \*\*eliminar\*\* y antes de \*\*guardar\*\* con `util.showDialogCondition`, con textos en `strings.xml` (español e inglés).

\- Flecha de volver en `TaskActivity` (`android.R.id.home -> finish()`).



\---



\## Fase 1: Pantalla de estadísticas (`TaskStatsActivity`)

\- Pantalla nueva con el total de tareas, el conteo por estado y una barra de progreso con el porcentaje de completadas.

\- Botón "Estadísticas" en `MainActivity` que abre la pantalla con un `Intent` (`util.openActivity`), y botón para volver al menú principal.

\- Los cálculos están en `TaskViewModel` (`countTotal`, `countByStatus`, `completedPercent`) y usan `count`. El porcentaje evita dividir entre cero.

\- La pantalla se actualiza en `onResume`.

\- Declarada en el manifiesto.



\## Fase 2: Búsqueda y filtros en `TaskListActivity`

\- `EditText` de búsqueda y `Spinner` con las opciones `TODAS`, `PENDING`, `IN\_PROGRESS`, `COMPLETED` y `CANCELLED`.

\- `applyFilters(text, status)` filtra siempre sobre la lista completa (`allTasks`) combinando el texto (sin distinguir mayúsculas) y el estado, y se ejecuta en tiempo real con un `TextWatcher` y un `OnItemSelectedListener`.

\- `TaskListAdapter.updateTasks()` recibe la lista filtrada y llama a `notifyDataSetChanged()`.

\- \*\*Problema adicional encontrado:\*\* al editar una tarea y volver, la lista no reflejaba el cambio porque solo se cargaba en `onCreate`. Se solucionó recargando y aplicando el filtro en `onResume`.

