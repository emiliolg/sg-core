
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
import java.io.IOException;

import org.junit.Test;

import tekgenesis.codegen.entity.EnumCodeGenerator;
import tekgenesis.codegen.entity.ExceptionEnumCodeGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.core.Constants;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.common.util.Files;
import tekgenesis.metadata.entity.EnumBuilder;
import tekgenesis.type.EnumType;

import static java.lang.Boolean.getBoolean;

import static tekgenesis.metadata.entity.EnumBuilder.enumType;
import static tekgenesis.type.Modifier.EXCEPTION;

/**
 * Test Java Enum Code generator.
 */
public class EnumGenerationTest {

    //~ Instance Fields ..............................................................................................................................

    private final File goldenDir = new File("codegen/metamodel/src/test/data");
    private final File outputDir = new File("target/codegen/metamodel/test-output");

    //~ Methods ......................................................................................................................................

    @Test public void commonEnum()
        throws IOException
    {
        final EnumBuilder builder = enumType("tekgenesis.test.CommonEnum", "tekgenesis.test", "CommonEnum").value("ONE", "One").value("TWO", "Two");

        checkEnumFile(builder.build());
    }

    @Test public void exceptionEnum()
        throws IOException
    {
        final EnumBuilder builder = enumType("tekgenesis.test.EnumTestException", "tekgenesis.test", "EnumTestException").withModifier(EXCEPTION)
                                    .value("ONE", "One")
                                    .value("TWO", "Two");

        checkEnumFile(builder.build());
    }

    private void checkEnumFile(EnumType enumType)
        throws IOException
    {
        checkEnumFile(outputDir, goldenDir, enumType);
    }

    //~ Methods ......................................................................................................................................

    static void checkEnumFile(File output, File golden, EnumType enumType)
        throws IOException
    {
        final JavaCodeGenerator cg   = new JavaCodeGenerator(output, enumType.getDomain());
        final EnumCodeGenerator base = new EnumCodeGenerator(cg, enumType, false);
        base.generate();

        final String fileName = enumType.getImplementationClassName().replaceAll("\\.", File.separator);

        final File outFile    = new File(output, fileName + Constants.JAVA_EXT);
        final File goldenFile = new File(golden, fileName + ".j");

        if (getBoolean("golden.force")) Files.copy(outFile, goldenFile, true);
        else Tests.checkDiff(outFile, goldenFile);

        if (enumType.isException()) {
            final ExceptionEnumCodeGenerator excp = new ExceptionEnumCodeGenerator(cg, enumType);
            excp.generate();

            final String exFileName = enumType.getImplementationClassName()
                                      .replaceAll("\\.", File.separator)
                                      .replace(enumType.getName(), excp.getSourceName());

            final File exOutFile    = new File(output, exFileName + Constants.JAVA_EXT);
            final File exGoldenFile = new File(golden, exFileName + ".j");

            if (getBoolean("golden.force")) Files.copy(exOutFile, exGoldenFile, true);
            else Tests.checkDiff(exOutFile, exGoldenFile);
        }
    }
}  // end class EnumGenerationTest
