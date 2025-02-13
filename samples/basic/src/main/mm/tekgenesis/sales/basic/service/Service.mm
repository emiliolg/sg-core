package tekgenesis.sales.basic.service;

final type Product {
	productId "Id"              : String(8);
	model "Model No."           : String(30);
	description "Description"   : String(100), optional;
	price "Price"               : Decimal(10,2);
	created "Created"           : DateTime;
}

final type ProductComparison {
    left : Product;
	rigth : Product;
}

final type ProductList {
    list : Int;
    products : Product*;
}

final type Query {
    q : String;
    result : Product*;
}

handler SiteHandler
    on_route "/site"
    unrestricted
{
    "/" : Html, home;
    "/cache" : Html, cache;
    "/product/$pid" : Html, product;
    "/products" : Html, products;
    "/product/$pid/image" : Resource, image;
    "/failure"     : Html, failure, parameters { method: Int(3); };
    "/search" : Html, search, parameters {
        q : String, optional;
    };
}

remote handler ReturnTypesHandler
    on_route "/return"
{
    "/Boolean"  : Boolean,          returnBoolean;
    "/Real"     : Real,             returnReal;
    "/Decimal"  : Decimal(10,2),    returnDecimal;
    "/Int"      : Int,              returnInt;
    "/DateTime" : DateTime,         returnDateTime;
    "/Date"     : Date,             returnDate;
    "/Type"     : Product,          returnType;
    "/Enum"     : State,            returnEnum;
    "/Html"     : Html,             returnHtml;
    "/Resource" : Resource,         returnResource;
    "/Void"     : Void,             returnVoid;
    "/Any"      : Any,              returnAny;
}

remote handler ProductHandler "Product Services"
    on_route "/products"
{
    "/$id"                    : Product, show, summary "Get Product info by id";
    "/$id"                    : Void, update, body Product, method post;
    "/$id/details"            : Product, details;
    "/featured"               : Product*, featured;
    "/list/$list"             : ProductList, list;
    "/headers"                : String, headers, parameters {
        contentType : String;
        charset : String, default "UTF8";
    };
    "/redirect/$code:Int"     : Void, redirection;
    "/failure"                : String, failure;
    "/failure"                : String, failure, method post, body Product;
    "/failureWithMessage"     : Int, failureWithMessage, parameters { method: Int(3); };
    "/failureWithMessageStr"  : String, failureWithMessageStr, parameters { method: Int(3); };
    "/$id/related"            : Product, related, body Product, method post;
    "/body/multiple"          : String, bodyMultiple, body Product*, method post;
    "/body/simple"            : String, bodySimple, body String, method post;
    "/body/empty"            : String,  bodyEmpty, method post;
    "/parameters/$id:Int"     : String, parameters, parameters {
        from : String;
        to : String, optional;
        step : Int, default 10;
    };
    "/parameters/multiples"   : String, multiples, parameters {
        a : Int*;
        b : Real*, optional;
        c : Decimal(6,4)*;
        d : Date*;
        e : DateTime*;
        f : String(25)*;
        g : Boolean*;
        h : State*;
    };
    "/parameters/conversions" : String, conversions, parameters {
        a : Int, default 3;
        b : Real, default 3.1415;
        c : Decimal(6,4), default 3.1415;
        d : Date, default "2014-01-31";
        e : DateTime, default "2014-01-31 06:47Z";
        f : String(25), default "Hello World!";
        g : Boolean, default false;
        h : State, default ACTIVE;
    };
    "/localized/message"          : String, localizedMessage;
    "/cookies/outbound"           : Void, outboundCookies;
    "/cookies/inbound"            : String, inboundCookies;
}

internal handler InternalProductHandler
    on_route "/internal"
{
    "/message"                  : Html, internal;
    "/timestamp"                : Html, timestamp;
}

handler CountryHandler
    on_route "/country"
{
    "/"                 : Country*, list, parameters {
        success : Boolean, default true;
    };
    "/"                 : Void, post, body Country, method post, parameters {
        success : Boolean, default true;
    };
    "/$iso2:String(2)"  : Void, put, body Country, method put, parameters {
        success : Boolean, default true;
    };
    "/$iso2:String(2)"  : Void, delete, method delete, parameters {
        success : Boolean, default true;
    };
    "/getAndInsert"     : Country, getAndInsert, parameters {
        success : Boolean, default true;
    };
    "/$iso2:String(2)"  : Country, get;
    "/exception"  : Country, postWithException, body Country, method post;
}

final type Country {
    name : String(30);
    iso2 : String(2);
}

enum State {
    ACTIVE;
    DEACTIVE;
    DEPRECATED;
    DELETED;
}

handler ExternalServicesHandler
    on_route "/invocations"
{
    "/compumundo/services" : String, compumundo;
    "/garbarino/services" : String, garbarino;
}

remote handler ExceptionHandler
    on_route "/exceptions"
    raise HandlerError
{
    "/badRequest" : String, returnBadRequest;
    "/badRequestWithObject" : String, returnBadRequestWithObject;
    "/badRequestException" : String, exception;
    "/badRequestExceptionForVoid" : Void, exceptionForVoid, method post;
    "/badRequestExceptionWithLabel" : String, exceptionWithMsg, parameters { msg : String; };
    "/badRequestSubroutineValidException" : String, subroutineValidException;
    "/badRequestIntSubroutineValidException" : Int, subroutineIntValidException;
    "/badRequestSubroutineInvalidException" : String, subroutineInvalidException;
    "/badRequestSubroutineIntInvalidException" : Int, subroutineIntInvalidException;
}

exception enum HandlerError {
    ERROR_MESSAGE : "Error message";
    QUOTE_MESSAGE : "%s";
}

