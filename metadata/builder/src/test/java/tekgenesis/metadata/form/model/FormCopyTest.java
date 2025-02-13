
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import org.junit.Test;

import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormBuilder;
import tekgenesis.metadata.form.widget.WidgetBuilder;
import tekgenesis.repository.ModelRepository;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.expr.ExpressionFactory.*;
import static tekgenesis.metadata.form.InstanceReference.createInstanceReference;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.*;
import static tekgenesis.metadata.form.widget.FormBuilderPredefined.integer;
import static tekgenesis.type.Types.intType;

@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class FormCopyTest {

    //~ Methods ......................................................................................................................................

    @Test public void copyFormModelTest()
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

        final Form      f  = builder.withRepository(new ModelRepository()).build();
        final FormModel fm = new FormModel(f);
        fm.setWorkItem(createInstanceReference(createQName("sales", "Customer"), "1:1"));
        final FormModel copy = fm.copy();

        final TestStreamWriter writer     = new TestStreamWriter();
        final TestStreamWriter copyWriter = new TestStreamWriter();
        fm.serialize(writer, true);
        copy.serialize(copyWriter, true);

        assertThat(writer.getS()).isEqualTo(copyWriter.getS());
    }

    //~ Inner Classes ................................................................................................................................

    class TestStreamWriter extends StreamWriter.Default {
        private String s;

        public TestStreamWriter() {
            s = "| ";
        }

        @Override public StreamWriter writeBoolean(boolean value) {
            return write(value);
        }

        @Override public StreamWriter writeByte(byte value) {
            return write(value);
        }

        @Override public StreamWriter writeChar(char value) {
            return write(value);
        }

        @Override public StreamWriter writeDouble(double value) {
            return write(value);
        }

        @Override public StreamWriter writeFloat(float value) {
            return write(value);
        }

        @Override public StreamWriter writeInt(int value) {
            return write(value);
        }

        @Override public StreamWriter writeLong(long value) {
            return write(value);
        }

        @Override public StreamWriter writeObject(Object value) {
            return write(value);
        }

        @Override public StreamWriter writeObjectConst(Object value) {
            return write(value);
        }

        @Override public StreamWriter writeShort(short value) {
            return write(value);
        }

        @Override public StreamWriter writeString(String value) {
            return write(value);
        }

        public String getS() {
            return s;
        }

        private StreamWriter write(Object value) {
            s += String.format("%s | ", value);
            return this;
        }
    }  // end class TestStreamWriter
}  // end class FormCopyTest
