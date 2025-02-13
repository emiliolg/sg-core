
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.console.handler;

import java.io.Serializable;

import org.jetbrains.annotations.NotNull;

import tekgenesis.console.handler.g.ConsoleMenuItemBase;

/**
 * User class for Model: ConsoleMenuItem
 */
public class ConsoleMenuItem extends ConsoleMenuItemBase implements Serializable {

    //~ Methods ......................................................................................................................................

    /** Returns true if this item must show badge. */
    public boolean isShowingBadge() {
        return getBadgeNumber() != null && getBadgeNumber() > 0;
    }

    //~ Methods ......................................................................................................................................

    static ConsoleMenuItem build(@NotNull final String fqn, @NotNull final String label, @NotNull final String icon) {
        return new ConsoleMenuItem().setFqn(fqn).setLabel(label).setIcon(icon);
    }

    static ConsoleMenuItem build(@NotNull final String fqn, @NotNull final String label, @NotNull final String icon, @NotNull final Integer badge) {
        return build(fqn, label, icon).setBadgeNumber(badge);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -503342060457887496L;
}
