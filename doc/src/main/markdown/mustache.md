# Mustache and Jade
Sui Generis allows the use of both Mustache and Jade template engines. To use them you should place your _.jade_ and _.mustache_ files in the _resources/html_ folder of the project.

Check the samples to see uses of Mustache ( [Map Web App](samples/web.html) and  [Custom index page](samples/custom-index.html) )

Sui Generis provides a set of Built-in Custom Tag, to facilitate integration.
They are the following:

## sha

Includes a static resource from the *public* folder, transforming the path to a versioned, cacheable and cdn aware path

* Mustache: `{{#sha}}path/to/resource.any{{/sha}}`
* Jade: `sg.sha('path/to/resource.any')`

## favicon

Outputs the sha path to the favicon in /public/img/favicon.png (can take an optional custom path). Defaults to the Tekgenesis favicon if the file is not found. 

* Mustache: `<link rel="icon" href="{{#favicon}}{{/favicon}}">`
* Jade: `link( rel="icon", href=sg.favicon() )`

## theme

Outputs the sha path to the bootstrap theme in /public/css/theme.css (can take an optional custom path). Defaults to the Tekgenesis theme if the file is not found.

* Mustache: `<link rel="stylesheet" href="{{#css-theme}}{{theme}}{{/css-theme}}">`
* Jade: `link( rel="stylesheet", href=sg.cssTheme(theme))`

## md

Outputs Markdown content as HTML.

* Mustache: 

```html
    {{#md}}

Markdown here!

    {{/md}}
```

* Jade: 

```
	:md 
		Markdown here!
```

## json

Outputs properly escaped JSON.

* Mustache: 
```
<script>
	var type = {{#json}} {{myType.toJson}} {{/json}}
</script>
```
* Jade: 
```
script
    :plain
        var type =
    :json
        myType
```

## <a name="currency_formatter"></a> [$](#currency_formatter)

Renders using a locale aware currency formatter the given value.

* Mustache: `{{#$}}{{myBigDecimal}}{{/$}}`
* Jade: `sg.currency(myBigDecimal)`

## HTML Include

Includes an HTML parameter at the given point.

* Mustache: 
```
<html sg-view="..." sg-params="content : Html">
	...
	{{#sg-include}}content{{/sg-include}}
	...
</html>
```
* Jade: Not supported.

For a relative path include, native language is supporter, looking inside the *html* folder.

* Mustache: `{{>html/sui-subtemplate}}`
* Jade: `include ./sui-subtemplate`

## Forms Bootstrap

Bootstraps page to include Sui Generis Forms.

* Mustache: `{{#sg-forms-bootstrap}}{{/sg-forms-bootstrap}}`
* Jade: `:sgFormsBootstrap`

## Component versions

Builds a String containing name and version of Sui Generis.

* Mustache: `{{#sg-versions}}{{/sg-versions}}`
* Jade: `sg.versions()`

