
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.form.Action;
import tekgenesis.form.Invoke;

/**
 * Expression showcase form class.
 */
@SuppressWarnings("WeakerAccess")
public class ExpressionShowcase extends ExpressionShowcaseBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action invoke() {
        final Action result = actions.getDefault();

        final Invoke invoke = result.withInvocation(getFunction());
        if (isDefined(Field.TARGET)) invoke.target(getTarget());
        invoke.arg(DateOnly.current()).arg(DateTime.current()).arg(getArg1()).arg(getArg2()).arg(getArg3());

        return result.withMessage("Invoked function " + getFunction());
    }

    @NotNull @Override
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public Action isNickAvailable() {
        final String nick = isDefined(Field.NICK) ? getNick() : "";
        setAvailable(!"pcelentano".equalsIgnoreCase(nick) && !"sracca".equalsIgnoreCase(nick));
        return actions.getDefault();
    }
}
