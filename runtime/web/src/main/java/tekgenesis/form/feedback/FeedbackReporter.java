
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.feedback;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that developers needs to implement in order to subscribe for feedback events.
 *
 * <p>To subscribe a FeedbackReporter you must implement this interface and package it as a jar file
 * with a file named</p>
 *
 * <p>There is a way to define if the reporter is enabled at load time. Just override the method
 * isEnabled.</p>
 *
 * <p>META-INF/services/tekgenesis.form.feedback.FeedbackReporter</p>
 *
 * <p>This file contains the single line:</p>
 *
 * <p>com.example.impl.FeedbackReporterImpl</p>
 */
public interface FeedbackReporter {

    //~ Methods ......................................................................................................................................

    /** Report a feedback event. */
    void reportFeedback(@NotNull FeedbackEvent feedbackEvent);
    /** Returns true if the Reporter is enabled. */
    default boolean isEnabled() {
        return true;
    }
}
