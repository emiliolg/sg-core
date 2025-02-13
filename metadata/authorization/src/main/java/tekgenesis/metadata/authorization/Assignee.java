
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.authorization;

import org.jetbrains.annotations.NotNull;

/**
 * Represents work item's assignees.
 */
public interface Assignee {

    //~ Methods ......................................................................................................................................

    /** Get assignees string representation. */
    @NotNull String asString();

    /** Returns true if assignee includes given user. */
    boolean includes(@NotNull final User user);

    /** Returns assignees id. */
    @NotNull String getId();

    /** Returns assignees image. */
    @NotNull String getImage();

    /** Returns assignees name. */
    @NotNull String getName();
}
