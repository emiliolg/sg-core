
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import java.util.Random;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.configuration.ChartConfiguration;

/**
 * User class for Form: MiniChart
 */
@Generated(value = "tekgenesis/showcase/ChartShowcase.mm", date = "1384375693660")
public class MiniChart extends MiniChartBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override public void load() {
        this.<ChartConfiguration>configuration(Field.SESSIONS).dimension(215, 80).mainXAxis().axisVisible(false).positiveQuadrant();
        addSession();
    }

    @NotNull @Override public Action refresh() {
        addSession();
        return actions.getDefault();
    }

    private void addSession() {
        final Random                 r        = new Random();
        final FormTable<SessionsRow> sessions = getSessions();
        if (sessions.size() > 30) sessions.remove(0);
        final int sessionsCount = 40 + new Random().nextInt(5);
        sessions.add().setSessionsCount(sessionsCount);
        setCurrentSessionOpened(sessionsCount);
    }

    //~ Inner Classes ................................................................................................................................

    public class SessionsRow extends SessionsRowBase {}
}
