
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.context;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.impl.java.InterfaceGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.collections.Seq;
import tekgenesis.field.ModelField;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.type.ArrayType;
import tekgenesis.type.InterfaceType;
import tekgenesis.type.Kind;

import static tekgenesis.codegen.common.MMCodeGenConstants.COMMON_SUPPRESSED_WARNINGS;
import static tekgenesis.codegen.impl.java.ClassGenerator.MODIFICATION_WARNING_LINE_1;
import static tekgenesis.codegen.impl.java.ClassGenerator.MODIFICATION_WARNING_LINE_2_WITHOUT_SUBCLASS;

/**
 * Class to manage the generation of the interface type.
 */
public class InterfaceTypeCodeGenerator extends InterfaceGenerator implements MMCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    protected final StructType type;

    //~ Constructors .................................................................................................................................

    /** Create an ContextCodeGenerator. */
    public InterfaceTypeCodeGenerator(JavaCodeGenerator cg, @NotNull final StructType type) {
        super(cg, type.getName());
        this.type = type;
    }

    //~ Methods ......................................................................................................................................

    @Override public String getSourceName() {
        return type.getSourceName();
    }

    protected void populate() {
        withComments("Generated interface for context: " + type.getName() + ".");
        withComments(MODIFICATION_WARNING_LINE_1);
        withComments(MODIFICATION_WARNING_LINE_2_WITHOUT_SUBCLASS);
        suppressWarnings(COMMON_SUPPRESSED_WARNINGS);

        final Seq<String> superTypes = type.getSuperTypes().map(StructType::getFullName);
        if (superTypes.isEmpty()) withInterfaces(InterfaceType.class.getName());
        else {
            for (final String superType : superTypes)
                withInterfaces(superType);
        }

        addMethods();

        super.populate();
    }

    private void addMethods() {
        for (final ModelField field : type.getChildren())
            addMethods((TypeField) field);
    }

    private void addMethods(@NotNull final TypeField field) {
        final String  name     = field.getName();
        final String  t        = getImplementationClassName(field);
        final boolean required = field.isRequired();

        final Method setter = setter(name, type.getImplementationClassName());
        setter.boxedNotNull();
        setter.arg(name, t).required(field.isRequired());
        setter.withSetterComments(name);
        setter.asInterfaceMethod();

        getter(name, t).asInterfaceMethod().withGetterComments(name).required(required);
    }

    private String getImplementationClassName(TypeField field) {
        if (field.getType().getKind() == Kind.ARRAY)
            return generic(List.class, ((ArrayType) field.getType()).getElementType().getImplementationClassName());
        else return field.getImplementationClassName();
    }
}
