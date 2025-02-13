
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.service;

import java.io.Serializable;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import static java.util.Collections.emptyList;

/**
 * Feedback service payload.
 */
@SuppressWarnings("FieldMayBeFinal")
public class Feedback implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private String application;

    @NotNull private String       description;
    @NotNull private String       error;
    @NotNull private List<String> history;

    @NotNull private String     summary;
    @NotNull private TicketType type;
    @NotNull private String     url;

    //~ Constructors .................................................................................................................................

    /** Gwt Constructor. */
    public Feedback() {
        type        = TicketType.ERROR;
        summary     = "";
        description = "";
        url         = "";
        application = "";
        error       = "";
        history     = emptyList();
    }

    /** Create feedback. */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Feedback(@NotNull String summary, @NotNull String description, @NotNull TicketType type, @NotNull String url,
                    @NotNull final List<String> history, @NotNull final String application, @NotNull final String error) {
        this.summary     = summary;
        this.description = description;
        this.type        = type;
        this.url         = url;
        this.history     = history;
        this.application = application;
        this.error       = error;
    }

    //~ Methods ......................................................................................................................................

    /** Get current application. */
    @NotNull public String getApplication() {
        return application;
    }

    /** Get description. */
    @NotNull public String getDescription() {
        return description;
    }

    /** Get contextual error. */
    @NotNull public String getError() {
        return error;
    }

    /** Get application history. */
    @NotNull public List<String> getHistory() {
        return history;
    }

    /** Get summary. */
    @NotNull public String getSummary() {
        return summary;
    }

    /** Get tycket type. */
    @NotNull public TicketType getType() {
        return type;
    }

    /** Get url. */
    @NotNull public String getUrl() {
        return url;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -7542052683327012014L;

    //~ Enums ........................................................................................................................................

    public enum TicketType { EXCEPTION, ERROR, SUGGESTION }
}  // end class Feedback
