
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.process.pvm.impl;

import tekgenesis.common.logging.Logger;
import tekgenesis.process.pvm.Transition;
import tekgenesis.process.pvm.execution.InterpretableExecution;

/**
 * An outgoing execution.
 */
class OutgoingExecution {

    //~ Instance Fields ..............................................................................................................................

    private InterpretableExecution outgoingExecution;
    private final Transition       outgoingTransition;

    //~ Constructors .................................................................................................................................

    /** Create an outgoing execution. */
    public OutgoingExecution(InterpretableExecution outgoingExecution, Transition outgoingTransition) {
        this.outgoingExecution  = outgoingExecution;
        this.outgoingTransition = outgoingTransition;
    }

    //~ Methods ......................................................................................................................................

    /** Take the transition. */
    public void take() {
        if (outgoingExecution.getReplacedBy() != null) outgoingExecution = outgoingExecution.getReplacedBy();
        if (!outgoingExecution.isDeleteRoot()) outgoingExecution.take(outgoingTransition);
        else log.info("Not taking transition '" + outgoingTransition + "', outgoing execution has ended.");
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger log = Logger.getLogger(OutgoingExecution.class);
}
