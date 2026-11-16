### Error 2: Corrección del modo de edición en TaskActivity.kt

**Descripción del problema:**

Durante la revisión del archivo `TaskActivity.kt`, se identificó una inconsistencia en la variable `isEditMode` dentro de la función `cleanScreen()`.

La variable se estaba estableciendo con el valor `true` al limpiar el formulario, provocando que la aplicación pudiera continuar trabajando en modo edición, aunque la intención fuera registrar una tarea nueva.
![imagen2_evidenciaDebug](evidencias/Pantalla%205.png)
![imagen2_error](evidencias/Pantalla%205.png)
**Solución implementada:**

Se modificó la función `cleanScreen()` para establecer `isEditMode = false`, permitiendo desactivar el modo de edición al limpiar el formulario.

![2_solucion](evidencias/Pantalla%205.png)

**Resultado esperado:**

Al limpiar el formulario, la aplicación queda preparada para registrar una nueva tarea, evitando que se mantenga activo el estado de edición de una tarea anterior.

**Archivo modificado:** `TaskActivity.kt`

**Función modificada:** `cleanScreen()`