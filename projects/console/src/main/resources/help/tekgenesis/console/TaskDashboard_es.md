# Tareas

_tekgenesis.admin_


Muestra el estado actual de todas las Tareas automaticas definidas.

Las tareas son la herramienta que permite ejecutar operaciones asincrónicas y periódicas.

![Task Sample](/img/tasks.png)

Se pueden visualizar las tareas en sus tres estados principales.

- Ejecutando: Cuando la tarea esta corriendo.
- Agendada: Cuando la tarea tiene un expresión cron asignada pero todavia no es tiempo de que se ejecute.
- Inactiva: Son todas las tareas que no tiene una expresión cron asignada o estan suspendidas.

###Ejecutando
 
![Tarea Ejecutando](/img/tasksExecuting.png)

###Inactivas
 
![Tarea Inactiva](/img/tasksInactive.png)

###Agendada
 
![Tarea Agendada](/img/tasksScheduled.png)

Haciendo click en el icono ![icon de tarea](/img/tasksIcon.png) el usuario puede acceder a las opciones de las tareas.

- Ejecutar Ahora: Ejecuta la tarea inmediatamente, a menos que la tarea ya se encuentre corriendo.
- Suspender Cron: Suspender la tarea. 
- Resumir Cron: Elimina la marca de tarea suspendida
- Editar Data: Permite editar el dato de la tarea.
- Agendar: Configura un nuevo cron expresión
- Para Cron: Remueve el cron expresión.

**Reglas de Expresiones Cron**
Las expresiones Cron son similares a las de unix.

``` 
┌───────────── segundos (0 - 59)
│ ┌───────────── minutos (0 - 59)
│ │ ┌───────────── horas (0 - 23)
│ │ │ ┌───────────── día del mes (1 - 31)
│ │ │ │ ┌───────────── mes (1 - 12)
│ │ │ │ │ ┌───────────── Día de la semana (SUN-SAT)
│ │ │ │ │ |
│ │ │ │ │ |
* * * * * *

```