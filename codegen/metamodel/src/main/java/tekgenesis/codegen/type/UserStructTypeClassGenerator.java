
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.type;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.CodeGeneratorConstants;
import tekgenesis.codegen.entity.UserClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.collections.ImmutableSet;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.type.ArrayType;
import tekgenesis.type.Kind;

import static tekgenesis.codegen.type.StructTypeCodeGenerator.typeConstructorComment;
import static tekgenesis.common.core.Strings.quoted;
import static tekgenesis.common.core.Tuple.tuple;

/**
 * Class to manage the generation of the User class of Struct types code.
 */
public class UserStructTypeClassGenerator extends UserClassGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final StructType type;

    //~ Constructors .................................................................................................................................

    /** Create an User Class CodeGenerator. */
    public UserStructTypeClassGenerator(JavaCodeGenerator cg, @NotNull StructType model, @NotNull String codeBasePackageName) {
        super(cg, model, codeBasePackageName, model.getName());
        type = model;
    }

    //~ Methods ......................................................................................................................................

    @Override protected void populate() {
        super.populate();
        generateConstructorMatchingSuper();
    }

    private Argument argument(Constructor c, TypeField f) {
        return c.arg(f.getName(), getImplementationClassName(f))
               .withAnnotation(extractImport(CodeGeneratorConstants.JSON_PROPERTY), quoted(f.getName()))
               .required(f.isRequired());
    }

    private void generateConstructorMatchingSuper() {
        final List<TypeField>      superArgs     = getSuperTypeArguments();
        final List<TypeField>      compositeArgs = getCompositeArguments();
        final ImmutableSet<String> args          = type.getArgs();

        if (args.isEmpty() && superArgs.isEmpty() && compositeArgs.isEmpty()) return;

        final Constructor c = constructor().withAnnotation(extractImport(CodeGeneratorConstants.JSON_CREATOR))
                              .withComments(typeConstructorComment(type));

        final List<String> allArgs = new ArrayList<>();

        superArgs.forEach((field) -> {
            allArgs.add(field.getName());
            argument(c, field);
        });

        compositeArgs.forEach((field) -> {
            allArgs.add(field.getName());
            argument(c, field);
        });

        allArgs.addAll(args);

        allArgs.forEach(a -> type.getField(a).map(f -> argument(c, f)));

        c.invokeSuper(allArgs);
    }

    @NotNull private List<TypeField> getCompositeArguments() {
        final List<TypeField> arguments = new ArrayList<>();
        //J-
        type.getChildren().filter((a)->a.getType() instanceof StructType && !type.getArgs().exists((args)->args.equals(a.getName())) && a.getType().isType())
                .map(typeField -> tuple(typeField,(StructType)typeField.getType()))
                .filter(arg-> arg.first().isRequired() && !arg.second().getArgs().isEmpty())
                .forEach((t)-> t.second().getArgs().map(a -> t.second().getField(a).get()).forEach(arguments::add));
        //J+
        return arguments;
    }

    private String getImplementationClassName(TypeField field) {
        if (field.getType().getKind() == Kind.ARRAY)
            return generic(List.class, ((ArrayType) field.getType()).getElementType().getImplementationClassName());
        else return field.getImplementationClassName();
    }

    @NotNull private List<TypeField> getSuperTypeArguments() {
        final List<TypeField> superArgs = new ArrayList<>();
        //J-
        type.getSuperTypes()
                .forEach((parent) -> parent.getArgs()
                                        .forEach((arg) -> parent.getField(arg).ifPresent(superArgs::add)));
        //J+
        return superArgs;
    }
}  // end class UserStructTypeClassGenerator
