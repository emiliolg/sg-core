
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import org.jetbrains.annotations.NotNull;

import tekgenesis.field.ModelField;
import tekgenesis.field.TypeField;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Types;

/**
 * Collects the data to build an {@link ViewAttribute}.
 */
@SuppressWarnings("UnusedReturnValue")
public class ViewAttributeBuilder extends AttributeBuilder {

    //~ Instance Fields ..............................................................................................................................

    private final ModelField baseAttribute;

    private String description = "";

    //~ Constructors .................................................................................................................................

    /** Constructs a ViewAttribute Builder. */
    public ViewAttributeBuilder(@NotNull String name, @NotNull ModelField baseAttribute) {
        super(name, Types.referenceType(baseAttribute.getName()));
        this.baseAttribute = baseAttribute;
    }

    //~ Methods ......................................................................................................................................

    @NotNull public TypeField build(MetaModel view) {
        return new ViewAttribute((View) view, getName(), description, getOptions(), baseAttribute, getType(), false);
    }

    /** Sets description. */
    public ViewAttributeBuilder description(@NotNull final String str) {
        description = str;
        return this;
    }

    /** Returns the base attribute. */
    public ModelField getBaseAttribute() {
        return baseAttribute;
    }
}  // end class ViewAttributeBuilder
