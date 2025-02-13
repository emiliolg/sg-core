
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

/**
 * Interface for Expression code element.
 */
public interface Code {

    //~ Methods ......................................................................................................................................

    /** Binds the code given the binder. */
    void bind(Binder binder);

    /** Returns the associated Instruction. */
    Instruction getInstruction();
}
