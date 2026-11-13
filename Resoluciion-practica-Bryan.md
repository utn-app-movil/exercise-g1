Resolución de la práctica TaskManager

Nombre Bryan  
Rama `Bryan`

 1. La aplicación se cierra al abrir la lista de tareas

- Qué observé. Al tocar Listar tareas, Logcat mostró `ActivityNotFoundException` para `TaskListActivity`. La traza apuntaba a la llamada a `startActivity` en `util.kt`.
- Qué cambié. Registré `.TaskListActivity` dentro de `<application>` en `AndroidManifest.xml`, con `android:exported="false"`.
- Cómo verificarlo. Tocá Listar tareas y comprobá que se abra la pantalla.

![Logcat muestra ActivityNotFoundException al abrir TaskListActivity](evidencias/Problema%201.png)

 2. Enero aparece como mes 00 en el formulario

- Qué observé. Al elegir el 24 de enero de 2026, el depurador mostró `month = 0` en `onDateSet()` y el formulario mostró `24/00/2026`.
- Qué cambié. En `TaskActivity.kt`, pasé `month + 1` a `getDateFormatString()` y conservé `dueMonth = month` para el calendario.
- Cómo verificarlo. Elegí el 24 de enero y comprobá que el formulario muestre `24/01/2026`.

![El depurador muestra month igual a cero para enero](evidencias/Problema%202A.png)

![El formulario muestra 24/00/2026 antes de la corrección](evidencias/Problema%202B.png)

 3. El formato de fecha falla al guardar

- Qué observé. En `parseStringToDateModern()`, el depurador mostró `dateString = "24/01/2026"` y `pattern = "dd/MM/yyyy"`. La función usaba `"MM/dd/yyyy"`, por lo que intentaba leer `24` como mes.
- Qué cambié. En `util.kt`, usé el parámetro `pattern` al crear el formateador. En `TaskActivity.saveTask()`, comprobé si `dueDate` era `null` antes de guardar y pasé la fecha convertida directamente al constructor de `Task`.
- Cómo verificarlo.** Guardá una tarea con fecha `24/01/2026`, buscala por su ID y comprobá que conserve la fecha.

![El depurador muestra el patrón recibido y el formato fijo que usaba la función](evidencias/Problema%203.png)

 4. La segunda fila repite la primera tarea

- Qué observé. En `onBindViewHolder()`, con `position = 1`, el adaptador cargaba `tasks[0]` porque restaba uno a la posición.
- Qué cambié. En `TaskListAdapter.kt`, usé `val item = tasks[position]`.
- Cómo verificarlo. Creá dos tareas con títulos distintos y comprobá que la segunda fila muestre la segunda tarea.

![El adaptador usa la primera tarea al preparar la segunda fila](evidencias/Problema%204.png)

 5. Al tocar una fila, la pantalla de edición recibe el título

- Qué observé. En `onItemClick()`, la tarea tenía ID `T02` y título `Informe final`. La llamada enviaba `task.title` en `EXTRA_MESSAGE_ID`, que la pantalla de edición usa para buscar por ID.
- Qué cambié. En `TaskListActivity.kt`, pasé `task.id` a `util.openActivity()`.
- Cómo verificarlo.** Tocá una tarea cuyo título sea distinto de su ID y comprobá que se carguen sus datos.

![El depurador muestra que se envía el título de la tarea T02](evidencias/Problema%205.png)

 6. El calendario reabre en el mes siguiente

- Qué observé. Con una tarea guardada para el `24/01/2026`, el depurador mostró `dueMonth = 1` al abrir el calendario. Para el selector, ese valor corresponde a febrero.
- Qué cambié. En `TaskActivity.kt`, dentro de `searchTask()`, asigné `dueMonth = task.dueDate.monthValue - 1`. La fecha que se muestra en el formulario sigue usando el mes de `LocalDate`.
- Cómo verificarlo. Cargá la tarea del 24 de enero y comprobá que el calendario abra en enero con el día 24 seleccionado.

![El formulario muestra enero y el depurador muestra dueMonth igual a uno](evidencias/Problema%206.png)

 7. El formulario sigue en modo edición al limpiarse

- Qué observé. Al tocar Cancelar, el depurador se detuvo en `cleanScreen()` después de habilitar el campo de ID. `isEditMode` seguía en `true`.
- Qué cambié. En `TaskActivity.kt`, puse `isEditMode = false` en `cleanScreen()` y dejé que `onPrepareOptionsMenu()` actualice la visibilidad de Eliminar según ese valor.
- Cómo verificarlo. Cargá una tarea y tocá Cancelar. El formulario debe quedar vacío, el campo de ID debe quedar habilitado y el ícono de Eliminar debe desaparecer.

![El depurador muestra isEditMode en true dentro de cleanScreen](evidencias/Problema%207.png)

 8. Las acciones desaparecen en tema oscuro

- Qué observé. Al activar el tema oscuro y abrir Gestión de Tareas, el formulario aparecía sin la barra superior con las acciones.
- Qué cambié. En `res/values-night/themes.xml`, puse `Theme.MaterialComponents.DayNight.DarkActionBar` como tema padre.
- Cómo verificarlo. Abrí Gestión de Tareas en modo oscuro y comprobá que aparezcan Guardar y Cancelar. Al cargar una tarea existente, debe aparecer también Eliminar.

![Formulario en modo oscuro sin la barra de acciones](evidencias/Problema%208.png)

 Funcionalidades agregadas

- Confirmaciones. La pantalla de tareas pide confirmación antes de actualizar o eliminar una tarea existente.
- Estadísticas. La pantalla muestra el total, el conteo por estado y el porcentaje de tareas completadas en una barra horizontal. Los cálculos se hacen en `TaskViewModel`.
- Búsqueda y filtros. La lista combina el texto ingresado con el estado seleccionado. El texto se busca en el título y la descripción.
- Eliminar y deshacer. Al deslizar una tarea a la izquierda, se elimina la fila y aparece la opción Deshacer para restaurarla.
