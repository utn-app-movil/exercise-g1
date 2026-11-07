Error 1: No se podía guardar una tarea
-Al presionar Guardar aparecía un mensaje “null” y la tarea no se guardaba.
-Puse un breakpoint en saveTask() y vi en el debugger que la variable dueDate (la fecha de vencimiento) quedaba en null.

Solucion 1: cambié el formato a "dd/MM/yyyy" para que coincida con cómo se muestra la fecha.

Error 2: La fecha se mostraba con un mes menos
-Puse un breakpoint en onDateSet(). Al elegir marzo, el debugger mostró month = 2.
Solución 2: le sumé 1 al mes al mostrarlo: month + 1.

Error 3: después de guardar o eliminar una tarea, el formulario se limpiaba, pero el botón eliminar seguía visible.
Solucion 3:
-Puse un breakpoint en cleanScreen() y el debugger mostró isEditMode: true con el formulario vacío. Cambié a isEditMode = false.


Error 4: ActivityNotFoundException
Problema:Al presionar "Listar tareas", la aplicación
se cerraba porque TaskListActivity no estaba registrada
en AndroidManifest.xml.
Solución 4: 
-Se agregó TaskListActivity dentro de
la etiqueta application del AndroidManifest.xml.
evidencia de que si funciona

Error 5 
-Identificación del error 
Se colocó un breakpoint en el método onBindViewHolder de TaskListAdapter.kt para revisar la variable position. Se identificó que el código utilizaba tasks[position - 1] cuando la posición era mayor que cero, provocando que se mostrara la tarea anterior en lugar de la correspondiente.
Solucion 5 
Se corrigió la línea del código por val item = tasks[position]. Posteriormente, se verificó mediante el Debugger que la variable position tenía el valor 1 y la colección contenía dos tareas, comprobando que ahora se selecciona el elemento de la posición correcta.
