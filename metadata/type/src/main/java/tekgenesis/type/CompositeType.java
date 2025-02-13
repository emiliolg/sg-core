
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import tekgenesis.common.collections.Seq;
import tekgenesis.field.TypeField;

/**
 * Implemented by StructType and Entity.
 */
public interface CompositeType extends MetaModel {

    //~ Methods ......................................................................................................................................

    /**
     * Returns the set of simple (Non composite) fields for the present type. Only the PrimaryKey
     * Fields are included for Entities
     */
    Seq<TypeField> retrieveSimpleFields();
}
