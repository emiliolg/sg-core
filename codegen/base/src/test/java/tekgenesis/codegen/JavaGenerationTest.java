
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.codegen.impl.java.JavaElement;
import tekgenesis.common.tools.test.Tests;

import static java.util.Arrays.asList;

/**
 * Test Code Generator;
 */

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class JavaGenerationTest {

    //~ Methods ......................................................................................................................................

    @Test public void constructorFromFields() {
        final String className = "Person";

        final JavaCodeGenerator g = new JavaCodeGenerator(outputDir, "tekgenesis.test");

        final ClassGenerator c = g.newClass(className).asPublic();

        final JavaElement.Field codeField = c.readOnlyProperty("code", Integer.TYPE);
        final JavaElement.Field nameField = c.property("name", String.class);

        c.constructor();
        c.constructor().args(codeField, nameField);
        c.generate();

        checkJavaFile(className);
    }
    @Test public void genericsAndAnnotations() {
        listOf(String.class);
        listOf(Integer.class);
    }

    @Test public void multipleGenerics() {
        final String className = "StringHashMap";

        final JavaCodeGenerator g = new JavaCodeGenerator(outputDir, "tekgenesis.test");

        final ClassGenerator c = g.newClass(className).asPublic();

        c.withSuperclass(c.generic(HashMap.class, String.class, Object.class));

        final JavaElement.Constructor cons = c.constructor();
        cons.arg("capacity", Integer.class).notNull().superArg();

        c.generate();

        checkJavaFile(className);
    }

    @Test public void simpleClass() {
        final String className = "Customer";

        final JavaCodeGenerator g = new JavaCodeGenerator(outputDir, "tekgenesis.test");

        final ClassGenerator c = g.newClass(className)
                                 .asPublic()
                                 .withInterfaces("java.io.Serializable")
                                 .withEquals(asList("code", "name"))
                                 .withHashCode("code", "name");

        c.field("code", "int");
        c.property("name", "java.lang.String").notNull();
        c.readOnlyProperty("type", "CustomerType").notNull();
        c.property("active", "boolean");

        c.constructor().asProtected();
        final JavaElement.Constructor cons = c.constructor();
        cons.arg("code", "int");
        cons.withBody("this.code = code;");
        final JavaElement.Method m = c.method("setCode");
        m.arg("code", String.class).notNull();
        m.withBody("this.code = code;");
        c.innerEnum("CustomerType", "CUSTOM", "STANDARD", "VIP");

        c.generate();

        checkJavaFile(className);
    }

    @Test public void simpleClassWithClassArgs() {
        final String className = "Customer2";

        final JavaCodeGenerator g = new JavaCodeGenerator(outputDir, "tekgenesis.test");

        final ClassGenerator c = g.newClass(className).asPublic().withSuperclass("CustomerBase").asSerializable();

        c.field("code", Integer.TYPE).asFinal();
        c.field("name", String.class).notNull();
        c.readOnlyProperty("type", "CustomerType").notNull();
        c.property("active", Boolean.TYPE);

        c.constructor().asProtected();
        final JavaElement.Constructor cons = c.constructor();
        cons.arg("code", Integer.TYPE);
        cons.withBody("this.code = code;");
        c.method("setCode").withBody("this.code = code;");
        c.innerEnum("CustomerType", "CUSTOM", "STANDARD", "VIP");
        c.generate();

        checkJavaFile(className);
    }

    @Test public void valueClass() {
        final String className = "Address";

        final JavaCodeGenerator g = new JavaCodeGenerator(outputDir, "tekgenesis.test");

        final ClassGenerator c = g.newValueClass(className).withComments("An Address class");

        c.field("street", "java.lang.String").notNull();
        c.field("number", "int");
        c.field("state", State.class);
        c.field("country", "java.lang.String").notNull();

        c.generate();
        checkJavaFile(className);
    }

    //~ Methods ......................................................................................................................................

    static void checkFile(String className, String ext, String genFileExt) {
        final String fileName = "tekgenesis/test/" + className;
        final File   f1       = new File(outputDir, fileName + ext);
        final File   f2       = new File(goldenDir, fileName + genFileExt);
        Tests.checkDiff(f1, f2);
    }

    private static void checkJavaFile(String fileName) {
        checkFile(fileName, javaExt, jExt);
    }

    private static void listOf(Class<?> clazz) {
        final String            className = clazz.getSimpleName() + "List";
        final JavaCodeGenerator g         = new JavaCodeGenerator(outputDir, "tekgenesis.test");

        final ClassGenerator c = g.newClass(className).asPublic();

        c.withSuperclass(c.generic(ArrayList.class, clazz));
        c.constructor().arg("size", Integer.class).superArg();
        c.constructor().arg("name", clazz).notNull();
        final JavaElement.Method get = c.method("get", clazz);
        get.arg("n", "int");
        get.boxedNotNull().return_("super.get(n)");
        c.generate();

        checkJavaFile(className);
    }

    //~ Static Fields ................................................................................................................................

    static final File         outputDir = new File("target/codegen/base/test-output");
    private static final File goldenDir = new File("codegen/base/src/test/data");

    private static final String javaExt = ".java";
    private static final String jExt    = ".j";

    //~ Enums ........................................................................................................................................

    enum State { CA, TX, NY, FL }
}  // end class JavaGenerationTest
