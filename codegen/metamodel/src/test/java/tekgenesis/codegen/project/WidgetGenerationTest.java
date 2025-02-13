
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

import tekgenesis.codegen.form.WidgetDefBaseCodeGenerator;
import tekgenesis.codegen.form.WidgetDefCodeGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.core.Constants;
import tekgenesis.common.tools.test.Tests;
import tekgenesis.common.util.Files;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.Types;

import static java.lang.Boolean.getBoolean;

import static tekgenesis.codegen.common.MMCodeGenConstants.BASE;
import static tekgenesis.codegen.project.FormGenerationTest.checkFormFile;
import static tekgenesis.metadata.form.UiModelTests.*;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;
import static tekgenesis.type.Types.*;

/**
 * Test Java widget code generation.
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class WidgetGenerationTest {

    //~ Instance Fields ..............................................................................................................................

    private final File goldenDir = new File("codegen/metamodel/src/test/data");

    private final File            outputDir  = new File("target/codegen/metamodel/test-output");
    private final ModelRepository repository = new ModelRepository();

    //~ Methods ......................................................................................................................................

    /** Test basic widget. */
    @Test public void basic()
        throws BuilderException, IOException
    {
        final WidgetDef w = widget("", "tekgenesis.test", "PrimitivesWidget")  //
                            .children(check("Bool").id("bool").withType(booleanType()),
                    field("Int").id("int").withType(intType()),
                    field("Int Optional").id("intOpt").withType(intType()).optional(),
                    field("Real").id("real").withType(realType()),
                    field("Real Optional").id("realOpt").withType(realType()).optional(),
                    field("Decimal").id("decimal").withType(Types.decimalType(10, 2)),
                    field("Decimal Optional").id("decimalOpt").withType(Types.decimalType(10, 2)).optional(),
                    field("String").id("string").withType(stringType()),
                    field("String Optional").id("stringOpt").withType(stringType()).optional(),
                    dateBox("Date").id("date").withType(dateType()),
                    dateBox("Date Optional").id("dateOpt").withType(dateType()).optional(),
                    datetimeBox("DateTime").id("datetime").withType(dateTimeType()),
                    datetimeBox("DateTime Optional").id("datetimeOpt").withType(dateTimeType()).optional()).withRepository(repository).build();

        repository.add(w);

        checkWidgetFile(w);
    }

    /** Test widget bound with entity. */
    @Test public void binding()
        throws BuilderException, IOException
    {
        final Entity    address = buildAddressEntity(repository);
        final WidgetDef w       = bindingAddressWidget(repository, address, "BindingAddressWidget");
        checkWidgetFile(w);
    }

    /** Test widget/form integration. */
    @Test public void integration()
        throws BuilderException, IOException
    {
        final Entity    address  = buildAddressEntity(repository);
        final Entity    customer = buildCustomerEntity(repository, address);
        final WidgetDef w        = bindingAddressWidget(repository, address, "AddressWidget");
        final Form      f        = bindingCustomerForm(repository, customer, w);

        checkWidgetFile(w);
        checkFormFile(repository, outputDir, goldenDir, f);
    }

    /** Test widget with table. */
    @Test public void withTable()
        throws BuilderException, IOException
    {
        final WidgetDef w = widget("", "tekgenesis.test", "TableWidget")  //
                            .children(                    //
                    field("").id("name"),                 //
                    table("").id("variants").children(    //
                             field("").id("variant"),     //
                             field("").id("price"),       //
                             check("").id("primary"))     //
                ).withRepository(repository).build();

        repository.add(w);

        checkWidgetFile(w);
    }

    private void checkWidgetFile(WidgetDef w)
        throws IOException
    {
        final JavaCodeGenerator          cg   = new JavaCodeGenerator(outputDir, w.getDomain());
        final WidgetDefBaseCodeGenerator base = new WidgetDefBaseCodeGenerator(cg, w, repository.getModel(w.getBinding()), repository);
        base.generate();
        final WidgetDefCodeGenerator user = new WidgetDefCodeGenerator(cg, w, base);

        user.generate();

        final String fileName = w.getImplementationClassFullName().replaceAll("\\.", File.separator) + BASE;

        final File outFile    = new File(outputDir, fileName + Constants.JAVA_EXT);
        final File goldenFile = new File(goldenDir, fileName + ".j");

        if (getBoolean("golden.force")) Files.copy(outFile, goldenFile, true);
        else Tests.checkDiff(outFile, goldenFile);
    }
}  // end class WidgetGenerationTest
