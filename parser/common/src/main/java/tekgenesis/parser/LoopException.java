
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser;

import org.jetbrains.annotations.NotNull;

/**
 * An exception to mark that the parser is looping and not consuming input.
 */
public class LoopException extends RuntimeException {

    //~ Instance Fields ..............................................................................................................................

    private final Position position;

    //~ Constructors .................................................................................................................................

    /**
     * Creates a Loop Exception.
     *
     * @param  position  The position where the parser is looping
     */
    public LoopException(@NotNull Position position) {
        super("Loop parsing");
        this.position = position;
    }

    //~ Methods ......................................................................................................................................

    /** returns he position where the parser is looping. */
    @NotNull public Position getPosition() {
        return position;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -8942612816530015609L;
}
