
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.configuration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;
import tekgenesis.metadata.form.widget.WidgetType;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static tekgenesis.common.Predefined.equalElements;
import static tekgenesis.common.Predefined.notNull;
import static tekgenesis.common.serializer.Streams.readNullableDouble;
import static tekgenesis.common.serializer.Streams.writeNullableDouble;

/**
 * Map widget serializable configuration.
 */
@SuppressWarnings("UnusedReturnValue")
public class MapConfig extends SizeConfig implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private Double                     lat           = TEK_LAT;
    @NotNull private Double                     lng           = TEK_LNG;
    @NotNull private Map<Integer, MarkerConfig> markerConfigs;
    @NotNull private Integer                    overlay       = DEFAULT_ROADMAP_OVERLAY;
    @NotNull private Integer                    zoom          = DEFAULT_ZOOM_LEVEL;

    //~ Constructors .................................................................................................................................

    /** Serialization constructor. */
    public MapConfig() {
        markerConfigs = new HashMap<>();
    }

    //~ Methods ......................................................................................................................................

    /** Set center. */
    @NotNull public MapConfig center(final double latitude, final double longitude) {
        lat = latitude;
        lng = longitude;
        return this;
    }

    @Override public void deserializeFields(StreamReader r) {
        super.deserializeFields(r);
        lat           = notNull(readNullableDouble(r), TEK_LAT);
        lng           = notNull(readNullableDouble(r), TEK_LNG);
        overlay       = r.readInt();
        zoom          = r.readInt();
        markerConfigs = deserializeMarker(r);
    }

    @Override
    @SuppressWarnings("RedundantIfStatement")  // It's clearer this way.
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapConfig)) return false;
        if (!super.equals(o)) return false;

        final MapConfig mapConfig = (MapConfig) o;

        if (!lat.equals(mapConfig.lat)) return false;
        if (!lng.equals(mapConfig.lng)) return false;
        if (!overlay.equals(mapConfig.overlay)) return false;
        if (!zoom.equals(mapConfig.zoom)) return false;
        if (!equalElements(mapConfig.markerConfigs.values(), markerConfigs.values())) return false;

        return true;
    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + lat.hashCode();
        result = 31 * result + lng.hashCode();
        result = 31 * result + overlay.hashCode();
        result = 31 * result + zoom.hashCode();
        result = 31 * result + markerConfigs.hashCode();
        return result;
    }

    /** Set overlay. */
    @NotNull public MapConfig overlay(final int o) {
        overlay = o;
        return this;
    }

    @Override public void serializeFields(StreamWriter w) {
        super.serializeFields(w);

        writeNullableDouble(w, lat);
        writeNullableDouble(w, lng);
        w.writeInt(overlay);
        w.writeInt(zoom);
        serializeMarker(w, markerConfigs);
    }

    /** Set zoom. */
    @NotNull public MapConfig zoom(final int z) {
        zoom = z;
        return this;
    }

    /** Zoom In. */
    @NotNull public MapConfig zoomIn() {
        return zoom(min(UPPER_ZOOM_LEVEL, zoom + 1));
    }

    /** Zoom Out. */
    @NotNull public MapConfig zoomOut() {
        return zoom(max(LOWER_ZOOM_LEVEL, zoom - 1));
    }

    /** True if center location is configured. */
    public boolean isCenterDefined() {
        return !TEK_LAT.equals(lat) && !TEK_LNG.equals(lng);
    }

    /** Return center latitude if defined. */
    @NotNull public Double getLat() {
        return lat;
    }

    /** Return center longitude if defined. */
    @NotNull public Double getLng() {
        return lng;
    }

    /** Return the color path of a marker. */
    @NotNull public String getMarkerColor(int row) {
        final MarkerConfig mc = markerConfigs.get(row);
        return mc == null ? BLUE_DEFAULT_MARKER : mc.getColor();
    }

    @NotNull public MapConfig setMarkerColor(final int row, @NotNull String url) {
        return setMarkerConfig(row, markerConfigs.containsKey(row) ? markerConfigs.get(row).getSize() : 1d, url);
    }

    @NotNull public MapConfig setMarkerConfig(final int row, final double size, @NotNull String url) {
        final MarkerConfig markerConfig = new MarkerConfig().setConfig(row, size, url);
        markerConfigs.put(row, markerConfig);
        return this;
    }

    /** Return the size of a marker. */
    @NotNull public Double getMarkerSize(int row) {
        final MarkerConfig mc = markerConfigs.get(row);
        if (mc == null || mc.getSize() < 0) return 1d;
        return mc.getSize() > MARKER_MAX_SIZE ? MARKER_MAX_SIZE : mc.getSize();
    }

    @NotNull public MapConfig setMarkerSize(final int row, final double size) {
        return setMarkerConfig(row, size, markerConfigs.containsKey(row) ? markerConfigs.get(row).getColor() : BLUE_DEFAULT_MARKER);
    }

    /** Return overlay type. */
    @NotNull public Integer getOverlay() {
        return overlay;
    }

    /** Return center latitude if defined. */
    @NotNull public Boolean isDefault() {
        return (TEK_LAT.equals(lat)) && (TEK_LNG.equals(lng)) && DEFAULT_ROADMAP_OVERLAY.equals(overlay) && DEFAULT_ZOOM_LEVEL.equals(zoom);
    }

    /** Return zoom level. */
    @NotNull public Integer getZoom() {
        return zoom;
    }

    @Override WidgetType getWidgetType() {
        return WidgetType.MAP;
    }

    @NotNull private Map<Integer, MarkerConfig> deserializeMarker(StreamReader r) {
        final int                        size   = r.readInt();
        final Map<Integer, MarkerConfig> result = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            final MarkerConfig markerConfig = new MarkerConfig();
            markerConfig.deserializeFields(r);
            result.put(markerConfig.getRow(), markerConfig);
        }
        return result;
    }

    private void serializeMarker(StreamWriter w, Map<Integer, MarkerConfig> markers) {
        w.writeInt(markers.size());
        for (final MarkerConfig config : markers.values())
            config.serializeFields(w);
    }

    //~ Static Fields ................................................................................................................................

    private static final Integer UPPER_ZOOM_LEVEL        = 21;
    private static final Integer LOWER_ZOOM_LEVEL        = 1;
    private static final Integer DEFAULT_ZOOM_LEVEL      = 14;
    private static final Integer DEFAULT_ROADMAP_OVERLAY = 1;
    private static final Double  TEK_LAT                 = -34.453428;
    private static final Double  TEK_LNG                 = -58.864671;
    private static final Double  MARKER_MAX_SIZE         = 5d;
    public static final String   BLUE_DEFAULT_MARKER     = "/public/sg/img/pin-blue.png";

    private static final long serialVersionUID = 1966252394094297055L;
}  // end class MapConfig
