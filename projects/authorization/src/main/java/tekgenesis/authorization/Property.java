
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.NotNull;

import tekgenesis.authorization.g.PropertyBase;
import tekgenesis.form.configuration.DynamicTypeConfiguration;

/**
 * User class for Entity: Property
 */
@SuppressWarnings("WeakerAccess")
public class Property extends PropertyBase {

    //~ Methods ......................................................................................................................................

    void configureDynamicType(@NotNull DynamicTypeConfiguration configuration) {
        switch (getType()) {
        case BOOLEAN:
            configuration.booleanType();
            break;
        case DATE:
            configuration.dateType();
            break;
        case INT:
            configuration.intType();
            break;
        case REAL:
            configuration.realType();
            break;
        default:
            configuration.stringType(MAX_PROPERTY_LENGTH);
            break;
        }
    }

    //~ Static Fields ................................................................................................................................

    protected static final int MAX_PROPERTY_LENGTH = 512;

    private static final long serialVersionUID = 9201067737499303464L;
}
