
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.sales.basic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.service.Status;
import tekgenesis.sales.sui.Views;
import tekgenesis.service.*;
import tekgenesis.service.html.Html;

import static tekgenesis.common.Predefined.unreachable;

/**
 * Class to test the custom error generation.
 */
public class CustomErrorImpl implements CustomError {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Option<Result<Html>> forError(@NotNull Factory factory, @NotNull Status status, @Nullable Throwable e) {
        final Views        views    = factory.html(Views.class);
        final Results      results  = factory.results();
        final Html         instance = views.customerror();
        final Result<Html> result;
        switch (status) {
        case NOT_FOUND:
            result = results.notFound(instance);
            break;
        case INTERNAL_SERVER_ERROR:
            result = results.internalServerError(instance);
            break;
        default:
            throw unreachable();
        }
        return Option.some(result);
    }
}
