
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import org.junit.Test;

import tekgenesis.expr.Expression;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.repository.ModelRepository;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.expr.ExpressionFactory.*;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.integer;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.list;
import static tekgenesis.metadata.form.widget.SimpleFormPrinter.dump;
import static tekgenesis.type.Types.intType;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class FormBuilderTest {

    //~ Methods ......................................................................................................................................

    @Test public void basicWidgets()
        throws BuilderException
    {
        final FormBuilder builder = form("", "tekgenesis.print", "formA").label("Formulario A");

        builder.children(check("Recibir ofertas?").defaultValue(true),
            label("Un titulo"),
            combo("Pais"),
            list("Intereses?"),
            verticalGroup("Domicilio").children(field("Direccion").id("direccion"),
                field("Codigo Postal").length(7),
                field("Localidad").defaultValue("Pilar"),
                field("Provincia").defaultValue("Buenos Aires"),
                field("Disabled").disable(eq(ref("direccion"), str("Carabobo")))),
            verticalGroup("Botones").children(verticalGroup("Arriba").children(button("Aceptar").disable(), button("Cancelar").disable()),
                verticalGroup("Abajo").children(button("Help"), button("Info"), area("Comentarios"))),
            display("<- Menu").link("menu"));

        final String basic = "form(formA,\"Formulario A\")\n" +
                             "\tcheck_box($C0,\"Recibir ofertas?\")\n" +
                             "\tlabel($L1,\"Un titulo\")\n" +
                             "\tcombo_box($C2,\"Pais\")\n" +
                             "\tlist_box($L3,\"Intereses?\")\n" +
                             "\tvertical($V4,\"Domicilio\")\n" +
                             "\t\ttext_field(direccion,\"Direccion\")\n" +
                             "\t\ttext_field($T5,\"Codigo Postal\")\n" +
                             "\t\ttext_field($T6,\"Localidad\")\n" +
                             "\t\ttext_field($T7,\"Provincia\")\n" +
                             "\t\ttext_field($T8,\"Disabled\")\n" +
                             "\tvertical($V9,\"Botones\")\n" +
                             "\t\tvertical($V10,\"Arriba\")\n" +
                             "\t\t\tbutton($B11,\"Aceptar\")\n" +
                             "\t\t\tbutton($B12,\"Cancelar\")\n" +
                             "\t\tvertical($V13,\"Abajo\")\n" +
                             "\t\t\tbutton($B14,\"Help\")\n" +
                             "\t\t\tbutton($B15,\"Info\")\n" +
                             "\t\t\ttext_area($T16,\"Comentarios\")\n" +
                             "\tdisplay($D17,\"<- Menu\")";

        verify(builder, basic);
    }  // end method basicWidgets

    @Test public void groupsDisableChildren()
        throws BuilderException
    {
        final WidgetBuilder vertical = verticalGroup("Vertical").id("v").disable(not(ref("b")));

        vertical.children(field("Product").id("product"),
            numeric("Unit Price", 10, 2).disable(eq(ref("valid"), bool(true))).id("unitPrice"),
            numeric("Tax", 10, 2).id("taxPrice").is(mul(ref("unitPrice"), ref("tax"))),
            combo("Quantity").disable(eq(ref("valid"), bool(true))).withType(intType()).id("quantity"));

        final WidgetBuilder horizontal = horizontalGroup("Horizontal").id("h").disable(ref("b"));

        horizontal.children(field("C").id("c"), field("D").id("d"), vertical);

        final FormBuilder builder = form("", "tekgenesis.sample", "GroupWithDisableForm").children(field("A").id("a"),
                check("B").id("b"),
                horizontal,
                field("E").id("e"));

        final Form form = builder.withRepository(new ModelRepository()).build();

        assertThat(form.getWidget("a").get().getDisableExpression()).isEqualTo(Expression.FALSE);
        assertThat(form.getWidget("b").get().getDisableExpression()).isEqualTo(Expression.FALSE);
        assertThat(form.getWidget("h").get().getDisableExpression().toString()).isEqualTo("b");
        assertThat(form.getWidget("c").get().getDisableExpression().toString()).isEqualTo("b");
        assertThat(form.getWidget("d").get().getDisableExpression().toString()).isEqualTo("b");
        assertThat(form.getWidget("v").get().getDisableExpression().toString()).isEqualTo("!(b)");
        assertThat(form.getWidget("product").get().getDisableExpression().toString()).isEqualTo("!(b)");
        assertThat(form.getWidget("unitPrice").get().getDisableExpression().toString()).isEqualTo("valid == true");
    }

    @Test public void simpleTable()
        throws BuilderException
    {
        final FormBuilder builder = form("", "tekgenesis.print", "formD").label("Tabla");

        final WidgetBuilder table = table("Product Prices").id("table");

        table.children(field("Product").disable().id("product"),
            numeric("Unit Price", 10, 2).disable(eq(ref("valid"), bool(true))).id("unitPrice"),
            numeric("Tax", 10, 2).id("taxPrice").is(mul(ref("unitPrice"), ref("tax"))),
            combo("Quantity").disable(eq(ref("valid"), bool(true))).withType(intType()).id("quantity"),
            numeric("Price", 10, 2).id("price").is(mul(add(ref("taxPrice"), ref("unitPrice")), ref("quantity"))),
            check("Deprecate").defaultValue(false).id("valid"));

        builder.children(integer("Tax").id("tax"), table, numeric("Total", 10, 2).id("total").is(sum(ref("price"))), display("<- Menu").link("menu"));

        final String tableExpected = "form(formD,\"Tabla\")\n" +
                                     "\ttext_field(tax,\"Tax\")\n" +
                                     "\ttable(table,\"Product Prices\")\n" +
                                     "\t\ttext_field(product,\"Product\")\n" +
                                     "\t\ttext_field(unitPrice,\"Unit Price\")\n" +
                                     "\t\ttext_field(taxPrice,\"Tax\")\n" +
                                     "\t\tcombo_box(quantity,\"Quantity\")\n" +
                                     "\t\ttext_field(price,\"Price\")\n" +
                                     "\t\tcheck_box(valid,\"Deprecate\")\n" +
                                     "\ttext_field(total,\"Total\")\n" +
                                     "\tdisplay($D0,\"<- Menu\")";

        verify(builder, tableExpected);
    }

    @Test public void widgetsWithExpressions()
        throws BuilderException
    {
        final FormBuilder builder = form("", "tekgenesis.print", "formC").label("Is Expressions");

        builder.children(numeric("Item $1", 10, 2).id("item1"),
            numeric("Item $2", 10, 2).id("item2"),
            numeric("Item $3", 10, 2).id("item3"),
            numeric("Sub Total", 10, 2).id("subTotal").is(add(ref("item1"), add(ref("item2"), ref("item3")))),
            numeric("Iva", 10, 2).id("iva").is(mul(ref("subTotal"), real(0.21))),
            numeric("Total", 10, 2).id("total").is(add(ref("subTotal"), ref("iva"))),
            display("<- Menu").link("menu"));

        final String expression = "form(formC,\"Is Expressions\")\n" +
                                  "\ttext_field(item1,\"Item $1\")\n" +
                                  "\ttext_field(item2,\"Item $2\")\n" +
                                  "\ttext_field(item3,\"Item $3\")\n" +
                                  "\ttext_field(subTotal,\"Sub Total\")\n" +
                                  "\ttext_field(iva,\"Iva\")\n" +
                                  "\ttext_field(total,\"Total\")\n" +
                                  "\tdisplay($D0,\"<- Menu\")";

        verify(builder, expression);
    }

    private void verify(FormBuilder builder, String out)
        throws BuilderException
    {
        final Form form = builder.withRepository(new ModelRepository()).build();
        assertThat(dump(form)).isEqualTo(out);
    }
}  // end class FormBuilderTest
