# Errores encontrados en el Lab

Resolución de la práctica — Jeremy Delgado.




---

## Error 1

**Problema:** Fecha atrasada un mes. El mes se mostraba incorrectamente porque no se estaba sumando 1 al formatear la fecha. Además, `parseStringToDateModern` ignoraba el parámetro `pattern` y utilizaba un formato fijo.

![Captura: calendario mostrando el mes incorrecto](img/img1_Error1)

![Captura: fecha mostrada en el campo Due Date](img/img2_Error1.png)

**Solución:** Se cambió el formato fijo de la fecha para que reciba el parámetro `pattern`. En la función `onDateSet`, dentro de su función interna `getDateFormatString`, al parámetro `monthValue` se le añadió un `+ 1` para que indique el mes correcto.

![Código: parseStringToDateModern usando el parámetro pattern](img/img3_Error1.png)

![Código: onDateSet con monthValue = month + 1](img/img4_Error1.png)

---

## Error 2

**Problema:** `TaskListActivity` se cerraba porque no estaba declarado en el manifest.

**Solución:**

![Código: AndroidManifest.xml con TaskListActivity declarado](img/img1_Error2.png)

---

## Error 3

**Problema:** Al guardar una task y buscarla con el id, al modificar su campo fecha el calendario inicia en enero o un mes adelantado, y no en su mes guardado.

![Captura: calendario abierto en un mes distinto al guardado (diciembre)](img/img1_Error3.png)

![Captura: calendario abierto en otro mes incorrecto (enero)](img/img2_Error3.png)

**Solución:** Añadir un `-1` a la variable `dueMonth` en la función `searchTask` de la clase `TaskActivity`.

![Código: searchTask con dueMonth = task.dueDate.monthValue - 1](img/img3_Error3.png)

---

## Error 4

**Problema:** Al abrir una tarea desde `TaskListActivity` se enviaba el `title` en lugar del `id` de la Task.

![Código: onItemClick enviando task.title](img/img1_Error4.png)

![Captura: lista de tareas en TaskListActivity](img/img2_Error4.png)

![Captura: TaskActivity abierta sin los datos de la tarea](img/img3_Error4.png)

**Solución:** Enviar el `id`, para que `TaskManager` encuentre la Task y su data.

![Código: onItemClick enviando task.id](img/img4_Error4.png)

![Captura: TaskActivity mostrando los datos correctos de la tarea](img/img5_Error4.png)

---

## Error 5

**Problema:** Al guardar las tareas, en `TaskList` se mostraba la Task 1 con sus datos correctos, pero la segunda también mostraba los datos de la Task 1.

![Código: onBindViewHolder con el cálculo de posición original](img/error-5-codigo-original.png)

![Captura: datos de la Task 1](img/error-5-task-1.png)

![Captura: datos de la Task 2](img/error-5-task-2.png)

![Captura: lista mostrando datos repetidos de la Task 1](img/error-5-lista-repetida.png)

**Solución:** Mostrar los datos solo por la posición.

![Código: onBindViewHolder con val item = tasks[position]](img/error-5-codigo-solucion.png)

![Captura: lista mostrando cada tarea con sus datos correctos](img/error-5-lista-correcta.png)

---

## Error 6

**Problema:** En `TaskActivity`, la función `cleanScreen` deja activado el modo de edición. Después del clear screen (que ocurre al presionar el botón X) se puede guardar una Task nueva con un id ya existente.

![Código: cleanScreen con isEditMode = true](img/error-6-codigo-clean-screen.png)

![Captura: formulario con datos antes de limpiar](img/error-6-formulario-1.png)

![Captura: formulario después de limpiar y guardar con id existente](img/error-6-formulario-2.png)

![Captura: mensaje "Information was saved successfully"](img/error-6-mensaje-guardado.png)

**Solución:** Cambiar el valor de la variable `isEditMode` por `false` para que entre a validar si ya existe una tarea con ese id.

![Captura: validación al intentar guardar un id ya existente](img/error-6-validacion.png)

![Captura: resultado final tras la corrección](img/error-6-resultado.png)

---

## Error 7

**Problema:**

![Captura del error 7](img/error-7-problema.png)

**Solución:**

![Captura de la solución del error 7](img/error-7-solucion.png)

---

## Error 8

**Problema:**

![Captura del error 8](img/error-8-problema.png)

**Solución:**

![Captura de la solución del error 8](img/error-8-solucion.png)