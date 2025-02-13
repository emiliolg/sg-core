
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.handler;

import tekgenesis.field.TypeField;
import tekgenesis.type.Type;

/**
 * Route post method body.
 */
class Body extends TypeField {

    //~ Constructors .................................................................................................................................

    private Body(Type type) {
        super("body", type);
    }

    //~ Methods ......................................................................................................................................

    boolean isDefined() {
        return !getType().isNull();
    }

    //~ Methods ......................................................................................................................................

    static Body fromType(Type type) {
        return new Body(type);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 7571455293128062015L;
}
