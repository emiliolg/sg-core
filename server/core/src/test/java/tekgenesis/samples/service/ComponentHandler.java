
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.samples.service;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateTime;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.Views;
import tekgenesis.service.html.Html;

/**
 * User class for Handler: ComponentHandler
 */
@SuppressWarnings("MagicNumber")
public class ComponentHandler extends ComponentHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final Views views;

    //~ Constructors .................................................................................................................................

    ComponentHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(Views.class);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<Html> banner(@NotNull String name) {
        final String image = "http://find.image.url.for.name/" + name;
        return ok(views.siteComponentBanner(image));
    }

    @NotNull @Override public Result<Html> header(@NotNull String name) {
        final String title       = "Title for " + name;
        final String description = "Description for " + name;
        return ok(views.siteComponentHeader(title, description));
    }

    @NotNull @Override public Result<Html> list(@NotNull String name) {
        final ProductList list = new ProductList();
        list.setList(1);
        for (int i = 0; i < 5; i++)
            list.getProducts().add(createProduct(name + i));
        return ok(views.siteComponentList(list));
    }

    @NotNull @Override public Result<Html> menu(@NotNull String name) {
        final Menu menu = new Menu();
        menu.setId(name);
        for (int i = 0; i < 5; i++)
            menu.getItems().add(createMenuItem(name + i));
        return ok(views.siteComponentMenu(menu));
    }

    @NotNull @Override public Result<Html> og(@NotNull String name) {
        return ok(views.siteComponentOg(name, name, name));
    }

    @NotNull @Override public Result<Html> product(@NotNull String name) {
        final Product product = createProduct(name);
        return ok(views.siteComponentProduct(product));
    }

    private MenuItem createMenuItem(String s) {
        final MenuItem item = new MenuItem();
        item.setId(s);
        item.setLabel(s);
        item.setLink("http://" + s);
        return item;
    }

    private Product createProduct(String s) {
        final Product product = new Product(s);
        product.setCreated(DateTime.dateTime(2014, 1, 31));
        product.setModel(s);
        product.setDescription(s);
        product.setPrice(new BigDecimal(5));
        return product;
    }
}  // end class ComponentHandler
