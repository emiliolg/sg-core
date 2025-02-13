
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.java;

import static tekgenesis.common.util.JavaReservedWords.INTERFACE;

/**
 * A Generator for Interfaces.
 */
public class InterfaceGenerator extends JavaItemGenerator<InterfaceGenerator> {

    //~ Constructors .................................................................................................................................

    protected InterfaceGenerator(JavaCodeGenerator cg, String name) {
        super(cg, name, INTERFACE);
    }

    InterfaceGenerator(JavaItemGenerator<?> parent, String name) {
        super(parent, name, INTERFACE);
    }

    //~ Methods ......................................................................................................................................

    @Override protected boolean isInterface() {
        return true;
    }
}
