
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.annotation.Pure;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.of;

/**
 * Widget qualification base class.
 */
abstract class BaseQualifiedWidget<This extends BaseQualifiedWidget<This>> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull final Option<This> qualification;
    @NotNull final Widget       widget;

    //~ Constructors .................................................................................................................................

    BaseQualifiedWidget(@NotNull final Widget widget) {
        qualification = empty();
        this.widget   = widget;
    }

    BaseQualifiedWidget(@NotNull final This qualification, @NotNull final Widget widget) {
        this.qualification = of(qualification);
        this.widget        = widget;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseQualifiedWidget)) return false;

        final BaseQualifiedWidget<?> that = (BaseQualifiedWidget<?>) o;
        return qualification.equals(that.qualification) && widget.equals(that.widget);
    }

    /**
     * Return {@link BaseQualifiedWidget qualified widget} full name (e.g.: "profile.name" or
     * "items.unitPrice#0"). Includes indexing (if any). See {@link #path()}
     */
    @NotNull @Pure public final String fqn() {
        final StrBuilder builder = new StrBuilder().startCollection(".");
        fqn(builder);
        return builder.toString();
    }

    @Override public int hashCode() {
        int result = qualification.hashCode();
        result = 31 * result + widget.hashCode();
        return result;
    }

    /** Map given model to deepest contextual model. */
    @NotNull @Pure public abstract Option<Model> mapping(@NotNull final Model m);

    /**
     * Return {@link BaseQualifiedWidget qualified widget} path (e.g.: "profile.name" or
     * "items.unitPrice"). Ignores indexing (if any). See {@link #fqn()}
     */
    @NotNull @Pure public final String path() {
        final StrBuilder builder = new StrBuilder().startCollection(".");
        path(builder);
        return builder.toString();
    }

    /** Return {@link SourceWidget source widget} with fqn as path. */
    @NotNull @Pure public SourceWidget toSourceWidget() {
        return new SourceWidget(fqn());
    }

    @Override public String toString() {
        return fqn();
    }

    /** Get widget if specified. */
    @NotNull public Widget widget() {
        return widget;
    }

    /** Recursively collect full qualified name (e.g. items.unitPrice#2). */
    @SuppressWarnings("WeakerAccess")
    final void fqn(@NotNull final StrBuilder builder) {
        qualification.ifPresent(q -> q.fqn(builder));
        name(builder);
    }

    /** Collect qualified widget name. */
    void name(@NotNull final StrBuilder builder) {
        builder.appendElement(widget.getName());
    }

    /** Recursively collect full qualified id (e.g. items.unitPrice). */
    @SuppressWarnings("WeakerAccess")
    final void path(@NotNull final StrBuilder builder) {
        qualification.ifPresent(q -> q.path(builder));
        builder.appendElement(widget.getName());
    }
}  // end class BaseQualifiedWidget
