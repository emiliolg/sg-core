# Handlers

Handlers are the way to define services and custom pages based on requests. Forms are used to interact with structured data and predefined widgets, but for other http services and fully customizable pages, Handlers are the way to go.

As entities have attributes and forms have widgets, Handlers are defined by routes. Routes are defined by a path, a return type (see [Types](language/basicTypes.html#types)), and a handling method. Optionally, parameters, http method, post payload type, and other options may be defined.

Sui Generis provides a mechanism to generate Java and Angular JS code to call this handlers from other systems.

# Example

```
handler ProductsService 
	on_route "/api/product"
{
	"/"                 : Product*, list;
	"/"                 : Product, create, method post, body Product;
	"/$pid"             : Product, get;
	"/$pid"             : Product, update, method put, body Product;
	"/$pid/features"    : Feature*, features;
	"/search"           : Product*, search, parameters { 
                                                            q     : String, optional;
                                                            limit : Int, default 10;
                                                        };
}
 
handler Site
{
	"/"         : Html, home;
	"/contact"  : Html, contact;
	"/news"     : Html, news;
}
```

Handlers are the way to handle routing for defining services and custom pages.

The MetaModel language allows model options specification when defining a handler. In the above example, the model option *on_route* specifies a path to be prepended to all routes specified on Handler.

# Routes

Every handler is composed by a set of routes. All routes have a path, a return type, and a handling method. Other options can be specified.

Below is a route on specified path, with a dynamic part *$pid*, returning a *Product* type, delegating execution on method *update*, for http method *put*, expecting a *Product* type as payload, with a literal summary.

```
"/api/product/$pid"             : Product, update, method put, body Product, summary "Product update";
```

Handler java generated method:

```java
/** Invoked for route "/api/product/$pid". */
@NotNull public abstract Result<Product> update(@NotNull String pid, @NotNull Product body);
```

## Path syntax

First literal expression on the route definition specifies the path. Each path starts with a '/' a may contain different types of parts:
 
**Static part**

Static parts indicates exact matching.

```
// Three static parts
"/api/products/deal"        : Deal, deal, summary "Return product deal of the day";

// Two static parts
"/api/products"             : Product*, list, summary "List all Products";
```
 
Handler java generated methods:

```java
/** Invoked for route "/api/products/deal". */
@NotNull public abstract Result<Deal> deal();

/** Invoked for route "/api/products". */
@NotNull public abstract Result<Seq<Product>> list();
```
 
**Dynamic part**

Dynamic parts matches exactly one path part. *String* is the default type for dynamic parts. In the bellow example, an String *pid* variable will be added to the *detail* generated method.

```
// Two static parts, and a String dynamic part
"/api/product/$pid"             : Product, detail, summary "Return product details for specified product id";

// Two static parts, and an Int dynamic part
"/api/priority/$pid:Int"             : Priority, detail, summary "Return priority details for specified priority id";
```
 
Handler java generated methods:

```java
/** Invoked for route "/api/product/$pid". */
@NotNull public abstract Result<Product> detail(@NotNull String pid);

/** Invoked for route "/api/priority/$pid". */
@NotNull public abstract Result<Priority> detail(int pid);
```
 
Note that a path may have more than one dynamic part.
 
**Wildcard part**

Wildcard parts are when you want a dynamic part to capture more than one path segment. Wildcard parts span several *'/'*.

```
"/sha/$file*"             : String, sha, summary "Return the SHA of given file path";
```

Handler java generated method:

```java
/** Invoked for route "/sha/$file*". */
@NotNull public abstract Result<String> sha(@NotNull String pid);
```
  
## Result

After the path literal expression, the route definition specifies the result type. Results may be of any of the basic Sui Generis [Types](language/basicTypes.html#types) excepting *Resource* and entity reference. Additionally, *Void* and *Html* types are accepted for routes.

## Http Method

If not otherwise specified, all routes will handle **GET** http method. To specify a different http method on route, add the *method* field option.

```
// Specified for GET method
"/api/product/$pid"        : Product, details, summary "Product details";

// Specified for DELETE method
"/api/product/$pid"        : Product, delete, method delete, summary "Product deletion";

// Specified for POST method
"/api/product"             : Product, create, method post, body Product, summary "Product creation";
```

## Parameters

Any route may specify parameters as well. Parameters are defined as entity fields and will not match if type conversion is not possible. Parameters admit *default* values and *optional* specification. Array parameters are allowed specifying types with *multiple* mark. A parameter to match must conform type checking and will be constrained to type scaling and truncation if needed.

```
"/parameters" : Void, example, parameters {
        // Required parameters
        a : String(3);
        b : Int, default 3;
        c : Real, default 3.1415;
        d : Date, default "2014-01-31";
        e : State, default ACTIVE;

        // Optional parameters
        f : String, optional;
        g : Decimal(6,4), optional;

        // Multiple parameters
        h : Int*;
        i : String(25)*;
    };
```

Handler java generated method:

```
/** Invoked for route "/parameters" */
@NotNull public abstract Result<Void> example(@NotNull String a, int b, double c, @NotNull DateOnly d, @NotNull State e, @Nullable String f, @Nullable BigDecimal g, @NotNull Seq<Integer> h, @NotNull Seq<String> i);
```

# Invokers

Sui Generis provides an API to make http requests. This API allows to specify several strategies to handle fault tolerance and load balancing. User can also specify timeouts and error handling. Invocation can be synchronous and asynchronous as well.

```java
HttpInvokers.invoker(Strategy.MASTER_SLAVE, server1, server2).resource("/status").execute();
```
