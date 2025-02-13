
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.QName;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.core.QName.createQName;

/**
 * Instance reference. Composed from a {@link QName} and a string specifying a primary key.
 */
@SuppressWarnings("FieldMayBeFinal")  // Gwt
public class InstanceReference implements Comparable<InstanceReference>, Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private QName  fqn;
    @NotNull private String key;

    //~ Constructors .................................................................................................................................

    private InstanceReference() {
        this(QName.EMPTY, "");
    }

    /** Create and instance reference. */
    private InstanceReference(@NotNull QName fqn, @NotNull String key) {
        this.fqn = fqn;
        this.key = key;
    }

    //~ Methods ......................................................................................................................................

    @Override
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public int compareTo(@NotNull InstanceReference o) {
        final int d = fqn.compareTo(o.fqn);
        return d != 0 ? d : key.compareTo(o.key);
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof InstanceReference)) return false;
        final InstanceReference that = (InstanceReference) o;
        return fqn.equals(that.fqn) && key.equals(that.key);
    }

    @Override public int hashCode() {
        return fqn.hashCode() * 31 + key.hashCode();
    }

    /** Print instance reference. */
    @Override public String toString() {
        return fqn + REFERENCE_PATH_SEPARATOR + key;
    }

    /** Returns true if key is defined for given instance. */
    public boolean isKeyDefined() {
        return isNotEmpty(key);
    }

    /** Returns instance fqn. */
    @NotNull public QName getFqn() {
        return fqn;
    }

    /** Returns instance key. */
    @NotNull public String getKey() {
        return key;
    }

    /** Return true if instance reference is empty. */
    public boolean isEmpty() {
        return this == EMPTY || fqn.isEmpty() && key.isEmpty();
    }

    //~ Methods ......................................................................................................................................

    /** Creates an InstanceReference from a concatenated string (Eg: domain.Entity/1:2:3) */
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public static InstanceReference createInstanceReference(@NotNull final String reference) {
        final int slash = reference.indexOf(REFERENCE_PATH_SEPARATOR);
        return new InstanceReference(createQName(reference.substring(0, slash)), reference.substring(slash + 1));
    }

    /** Creates an InstanceReference from a fully qualified name, and a key. */
    public static InstanceReference createInstanceReference(@NotNull final QName fqn, @NotNull final String key) {
        return new InstanceReference(fqn, key);
    }

    /** InstanceReference de serialization. */
    public static InstanceReference deserialize(StreamReader r) {
        final boolean some = r.readBoolean();
        return some ? (InstanceReference) r.readObject() : null;
    }

    /** InstanceReference serialization. */
    public static void serialize(StreamWriter w, InstanceReference reference) {
        final boolean some = reference != null;
        w.writeBoolean(some);
        if (some) w.writeObject(reference);
    }

    //~ Static Fields ................................................................................................................................

    public static final String REFERENCE_PATH_SEPARATOR = "/";

    /** Empty instance reference. */
    public static final InstanceReference EMPTY = new InstanceReference();

    private static final long serialVersionUID = -6370678905589727883L;
}  // end class InstanceReference
