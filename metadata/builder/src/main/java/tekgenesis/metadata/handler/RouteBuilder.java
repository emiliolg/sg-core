
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.service.Method;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.metadata.entity.FieldBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.InvalidTypeForDynamicPartException;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.common.collections.Colls.exists;
import static tekgenesis.common.core.QName.qualify;
import static tekgenesis.metadata.exception.ParameterCollidingException.parameterCollidingParameter;
import static tekgenesis.metadata.exception.ParameterCollidingException.parameterCollidingRoute;
import static tekgenesis.metadata.handler.Parts.retrieveParts;
import static tekgenesis.metadata.handler.Routes.normalize;

/**
 * Route builder.
 */
public class RouteBuilder extends FieldBuilder<RouteBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private final String                                handlerClass;
    @NotNull private final Collection<ParameterBuilder> parameters;
    private Seq<Part>                                   relativeParts;
    private String                                      relativePath;
    private Type                                        type;

    //~ Constructors .................................................................................................................................

    /** Route builder default constructor. */
    public RouteBuilder(String handlerClass) {
        this.handlerClass = handlerClass;
        relativePath      = "";
        relativeParts     = emptyIterable();
        type              = Types.nullType();
        parameters        = new ArrayList<>();
    }

    //~ Methods ......................................................................................................................................

    /** Build route. */
    public Route build(String onRoutePrefix) {
        final FieldOptions options      = getOptions();
        final String       absolutePath = onRoutePrefix + relativePath;
        final Seq<Part>    parts        = retrieveParts(absolutePath);

        // Process parameters
        final List<Parameter> params = new ArrayList<>();
        for (final ParameterBuilder builder : parameters)
            params.add(builder.build(null));

        return new Route(Routes.join(parts), type, options, parts, params);
    }

    /** Create route parameter builder. */
    public ParameterBuilder createParameterBuilder(String parameter, Type t) {
        return new ParameterBuilder(parameter, t);
    }

    /** Mark route as internal. */
    public RouteBuilder internal()
        throws BuilderException
    {
        with(FieldOption.INTERNAL);
        return this;
    }

    /** Add route body option. */
    public RouteBuilder withBody(@NotNull final Type t)
        throws BuilderException
    {
        return with(FieldOption.BODY, t);
    }

    /** Add route method. */
    public RouteBuilder withClassMethod(@NotNull final String method)
        throws BuilderException
    {
        return with(FieldOption.CLASS_METHOD, qualify(handlerClass, method));
    }

    /** Add route http method. */
    public RouteBuilder withHttpMethod(@NotNull final String method)
        throws BuilderException
    {
        return with(FieldOption.METHOD, method);
    }

    /** Add route http method. */
    public RouteBuilder withHttpMethod(@NotNull final Method method)
        throws BuilderException
    {
        return with(FieldOption.METHOD, method);
    }

    /** Add route key string. */
    public RouteBuilder withKey(String key)
        throws BuilderException
    {
        return with(FieldOption.KEY, key);
    }

    /** Add a parameter to the route. */
    public RouteBuilder withParameter(@NotNull final ParameterBuilder builder)
        throws BuilderException
    {
        if (relativeParts.filter(p -> !p.isStatic()).exists(p -> p != null && p.getName().equals(builder.getName())))
            throw parameterCollidingRoute(builder.getName(), getRelativePath());

        if (exists(parameters, p -> p != null && p.getName().equals(builder.getName())))
            throw parameterCollidingParameter(builder.getName(), getRelativePath());

        parameters.add(builder);
        return this;
    }

    /** Add route path. */
    public RouteBuilder withPath(@NotNull final String p)
        throws BuilderException
    {
        final String normalized = normalize(p);
        relativeParts = retrieveAndCheckParts(normalized);
        relativePath  = normalized;
        return this;
    }

    /** Add route summary description. */
    public RouteBuilder withSummary(String summary)
        throws BuilderException
    {
        return with(FieldOption.SUMMARY, summary);
    }

    /** Add route type. */
    public RouteBuilder withType(@NotNull final Type t) {
        type = t;
        return this;
    }

    /** Get route relative path. */
    public String getRelativePath() {
        return relativePath;
    }

    @Override protected void checkOptionSupport(FieldOption option)
        throws BuilderException {}

    /** Add secure method. */
    void withSecureMethod(SecureMethod method)
        throws BuilderException
    {
        with(FieldOption.SECURE_METHOD, method);
    }

    /** Return true if route is unrestricted. */
    boolean isUnrestricted() {
        return getOptions().getBoolean(FieldOption.UNRESTRICTED);
    }

    Method getHttpMethod() {
        return getOptions().getEnum(FieldOption.METHOD, Method.class, Method.GET);
    }

    /** Get route relative parts. */
    Seq<Part> getRelativeParts() {
        return relativeParts;
    }

    private Seq<Part> retrieveAndCheckParts(String normalized)
        throws InvalidTypeForDynamicPartException
    {
        try {
            return retrieveParts(normalized);
        }
        catch (final IllegalArgumentException e) {
            throw new InvalidTypeForDynamicPartException(e.getMessage(), handlerClass);
        }
    }
}  // end class RouteBuilder
