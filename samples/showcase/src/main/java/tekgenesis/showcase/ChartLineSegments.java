
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.configuration.ChartConfiguration;

import static tekgenesis.showcase.ChartLineSegmentsBase.Field.SEGMENTS;

/**
 * User class for form: ChartLineSegments
 */
public class ChartLineSegments extends ChartLineSegmentsBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when chart(column) is clicked. */
    @NotNull @Override public Action doClick(@NotNull Field field) {
        final SegmentsRow current = getSegments().getCurrent();
        setCurrent("Label: " + current.getLabel() + " with value: " + current.getInteger() + " with key: " + current.getKey());
        return actions.getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void load() {
        this.<ChartConfiguration>configuration(SEGMENTS).lineSteps(true);

        getSegments().add().setLabel("1988").setInteger(1);
        getSegments().add().setLabel("1989").setInteger(1);
        getSegments().add().setLabel("1989").setInteger(null);

        getSegments().add().setLabel("1989").setInteger(2);
        getSegments().add().setLabel("1990").setInteger(2);
        getSegments().add().setLabel("1990").setInteger(null);

        getSegments().add().setLabel("1990").setInteger(3);
        getSegments().add().setLabel("1991").setInteger(3);
        getSegments().add().setLabel("1992").setInteger(3);
        getSegments().add().setLabel("1993").setInteger(3);
        getSegments().add().setLabel("1994").setInteger(3);
        getSegments().add().setLabel("1994").setInteger(null);
    }

    //~ Inner Classes ................................................................................................................................

    public class SegmentsRow extends SegmentsRowBase {}
}
