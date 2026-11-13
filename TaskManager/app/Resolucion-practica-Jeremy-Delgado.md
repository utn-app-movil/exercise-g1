# Errores encontrados en el Lab

Resolución de la práctica — Jeremy Delgado.




---

## Error 1

**Problema:** Fecha atrasada un mes. El mes se mostraba incorrectamente porque no se estaba sumando 1 al formatear la fecha.

![Captura: calendario mostrando el mes incorrecto](img/img1_Error1.png)

![Captura: fecha mostrada en el campo Due Date](img/img2_Error1.png)

**Solución:** En la función `onDateSet`, dentro de su función interna `getDateFormatString`, al parámetro `monthValue` se le añadió un `+ 1` para que indique el mes correcto.

![Código: onDateSet con monthValue = month + 1](img/img4_Error1.png)

---

## Error 2

**Problema:** `parseStringToDateModern` ignoraba el parámetro `pattern` y utilizaba un formato fijo.

**Solución:** Se cambió el formato fijo de la fecha para que reciba el parámetro `pattern`.

![Código: parseStringToDateModern usando el parámetro pattern](img/img3_Error1.png)

---

## Error 3

**Problema:** `TaskListActivity` se cerraba porque no estaba declarado en el manifest.

**Solución:**

![Código: AndroidManifest.xml con TaskListActivity declarado](img/img1_Error2.png)

---

## Error 4

**Problema:** Al guardar una task y buscarla con el id, al modificar su campo fecha el calendario inicia en enero o un mes adelantado, y no en su mes guardado.

![Captura: calendario abierto en un mes distinto al guardado (diciembre)](img/img1_Error3.png)

![Captura: calendario abierto en otro mes incorrecto (enero)](img/img2_Error3.png)

**Solución:** Añadir un `-1` a la variable `dueMonth` en la función `searchTask` de la clase `TaskActivity`.

![Código: searchTask con dueMonth = task.dueDate.monthValue - 1](img/img3_Error3.png)

---

## Error 5

**Problema:** Al abrir una tarea desde `TaskListActivity` se enviaba el `title` en lugar del `id` de la Task.

![Código: onItemClick enviando task.title](img/img1_Error4.png)

![Captura: lista de tareas en TaskListActivity](img/img2_Error4.png)

![Captura: TaskActivity abierta sin los datos de la tarea](img/img3_Error4.png)

**Solución:** Enviar el `id`, para que `TaskManager` encuentre la Task y su data.

![Código: onItemClick enviando task.id](img/img4_Error4.png)

![Captura: TaskActivity mostrando los datos correctos de la tarea](img/img5_Error4.png)

---

## Error 6

**Problema:** Al guardar las tareas, en `TaskList` se mostraba la Task 1 con sus datos correctos, pero la segunda también mostraba los datos de la Task 1.

![Código: onBindViewHolder con el cálculo de posición original](img/img1_Error5.png)

![Captura: datos de la Task 1](img/img2_Error5.png)

![Captura: datos de la Task 2](img/img3_Error5.png)

![Captura: lista mostrando datos repetidos de la Task 1](img/img4_Error5.png)

**Solución:** Mostrar los datos solo por la posición.

![Código: onBindViewHolder con val item = tasks[position]](img/img5_Error5.png)

![Captura: lista mostrando cada tarea con sus datos correctos](img/img6_Error5.png)

---

## Error 7

**Problema:** En `TaskActivity`, la función `cleanScreen` deja activado el modo de edición. Después del clear screen (que ocurre al presionar el botón X) se puede guardar una Task nueva con un id ya existente.

![Código: cleanScreen con isEditMode = true](img/img1_Error6.png)

![Captura: formulario con datos antes de limpiar](img/img2_Error6.png)

![Captura: formulario después de limpiar y guardar con id existente](img/img3_Error6.png)

![Captura: mensaje "Information was saved successfully"](img/img4_Error6.png)

**Solución:** Cambiar el valor de la variable `isEditMode` por `false` para que entre a validar si ya existe una tarea con ese id.

![Captura: validación al intentar guardar un id ya existente](img/img5_Error6.png)

![Captura: resultado final tras la corrección](img/img6_Error6.png)

---

## Error 8

**Problema:** Al editar o eliminar una tarea abierta desde `TaskListActivity`, al volver a la lista la fila sigue mostrando los datos anteriores. `TaskListActivity` crea el adapter una sola vez en `onCreate` y nunca lo refresca ni llama a `notifyDataSetChanged()`, aunque `TaskMemoryManager` sí actualiza la lista en memoria. Para que pase el error hay que abrir la tarea desde task list presionar la tarjeta, editar la info como title o estado y volver con la fecha del back del sistema, no con la flecha de la app.

![Captura: lista con la tarea antes de editarla](img/img1_Error8.png)

![Captura: TaskActivity con los datos modificados](img/img2_Error8.png)

![Captura: al volver con Back, la lista sigue mostrando los datos viejos](img/img3_Error8.png)

![Captura: al rotar la pantalla aparecen los datos nuevos](img/img4_Error8.png)

![Captura: tarea que se elimina](img/img5_Error8.png)

![Captura: tarea que se elimina, sigue apareciendo en la lista](img/img6_Error8.png)

![Captura: se entra nuevamente a la tarjeta y los datos no se encuntran](img/img7_Error8.png)

**Solución:** Guardar el adapter en una variable de la clase y, en  onResume , llamar a  notifyDataSetChanged()  para que la lista se actualice al volver de  TaskActivity .

![Código: refresco de la lista en onResume con notifyDataSetChanged](img/img8_Error8.png)
