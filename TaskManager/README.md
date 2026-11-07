Apuntes para el readme
Kristel Ugalde
Yul Tapia
Primer error identificado:
El aplicativo deja poner caracteres de gran densidad y permite guardarlos (si esta vacio no lo permite)


Lo solucionamos poniendo un límite de caracteres y caracteres específicos, por ejemplo: números, caracteres especiales etc y se edita el TaskActivity


Segundo error
Las fechas estan en formato diferente, y la aplicación crashea cuando se pone un día mayor

Lo solucionamos editando el util.kt, lo que está sucediendo es que el formato de fechas no está funcionando, por lo que da nulo, entonces con la pulguita detectamos el error en util.


problema 3:

A la hora de listar la app se cae lo cúal no permite ejecutar esa parte, el error es porque la app abre la pantalla de tarea vacía, entonces al abrirla ella se cae.


Problema 4

El error que persistía básicamente era que antes solo cargaba dos elementos en la lista y uno no se cargaba, en el TaskListAdapter había un error que no dejaba que hubieran tres elementos, lo solucionamos y ahora sí deja.

Problema 5:
Cuando limpias o cancelas la pantalla de tareas, el formulario queda atrapado en "modo edición" (isEditMode = true) entonces al intententar guardar una tarea nueva solo actualiza más no guarda

La solución: Como solución hay que pasar de true a false, así la app reconoce que está creando un nuevo registro.








Problema 6
Notamos que al momento de buscar mediante un id por ejemplo el 1, el calendario para ver y cambiar la fecha aparece el calendario abre otra fecha por ejemplo este lo guardé en noviembre y sale octubre.

La solución que ejecutamos es que al poner -1 al apartado del mes se pone en formato android y que funcione de verdad


Problema 7
Como dice el enunciado, no se muestran los dialog al momento de guardar o eliminar, adjuntamos evidencia.

Como solución (queda pendiente)
