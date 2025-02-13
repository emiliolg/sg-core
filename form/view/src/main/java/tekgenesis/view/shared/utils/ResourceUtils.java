
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.shared.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Resource;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.media.Mimes;
import tekgenesis.type.Type;
import tekgenesis.type.resource.AbstractResource;
import tekgenesis.type.resource.Variant;
import tekgenesis.view.client.Application;

import static tekgenesis.common.Predefined.*;
import static tekgenesis.common.core.Constants.FILE_NAME_URL_PARAM;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.common.media.Mime.APPLICATION_OCTET_STREAM;
import static tekgenesis.common.media.Mime.IMAGE_SVG_XML;

/**
 * Resource Utils method that could be shared between client or server code.
 */
public class ResourceUtils {

    //~ Constructors .................................................................................................................................

    private ResourceUtils() {}

    //~ Methods ......................................................................................................................................

    /** Returns true is resource has preview image. */
    public static boolean hasPreview(Resource resource) {
        return !resource.getMaster().getName().contains(".psd") &&
               (resource.getThumb() != null || resource.getLarge() != null || Mimes.isImage(resource.getMaster().getMimeType()) || isImage(resource));
    }

    /**
     * Returns the URI of the customVariant if found. Opposite case, it returns the URI of the
     * master variant
     */
    public static Tuple<SafeUri, String> getCustomOrMasterUri(@NotNull Resource value, @Nullable String customVariant) {
        return getResourceUrl(value, null, customVariant);
    }

    /** Returns if Resource is scalable image. */
    public static boolean isScalableImage(Resource resource, String uri) {
        return Mimes.getMimeType(resource.getMaster().getUrl()).equals(IMAGE_SVG_XML.getMime()) || uri.endsWith(".svg");
    }

    /** Returns Master Mime Type. */
    public static String getMimeType(Resource resource) {
        final Resource.Entry master = resource.getMaster();
        try {
            return master.getMimeType();
        }
        catch (final UnsupportedOperationException e) {
            return Mimes.getMimeType(master.getUrl());
        }
    }

    /** Returns is Resource is a video. */
    public static boolean isVideo(Resource resource) {
        return resource.getThumb() != null && isEmpty(resource.getMaster().getMimeType()) &&
               Mimes.getMimeType(resource.getMaster().getUrl()).equals(APPLICATION_OCTET_STREAM.getMime());
    }

    /** Returns values as Resource. */
    public static Resource getResourceValue(Type type, Object value) {
        if (type.isResource()) return (Resource) value;

        final String url         = (String) value;
        final String resourceUrl;
        if (type.isString()) resourceUrl = url;
        else if (type.isEnum()) resourceUrl = "/img?enum=" + type.getImplementationClassName() + "&name=" + url;

        // if (isEmpty(resourceUrl))
        // throw new IllegalArgumentException("Cant create resource for type : " + type + ", with value " + value);
        else throw new IllegalArgumentException("Cant create resource for type : " + type + ", with value " + value);
        final String name = url.substring(url.lastIndexOf('/') + 1, url.length());
        return AbstractResource.createSimpleResource(String.valueOf(resourceUrl.hashCode()), true, name, resourceUrl, "", "");
    }

    /** Returns values as Resource. */
    public static List<Resource> getResourceValues(Type type, Iterable<Object> values) {
        if (type.isEnum()) return cast(values);
        final List<Resource> resources = new ArrayList<>();
        for (final Object value : values)
            resources.add(getResourceValue(type, value));
        return resources;
    }

    /** Returns URI for large display. */
    public static Tuple<SafeUri, String> getSmallestDisplayUri(@NotNull Resource value, @Nullable String largeVariant) {
        return getResourceUrl(value, Variant.LARGE, largeVariant);
    }

    /** Returns URI to display as thumb. */
    public static Tuple<SafeUri, String> getSmallestThumbUri(@NotNull Resource value, @Nullable String thumbVariant) {
        return getResourceUrl(value, Variant.THUMB, thumbVariant);
    }

    /** Returns main URI. */
    public static Tuple<SafeUri, String> getUri(Resource value) {
        return getResourceUrl(value, Variant.MASTER, null);
    }

    /** Returns Resource as Type. */
    public static List<Object> getValuesFromResource(Type type, Iterable<Resource> resources) {
        final List<Object> values = new ArrayList<>();
        for (final Resource resource : resources)
            values.add(getValueFromResource(type, resource));
        return values;
    }

    private static Resource.Entry getBestOptionEntry(@NotNull Resource resource, @Nullable Variant variant, @Nullable String variantName) {
        final Resource.Entry entry = (!isEmpty(variantName)) ? resource.getEntry(variantName) : null;
        if (variant != null) {
            switch (variant) {
            case THUMB:
                return notNull(entry, notNull(resource.getThumb(), notNull(resource.getLarge(), resource.getMaster())));
            case LARGE:
                return notNull(entry, notNull(resource.getLarge(), resource.getMaster()));
            case MASTER:
                return resource.getMaster();
            }
        }
        else return notNull(entry, resource.getMaster());
        return null;
    }

    private static boolean isImage(Resource resource) {
        return Mimes.isImage(Mimes.getMimeType(resource.getMaster().getUrl()));
    }

    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    private static Tuple<SafeUri, String> getResourceUrl(Resource resource, @Nullable Variant variant, @Nullable String variantName) {
        final Resource.Entry entry = ensureNotNull(getBestOptionEntry(resource, variant, variantName), "Unsupported variant " + variantName);
        if (entry.isExternal()) return tuple(UriUtils.fromString(entry.getUrl()), entry.getVariant());
        final String url = Application.getInstance().getResourceServerUrl();
        return tuple(UriUtils.fromString(url + "?sha=" + entry.getSha() + FILE_NAME_URL_PARAM + UrlUtils.encode(entry.getName())),
            entry.getVariant());
    }  // end method getResourceUrl

    /** Returns Resource as Type. */
    private static Object getValueFromResource(Type type, Resource value) {
        if (type.isResource()) return value;
        else if (type.isString() || type.isEnum()) return value.getMaster().getUrl();
        else throw new UnsupportedOperationException("Cant get " + type.getImplementationClassName() + " Value from resource ");
    }
}  // end class ResourceUtils
