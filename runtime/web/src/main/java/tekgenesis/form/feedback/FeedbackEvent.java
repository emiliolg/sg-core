
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
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.metadata.authorization.User;

/**
 * A Feedback Issue.
 */
public interface FeedbackEvent {

    //~ Methods ......................................................................................................................................

    /** Current application's name. */
    @NotNull String getApplication();

    /** Date of the event. */
    @NotNull DateTime getDate();

    /** Event's description. */
    @NotNull String getDescription();

    /** Contextual error if defined. */
    @Nullable String getError();

    /** Application top history. */
    @NotNull Seq<String> getHistory();

    /** Event's summary. */
    @NotNull String getSummary();

    /** Feedback event type. */
    @NotNull FeedbackType getType();

    /** Current application's url. */
    @NotNull String getUrl();

    /** Reporter's username. */
    @NotNull User getUser();

    //~ Enums ........................................................................................................................................

    /**
     * Feedback event type.
     */
    enum FeedbackType { EXCEPTION, ERROR, SUGGESTION }
}
