
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.form.Action;
import tekgenesis.form.Suggestion;
import tekgenesis.form.configuration.ChartConfiguration;
import tekgenesis.persistence.QueryTuple;

import static tekgenesis.authorization.ApplicationAuditFormBase.Field.TOP_MONTH;
import static tekgenesis.authorization.ApplicationAuditFormBase.Field.TOP_TODAY;
import static tekgenesis.authorization.ApplicationAuditFormBase.Field.TOP_WEEK;
import static tekgenesis.authorization.Applications.applicationMapFromQuery;
import static tekgenesis.authorization.Audits.*;
import static tekgenesis.authorization.g.ApplicationAuditBase.listWhere;
import static tekgenesis.authorization.g.ApplicationAuditTable.APPLICATION_AUDIT;

/**
 * User class for Form: ApplicationAuditForm
 */
public class ApplicationAuditForm extends ApplicationAuditFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action clickMonth(@NotNull Field field) {
        return clickAndSearch(getTopMonth().getCurrent().getLabelMonth());
    }

    @NotNull @Override public Action clickTable() {
        return clickAndSearch(getUsages().getCurrent().getAppId());
    }

    @NotNull @Override public Action clickToday(@NotNull Field field) {
        return clickAndSearch(getTopToday().getCurrent().getLabelToday());
    }

    @NotNull @Override public Action clickWeek(@NotNull Field field) {
        return clickAndSearch(getTopWeek().getCurrent().getLabelWeek());
    }

    @Override public void load() {
        loadCharts();
        loadLastUsages(getUsages());
    }

    @NotNull @Override public Action selectSuggest() {
        populateTable(getApplicationAudits(), listWhere(APPLICATION_AUDIT.APPLICATION.eq(getAppSearcher())).toList());

        return actions.getDefault();
    }

    private void chartConfigFor(Field... fields) {
        for (final Field field : fields)
            this.<ChartConfiguration>configuration(field).showLabelsOnHover(true).width(0).height(CHART_HEIGHT);
    }

    @NotNull private Action clickAndSearch(String appId) {
        setAppSearcher(appId);
        return selectSuggest();
    }

    private void loadCharts() {
        chartConfigFor(TOP_TODAY, TOP_WEEK, TOP_MONTH);

        final ImmutableList<QueryTuple> auditsToday = getTopAuditsFor(APPLICATION_AUDIT.APPLICATION, APPLICATION_AUDIT.DAY_EVENTS);
        final ImmutableList<QueryTuple> auditsWeek  = getTopAuditsFor(APPLICATION_AUDIT.APPLICATION, APPLICATION_AUDIT.WEEK_EVENTS);
        final ImmutableList<QueryTuple> auditsMonth = getTopAuditsFor(APPLICATION_AUDIT.APPLICATION, APPLICATION_AUDIT.MONTH_EVENTS);

        loadChart(auditsToday, getTopToday());
        loadChart(auditsWeek, getTopWeek());
        loadChart(auditsMonth, getTopMonth());
    }

    //~ Methods ......................................................................................................................................

    /** suggestions from query string. */
    @NotNull public static Iterable<Suggestion> suggest(@Nullable final String query) {
        return applicationMapFromQuery(query);
    }

    //~ Inner Classes ................................................................................................................................

    public class ApplicationAuditsRow extends ApplicationAuditsRowBase implements AuditRow {
        @Override public void setAudit(ApplicationAudit applicationAudit) {
            setUser(applicationAudit.getUser());
            setApplication(applicationAudit.getApplication());
            setLastEvent(applicationAudit.getLastEvent());
            setYesterdayEvents(applicationAudit.getYesterdayEvents());
            setDayEvents(applicationAudit.getDayEvents());
            setLastWeekEvents(applicationAudit.getLastWeekEvents());
            setWeekEvents(applicationAudit.getWeekEvents());
            setLastMonthEvents(applicationAudit.getLastMonthEvents());
            setMonthEvents(applicationAudit.getMonthEvents());
        }
    }

    public class TopMonthRow extends TopMonthRowBase implements ChartAuditRow {
        @Override public void setInt(Integer value) {
            setValueMonth(value);
        }
        @Override public void setString(String label) {
            setLabelMonth(label);
        }
    }

    public class TopTodayRow extends TopTodayRowBase implements ChartAuditRow {
        @Override public void setInt(Integer value) {
            setValueToday(value);
        }
        @Override public void setString(String label) {
            setLabelToday(label);
        }
    }

    public class TopWeekRow extends TopWeekRowBase implements ChartAuditRow {
        @Override public void setInt(Integer value) {
            setValueWeek(value);
        }
        @Override public void setString(String label) {
            setLabelWeek(label);
        }
    }

    public class UsagesRow extends UsagesRowBase implements AuditRow {
        @Override public void setAudit(ApplicationAudit applicationAudit) {
            setAppId(applicationAudit.getApplication());
            setPic(applicationAudit.getUser().getImage());
            setText(getUserEvent(applicationAudit));
            setTimeAgo(applicationAudit.getLastEvent());
        }
    }
}  // end class ApplicationAuditForm
