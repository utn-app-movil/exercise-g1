# TRABAJO EVALUATIVO

## Solución de la Fase 0
### Guía de Errores Intencionales de Código (Debugging - 45 min)

#### 1. Error de validación de fecha pasada
Encontré un error de validación ya que se puede elegir una fecha pasada:

![Fecha pasada permitida](imagenes/image001.png)

Para solucionar eso me dirigí a `TaskActivity.kt` y corregí:

![Corrección de fecha mínima](imagenes/image002.png)

Ya no permite seleccionar fechas anteriores:

![Fecha restringida correctamente](imagenes/image003.png)


#### 2. Error al hacer clic en Task List
Otro error sería que al darle clic a **Task List** me saca de la aplicación:

![Pantalla Principal de TaskManager](imagenes/image004.png)

**Solucionado:**
Para esto nos vamos a `AndroidManifest.xml`, y agregamos la actividad:

![Configuración en Manifest](imagenes/image005.png)

![Pantalla en blanco inicial](imagenes/image006.png)

Task list ya con datos de prueba:

![Task list con datos](imagenes/image007.png)


#### 3. Error al recrear una tarea con el mismo ID
Cuando creo una tarea con ID 1 (por ejemplo), la guarda y después la borro, e intento crear una nueva con ese mismo ID, me sale un mensaje de `NULL`:

Para eso vamos a `TaskActivity.kt` y realizamos esto:

![Modificación en cleanScreen](imagenes/image008.png)


#### 4. Error al duplicar la primera tarea en la lista
Al agregar una serie de tareas, en la lista de tareas se repite la primera tarea que se agregó, o sea se repite la misma tarea en la lista:

![Tareas repetidas](imagenes/image009.png)

Para solucionar vamos a `TaskListAdapter.kt`, revisamos este código:

![Código anterior de onBindViewHolder](imagenes/image010.png)

Y para solucionarlo lo corregimos por:

![Código corregido de onBindViewHolder](imagenes/image011.png)

![Lista de tareas corregida](imagenes/image012.png)

Solucionado.


#### 5. Error al rellenar los campos al seleccionar una tarea
En **Task List** logro ver dos tareas distintas. Al darle clic a una de estas para realizar una función específica se supone que debería de rellenar los campos con los valores y la data correspondiente de esta tarea, pero no es así. En las fotos logramos observar mejor el error:

Información de `t2`:

![Información detallada de t2](imagenes/image013.png)

Información en `TaskList`:

![TaskList con t1 y t2](imagenes/image014.png)

Al darle clic a `t2` me sale así (vacío o con datos incorrectos):

![Error al abrir t2](imagenes/image015.png)

Para solucionarlo vamos a realizar lo siguiente:  
Corregir el lugar donde se configura el evento de clic en tu adaptador de la lista, `TaskListActivity.kt`.  
El código tiene este bloque:

![Código de clic incorrecto](imagenes/image016.png)

Hay que modificarlo y cambiarlo por:

![Código de clic corregido con task.id](imagenes/image017.png)

AHORA SÍ, al darle clic en `TaskList` sí me completa los campos con la información de esa tarea:

![Campos rellenados correctamente con la tarea seleccionada](imagenes/image018.png)