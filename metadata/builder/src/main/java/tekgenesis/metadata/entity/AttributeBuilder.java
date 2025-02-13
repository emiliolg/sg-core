
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

import tekgenesis.field.FieldOption;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;

import static java.util.Collections.singletonList;

/**
 * Collects the data to build an {@link Attribute}.
 */
@SuppressWarnings("UnusedReturnValue")
public class AttributeBuilder extends CompositeFieldBuilder<AttributeBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private String  description = "";
    private boolean inner;
    private boolean multiple;

    @NotNull private String reverseReference;
    private String          seqName;
    private boolean         synthesized = false;

    //~ Constructors .................................................................................................................................

    /** Constructor with name and type. */
    public AttributeBuilder(@NotNull String name, Type type) {
        super(name, type);
        multiple         = false;
        seqName          = null;
        reverseReference = "";
    }

    //~ Methods ......................................................................................................................................

    @NotNull public TypeField build(MetaModel entity) {
        return new Attribute((DbObject) entity,
            getName(),
            getType(),
            description,
            getOptions(),
            inner,
            multiple,
            reverseReference,
            synthesized,
            seqName);
    }

    /** sets the attribute column. */
    public AttributeBuilder column(final String columnName) {
        try {
            withOption(FieldOption.COLUMN, singletonList(columnName));
        }
        catch (final BuilderException ignore) {}
        return this;
    }

    /** Sets description. */
    public AttributeBuilder description(@NotNull final String str) {
        description = str;
        return this;
    }

    /** Set this Attribute as a reference to a inner Entity. */
    public AttributeBuilder inner() {
        inner = true;
        return this;
    }

    /** Set this Attribute as a 'Multiple' one (ManyToXX Reference). */
    public AttributeBuilder multiple() {
        multiple = true;
        return this;
    }

    /**
     * Set this Attribute as 'Serial' with the default Sequence Name. Its value will define the
     * entity identity.
     */
    public AttributeBuilder serial() {
        return serial("");
    }

    public AttributeBuilder serial(final String sequenceName) {
        seqName = sequenceName;
        return this;
    }

    /** Flags the attribute as synthesized. */
    public AttributeBuilder synthesized() {
        synthesized = true;
        return this;
    }
    /** Set the name of the Attribute holding the reverse Reference. */
    public AttributeBuilder withReverseReference(@NotNull String attributeName) {
        reverseReference = attributeName;
        return this;
    }

    boolean isSerial() {
        return seqName != null;
    }
}  // end class AttributeBuilder
