
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

import tekgenesis.codegen.form.FormBaseCodeGenerator;
import tekgenesis.codegen.form.FormCodeGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.core.Constants;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.common.util.Files;
import tekgenesis.metadata.common.ModelLinkerImpl;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.EntityBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.DuplicateFieldException;
import tekgenesis.metadata.exception.InvalidFieldNameException;
import tekgenesis.metadata.exception.InvalidTypeException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormBuilderPredefined;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.Types;

import static java.lang.Boolean.getBoolean;

import static tekgenesis.codegen.common.MMCodeGenConstants.BASE;
import static tekgenesis.metadata.entity.EntityBuilder.*;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;
import static tekgenesis.type.Types.*;

/**
 * Test Java Form Code Generator;
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class FormGenerationTest {

    //~ Instance Fields ..............................................................................................................................

    private final File goldenDir = new File("codegen/metamodel/src/test/data");

    private final File            outputDir  = new File("target/codegen/metamodel/test-output");
    private final ModelRepository repository = new ModelRepository();

    //~ Methods ......................................................................................................................................

    @Test public void attrNameDiffersPKBinding()
        throws BuilderException, IOException
    {
        final Entity e = builderForSomeEntity().build();
        repository.add(e);

        final Form f = form("", "tekgenesis.test", "AttrDiffersPKForm").withBinding(e)
                       .children(field("").id("altoInt").withType(intType()).withBinding(e.getAttribute("oneInt").get()),
                    check("").id("mgBool").withType(booleanType()).withBinding(e.getAttribute("someBool").get()),
                    field("").id("martinsString").withType(stringType()).withBinding(e.getAttribute("someString").get()))
                       .withRepository(repository)
                       .build();
        repository.add(f);

        checkFormFile(f);
    }

    @Test public void boundTableDifferName()
        throws BuilderException, IOException
    {
        final Entity someOther = entity("tekgenesis.test", "SomeOtherEntity").primaryKey("someEntity")
                                 .searchable()
                                 .fields(reference("someEntity", "tekgenesis.test.SomeEntity").id("someEntity"),
                    EntityBuilder.integer("quantity").id("quantity"),
                    EntityBuilder.integer("discount").id("discount"))
                                 .build();

        final Entity some = entity("tekgenesis.test", "SomeEntity").primaryKey("oneInt", "someString")
                            .searchable()
                            .fields(EntityBuilder.integer("oneInt").id("oneInt"),
                    EntityBuilder.integer("someInt").id("someInt"),
                    bool("someBool").id("someBool"),
                    string("someString").id("someString"),
                    reference("someOtherEntities", "tekgenesis.test.SomeOtherEntity").multiple().id("someOtherEntities"))
                            .build();
        repository.add(some);
        repository.add(someOther);

        new ModelLinkerImpl(repository).link(some);
        new ModelLinkerImpl(repository).link(someOther);

        final Form f = form("", "tekgenesis.test", "BoundTableDifferNameForm").withBinding(some)
                       .children(field("").id("altoInt").withType(intType()).withBinding(some.getAttribute("oneInt").get()),
                    check("").id("mgBool").withType(booleanType()).withBinding(some.getAttribute("someBool").get()),
                    field("").id("martinsString").withType(stringType()).withBinding(some.getAttribute("someString").get()),
                    FormBuilderPredefined.table("")
                                         .withBinding(some.getAttribute("someOtherEntities").get())
                                         .id("someOtherEntities5")
                                         .children(
                                             field("").id("quantity").withType(intType()).withBinding(someOther.getAttribute("quantity").get())))
                       .withRepository(repository)
                       .build();
        repository.add(f);

        checkFormFile(f);
    }  // end method boundTableDifferName

    @Test public void complexForm()
        throws BuilderException, IOException
    {
        final Form f = form("", "tekgenesis.test", "ComplexForm")              //
                       .children(field("My Int").id("myInt").withType(intType()).onChange("somethingHappened"),
                    subform("SubForm", "tekgenesis.test.PrimitivesForm").id("mySubForm"),
                    button("Button").id("myButton").onClick("somethingHappened"))  //
                       .withRepository(repository).build();

        repository.add(f);

        checkFormFile(f);
    }

    @Test public void fullPKBinding()
        throws BuilderException, IOException
    {
        final Entity e = builderForSomeEntity().build();
        repository.add(e);

        final Form f = form("", "tekgenesis.test", "FullPKForm").withBinding(e)
                       .children(field("").id("oneInt").withType(intType()).withBinding(e.getAttribute("oneInt").get()),
                    check("").id("someBool").withType(booleanType()).withBinding(e.getAttribute("someBool").get()),
                    field("").id("someString").withType(stringType()).withBinding(e.getAttribute("someString").get()))
                       .withRepository(repository)
                       .build();
        repository.add(f);

        checkFormFile(f);
    }

    // ademas generar el test ui.
    @Test public void iframe()
        throws BuilderException, IOException
    {
        final Form f = form("", "tekgenesis.test", "IFrameForm")  //
                       .children(field("A String").id("aString").withType(stringType()).onChange("aStringChange"),
                    FormBuilderPredefined.iFrame("My Frame")  //
                    .id("myFrame"))                       //
                       .withRepository(repository).build();

        repository.add(f);

        checkFormFile(f);
    }

    @Test public void primitiveTypes()
        throws BuilderException, IOException
    {
        final Form f = form("", "tekgenesis.test", "PrimitivesForm")     //
                       .children(field("My Int").id("myInt").withType(intType()),
                    field("My Real").id("myReal").withType(realType()),
                    field("My Decimal").id("myDecimal").withType(Types.decimalType(10, 2)),
                    field("My String").id("myString").withType(stringType()),
                    dateBox("My Date").id("myDate").withType(dateType()),
                    combo("My Combo").id("myCombo").withType(stringType()))  //
                       .withRepository(repository).build();

        repository.add(f);

        checkFormFile(f);
    }

    @Test public void semiPKBinding()
        throws BuilderException, IOException
    {
        final Entity e = builderForSomeEntity().build();
        repository.add(e);

        final Form f = form("", "tekgenesis.test", "SemiPKForm").withBinding(e)
                       .children(field("").id("oneInt").withType(intType()).withBinding(e.getAttribute("oneInt").get()),
                check("").id("someBool").withType(booleanType()).withBinding(e.getAttribute("someBool").get()))
                       .withRepository(repository)
                       .build();
        repository.add(f);

        checkFormFile(f);
    }

    @Test public void table()
        throws BuilderException, IOException
    {
        final Form f = form("", "tekgenesis.test", "TableForm")               //
                       .children(
                    FormBuilderPredefined.table("My Items")                   //
                    .id("myItem")                                             //
                                         .children(field("My Int").id("myInt").withType(intType()),
                                             field("My String").id("myString").withType(stringType()),
                                             combo("My Combo").id("myCombo").withType(stringType())))  //
                       .withRepository(repository).build();

        repository.add(f);

        checkFormFile(f);
    }

    @Test public void tableWithCallbacks()
        throws BuilderException, IOException
    {
        final Form f = form("", "tekgenesis.test", "TableWithCallbacksForm")  //
                       .children(field("Outer Int").id("outInt").withType(intType()).onChange("outerIntChange"),
                    FormBuilderPredefined.table("My Items")                   //
                    .id("myItem")                                             //
                                         .children(field("My Int").id("myInt").withType(intType()).onChange("tableIntChange"),
                                             field("My String").id("myString").withType(stringType()).onChange("tableStringChange"),
                                             combo("My Combo").id("myCombo").withType(stringType())))  //
                       .withRepository(repository).build();

        repository.add(f);

        checkFormFile(f);
    }

    private EntityBuilder builderForSomeEntity()
        throws DuplicateFieldException, InvalidFieldNameException, InvalidTypeException
    {
        return entity("tekgenesis.test", "SomeEntity").primaryKey("oneInt", "someString")
               .fields(EntityBuilder.integer("oneInt").id("oneInt"),
                EntityBuilder.integer("someInt").id("someInt"),
                bool("someBool").id("someBool"),
                string("someString").id("someString"));
    }

    private void checkFormFile(Form f)
        throws IOException
    {
        checkFormFile(repository, outputDir, goldenDir, f);
    }

    //~ Methods ......................................................................................................................................

    static void checkFormFile(ModelRepository r, File output, File golden, Form f)
        throws IOException
    {
        final JavaCodeGenerator     cg   = new JavaCodeGenerator(output, f.getDomain());
        final FormBaseCodeGenerator base = new FormBaseCodeGenerator(cg, f, r.getModel(f.getBinding()), r);
        base.generate();
        final FormCodeGenerator user = new FormCodeGenerator(cg, f, base);

        user.generate();

        final String fileName = f.getImplementationClassFullName().replaceAll("\\.", File.separator) + BASE;

        final File outFile    = new File(output, fileName + Constants.JAVA_EXT);
        final File goldenFile = new File(golden, fileName + ".j");

        if (getBoolean("golden.force")) Files.copy(outFile, goldenFile, true);
        else Tests.checkDiff(outFile, goldenFile);
    }
}  // end class FormGenerationTest
