
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

import tekgenesis.common.core.Constants;
import tekgenesis.form.Action;
import tekgenesis.form.FormTable;
import tekgenesis.form.configuration.MapConfiguration;

/**
 * Map Form class.
 */
@SuppressWarnings({ "MagicNumber", "WeakerAccess" })
public class MapForm extends MapFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action doStuff() {
        final MapSubSubForm                   subForm = createLocate();
        final FormTable<MapSubSubForm.MapRow> map     = subForm.getMap();
        final MapSubSubForm.MapRow            mapRow  = map.add();
        mapRow.setLat(0);
        mapRow.setLng(0);
        final MapSubSubForm.MapRow mapRow2 = map.add();
        mapRow2.setLat(1);
        mapRow2.setLng(-1);

        return actions.getDefault();
    }

    @Override public void load() {
        final FormTable<PlacesRow> places = getPlaces();

        final PlacesRow tekgenesis = places.add();
        tekgenesis.setLatitud(-34.4522);
        tekgenesis.setLongitud(-58.8657546043);
        tekgenesis.setName("Tekgenesis");

        final PlacesRow pilar = places.add();
        pilar.setLatitud(-34.46);
        pilar.setLongitud(-58.91);
        pilar.setName("Pilar Centro");

        final PlacesRow highland = places.add();
        highland.setLatitud(-34.43);
        highland.setLongitud(-58.801);
        highland.setName("Highland Park");

        final FormTable<MapRow> map = getMap();

        for (final PlacesRow row : places) {
            final MapRow marker = map.add();
            marker.setLat(row.getLatitud());
            marker.setLng(row.getLongitud());
            marker.setTitle(row.getName());
            marker.setImg(Constants.TEK_LOGO);
        }

        final FormTable<Map2Row> map2 = getMap2();

        for (final PlacesRow row : places) {
            final Map2Row marker = map2.add();
            marker.setLat2(row.getLatitud());
            marker.setLng2(row.getLongitud());
            marker.setTitle2(row.getName());
            marker.setImg2(Constants.TEK_LOGO);
        }
    }

    //~ Inner Classes ................................................................................................................................

    public class Map2Row extends Map2RowBase {
        @NotNull @Override public Action show() {
            setClicked("You've selected " + getMap().getCurrent().getTitle());
            return actions.getDefault();
        }
    }

    public class MapRow extends MapRowBase {
        @NotNull @Override public Action show() {
            setClicked("You've selected " + getTitle());
            return actions.getDefault();
        }
    }

    public class PlacesRow extends PlacesRowBase {
        @NotNull @Override public Action go() {
            final MapConfiguration conf  = configuration(Field.MAP);
            final MapConfiguration conf2 = configuration(Field.MAP2);
            conf.center(getLatitud(), getLongitud()).zoom(15);
            conf2.center(getLatitud(), getLongitud()).zoom(15);
            return actions.getDefault();
        }
    }
}  // end class MapForm
