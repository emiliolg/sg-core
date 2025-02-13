
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import javax.annotation.Generated;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.form.Action;
import tekgenesis.form.configuration.MapConfiguration;
import tekgenesis.form.configuration.MapConfiguration.MarkerColor;

import static tekgenesis.form.configuration.MapConfiguration.MarkerColor.*;
import static tekgenesis.form.configuration.MapConfiguration.Overlay.SATELLITE;

/**
 * User class for Form: MapConfigurationForm
 */
@Generated(value = "tekgenesis/showcase/MapShowcase.mm", date = "1365089522426")
public class MapConfigurationForm extends MapConfigurationFormBase {

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Action changeSize() {
        final MapConfiguration configuration = configuration(Field.ARGENTINA);
        configuration.markerConfig(1, getMarkerSize().doubleValue(), getMapColor());
        return actions().getDefault();
    }

    /** Invoked when the form is loaded. */
    @Override public void load() {
        setColorOptions(Colls.listOf("Blue", "Yellow", "Green", "Red"));

        final MapConfiguration argConfig = configuration(Field.ARGENTINA);
        argConfig.center(-38.15, -65.916667).zoom(4);  // Puelches, La Pampa
        getArgentina().add().setLatLng(-38.15, -65.916667);
        getArgentina().add().setLatLng(-38.19, -66.416667);
        argConfig.markerSize(1, 3);                    // setting 3 times the size of the row 1 marker
        argConfig.markerSize(3, 3);                    // if size is set to a non existent row, nothing will happen.
        argConfig.markerColor(1, YELLOW);

        final MapConfiguration etiConfig = configuration(Field.ETHIOPIA);
        etiConfig.center(9.232249, 39.527161).zoom(6);  // Ethiopia
        getEthiopia().add().setLatLng(9.232249, 39.527161);

        final MapConfiguration pilConfig = configuration(Field.PILAR);
        pilConfig.center(-34.4522, -58.8657546043).zoom(15).overlay(SATELLITE);  // TekGenesis
        getPilar().add().setLatLng(-34.4522, -58.8657546043);
    }

    @NotNull @Override public Action size1280() {
        setMapsDimensions(1280, 720);
        return actions.getDefault();
    }

    @NotNull @Override public Action size640() {
        setMapsDimensions(640, 480);
        return actions.getDefault();
    }

    @NotNull @Override public Action size854() {
        setMapsDimensions(854, 480);
        return actions.getDefault();
    }

    /** Invoked when button(zoomIn) is clicked. */
    @NotNull @Override public Action zoomIn() {
        final MapConfiguration argConfig = configuration(Field.ARGENTINA);
        argConfig.zoomIn();
        final MapConfiguration etiConfig = configuration(Field.ETHIOPIA);
        etiConfig.zoomIn();
        final MapConfiguration pilConfig = configuration(Field.PILAR);
        pilConfig.zoomIn();
        return actions.getDefault();
    }

    /** Invoked when button(zoomOut) is clicked. */
    @NotNull @Override public Action zoomOut() {
        final MapConfiguration argConfig = configuration(Field.ARGENTINA);
        argConfig.zoomOut();
        final MapConfiguration etiConfig = configuration(Field.ETHIOPIA);
        etiConfig.zoomOut();
        final MapConfiguration pilConfig = configuration(Field.PILAR);
        pilConfig.zoomOut();
        return actions.getDefault();
    }

    private MarkerColor getMapColor() {
        switch (getColor()) {
        case "Blue":
            return BLUE;
        case "Yellow":
            return YELLOW;
        case "Red":
            return RED;
        case "Green":
            return GREEN;
        }
        throw new IllegalArgumentException("Color doesn't exist");
    }

    private void setMapsDimensions(int w, int h) {
        final MapConfiguration argConfig = configuration(Field.ARGENTINA);
        argConfig.dimension(w, h);
        final MapConfiguration etiConfig = configuration(Field.ETHIOPIA);
        etiConfig.dimension(w, h);
        final MapConfiguration pilConfig = configuration(Field.PILAR);
        pilConfig.dimension(w, h);
    }

    //~ Inner Classes ................................................................................................................................

    public class ArgentinaRow extends ArgentinaRowBase {
        public void setLatLng(double lat, double lng) {
            setArgLat(lat);
            setArgLng(lng);
        }
    }

    public class EthiopiaRow extends EthiopiaRowBase {
        public void setLatLng(double lat, double lng) {
            setEtiLat(lat);
            setEtiLng(lng);
        }
    }

    public class PilarRow extends PilarRowBase {
        public void setLatLng(double lat, double lng) {
            setPilLat(lat);
            setPilLng(lng);
        }
    }
}  // end class MapConfigurationForm
