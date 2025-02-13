
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.handler;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.metadata.handler.Handler;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;

/**
 * Class to manage the generation of the Form final (User editable) code.
 */
public class HandlerCodeGenerator extends ClassGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final HandlerBaseCodeGenerator base;

    //~ Constructors .................................................................................................................................

    /** Create a Form code generator. */
    public HandlerCodeGenerator(JavaCodeGenerator generator, Handler handler, HandlerBaseCodeGenerator base) {
        super(generator, handler.getImplementationClassName());
        withSuperclass(handler.getFullName() + BASE).withComments("User class for Handler: " + handler.getName());
        this.base = base;
    }

    //~ Methods ......................................................................................................................................

    @Override protected void populate() {
        generateConstructor();

        for (final Method method : base.getAbstractMethods()) {
            final Method concrete = method.overrideIn(this, false);
            concrete.return_("notImplemented()");
        }

        super.populate();
    }

    private void generateConstructor() {
        final Constructor constructor = constructor().asPackage();
        constructor.arg(FACTORY, FACTORY_CLASS).notNull();
        constructor.invokeSuper(FACTORY);
    }
}
