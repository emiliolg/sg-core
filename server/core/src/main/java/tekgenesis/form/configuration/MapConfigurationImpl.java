
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.configuration;

import java.awt.geom.Point2D;

import org.jetbrains.annotations.NotNull;

import tekgenesis.metadata.form.configuration.MapConfig;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Widget;

class MapConfigurationImpl extends AbstractWidgetConfiguration<MapConfig> implements MapConfiguration {

    //~ Constructors .................................................................................................................................

    MapConfigurationImpl(@NotNull final Model model, @NotNull final Widget widget) {
        super(model, widget);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public MapConfiguration center(Point2D center) {
        config().center(center.getX(), center.getY());
        return this;
    }

    @NotNull @Override public MapConfiguration center(double lat, double lng) {
        config().center(lat, lng);
        return this;
    }

    @NotNull @Override public MapConfiguration dimension(int w, int h) {
        config().dimension(w, h);
        return this;
    }

    @NotNull @Override public MapConfiguration height(int h) {
        config().height(h);
        return this;
    }

    @NotNull @Override public MapConfiguration markerColor(int row, MarkerColor color) {
        config().setMarkerColor(row, color.getUrl());
        return this;
    }

    @NotNull @Override public MapConfiguration markerConfig(int row, double size, MarkerColor color) {
        config().setMarkerConfig(row, size, color.getUrl());
        return this;
    }

    @NotNull @Override public MapConfiguration markerSize(int row, double size) {
        config().setMarkerSize(row, size);
        return this;
    }

    @NotNull @Override public MapConfiguration overlay(@NotNull Overlay overlay) {
        config().overlay(overlay.ordinal());
        return this;
    }

    @NotNull @Override public MapConfiguration width(int w) {
        config().width(w);
        return this;
    }

    @NotNull @Override public MapConfiguration zoom(int zoom) {
        config().zoom(zoom);
        return this;
    }

    @NotNull @Override public MapConfiguration zoomIn() {
        config().zoomIn();
        return this;
    }

    @NotNull @Override public MapConfiguration zoomOut() {
        config().zoomOut();
        return this;
    }

    @NotNull @Override MapConfig createConfig() {
        return new MapConfig();
    }
}  // end class MapConfigurationImpl
