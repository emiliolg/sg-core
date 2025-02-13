
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

import tekgenesis.form.Action;
import tekgenesis.form.ExecutionFeedback;

import static tekgenesis.showcase.FeedbackMessages.*;

/**
 * User class for Form: FeedbackForm
 */
public class FeedbackForm extends FeedbackFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action feedbackAction(@NotNull ExecutionFeedback feedback) {
        feedback.step(STARTING.label());
        for (int i = 0; i < 100 && !feedback.isCanceled(); i += 5) {
            try {
                feedback.step(i, PROGRESS.label(i));
                Thread.sleep(SLEEP_TIME);
            }
            catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        final String msg = feedback.isCanceled() ? CANCELED.label() : COMPLETED.label();
        setInnerMessage(msg);
        return actions.getDefault();
    }

    @NotNull @Override public Action feedbackException(@NotNull ExecutionFeedback feedback) {
        feedback.step(STARTING.label());
        for (int i = 0; i < 100 && !feedback.isCanceled(); i += 5) {
            try {
                feedback.step(i, PROGRESS.label(i));
                Thread.sleep(SLEEP_TIME);
                if (i == EXCEPTION_PCTG) throw new RuntimeException(EXCEPTION.label());
            }
            catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        final String msg = feedback.isCanceled() ? CANCELED.label() : COMPLETED.label();
        setInnerMessage(msg);
        return actions.getDefault();
    }

    @NotNull @Override public Action simpleClick() {
        return actions.getDefault().withMessage(COMPLETED.label());
    }

    //~ Static Fields ................................................................................................................................

    private static final int SLEEP_TIME     = 3000;
    private static final int EXCEPTION_PCTG = 60;
}  // end class FeedbackForm
