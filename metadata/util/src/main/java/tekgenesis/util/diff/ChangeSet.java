
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.diff;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents a Set of Change. It is a Change itself.
 */
public class ChangeSet implements Change {

    //~ Instance Fields ..............................................................................................................................

    private final ArrayList<Change> changes = new ArrayList<>();

    //~ Methods ......................................................................................................................................

    /** Adds a new change to the set. */
    public void add(@NotNull Change change) {
        changes.add(change);
    }

    /** Returns an Iterabl of all the changes from the set. */
    public Iterable<Change> getChanges() {
        return changes;
    }

    @Override public String getMessage() {
        final StringBuilder builder = new StringBuilder();

        builder.append("Changes: \n");
        for (final Change change : changes)
            builder.append("\t").append(change.getMessage()).append('\n');
        return builder.toString();
    }

    /** Returns true if the Set has no changes. */
    public boolean isEmpty() {
        return changes.isEmpty();
    }
}
