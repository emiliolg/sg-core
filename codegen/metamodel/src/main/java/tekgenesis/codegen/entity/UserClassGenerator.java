
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.entity;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.type.ModelType;

import static tekgenesis.codegen.common.MMCodeGenConstants.BASE;

/**
 * Class to manage the generation of the User class code.
 */
public class UserClassGenerator extends ClassGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final String codeBasePackageName;

    private final String name;

    //~ Constructors .................................................................................................................................

    /** Create an User Class CodeGenerator. */
    public UserClassGenerator(JavaCodeGenerator cg, @NotNull final ModelType model, @NotNull String codeBasePackageName, String className) {
        super(cg, className);
        name                     = className;
        this.codeBasePackageName = codeBasePackageName;
    }

    //~ Methods ......................................................................................................................................

    protected void populate() {
        final String baseClassName = codeBasePackageName + "." + name + BASE;
        withSuperclass(baseClassName).withComments("User class for Model: " + name);

        super.populate();
    }
}
