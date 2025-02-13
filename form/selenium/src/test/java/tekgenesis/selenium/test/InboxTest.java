
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.selenium.test;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;
import org.openqa.selenium.remote.service.DriverService;

import tekgenesis.authorization.shiro.ShiroProps;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.inbox.Inbox;
import tekgenesis.sales.basic.Product;
import tekgenesis.sales.basic.ProductDataWorkItem;
import tekgenesis.selenium.SeleniumBrowser;
import tekgenesis.selenium.SeleniumDriverRule;
import tekgenesis.selenium.SeleniumRule;

import static org.assertj.core.api.Assertions.assertThat;

import static tekgenesis.inbox.Inbox.INBOX_FQN;
import static tekgenesis.persistence.Sql.deleteFrom;
import static tekgenesis.sales.basic.g.CategoryTable.CATEGORY;
import static tekgenesis.sales.basic.g.ProductTable.PRODUCT;
import static tekgenesis.transaction.Transaction.runInTransaction;

@Category(AppTests.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "DuplicateStringLiteralInspection", "JavaDoc", "OverlyLongMethod" })
public class InboxTest {

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

    @Test public void testBasicInbox() {
        final SeleniumDriverRule.FormDom inbox = driver.formDom(Inbox.INBOX_FQN);

        assertThat(inbox.table("inboxTable").isDisplayed()).isFalse();

        assertThat(tekgenesis.sales.basic.Category.list().list()).isEmpty();
        assertThat(Product.list().list()).isEmpty();

        inbox.button("toggleView").click();
        driver.delay();
        assertThat(inbox.table("inboxTable").isDisplayed()).isTrue();

        final SeleniumDriverRule.FormDom category = driver.formDom("tekgenesis.sales.basic.CategoryForm");
        category.textField("id").setValue("1");
        final String tvs = "Televisores";
        category.textField("name").setValue(tvs);
        category.saveAndStay();

        final SeleniumDriverRule.FormDom product = driver.formDom("tekgenesis.sales.basic.ProductForm");
        product.textField("productId").setValue("180");
        product.textField("model").setValue("LED TV");
        product.textField("price").setValue("9999");
        product.comboBox("state").setValue("Created");
        product.suggestBox("category").setValue(tvs);
        product.saveAndStay();

        assertThat(tekgenesis.sales.basic.Category.list().list()).isNotEmpty();
        assertThat(Product.list().list()).isNotEmpty();
        assertThat(ProductDataWorkItem.list().list()).isNotEmpty();

        final SeleniumDriverRule.FormDom  inboxWithItems = driver.formDom(INBOX_FQN);
        final SeleniumDriverRule.TableDom tableWithItems = inboxWithItems.table("inboxTable");
        assertThat(tableWithItems.isDisplayed()).isTrue();
        assertThat(tableWithItems.rowCount()).isEqualTo(1);

        assertThat(tableWithItems.row(0).display("assigneeT").getText()).isEqualTo("admin");
        assertThat(tableWithItems.row(0).display("descriptionT").getText()).startsWith("This is a great description");
    }

    //~ Methods ......................................................................................................................................

    @Parameterized.Parameters(name = "browser : {0}")
    public static Seq<Object[]> listBrowsers() {
        return ImmutableList.<Object[]>of(new Object[] { SeleniumBrowser.CHROME }, new Object[] { SeleniumBrowser.FIREFOX });
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static SeleniumRule service = new SeleniumRule() {
            @Override protected void resetEnv() {
                super.resetEnv();
                final ShiroProps shiroProps = new ShiroProps();
                shiroProps.builtInAuthentication = true;
                env.put("", shiroProps);
            }
        };
}  // end class InboxTest
