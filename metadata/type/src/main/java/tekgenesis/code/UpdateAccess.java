
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
 * An operation representing the access to a isUpdate expression.
 */
public class UpdateAccess implements Code {

    //~ Instance Fields ..............................................................................................................................

    @Nullable private transient BoundRef<Boolean> updateRef;

    //~ Constructors .................................................................................................................................

    /** Creates an instance of UpdateAccess class. */

    public UpdateAccess() {
        updateRef = null;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Binds the value of the update in the form model to the updateRef atribute.
     *
     * @param   binder
     *
     * @return  void
     */
    @Override public void bind(Binder binder) {
        updateRef = binder.bindUpdate();
    }

    @Override public String toString() {
        return "isUpdate()";
    }

    /**
     * Get the Instruction of the UpdateAccess.
     *
     * @return  Instruction
     */
    @Override public Instruction getInstruction() {
        return Instruction.IS_UPDATE;
    }

    /**
     * Get permission Ref name.
     *
     * @return  BoundRef<Boolean>
     */
    @NotNull BoundRef<Boolean> getRef() {
        return ensureBind(updateRef);
    }
}  // end class UpdateAccess
