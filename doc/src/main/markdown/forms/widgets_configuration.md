# Configuring widgets

As mentioned earlier widgets can be configured to meet all the developer's needs. The first tier of configuration are all the available [Field options](forms.html#field-options). You have them all covered by widget on the [Widgets](widgets/widgets.html) page. There is also a second tier of configuration that some widgets have, that is available through Java code, that can be used from Form class. So, in this section, we are going to cover all the interfaces available for configuring widgets from Java.

# Widgets

The widgets that can have configuration through this mechanism are: *map*, *chart*, *dynamic*, *upload*, *subform*, *date_box*, *date_time_box*, *double_date_box* and *radio_group*. For all of them, the way to reach the configuration to edit it is the same. This is done by calling one of the methods that all Form Java classes have:

```java
/** Returns a typed configuration for a given field. */
@NotNull protected <T extends WidgetConfiguration> T configuration(@NotNull Fields field) { ... }
```

This method, as its comments says, returns a typed configuration based on the widget type of the field that is passed as an argument and for that widget also. All the available implementations of the WidgetConfiguration class are what developers use to configure them. Typically, configuration of widgets is done on the *load* method of the form. That is the right moment, when the form is loading, to configure widgets. Alternatively, configuration is done or gets modified when a condition is met, in an *on_change* / *on_click* method call.

### Map

For maps, the center's point, zoom level, overlay, a marker's size and a marker's color can be configured through the following interface.

@inline-code(runtime/web/src/main/java/tekgenesis/form/configuration/MapConfiguration.java)

### Chart

For charts, the series colors, hoverability (and which info the hover dialog has), the presence of legend, the visibility of the axis and how the labels are shown in X axis can be configured through the following interface.
@inline-code(runtime/web/src/main/java/tekgenesis/form/configuration/ChartConfiguration.java)

### Dynamic

For dynamics, can be configured through the following interface.
@inline-code(runtime/web/src/main/java/tekgenesis/form/configuration/DynamicConfiguration.java)

### Upload

For upload widgets, cropping and size constrains can be configured through the following interface.
@inline-code(runtime/web/src/main/java/tekgenesis/form/configuration/UploadConfiguration.java)

### Subform

For subforms, its visibility can be toggled through the following interface.

@inline-code(runtime/web/src/main/java/tekgenesis/form/configuration/SubformConfiguration.java)

### DateBox, DateTimeBox and DoubleDateBox

For date widgets, handy methods available in the following interface, let developers customise days disability in calendars.

@inline-code(runtime/web/src/main/java/tekgenesis/form/configuration/DateConfiguration.java)

### RadioGroup

For radio group widgets, each option's style class can be defined using the following interface.

@inline-code(runtime/web/src/main/java/tekgenesis/form/configuration/RadioGroupConfiguration.java)

