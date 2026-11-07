# Errores encontrados

**Estudiante:** María

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

![Error de formato de fecha](C:\Users\USUARIO\Pictures\Screenshots\Captura1.png)

#### Solución aplicada

Se corrigió el código reemplazando el formato fijo `"MM/dd/yyyy"` por el parámetro `pattern`, para que la función utilice el formato recibido.

**Código incorrecto:**

`val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())`

**Código corregido:**

`val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())`

**Evidencia 2: Código después de la corrección.**

![Código corregido](evidencias/error-01-codigo-corregido.png)

#### Revisión adicional del calendario

Durante el proceso de depuración también se inspeccionaron las variables `dayOfMonth`, `month` y `year` del selector de fechas, para analizar los valores utilizados por la aplicación.

**Evidencia 3: Inspección de variables del calendario.**

![Variables del calendario](C:\Users\USUARIO\Pictures\Screenshots\Captura2.png)

#### Resultado

Se corrigió la función de conversión para que utilice el patrón de fecha recibido como parámetro, evitando la interpretación incorrecta causada por el formato fijo.

**Verificación pendiente:** comprobar mediante una prueba funcional que la tarea se guarda correctamente con la fecha seleccionada y adjuntar la captura correspondiente.
