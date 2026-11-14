Estoy empezando a vre errores
1er error:
Cuando abro pantalla y le doy el botón de TaskListActivity se cierra la aplicación y la pantalla.
![Error1](image.png)
Solución:
![alt text](image-1.png)
Agregamos una función dentro de la aplicación le agregamos función al boton TaskListActivity

2do error
En TaskListAdapter.kt hay una función que da error a la hora de correr porque esta mal creada ya que este código está en el Adapter de un RecyclerView. onBindViewHolder se llama por cada fila que se va a mostrar, y recibe la posición (position) de esa fila. Su trabajo es tomar el dato que corresponde a esa posición y pasárselo al ViewHolder con holder.bind(...) para que lo dibuje.
![alt text](image-2.png)
Solución
Le quitamos esa linea al código y lo unico que le agregamos a la linea es val item = tasks[position]

3er error
A la hora de guardar una tarea si en la fecha se puso un numero alto mayor a 12 a la hora de guardar la tarea devolverá un dato null
![alt text](image-3.png)
Solución:
Configurarlo para que use el parametro pattern recibido
cambiamos esto:
DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())
Por esto:
DateTimeFormatter.ofPattern(pattern, Locale.getDefault())

4to error
Cuando se intenta llenar una tarea y uno presiona el boton de cancelar que es la X aparece un boton de una papelera para eliminar la tarea, si embargo al presionarla ocurre un error y no se sabe que eliminar
![alt text](image-4.png)
Solución:
1.
isEditMode = false: Le indica a la aplicación que el formulario está listo para crear una tarea totalmente nueva, no para editar una vieja.
2.
txtId.isEnabled = true: Vuelve a habilitar la casilla del ID para que puedas escribirle un código o número al nuevo registro.
3.
invalidateOptionsMenu(): Le avisa a la barra superior (ActionBar) que redibuje los botones. Como isEditMode ahora es false, la app oculta automáticamente el botón de Eliminar (la papelera), ya que no puedes eliminar una tarea que todavía no existe.

5to error
Hay un bug en TaskActivity ya que en
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.crud_actions, menu)
        menuDelete = menu!!.findItem(R.id.mnuDelete)
        menuDelete.isVisible = isEditMode
        invalidateOptionsMenu()
        return true
    }

invalidateOptionsMenu() genera un bucle infinito
Solución:
Eliminar el invalidateOptionsMenu()

6to error
Realizar un cuadro de dialogo antes de realizar alguna acción sobre la tarea
Solución:
Se integró util.showDialogCondition al actualizar tareas en  TaskActivity.kt.


Agregar el archivo TaskStatsActivity, su XML ( activity_task_stats.xml), su registro en el Manifest y el botón de acceso desde MainActivity.
