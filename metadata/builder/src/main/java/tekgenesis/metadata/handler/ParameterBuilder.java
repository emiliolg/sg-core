
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.metadata.entity.CompositeFieldBuilder;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;

/**
 * Handler Route parameter builder.
 */
public class ParameterBuilder extends CompositeFieldBuilder<ParameterBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private String description = "";
    private String route       = "";

    //~ Constructors .................................................................................................................................

    /** Handler Route parameter builder constructor. */
    public ParameterBuilder(@NotNull String name, @NotNull Type type) {
        super(name, type);
    }

    //~ Methods ......................................................................................................................................

    @Override public Parameter build(@Nullable MetaModel ignored) {
        return new Parameter(getName(), getType(), route, description, getOptions());
    }

    /** Add parameter description. */
    public ParameterBuilder description(String label) {
        description = label;
        return this;
    }

    /** Add parameter parent route. */
    public void route(String r) {
        route = r;
    }
}
