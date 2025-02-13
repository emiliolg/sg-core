
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.admin.notice.Advice;
import tekgenesis.admin.notice.Level;
import tekgenesis.admin.notice.State;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Tuple;
import tekgenesis.form.Action;
import tekgenesis.persistence.Criteria;
import tekgenesis.type.permission.PredefinedPermission;

import static tekgenesis.admin.notice.g.AdviceTable.ADVICE;

/**
 * User class for Form: AdviceForm
 */
public class AdviceForm extends AdviceFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action clearFilter() {
        setFilter("");
        // loadNotices();
        return actions.getDefault();
    }

    @NotNull @Override public Action dismissAll() {
        for (final NoticesRow advicesRow : getNotices()) {
            if (!advicesRow.isDismiss()) {
                advicesRow.setDismiss(true);
                final Integer id = advicesRow.getId();
                if (id != null) {
                    final Advice advice = Advice.find(id);
                    if (advice != null) {
                        advice.setState(State.DISMISSED);
                        advice.persist();
                    }
                }
            }
        }
        return actions.getDefault();
    }

    /** Invoked when notices lazy requests items. */
    @Nullable public Action lazyLoadNotices(int offset, int limit) {
        final String filter = getFilter();

        Criteria criteria = Criteria.EMPTY;
        if (isDefined(Field.FILTER) && filter != null && !filter.trim().isEmpty()) criteria = ADVICE.DESCRIPTION.like("%" + filter + "%");
        if (isDefined(Field.SHOW_DISMISSED) && !isShowDismissed()) criteria = criteria.and(ADVICE.STATE.ne(State.DISMISSED));
        if (getTimeline() != SincePeriod.ANYTIME) criteria = criteria.and(ADVICE.CREATION_TIME.ge(getSinceDateTime()));

        Advice.listWhere(criteria).orderBy(ADVICE.CREATION_TIME.descending()).offset(offset).limit(limit).list().forEach(a ->
                getNotices().add()
                            .populate(a));

        return actions().getDefault();
    }

    @NotNull @Override public Action refresh() {
        // noinspection MagicNumber
        lazyLoadNotices(0, 20);
        return actions.getDefault();
    }

    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private DateTime getSinceDateTime() {
        switch (getTimeline()) {
        case LAST_24:
            return DateTime.current().addDays(-1);
        case LAST_WEEK:
            return DateTime.current().addWeeks(-1);
        case LAST_MONTH:
            return DateTime.current().addMonths(-1);
        case LAST_YEAR:
            return DateTime.current().addYears(-1);
        default:
            return DateTime.EPOCH;
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class NoticesRow extends NoticesRowBase {
        @Override public void copyTo(@NotNull Advice notice) {
            super.copyTo(notice);
            notice.setState(isDismiss() ? State.DISMISSED : State.NEW);
        }

        /** Called each time table(advices) changes. */
        @NotNull public Action dismiss() {
            if (forms.hasPermission(PredefinedPermission.CREATE)) {
                final Integer primaryKey = getId();
                if (primaryKey != null) {
                    final Advice currentAdvice = Advice.find(primaryKey);
                    if (currentAdvice != null) {
                        copyTo(currentAdvice);
                        currentAdvice.persist();
                        setId(currentAdvice.getId());
                    }
                }
            }
            return actions.getDefault();
        }
        @Override public void populate(@NotNull Advice n) {
            super.populate(n);
            final Tuple<String, String> icon = getIcon(n.getLevel());
            setLevelIcon(icon.first());
            setTooltip(icon.second());
            final boolean dismissed = n.getState() == State.DISMISSED;
            setDismiss(dismissed);
        }

        @SuppressWarnings("DuplicateStringLiteralInspection")
        private Tuple<String, String> getIcon(Level level) {
            final Tuple<String, String> info = Tuple.tuple("info", "Info");
            switch (level) {
            case INFO:
                return info;
            case WARNING:
                return Tuple.tuple("warning", "Warning");
            case SEVERE:
                return Tuple.tuple("info-circle", "Severe");
            }
            return info;
        }
    }  // end class NoticesRow
}  // end class AdviceForm
