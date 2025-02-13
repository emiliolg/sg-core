
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.feedback;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.DateTime;
import tekgenesis.metadata.authorization.User;

/**
 * Feedback event.
 */
public class FeedbackEventImpl implements FeedbackEvent {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final String      application;
    @NotNull private final String      description;
    @NotNull private final String      error;
    @NotNull private final Seq<String> history;

    @NotNull private final String       summary;
    @NotNull private final DateTime     time;
    @NotNull private final FeedbackType type;
    @NotNull private final String       url;
    @NotNull private final User         user;

    //~ Constructors .................................................................................................................................

    /** Feedback event constructor. */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public FeedbackEventImpl(@NotNull final String summary, @NotNull final String description, @NotNull final FeedbackType type,
                             @NotNull final String url, @NotNull final String application, @NotNull final User user, @NotNull final DateTime time,
                             @NotNull final List<String> history, @NotNull final String error) {
        this.summary     = summary;
        this.description = description;
        this.type        = type;
        this.url         = url;
        this.application = application;
        this.user        = user;
        this.time        = time;
        this.error       = error;
        this.history     = Colls.seq(history);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public String getApplication() {
        return application;
    }

    @NotNull @Override public DateTime getDate() {
        return time;
    }

    @NotNull @Override public String getDescription() {
        return description;
    }

    @NotNull public String getError() {
        return error;
    }

    @NotNull @Override public Seq<String> getHistory() {
        return history;
    }

    @NotNull @Override public String getSummary() {
        return summary;
    }

    @NotNull @Override public FeedbackType getType() {
        return type;
    }

    @NotNull @Override public String getUrl() {
        return url;
    }

    @NotNull @Override public User getUser() {
        return user;
    }
}  // end class FeedbackEventImpl
