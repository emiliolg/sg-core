
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
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.QName;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.CollidingPathException;
import tekgenesis.metadata.exception.IncompatiblePathException;
import tekgenesis.type.EnumType;
import tekgenesis.type.Modifier;

import static tekgenesis.type.Modifier.INTERNAL;

/**
 * Handler builder.
 */
public class HandlerBuilder extends ModelBuilder.Default<Handler, HandlerBuilder> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Routing collisions;

    @Nullable private String                        form;
    @NotNull private String                         onRoute;
    @Nullable private EnumType                      raising;
    @NotNull private final Collection<RouteBuilder> routes;
    @NotNull private SecureMethod                   secureMethod;

    //~ Constructors .................................................................................................................................

    /** Builder constructor. */
    public HandlerBuilder(@NotNull String src, @NotNull String pkg, @NotNull String name) {
        super(src, pkg, name);
        routes       = new ArrayList<>();
        collisions   = Routing.create();
        form         = null;
        raising      = null;
        onRoute      = "";
        secureMethod = SecureMethod.USERNAMEPASSWORD;
    }

    //~ Methods ......................................................................................................................................

    /** Add a route to the handler. */
    public HandlerBuilder addRoute(@NotNull RouteBuilder r)
        throws BuilderException
    {
        consolidate(r);
        checkCollision(r);
        routes.add(r);
        return this;
    }

    /** Mark handler as remote. */
    public HandlerBuilder asRemote() {
        modifiers.add(Modifier.REMOTE);
        return this;
    }

    @Override public Handler build()
        throws BuilderException
    {
        final List<Route> handlerRoutes = new ArrayList<>();
        final Handler     handler       = new Handler(QName.createQName(domain, id), label, modifiers, sourceName, form, raising, handlerRoutes);

        // Process routes
        for (final RouteBuilder routeBuilder : routes)
            handlerRoutes.add(routeBuilder.build(onRoute));

        return handler;
    }

    @NotNull @Override public List<BuilderError> check() {
        return Collections.emptyList();
    }

    /** Handler on route. */
    public HandlerBuilder onRoute(String r) {
        onRoute = Routes.normalize(r);
        return this;
    }

    /** Handler bind form. */
    public HandlerBuilder withForm(String f) {
        form = QName.qualify(getDomain(), f);
        return this;
    }

    /** Raising application exception with given enum type. */
    public HandlerBuilder withRaiseEnum(EnumType r) {
        raising = r;
        return this;
    }

    /** Configure secure method.* */
    public HandlerBuilder withSecureMethod(@NotNull final SecureMethod method) {
        secureMethod = method;
        return this;
    }

    /** Mark handler as internal. */
    HandlerBuilder asInternal() {
        modifiers.add(Modifier.INTERNAL);
        return this;
    }

    private void checkCollision(@NotNull final RouteBuilder r)
        throws BuilderException
    {
        final Route route = new Route(r.getRelativePath(), r.getRelativeParts(), r.getOptions());

        final Route other;
        try {
            other = collisions.addRoute(route);
        }
        catch (final IllegalStateException e) {
            throw new IncompatiblePathException(e.getMessage(), r.getRelativePath(), id);
        }

        if (other != null) throw new CollidingPathException(r.getRelativePath(), other.getPath(), id);
    }

    private void consolidate(RouteBuilder builder)
        throws BuilderException
    {
        if (hasModifier(INTERNAL)) builder.internal();
        builder.withSecureMethod(builder.isUnrestricted() ? SecureMethod.UNRESTRICTED : secureMethod);
    }
}  // end class HandlerBuilder
