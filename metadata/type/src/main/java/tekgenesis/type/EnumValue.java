
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Strings;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;

import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.Predefined.notNull;

/**
 * An Enum Value.
 */
@SuppressWarnings("FieldMayBeFinal")
public class EnumValue implements ModelField, Serializable {

    //~ Instance Fields ..............................................................................................................................

    private String documentation;

    private EnumType enumType;

    private String         label;
    private String         name;
    private final int      ordinal;
    private Serializable[] values;

    //~ Constructors .................................................................................................................................

    /** constructor.* */
    EnumValue() {
        enumType      = null;
        label         = null;
        name          = null;
        values        = null;
        documentation = "";
        ordinal       = 0;
    }

    /** Create an Enum Value. */
    public EnumValue(@NotNull EnumType enumType, String name, @NotNull String label, int ordinal, @NotNull String documentation,
                     @NotNull Serializable[] values) {
        this.name          = notNull(name);
        this.label         = label;
        this.enumType      = enumType;
        this.ordinal       = ordinal;
        this.values        = values;
        this.documentation = documentation;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object obj) {
        return obj instanceof EnumValue && name.equals(((EnumValue) obj).name);
    }

    @Override public boolean hasChildren() {
        return false;
    }

    @Override public int hashCode() {
        return name.hashCode();
    }

    /** Returns true if it has a (Non Default) Label. */
    public boolean hasLabel() {
        return !label.isEmpty();
    }

    @Override public String toString() {
        return name + ":" + getLabel();
    }

    @NotNull @Override public ImmutableList<? extends ModelField> getChildren() {
        return Colls.emptyList();
    }
    @NotNull @Override public String getFieldDocumentation() {
        return documentation;
    }

    @NotNull @Override public String getLabel() {
        return notEmpty(label, Strings.toWords(name));
    }
    @NotNull @Override public String getName() {
        return name;
    }

    @NotNull @Override public FieldOptions getOptions() {
        return FieldOptions.EMPTY;
    }

    /** Get Ordinal number for Enum Value. */
    public int getOrdinal() {
        return ordinal;
    }

    @NotNull @Override public EnumType getType() {
        return enumType;
    }

    @Override public void setType(@NotNull Type type) {
        throw new IllegalStateException();
    }

    /** Return the list of values for this Enum instance. */
    @NotNull public Object[] getValues() {
        return values;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 4000860122540519612L;
}  // end class EnumValue
