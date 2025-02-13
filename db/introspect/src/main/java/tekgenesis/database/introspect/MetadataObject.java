
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Constants.HASH_SALT;

/**
 * A Named metadata object.
 */
public abstract class MetadataObject<This extends MetadataObject<This>> implements Serializable, Comparable<MetadataObject<This>> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String name;

    //~ Constructors .................................................................................................................................

    protected MetadataObject(@NotNull final String name) {
        this.name = name;
    }

    //~ Methods ......................................................................................................................................

    public int compareTo(@NotNull final MetadataObject<This> that) {
        return getFullName().compareTo(that.getFullName());
    }

    @Override public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj instanceof MetadataObject) {
            final MetadataObject<This> that = cast(obj);
            return name.equals(that.name) && getQualification().equals(that.getQualification());
        }
        return false;
    }
    @Override public int hashCode() {
        return HASH_SALT * name.hashCode() + getQualification().hashCode();
    }

    /** returns true if the objects are equivalent. */
    public abstract boolean sameAs(This to);

    @Override public String toString() {
        return getFullName();
    }

    /** Getter for fully qualified name of object. */
    @NotNull public String getFullName() {
        final String q = getQualification();
        return q.isEmpty() ? name : name.isEmpty() ? q : q + "." + name;
    }

    /** A value guaranteed to be unique in the database for this object. */
    @NotNull public String getLookupKey() {
        return getFullName();
    }

    /** Getter for name of object. */
    @NotNull public final String getName() {
        return name;
    }
    @NotNull abstract String getQualification();

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5346413974520029363L;
}  // end class MetadataObject
