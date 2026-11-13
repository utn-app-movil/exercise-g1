# TaskManager – Errores encontrados y soluciones

Práctica de corrección de errores en la aplicación Android **TaskManager**.

---

## Error #1: Fecha atrasada un mes

**Problema:** el mes se mostraba incorrectamente porque no se sumaba 1 al formatear la fecha. El `DatePickerDialog` entrega el mes en base 0 (enero = 0).

**Antes (error):**

![Calendario con el 5 de noviembre de 2026 seleccionado](imagenes/error1Img1.png)

![El campo Due Date muestra 05/10/2026, un mes atrasado](imagenes/error1Img2.png)

**Evidencia de debugging:**

![Breakpoint en onDateSet: month vale 10 al elegir noviembre](imagenes/error1Img3.png)

**Solución:**
- En `TaskActivity`, en la función `onDateSet`, al llamar a `getDateFormatString` se le suma `+1` al mes.

**Después (corregido):**

![Código: onDateSet suma 1 al mes al formatear la fecha](imagenes/solu1Img2.png)

---

## Error #2: `parseStringToDateModern` ignora el parámetro `pattern`

**Problema:** en la clase `util`, la función `parseStringToDateModern` tenía el formato de fecha hardcodeado (`"MM/dd/yyyy"`) e ignoraba el parámetro `pattern`.

**Solución:** se cambió el formato fijo para que la función utilice el parámetro `pattern`.

**Después (corregido):**

![Código: parseStringToDateModern usa el parámetro pattern en DateTimeFormatter.ofPattern](imagenes/solu1Img1.png)

---

## Error #3: La aplicación se cierra al abrir la lista de tareas

**Problema:** al tocar el botón *Listar tareas* en la pantalla principal, la aplicación se cerraba.

**Antes (error):**

![Al tocar Listar tareas la aplicación se cierra](imagenes/error2img1.png)

**Solución:** revisando el `AndroidManifest.xml`, la clase `TaskListActivity` no estaba declarada. Se agregó al manifest.

**Después (corregido):**

![AndroidManifest.xml con TaskListActivity declarada y la lista abierta en el emulador](imagenes/solu2Img1.png)

---

## Error #4: El calendario abre un mes adelantado al modificar la fecha

**Problema:** al guardar una tarea, buscarla por id y querer modificar la fecha, el calendario aparecía por defecto un mes adelantado. Al cargar los datos, la fecha sí se mostraba bien.

**Antes (error):**

![Tarea cargada por id con la fecha 14/11/2026](imagenes/error3img1.png)

![Al abrir el calendario aparece diciembre en lugar de noviembre](imagenes/error3img2.png)

**Evidencia de debugging:**

![Breakpoint en showDatePickerDialog: dueMonth vale 11 para una tarea de noviembre](imagenes/error3img3.png)

**Solución:** en la función `searchTask` de la clase `TaskActivity`, restar 1 a `dueMonth` (`task.dueDate.monthValue - 1`), porque el `DatePickerDialog` usa meses de 0 a 11.

**Después (corregido):**

![Código: searchTask resta 1 a dueMonth](imagenes/solu3Img1.png)

---

## Error #5: Se enviaba el título en lugar del id al abrir una tarea

**Problema:** en la lista de tareas, al hacer clic en una tarea, el título se cargaba en el campo de id de la pantalla de gestión.

**Antes (error):**

![Lista con la tarea cuyo título es task](imagenes/error4img1.png)

![Al tocar la tarea, el campo Id muestra el título y los demás campos quedan vacíos](imagenes/error4img2.png)

**Solución:** en la función `onItemClick` de `TaskListActivity`, cambiar el valor enviado en `util.openActivity` de `task.title` a `task.id`.

**Después (corregido):**

![Código: onItemClick envía task.id](imagenes/solu4Img1.png)

---

## Error #6: La lista mostraba la primera tarea repetida

**Problema:** al guardar varias tareas, la lista mostraba la tarea 1 con sus datos correctos y la segunda también mostraba los datos de la tarea 1.

**Antes (error):**

![Primera tarea guardada: task1 con fecha 14/11/2026](imagenes/error5img1.png)

![Segunda tarea guardada: task2 con fecha 21/11/2026](imagenes/error5img2.png)

![La lista muestra task1 dos veces en lugar de task1 y task2](imagenes/error5img3.png)

**Solución:** en `TaskListAdapter.onBindViewHolder`, mostrar los datos únicamente según la posición: `val item = tasks[position]`.

**Después (corregido):**

![Lista corregida: task1 y task2 en filas distintas](imagenes/solu5Img1.png)

![Código: onBindViewHolder usa tasks[position]](imagenes/solu5Img2.png)

---

## Error #7: Se podía sobrescribir una tarea con un id ya existente

**Problema:** después de usar *cleanScreen* (guardar o cancelar), la aplicación permitía guardar una tarea con un id ya existente, sobrescribiendo los datos sin avisar.

**Antes (error):**

![Tarea guardada con id 1 y título task1](imagenes/error6img1.png)

![Mismo id 1 guardado con otro título, sin aviso de duplicado](imagenes/error6img2.png)

![Pantalla vacía después de guardar (cleanScreen)](imagenes/error6img3.png)

**Evidencia de debugging:**

![Breakpoint en saveTask: ya existe una tarea con ese id, pero isEditMode vale true](imagenes/error6img4.png)

**Solución:** en la función `cleanScreen` de `TaskActivity`, cambiar `isEditMode = true` por `isEditMode = false`, para que se valide si ya existe una tarea con ese id.

**Después (corregido):**

![Prueba con la corrección: formulario con el id 1 ya existente](imagenes/solu6Img1.png)

![Código: cleanScreen con isEditMode = false](imagenes/solu6Img2.png)

---

## Error #8: La lista no se actualiza al volver de editar o eliminar una tarea

**Problema:** al editar o eliminar una tarea abierta desde `TaskListActivity`, al volver a la lista la fila sigue mostrando los datos anteriores. `TaskListActivity` crea el adapter una sola vez en `onCreate` y nunca lo refresca con `notifyDataSetChanged()`, aunque `TaskMemoryManager` sí actualiza la lista en memoria. Para reproducirlo hay que abrir una tarea desde la lista, editar datos como el título o el estado, y volver con el botón Back del sistema, no con la flecha de la app.

**Antes (error):**

Edición de una tarea:

![Lista con la tarea antes de editarla](imagenes/error8img1.png)

![Editar los datos de las task1](imagenes/error8img2.png)

![Al volver con Back, la lista sigue mostrando los datos viejos](imagenes/error8img3.png)

![Al rotar la pantalla aparecen los datos nuevos](imagenes/error8img4.png)

Eliminación de una tarea:

![Tarea que se elimina](imagenes/error8img5.png)

![Confirmación de tarea eliminada](imagenes/error8img6.png)

![Tras eliminarla, la tarea sigue apareciendo en la lista](imagenes/error8img7.png)

![Al entrar de nuevo a la tarea, los datos no se encuentran](imagenes/error8img8.png)

**Solución:** guardar el adapter en una variable de la clase (`private lateinit var taskAdapter`) y, en `onResume`, llamar a `taskAdapter.notifyDataSetChanged()` para que la lista se actualice cada vez que se vuelve de `TaskActivity`.

**Después (corregido):**

![Código: añadir la variable taskAdapter](imagenes/solu8img1.png)

![Código: refresco de la lista en onResume con notifyDataSetChanged](imagenes/solu8img2.png)