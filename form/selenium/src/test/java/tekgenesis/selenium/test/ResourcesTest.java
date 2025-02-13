
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.selenium.test;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.Parameterized;
import org.openqa.selenium.remote.service.DriverService;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.selenium.SeleniumBrowser;
import tekgenesis.selenium.SeleniumDriverRule;
import tekgenesis.selenium.SeleniumDriverRule.FormDom;
import tekgenesis.selenium.SeleniumDriverRule.RowDom;
import tekgenesis.selenium.SeleniumDriverRule.TableDom;
import tekgenesis.selenium.SeleniumRule;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.persistence.Sql.deleteFrom;
import static tekgenesis.sales.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.sales.basic.g.ProductTable.PRODUCT;
import static tekgenesis.transaction.Transaction.runInTransaction;

@Category(AppTests.class)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc" })
public class ResourcesTest {

    //~ Instance Fields ..............................................................................................................................

    @Parameterized.Parameter public SeleniumBrowser browserName = SeleniumBrowser.CHROME;

    @Rule public SeleniumDriverRule driver = new SeleniumDriverRule() {
            @Override public DriverService getService() {
                return service.getService();
            }
            @Override protected SeleniumBrowser getBrowser() {
                return browserName;
            }
        };

    //~ Methods ......................................................................................................................................

    @Before public void beforeEach() {  //
        runInTransaction(() -> {
            deleteFrom(CATEGORY).execute();
            deleteFrom(PRODUCT).execute();
        });
    }

    @Test public void productResourcesTest() {
        final FormDom category = driver.formDom("tekgenesis.sales.basic.CategoryForm");
        category.textField("id").setValue("1");
        final String tvs = "Televisores";
        category.textField("name").setValue(tvs);
        assertThat(category.textArea("descr").getText()).isEqualTo(tvs);

        category.saveAndStay();

        final FormDom product = driver.formDom("tekgenesis.sales.basic.ProductForm");
        product.textField("productId").setValue("1");
        product.textField("model").setValue("1");
        product.textField("price").setValue("1");
        product.suggestBox("category").setValue(tvs);

        product.upload("gallery").upload("http://www.estudios-electricos.com/uploads/archivos/asd_cy.pdf");
        product.upload("gallery").upload(Constants.TEK_LOGO);
        product.upload("gallery").upload("https://www.google.com/images/srpr/logo6w.png");

        product.saveAndStay();

        final FormDom load = driver.formDom("tekgenesis.sales.basic.ProductForm", "1");

        assertThat(load.upload("gallery").count()).isEqualTo(3);
    }

    @Test public void tableImagesTest() {
        final FormDom f = driver.formDom("tekgenesis.showcase.TableImageForm");

        f.textField("id").setValue("1");

        f.button("addRowBottom").click();
        final TableDom table = f.table("imgs");
        final RowDom   row   = table.row(table.rowCount() - 1);
        driver.delay();

        row.textField("imgsName").setValue("Google");
        row.upload("imgsImg").upload("http://www.google.com/images/srpr/logo6w.png");

        f.saveAndStay();

        final FormDom  newForm  = driver.formDom("tekgenesis.showcase.TableImageForm", "1");
        final TableDom newTable = newForm.table("imgs");
        final RowDom   newRow   = newTable.row(newTable.rowCount() - 1);
        driver.delay();

        assertThat(newRow.textField("imgsName").getText()).isEqualTo("Google");
        assertThat(newRow.upload("imgsImg").count()).isEqualTo(1);
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "browser : {0}")
    public static Seq<Object[]> listBrowsers() {
        return ImmutableList.<Object[]>of(new Object[] { SeleniumBrowser.CHROME }, new Object[] { SeleniumBrowser.FIREFOX });
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static SeleniumRule service = new SeleniumRule();
}  // end class ResourcesTest
