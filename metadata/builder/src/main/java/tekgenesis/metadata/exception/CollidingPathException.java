
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import org.jetbrains.annotations.Nullable;

/**
 * Exception to throw when a path collision is detected.
 */
public class CollidingPathException extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** Creates a new invalid path exception. */
    public CollidingPathException(String path, @Nullable String collision, String modelName) {
        super(String.format("Path '%s' collides with '%s'", path, collision), modelName);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5741411929254380781L;
}
