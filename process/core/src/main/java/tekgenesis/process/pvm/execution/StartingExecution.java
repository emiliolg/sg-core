
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm.execution;

import java.io.Serializable;

import tekgenesis.process.pvm.Activity;

/**
 * An execution that was just started (Instance not created yet).
 */
public class StartingExecution implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private final Activity selectedInitial;

    //~ Constructors .................................................................................................................................

    /** Creates the execution. */
    public StartingExecution(Activity selectedInitial) {
        this.selectedInitial = selectedInitial;
    }

    //~ Methods ......................................................................................................................................

    /** Return the initial activity to be executed. */
    public Activity getInitial() {
        return selectedInitial;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 61712810555029866L;
}
