
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.code;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static tekgenesis.code.RefAccess.ensureBind;

/**
 * An operation representing the access to a isReadOnly expression.
 */
public class ReadOnlyAccess implements Code {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private transient BoundRef<Boolean> readOnlyRef;

    //~ Constructors .................................................................................................................................

    /** Creates an instance of ReadOnlyAccess class. */

    public ReadOnlyAccess() {
        readOnlyRef = null;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Binds the value of the readOnly in the form model to the readOnlyRef atribute.
     *
     * @param   binder
     *
     * @return  void
     */
    @Override public void bind(Binder binder) {
        readOnlyRef = binder.bindReadOnly();
    }

    @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public String toString() {
        return "isReadOnly()";
    }

    /**
     * Get the Instruction of the ReadOnlyAccess.
     *
     * @return  Instruction
     */
    @Override public Instruction getInstruction() {
        return Instruction.IS_READ_ONLY;
    }

    /**
     * Get permission Ref name.
     *
     * @return  BoundRef<Boolean>
     */
    @NotNull BoundRef<Boolean> getRef() {
        return ensureBind(readOnlyRef);
    }
}  // end class ReadOnlyAccess
