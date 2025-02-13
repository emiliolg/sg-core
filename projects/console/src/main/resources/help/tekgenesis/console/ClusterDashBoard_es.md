# Dashboard

_tekgenesis.admin_

## Overview

La sección Dashboard permite ver  el estado general del Cluster nodo por nodo.
La pantalla principal muestra cada nodo y su estado actual.

Por cada nodo se puede observar:

- Uso de Heap Memory: El uso de memoria Heap de la JVM. La memoria Heap esta dividida en tres secciones: Eden, Survivor y Old Gen
- Uptime: El uptime del nodo
- Componentes: Componentes instalados y sus versiones

![DashBoard](/img/dashboard.png)

![TheradDump](/img/threaddump.png) Thread Dump: Permite descargar un Thread Dump del nodo. (Archivo de texto con un dump de todos los thread disponibles en la JVM y el estado actual de cada uno de ellos.)
![Restart](/img/restart.png) Reiniciar: Permite reiniciar el nodo

Cuando un nodo es seleccionado se puede acceder al detalle del mismo. En esta vista del nodo se podrá acceder a las opciones de Servicios, Configuración, Logging y Propiedades, pero teniendo en cuenta que cualquier modificación realizada  SOLO afecta al nodo que se haya seleccionado y no a el cluster completo.

![Summary](/img/dashSummary.png) 

## Servicios
La sección de Servicios permite al usuario hacer Start/Stop habilitar/deshabilitar los servicios disponibles en el nodo seleccionado. 

![Services](/img/dashServices.png) 


Un Servicio puede se le puede hacer Start/Stop , este estado es TEMPORAL, cuando el Nodo es reiniciado el servicio volverá a re-inicializarse.
Un Servicio puede habilitarse/deshabilitarse, este es un estado PERMANENTE, cuando el Nodo es reiniciado el servicio mantedrá su estado de habilitado/deshabilitado.

- ElasticSearchService: Es responsable de la comunicación con el servidor de  ElasticSearch.
- EmailService: Es responsable de el envio de emails al Servidor de Email.
- ImporterService: Es responsable de los Importers.
- JmxService: Es responsable de inicializar los JMX.
- MetricService: Es responsable de enviar las metricas.
- TaskService: Es responsable del monitoreo y ejecución de las Tareas.
- WebProxyService: Es responsable de inicializar la comunicación con el WebProxy contra el HA Proxy.
