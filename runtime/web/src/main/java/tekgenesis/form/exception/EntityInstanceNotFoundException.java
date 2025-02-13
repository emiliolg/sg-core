
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.exception;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.exception.SuiGenerisException;

import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.metadata.form.MetadataFormMessages.MSGS;

/**
 * Exception to be used when an instance is not found.
 */

public class EntityInstanceNotFoundException extends SuiGenerisException {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String  entityName;
    @Nullable private final String pk;

    //~ Constructors .................................................................................................................................

    /** Build an exception for when an entity is not found. */
    public EntityInstanceNotFoundException(@NotNull final String entityName) {
        this.entityName = entityName;
        pk              = null;
    }

    /** Build an exception for when an entity instance with a pk is not found. */
    public EntityInstanceNotFoundException(@NotNull final String entityName, @Nullable final String pk) {
        this.entityName = entityName;
        this.pk         = pk;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getMessage() {
        if (hasPk()) return MSGS.notFoundWithPk(getEntityName(), notNull(getPk()));
        else return MSGS.notFound(getEntityName());
    }

    /** Return true if this exception has pk. */
    private boolean hasPk() {
        return pk != null;
    }

    /** Returns the entity name. */
    @NotNull private String getEntityName() {
        return entityName;
    }

    /** Returns the pk. */
    @Nullable private String getPk() {
        return pk;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3534541862436982561L;
}  // end class EntityInstanceNotFoundException
