
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

import tekgenesis.common.collections.Colls;
import tekgenesis.form.Action;
import tekgenesis.form.configuration.MapConfiguration;

/**
 * Map drag Form class.
 */
@SuppressWarnings("WeakerAccess")
public class MapDragForm extends MapDragFormBase {

    //~ Methods ......................................................................................................................................

    /** Invoked when the form is loaded. */
    @Override
    @SuppressWarnings("MagicNumber")
    public void load() {
        final MapRow row = getMap().add();
        row.setLat(-34.4522);
        row.setLng(-58.8657546043);
    }

    @NotNull @Override public Action locate(double lat, double lng) {
        getMap().add().setLat(lat).setLng(lng);
        this.<MapConfiguration>configuration(Field.MAP).center(lat, lng);

        return actions().getDefault();
    }

    /** Invoked when button(button) is clicked. */
    @NotNull @Override
    @SuppressWarnings("StringBufferReplaceableByString")
    public Action updateCoords() {
        for (final MapRow row : Colls.seq(getMap()).getFirst()) {
            final StringBuilder s = new StringBuilder("Positions = { ");
            s.append(row.getLat()).append(" , ").append(row.getLng());
            s.append("}");

            setCoords(s.toString());
        }

        return actions.getDefault();
    }

    //~ Inner Classes ................................................................................................................................

    public class MapRow extends MapRowBase {}
}
