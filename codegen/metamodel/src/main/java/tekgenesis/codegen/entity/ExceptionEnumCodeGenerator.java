
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

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.exception.ApplicationException;
import tekgenesis.type.EnumType;

import static tekgenesis.codegen.CodeGeneratorConstants.PREDEFINED_CLASS;
import static tekgenesis.codegen.common.MMCodeGenConstants.ARGS;
import static tekgenesis.codegen.common.MMCodeGenConstants.OBJECT_ARRAY;
import static tekgenesis.codegen.entity.EnumCodeGenerator.innerExceptionName;
import static tekgenesis.common.core.Constants.CONSTRUCTOR_FOR;

/**
 * Class to generate ExceptionEnum's exception class.
 */
public class ExceptionEnumCodeGenerator extends ClassGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final EnumType enumType;

    //~ Constructors .................................................................................................................................

    /** Constructor for ExceptionEnum code generator. */
    public ExceptionEnumCodeGenerator(@NotNull JavaCodeGenerator codeGenerator, @NotNull EnumType model) {
        super(codeGenerator, innerExceptionName(model.getName()));
        enumType = model;

        withComments("Generated exception class for Enum: " + model.getName() + ".");
        withComments(MODIFICATION_WARNING_LINE_1);
        withComments(MODIFICATION_WARNING_LINE_2);

        asFinal().asPublic().withSuperclass(ApplicationException.class).withSerialVersionUID();

        final Constructor constructor = constructor();
        constructor.arg("ex", model.getName()).notNull().asFinal();
        constructor.arg(ARGS, OBJECT_ARRAY).notNull();
        constructor.invokeSuper("ex", ARGS);
        constructor.withComments(CONSTRUCTOR_FOR + getName());

        method("getException", model.getName()).notNull()
            .return_(invokeStatic(PREDEFINED_CLASS, "cast", "getEnumeration()"))
            .withComments("Returns the Exception as an Enumeration");
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return innerExceptionName(enumType.getName());
    }
}
