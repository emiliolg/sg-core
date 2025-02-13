# Custom index Example

Sui Generis comes with a default home page, this are the steps to override it with a fully custom html page. 

## Create the new home html:

Create the Mustache (html template) file inside "/resources/html/home.mustache". (You can see the provided Built-in Custom Tags [here](../mustache.html))
This html contains a menu and a form listening to history changes.
The attribute sg-view="some.package.Views" will serve as the binding between the Handler and the view.

```html
<!DOCTYPE html>
<html sg-view="some.package.Views">
<head>
    <title>Admin - Company Name</title>

    <!-- #img looks resources in the img folder: /resources/img/favicon.ico -->    
    <link rel="icon" href="{{#img}}favicon.ico{{/img}}"/>

    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>

    <!-- #css-theme loads the theme.css if present or defaults to the suigeneris bootstrap theme -->    
    <link rel="stylesheet" href="{{#css-theme}}{{/css-theme}}" id="theme">
    <style>
        .navbar h3 { margin: 0; padding: 5px 5px 5px 0; }
        .brand.loadBrand { margin-top: 8px; padding-left: 165px; }
    </style>
</head>

<body ng-controller="AdminCtrl">
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">

        <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
                <a id="userDropdown" class="dropdown-toggle user-dropdown">
                    <span id="sg-user-name" data-sg-type="userName" data-sg-config='{"picture":true, "username":false}'></span>
                </a>
                <ul class="dropdown-menu" id="userOptions">
                    <li id="sg-user-profile" data-sg-type="userProfile"></li>
                    <li id="sg-shortcuts" data-sg-type="shortcuts"></li>
                    <li class="divider"></li>
                    <li id="sg-change-ou" data-sg-type="changeOu"></li>
                    <li id="sg-switch-user" data-sg-type="switchUser"></li>
                    <li id="sg-logout" data-sg-type="logout"></li>
                </ul>
            </li>
            <li id="sg-fullscreen" data-sg-type="fullscreen"></li>
        </ul>

        <div class="navbar-header">
            <button id="sg-toggle-menu" data-sg-type="toggleMenu"></button>
            <h3><a href="/">Admin</a></h3>
        </div>

        <div class="navbar-collapse collapse push-overlay-nav">
            <div id="menu" data-sg-type="menu" data-sg-config='{"position":"top", "hideRoot":true, "fqn":"some.package.MyMenu"}'>
                <div class="brand loadBrand">Loading...</div>
            </div>
        </div>

    </div>
</nav>

<div class="container-fluid">
    <div id="form" data-sg-type="form" data-sg-config='{"history":true}'></div>
</div>

<!-- Suigeneris forms bootstrap: includes the javascript files for the suigeneris forms and all the style.css in the project -->
{{#sg-forms-bootstrap}}{{/sg-forms-bootstrap}}

<!-- jquery dependency just needed if there are form Charts in the project-->
<!--<script defer async src="/external/js/jquery-2.2.4.min.js"></script>-->

</body>
</html>
```


## Define a handler serving the route "/"

```mm
handler HtmlSecuredHandler  {
    "/" : Html, home;
}
```


## Implement the handler to return the new html

The previously defined attribute sg-view="some.package.Views" will auto generate a Views.java class that you can use to return the Html page from the Handler.

```java
public class HtmlSecuredHandler
    extends HtmlSecuredHandlerBase
{
    private final Views v;

    //~ Constructors .............................................................................................................

    HtmlSecuredHandler(@NotNull Factory factory) {
        super(factory);
        v = factory.html(Views.class);
    }

    //~ Methods ..................................................................................................................

    /** Invoked for route "/" */
    @NotNull @Override public Result<Html> home() { return ok(v.home()); }

}
```