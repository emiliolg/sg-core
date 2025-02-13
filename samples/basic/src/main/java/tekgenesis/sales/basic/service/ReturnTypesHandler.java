
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic.service;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Resource;
import tekgenesis.common.core.Resource.Entry;
import tekgenesis.common.env.context.Context;
import tekgenesis.persistence.ResourceHandler;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;
import tekgenesis.service.html.Html;

import static tekgenesis.common.core.DateOnly.date;
import static tekgenesis.common.core.DateTime.dateTime;
import static tekgenesis.common.core.Decimals.scaleAndCheck;
import static tekgenesis.common.media.MediaType.forMime;
import static tekgenesis.common.media.Mime.IMAGE_GIF;
import static tekgenesis.sales.basic.service.State.ACTIVE;
import static tekgenesis.transaction.Transaction.invokeInTransaction;

/**
 * User class for Handler: ReturnTypesHandler
 */
@SuppressWarnings("MagicNumber")
public class ReturnTypesHandler extends ReturnTypesHandlerBase {

    //~ Instance Fields ..............................................................................................................................

    private final Views views;

    //~ Constructors .................................................................................................................................

    ReturnTypesHandler(@NotNull Factory factory) {
        super(factory);
        views = factory.html(Views.class);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<byte[]> returnAny() {
        return ok(IMAGE_BASE_64.getBytes()).withContentType(forMime(IMAGE_GIF));
    }

    @NotNull @Override public Result<Boolean> returnBoolean() {
        return ok(true);
    }

    @NotNull @Override public Result<DateOnly> returnDate() {
        return ok(date(2014, 1, 31));
    }

    @NotNull @Override public Result<DateTime> returnDateTime() {
        return ok(dateTime(2014, 1, 31, 6, 47));
    }

    @NotNull @Override public Result<BigDecimal> returnDecimal() {
        return ok(scaleAndCheck("returnDecimal", new BigDecimal(.99), false, 10, 2));
    }

    @NotNull @Override public Result<State> returnEnum() {
        return ok(ACTIVE);
    }

    @NotNull @Override public Result<Html> returnHtml() {
        return created(views.httpTestSite());
    }

    @NotNull @Override public Result<Integer> returnInt() {
        return ok(1);
    }

    @NotNull @Override public Result<Double> returnReal() {
        return ok(.99);
    }

    @NotNull @Override public Result<Entry> returnResource() {
        final Resource resource = invokeInTransaction(() ->
                    Context.getSingleton(ResourceHandler.class)
                           .create()
                           .upload("xyz", IMAGE_GIF.getMime(), new ByteArrayInputStream(IMAGE_BASE_64.getBytes())));
        return ok(resource.getMaster());
    }

    @NotNull @Override public Result<Product> returnType() {
        final Product product = new Product();
        product.setProductId("xyz");
        return ok(product);
    }

    @NotNull @Override public Result<Void> returnVoid() {
        return ok();
    }

    //~ Static Fields ................................................................................................................................

    @SuppressWarnings("SpellCheckingInspection")
    public static final String IMAGE_BASE_64 = "data:image/gif;base64,R0lGODlhDwAPAKECAAAAzMzM/////\n" +
                                               "wAAACwAAAAADwAPAAACIISPeQHsrZ5ModrLlN48CXF8m2iQ3YmmKqVlRtW4ML\n" +
                                               "wWACH+H09wdGltaXplZCBieSBVbGVhZCBTbWFydFNhdmVyIQAAOw==";
}  // end class ReturnTypesHandler
