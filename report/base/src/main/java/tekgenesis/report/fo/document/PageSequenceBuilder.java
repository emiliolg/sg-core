
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.document;

import java.util.ArrayList;
import java.util.List;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.report.fo.Fo;
import tekgenesis.report.fo.components.FoBuilder;

/**
 * Page Sequence Builder.
 */
public class PageSequenceBuilder extends FoBuilder<PageSequenceBuilder, PageSequence> {

    //~ Instance Fields ..............................................................................................................................

    String                              name     = null;
    private final List<FoBuilder<?, ?>> children = new ArrayList<>();
    private final List<FoBuilder<?, ?>> footer   = new ArrayList<>();
    private final List<FoBuilder<?, ?>> header   = new ArrayList<>();

    //~ Methods ......................................................................................................................................

    /** Adds Elements. */
    public PageSequenceBuilder addChildren(FoBuilder<?, ?>... fo) {
        children.addAll(ImmutableList.fromArray(fo));
        return this;
    }

    /**
     * Adds Footer.
     *
     * @return  PageSequenceBuilder
     */
    public PageSequenceBuilder addFooter(FoBuilder<?, ?>... fo) {
        footer.addAll(ImmutableList.fromArray(fo));
        return this;
    }

    /**
     * Adds Header.
     *
     * @return  PageSequenceBuilder
     */
    public PageSequenceBuilder addHeader(FoBuilder<?, ?>... fo) {
        header.addAll(ImmutableList.fromArray(fo));
        return this;
    }

    public PageSequence build() {
        return new PageSequence(name, build(children), build(header), build(footer));
    }

    /**
     * Builds the Page Sequence.
     *
     * @return  a List of Fo objects
     */
    public List<Fo> build(List<FoBuilder<?, ?>> ch) {
        final List<Fo> c = new ArrayList<>();
        for (final FoBuilder<?, ?> child : ch)
            c.add(child.build());
        return c;
    }

    /** Set name. */
    public PageSequenceBuilder withName(String n) {
        name = n;
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** @return  a new page Sequence */
    public static PageSequenceBuilder pageSequence() {
        return new PageSequenceBuilder();
    }
}  // end class PageSequenceBuilder
