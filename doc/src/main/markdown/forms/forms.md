# Forms

In a Sui Generis environment, where entities represent the data model stored in database, forms are used to present that data to the user. Such data can come from entities stored in the database or from other available sources. Forms can be bounded to an entity, and as we will see later, the framework takes advantage of this.

As entities have attributes, forms are defined using widgets. Widgets are defined by an id, a type and a widget type.
They can be bound to an entity's attribute, in which case the type is inhered. If no widget type is defined or no type is defined it is inferred from the other.
The value of these widgets will in all of these cases be a Sui Generis supported type (see [Types](../language/basicTypes.html#types)).
# Example
For the entity:
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/Person.mm#person)
The form:
@inline-code(doc/samples/snippets/src/main/mm/tekgenesis/snippets/Person.mm#personForm)

Forms are the standard interactive way to read, create, update, and delete entities. They can also be used without been bound to an entity, as a program entry point, or as a service.

As with entities, the metamodel language allows specifying model options when defining a form. 

##Form Binding
In the above example, a binding between the *PersonForm* and *Person* entity is specified. As mentioned before, forms come in different flavors: binding an entity, with default implementation, with specified primary key, and listing entities. More on this will be covered later on Form Flavors.

The same way entities are composed by attributes of different types, forms are composed by widgets. In the above example we found a header with a nested message widget, an horizontal widget with nested *firstName* and *lastName* bound attribute widgets, and widgets for bound attributes *ssn*, *birthday*, and *gender*. Finally, a footer widget containing buttons of type save, cancel, and delete.

# Widgets

Every form is composed by a set of widgets. All widgets have an id, a widget type and/or a type. If a widget type is not explicitly declared, is inferred from type. Widgets have several field options to customize, extend, limit, transform, decorate, or style the resulting form user experience.
@inline-code(doc/samples/doc-project/src/main/mm/tekgenesis/doc/Samples.mm#summary)

Widget with id summary, label "Summary", type String, and widget type text_field inferred from type.

## Widget Types

Sui Generis offers a wide variety of widget types to be used on form definitions. Most commonly used widget types include *text_field*, *button*, *combo_box*, *display*, *suggest_box*, *internal* and *table*.

Internal widgets have no visual representation on the form user interface. These widgets are convenient for ids, codes, flags, and other internal form data that is not intended to be shown to the final user.


Other widget types include [anchor](widgets/widgets.html#anchor), [breadcrumb](widgets/widgets.html#breadcrumb), [button](widgets/widgets.html#button), [check_box](widgets/widgets.html#checkbox), [check_box_group](widgets/widgets.html#checkboxgroup),  [color_picker](widgets/widgets.html#colorpicker), [combo_box](widgets/widgets.html#combobox), [combo_date_box](widgets/widgets.html#combodatebox), [date_box](widgets/widgets.html#datebox), [date_picker](widgets/widgets.html#datepicker), [date_time_box](widgets/widgets.html#datetimebox), [dialog](widgets/widgets.html#dialog), [display](widgets/widgets.html#display), [double_date_box](widgets/widgets.html#doubledatebox), drag_list, [dropdown](widgets/widgets.html#dropdown), [dynamic](widgets/widgets.html#dynamic),  [footer](widgets/widgets.html#footer), [gallery](widgets/widgets.html#gallery), [header](widgets/widgets.html#header), [horizontal](widgets/widgets.html#horizontal), [image](widgets/widgets.html#image), [internal](widgets/widgets.html#internal), [label](widgets/widgets.html#label), [list_box](widgets/widgets.html#listbox), [mail_field](widgets/widgets.html#mailfield) , [message](widgets/widgets.html#message), [password_field](widgets/widgets.html#passwordfield), [popover](widgets/widgets.html#popover),[PickList](widgets/widgets.html#picklist), [progress](widgets/widgets.html#progress), [radio_group](widgets/widgets.html#radiogroup), range, range_value, [rich_text_area](widgets/widgets.html#richtextarea), [search_box](widgets/widgets.html#searchbox), [showcase](widgets/widgets.html#showcase), [suggest_box](widgets/widgets.html#suggestbox), [tags](widgets/widgets.html#tags), [tags_combo_box](widgets/widgets.html#tagscombobox), [tags_suggest_box](widgets/widgets.html#tagssuggestbox), [text_area](widgets/widgets.html#textarea), [text_field](widgets/widgets.html#textfield), [time_picker](widgets/widgets.html#timepicker), [toggle_button](widgets/widgets.html#togglebutton), [upload](widgets/widgets.html#upload), [vertical](widgets/widgets.html#vertical) and [video](widgets/widgets.html#video) .

Multiple widgets such as [table](tables/tables.html), [chart](other_multiples/other_multiples.html#chart), [section](other_multiples/other_multiples.html#section), [map](other_multiples/other_multiples.html#maps), and scroll_section are described later.
Other such as subform and tabs, tree_view.

## Widget Type Inference

Widget type inference refers to the automatic widget type deduction in metamodel language. Specifying the attribute type for a widget, a widget type is automatically inferred.

|Type|Inferred widget type|
|----|--------------------|
|String|*text_field*|
|String(144)|*text_area* (for Strings of length 40 or above)|
|Int|*text_field* (numeric, int format)|
|Real|*text_field* (numeric, real format)|
|Decimal(10,2)|*text_field* (numeric, decimal format matching scale)|
|DateTime|*date_time_box*|
|Date|*date_box*|
|Enum|*combo_box*|
|Entity|suggest_box|
|Entity*|*table* (for bound multiple attributes)|
|Resource|*upload*|

## Widget Binding

When a form is bound to an entity, widgets can be bound to any entity attribute. Binding a widget to an attribute comes with great benefits: id, label, and type are all inherited from the binding, and code for model to view, view to model is automatically generated. More on this is covered in the [Generated Code](#generated-code) section.
From the above form example:

```
form PersonForm "Person" : Person
{
...
	firstName; // id, label, and type are inherited from attribute. Widget type text_field is inferred from inherited String type.
	lastName; // id, label, and type are inherited from attribute. Widget type text_field is inferred from inherited String type.	
...
	ssn, display; // id, label, and type are inherited from attribute. Inferred widget type text_field is overridden by specified display.
	birth "Date of birth" : birthday, date_picker; // id, label, and widget type are specified. Inferred widget type date_box is overridden by specified date_picker.
	gender "Gender" : sex; // id and label are specified. Widget type combo_box is inferred from inherited Sex enum type.
...
}
```

When a widget is Bound to an attribute, the id, label and type are inherited. Since widget type can be inferred from type, declaration becomes greatly simplified.

# Model Options

These options are defined at form level, where the formId and label are specified.


|Option|Usage|Description|
|------|-----|-----------|
|**primary_key** |primary_key *aField, another*|Tells which fields are part of form's primary key. Defaults to entity's primary key when bounded to an entity if not specified. If not bounded, part of form's calling url is passed here.|
|**on_display** |on_display *displayMethod* |Invoke *displayMethod* when form is displayed. Called every time form is displayed.|
|**on_schedule** |on_schedule *scheduleMethod 5* |Invoke scheduleMethod repeatedly, with the defined interval in seconds upon finish loading and displaying a form.|
|**on_route** |on_route */someRoute* |Path where the form is going to be served. More on this later on ... (Link to Routing?).|
|**permissions** |permissions *permission1, permission2* |Form's specific permissions. More on this later on ... (Link to Authorization? Forbidden?).|
|**parameters** |parameters *aField, another* |Fields that will act as forms's parameters when invoking a Form as a Service. More on this later on ... (Link to Form as a Service?).|
|**handler** | handler HandlerClass |Allows overriding for the default form handler.|
|**project** | project ProjectName |Defines the project name for this form.|
|**unrestricted** | unrestricted | Makes this form to be always accesible, no matter which permissions the user has.|

## Deprecated model options
|Option|Usage|Description|Replacement|
|------|-----|-----------|-----------|
|**on_load** |on_load *loadMethod* |Invoke *loadMethod* when form is loaded. Called only once per form instance.|Override the load() method|
|**on_cancel** |on_cancel *cancelMethod* |Invoke *cancelMethod* when form is canceled.|Override the cancel() method|

# Generated Code
As mentioned in the [Metamodel Language Section](../language/metamodels.html#code-generation), for each Form a base class and a user class are generated.
The user class will be named as the Form, and the base class will have the same name with the "base" suffix.
If tables or other multiple widgets are used, a base and a user class  are also created as inner classes of the top-level base and user classes respectively.  

## Base Class
Base class includes all generated getter and setter methods. These methods will be named as get or set widget id (if widget has no id, setter and getter will not be genereated.). Setter methods return the model to enforce a fluent API.
 
Some of the methods generated in the base class are the following:

|Method|Usage|
|------|-----|
|**addListener** **removeListener**| To register and remove form listeners. (See [Listeners](listeners.md))|
|**isDefined (Field)**| Returns true if the field value is not null.|
|**reset(Field..)**| Resets the specified fields.|
|**focus(Field)**| Gives focus to the specified field.| 
|**message(Field)**| to allow marking a field with a message.|
|**label(Field)**| to obtain the internationalized label for the specified field.| 
 
Also **populate** method is always created, but if form is not entity based, the method will be final, not allowing the user to override it, and throwing and exception. Populate methods is invoked when populating a form instance

When the form is based on an entity delete, update, create methods are generated. 
**Find** method will also be generated, if base entity has no primary key, it will be generated as abstract, forcing the user class to implement. 


## User Class
User class should include all the methods invoked from on_click, on_change and other field options.
Furthermore, the user class can include overriding of inherited methods, to change behavior. 
 

### Lifecycle interaction methods
To interact in the forms lifecycle the following methods can be overridden. This methods are not generated they are from FormInstance class.
For full method listing See [Sui Generis API Documentation](../javadoc/index.html) for base class tekgenesis.form.FormInstance. 

|Method|Usage|
|------|-----|
|public void load() |*loadMethod* is invoked when form is loaded. Called only once per form instance.|
|public Action cancel()| *cancelMethod* is invoked when form is canceled.|
see [lifecycle](lifecycle.html) for further information on these methods.

# Field Options

Widgets in forms have a different type options.
First the more common options are shown, then more particular ones and to which widgets apply.

## Options for value widgets

Value widgets are the ones that can gain a value. Some examples are text_field, date_box, suggest_box, tags.
These are all the options available for value widgets.

|Option|Usage|Description|
|------|-----|-----------|
|**default** |default *value* |Specifies the default value of the widget. This value will be set and displayed initially and users can modify it freely|
|**is** |is *value* |Specifies which **is** the value of the widget. The value for this widget cannot be modified later by the user. Useful when a widget's value depends on other widget's value. @todo (see calculated example )|
|**disable** |disable when *condition* |Disables the widget when a condition is satisfied. Disabled widgets are greyed out, their values cannot be modified, and all behavior is deactivated.|
|**hide** |hide when *condition* |Hides the widget when a condition is satisfied. Hidden widgets retain their values in the model.|
|**reset** |disable/hide when *condition*, reset |Reset works in conjunction with disable and hide options. When the condition its satisfied, besides hiding or disabling, the widget is reset to its initial state. If reset is applied to a Group widget, it will automatically reset all its internal widgets as well.|
|**depends_on** |depends_on *aField* |Defines dependant/dependee relations between widgets. In that way, if a widget changes its value, all the the dependants widget are reset to their initial state. @todo (see sample depends_on)|
|**check** |check *condition* : [inline] [warning] "message" | Checks for a condition and displays a message (error by default) in the widget. It appears as a balloon over the widget when the widget gains focus, use inline modifier if you want the tooltip to be placed near the widget. Forms that have errors cannot be submitted.|
|**skip_tab** |skip_tab |Widgets with this option will not gain focus when tabbing accross the page with the TAB key.|
|**placeholder** |placeholder *"placeholder text"* |The text defined by this option will be used as a placeholder text in the widget. This text is not set as value for the widget, it is shown as a visual hint. It can be an expression.|
|**on_change** |on_change *changedMethod* |Invoke method *changeMethod* when the widget's value changes. Fired when the value changes from the widget and from the model using the setter method.|
|**on_ui_change** |on_ui_change *uiChangedMethod* |Invoke method *uiChangeMethod* when the widget's value changes. Fired only when value changes from the widget (UI).|
|**on_blur** |on_blur *bluredMethod* |Invoke *bluredMethod* when widget looses focus.|
|**multiple** | multiple |Mutates a value widget to a multi valued one. Accessors will receive and return a collection of the specified type.|
|**abstract_invocation** |delegates invocation on containing form|Abstract invocation works in conjunction with on_change, on_ui_change, on_blur, and on_click options. Action performed on widget is delegated to containing form (that implements all subforms abstract invocations) or ignored.|

## Field Options for Non-Internal Widgets.

All widgets but internal ones have these field options.

|Option|Usage|Description|
|------|-----|-----------|
|**col** |col *number* |This field option allows setting the column count for this component modifying the bootstrap column class used (see @inline(externalLinks.md#bootstrapgrid)).|
|**label_expression** |label_expression *expression* |This field option allows redefining the default label, using an expression that evaluates to String.|
|**style** |style *"some-style"* |Sets the style class to the given String or to the expression's evaluation result. This class applies to the label / widget set. (For styles see [Styling a Form](styling/styling_a_form.html))|
|**content_style** |content_style *"other-style"* |Sets the content style class to the given String or to the expression's evaluation result. This class applies to the content, typically the widget. (For styles see [Styling a Form](styling/styling_a_form.html))|
|**inline_style** |inline_style *"width: 100px"* |Sets an inline style to the given String or to the expression's evaluation result. This raw css applies to the content, typically the widget.|
|**required** |required |All widget in Sui Generis are required by default.|
|**optional** |optional / optional when *condition* |Use it to override requirement default behaviour. If a condition is specified, only when that condition is satisfied the widget will be optional.|
|**hint** |hint *"hint text"* |Sets the widget's hint text to the given String or the expression's evaluation result. Hint texts appears below the widget in a small font size.|
|**tooltip** |tooltip *"tooltip text"* |Sets the widget's tooltip text to the given String or the expression's evaluation result. Tooltip texts adds an information icon by the component and shows a balloon over the widget over it when hovered. |
|**shortcut** |shortcut *ctrl+a* |Sets a global shortcut to perform widgets action (buttons) or to gain focus (other widgets).|
|**icon** |icon *"icon-check"* |Sets an icon over one of the predefined ones. Provides autocompletion over them.(see [icons](styling/styling_a_form.html#icons))|
|**icon_expr** |icon_expression *expression* |Sets an icon to the expression's evaluation result. Use this for dynamic icons, for fixed icon, use *icon* instead.(see [icons](styling/styling_a_form.html#icons))|
|**affix** |affix | @todo ???????????????????????????????????????????? define affix|
|**align** |align center|Overrides default widget alignment. Possible values: center, left or right. @todo see samples for align|
|**expand** |expand|Widget expands to the expected length.@todo find cases && samples|
|**col** |col 3|Sets how many cols this widget occupies. (see [styles](styling/styling_a_form.html#styles-and-themes))|
|**label_col** |label_col 3|Sets how many cols this widget's label occupies.(see [styles](styling/styling_a_form.html#styles-and-themes))|
|**offset_col** |offset_col 3|Produces an offset of the specified amount of columns.(see [styles](styling/styling_a_form.html#styles-and-themes))|
|**no_label** |no_label|Does not render the lable assosiated with the widget.|
|**top_label** |top_label|Renders the label on top of the widget instead of by it.|

## Other

|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**mask** |mask *percent* |text_field, display, combo_box, list_box, suggest_box, radio_group|Masks are used to format the value according to the user interest. Predefined masks are: currency, percent, unit, scientific, decimal, uppercase, lowercase, time_ago, identifier, file_size, number_only|
|**custom_mask** |custom_mask *"AAA-###"* or custom_mask *("AAA-###","AA-###-AA")* |text_field, display, combo_box, list_box, suggest_box, radio_group|Applies the mask to the field filtering and formatting the entered text. 'A' is used for the mask to accepts letters, 'a' for lowercase. '#' is used to accept numbers. 'X' if the accepted character can be either a letter or a number and 'x' if that letter is wanted lowercase. |
|**icon_selected** |icon_selected *"icon-check-selected"*|popover, toggle_button|Sets an icon for when the widget is selected.|
|**change_delay** |change_delay *500*|suggest_box, tags_suggest_box, search_box, text_field|Time in ms to wait before calling an on_suggest or on_change method. Used when trying to reduce the quantity of times the method is called, due to it excecution time or any other reason. Default is 50ms.|
|**change_threshold** |change_threshold *3*|suggest_box, tags_suggest_box, search_box, text_field|Specifies how many characters should be typed before retrieving suggestions or calling on_change method. Default is 2 in suggest_box.|
|**length** |text_field(10) / text_area(10) / display(10) / suggest_box(10)|text_field, text_area, display, suggest_box|Argument to define the length in characters for the widget. If not defined the length will be inferred from the type's length.|
|**rows** |table(10) / text_area(10) / list_box(10)|table, text_area, list_box|Argument for tables (to specify how many rows per page are visible), text_areas (to specify how many lines the area will have) and list_boxes (to specify how many rows the list will have till scroll).|
|**on_click** |on_click *clickMethod*|button, toggle_button, table|Invoke *clickMethod* when the user clicks the widget. In tables, the click is fired when the user clicks a row.|
|**link** |link *expression*|display, label, suggest_box|Generates a link on the widget with the given String or the result of expression's evaluation.|
|**link_form** |link_form *formId*|display, label, suggest_box|Generates a link to the given form id. On suggest_box widgets loads the form with the selected instance.|
|**link_pk** |link_pk *pk*|display, label, suggest_box|Used in conjuction with the previous one to specify a pk to load with the specified link.|
|**target_blank** |target_blank|display, label, suggest_box|Used with any of the link field options. Used to load the link in a new tab instead of the current one.|
|**filtering** |filtering *id*|table, section, scroll_section, dynamic?|Used to define which table the current section filters.|
|**synchronous** |synchronous|label, button, toggle_button, display|Marks widget as synchronous. All synchronous widgets will be disabled while processing takes part in the server side.|
|**text_length** |text_length *expression*|message, display|Sets length to the result of the expressions evaluation. Expression must evaluate to Integer.|
|**confirm** |confirm *expression*|button, label|Uses String or result of string expression's evaluation to display a message to the user before firing an action. If String is empty no confirmation is shown to the user.|
|**toggle_button_type** |toggle_button( *toggle_button_type*)|toggle_button_type|Argument for toggle button widgets. Supported toggle button types are *deprecate* or *custom*, default value is *custom*|
|**collapsible** |collapsible|groups|Makes groups collapsible when present.|
|**scrollable** |scrollable|section|Section will be horizontally scrollable.|
|**split** |split|dropdown|If a dropdown has split specified, the first child widget will be displayed beside the icon that opens the dropdown as the main action (see [dropDown](widgets/widgets.html#dropdown)|
|**tab_type** |tabs( *tab_type*)|tabs|Argument for tabs to specify the tabs location. Supported values are *pill*, *top*, *left*, *bottom*, *right*.|
|**flow** |flow|input_group|Applies flow layout to input_group.|
|**step** |step 15|time_picker|Specifies the time_picker step. Must be divisor of 60.|
|**with_timezone** |with_timezone|time_picker|Specifies if time_picker's value should take into account client's timezone. |

##Map and Chart
|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**chart_type** |chart( *chart_type*)|chart|Argument for chart widgets. Supported chart types are *column*, *pie*, *line* and *bar*.|
|**axis** |axis 2|widget as a chart series|Tells which axis this series will be displayed on. Axis are numbered starting in 1 for the main one.|
|**map_type** |map( *map_type*)|map|Argument for map widgets. Supported map types are *google*, *bing*, *openstreet* and *unigis*.|
|**on_new_location** |on_new_location *selectionMethod*|map|Method to be called when user clicks on locate button.|


##Button
|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**button_type** |button( *button_type*)|button|Argument for button widgets. Supported button types are *save*, *delete*, *cancel*, *validate*, *export*, *add_row* and *remove_row*. If ommited, the button will be a plain button. For *export*, *add_row* or *remove_row* option, a second optional argument is accepted: the table widget id the button is referring to (it can be inferred if there is a single Table in the Form). Option *validate* also accepts a second optional argument: a group widget id indicating which group the button will validate (if empty the form will be validated).  |
|**export_type** |export_type csv|button(export)|Used in buttons of type export to specify which format to export to. Supported values csv or pdf.|
|**feedback** |feedback|button|Provides feedback of long execution actions.|

##Text Components
|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**no_paste** |no_paste|text_field, text_area|Disable paste for that widget.|
|**secret** |secret|text_field|Input text will be hidden on screen.|
|**nopaste** |nopaste|text_field|Does not allow users to paste content on that text_field.|
|**signed** |signed|text_field bound to number types|Makes this number type to be signed.|
|**unsigned** |unsigned|text_field bound to number types|Makes this number type to be unsigned, stopping bound entity field signed inheritance.|
|**width** |width|text_field|Width in characters of a text_field.|

##Popover
|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**hover** |hover|popover|If present, popover will open when mouse hovers over the trigger button/icon. If not, popoup will open only when clicking the trigger.|
|**popover_type** |popover( *popover_type*)|popover|Argument for popover widgets. Allows to specify its position. Possible popover types are *top*, *left*, *bottom* and *right*.|


##Password
|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**metering** |metering 3|password_field|Specifies the level the password should have.|


##Image Showcase and Galleries
|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**large_variant** |large_variant "custom_variant"|gallery|Specifies the name of the large_variant of the resource to be used on the gallery widget when the user sees the large picture.|
|**thumb_variant** |thumb_variant "custom_variant"|gallery|Specifies the name of the thumb_variant of the resource to be used on the gallery widget as thumb.|
|**custom_variant** |custom_variant "custom_variant"|gallery, image, showcase|Specifies the name of the custom_variant of the resource to be used when the full image is displayed.|


##Suggest Boxes
|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**full_text** |full_text|suggest_box, tags_suggest_box, search_box, display| Widget should show full text (not cover text).|
|**with_image** |with_image|suggest_box, tags_suggest_box, search_box, display| Displays entity's image along with the display text.|
|**filter** |filter *(searchableField =|!= stringExpr when boolExpr, searchableField =|!= (stringExpr, ...), ...)*|suggest_box, tags_suggest_box, search_box|Lets users redefine suggest boxes' filter expression. [Read more](../entities/index/indexes.html#filter-expression)|
|**filter_raw** |filter *"filter expression"*|suggest_box, tags_suggest_box, search_box|Lets users redefine suggest boxes' filter expression. More on this on....|
|**on_suggest** |on_suggest *suggestMethod*|suggest_box, tags_suggest_box, search_box|Invoke *suggestMethod* when user starts typing in the box. Method receives as argument the currently typed text, and returns a list of suggested matches.|
|**on_suggest_sync** |on_suggest *suggestSyncMethod*|suggest_box, tags_suggest_box, search_box|Invoke *suggestSyncMethod* when user starts typing in the box. Method receives as argument the currently typed text, and returns a list of suggested matches. Since invocation performs a sync, this invocation tends to be heavier than the previous one.|
|**on_new** |on_new *newMethod* |suggest_box, tags_suggest_box, search_box|Displays a "Create new" suggestion on the box list and invokes *newMethod* when user selects it. Method receives as argument the currently typed text.|
|**on_new_form** |on_new_form *form_id*|suggest_box, tags_suggest_box, search_box|Displays a "Create new" suggestion on the box list and on selection loads the given form trying to set what the currently typed text in some field. The specified form must be bounded to the same entity of the suggest_box's binding. |
|**query_mode** |query_mode *query_mode* |suggest_box, tags_suggest_box, search_box|It specifies the type of search in lucene index. Options: *prefix*: searches in the index using a prefix query, *contains*: searches the query string as a substring, *fuzzy*: apply fuzzy logic to the search, allowing some alteration of the query string, *strict*: searches in the index matching the exact query string as a term. No prefix nor wildcards.|


##Date Components
|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**date_type** |date_box( *date_type*)|date_box, date_time_box, double_date_box, date_picker|Argument for date widgets. Possible date type values are *short_format*, *medium_format*, *long_format*, *full_format*.|
|**from** |from *date*|date_box, date_time_box, double_date_box, date_picker|Limits date widgets value to start from the given one. Allows usage of *today()* or *now()* predefined functions to perform basic date operations.|
|**to** |to *date*|date_box, date_time_box, double_date_box, date_picker|Limits date widgets value to end in the given one. Allows usage of *today()* or *now()* predefined functions to perform basic date operations.|
|**midnight_as_24** |midnight_as_24|date_time_box|Displays 00:00 as 24:00.|
|**reset_time** |reset_time *20:45*|date_time_box|Uses the provided time to reset the time part of the date every time the users changes the selected date.|



##Table
|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**draggable** |draggable|table|Dragging rows for reordering will be allowed.|
|**sortable** |sortable|table|Enables sorting handlers over the table's header to sort table's rows. Sorting will be stored locally in the browser so that users will preserve ordering.|
|**filterable** |filterable|table|Makes on-th-fly filters based on column's data type on tables header.|
|**on_selection** |on_selection *selectionMethod*|table|Invoke *selectionMethod* when a table's row is selected while navigating with keys or by clicking it.|
|**row_style** |row_style expression|table|Uses String or result of string expression's evaluation as style class for every row of the table.|
|**row_inline_style** |row_inline_style *"width: 100px"*|table|Uses String or result of string expression's evaluation as inline style for every row of the table.
|**column_style** |column_style expression|table|Uses String or result of string expression's evaluation as style class for every column of the table.|
|**lazy** |lazy|table|Specifies that the table must have lazy paging.|
|**lazy_fetch** |lazy_fetch|table|Specifies that the table will fetch its rows by lazy (on-demand) calling the server.|
|**unique** |unique|widget as a column|If specified in a column's widget, the table will ensure that the column has a unique value in every row.|
|**hide_column** |hide_column when *condition*|widget as a column|Used in a column's widget to hide it based on a condition.|



##Subform
|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**display** |display *expression*|subform|Uses String or result of string expression's evaluation as the text of the link that fires the subform.|
|**on** |on *anchorId*|subform|If defined in a subform, it will be displayed 'inline' at the given anchor instead of using a dialog when clicking the link.|
|**inline** |inline|subform|Subform will be embedded in the parent form.|



##Upload
|Option|Usage|Applicable to|Description|
|------|-----|-------------|-----------|
|**file_type** |upload( *file_type*)|upload|Argument for upload widgets. Allows to specify which files the upload widget lets users upload. Possible file type are all popular MIME types.|
|**camera** |camera|upload|Upload widget enables picture upload using the camera.|
