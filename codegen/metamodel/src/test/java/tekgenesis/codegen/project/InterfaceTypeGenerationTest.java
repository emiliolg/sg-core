
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.project;

import java.io.File;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import tekgenesis.codegen.context.InterfaceTypeCodeGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.entity.StructBuilder;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateFieldException;
import tekgenesis.metadata.exception.InvalidFieldNameException;
import tekgenesis.metadata.exception.InvalidTypeException;
import tekgenesis.type.Types;

import static tekgenesis.codegen.project.StructTypeGenerationTest.field;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.QName.qualify;
import static tekgenesis.common.tools.test.Tests.checkDiff;

/**
 * Test Interface Type Code Generator;
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class InterfaceTypeGenerationTest {

    //~ Instance Fields ..............................................................................................................................

    private final File goldenDir = new File("codegen/metamodel/src/test/data");

    private final File outputDir = new File("target/codegen/metamodel/test-output");

    //~ Methods ......................................................................................................................................

    @Test public void simpleInterfaceType()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.SimpleInterface");
        checkFile(createSimpleInterfaceType(name, "a", "b", "c"));
    }

    @Test public void simpleInterfaceWithMultipleExtendsType()
        throws BuilderException
    {
        final QName name = createQName("tekgenesis.test.InterfaceWithMultipleExtends");

        final StructBuilder builder = StructBuilder.struct(name);

        builder.withSuperType(createSimpleInterfaceType(QName.createQName("tekgenesis.test.ImaginaryA"), "a", "d"));
        builder.withSuperType(createSimpleInterfaceType(QName.createQName("tekgenesis.test.ImaginaryB"), "b"));

        addField(builder, "a");
        addField(builder, "b");
        addField(builder, "c");

        checkFile(builder.asInterface().build());
    }

    private void addField(StructBuilder builder, String field)
        throws DuplicateFieldException, InvalidFieldNameException, InvalidTypeException
    {
        builder.addField(field(field, Types.stringType(), "Field " + field));
    }

    private void checkFile(@NotNull StructType type) {
        final JavaCodeGenerator          cg   = new JavaCodeGenerator(outputDir, type.getDomain());
        final InterfaceTypeCodeGenerator base = new InterfaceTypeCodeGenerator(cg, type);
        base.generate();

        final String fileName = qualify(type.getDomain(), type.getImplementationClassName()).replaceAll("\\.", File.separator);
        checkDiff(new File(outputDir, fileName + Constants.JAVA_EXT), new File(goldenDir, fileName + ".j"));
    }

    private StructType createSimpleInterfaceType(QName name, String... fields)
        throws DuplicateFieldException, InvalidFieldNameException, InvalidTypeException
    {
        final StructBuilder builder = StructBuilder.struct(name);
        for (final String field : fields)
            addField(builder, field);
        return builder.asInterface().build();
    }
}  // end class InterfaceTypeGenerationTest
