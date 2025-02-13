
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import java.util.ArrayList;
import java.util.List;

import tekgenesis.common.Predefined;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.report.fo.Fo;

/**
 * Fo Container Builder.
 */
public abstract class FoContainerB<This, T extends Fo> extends FoBuilder<This, T> {

    //~ Instance Fields ..............................................................................................................................

    protected List<FoB> children = new ArrayList<>();

    //~ Methods ......................................................................................................................................

    /** Build the elements. */
    public List<Fo> buildChildren() {
        final List<Fo> c = new ArrayList<>();
        for (final FoB child : children)
            c.add(child.build());
        return c;
    }

    /** Specify children elements. */
    public final This children(FoB... foBuilders) {
        children.addAll(ImmutableList.fromArray(foBuilders));
        return Predefined.cast(this);
    }
}
