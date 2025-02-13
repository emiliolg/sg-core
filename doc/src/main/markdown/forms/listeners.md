# Form Listeners

Similar to EntityListeners, you can add listeners programmatically.

## Available Events

Using form listener, you can listen to the following events:

- BEFORE_LOAD
- AFTER_LOAD
- BEFORE_CANCEL
- AFTER_CANCEL,
- BEFORE_CREATE
- AFTER_CREATE
- BEFORE_UPDATE
- AFTER_UPDATE
- BEFORE_PERSIST
- AFTER_PERSIST
- BEFORE_DELETE
- AFTER_DELETE
- BEFORE_DEPRECATE
- AFTER_DEPRECATE

## Adding a listener

```java
    addListener(FormListenerType.BEFORE_UPDATE, formInstance -> {
        formInstance.setSomeField("Field value set from listener"); 
    });
```