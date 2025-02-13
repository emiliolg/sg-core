
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
 * The code to execute a constant.
 */
public class Jump implements Code {

    //~ Instance Fields ..............................................................................................................................

    private final int         address;
    private final Instruction instruction;

    //~ Constructors .................................................................................................................................

    /** creates a Jump Instruction. */
    public Jump(Code code, int address) {
        instruction  = code.getInstruction();
        this.address = address;
    }

    //~ Methods ......................................................................................................................................

    @Override public void bind(Binder binder) {}

    @Override public String toString() {
        return instruction + "(" + address + ")";
    }

    /** return the address. */
    public int getAddress() {
        return address;
    }

    @Override public Instruction getInstruction() {
        return instruction;
    }
}
