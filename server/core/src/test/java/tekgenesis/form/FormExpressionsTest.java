
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.FormRule;
import tekgenesis.database.DatabaseConstants;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.samples.form.*;
import tekgenesis.showcase.BinaryNode;
import tekgenesis.showcase.BinaryTree;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.samples.form.DependeciesForm.*;
import static tekgenesis.samples.form.EmployeeRecursiveProfileBase.Field.MANAGER;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class FormExpressionsTest {

    //~ Instance Fields ..............................................................................................................................

    public DbRule db = new DbRule(DbRule.AUTHORIZATION, DbRule.FORM) {
            @Override protected void before() {
                createDatabase(DatabaseConstants.MEM);
            }
        };

    @Rule public TestRule chain = db.around(new FormRule());

    //~ Methods ......................................................................................................................................

    @Test public void dateExpression() {
        final DateExpressions instance = FormsImpl.init(DateExpressions.class);

        assertThat(instance.getDateTime()).isEqualTo(DateOnly.current().toDateTime());
        assertThat(instance.getDate()).isEqualTo(DateTime.current().toDateOnly());
    }

    @Test public void formExpressionsTestsDefaultAndOnLoad() {
        final FormExpressionsTestDefaultAndOnLoad instance = FormsImpl.init(FormExpressionsTestDefaultAndOnLoad.class);

        // Test simple default;
        assertThat(instance.getReserved()).isEqualTo(2);

        // Test boolean evaluation;
        assertThat(instance.isValid()).isTrue();

        // Some trivial update;
        instance.setReserved(6);

        // Test boolean evaluation;
        assertThat(instance.isValid()).isFalse();
    }

    @Test public void formExpressionsTestsDefaults() {
        final FormExpressionsTestDefaults instance = FormsImpl.init(FormExpressionsTestDefaults.class);

        // Test simple default;
        assertThat(instance.getIgnored()).isEqualTo(-1);
        assertThat(instance.getItem1()).isEqualTo(10);

        // Test value set after on_change called;
        assertThat(instance.getItem2()).isEqualTo(20);

        // Test default firing on_change;
        assertThat(instance.getItem3()).isEqualTo(30);

        // Test expression computed after all;
        assertThat(instance.getTotal()).isEqualTo(60);
    }

    @Test public void formExpressionsTestsOnChangeRecursion() {
        final FormExpressionsTestRecursion instance = FormsImpl.init(FormExpressionsTestRecursion.class);

        assertThat(instance.getA()).isEqualTo(20);

        /*
         *  on_change method should have been called twice at this instance:
         *  1- After 'A' default evaluation to 10 -> 'B' to 20 -> 'C' to 40 -> on_change#1;
         *  2- After on_change #1 execution, setting 'A' to 20 -> 'B' to 40 -> 'C' to 80 -> on_change#2;
         */
        assertThat(instance.getCount()).isEqualTo(2);

        instance.setA(30);
        assertThat(instance.getA()).isEqualTo(20);

        /*
         *  on_change method should have been called four times at this instance;
         *  3- After 'A' set to 30 -> 'B' to 60 -> 'C' to 120 -> on_change#3;
         *  2- After on_change #3 execution, setting 'A' to 20 -> 'B' to 40 -> 'C' to 80 -> on_change#4;
         */
        assertThat(instance.getCount()).isEqualTo(4);
    }

    @Test public void formExpressionsTestsRelatedTables() {
        final FormExpressionsTestRelatedTables instance = FormsImpl.init(FormExpressionsTestRelatedTables.class);

        // Check sizes and sum calculations;
        final FormTable<FormExpressionsTestRelatedTables.FirstRow> first = instance.getFirst();
        assertThat(first.size()).isEqualTo(1);
        assertThat(instance.getFtot()).isEqualTo(20);

        // Check sizes and sum calculations;
        final FormTable<FormExpressionsTestRelatedTables.SecondRow> second = instance.getSecond();
        assertThat(second.size()).isEqualTo(2);
        assertThat(instance.getStot()).isEqualTo(70);

        // Add a row on first table and check dependency calculation;
        final FormExpressionsTestRelatedTables.FirstRow fAdd = first.add();
        assertThat(first.size()).isEqualTo(2);
        assertThat(instance.getFtot()).isEqualTo(20);
        assertThat(instance.getStot()).isEqualTo(70);

        // Modify a row on first table and check dependency calculation
        fAdd.setC(30);
        assertThat(instance.getFtot()).isEqualTo(60);
        assertThat(instance.getStot()).isEqualTo(150);

        // Add a row on second table and check dependency calculation;
        final FormExpressionsTestRelatedTables.SecondRow sAdd = second.add();
        assertThat(instance.getStot()).isEqualTo(220);

        // Modify a row on second table and check dependency calculation
        sAdd.setY(20);
        assertThat(instance.getStot()).isEqualTo(230);
    }

    @Test public void formExpressionsTestsTables() {
        final FormExpressionsTestTables instance = FormsImpl.init(FormExpressionsTestTables.class);

        final FormTable<FormExpressionsTestTables.TableRow> table = instance.getTable();

        // Test load method previously being called;
        assertThat(table.size()).isEqualTo(3);

        // Test table row values;
        for (int i = 0; i < table.size(); i++) {
            final FormExpressionsTestTables.TableRow row = table.get(i);
            assertThat(row.getCol1()).isEqualTo(i * 10 + 10);
            assertThat(row.getCol2()).isEqualTo(row.getCol1() * 2);
            assertThat(row.getCol3()).isEqualTo(row.getCol1() * 3);
            assertThat(row.getTotal()).isEqualTo(row.getCol2() + row.getCol3());
        }

        // Test post expressions;
        assertThat(instance.getPost()).isEqualTo(300);

        // Remove last and test recalculations;
        table.remove(table.size() - 1);
        assertThat(instance.getPost()).isEqualTo(150);
    }

    @Test public void formUpdateExpressionTest() {
        final UpdateExpressionTest instance = FormsImpl.init(UpdateExpressionTest.class);

        // checks that isUpdate is false.
        assertThat(instance.isUpdate()).isFalse();

        // sets id equals to 1 and name (some) equals to test. After that it submit the form.
        instance.setId(1);
        instance.setSome("test");
        runInTransaction(instance::create);

        // Gets the form with pk equals to 1.
        final Tuple<UpdateExpressionTest, FormModel> init = invokeInTransaction(() -> FormsImpl.init(UpdateExpressionTest.class, "1"));

        final UpdateExpressionTest update = init.first();

        // asserts that every field setted before are perfectly populated and isUpdate is true.
        assertThat(update.isUpdate()).isTrue();
        assertThat(update.getSome()).isEqualTo("test");
        assertThat(update.getId()).isEqualTo(1);
    }

    @Test public void recursiveOnChangeWidgetWithMultiple() {
        final OnChangeRecursiveWidgetWithMultipleForm form = FormsImpl.init(OnChangeRecursiveWidgetWithMultipleForm.class);
        assertThat(form.stats()).isEqualTo(new int[] { 0, 1 });  // root++ (on initial compute)

        final OnChangeRecursiveWidgetWithMultiple                      widget1 = form.getChild();
        final FormTable<OnChangeRecursiveWidgetWithMultiple.ValuesRow> values1 = widget1.getValues();
        values1.add().setValue("#0->v1");  // root++ changes++
        assertThat(form.stats()).isEqualTo(new int[] { 1, 2 });
        values1.add().setValue("#1->v1");  // root++ changes++
        values1.add().setValue("#2->v1");  // root++ changes++
        assertThat(form.stats()).isEqualTo(new int[] { 3, 4 });

        final OnChangeRecursiveWidgetWithMultiple                      widget2 = widget1.getChild().create();  // root++
        final FormTable<OnChangeRecursiveWidgetWithMultiple.ValuesRow> values2 = widget2.getValues();
        values2.add().setValue("#0->v2");                                                                      // root++ changes++
        assertThat(form.stats()).isEqualTo(new int[] { 4, 6 });
        values2.add().setValue("#1->v2");                                                                      // root++ changes++
        values2.add().setValue("#2->v2");                                                                      // root++ changes++
        assertThat(form.stats()).isEqualTo(new int[] { 6, 8 });

        final OnChangeRecursiveWidgetWithMultiple                      widget3 = widget2.getChild().create();  // root++
        final FormTable<OnChangeRecursiveWidgetWithMultiple.ValuesRow> values3 = widget3.getValues();
        values3.add().setValue("#0->v3");                                                                      // root++ change++
        assertThat(form.stats()).isEqualTo(new int[] { 7, 10 });
        values3.add().setValue("#1->v3");                                                                      // root++ changes++
        values3.add().setValue("#2->v3");                                                                      // root++ changes++
        assertThat(form.stats()).isEqualTo(new int[] { 9, 12 });
    }

    @Test public void recursiveOnChangeWidgetWithMultipleAndDefault() {
        final OnChangeRecursiveWidgetWithMultipleAndDefaultForm form = FormsImpl.init(OnChangeRecursiveWidgetWithMultipleAndDefaultForm.class);
        assertThat(form.stats()).isEqualTo(new int[] { 0, 1 });  // root++ (initial compute, added root widget)

        final OnChangeRecursiveWidgetWithMultipleAndDefault                      widget1 = form.getChild();
        final FormTable<OnChangeRecursiveWidgetWithMultipleAndDefault.ValuesRow> values1 = widget1.getValues();
        values1.add().setValue("#0->v1");  // root+=2 changes+=2 (default and update)
        assertThat(form.stats()).isEqualTo(new int[] { 2, 3 });
        values1.add().setValue("#1->v1");  // root+=2 changes+=2 (default and update)
        values1.add().setValue("#2->v1");  // root+=2 changes+=2 (default and update)
        assertThat(form.stats()).isEqualTo(new int[] { 6, 7 });

        final OnChangeRecursiveWidgetWithMultipleAndDefault                      widget2 = widget1.getChild().create();  // root++
        final FormTable<OnChangeRecursiveWidgetWithMultipleAndDefault.ValuesRow> values2 = widget2.getValues();
        values2.add().setValue("#0->v2");                                                                                // root+=2 changes+=2
                                                                                                                         // (default and update)
        assertThat(form.stats()).isEqualTo(new int[] { 8, 10 });
        values2.add().setValue("#1->v2");                                                                                // root+=2 changes+=2
                                                                                                                         // (default and update)
        values2.add().setValue("#2->v2");                                                                                // root+=2 changes+=2
                                                                                                                         // (default and update)
        assertThat(form.stats()).isEqualTo(new int[] { 12, 14 });

        final OnChangeRecursiveWidgetWithMultipleAndDefault                      widget3 = widget2.getChild().create();  // root++
        final FormTable<OnChangeRecursiveWidgetWithMultipleAndDefault.ValuesRow> values3 = widget3.getValues();
        values3.add().setValue("#0->v3");                                                                                // root+=2 changes+=2
                                                                                                                         // (default and update)
        assertThat(form.stats()).isEqualTo(new int[] { 14, 17 });
        values3.add().setValue("#1->v3");                                                                                // root+=2 changes+=2
                                                                                                                         // (default and update)
        values3.add().setValue("#2->v3");                                                                                // root+=2 changes+=2
                                                                                                                         // (default and update)
        assertThat(form.stats()).isEqualTo(new int[] { 18, 21 });
    }

    @Test public void recursiveWidgetWithinMultiple() {
        final RecursiveWidgetWithinMultipleForm form = FormsImpl.init(RecursiveWidgetWithinMultipleForm.class);
        assertThat(form.getChanges()).isEqualTo(1);  // changes++ (on initial compute with default)

        final RecursiveWidgetWithinMultiple            child     = form.getChild();
        final RecursiveWidgetWithinMultiple.WidgetsRow childRow1 = child.getWidgets().add();
        assertThat(form.getChanges()).isEqualTo(2);  // changes++ (on initial compute with default)
        final RecursiveWidgetWithinMultiple.WidgetsRow childRow2 = child.getWidgets().add();
        assertThat(form.getChanges()).isEqualTo(3);  // changes++ (on initial compute with default)

        final RecursiveWidgetWithinMultiple childRow1Inner = childRow1.getInner();
        childRow1Inner.getWidgets().add();
        assertThat(form.getChanges()).isEqualTo(4);  // changes++ (on initial compute with default)
        childRow1Inner.getWidgets().add();
        assertThat(form.getChanges()).isEqualTo(5);  // changes++ (on initial compute with default)

        final RecursiveWidgetWithinMultiple            childRow2Inner     = childRow2.getInner();
        final RecursiveWidgetWithinMultiple.WidgetsRow childRow2InnerRow1 = childRow2Inner.getWidgets().add();
        assertThat(form.getChanges()).isEqualTo(6);  // changes++ (on initial compute with default)
        final RecursiveWidgetWithinMultiple.WidgetsRow childRow2InnerRow2 = childRow2Inner.getWidgets().add();
        assertThat(form.getChanges()).isEqualTo(7);  // changes++ (on initial compute with default)
    }

    @Test public void subformExpressionsDotNotation() {
        final SubformsExpressions instance = FormsImpl.init(SubformsExpressions.class);

        final FormTable<SubformsExpressions.ProductsRow> table = instance.getProducts();

        // Test load method previously being called;
        assertThat(table.size()).isEqualTo(5);
        assertThat(instance.getTotal()).isEqualTo(5);

        // Test expressions on rows referencing subform inner values with dot notation
        for (int i = 0; i < products.length; i++) {
            final SubformsExpressions.ProductsRow runtime = table.get(i);
            assertThat(runtime.getDeps()).isEqualTo(dependenciesCount(dependencies[i]));
            assertThat(runtime.getProduct()).isEqualTo(products[i]);
        }
    }

    @Test public void subformExpressionsDotNotationUpdate() {
        final SubformsExpressions instance = FormsImpl.init(SubformsExpressions.class);

        final FormTable<SubformsExpressions.ProductsRow> table = instance.getProducts();

        // Test load method previously being called;
        assertThat(table.size()).isEqualTo(5);

        final int             last    = products.length - 1;
        final int             first   = 0;
        final DependeciesForm subform = table.get(last).getDependencies();
        if (subform != null) {
            subform.setProduct(products[first]);
            subform.populate();
        }

        // Test update affected fields
        final SubformsExpressions.ProductsRow runtime = table.get(last);
        assertThat(runtime.getDeps()).isEqualTo(dependenciesCount(dependencies[first]));
        assertThat(runtime.getProduct()).isEqualTo(products[first]);
    }

    @Test public void widgetsOnChangeRecursive() {
        final OnChangeWidgetRecursiveForm form = FormsImpl.init(OnChangeWidgetRecursiveForm.class);
        assertThat(form.stats()).isEqualTo(new int[] { 1, 0 });

        final OnChangeRecursiveWidget widget1 = form.getChild();
        widget1.setValue("v1");
        assertThat(form.stats()).isEqualTo(new int[] { 2, 1 });

        final OnChangeRecursiveWidget widget2 = widget1.getChild().create();
        widget2.setValue("v2");
        assertThat(form.stats()).isEqualTo(new int[] { 4, 2 });

        final OnChangeRecursiveWidget widget3 = widget2.getChild().create();
        widget3.setValue("v3");
        assertThat(form.stats()).isEqualTo(new int[] { 6, 3 });

        final OnChangeRecursiveWidget widget4 = widget3.getChild().create();
        widget4.setValue("v4");
        assertThat(form.stats()).isEqualTo(new int[] { 8, 4 });

        widget1.setValue("v1'");
        assertThat(form.stats()).isEqualTo(new int[] { 9, 5 });

        widget2.setValue("v2'");
        assertThat(form.stats()).isEqualTo(new int[] { 10, 6 });

        widget3.setValue("v3'");
        assertThat(form.stats()).isEqualTo(new int[] { 11, 7 });

        widget4.setValue("v4'");
        assertThat(form.stats()).isEqualTo(new int[] { 12, 8 });
    }

    @Test public void widgetsRecursiveExpressions() {
        final BinaryTree tree = FormsImpl.init(BinaryTree.class);
        tree.insert("B");

        final BinaryNode head = tree.getHead().get();
        assertThat(head.getInorder()).isEqualTo(" A  B  E  F  H  M  R  U  W ");

        tree.insert("G").insert("D").insert("I");
        assertThat(head.getInorder()).isEqualTo(" A  B  D  E  F  G  H  I  M  R  U  W ");

        tree.insert("C").insert("Z");
        assertThat(head.getInorder()).isEqualTo(" A  B  C  D  E  F  G  H  I  M  R  U  W  Z ");

        final String inorder = head.toString();
        assertThat(tree.getInorder()).isEqualTo(inorder);
        assertThat(head.getInorder()).isEqualTo(inorder);
        assertThat(tree.contains("A")).isTrue();
        assertThat(tree.contains("D")).isTrue();
        assertThat(tree.contains("F")).isTrue();
        assertThat(tree.contains("H")).isTrue();
        assertThat(tree.contains("I")).isTrue();
        assertThat(tree.contains("P")).isFalse();
        assertThat(tree.contains(" ")).isFalse();
    }

    @Test public void widgetsRecursiveHierarchy() {
        final EmployeeRecursiveProfile profile = FormsImpl.init(EmployeeRecursiveProfile.class);
        assertThat(profile.getName()).isEqualTo("Juana I de Castilla");
        assertThat(profile.isDefined(MANAGER)).isFalse();

        final EmployeeRecursiveWidget employee = profile.getEmployee();
        assertThat(employee.getFull()).isEqualTo("Juana I de Castilla");

        final OptionalWidget<EmployeeRecursiveWidget> optional = employee.getManager();
        assertThat(optional).isNotNull();
        assertThat(optional.isEmpty()).isTrue();
        assertThat(optional.isPresent()).isFalse();

        // Update values
        employee.setFirst("Jeanne");
        employee.setLast("d'Arc");

        // Assert expression recompute
        assertThat(employee.getFull()).isEqualTo("Jeanne d'Arc");
        assertThat(profile.getName()).isEqualTo("Jeanne d'Arc");

        final EmployeeRecursiveWidget manager = optional.create();
        assertThat(optional.isEmpty()).isFalse();
        assertThat(optional.isPresent()).isTrue();

        // Assert default computes
        assertThat(manager.getFull()).isEqualTo("Juana I de Castilla");
        assertThat(employee.getManager().get().getFull()).isEqualTo("Juana I de Castilla");
        assertThat(profile.getManager()).isEqualTo("Juana I de Castilla");

        // Update third level (profile -> employee -> manager) values
        manager.setFirst("Manager Jeanne");
        manager.setLast("d'Arc");

        // Assert local expressions compute
        assertThat(manager.getFull()).isEqualTo("Manager Jeanne d'Arc");
        // Assert parent accessor updated
        assertThat(employee.getManager().get().getFull()).isEqualTo("Manager Jeanne d'Arc");
        // Assert parent-parent notification
        assertThat(profile.getManager()).isEqualTo("Manager Jeanne d'Arc");
    }  // end method widgetsRecursiveHierarchy

    @Test public void widgetsRecursiveOnChange() {
        final OnChangeRecursiveForm form = FormsImpl.init(OnChangeRecursiveForm.class);
        assertThat(form.stats()).isEqualTo(new int[] { 0, 0, 0, 0, 0 });

        final OnChangeRecursiveWidget widget1 = form.getChild();
        widget1.setValue("v1");
        assertThat(form.stats()).isEqualTo(new int[] { 1, 0, 0, 0, 1 });

        final OnChangeRecursiveWidget widget2 = widget1.getChild().create();
        widget2.setValue("v2");
        assertThat(form.stats()).isEqualTo(new int[] { 1, 1, 0, 0, 2 });

        final OnChangeRecursiveWidget widget3 = widget2.getChild().create();
        widget3.setValue("v3");
        assertThat(form.stats()).isEqualTo(new int[] { 1, 1, 1, 0, 3 });

        final OnChangeRecursiveWidget widget4 = widget3.getChild().create();
        widget4.setValue("v4");
        assertThat(form.stats()).isEqualTo(new int[] { 1, 1, 1, 1, 4 });
    }

    @Test public void widgetsSimpleHierarchy() {
        final EmployeeSimpleProfile profile = FormsImpl.init(EmployeeSimpleProfile.class);
        assertThat(profile.getName()).isEqualTo("Juana I de Castilla");

        // Assert default computes
        final EmployeeSimpleWidget employee = profile.getEmployee();
        assertThat(employee.getFirst()).isEqualTo("Juana I");
        assertThat(employee.getLast()).isEqualTo("de Castilla");
        assertThat(employee.getFull()).isEqualTo("Juana I de Castilla");

        // Update values
        employee.setFirst("Jeanne");
        employee.setLast("d'Arc");

        // Assert values
        assertThat(employee.getFirst()).isEqualTo("Jeanne");
        assertThat(employee.getLast()).isEqualTo("d'Arc");
        // Assert expression recompute
        assertThat(employee.getFull()).isEqualTo("Jeanne d'Arc");

        // Assert parent notification and recompute
        assertThat(profile.getName()).isEqualTo("Jeanne d'Arc");
    }
}  // end class FormExpressionsTest
