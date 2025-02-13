
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import java.util.EnumSet;

import tekgenesis.type.Modifier;

/**
 * Invalid modifiers exception.
 */
public class InvalidModifiersException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Exception's Constructor specifying the invalid modifiers to model.* */
    public InvalidModifiersException(EnumSet<Modifier> modifiers, String model) {
        super(String.format("Invalid modifiers '%s'", modifiers.toString()), model);
    }

    protected InvalidModifiersException(String msg, String modelName) {
        super(msg, modelName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 3089885231086122278L;
}
