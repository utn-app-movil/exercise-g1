## Bug 01 — El formulario queda en modo edición después de limpiarlo

El primer bug encontrado está en el metodo `cleanScreen()` del archivo `TaskActivity.kt`, en la línea 193. Este metodo limpia los campos del formulario, pero la instruccion `isEditMode = true` deja activo el modo edicion. Esto interfiere con el siguiente guardado, porque el método `saveTask()` utiliza esa variable para decidir si debe crear o actualizar una tarea y para validar si el ID ya existe.

Para encontrarlo se utilizó el Debugger de Android Studio con un breakpoint en la linea 193. En la captura `bug1a.png` se muestra que la variable tiene el valor `false` antes de ejecutar la asignación. En la captura `bug1b.png` se muestra que, despues de ejecutar el metodo, su valor es `true`.

La corrección realizada fue cambiar `isEditMode = true` por `isEditMode = false`, para que el formulario vuelva al modo de creación al limpiarse.

Las dos capturas documentan el comportamiento del código original. Queda pendiente comprobar en el emulador el siguiente guardado y agregar una captura del funcionamiento con la corrección.

Hasta el momento solo encontre uno, aunque he notado más; como por ejemplo al darle al boton "task list" ejecuta una funcion equivocada, fallos en la validacion de fechas, el open activity de util tambien.