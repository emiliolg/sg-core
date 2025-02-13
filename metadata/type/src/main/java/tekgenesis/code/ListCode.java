
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
 * Code representing a list.
 */
public class ListCode implements Code {

    //~ Instance Fields ..............................................................................................................................

    private final int size;

    //~ Constructors .................................................................................................................................

    /** List code constructor. */
    public ListCode(final int size) {
        this.size = size;
    }

    //~ Methods ......................................................................................................................................

    @Override public void bind(Binder binder) {}

    @Override public String toString() {
        return getInstruction() + "(" + getSize() + ")";
    }

    @Override public Instruction getInstruction() {
        return Instruction.LIST;
    }

    /** Get list size. */
    public int getSize() {
        return size;
    }
}
