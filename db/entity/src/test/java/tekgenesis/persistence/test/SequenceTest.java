
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateOnly;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.test.basic.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.tools.test.Tests.assertNotNull;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class SequenceTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String dbName = null;
    private final DbRule     db     = new DbRule() {
            @Override protected void before() {
                createDatabase(dbName);
            }
        };

    @Rule public TestRule r = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void sequenceTestBasic() {
        final SeqInvoice invoice = SeqInvoice.create(BigDecimal.ONE);
        invoice.setStatement("Invoice Statement");
        invoice.setDate(DateOnly.current());

        final SeqProduct product = SeqProduct.create("12345678");
        product.setDescription("Product Description");

        final SeqItem item = SeqItem.create();
        item.setInvoice(invoice);
        item.setProduct(product);
        item.setQty(1);

        invoice.insert();
        product.insert();
        item.insert();

        final SeqItem seqItem = assertNotNull(SeqItem.find(item.getId()));
        assertThat(seqItem.getInvoice()).isEqualTo(invoice);
        assertThat(seqItem.getProduct()).isEqualTo(product);
    }

    @Test public void sequenceTestComplex() {
        final SeqObject obj = SeqObject.create();
        obj.setValue("Object value");
        obj.insert();

        final SeqProperties properties = SeqProperties.create();
        properties.setName("Name properties");
        properties.setDescription("Description properties");
        final SeqProperty property = properties.getProperties().add();
        property.setName("Property name");
        final SeqValue value = property.getValues().add();
        value.setName("Value name");
        value.setValue(obj);

        properties.insert();

        final SeqOptionalProperties optProperties = SeqOptionalProperties.create();
        optProperties.setName("Optional name");
        optProperties.setProperties(properties);

        optProperties.insert();

        final SeqOptionalProperties ops  = assertNotNull(SeqOptionalProperties.find(optProperties.getId()));
        final SeqProperty           prop = ops.getProperties().getProperties().get(0);
        assertThat(prop.getValues().get(0).getValue().getValue()).isEqualTo(obj.getValue());
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }
}  // end class SequenceTest
