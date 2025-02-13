
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.translate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.io.PropertyWriter;
import tekgenesis.lang.mm.FileUtils;

/**
 * Translation property item.
 */
class Property {

    //~ Instance Fields ..............................................................................................................................

    private final boolean         added;
    private final boolean         changed;
    private final boolean         generated;
    @NotNull private final String key;
    @NotNull private final String label;
    @NotNull private final String value;

    //~ Constructors .................................................................................................................................

    Property(@NotNull final String key, @NotNull final String value, boolean generated, @NotNull final String label, boolean added, boolean changed) {
        this.key       = key;
        this.value     = value;
        this.generated = generated;
        this.label     = label;
        this.added     = added;
        this.changed   = changed;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property)) return false;

        final Property property = (Property) o;

        return generated == property.generated && key.equals(property.key) && label.equals(property.label) && value.equals(property.value);
    }

    @Override public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + label.hashCode();
        result = 31 * result + (generated ? 1 : 0);
        return result;
    }

    void write(@NotNull final PropertyWriter writer) {
        writer.writeComment(label);
        if (generated) writer.writeComment(FileUtils.GENERATED);
        writer.writeProperty(key, value);
        writer.skipLine();
    }

    boolean isAdded() {
        return added;
    }

    boolean isChanged() {
        return changed;
    }

    boolean isGenerated() {
        return generated;
    }

    @NotNull String getKey() {
        return key;
    }

    @NotNull String getLabel() {
        return label;
    }

    @NotNull String getValue() {
        return value;
    }
}  // end class Property
