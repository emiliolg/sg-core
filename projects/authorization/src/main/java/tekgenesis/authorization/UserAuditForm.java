
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

import tekgenesis.authorization.Audits.*;
import tekgenesis.authorization.g.UserTable;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.form.Action;
import tekgenesis.form.Suggestion;
import tekgenesis.form.configuration.ChartConfiguration;
import tekgenesis.persistence.QueryTuple;

//J-
import static java.util.Collections.emptyList;
import static tekgenesis.authorization.Audits.*;
import static tekgenesis.authorization.UserAuditFormBase.Field.TOP_MONTH;
import static tekgenesis.authorization.UserAuditFormBase.Field.TOP_TODAY;
import static tekgenesis.authorization.UserAuditFormBase.Field.TOP_WEEK;
import static tekgenesis.authorization.g.ApplicationAuditTable.APPLICATION_AUDIT;
import static tekgenesis.authorization.g.UserTable.USER;
import static tekgenesis.persistence.Sql.selectFrom;
//J+

/**
 * User class for Form: UserAuditForm
 */
public class UserAuditForm extends UserAuditFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action clickMonth(@NotNull Field field) {
        return clickAndSearch(getTopMonth().getCurrent().getLabelMonth());
    }

    @NotNull @Override public Action clickTable() {
        return clickAndSearch(getUsages().getCurrent().getUserId());
    }

    @NotNull @Override public Action clickToday(@NotNull Field field) {
        return clickAndSearch(getTopToday().getCurrent().getLabelToday());
    }

    @NotNull @Override public Action clickWeek(@NotNull Field field) {
        return clickAndSearch(getTopWeek().getCurrent().getLabelWeek());
    }

    /** Invoked when the form is loaded. */
    @Override public void load() {
        loadCharts();
        loadLastUsages(getUsages());
    }

    @NotNull @Override public Action selectSuggest() {
        final String userSearcher = getUserSearcher();
        if (userSearcher != null)
            populateTable(getApplicationAudits(), selectFrom(APPLICATION_AUDIT).where(APPLICATION_AUDIT.USER_ID.eq(userSearcher)).list());
        return actions.getDefault();
    }

    private void chartConfigFor(Field... fields) {
        for (final Field field : fields)
            this.<ChartConfiguration>configuration(field).showLabelsOnHover(true).width(0).height(CHART_HEIGHT);
    }

    @NotNull private Action clickAndSearch(String userId) {
        setUserSearcher(userId);
        return selectSuggest();
    }

    private void loadCharts() {
        chartConfigFor(TOP_TODAY, TOP_WEEK, TOP_MONTH);

        final ImmutableList<QueryTuple> auditsToday = getTopAuditsFor(APPLICATION_AUDIT.USER_ID, APPLICATION_AUDIT.DAY_EVENTS);
        final ImmutableList<QueryTuple> auditsWeek  = getTopAuditsFor(APPLICATION_AUDIT.USER_ID, APPLICATION_AUDIT.WEEK_EVENTS);
        final ImmutableList<QueryTuple> auditsMonth = getTopAuditsFor(APPLICATION_AUDIT.USER_ID, APPLICATION_AUDIT.MONTH_EVENTS);

        loadChart(auditsToday, getTopToday());
        loadChart(auditsWeek, getTopWeek());
        loadChart(auditsMonth, getTopMonth());
    }

    //~ Methods ......................................................................................................................................

    /** suggestions from query string. */
    @NotNull public static Iterable<Suggestion> suggest(@Nullable final String query) {
        if (query == null) return emptyList();
        return userMapFromQuery(query);
    }

    private static Iterable<Suggestion> userMapFromQuery(String query) {
        return selectFrom(USER).where(UserTable.USER.ID.like("%" + query + "%")).limit(10).map(User::getId).map(Suggestion::create).toList();
    }

    //~ Inner Classes ................................................................................................................................

    public class ApplicationAuditsRow extends ApplicationAuditsRowBase implements AuditRow {
        @Override public void setAudit(ApplicationAudit applicationAudit) {
            setApplication(getApplicationLabel(applicationAudit));
            setUser(applicationAudit.getUser());
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
            setUserId(applicationAudit.getUserId());
            setPic(applicationAudit.getUser().getImage());
            setText(getUserEvent(applicationAudit));
            setTimeAgo(applicationAudit.getLastEvent());
        }
    }
}  // end class UserAuditForm
