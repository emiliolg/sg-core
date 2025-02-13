
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type.resource;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.annotation.GwtIncompatible;
import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.core.Metadata;
import tekgenesis.common.core.Resource;
import tekgenesis.common.logging.Logger;
import tekgenesis.common.media.Mime;
import tekgenesis.common.serializer.StreamReader;
import tekgenesis.common.serializer.StreamWriter;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.logging.Logger.getLogger;

/**
 * The Base implementation of a Resource.
 */
public abstract class AbstractResource implements Resource {

    //~ Instance Fields ..............................................................................................................................

    private String uuid = null;

    //~ Constructors .................................................................................................................................

    protected AbstractResource() {}
    protected AbstractResource(String uuid) {
        this.uuid = uuid;
    }

    //~ Methods ......................................................................................................................................

    /** Return resource as SimpleResourceImpl. */
    public abstract SimpleResourceImpl asSimple();

    @Override
    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public int compareTo(@NotNull Resource resource) {
        return uuid.compareTo((resource.getUuid()));
    }

    @Override public Resource copy() {
        return new SimpleResourceImpl(uuid, entries());
    }

    @Override public boolean equals(Object obj) {
        return this == obj || obj instanceof Resource && getUuid().equals(((Resource) obj).getUuid());
    }

    @Override public int hashCode() {
        return uuid.hashCode();
    }

    // @Override public Iterator<Entry> iterator() { return getEntries().iterator(); }

    @Override public void serialize(StreamWriter w) {
        w.writeString(getUuid());
        final ImmutableCollection<Entry> es = getEntries();
        w.writeInt(es.size());
        for (final Entry e : es) {
            w.writeString(e.getVariant());
            w.writeBoolean(e.isExternal());
            w.writeString(e.getName());
            w.writeString(e.getUrl());
            w.writeString(e.getMimeType());
        }
    }

    @Override public String toString() {
        return uuid;
    }

    @Override public ImmutableCollection<Entry> getEntries() {
        return immutable(entries().values());
    }

    @Nullable @Override public Entry getEntry(String variant) {
        return entries().get(variant);
    }

    @JsonIgnore @Nullable @Override public Entry getLarge() {
        return getEntry(LARGE);
    }

    @JsonIgnore @NotNull @Override public Entry getMaster() {
        final Entry master = getEntry(MASTER);
        if (master != null) return master;
        else {
            logger.error(MISSING_MASTER_RESOURCE + " for " + getUuid());
            return MISSING_RESOURCE_ENTRY;
        }
    }  // end method getMaster

    @JsonIgnore @Nullable @Override public Entry getThumb() {
        return getEntry(THUMB);
    }

    @Override public String getUuid() {
        return uuid;
    }

    @NotNull protected abstract Map<String, Entry> entries();

    //~ Methods ......................................................................................................................................

    /** Create a Simple (in memory) Resource with a master , to be used from the GWT side. */
    public static Resource createSimpleResource(@NotNull String uuid, boolean external, @NotNull String name, @NotNull String url,
                                                @NotNull String thumbUrl, @NotNull String mimeType) {
        final AbstractResource resource = new SimpleResourceImpl(uuid);
        resource.entries().put(MASTER, new EntryImpl(MASTER, external, name, url, mimeType, Metadata.empty()));
        if (isNotEmpty(thumbUrl)) resource.entries().put(THUMB, new EntryImpl(THUMB, external, name, thumbUrl, mimeType, Metadata.empty()));
        return resource;
    }

    /** Build it from the Serializer Stream. */
    @NotNull public static Resource instantiate(StreamReader r) {
        final String uuid = r.readString();

        final AbstractResource   resource = new SimpleResourceImpl(uuid);
        final Map<String, Entry> entries  = resource.entries();

        final int n = r.readInt();
        for (int i = 0; i < n; i++) {
            final String  variant  = r.readString();
            final boolean b        = r.readBoolean();
            final String  name     = r.readString();
            final String  url      = r.readString();
            final String  mimeType = r.readString();
            final Entry   e        = new EntryImpl(variant, b, name, url, mimeType, Metadata.empty());
            entries.put(e.getVariant(), e);
        }
        if (!entries.containsKey(MASTER)) logger.warning(MISSING_MASTER_RESOURCE + " (uuid:" + uuid + "')");

        return resource;
    }

    //~ Static Fields ................................................................................................................................

    private static final transient Logger logger = getLogger(Resource.class);

    public static final String MASTER = "MASTER";
    public static final String THUMB  = "THUMB";
    public static final String LARGE  = "LARGE";

    private static final long   serialVersionUID        = 8274205277677497612L;
    private static final String MISSING_MASTER_RESOURCE = "Missing MASTER resource";

    private static final Entry MISSING_RESOURCE_ENTRY = new EntryImpl(MASTER,
            true,
            "missing_master",
            "public/sg/img/noImage.jpg",
            Mime.IMAGE_JPEG.toString(),
            Metadata.empty());

    //~ Inner Classes ................................................................................................................................

    @SuppressWarnings("FieldMayBeFinal")
    public static class EntryImpl implements Entry {
        private boolean  external;
        private Metadata metadata;
        private String   mimeType;
        private String   name;
        private String   url;
        private String   variant;

        /** constructor.* */
        EntryImpl() {
            external = false;
            mimeType = null;
            name     = null;
            url      = null;
            variant  = null;
            metadata = null;
        }

        /** Entry Implementation. */
        public EntryImpl(@NotNull String variant, boolean external, @NotNull String name, @NotNull String url, @NotNull String mimeType,
                         @NotNull Metadata metadata) {
            this.external = external;
            this.variant  = isEmpty(variant) ? MASTER : variant;
            this.name     = name;
            this.url      = url;
            this.mimeType = mimeType;
            this.metadata = metadata;
        }

        @GwtIncompatible @Override
        @SuppressWarnings("NonJREEmulationClassesInClientCode")
        public int copyTo(Writer writer) {
            throw new UnsupportedOperationException();
        }

        @Override public int copyTo(OutputStream outputStream) {
            throw new UnsupportedOperationException();
        }

        @Override public boolean isExternal() {
            return external;
        }

        @NotNull @Override public Metadata getMetadata() {
            return metadata;
        }

        @Override public String getMimeType() {
            return mimeType;
        }

        @NotNull @Override public String getName() {
            return name;
        }

        @NotNull @Override public String getSha() {
            if (external) return "NO SHA. IS EXTERNAL";
            return url;
        }

        @NotNull @Override public String getUrl() {
            return url;
        }

        @NotNull @Override public String getVariant() {
            return variant;
        }

        private static final long serialVersionUID = 7718055284411617748L;
    }  // end class EntryImpl
}  // end class AbstractResource
