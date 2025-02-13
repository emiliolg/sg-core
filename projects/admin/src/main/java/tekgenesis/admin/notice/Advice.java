
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.admin.notice;

import org.jetbrains.annotations.NotNull;

import tekgenesis.admin.notice.g.AdviceBase;
import tekgenesis.common.core.DateTime;

/**
 * User class for Model: Advice
 */
public class Advice extends AdviceBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public DateTime getCreationTime() {
        return super.getCreationTime().equals(DateTime.EPOCH) ? DateTime.current() : super.getCreationTime();
    }

    //~ Methods ......................................................................................................................................

    /** Create advice from description, type and level. */
    public static Advice create(String description, Level level, AdviceType type) {
        final Advice notice = new Advice();
        notice.setDescription(description);
        notice.setLevel(level);
        notice.setType(type);
        notice.setState(State.NEW);
        return notice;
    }

    /** Add User Notice. */
    public static void notice(final String description, final Level level) {
        final Advice notice = new Advice();
        notice.setDescription(description);
        notice.setLevel(level);
        notice.setType(AdviceType.USER_NOTICE);
        notice.insert();
    }
}
