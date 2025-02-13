
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.parser;

/**
 * The listener interface for receiving parsing errors.
 */
public interface ParserErrorListener {

    //~ Methods ......................................................................................................................................

    /** Invoked when an error occurs. */
    void error(Position position, String message);
}
