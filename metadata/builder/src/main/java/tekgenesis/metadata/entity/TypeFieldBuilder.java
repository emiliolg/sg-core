
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import tekgenesis.type.Type;

/**
 * Builder for Type Fields.
 */
public class TypeFieldBuilder extends CompositeFieldBuilder<TypeFieldBuilder> {

    //~ Constructors .................................................................................................................................

    /** Create a TypeDefBuilder. */
    public TypeFieldBuilder(String name, Type type) {
        super(name, type);
    }
}
