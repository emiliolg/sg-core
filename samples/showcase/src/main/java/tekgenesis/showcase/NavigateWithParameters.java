
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.math.BigDecimal;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.form.Action;

import static java.util.Arrays.asList;

import static tekgenesis.showcase.g.NamedItemBase.findOrCreate;
import static tekgenesis.transaction.Transaction.runInTransaction;

/**
 * User class for Form: NavigateWithParameters
 */
@SuppressWarnings("ALL")
public class NavigateWithParameters extends NavigateWithParametersBase {

    //~ Methods ......................................................................................................................................

    @Override public void load() {
        runInTransaction(() -> {
            findOrCreate(120).setName("One Name").persist();
            findOrCreate(121).setName("Two Names").persist();
            findOrCreate(122).setName("Third Name").setColor("super color").persist();
        });
    }

    @NotNull @Override public Action navigate() {
        final List<Options> someOptions = asList(Options.OPTION2, Options.OPTION4);

        final NamedItem one   = findOrCreate(120).setName("One Name").persist();
        final NamedItem two   = findOrCreate(121).setName("Two Names").persist();
        final NamedItem three = findOrCreate(122).setName("Third Name").setColor("super color").persist();

        final List<NamedItem> someEntities = asList(one, two);
        return actions.navigate(NavigateWithParameters.class)
               .withParameters(
                parameters().withName("My Parameter Name").withOneOption(Options.OPTION5).withSomeOptions(someOptions).withOneDate(
                                DateTime.current()).withSomeInt(3).withOneDateTime(DateOnly.current()).withDecimal(BigDecimal.valueOf(32.5))
                            .withOneEntity(three)
                            .withMoreEntity(someEntities)
                            .withMoreEntityKey(asList("122")));
    }
}
