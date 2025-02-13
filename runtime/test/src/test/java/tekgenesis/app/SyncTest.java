
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;

import tekgenesis.app.properties.ApplicationProps;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Resource;
import tekgenesis.common.core.Resource.Entry;
import tekgenesis.common.core.Resource.Factory;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.tools.test.AppTests;
import tekgenesis.common.tools.test.ApplicationRule;
import tekgenesis.common.util.Reflection;
import tekgenesis.database.Database;
import tekgenesis.database.Databases;
import tekgenesis.database.support.JdbcUtils;
import tekgenesis.index.SearchResult;
import tekgenesis.persistence.ResourceHandler;
import tekgenesis.properties.SchemaProps;
import tekgenesis.sales.basic.*;
import tekgenesis.service.ServiceManager;
import tekgenesis.task.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.media.Mime.IMAGE_PNG;
import static tekgenesis.common.media.Mime.TEXT_PLAIN;
import static tekgenesis.persistence.Sql.selectFrom;
import static tekgenesis.sales.basic.g.CategoryDefaultViewTable.CATEGORY_DEFAULT_VIEW;
import static tekgenesis.sales.basic.g.CategoryViewTable.CATEGORY_VIEW;
import static tekgenesis.sales.basic.g.ProductViewSearcherBase.PRODUCT_VIEW_SEARCHER;
import static tekgenesis.transaction.Transaction.invokeInTransaction;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 */
@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber", "ConstantConditions", "SpellCheckingInspection" })
@org.junit.experimental.categories.Category(AppTests.class)
public class SyncTest {

    //~ Instance Fields ..............................................................................................................................

    @Rule public TestRule clean = (statement, description) ->
                                  new Statement() {
                @Override public void evaluate()
                    throws Throwable
                {
                    cleanRecords();
                    statement.evaluate();
                }
            };

    //~ Methods ......................................................................................................................................

    @Test public void sync()
        throws ExecutionException, InterruptedException
    {
        cleanRecords();
        final ScheduledTask task = createRefreshRemoteViewTask();

        runInTransaction(() -> {
            final Category category = Category.create(1);
            category.setName("Teles");
            category.setDescr("Televisores");

            category.insert();
        });

        submitTask(task);

        runInTransaction(() -> {
            final CategoryView find = CategoryView.find(1);
            assertThat(find).isNotNull();
            assertThat(find.getVdescr()).isEqualTo("Televisores");
            assertThat(find.getVname()).isEqualTo("Teles");

            final Category category1 = Category.create(2);
            category1.setName("Planchas");
            category1.setDescr("Planchas");

            category1.insert();
        });

        task.call();

        runInTransaction(() -> {
            assertThat(CategoryView.list().count()).isEqualTo(2);
            final CategoryView categoryView = CategoryView.find(1);
            assertThat(categoryView).isNotNull();
            assertThat(categoryView.getVdescr()).isEqualTo("Televisores");

            final CategoryView planchas = CategoryView.find(2);
            assertThat(planchas).isNotNull();
            assertThat(planchas.getVdescr()).isEqualTo("Planchas");

            Category.find(2).delete();
        });

        task.call();

        runInTransaction(() -> {
            assertThat(CategoryView.list().count()).isEqualTo(1);
            final CategoryView view = CategoryView.find(1);
            assertThat(view).isNotNull();
            assertThat(view.getVdescr()).isEqualTo("Televisores");

            final CategoryView view2 = CategoryView.find(2);
            assertThat(view2).isEqualTo(null);

            assertThat(CategorySqlViewView.list().count()).isEqualTo(1);
        });
    }  // end method sync

    @Test public void syncCountries()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        final Country ar = Country.create("AR");
        ar.setName("Argentina");
        runInTransaction(ar::insert);

        final StateProvince bsAs = StateProvince.create("AR", "B");
        bsAs.setName("Buenos Aires");
        runInTransaction(() -> {
            bsAs.insert();

            final City city = City.create();
            city.setName("Olivos");
            city.setStateProvince(bsAs);
            city.insert();
        });

        submitTask(task);

        runInTransaction(() -> {
            final ImmutableList<CityView> cityViews = CityView.list().list();
            assertThat(cityViews).isNotEmpty();

            final CityView cityView = cityViews.getFirst().get();
            assertThat(cityView.getName()).isEqualTo("Olivos");
            assertThat(cityView.getStateProvinceCode()).isEqualTo("B");
            assertThat(cityView.getStateProvinceCountryIso()).isEqualTo("AR");
        });
    }

    @Test public void syncDefaultKey()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        final CategoryDefault category = CategoryDefault.create();
        category.setName("Teles");
        category.setDescr("Televisores");

        runInTransaction((Runnable) category::insert);

        submitTask(task);

        final CategoryDefault categoryDefault = CategoryDefault.create();

        runInTransaction(() -> {
            final CategoryDefaultView find = CategoryDefaultView.list().get();
            assertThat(find).isNotNull();
            assertThat(find.getVdescr()).isEqualTo("Televisores");
            assertThat(find.getVname()).isEqualTo("Teles");

            categoryDefault.setName("Planchas");
            categoryDefault.setDescr("Planchas");

            categoryDefault.insert();
        });

        submitTask(task);

        runInTransaction(() -> {
            assertThat(CategoryDefaultView.list().count()).isEqualTo(2);
            final CategoryDefaultView categoryDefaultView = selectFrom(CATEGORY_DEFAULT_VIEW).orderBy(CATEGORY_DEFAULT_VIEW.VNAME.descending()).get();
            assertThat(categoryDefaultView).isNotNull();
            assertThat(categoryDefaultView.getVdescr()).isEqualTo("Televisores");

            final CategoryDefaultView planchas = CategoryDefaultView.find(categoryDefault.getId());
            assertThat(planchas).isNotNull();
            assertThat(planchas.getVdescr()).isEqualTo("Planchas");

            CategoryDefault.find(categoryDefault.getId()).delete();
        });

        submitTask(task);

        runInTransaction(() -> {
            assertThat(CategoryDefaultView.list().count()).isEqualTo(1);
            final CategoryDefaultView categoryDefaultView2 = CategoryDefaultView.list().get();
            assertThat(categoryDefaultView2).isNotNull();
            assertThat(categoryDefaultView2.getVdescr()).isEqualTo("Televisores");

            final CategoryDefaultView defaultView = CategoryDefaultView.find(categoryDefault.getId());
            assertThat(defaultView).isEqualTo(null);
        });
    }  // end method syncDefaultKey

    @Test public void syncDeprecable()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        final CategoryDefault category = CategoryDefault.create();
        category.setName("Teles");
        category.setDescr("Televisores");
        category.deprecate(true);

        runInTransaction((Runnable) category::insert);

        final DateTime deprecationTime = invokeInTransaction(() -> CategoryDefault.find(category.getId()).getDeprecationTime());

        submitTask(task);

        runInTransaction(() -> {
            final CategoryDefaultView find = CategoryDefaultView.find(category.getId());
            assertThat(find).isNotNull();
            assertThat(find.getVdescr()).isEqualTo("Televisores");
            assertThat(find.getDeprecationTime()).isEqualTo(deprecationTime);
        });
    }

    @Test public void syncEntityFields()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        final Category category = Category.create(1);
        final Category led      = Category.create(2);

        runInTransaction(() -> {
            category.setName("Teles");
            category.setDescr("Televisores");

            category.persist();
            led.setName("LED");
            led.setDescr("LED");
            led.persist();
        });

        final Product product = Product.create("1");
        runInTransaction(() -> {
            product.setDescription("Sony 40");
            product.setCategory(category);
            final ProductByCat add = product.getSecondary().add();
            add.setSecondaryCategory(led);
            product.setCategoryAtt();
            product.setTags(EnumSet.of(Tag.WEB, Tag.OUTLET));
            product.insert();
        });
        submitTask(task);

        try {
            Thread.sleep(2000);
        }
        catch (final InterruptedException e) {
            // ignore Wait for index to update
        }

        runInTransaction(() -> {
            final CategoryView find = CategoryView.find(1);
            assertThat(find).isNotNull();
            assertThat(find.getVdescr()).isEqualTo("Televisores");

            final ProductView prod = ProductView.find("1");
            assertThat(prod).isNotEqualTo(null);
            assertThat(prod.getVcategory().getVid()).isEqualTo(1);
            assertThat(prod.getTags()).contains(Tag.WEB, Tag.OUTLET);
        });
        final ProductViewSearcher searcher = new ProductViewSearcher();
        final List<SearchResult>  search   = searcher.search("", PRODUCT_VIEW_SEARCHER.CATEGORY_ATT.eq("LCD"), 10);
        assertThat(search).hasSize(0);
        final List<SearchResult> search2 = searcher.search("", PRODUCT_VIEW_SEARCHER.CATEGORY_ATT.eq("LED"), 10);
        assertThat(search2).hasSize(1);
    }  // end method syncEntityFields

    @SuppressWarnings("OverlyLongMethod")
    @Test public void syncEntityFieldsClob()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        final CategoryDefault category = CategoryDefault.create();
        category.setName("Teles");
        category.setDescr("Televisores");

        runInTransaction(category::persist);

        final ProductDefault product = ProductDefault.create();
        product.setDescription("Sony 40");
        product.setMainCategory(category);

        final StringBuilder comments = new StringBuilder();
        for (int i = 1; i < 5000; i++)
            comments.append("c");

        product.setComments(comments.toString());
        runInTransaction((Runnable) product::insert);

        submitTask(task);

        runInTransaction(() -> {
            final CategoryDefaultView find = CategoryDefaultView.find(category.getId());
            assertThat(find).isNotNull();
            assertThat(find.getVdescr()).isEqualTo("Televisores");

            final ProductDefaultView prod = ProductDefaultView.find(product.getId());
            assertThat(prod).isNotEqualTo(null);
            assertThat(prod.getVCategory().getVdescr()).isEqualTo(category.getDescr());
            assertThat(prod.getComments()).isEqualTo(comments.toString());

            product.setComments(comments.toString());
            product.setDescription("New Description");
            product.update();
        });
        submitTask(task);

        runInTransaction(() -> {
            final ProductDefaultView prod2 = ProductDefaultView.find(product.getId());
            assertThat(prod2).isNotEqualTo(null);
            assertThat(prod2.getVCategory().getVdescr()).isEqualTo(category.getDescr());
            assertThat(prod2.getComments()).isEqualTo(comments.toString());
        });
    }  // end method syncEntityFieldsClob

    @Test public void syncEntityFieldsDefaultKey()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        final CategoryDefault category = CategoryDefault.create();
        category.setName("Teles");
        category.setDescr("Televisores");

        runInTransaction(category::persist);

        final ProductDefault product = ProductDefault.create();
        product.setDescription("Sony 40");
        product.setMainCategory(category);

        runInTransaction(() -> {
            product.insert();

            final Review review = Review.create();
            review.setProduct(product);
            review.setReview("Muy bueno el producto!;");
            review.insert();

            final Review review2 = Review.create();
            review2.setProduct(product);
            review2.setReview("Malísimo el producto;");
            review2.insert();
        });

        submitTask(task);

        runInTransaction(() -> {
            final CategoryDefaultView find = CategoryDefaultView.find(category.getId());
            assertThat(find).isNotNull();
            assertThat(find.getVdescr()).isEqualTo("Televisores");

            final ProductDefaultView prod = ProductDefaultView.find(product.getId());
            assertThat(prod).isNotEqualTo(null);
            assertThat(prod.getVCategory().getVdescr()).isEqualTo(category.getDescr());

            assertThat(prod.getRevs().size()).isEqualTo(2);
        });
    }  // end method syncEntityFieldsDefaultKey

    @Test public void syncEntityFieldsInners()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        final CategoryDefault category = CategoryDefault.create();
        category.setName("Teles");
        category.setDescr("Televisores");

        runInTransaction(category::persist);

        runInTransaction(() -> {
            final ProductDefaultInners product = ProductDefaultInners.create();
            product.setDescription("Sony 40");
            product.setMainCategory(category);

            product.insert();

            product.getReviews().merge(listOf("Muy bueno el producto!;", "Malísimo el producto;"), Rev::setReview);

            product.update();
        });

        submitTask(task);

        runInTransaction(() -> {
            final CategoryDefaultView find = CategoryDefaultView.find(category.getId());
            assertThat(find).isNotNull();
            assertThat(find.getVdescr()).isEqualTo("Televisores");

            final ProductDefaultViewInners prod = ProductDefaultViewInners.list().get();
            assertThat(prod).isNotEqualTo(null);
            assertThat(prod.getVCategory().getVdescr()).isEqualTo(category.getDescr());

            assertThat(prod.getRevsInner().size()).isEqualTo(2);
        });
    }  // end method syncEntityFieldsInners
    @SuppressWarnings("OverlyLongMethod")
    @Test public void syncEntityFieldsResources()
        throws IOException, ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        final CategoryDefault category = CategoryDefault.create();
        category.setName("Teles");
        category.setDescr("Televisores");

        runInTransaction(category::persist);

        final ProductDefault product = ProductDefault.create();
        product.setDescription("Sony 40");
        product.setMainCategory(category);
        final ResourceHandler handler = Context.getSingleton(ResourceHandler.class);
        final Factory         factory = handler.create();

        final Resource resource = factory.upload("Resource.txt", TEXT_PLAIN.getMime(), new StringReader("Hola"));
        product.setImage(resource);
        resource.addVariant("Short").upload("Resource.txt", TEXT_PLAIN.getMime(), new StringReader("Hi"));
        resource.addVariant("Ext").upload("Resource.txt", "http://google.com");

        runInTransaction((Runnable) product::insert);

        final ProductDefault product2 = ProductDefault.create();
        product2.setDescription("Sony 45");
        product2.setMainCategory(category);

        final Factory  factory2  = handler.create();
        final Resource resource2 = factory2.upload("Cart.png",
                IMAGE_PNG.getMime(),
                Thread.currentThread().getContextClassLoader().getResourceAsStream("public/img/cart.png"));
        product2.setImage(resource2);
        resource2.addVariant("Large")
            .upload("Cart.png", IMAGE_PNG.getMime(), Thread.currentThread().getContextClassLoader().getResourceAsStream("public/img/g-loading.png"));

        runInTransaction((Runnable) product2::insert);

        submitTask(task);

        final Tuple<Entry, Entry> entries = invokeInTransaction(() -> {
                final CategoryDefaultView find = CategoryDefaultView.find(category.getId());
                assertThat(find).isNotNull();
                assertThat(find.getVdescr()).isEqualTo("Televisores");

                final ProductDefaultView prod = ProductDefaultView.find(product.getId());
                assertThat(prod).isNotEqualTo(null);
                assertThat(prod.getVCategory().getVdescr()).isEqualTo(category.getDescr());

                assertThat(prod.getImage().getEntries().size()).isEqualTo(3);
                final Entry m = prod.getImage().getMaster();
                assertThat(m.isExternal()).isTrue();
                final Entry s = prod.getImage().getEntry("Short");
                assertThat(s.isExternal()).isTrue();
                assertThat(prod.getImage().getEntry("Ext").getUrl()).isEqualTo("http://google.com");
                return tuple(m, s);
            });

        final Entry             master        = entries.first();
        final Entry             shortEntry    = entries.second();
        final URL               urlConnection = new URL(master.getUrl());
        final HttpURLConnection connection    = (HttpURLConnection) urlConnection.openConnection();
        assertThat(connection.getResponseCode()).isEqualTo(HttpURLConnection.HTTP_OK);

        final InputStream inputStream = connection.getInputStream();
        final byte[]      bytes       = new byte[4];
        inputStream.read(bytes);
        inputStream.close();
        connection.disconnect();

        assertThat(new String(bytes, "utf8")).isEqualTo("Hola");

        final URL               url               = new URL(shortEntry.getUrl());
        final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        assertThat(httpURLConnection.getResponseCode()).isEqualTo(HttpURLConnection.HTTP_OK);

        final InputStream stream = httpURLConnection.getInputStream();
        final byte[]      bytes1 = new byte[2];
        stream.read(bytes1);
        stream.close();
        httpURLConnection.disconnect();

        assertThat(new String(bytes1, "utf8")).isEqualTo("Hi");

        runInTransaction(() -> {
            final ProductDefaultView prod2 = ProductDefaultView.find(product2.getId());
            assertThat(prod2).isNotEqualTo(null);
            assertThat(prod2.getVCategory().getVdescr()).isEqualTo(category.getDescr());

            assertThat(prod2.getImage().getEntries().size()).isEqualTo(2);
            final Entry master2 = prod2.getImage().getMaster();
            assertThat(master2.isExternal()).isTrue();
            assertThat(master2.getMetadata().getHeight()).isEqualTo(56);
            assertThat(master2.getMetadata().getWidth()).isEqualTo(56);
            assertThat(master2.getMetadata().getSize()).isEqualTo(3556);
            assertThat(prod2.getImage().getEntry("Large").getMetadata().getHeight()).isEqualTo(468);
            assertThat(prod2.getImage().getEntry("Large").getMetadata().getWidth()).isEqualTo(640);
            assertThat(prod2.getImage().getEntry("Large").getMetadata().getSize()).isEqualTo(141676);
        });
    }  // end method syncEntityFieldsResources

    @SuppressWarnings("OverlyLongMethod")
    @Test public void syncLastDeleted()
        throws ExecutionException, InterruptedException
    {
        final ArrayList<RefreshRemoteViewTaskBase.RefreshViewListener.RefreshResult> sinceSync = new ArrayList<>();
        final String                                                                 fullName  = CategoryView.class.getName();
        final RefreshRemoteViewTaskBase.RefreshViewListener                          listener  = refreshed -> sinceSync.add(refreshed.get(fullName));

        final ScheduledTask task = createRefreshRemoteViewTask();
        RefreshRemoteViewTaskBase.registerListener("tekgenesis.sales.basic", listener);

        final Category category = Category.create(1);
        category.setName("Teles");
        category.setDescr("Televisores");

        runInTransaction(category::insert);

        sinceSync.clear();
        submitTask(task);

        final Category category1 = Category.create(2);

        runInTransaction(() -> {
            final CategoryView find = CategoryView.find(1);
            assertThat(find).isNotNull();
            assertThat(find.getVdescr()).isEqualTo("Televisores");
            assertThat(find.getVname()).isEqualTo("Teles");

            assertThat(sinceSync).hasSize(1);
            assertThat(sinceSync.get(0).since()).isEqualTo(DateTime.EPOCH);
            category1.setName("Planchas");
            category1.setDescr("Planchas");

            category1.insert();
        });

        sinceSync.clear();
        submitTask(task);

        runInTransaction(() -> {
            assertThat(CategoryView.list().count()).isEqualTo(2);
            final CategoryView categoryView = CategoryView.find(1);
            assertThat(categoryView).isNotNull();
            assertThat(categoryView.getVdescr()).isEqualTo("Televisores");

            final CategoryView planchas = CategoryView.find(2);
            assertThat(planchas).isNotNull();
            assertThat(planchas.getVdescr()).isEqualTo("Planchas");

            assertThat(sinceSync).hasSize(1);
            assertThat(sinceSync.get(0).since().toString()).isEqualTo(Category.find(category.getIdKey()).getUpdateTime().toString());

            sinceSync.clear();
        });
        submitTask(task);
        assertThat(sinceSync).isEmpty();

        runInTransaction(() -> Category.find(category1.getIdKey()).delete());
        sinceSync.clear();
        submitTask(task);

        assertThat(sinceSync).hasSize(1);

        runInTransaction(() -> {
            assertThat(CategoryView.list().count()).isEqualTo(1);
            final CategoryView view = CategoryView.find(1);
            assertThat(view).isNotNull();
            assertThat(view.getVdescr()).isEqualTo("Televisores");
        });
        sinceSync.clear();
        submitTask(task);

        assertThat(sinceSync).isEmpty();

        final Category category3 = Category.create(3);
        category3.setName("Cocinas");
        category3.setDescr("Cocinas");

        runInTransaction(category3::insert);

        sinceSync.clear();
        submitTask(task);

        assertThat(sinceSync).hasSize(1);

        runInTransaction(() -> {
            assertThat(CategoryView.list().count()).isEqualTo(2);
            final CategoryView view2 = CategoryView.find(3);
            assertThat(view2).isNotNull();
            assertThat(view2.getVdescr()).isEqualTo("Cocinas");
        });

        RefreshRemoteViewTaskBase.unRegisterListener(fullName, listener);
    }  // end method syncLastDeleted

    @SuppressWarnings("OverlyLongMethod")
    @Test public void syncListener()
        throws ExecutionException, InterruptedException
    {
        final ArrayList<RefreshRemoteViewTaskBase.RefreshViewListener.RefreshResult> sinceSync = new ArrayList<>();
        final RefreshRemoteViewTaskBase.RefreshViewListener                          listener  = refreshed -> sinceSync.addAll(refreshed.values());

        final String        domainName = "tekgenesis.sales.basic";
        final ScheduledTask task       = createRefreshRemoteViewTask();
        RefreshRemoteViewTaskBase.registerListener(domainName, listener);

        final Category category = Category.create(1);
        category.setName("Teles");
        category.setDescr("Televisores");

        runInTransaction(category::insert);

        submitTask(task);

        final Category category1 = Category.create(2);

        runInTransaction(() -> {
            final CategoryView find = CategoryView.find(1);
            assertThat(find).isNotNull();
            assertThat(find.getVdescr()).isEqualTo("Televisores");
            assertThat(find.getVname()).isEqualTo("Teles");

            assertThat(sinceSync.size()).isEqualTo(2);
            assertThat(sinceSync.get(0).since()).isEqualTo(DateTime.EPOCH);
            assertThat(sinceSync.get(1).since()).isEqualTo(DateTime.EPOCH);

            category1.setName("Planchas");
            category1.setDescr("Planchas");

            category1.insert();
        });

        sinceSync.clear();
        submitTask(task);

        runInTransaction(() -> {
            assertThat(CategoryView.list().count()).isEqualTo(2);
            final CategoryView categoryView = CategoryView.find(1);
            assertThat(categoryView).isNotNull();
            assertThat(categoryView.getVdescr()).isEqualTo("Televisores");

            final CategoryView planchas = CategoryView.find(2);
            assertThat(planchas).isNotNull();
            assertThat(planchas.getVdescr()).isEqualTo("Planchas");

            assertThat(sinceSync).hasSize(2);
            assertThat(sinceSync.get(0).since().toString()).isEqualTo(Category.find(category.getIdKey()).getUpdateTime().toString());
            assertThat(sinceSync.get(0).force()).isFalse();

            final Category category2 = Category.find(category1.getIdKey());
            category2.delete();
            category2.delete();
        });
        sinceSync.clear();
        submitTask(task);

        assertThat(sinceSync).hasSize(2);
        assertThat(sinceSync.get(0).since().toMilliseconds()).isEqualTo(sinceSync.get(1).since().toMilliseconds());
        assertThat(sinceSync.get(0).force()).isTrue();
        assertThat(sinceSync.get(1).force()).isFalse();
        assertThat(sinceSync.get(1).removedKeys()).containsExactly(category1.keyAsString());
        runInTransaction(() -> {
            assertThat(CategoryView.list().count()).isEqualTo(1);
            final CategoryView view = CategoryView.find(1);
            assertThat(view).isNotNull();
            assertThat(view.getVdescr()).isEqualTo("Televisores");

            final CategoryView view2 = CategoryView.find(2);
            assertThat(view2).isEqualTo(null);
        });
        final String fullName = CategoryView.class.getName();
        RefreshRemoteViewTaskBase.unRegisterListener(fullName, listener);
    }  // end method syncListener

    @SuppressWarnings("OverlyLongMethod")
    @Test public void syncListenerComposite()
        throws ExecutionException, InterruptedException
    {
        final ArrayList<RefreshRemoteViewTaskBase.RefreshViewListener.RefreshResult> sinceSync = new ArrayList<>();
        final String                                                                 fullName  = CategoryCompositeView.class.getName();
        final RefreshRemoteViewTaskBase.RefreshViewListener                          listener  = refreshed -> sinceSync.add(refreshed.get(fullName));

        final String        domainName = "tekgenesis.sales.basic";
        final ScheduledTask task       = createRefreshRemoteViewTask();
        RefreshRemoteViewTaskBase.registerListener(domainName, listener);

        final CategoryComposite category = CategoryComposite.create(1, "Teles", "TelesShort");
        category.setName("Teles");

        runInTransaction(category::insert);

        submitTask(task);

        final CategoryComposite category1 = CategoryComposite.create(2, "Planchas", "APlanchasShort");
        runInTransaction(() -> {
            final CategoryCompositeView find = CategoryCompositeView.find(1, "Teles", "TelesShort");
            assertThat(find).isNotNull();
            assertThat(find.getVdescr()).isEqualTo("Teles");
            assertThat(find.getVname()).isEqualTo("Teles");
            assertThat(find.getVshort()).isEqualTo("TelesShort");

            assertThat(sinceSync.size()).isEqualTo(1);
            assertThat(sinceSync.get(0).since()).isEqualTo(DateTime.EPOCH);

            category1.setName("Planchas");

            category1.insert();
        });

        sinceSync.clear();
        submitTask(task);

        runInTransaction(() -> {
            assertThat(CategoryComposite.list().count()).isEqualTo(2);
            final CategoryCompositeView categoryView = CategoryCompositeView.find(1, "Teles", "TelesShort");
            assertThat(categoryView).isNotNull();
            assertThat(categoryView.getVdescr()).isEqualTo("Teles");

            final CategoryCompositeView planchas = CategoryCompositeView.find(2, "Planchas", "APlanchasShort");
            assertThat(planchas).isNotNull();
            assertThat(planchas.getVdescr()).isEqualTo("Planchas");
            assertThat(planchas.getVshort()).isEqualTo("APlanchasShort");

            assertThat(sinceSync).hasSize(1);
            assertThat(sinceSync.get(0).since().toString()).isEqualTo(
                CategoryComposite.find(category.getIdKey(), category.getDescr(), category.getShortDesc()).getUpdateTime().toString());
            assertThat(sinceSync.get(0).force()).isFalse();

            final CategoryComposite category2 = CategoryComposite.create(0, "Leds", "LEDShort");
            category2.setName("Planchas");

            category2.insert();
        });

        sinceSync.clear();
        submitTask(task);
        assertThat(sinceSync).hasSize(1);

        runInTransaction(() -> {
            assertThat(CategoryCompositeView.list().count()).isEqualTo(3);

            CategoryComposite.find(category1.getIdKey(), category1.getDescr(), category1.getShortDesc()).delete();
        });

        submitTask(task);

        assertThat(sinceSync).hasSize(2);
        assertThat(sinceSync.get(0).since().toMilliseconds()).isLessThan(sinceSync.get(1).since().toMilliseconds());
        assertThat(sinceSync.get(1).force()).isFalse();

        runInTransaction(() -> {
            assertThat(CategoryCompositeView.list().count()).isEqualTo(2);
            final CategoryCompositeView view = CategoryCompositeView.find(1, "Teles", "TelesShort");
            assertThat(view).isNotNull();
            assertThat(view.getVdescr()).isEqualTo("Teles");

            final CategoryCompositeView view2 = CategoryCompositeView.find(2, "Planchas", "APlanchasShort");
            assertThat(view2).isEqualTo(null);
        });

        RefreshRemoteViewTaskBase.unRegisterListener(fullName, listener);
    }  // end method syncListenerComposite

    @Test public void syncMoreThan10000()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        runInTransaction(() -> {
            for (int i = 1; i <= 40000; i++) {
                final Category category = Category.create(i);
                category.setName("Teles" + i);
                category.setDescr("Televisores;" + i);

                category.persist();
            }
        });

        submitTask(task);

        runInTransaction(() -> assertThat(CategoryView.list().count()).isEqualTo(40000));
    }

    @Test public void syncMoreThan10000BorderCase()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        runInTransaction(() -> {
            for (int i = 1; i <= 11000; i++) {
                final Category category = Category.create(i);
                category.setName("Teles" + i);
                category.setDescr("Televisores;" + i);

                category.persist();
            }
        });

        runInTransaction(() -> {
            final DateTime  updateTime  = Category.find(9996).getUpdateTime();
            final Timestamp toTimestamp = JdbcUtils.toTimestamp(updateTime.addSeconds(1));
            toTimestamp.setNanos(0);
            final Database db = Databases.openDefault();

            db.sqlStatement("update QName(BASIC, CATEGORY) set UPDATE_TIME = ? where ID_KEY = 9996").onArgs(toTimestamp).execute();
            db.sqlStatement("update QName(BASIC, CATEGORY) set UPDATE_TIME = ? where ID_KEY = 9997").onArgs(toTimestamp).execute();
            db.sqlStatement("update QName(BASIC, CATEGORY) set UPDATE_TIME = ? where ID_KEY = 9998").onArgs(toTimestamp).execute();
            db.sqlStatement("update QName(BASIC, CATEGORY) set UPDATE_TIME = ? where ID_KEY = 9999").onArgs(toTimestamp).execute();

            final Timestamp timestamp = JdbcUtils.toTimestamp(updateTime.addSeconds(2));
            db.sqlStatement("update QName(BASIC, CATEGORY) set UPDATE_TIME = ? where ID_KEY = 10000").onArgs(timestamp).execute();
            final Timestamp timestamp1 = JdbcUtils.toTimestamp(updateTime.addSeconds(3));

            db.sqlStatement("update QName(BASIC, CATEGORY) set UPDATE_TIME = ? where ID_KEY > 10000").onArgs(timestamp1).execute();
        });

        submitTask(task);

        runInTransaction(() -> assertThat(CategoryView.list().count()).isEqualTo(11000));
    }  // end method syncMoreThan10000BorderCase

    @Test public void syncMoreThan10000Force()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        runInTransaction(() -> {
            for (int i = 1; i <= 15000; i++) {
                final Category category = Category.create(i);
                category.setName("Teles" + i);
                category.setDescr("Televisores;" + i);

                category.persist();
            }
        });

        final TaskService taskService = getTaskService();
        taskService.submit(task).get();

        runInTransaction(() -> assertThat(CategoryView.list().count()).isEqualTo(15000));

        final Database db = Databases.openDefault();
        runInTransaction(() -> db.sqlStatement("update QName(BASIC, CATEGORY_VIEW) set UPDATE_TIME = ? ").onArgs(DateTime.EPOCH).execute());

        submitTask(task);

        runInTransaction(() -> {
            assertThat(CategoryView.list().count()).isEqualTo(15000);

            assertThat(CategoryView.list().where(CATEGORY_VIEW.UPDATE_TIME.eq(DateTime.EPOCH)).count()).isEqualTo(0);
        });
    }

    @Test public void syncMoreThan10000SameUpdateTime()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        final DateTime now = DateTime.current();
        runInTransaction(() -> {
            for (int i = 1; i <= 40000; i++) {
                final Category category = Category.create(i);
                category.setName("Teles" + i);
                category.setDescr("Televisores;" + i);

                category.persist();
            }
        });

        runInTransaction(() -> {
            final Database db = Databases.openDefault();

            db.sqlStatement("update QName(BASIC, CATEGORY) set UPDATE_TIME = ? ").onArgs(now).execute();
        });

        submitTask(task);

        runInTransaction(() -> assertThat(CategoryView.list().count()).isEqualTo(40000));
    }
    @Test public void syncMoreThan10000SameUpdateTimeCompositeKey()
        throws ExecutionException, InterruptedException
    {
        final ScheduledTask task = createRefreshRemoteViewTask();

        final DateTime now = DateTime.current();
        runInTransaction(() -> {
            for (int i = 1; i <= 40000; i++) {
                final CategoryComposite category = CategoryComposite.create(i, "desc" + i, "shortDesc" + i);

                category.setName("Teles" + i);
                category.persist();
            }
        });
        final Database db = Databases.openDefault();

        runInTransaction(() -> db.sqlStatement("update QName(BASIC, CATEGORY_COMPOSITE) set UPDATE_TIME = ? ").onArgs(now).execute());

        submitTask(task);

        runInTransaction(() -> assertThat(CategoryCompositeView.list().count()).isEqualTo(40000));
    }

    @Test public void syncMoreThan10000SameUpdateTimeStringKey()
        throws ExecutionException, InterruptedException
    {
        cleanRecords();
        final ScheduledTask task = createRefreshRemoteViewTask();

        final DateTime now = DateTime.current();
        runInTransaction(() -> {
            final Category category = Category.create(1);
            category.setName("Teles");
            category.setDescr("Televisores");
            category.insert();

            for (int i = 1; i <= 20000; i++) {
                final Product p = Product.create(i + "Pr");
                p.setDescription(i + " Product ");
                p.setCategory(category);
                p.insert();
            }
            for (int i = 1; i <= 20000; i++) {
                final Product p = Product.create("Pr" + i);
                p.setDescription("Product " + i);
                p.setCategory(category);
                p.insert();
            }
        });

        runInTransaction(() -> {
            final Database db = Databases.openDefault();

            db.sqlStatement("update QName(BASIC, CATEGORY) set UPDATE_TIME = ? ").onArgs(now).execute();
        });

        submitTask(task);

        runInTransaction(() -> assertThat(ProductView.list().count()).isEqualTo(40000));
    }

    private ScheduledTask createRefreshRemoteViewTask() {
        return ScheduledTask.createFromFqn("tekgenesis.sales.basic.TekgenesisSalesBasicRefreshRemoteViewTask").get();
    }

    private void submitTask(ScheduledTask task)
        throws InterruptedException, ExecutionException
    {
        try {
            getTaskService().submit(task).get(10, TimeUnit.MINUTES);
        }
        catch (final TimeoutException e) {
            fail("Task lasted more than one minute");
        }
    }

    private TaskService getTaskService() {
        return Context.getSingleton(ServiceManager.class)  //
               .getService(TaskService.class)              //
               .orElseThrow(() -> new RuntimeException("No TaskService!"));
    }

    //~ Methods ......................................................................................................................................

    static void cleanRecords() {
        runInTransaction(() -> {
            final Database db = Databases.openDefault();
            db.sqlStatement("delete from QName(SG, LAST_DELETED)").execute();
            db.sqlStatement("delete from QName(SG, DELETED_INSTANCES)").execute();
            db.sqlStatement("delete from QName(BASIC, PRODUCT_DATA_WORK_ITEM)").execute();
            db.sqlStatement("delete from QName(BASIC, PRODUCT_DATA)").execute();
            db.sqlStatement("delete from QName(BASIC, PRODUCT_BY_CAT)").execute();
            db.sqlStatement("delete from QName(BASIC, PRODUCT)").execute();
            db.sqlStatement("delete from QName(BASIC, PRODUCT_VIEW)").execute();
            db.sqlStatement("delete from QName(BASIC, CATEGORY)").execute();
            db.sqlStatement("delete from QName(BASIC, CATEGORY_VIEW)").execute();
            db.sqlStatement("delete from QName(BASIC, REVIEW)").execute();
            db.sqlStatement("delete from QName(BASIC, PRODUCT_DEFAULT)").execute();
            db.sqlStatement("delete from QName(BASIC, REV)").execute();
            db.sqlStatement("delete from QName(BASIC, PRODUCT_DEFAULT_INNERS)").execute();
            db.sqlStatement("delete from QName(BASIC, PRODUCT_DEFAULT_VIEW)").execute();
            db.sqlStatement("delete from QName(BASIC, CATEGORY_DEFAULT)").execute();
            db.sqlStatement("delete from QName(BASIC, CATEGORY_DEFAULT_VIEW)").execute();
            db.sqlStatement("delete from QName(BASIC, CATEGORY_COMPOSITE)").execute();
            db.sqlStatement("delete from QName(BASIC, CATEGORY_COMPOSITE_VIEW)").execute();
            db.sqlStatement("delete from QName(BASIC, CATEGORY_SQL_VIEW_VIEW)").execute();
            db.sqlStatement("delete from QName(BASIC, CITY_VIEW)").execute();
            db.sqlStatement("delete from QName(BASIC, COUNTRY_VIEW)").execute();
            db.sqlStatement("delete from QName(BASIC, CITY)").execute();
            db.sqlStatement("delete from QName(BASIC, STATE_PROVINCE)").execute();
            db.sqlStatement("delete from QName(BASIC, COUNTRY)").execute();
        });

        final HashMap<?, ?> invokers = Reflection.getPrivateStaticField(ViewRefresher.class, "invokers");
        invokers.clear();
    }

    //~ Static Fields ................................................................................................................................

    @ClassRule public static ApplicationRule app = new ApplicationRule() {
            @Override protected void configureTaskProperties(TaskServiceProps props) {
                props.enableSchedule = false;
                props.enabled        = true;
            }

            @Override protected void configureAppProperties(final ApplicationProps prop) {
                super.configureAppProperties(prop);
                prop.syncEnabled = true;
            }

            @Override protected void before()
                throws Exception
            {
                super.before();
                final SchemaProps schemaProps = new SchemaProps();
                schemaProps.remoteUrl = "http://localhost:8080";
                Context.getEnvironment().put(schemaProps);
            }
        };
}  // end class SyncTest
