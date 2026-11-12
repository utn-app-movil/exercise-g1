# Resolución de la Práctica de examen.
**Autores:** Brandon Conejo y Esmeralda Cordoba

## Fase 0: Debugging.
---
**Punto 1**

ERROR 1:
-La app crashea al abrirse el botón de ver la lista de tasks
-Esto se debia a que no estaba en el manifesto de android
-Lo arreglamos poniendolo en el manifesto de android

ERROR 2:
-La primera entrada se repetia 2 veces si habia más de 1 entrada
-Esto se debia a que, a la hora de pasar la posición del array, habia un -1. Lo que causaba que se repitiera la entrada
-Se arregló quitandole ese -1

ERROR 3:
-En algunas entradas de la lista de tareas no se traen los datos correctamente
-Esto debido a que se estaba enviando el titulo en vez del ID
-Se arregló haciendo que envie el valor correcto

ERROR 4: 
-La fecha aparece en el mismo formato no importa qué (el formato inferior de mm/dd/aaaa)
-La aplicación no estaba tomando el formato, lo estaba setteando rigidamente
-Lo arreglamos haciendo que use Pattern en vez de algo hardcodeado

ERROR 5:
En la función de OnDateSet que se encuentra en el TaskActivity.kt, se está tomando el mes de Noviembre con el valor 10 lo cual no es correcto porque debería ser 11.
Eso me llevó a pensar que lo que está pasando es que está iniciando en 0.
Consulté en Google y al parecer el ícono del Calendario y el DatePickerDialog (Que es la ventanita en la que se marca la fecha) manejan los meses de forma 0-11cuando debería ser inicializado en 1-12.

Cómo lo solucioné:
Dado que se está utilizando la fecha, en este apartado lo toma como si fuese de 1-12, entonces se le debe colocar un -1 para que entienda el comportamiento del DatePickerDialog.
Y al aplicar el getDateFormatString se le suma un 1 al mes para que al seleccionar una fecha en el calendario muestre los meses del 1 al 12 como corresponde.

ERROR 6:
En el TaskActivity se está exigiendo que las observaciones (txtObservation) y la persona asignada (txtAssignTo) no queden en blanco (isNotBlank()).
En la mayoría de sistemas u sitios, estos campos son opcionales, por lo que obligar a que estos campos se llenen y que alguien decida no hacerlo mostrará el mensaje "Datos incompletos".

Cómo lo solucioné: Simplemente eliminé a txtObservation y txtAssignTo de las restricciones.

ERROR 7:En TaskActivity.kt el estado de isEditMode ubicado en cleanScreen().
Justo después de guardar con éxito una task nueva, la pantalla se limpia pero isEditMode se queda en true, como resultado, la aplicación cree que se sigue editando, el campo del txtId queda bloqueado (enabled = false) y el botón de eliminar aparece en el menú superior cuando no debería (ya que es en una pantalla limpia para crear una nueva task).

Cómo lo solucioné: Simplemente se debe cambiar isEditMode a false.

ERROR 8: Falta de return.
Si la tarea ya existe, muestra el Toast de datos duplicados, pero no detiene la ejecución, por lo que continúa guardando la task de todos modos.

Cómo lo solucioné: Colocando el return justo antes del else y cerrando el Toast.

---
**Punto 2**

ERROR 9:
Debería mostrarse un dialog con una pregunta de verificación cuando se desea actualizar o eliminar en la pantalla de Task.