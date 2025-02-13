
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.database.introspect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class SchemaObject<This extends MetadataObject<This>> extends MetadataObject<This> {

    //~ Instance Fields ..............................................................................................................................

    private final String     remarks;
    private final SchemaInfo schema;

    //~ Constructors .................................................................................................................................

    SchemaObject(final SchemaInfo schema, final String name, @Nullable String remarks) {
        super(name);
        this.schema  = schema;
        this.remarks = remarks;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the Full name without schema prefix. */
    @NotNull public String getPlainName() {
        return schema.getPlainName() + "." + getName();
    }

    /** Return Remarks. */
    public String getRemarks() {
        return remarks;
    }

    /** Gets the database schema. */
    @NotNull public SchemaInfo getSchema() {
        return schema;
    }

    @NotNull @Override String getQualification() {
        return schema.getFullName();
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3099561832386790624L;
}  // end class SchemaObject
