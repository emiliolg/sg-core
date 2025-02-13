
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.dependency;

import org.assertj.core.api.Fail;
import org.assertj.core.api.IterableAssert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import tekgenesis.aggregate.AggregateFn;
import tekgenesis.expr.ExpressionFactory;
import tekgenesis.metadata.common.ModelLinkerImpl;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.dependency.TopologicalSorter.CycleDetectedPrettyException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormBuilder;
import tekgenesis.metadata.form.widget.FormBuilderPredefined;
import tekgenesis.metadata.form.widget.WidgetBuilder;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.Types;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

import static tekgenesis.expr.ExpressionFactory.*;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.horizontalGroup;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.internal;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.subform;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.table;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class PrecedenceDataTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public TestName name = new TestName();

    //~ Methods ......................................................................................................................................

    @Test public void bareTable()
        throws BuilderException
    {
        final Analyzer analyzer = form(table("Product Prices").id("table").children(num("a"), num("b")));

        analyzer.assertFormPrecedence();  // Empty precedence

        analyzer.assertPrecedence("table.row");  // Empty precedence
    }

    @Test public void basicCheck()
        throws BuilderException
    {
        final Analyzer analyzer = form(
                num("a").check(gt(ref("a"), integer(0)), "'A' should be greater than zero.")
                        .check(gt(integer(10), ref("a")), "'A' should be lesser than ten."),
                num("b").check(gt(ref("b"), ref("a")), "'B' should be greater than 'A'."));

        analyzer.assertFormPrecedence("a", "b");
    }

    @Test public void basicForm()
        throws BuilderException
    {
        final Analyzer analyzer = form(num("a"),
                num("b").is(mul(ref("c"), integer(2))),  // b is c * 2;
                num("c").is(mul(ref("a"), integer(2))),  // c is a * 2;
                num("d").is(add(ref("b"), ref("c"))),    // d is b + c;
                num("e"));

        analyzer.assertFormPrecedence("a", "c", "b", "d");
    }

    @Test public void basicFormDefault()
        throws BuilderException
    {
        final Analyzer analyzer = form(num("a").is(ref("e")),      // a is e;
                num("b").defaultValue(mul(ref("c"), integer(2))),  // default c * 2;
                num("c").is(mul(ref("a"), integer(2))),            // c is a * 2;
                num("d").defaultValue(add(ref("b"), ref("c"))),    // default b + c;
                num("e"));

        analyzer.assertFormPrecedence("e", "a", "c", "b", "d");
    }

    @Test public void basicTable()
        throws BuilderException
    {
        final Analyzer analyzer = form(
                table("Product Prices").id("table")
                                       .children(num("a"),
                                           num("b").is(mul(ref("c"), integer(2))),  // b is c * 2;
                                           num("c").is(mul(ref("a"), integer(2))),  // c is a * 2;
                                           num("d").is(add(ref("b"), ref("c"))),    // d is b + c;
                                           num("e")));

        analyzer.assertFormPrecedence("a", "c", "b", "d");

        analyzer.assertPrecedence("table.row", "a", "c", "b", "d");
    }

    @Test public void complexTable()
        throws BuilderException
    {
        final Analyzer analyzer = form(num("prev"),
                table("Product Prices").id("table")
                                       .children(num("a").defaultValue(ref("prev")),  // default prev;
                                           num("b").is(mul(ref("c"), integer(2))),  // b is c * 2;
                                           num("c").is(mul(ref("a"), integer(2))),  // c is a * 2;
                                           num("d").is(add(ref("b"), ref("c"))),    // d is b + c;
                                           num("e")),
                num("post").is(sum(ref("d")))                                       // post is sum(d);
                );

        analyzer.assertFormPrecedence("prev", "a", "c", "b", "d", "post");

        analyzer.assertPrecedence("prev", "prev", "a", "c", "b", "d", "post");

        analyzer.assertPrecedence("table.row", "a", "c", "b", "d", "post");

        analyzer.assertPrecedence("post", "post");
    }

    @Test public void cyclicDependsOn()
        throws BuilderException
    {
        final Analyzer analyzer = form(field("country").dependsOn("city"), field("state").dependsOn("country"), field("city").dependsOn("state"));

        analyzer.assertCycleDetected();
    }

    @Test public void cyclicForm()
        throws BuilderException
    {
        final Analyzer analyzer = form(num("a").is(ref("b")),  // a is b;
                num("b").is(ref("a"))  // b is a;
                );

        analyzer.assertCycleDetected();
    }

    @Test public void dependsOn()
        throws BuilderException
    {
        final Analyzer analyzer = form(field("country"),
                field("state").dependsOn("country"),
                field("city").dependsOn("state"),
                field("street"),
                field("zip").hide(ExpressionFactory.eq(ref("country"), str("argentina"))));

        analyzer.assertFormPrecedence("country", "zip", "state", "city");

        analyzer.assertPrecedence("country", "country", "zip", "state", "city");

        analyzer.assertPrecedence("country.dependants", "state", "city");
        analyzer.assertPrecedence("state.dependants", "city");
        analyzer.assertPrecedence("city.dependants");  // No dependants
    }

    @Test public void falseCyclicForm()
        throws BuilderException
    {
        final Analyzer analyzer = form(num("a").disable(gt(ref("b"), integer(2))),  // a.disable when b > 2;
                num("b").disable(gt(ref("a"), integer(2)))  // b.disable when a > 2;
                );

        analyzer.assertFormPrecedence("a", "b");
    }

    @Test public void labelExpressionTable()
        throws BuilderException
    {
        final Analyzer analyzer = form(field("prev"), table("Product Prices").id("table").children(num("a").labelExpression(ref("prev"))));

        analyzer.assertFormPrecedence("prev", "table", "a");

        analyzer.assertPrecedence("prev", "prev", "table", "a");
    }

    @Test public void relatedTables()
        throws BuilderException
    {
        final Analyzer analyzer = form(num("prev"),
                table("first").id("first")
                              .rowStyleClass(wildcard(gt(ref("prev"), ref("b")), str("red"), str("yellow")))
                              .children(num("a"), num("b").is(mul(ref("c"), integer(2))),  // b is c * 2;
                                  num("c")),
                table("second").id("second")
                               .children(num("d").is(mul(ref("f"), ref("e"))),  // d is f * e;
                                   num("e").is(sum(ref("c"))),  // e is sum(c);
                                   num("f")));

        analyzer.assertFormPrecedence("f", "c", "e", "d", "prev", "b", "first");

        analyzer.assertPrecedence("first.row", "c", "e", "d", "b", "first");

        analyzer.assertPrecedence("second.row", "f", "e", "d");
    }

    @Test public void subformReferences()
        throws BuilderException
    {
        final Analyzer analyzer = form(subform("A Subform", "tekgenesis.predefined.Form").id("subformA"),
                subform("B Subform", "tekgenesis.predefined.Form").id("subformB"),
                num("a").is(ref("subformA.total")),
                num("b").is(ref("subformB.total")),
                num("c").is(add(ref("a"), ref("b"))),
                num("d").is(add(ref("subformA.tax"), ref("subformB.tax"))));

        analyzer.assertFormPrecedence("subformB.tax", "subformA.tax", "d", "subformB.total", "b", "subformA.total", "a", "c");

        // Assert precedence for subforms (fields referencing subform with dot notation)
        analyzer.assertPrecedence("subformA", "a", "c", "d");
        analyzer.assertPrecedence("subformB", "d", "b", "c");
        analyzer.assertPrecedence("d", "d");
        analyzer.assertPrecedence("c", "c");
    }

    @Test public void subformReferencesInsideMultiple()
        throws BuilderException
    {
        final Analyzer analyzer = form(
                table("Products").id("products")
                                 .children(field("product").is(ref("dependencies.product")),
                                     num("deps").is(ref("dependencies.total")),
                                     subform("Dependencies", "tekgenesis.predefined.Form").id("dependencies")),
                internal("total").withType(Types.intType()).is(rows(ref("product"))));

        analyzer.assertFormPrecedence("dependencies.total", "deps", "dependencies.product", "product", "total");

        // Assert precedence for subform (fields referencing subform with dot notation)
        analyzer.assertPrecedence("dependencies", "product", "total", "deps");
    }

    @Test public void subformReferencesWithDisplay()
        throws BuilderException
    {
        final Analyzer analyzer = form(subform("A Subform", "tekgenesis.predefined.Form").id("sub").display(ref("sub.total")),
                num("a").is(ref("sub.total")),
                num("b").is(ref("sub.tax")),
                subform("B Subform", "tekgenesis.predefined.Form").id("extra").display(ref("sub.total")));

        analyzer.assertFormPrecedence("sub.tax", "b", "sub.total", "extra", "a", "sub");

        // Assert precedence for subforms (fields referencing subform with dot notation)
        analyzer.assertPrecedence("sub", "b", "extra", "a", "sub");
        analyzer.assertPrecedence("a", "a");
        analyzer.assertPrecedence("b", "b");
        analyzer.assertPrecedence("extra", "extra");
    }

    @Test public void tableWithAggregates()
        throws BuilderException
    {
        final Analyzer analyzer = form(
                table("Product Prices").id("table").children(num("a"), num("b").is(mul(ref("c"), integer(2))),  // b is c * 2;
                    num("c")).aggregate(AggregateFn.SUM));

        analyzer.assertFormPrecedence("c", "b", "a", "table");

        analyzer.assertPrecedence("table.row", "c", "b", "a", "table");
    }

    @Test public void tableWithComplexRowStyle()
        throws BuilderException
    {
        final Analyzer analyzer = form(num("prev"),
                table("Product Prices").id("table")
                                       .rowStyleClass(wildcard(gt(ref("prev"), ref("b")), str("red"), str("yellow")))
                                       .children(num("a"), num("b").is(mul(ref("c"), integer(2))),  // b is c * 2;
                                           num("c")));

        analyzer.assertFormPrecedence("c", "prev", "b", "table");

        analyzer.assertPrecedence("table.row", "c", "b", "table");
    }

    @Test public void tableWithGroupColumn()
        throws BuilderException
    {
        final Analyzer analyzer = form(
                table("Product Prices").id("table")
                                       .children(num("a"),
                                           num("b").is(mul(ref("a"), integer(2))),                                       // b is a * 2;
                                           horizontalGroup("horizontal").children(num("d").is(add(ref("a"), ref("b"))),  // d is a + b;
                                               num("e"))),
                num("count").is(count(ref("a"))),
                num("size").is(ExpressionFactory.size(ref("e"))));

        analyzer.assertFormPrecedence("e", "size", "a", "count", "b", "d");

        // Check proper dependencies inside table column groups for cardinality changes
        analyzer.assertPrecedence("table.row", "e", "size", "a", "count", "b", "d");
    }

    @Test public void tableWithSimpleRowStyle()
        throws BuilderException
    {
        final Analyzer analyzer = form(num("prev"),
                table("Product Prices").id("table")
                                       .rowStyleClass(wildcard(gt(ref("prev"), integer(2)), str("red"), str("yellow")))
                                       .children(num("a"), num("b")));

        analyzer.assertFormPrecedence("prev", "table");

        analyzer.assertPrecedence("table.row");
    }

    private Analyzer form(WidgetBuilder... children)
        throws BuilderException
    {
        final String      formName = name.getMethodName();
        final FormBuilder builder  = FormBuilderPredefined.form("", "tekgenesis.test", formName).children(children);
        return new Analyzer(formName, builder);
    }

    //~ Methods ......................................................................................................................................

    private static WidgetBuilder field(String id) {
        return FormBuilderPredefined.field(id).id(id);
    }

    private static WidgetBuilder num(String id)
        throws BuilderException
    {
        return FormBuilderPredefined.integer(id).id(id);
    }

    //~ Inner Classes ................................................................................................................................

    private static class Analyzer {
        private final FormBuilder builder;
        private PrecedenceData    data;

        private final String formName;

        private Analyzer(String formName, FormBuilder builder) {
            this.formName = formName;
            this.builder  = builder;
            data          = null;
        }

        public void assertCycleDetected() {
            try {
                calculateData();
                failBecauseExceptionWasNotThrown(CycleDetectedPrettyException.class);
            }
            catch (final CycleDetectedPrettyException e) {
                assertThat(e).hasMessageContaining(TopologicalSorter.CYCLE_DETECTED_MSG);
            }
            catch (final BuilderException e) {
                Fail.fail(e.getMessage());
            }
        }

        public void assertFormPrecedence(String... references) {
            assertPrecedenceForReference(formName, references);
        }

        public void assertPrecedence(String reference, String... references) {
            assertPrecedenceForReference(reference, references);
        }

        private void addPredefinedForm(ModelRepository repository)
            throws BuilderException
        {
            final FormBuilder b = FormBuilderPredefined.form("", "tekgenesis.predefined", "Form");
            b.addWidget(field("product"));
            b.addWidget(num("total"));
            b.addWidget(num("tax"));
            repository.add(b.withRepository(repository).build());
        }

        private IterableAssert<String> assertPrecedenceForReference(String reference, String[] references) {
            return assertThat(getData().getRecomputeList(reference)).containsOnly(references);
        }

        private PrecedenceData calculateData()
            throws BuilderException
        {
            final ModelRepository repository = createModelRepository();
            final Form            form       = builder.withRepository(repository).build();
            repository.add(form);
            ModelLinkerImpl.linkForm(repository, form);
            return form.getPrecedenceData();
        }

        private ModelRepository createModelRepository()
            throws BuilderException
        {
            final ModelRepository repository = new ModelRepository();
            addPredefinedForm(repository);
            return repository;
        }

        private PrecedenceData getData() {
            if (data == null) {
                try {
                    data = calculateData();
                }
                catch (final BuilderException e) {
                    fail(e.getMessage());
                }
            }
            return data;
        }
    }  // end class Analyzer
}  // end class PrecedenceDataTest
