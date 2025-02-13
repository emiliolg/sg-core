
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static tekgenesis.common.Predefined.ensureNotNull;

/**
 * A type that represents an Entity reference.
 */
@SuppressWarnings("FieldMayBeFinal")
public class EntityReference extends AbstractType {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private String domain;
    @Nullable private String name;

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    public EntityReference() {
        name   = null;
        domain = null;
    }

    /** Constructor with a domain and the referred entity name. */
    public EntityReference(@Nullable final String domain, @Nullable final String name) {
        this.name   = name;
        this.domain = domain;
    }

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        return getFullName();
    }

    /** Returns the referred entity domain. */
    @NotNull public String getDomain() {
        return ensureNotNull(domain, "Entity Reference contains no domain value");
    }

    /** Returns the referred entity full name. */
    @NotNull public final String getFullName() {
        return domain + "." + name;
    }

    @Override public String getImplementationClassName() {
        return getFullName();
    }

    @NotNull @Override public Kind getKind() {
        return Kind.REFERENCE;
    }

    /** Returns the referred entity name. */
    @NotNull public String getName() {
        return ensureNotNull(name, "Entity Reference contains no name value");
    }

    @Override public boolean isDatabaseObject() {
        return true;
    }

    @Override public boolean isEntity() {
        return true;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 9121012343584208247L;
}  // end class EntityReference
