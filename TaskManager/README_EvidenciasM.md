# Errores encontrados

**Estudiante:** María Murillo Chaves

## Fase 0: Identificación y corrección de errores

### Error 1: Formato incorrecto de fecha

**Archivo:** `util.kt`  
**Método:** `parseStringToDateModern()`

#### Descripción del error

Al intentar guardar una tarea, se identificó un problema en la conversión de fechas. El valor recibido era `28/10/2026` y el patrón solicitado era `dd/MM/yyyy`, mientras que el formateador utilizaba `MM/dd/yyyy`.

Esto provocaba que la fecha no pudiera interpretarse correctamente, ya que el programa intentaba utilizar el número 28 como mes.

#### Identificación mediante Debug

Se ejecutó la aplicación en modo Debug y se colocó un breakpoint en la función `parseStringToDateModern()`.

Al inspeccionar las variables, se encontraron los siguientes valores:

- `dateString = "28/10/2026"`
- `pattern = "dd/MM/yyyy"`
- `formatter = "MM/dd/yyyy"`

Esto permitió identificar que el formato utilizado no coincidía con el formato recibido por la función.

**Evidencia 1: Identificación del error mediante Debug.**

![Error de formato de fecha](Evidencias%20Mar%C3%ADa/Captura%201%20-%20Error%201.png)

#### Solución aplicada

Se corrigió el código reemplazando el formato fijo `"MM/dd/yyyy"` por el parámetro `pattern`, para que la función utilice el formato recibido.

**Código incorrecto:**

`val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())`

**Código corregido:**

`val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())`

**Evidencia 2: Código después de la corrección.**

![Código corregido](Evidencias%20Mar%C3%ADa/Captura%202%20-%20Error%201.png)

#### Revisión adicional del calendario

Durante el proceso de depuración también se inspeccionaron las variables `dayOfMonth`, `month` y `year` del selector de fechas, para analizar los valores utilizados por la aplicación.

**Evidencia 3: Inspección de variables del calendario.**

![Variables del calendario](Evidencias%20Mar%C3%ADa/Captura%203%20-%20Error%201.png)

#### Resultado

Se corrigió la función de conversión para que utilice el patrón de fecha recibido como parámetro, evitando la interpretación incorrecta causada por el formato fijo.

**Verificación pendiente:** comprobar mediante una prueba funcional que la tarea se guarda correctamente con la fecha seleccionada y adjuntar la captura correspondiente.


### Error 2: ActivityNotFoundException al listar tareas

**Archivo:** `AndroidManifest.xml`  
**Componente afectado:** `TaskListActivity`

#### Descripción del error

Al seleccionar la opción para listar las tareas, la aplicación se cerraba inesperadamente y Android Studio mostraba una excepción en Logcat. El problema ocurría porque la actividad `TaskListActivity` no estaba registrada en el archivo `AndroidManifest.xml`.

#### Identificación mediante Debug y Logcat

Se ejecutó TaskManager y se intentó abrir la pantalla de listado de tareas. En Logcat se identificó la excepción:

```text
android.content.ActivityNotFoundException:
Unable to find explicit activity class
{cr.ac.utn.taskmanager/cr.ac.utn.taskmanager.TaskListActivity}
```

Este mensaje permitió comprobar que Android no podía localizar la actividad que la aplicación intentaba abrir.

**Evidencia 1: Excepción mostrada en Logcat antes de corregir el Manifest.**

![ActivityNotFoundException en Logcat](Evidencias%20Mar%C3%ADa/Captura%201%20-%20Error%202.png)

#### Solución aplicada

Se agregó la declaración de `TaskListActivity` dentro de la etiqueta `<application>` de `AndroidManifest.xml`, para que Android pudiera reconocerla al iniciar la pantalla mediante un `Intent`.

**Código agregado:**

```xml
<activity
    android:name=".TaskListActivity"
    android:exported="false"
    android:windowSoftInputMode="adjustResize">
    <meta-data
        android:name="android.support.PARENT_ACTIVITY"
        android:value=".MainActivity" />
</activity>
```

**Evidencia 2: Declaración de `TaskListActivity` agregada al Manifest.**

![TaskListActivity registrada en AndroidManifest](Evidencias%20Mar%C3%ADa/Captura%202%20-%20Error%202.png)

#### Resultado

Se identificó y corrigió la falta de registro de la actividad, eliminando la causa del error `ActivityNotFoundException` observado al intentar acceder al listado.


### Error 3: El listado muestra tareas incorrectas o repetidas

**Archivo:** `TaskListAdapter.kt`  
**Método:** `onBindViewHolder()`

#### Descripción del error

Al visualizar varias tareas en él `RecyclerView`, el adaptador tomaba el elemento de la posición anterior cuando la posición actual era mayor que cero. Como consecuencia, una misma tarea se mostraba repetida y las siguientes no coincidían con la posición que les correspondía.

#### Identificación mediante Debug

Se colocó un breakpoint en el método `onBindViewHolder()` y se inspeccionaron las variables `position` e `item`.

En la versión incorrecta, al evaluar `position = 1`, el objeto `item` continuaba mostrando la tarea con `id = "1"` y título `"Tarea 1"`, porque el código utilizaba `tasks[position - 1]`.

**Evidencia 1: Primera observación de la asignación incorrecta.**

![Adaptador con índice incorrecto](Evidencias%20Mar%C3%ADa/Captura%201%20-%20Error%203.png)

**Evidencia 2: Debugger mostrando `position = 1` pero la tarea anterior en `item`.**

![Posición uno con la primera tarea](Evidencias%20Mar%C3%ADa/Captura%202%20-%20Error%203.png)

#### Solución aplicada

Se eliminó la resta de una posición y se utilizó directamente el índice que recibe `onBindViewHolder()`.

**Código incorrecto:**

```kotlin
val item = if (position > 0) tasks[position - 1] else tasks[position]
```

**Código corregido:**

```kotlin
val item = tasks[position]
```

**Evidencia 3: Código corregido y revisión de la primera posición.**

![Acceso directo a la tarea en la posición actual](Evidencias%20Mar%C3%ADa/Captura%203%20-%20Error%203.png)

**Evidencia 4: Debugger mostrando una tarea distinta al llegar a `position = 1`.**

![Segunda posición muestra la tarea correspondiente](Evidencias%20Mar%C3%ADa/Captura%204%20-%20Error%203.png)

#### Resultado

Después de la corrección, el adaptador utiliza `tasks[position]` para asociar cada posición del listado con la tarea correspondiente. En la evidencia posterior, `position = 1` muestra la tarea con `id = "2"` y título `"TAREA DE WEB"`, en lugar de repetir la primera tarea.

### Error 4: Se enviaba el título en lugar del identificador de la tarea

**Archivo:** `TaskListActivity.kt`  
**Método:** `onItemClick()`

#### Descripción del error

Al tocar una tarea del listado para abrirla, el método `onItemClick()` enviaba `task.title` mediante `EXTRA_MESSAGE_ID`. Sin embargo, la pantalla `TaskActivity` utiliza ese parámetro para buscar una tarea por su identificador (`id`), no por su título.

Aunque el título era correcto como dato de la tarea, no era el valor adecuado para realizar la búsqueda.

#### Identificación mediante Debug

Se colocó un breakpoint en la llamada a `util.openActivity()` y se inspeccionó el objeto `task`.

Las variables mostraban:

- `task.id = "1"`
- `task.title = "TAREA DE MOVILES"`

El código incorrecto enviaba el título `"TAREA DE MOVILES"` como valor de `EXTRA_MESSAGE_ID`, cuando debía enviar el identificador `"1"`.

**Evidencia 1: Debugger y código que envía `task.title`.**

![Envío incorrecto del título](Evidencias%20Mar%C3%ADa/Captura%201%20-%20Error%204.png)

#### Solución aplicada

Se reemplazó `task.title` por `task.id` en la llamada a `util.openActivity()`.

**Código incorrecto:**

```kotlin
util.openActivity(this,
    TaskActivity::class.java,
    EXTRA_MESSAGE_ID, task.title)
```

**Código corregido:**

```kotlin
util.openActivity(this,
    TaskActivity::class.java,
    EXTRA_MESSAGE_ID, task.id)
```

**Evidencia 2: Debugger y código corregido que envía `task.id`.**

![Envío correcto del identificador](Evidencias%20Mar%C3%ADa/Captura%202%20-%20Error%204.png)

#### Resultado

La llamada a `TaskActivity` ahora recibe el ID de la tarea seleccionada. El Debugger permitió observar que los valores del objeto permanecieron iguales, pero cambió correctamente el argumento enviado mediante `EXTRA_MESSAGE_ID`.
