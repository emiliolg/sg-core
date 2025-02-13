
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.widget.Widget;

/**
 * Parent and {@link Widget anchor widget} with optional item.
 */
public interface Parent<T extends Parentable<T>> {

    //~ Methods ......................................................................................................................................

    /** Return {@link Widget anchor}. */
    @NotNull Widget anchor();

    /** Return fully qualified name (e.g.: sale#2.item#3) */
    @NotNull String fqn();

    /** Return {@link Option<Integer> optional item}. */
    @NotNull Option<Integer> item();

    /** Return local name (e.g.: item#3) */
    @NotNull default String name() {
        return anchor().getName() + item().map(i -> "#" + i).orElse("");
    }

    /** Return {@link Parentable<T> parentable}. */
    @NotNull T value();
}
