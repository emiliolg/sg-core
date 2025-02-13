
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Pixel;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.control.DragFeature;
import org.gwtopenmaps.openlayers.client.control.DragFeatureOptions;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.event.MapZoomListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.OSM;
import org.gwtopenmaps.openlayers.client.layer.TransitionEffect;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.XYZ;
import org.gwtopenmaps.openlayers.client.layer.XYZOptions;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.common.logging.Logger;
import tekgenesis.metadata.form.configuration.MapConfig;
import tekgenesis.metadata.form.model.MultipleModel;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.IconType;
import tekgenesis.metadata.form.widget.MapType;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.view.client.Application;
import tekgenesis.view.client.controller.ViewCreator;
import tekgenesis.view.client.ui.base.HtmlWidgetFactory;
import tekgenesis.view.client.ui.base.Icon;

import static java.util.Collections.emptyList;

import static org.gwtopenmaps.openlayers.client.control.DragFeature.DragFeatureListener;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.Predefined.option;
import static tekgenesis.view.client.ui.base.HtmlDomUtils.querySelectorAll;
import static tekgenesis.view.client.ui.utils.Animation.isNotFullScreen;
import static tekgenesis.view.client.ui.utils.Animation.toggleFullScreen;

/**
 * MapUI open layers widget.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class MapUI extends BaseMultipleUI implements HasLabelUI, MultipleUI, MapZoomListener {

    //~ Instance Fields ..............................................................................................................................

    private Bounds bounds;

    private MapConfig              config;
    private int                    currentSectionIndex;
    @Nullable private WidgetUI     currentSectionUI;
    private Projection             defaultProjection;
    private final boolean          draggable;
    private final List<MapHandler> handlers;
    private final Label            label;
    private boolean                loaded;
    @SuppressWarnings("BooleanVariableAlwaysNegated")  // Easier to understand
    private boolean                loading;
    private final FlowPanel        mapPanel;
    private MapWidget              mapWidget;
    private List<LatLng>           markers;
    private SelectFeature          selectFeature = null;
    private final PopupPanel       simplePopup;
    private Vector                 vectorLayer;

    //~ Constructors .................................................................................................................................

    /** Creates a OpenLayer UI widget. */
    public MapUI(@NotNull final ModelUI container, @NotNull final Widget model) {
        super(container, model);
        mapPanel = HtmlWidgetFactory.div();
        mapPanel.addStyleName(MAP_WIDGET + " controls");
        initWidget(mapPanel);
        label             = HtmlWidgetFactory.label();
        defaultProjection = null;
        currentSectionUI  = null;
        applyConfig(new MapConfig());
        mapWidget   = null;
        vectorLayer = null;
        bounds      = null;
        draggable   = model.isDraggable();
        markers     = emptyList();
        handlers    = new ArrayList<>();
        simplePopup = new PopupPanel(true);
        simplePopup.setStyleName("map-popup");
        addPopupListener();
        addFullScreenBtn();

        if (isNotEmpty(model.getOnNewLocationMethodName()) && Geolocation.isSupported()) addGeolocationBtn();
    }

    //~ Methods ......................................................................................................................................

    /** Registers a handler to fake value changes. */
    public void addMapHandler(MapHandler handler) {
        handlers.add(handler);
    }

    /** Applies a given config and redraws the map. */
    public void applyConfigAndRedraw(MapConfig c) {
        if (c != config) {
            applyConfig(c);
            redraw();
        }
    }

    @Override public void doneFiltering() {
        redraw();
    }

    @Override public void onMapZoom(MapZoomEvent mapZoomEvent) {
        simplePopup.hide();
    }

    @Override public void toggleFilteredSection(int rowIndex, boolean accepted) {
        markers.get(rowIndex).setVisible(accepted);
    }

    @Override public void updateModel(@NotNull MultipleModel multiple) {
        markers = new ArrayList<>(multiple.size());
        for (final RowModel row : multiple)
            markers.add(createTuple(row));
        redraw();
    }

    @Override public String getLabel() {
        return label.getText();
    }

    @Override public void setLabel(String label) {
        this.label.setText(label);
    }

    @Override public Iterable<WidgetUI> getSection(int section) {
        if (section == currentSectionIndex) return option(currentSectionUI);
        else return Option.empty();
    }

    @Override protected void fitToExactSections() {}

    private void addDrag() {
        if (draggable) {
            final DragFeature dragFeature = createDragFeature();
            mapWidget.getMap().addControl(dragFeature);
            dragFeature.activate();
        }
    }

    private void addFullScreenBtn() {
        final Button btn = new Button();
        btn.addClickHandler(event -> {
            toggleFullScreen(mapPanel.getElement());
            if (isNotFullScreen()) Icon.replaceInWidget(btn, IconType.EXPAND.getClassName());
            else Icon.replaceInWidget(btn, IconType.COMPRESS.getClassName());
        });
        Icon.replaceInWidget(btn, IconType.EXPAND.getClassName());
        btn.addStyleName("btn-fullscreen");
        btn.setTabIndex(-2);
        mapPanel.add(btn);
    }

    private void addGeolocationBtn() {
        final Button btn = new Button();
        btn.addClickHandler(event -> {
            final Geolocation geo = Geolocation.getIfSupported();
            geo.getCurrentPosition(new Callback<Position, PositionError>() {
                    @Override public void onSuccess(Position result) {
                        final Position.Coordinates c = result.getCoordinates();
                        handlers.forEach(h -> h.onNewLocation(c.getLatitude(), c.getLongitude()));
                    }

                    @Override public void onFailure(PositionError reason) {
                        logger.info("Cannot retrieve users location: " + reason.getMessage());
                    }
                });
        });
        Icon.replaceInWidget(btn, IconType.LOCATION_ARROW.getClassName());
        btn.addStyleName("btn-location");
        btn.setTabIndex(-2);
        mapPanel.add(btn);
    }

    private void addMarker(final Point p, final int i) {
        final Map map = mapWidget.getMap();

        p.transform(defaultProjection, new Projection(map.getProjection()));

        // Create a style that we will use for the point, this style also contains a label
        final Style st      = new Style();
        final int   pinSize = (int) (PIN_SIZE * config.getMarkerSize(i));
        st.setGraphicOffset(-pinSize * 2 / 7, -pinSize);  // the marker is centered on 2/7 of pin.png's width
        st.setGraphicSize(pinSize, pinSize);
        st.setExternalGraphic(config.getMarkerColor(i));
        st.setFillOpacity(1.0);

        // Create the vectorfeature
        final VectorFeature pointFeature = new VectorFeature(p, st);
        pointFeature.getAttributes().setAttribute(SECTION, i);
        vectorLayer.addFeature(pointFeature);
    }

    private void addMarkerClick() {
        // Secondly add a VectorFeatureSelectedListener to the feature
        final Map map = mapWidget.getMap();

        // on feature selected show popup
        vectorLayer.addVectorFeatureSelectedListener(eventObject -> {
            final VectorFeature vectorFeature = vectorLayer.getSelectedFeatures()[0];
            final LonLat        centerLonLat  = vectorFeature.getCenterLonLat();
            final Pixel         pixel         = map.getPixelFromLonLat(centerLonLat);
            final int           sectionNumber = vectorFeature.getAttributes().getAttributeAsInt(SECTION);
            showPopUp(pixel.x(), pixel.y(), sectionNumber);
        });
    }

    private void addPopupListener() {
        simplePopup.addCloseHandler(event -> {
            // Un-select all features on popup close
            selectFeature.unselectAll(null);
        });
    }

    private void applyConfig(MapConfig c) {
        config = c;
    }

    private DragFeature createDragFeature() {
        final DragFeatureOptions dragFeatureOptions = new DragFeatureOptions();
        dragFeatureOptions.onComplete(createDragFeatureListener());
        return new DragFeature(vectorLayer, dragFeatureOptions);
    }

    private DragFeatureListener createDragFeatureListener() {
        return (vectorFeature, pixel) -> {
                   final Map map = mapWidget.getMap();
                   // section sectionNumber
                   final int sectionNumber = vectorFeature.getAttributes().getAttributeAsInt(SECTION);

                   // transform pixel to map coordinates
                   final LonLat lonLat = map.getLonLatFromPixel(pixel);
                   lonLat.transform(map.getProjection(), defaultProjection.getProjectionCode());

                   for (final MapHandler mapHandler : handlers)
                       mapHandler.onSectionChange(sectionNumber, lonLat.lat(), lonLat.lon());
               };
    }

    @Nullable private LatLng createTuple(RowModel model) {
        final Double lat = (Double) model.getByFieldSlot(LATITUDE);
        final Double lon = (Double) model.getByFieldSlot(LONGITUDE);
        return lat != null && lon != null ? new LatLng(lat, lon) : null;
    }

    private void init(MapType mapType) {
        loading = true;
        // Load Api and set mainLayer based on map type
        switch (mapType) {
        case OPENSTREET:
            ApiLoader.go(OPEN_LAYERS_JS, () -> ApiLoader.go(OSM_URL, this::initOSM));
            break;
        case UNIGIS:
            ApiLoader.go(OPEN_LAYERS_JS, () -> ApiLoader.go(OSM_URL, this::initXyz));
            break;
        }
    }  // end method init

    private void initOSM() {
        final Layer mainLayer = OSM.Mapnik(MAPNIK);
        mainLayer.setIsBaseLayer(true);
        setCommonMapOptions(mainLayer);
    }

    private void initXyz() {
        final XYZOptions unigisOptions = new XYZOptions();
        unigisOptions.setIsBaseLayer(true);
        unigisOptions.setAttribution("UNIGIS");
        unigisOptions.setTransitionEffect(TransitionEffect.RESIZE);
        unigisOptions.setVisibility(true);

        final String unigisServer = Application.getInstance().getMapServerUrl();
        final XYZ    unigisLayer  = new XYZ("unigis",
                "http://" + unigisServer + "/UNIGIS/tiles/xyz.aspx?x=${x}&y=${y}&z=${z}&mapa=UNIGIS",
                unigisOptions);
        unigisLayer.setOpacity(OPACITY);

        setCommonMapOptions(unigisLayer);
    }

    private void redraw() {
        if (!loading) init(getModel().getMapType());
        else if (loaded) {
            final Map map = mapWidget.getMap();
            updateMarkers();
            mapPanel.setHeight(config.getHeightPx());
            if (config.getWidth() > 0) mapPanel.setWidth(config.getWidthPx());
            else mapPanel.setWidth("");

            mapWidget.setHeight("100%");
            mapWidget.setWidth("100%");

            // Fit to bounds if markers>1 and there is not configurations override
            if (!markers.isEmpty() && config.isDefault()) map.zoomToExtent(bounds);
            else {
                // Center and zoom to a location if defined
                if (config.isCenterDefined()) {
                    final LonLat lonLat = getCenterAsLonLat();
                    lonLat.transform(defaultProjection.getProjectionCode(), map.getProjection());      // transform lon lat to
                                                                                                       // OSM coordinate system
                    map.setCenter(lonLat, config.getZoom());
                }
                else map.zoomTo(config.getZoom());
            }
        }
    }                                                                                                  // end method redraw

    private void removeTabNavigation() {
        final JsArray<Element> feedBacks = querySelectorAll(mapPanel.getElement(), "a");
        if (feedBacks != null) {
            for (int i = 0; i < feedBacks.length(); i++) {
                final Element element = feedBacks.get(i);
                element.setTabIndex(-2);
            }
        }
    }

    private void showPopUp(int x, int y, int section) {
        final SectionWrapper view = new SectionWrapper();
        ViewCreator.createSingleView(container(), getMultipleModel().getWidgetByFieldSlot(WIDGET), view);
        // Widgets, if present, are in the third column
        currentSectionUI    = view.getWidget().withinMultiple(this, section, WIDGET);
        currentSectionIndex = section;

        for (final MapHandler mapHandler : handlers)
            mapHandler.onMarkerCreated(section);

        simplePopup.setWidget(currentSectionUI);
        simplePopup.setPopupPosition(mapWidget.getAbsoluteLeft() + x, mapWidget.getAbsoluteTop() + y);
        simplePopup.show();
    }

    /* could return bounds */
    private void updateMarkers() {
        bounds = new Bounds();
        if (markers != null) {
            vectorLayer.destroyFeatures();
            addDrag();
            addMarkerClick();
            for (int i = 0; i < markers.size(); i++) {
                final LatLng marker = markers.get(i);
                if (marker != null && marker.visible) {
                    final Point point = new Point(marker.lng, marker.lat);
                    addMarker(point, i);
                    bounds.extend(point);
                }
            }
        }
    }
    // suppressed warning (shouldn't be necessary when we delete MapUI)
    @SuppressWarnings("ConstantConditions")
    private LonLat getCenterAsLonLat() {
        return config.getLng() != null && config.getLat() != null ? new LonLat(config.getLng(), config.getLat()) : new LonLat(0, 0);
    }

    private void setCommonMapOptions(Layer mainLayer) {
        // common configurations
        loaded = true;
        final MapOptions defaultMapOptions = new MapOptions();
        @SuppressWarnings("MagicNumber")
        final Bounds     extent = new Bounds(-20037508, -20037508, 20037508, 20037508);
        defaultMapOptions.setNumZoomLevels(NUM_ZOOM_LEVELS);
        defaultMapOptions.setDisplayProjection(new Projection("EPSG:4326"));
        defaultMapOptions.setProjection("EPSG:900913");
        defaultMapOptions.setUnits("m");
        defaultMapOptions.setMaxResolution(MAX_RESOLUTION);
        defaultMapOptions.setMaxExtent(extent);
        defaultMapOptions.setRestrictedExtent(extent);

        // Create a MapWidget
        mapWidget         = new MapWidget("100%", "100%", defaultMapOptions);
        defaultProjection = new Projection("EPSG:4326");
        final Map map = mapWidget.getMap();
        map.addLayer(mainLayer);
        map.addMapZoomListener(this);

        // Center and zoom to a location
        final LonLat lonLat = getCenterAsLonLat();
        lonLat.transform(defaultProjection.getProjectionCode(), map.getProjection());  // transform lonlat to OSM coordinate
                                                                                       // system
        map.setCenter(lonLat, config.getZoom());

        // Add a marker Layer
        vectorLayer = new Vector("MarkerLayer");

        // Add SelectFeature to vectorLayer
        selectFeature = new SelectFeature(vectorLayer);
        selectFeature.setAutoActivate(true);
        map.addControl(selectFeature);

        addMarkerClick();
        addDrag();

        map.addLayer(vectorLayer);
        mapPanel.add(mapWidget);
        redraw();
        removeTabNavigation();
    }  // end method setCommonMapOptions

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(MapUI.class);

    private static final float MAX_RESOLUTION = 156543.0339f;

    @NonNls private static final String MAPNIK  = "Mapnik";
    private static final int            OPACITY = 70;

    @NonNls private static final String OPEN_LAYERS_JS = "/external/js/OpenLayers.js";
    @NonNls private static final String OSM_URL        = "/external/js/OpenStreetMap.js";

    private static final int NUM_ZOOM_LEVELS = 19;

    private static final int PIN_SIZE = 32;

    @NonNls private static final String MAP_WIDGET = "mapWidget";

    public static final int             LATITUDE  = 0;
    public static final int             LONGITUDE = 1;
    private static final int            WIDGET    = 2;
    @NonNls
    @SuppressWarnings("DuplicateStringLiteralInspection")
    private static final String         SECTION = "section";

    //~ Inner Interfaces .............................................................................................................................

    public interface MapHandler {
        /** Called on marker created. */
        void onMarkerCreated(int row);

        /** On new location. */
        void onNewLocation(final double latitude, final double longitude);

        /** On section change. */
        void onSectionChange(final int section, final Double lat, final Double lng);

        class Default implements MapHandler {
            @Override public void onMarkerCreated(int row) {
                logger.info("MapHandler.onMarkerCreated row :: " + row);
            }

            @Override public void onNewLocation(double lat, double lng) {
                logger.info("MapHandler.onNewLocation with lat :: " + lat + " and lng :: " + lng);
            }

            @Override public void onSectionChange(int section, Double lat, Double lng) {
                logger.info("MapHandler.onSectionChange section :: " + section + " with lat :: " + lat + " and lng :: " + lng);
            }
        }
    }

    //~ Inner Classes ................................................................................................................................

    private static class LatLng {
        private final double lat;
        private final double lng;
        private boolean      visible;

        LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
            visible  = true;
        }

        private void setVisible(boolean visible) {
            this.visible = visible;
        }
    }

    private class SectionWrapper implements HasWidgetsUI {
        private WidgetUI unique = null;

        private SectionWrapper() {}

        @Override public void addChild(WidgetUI ui) {
            unique = ui;
        }

        private WidgetUI getWidget() {
            return unique;
        }
    }
}
