# Cache

_tekgenesis.admin_

## Overview

En la sección Cache el usuario podrá ver cuantos Caches están configurados y cuantos elementos posee el cache en cada nodo.
Hay dos tipos de Caches

- Cache de Entidad 
Estos caches se encuentran vinculados a una entidad específica.

- Cache de Usuarios
Estos caches son creados por los desarrolladores programaticamente.

![Database](/img/cache.png)

Mediante el campo de busqueda se podrá filtrar la tabla.
La tabla de caches provee la siguiente información
**Nombre de cache**
El nombre con el que se hace referencia el cache o el nombre de la entidad a la cual se encuentra vinculado.
**Modo** 
El modo de un cache indica la forma en que son replicados los cambios de los valores del mismo a otros nodos. Siendo los valores disponibles:

- Invalidation Async
No hay replicación, pero si invalidación de datos asincrónicamente.

- Local
El cache es local y on invalidación remota a otros nodos

- Invalidation Sync
No hay replicación , pero si invalidación de datos sincrónica.

Mediante el botón ![clear](/img/delete.png) se puede forzar la limpieza del cache.