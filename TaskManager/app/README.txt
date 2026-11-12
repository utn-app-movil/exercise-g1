Error #1: Modo de edición incorrecto
Archivo: TaskActivity.kt
Problema: El método cleanScreen() establecía isEditMode = true después de limpiar los campos.
Solución: Cambiar el valor a false para permitir el registro de nuevas tareas.

Error #2: Error en la selección de fechas
Problema: La aplicación utilizaba los meses de DatePickerDialog sin convertirlos al formato convencional, lo que provocaba fechas incorrectas.
Solución: Se agregó + 1 al mes mostrado en onDateSet() y - 1 al mes obtenido de LocalDate cuando se carga una tarea existente.

Error #3: Envío incorrecto del identificador de la tarea
Archivo: TaskListActivity.kt
Problema: Al seleccionar una tarea desde el RecyclerView, se enviaba task.title en lugar de task.id, impidiendo encontrar correctamente la tarea seleccionada.
Solución: Se reemplazó task.title por task.id en el método onItemClick()

Error #4: Falta de confirmación al actualizar y eliminar tareas
Archivo: TaskActivity.kt
Problema: La aplicación ejecutaba directamente las operaciones de actualización y eliminación, sin solicitar confirmación al usuario.
Solución: Se implementaron dos métodos con AlertDialog.Builder para confirmar ambas operaciones antes de ejecutarlas.

Error #5: Posición incorrecta en el RecyclerView
Problema: El adaptador utilizaba position - 1 para obtener las tareas, provocando que se repitieran elementos y que algunos no aparecieran.
Solución: Se corrigió el método onBindViewHolder() para utilizar tasks[position].
Funcionalidad implementada: Se agregó búsqueda dinámica por título y filtrado por estado, permitiendo combinar ambos criterios.