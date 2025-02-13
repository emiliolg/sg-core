
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

/**
 * Configuration for Map widgets.
 */
@SuppressWarnings("UnusedReturnValue")
public interface MapConfiguration extends SizeConfiguration<MapConfiguration> {

    //~ Methods ......................................................................................................................................

    /** Set map center location. */
    @NotNull MapConfiguration center(Point2D center);

    /** Set map center location. */
    @NotNull MapConfiguration center(final double lat, final double lng);

    /** Set a map marker's color. Will set default size 1. */
    @NotNull MapConfiguration markerColor(int row, MarkerColor color);

    /** Set a map marker's size and its color. Size should be a double between 0 and 5. */
    @NotNull MapConfiguration markerConfig(int row, double size, MarkerColor color);

    /** Set a map marker's size. Size should be a double between 0 and 5. Default color is 1. */
    @NotNull MapConfiguration markerSize(int row, double size);

    /** Set map overlay configuration. */
    @NotNull MapConfiguration overlay(@NotNull final Overlay overlay);

    /** Set map zoom level. */
    @NotNull MapConfiguration zoom(final int zoom);

    /** Zoom in. */
    @NotNull MapConfiguration zoomIn();

    /** Zoom out. */
    @NotNull MapConfiguration zoomOut();

    //~ Enums ........................................................................................................................................

    /**
     * Marker colors.
     */
    enum MarkerColor {
        YELLOW("/public/sg/img/pin-yellow.png"),  // yellow pin
        GREEN("/public/sg/img/pin-green.png"),    // green  pin
        BLUE(MapConfig.BLUE_DEFAULT_MARKER),      // blue   pin
        RED("/public/sg/img/pin-red.png");        // red    pin

        @NotNull private final String url;

        /** Constructor with the path to the picture with that color. */
        MarkerColor(@NotNull String url) {
            this.url = url;
        }

        /** Returns the url with the path of the image. */
        @NotNull public String getUrl() {
            return url;
        }
    }

    /**
     * Map Overlay types.
     */
    enum Overlay {
        /** This map type displays a transparent layer of major streets on satellite images. */
        HYBRID,
        /** This map type displays a normal street map. */
        ROADMAP,
        /** This map type displays satellite images. */
        SATELLITE,
        /** This map type displays maps with physical features such as terrain and vegetation. */
        TERRAIN
    }
}  // end interface MapConfiguration
