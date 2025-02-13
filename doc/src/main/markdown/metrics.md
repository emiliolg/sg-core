
# Metrics on Graphite.

Sui Generis provides metric publishing on @inline(externalLinks.md#graphite).
Both out of the box and custom metrics can be published, bellow automatically published metrics are listed, and custom metrics explained.

## Automatically published metrics 

The following metrics  are automatically published:

- Handlers 
- Forms
- Tasks
- Emails
- JVM
- Http Request

## API for custom metrics 

All metrics are _autoclosable_.

Properly naming your metrics is critical to avoid conflicts, confusing data and potentially wrong interpretation later on.  

**Metrics are reported using the following schema**

```html
<hostname>.<namespace>.<instrumented section>.<target (noun)>.<action (past tense verb)>
```

Metrics are created through the use of tekgenesis.metric.core.MetricsFactory, check [Sui Generis API Documentation](javadoc/index.html), for further details.

### Meter
Simple meter support.

@inline-code(doc/samples/snippets/src/main/java/tekgenesis/snippets/metrics/MetricsSnippets.java#meter)


This metric will we exposed as **Hostname.MyNamespace.target.myMethod** with the corresponding metrics values after the _action_

### Timer
Simple Timer support.
This metric starts timing on start(), and stops on close (either auto close or close() invocation).

@inline-code(doc/samples/snippets/src/main/java/tekgenesis/snippets/metrics/MetricsSnippets.java#time)
or 
@inline-code(doc/samples/snippets/src/main/java/tekgenesis/snippets/metrics/MetricsSnippets.java#time2)


### Calls 
Call Metric is both a Timer and a Meter over successful and failed invocations based on the call result.
In order to mark the call as successful or failed, the mark method receives a boolean parameter. (True for success and False for failure). 


@inline-code(doc/samples/snippets/src/main/java/tekgenesis/snippets/metrics/MetricsSnippets.java#call)

## Configuration

By default metrics are disabled. In order to enable them the following properties must be set:  

| property | description |  available values | default | 
| --- | --- |  --- | --- | 
|metric.domain|  domain name for all the metrics | * | sg |
|metric.reporter| where the metrics will be published | NONE , GRAPHITE | NONE |
|graphite.host | graphite server hostname | * | localhost | 
|graphite.port | graphite server port | * | 2003 | 
|graphite.publishRate | publish rate in seconds | * | 60 | 


