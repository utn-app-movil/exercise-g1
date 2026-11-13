# Práctica en clases semana 8

**Profesor:** Ever Barahona Mendoza

**Estudiante**: Yordi Contreras Zamora

**Asignatura:** Desarrollo de Aplicaciones para Dispositivos Móviles

## Errores y correcciones del código TaskManager

### Error 1

Atraso en horario

Fecha atrasada un mes, el mes se digitaba en noviembre y aparecía número de mes de octubre.

![img1.png](app/img/img1.png)

Lo guarda en octubre, lo cual es incorrecto, se debe formatear en el mes que se indica.

**Solución**: Cambio en la función `OnDateSet` donde se incrementa uno más al mes y en `util`, en el método `parseStringToDateModern` donde se cambia el código hardcodeado y se pone el `pattern`:

Evidencias: En esta sección se le incluye un `+` al mes en `OnDateSet`

![img2.png](app/img/img2.png)

![img3.png](app/img/img3.png)

**Solución**:

![img4.png](app/img/img4.png)

![img5.png](app/img/img5.png)

---

### Error 2

No se muestra el tasklist en la app, debido a que el error está en el manifest que no llama la actividad.

![img6.png](app/img/img6.png)

**Solución**: se agrega una nueva actividad donde se incluye `.TaskListActivity`:

![img7.png](app/img/img7.png)

Evidencia de la lista funcionando:

![img8.png](app/img/img8.png)

---

### Error 3

Al guardar una tarea y buscarla, inicia en un mes adelantado.

![img9.png](app/img/img9.png)

![img10.png](app/img/img10.png)

**Solución**:

Añadir un `-1` a `dueMonth` en la función `searchTask` en `TaskActivity`:

![img11.png](app/img/img11.png)

---

### Error 4

Abrir una tarea desde la lista de tareas y se envía un title en vez del id de la tarea:

![img12.png](app/img/img12.png)

![img13.png](app/img/img13.png)

![img14.png](app/img/img14.png)

![img15.png](app/img/img15.png)

**Solución**:

En el value se quita el `task.title` y se sustituye por `id`.

![img16.png](app/img/img16.png)

---

### Error 5

Al guardar las tareas en la lista se mostraba el 1 con la información correcta y la segunda tarea mostraba los de la tarea 1.

![img17.png](app/img/img17.png)

![img18.png](app/img/img18.png)

**Solución**:

En este método se quita la condicional y el `vector(position-1)`:

![img19.png](app/img/img19.png)

Resultado, datos listados y lista de tarjetas bien formateadas y ordenadas.

![img20.png](app/img/img20.png)

![img21.png](app/img/img21.png)

![img22.png](app/img/img22.png)

---

### Error 6

La función de limpiar pantalla de TaskManager, su función `clearScreen` tiene el modo de edición activo: Lo que pasa después del clean screen de guardar una tarea, se puede almacenar una tarea nueva con un id existente.

![img23.png](app/img/img23.png)

**Solución**: cambiar el valor de `isEditMode` a verdadero para evitar la redundancia de id.

![img24.png](app/img/img24.png)

**Id 1 ya existente:**

![img25.png](app/img/img25.png)

![img26.png](app/img/img26.png)

---

### Error 7

`parseStringToDateModern` ignoraba el parámetro `pattern` y utilizaba un formato estático.

**Solución**: Se cambió el formato fijo de la fecha para que reciba `pattern`.

![img3.png](app/img/img3.png)

---

---
### Error 8

Al editar o eliminar una tarea desde `TaskListActivity`, al volver a la lista sigue mostrando los datos anteriores. `TaskListActivity` crea el adapter una sola vez en `onCreate` y nunca lo refresca ni llama a `notifyDataSetChanged()`, aunque `TaskMemoryManager` sí actualiza la lista en memoria. Para que pase el error hay que abrir la tarea desde la carta de la List, editar la información como estado y volver con la fecha del sistema, no con la flecha de la app.

![img8.png](app/img/imgError8_1.png)

- **Resultado / Evidencia:** Tras aplicar la corrección, se validó el flujo editando la tarjeta de **"Jose"** a **"Yordi"** y regresando con el botón *Back* del sistema; la interfaz refrescó el listado inmediatamente y mostró el nombre actualizado correctamente.

**Dato** de Jose original se cambiará por Yordi

![img4.png](app/img/imgError8_4.png)

![img4.png](app/img/imgError8_3.png)

Cambio a Yordi

![img5.png](app/img/imgError8_2.png)