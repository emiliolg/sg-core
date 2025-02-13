
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
import tekgenesis.common.core.Option;
import tekgenesis.form.FormTable;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.persistence.QueryTuple;
import tekgenesis.persistence.TableField.Int;
import tekgenesis.persistence.TableField.Str;
import tekgenesis.persistence.expr.Expr;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.authorization.g.ApplicationAuditTable.APPLICATION_AUDIT;
import static tekgenesis.authorization.shiro.AuthorizationUtils.getModelRepository;
import static tekgenesis.common.Predefined.cast;
import static tekgenesis.metadata.form.widget.UiModelLocalizer.localizer;
import static tekgenesis.persistence.Sql.select;
import static tekgenesis.persistence.Sql.selectFrom;

/**
 * Helper class for audits.
 */
class Audits {

    //~ Constructors .................................................................................................................................

    private Audits() {}

    //~ Methods ......................................................................................................................................

    static void loadChart(ImmutableList<QueryTuple> list, FormTable<? extends ChartAuditRow> chart) {
        for (int i = list.size() - 1; i >= 0; i--) {
            final QueryTuple    audit   = list.get(i);
            final ChartAuditRow newCol  = chart.add();
            final String        s       = cast(audit.get(1));
            final Integer       integer = cast(audit.get(2));
            newCol.setString(s);
            newCol.setInt(integer);
        }
    }

    static void loadLastUsages(FormTable<? extends AuditRow> auditTable) {
        for (final ApplicationAudit lastUsage :
             selectFrom(APPLICATION_AUDIT).orderBy(APPLICATION_AUDIT.LAST_EVENT.descending()).limit(EVENT_LIMIT).list())
        {
            final AuditRow newRow = auditTable.add();
            newRow.setAudit(lastUsage);
        }
    }

    static void populateTable(FormTable<? extends AuditRow> applicationAuditsTable, ImmutableList<ApplicationAudit> applicationAudits) {
        applicationAuditsTable.clear();
        applicationAudits.forEach(a -> applicationAuditsTable.add().setAudit(a));
    }

    static String getApplicationLabel(ApplicationAudit applicationAudit) {
        final ModelRepository repository  = getModelRepository();
        final Option<Form>    currentForm = repository.getModel(applicationAudit.getApplication(), Form.class);
        if (currentForm.isPresent()) return localizer(currentForm.get()).localize().getLabel();
        else return applicationAudit.getApplication();
    }

    static ImmutableList<QueryTuple> getTopAuditsFor(final Str groupBy, final Int sum) {
        final Expr<Integer> sumCol = sum.sum().as(SUM_COL_NAME);
        return select(groupBy, sumCol).from(APPLICATION_AUDIT).groupBy(groupBy).limit(CHART_LIMIT).orderBy(sumCol.descending()).list();
    }

    @NotNull static String getUserEvent(ApplicationAudit applicationAudit) {
        // todo i18n
        return applicationAudit.getUser().getName() + " visited " + getApplicationLabel(applicationAudit);
    }

    //~ Static Fields ................................................................................................................................

    private static final String SUM_COL_NAME = "sum_events";
    static final int            CHART_HEIGHT = 200;
    private static final int    EVENT_LIMIT  = 4;
    private static final int    CHART_LIMIT  = 5;

    //~ Inner Interfaces .............................................................................................................................

    interface AuditRow {
        /** Sets the audit to the row. */
        void setAudit(ApplicationAudit applicationAudit);
    }

    interface ChartAuditRow {
        /** Sets the value in the chart. */
        void setInt(@Nullable Integer value);
        /** Sets the label in the chart. */
        void setString(@Nullable String label);
    }
}  // end class Audits
