
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

import tekgenesis.common.core.QName;
import tekgenesis.metadata.exception.InvalidFieldNameException;

/**
 * Builder for IxEntities.
 */
public class IxEntityBuilder extends EntityBuilder {

    //~ Constructors .................................................................................................................................

    /** Create a Builder for IxEntities. */
    public IxEntityBuilder(String sourceName, @NotNull String pkg, @NotNull String schemaId, @NotNull String name, @NotNull String tableName) {
        super(sourceName, pkg, tableName, "", schemaId);
        databaseName(QName.createQName(name));
        primaryKeyDefault = false;
    }

    //~ Methods ......................................................................................................................................

    @Override boolean allowOptionalInPrimaryKey() {
        return true;
    }

    @Override void checkValidName(AttributeBuilder a)
        throws InvalidFieldNameException {}
}
