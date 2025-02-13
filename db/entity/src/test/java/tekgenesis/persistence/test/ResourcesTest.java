
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.test;

import java.io.*;
import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Resource;
import tekgenesis.common.tools.test.DbRule;
import tekgenesis.common.tools.test.DbTests;
import tekgenesis.common.tools.test.TransactionalRule;
import tekgenesis.persistence.resource.DbResourceHandler;
import tekgenesis.test.basic.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.Parameter;
import static org.junit.runners.Parameterized.Parameters;

import static tekgenesis.common.core.Constants.UTF8;
import static tekgenesis.common.media.Mime.APPLICATION_OCTET_STREAM;
import static tekgenesis.common.media.Mime.TEXT_PLAIN;
import static tekgenesis.common.tools.test.Tests.assertNotNull;

@RunWith(Parameterized.class)
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class ResourcesTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameter public String  dbName = null;
    private DbResourceHandler rh     = null;

    private final DbRule db = new DbRule(DbRule.SG, DbRule.AUTHORIZATION, DbRule.BASIC_TEST, DbRule.BASIC, DbRule.SHOWCASE, DbRule.MAIL) {
            @Override protected void before() {
                createDatabase(dbName);
                rh = new DbResourceHandler(env, database);
            }
        };

    @Rule public TestRule tr = TransactionalRule.into(db);

    //~ Methods ......................................................................................................................................

    @Test public void basicResources() {
        final Resource resource = rh.create().upload("name.txt", TEXT_PLAIN.getMime(), new StringReader(content));

        final Category category = Category.create(1);
        category.setName("Category-1");
        category.setDescr("Category-1 Description");

        final String  productId = "12345678";
        final Product product   = Product.create(productId);
        product.setModel("Product 12345678");
        product.setDescription("Product 12345678 Description");
        product.setState(State.ACTIVE);
        product.setPrice(BigDecimal.valueOf(5.5));
        product.setCategory(category);

        product.getImages().add().setImageId(resource);
        category.insert();
        product.insert();

        final Product        p1             = assertNotNull(Product.find(productId));
        final Resource.Entry storedResource = assertNotNull(p1.getImages().get(0).getImageId()).getMaster();

        assertThat(storedResource.getName()).isEqualTo("name.txt");
        assertThat(storedResource.getMimeType()).isEqualTo(TEXT_PLAIN.getMime());

        final StringWriter writer = new StringWriter();
        storedResource.copyTo(writer);
        assertThat(writer.toString()).isEqualTo(content);
    }

    @Test public void resourceGC() {
        final String   resourceContent = "Resource 1";
        final Resource r               = rh.create().upload("name", TEXT_PLAIN.getMime(), new StringReader(resourceContent));

        final ResA create = ResA.create();
        create.setName("a1");
        create.setRes(r);
        create.persist();

        rh.collect();

        assertThat(rh.findResource(r.getUuid()).isPresent()).isTrue();

        final Resource r1 = rh.create().upload("name", TEXT_PLAIN.getMime(), new StringReader(resourceContent));

        assertThat(rh.findResource(r1.getUuid()).isPresent()).isTrue();

        rh.collect();

        assertThat(rh.findResource(r1.getUuid()).isPresent()).isFalse();

        final Resource r3 = rh.create().upload("name", TEXT_PLAIN.getMime(), new StringReader(resourceContent));

        assertThat(rh.findResource(r3.getUuid()).isPresent()).isTrue();

        final ResB resB = ResB.create();
        resB.setName("b1");
        resB.setRes2(r3);
        resB.persist();
        final int id = resB.getId();

        final ResB b = assertNotNull(ResB.find(id));

        assertThat(assertNotNull(b.getRes2()).getUuid()).isNotNull();

        final long millis = System.currentTimeMillis();
        rh.collect();

        System.out.println("Gc run in " + (System.currentTimeMillis() - millis) + " ms");

        assertThat(rh.findResource(r3.getUuid()).isPresent()).isTrue();
    }  // end method resourceGC

    @Test public void testBasicAppResourcesHandler() {
        final String uuid = rh.create().upload("name", TEXT_PLAIN.getMime(), new StringReader(content)).getUuid();

        final Resource.Entry resource = rh.findResource(uuid).getOrFail("Undefined Resource").getMaster();
        final StringWriter   writer   = new StringWriter();
        resource.copyTo(writer);
        assertThat(writer.toString()).isEqualTo(content);
    }

    @Test public void testBasicAppResourcesHandlerBinary()
        throws IOException
    {
        final String uuid = rh.create()
                            .upload("name", APPLICATION_OCTET_STREAM.getMime(), new ByteArrayInputStream(content.getBytes("utf-8")))
                            .getUuid();

        final Resource.Entry        resource = rh.findResource(uuid).getOrFail("Undefined Resource").getMaster();
        final ByteArrayOutputStream baos     = new ByteArrayOutputStream(content.length());
        resource.copyTo(baos);
        baos.close();
        assertThat(baos.toString("utf-8")).isEqualTo(content);
    }

    @Test public void testResourceCopyTo()
        throws Exception
    {
        final String uuid = rh.create().upload("name", TEXT_PLAIN.getMime(), new StringReader(content)).getUuid();

        final Resource.Entry        resource = rh.findResource(uuid).getOrFail("Undefined Resource").getMaster();
        final ByteArrayOutputStream baos     = new ByteArrayOutputStream();
        resource.copyTo(baos);
        assertThat(baos.toString(UTF8)).isEqualTo(content);
    }

    //~ Methods ......................................................................................................................................

    @Parameters(name = "database : {0}")
    public static Seq<Object[]> listDbs() {
        return DbTests.listDatabases();
    }

    //~ Static Fields ................................................................................................................................

    static final String content = "THIS IS THE IMAGE";
}  // end class ResourcesTest
