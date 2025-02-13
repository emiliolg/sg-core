
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

import tekgenesis.codegen.form.ExternalFormCodeGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.core.Constants;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.EntityBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.tools.test.Tests.checkDiff;
import static tekgenesis.field.FieldOption.SIGNED;
import static tekgenesis.metadata.entity.EntityBuilder.*;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.internal;

/**
 * Test Java External Form Code Generator;
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "ClassWithTooManyMethods" })
public class ExternalFormGenerationTest {

    //~ Instance Fields ..............................................................................................................................

    private final File goldenDir = new File("codegen/metamodel/src/test/data");

    private final File outputDir = new File("target/codegen/metamodel/test-output");

    private final ModelRepository rep = new ModelRepository();

    //~ Methods ......................................................................................................................................

    public void checkGeneration(@NotNull final Form f) {
        final JavaCodeGenerator         cg   = new JavaCodeGenerator(outputDir, f.getDomain());
        final ExternalFormCodeGenerator base = new ExternalFormCodeGenerator(cg, f, "darpa", rep);
        base.generate();

        final String fileName = f.getImplementationClassFullName().replaceAll("\\.", File.separator);
        checkDiff(new File(outputDir, fileName + Constants.JAVA_EXT), new File(goldenDir, fileName + ".j"));
    }

    @Test public void testExternalFormCodeGenerationDefaultKey()
        throws BuilderException
    {
        final EntityBuilder f = entity("tekgenesis.test", "FirstDefault");
        f.addField(string("a"));
        f.addField(string("b", 10));

        final Entity first = f.build();
        rep.add(first);

        final Form form = form("", "tekgenesis.test", "FirstDefaultForm").withBinding(first)
                          .children(internal("id").withBinding(first.getAttribute("id").get()),
                    field("A").withBinding(first.getAttribute("a").get()),
                    field("B").withBinding(first.getAttribute("b").get()))
                          .withRepository(rep)
                          .build();
        rep.add(form);

        checkGeneration(form);
    }

    @Test public void testExternalFormCodeGenerationNestedKeys()
        throws BuilderException
    {
        final EntityBuilder t = entity("tekgenesis.test", "Third").primaryKey("a", "b");
        t.addField(string("a"));
        t.addField(real("b").with(SIGNED));
        t.addField(decimal("c", 10, 3));

        final Entity third = t.build();
        rep.add(third);

        final EntityBuilder s = entity("tekgenesis.test", "Second").primaryKey("a", "b", "p");
        s.addField(string("a"));
        s.addField(real("b"));
        s.addField(decimal("c", 10, 3));
        s.addField(reference("p", third));

        final Entity second = s.build();
        rep.add(second);

        final EntityBuilder f = entity("tekgenesis.test", "First").primaryKey("a", "b", "c", "d");
        f.addField(string("a"));
        f.addField(string("b", 10));
        f.addField(reference("c", second));
        f.addField(reference("d", second));

        final Entity first = f.build();
        rep.add(first);

        final Form form = form("", "tekgenesis.test", "FirstForm").withBinding(first)
                          .children(field("A").withBinding(first.getAttribute("a").get()),
                    field("B").withBinding(first.getAttribute("b").get()),
                    suggest("C").withBinding(first.getAttribute("c").get()),
                    suggest("D").withBinding(first.getAttribute("d").get()))
                          .withRepository(rep)
                          .build();
        rep.add(form);

        checkGeneration(form);
    }

    @Test public void testExternalFormCodeGenerationStringKey()
        throws BuilderException
    {
        final EntityBuilder s = entity("tekgenesis.test", "SecondString").primaryKey("a");
        s.addField(string("a"));
        s.addField(decimal("c", 10, 3));

        final Entity second = s.build();
        rep.add(second);

        final EntityBuilder f = entity("tekgenesis.test", "FirstString").primaryKey("c");
        f.addField(string("a"));
        f.addField(string("b", 10));
        f.addField(reference("c", second));
        f.addField(reference("d", second));

        final Entity first = f.build();
        rep.add(first);

        final Form form = form("", "tekgenesis.test", "FirstStringForm").withBinding(first)
                          .children(field("A").withBinding(first.getAttribute("a").get()),
                    field("B").withBinding(first.getAttribute("b").get()),
                    suggest("C").withBinding(first.getAttribute("c").get()),
                    suggest("D").withBinding(first.getAttribute("d").get()))
                          .withRepository(rep)
                          .build();
        rep.add(form);

        checkGeneration(form);
    }
}  // end class ExternalFormGenerationTest
