# Configuration

_tekgenesis.admin_

## Overview

En la sección Configuración se podrán realizar cambio globales a todo el cluster. Todo cambio realizado se replicada en cada nodo del cluster.

###Http
El usuario puede activar/desactivar el Http-Header-Id para tracear las respuestas de las llamadas Http.

![http](/img/http.png)

###Logging
Mediante las siguiente propiedades se podrán cambiar, en forma global, la configuración de logs de la aplicación


|Propiedad |Descripcion|
|----|--------------------|
|Root Log Level|El nivel principal de log a aplicar globalmente|
|Log Filename|El nombre del archivo de salida|
|Max File Size|Tamaño máximo|
|Log Directory|Directorio|
|Max Days|Cantidad máxima de días a almacenar los logs|
|Console Ouput|Activa/Desactiva la salida estándar|
|Xml Output|Activa/Desactiva grabar los logs en xml|
|File Output|Activa/Desactiva la salida por archivo|
|Debug All|Seteo rápido para marcar todos los logs con nivel DEBUG|
|Debug Tekgenesis|Seteo rápido para marcar los logs de Sui Generiscon nivel DEBUG|
|Context Name|Nombre del contexto de logs|

###Gelf
Gelf es el protocolo utilizado para poder enviar la información de log a Graylog.


|Propiedad |Descripcion|
|----|--------------------|
|Server|El nombre/ip del servidor Gelf|
|Port|El puerto del servidor Gelf|
|Facility|El nombre del Facility con el cual los logs serán publicados|
|Output	|Activa/desactiva la salida estándar.|

###Loggers
Un logger es el nombre que se le da a la fuente de donde proviene un Log especifico. Generalmente un logger esta asociado a una clase en particular.
Usando el campo de búsqueda se podrá filtrar los diferentes loggers y activar/desactivar sus respectivos niveles de log.

![logging](/img/logging.png)
###Properties
Las propiedades pueden consultar y modificarse desde esta sección. Todos los cambios realizados serán propagados a todos los nodos del cluster. 
Cuando un valor es modificado, el mismo es guardado en la base de datos para que en el proximo reinicio del nodo el nuevo valor sea tomado en cuenta
![properties](/img/properties.png)


###MBeans

Desde la Consola de Administración el usuario puede acceder a todos los MBeans expuestos por la JVM.
![mbeans](/img/mbeans.png)
Cuando un MBean es seleccionado, se podrá acceder a sus atributos y operaciones.
![selectedmbeans](/img/selectedmBeans.png)

