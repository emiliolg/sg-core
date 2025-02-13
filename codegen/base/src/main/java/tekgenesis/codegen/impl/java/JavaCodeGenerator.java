
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.impl.java;

import java.io.File;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.CodeGenerator;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;

import static tekgenesis.codegen.CodeGeneratorConstants.DEFAULT_NOT_NULL_ANNOTATION;
import static tekgenesis.codegen.CodeGeneratorConstants.DEFAULT_NULLABLE_ANNOTATION;

/**
 * A Generic generator for Java Code.
 */
@SuppressWarnings({ "WeakerAccess", "ClassEscapesDefinedScope" })
public class JavaCodeGenerator extends CodeGenerator<JavaArtifactGenerator> {

    //~ Instance Fields ..............................................................................................................................

    private String notNullAnnotation;
    private String nullableAnnotation;

    //~ Constructors .................................................................................................................................

    /**
     * Creates a Code generator, that will generate source files in the specified root directory It
     * initialize the <code>package</code> for the items generated to the specified one.
     */
    public JavaCodeGenerator(@NotNull File rootDir, @NotNull String packageName) {
        super(packageName, rootDir);
        notNullAnnotation  = DEFAULT_NOT_NULL_ANNOTATION;
        nullableAnnotation = DEFAULT_NULLABLE_ANNOTATION;
    }

    //~ Methods ......................................................................................................................................

    /**
     * Create a new Artifact Generator for Java code.
     *
     * @param   name  The name
     *
     * @return  JavaArtifactGenerator
     */
    @Override public JavaArtifactGenerator newArtifactGenerator(String name) {
        return new JavaArtifactGenerator(getRootDir(), getCurrentPackage(), name, notNullAnnotation, nullableAnnotation);
    }

    /** Adds a new class to be generated. */
    public ClassGenerator newClass(@NotNull String name) {
        return new ClassGenerator(this, name);
    }

    /** Adds a new enum to be generated. */
    public EnumGenerator newEnum(@NotNull String name, @NotNull String... enumConstants) {
        return newEnum(name, ImmutableList.fromArray(enumConstants));
    }
    /** Adds a new enum to be generated. */
    public EnumGenerator newEnum(@NotNull String name, @NotNull Seq<String> enumConstants) {
        return new EnumGenerator(this, name, enumConstants);
    }

    /** Adds a new interface to be generated. */
    public InterfaceGenerator newInterface(@NotNull String name) {
        return new InterfaceGenerator(this, name);
    }
    /** Adds a value class to be generated. */
    public ClassGenerator newValueClass(@NotNull String name) {
        return new ValueClassGenerator(this, name);
    }

    /** Change the default annotation package. */
    public CodeGenerator<?> withNotNullAnnotations(String notNull, String nullable) {
        notNullAnnotation  = notNull;
        nullableAnnotation = nullable;
        return this;
    }

    /** Sets the package for items generated. */
    public CodeGenerator<?> withPackage(@NotNull String name) {
        setCurrentPackage(name);
        return this;
    }
}  // end class JavaCodeGenerator
