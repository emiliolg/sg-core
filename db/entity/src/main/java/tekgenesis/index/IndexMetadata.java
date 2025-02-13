
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.index;

import java.io.IOException;

import com.google.gson.JsonObject;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.jetbrains.annotations.NotNull;

import static tekgenesis.index.IndexConstants.*;
import static tekgenesis.index.Mappings.updateMetadataObject;

/**
 * Represents index metadata -> last update time, if it is deprecated and by whom it's being
 * updated.
 */
public class IndexMetadata {

    //~ Instance Fields ..............................................................................................................................

    private boolean deprecated;
    private long    updateTime;

    //~ Constructors .................................................................................................................................

    /** Index metadata constructor. */
    private IndexMetadata(boolean deprecated, long updateTime) {
        this.deprecated = deprecated;
        this.updateTime = updateTime;
    }

    //~ Methods ......................................................................................................................................

    /** Return as JSON. */
    public XContentBuilder toJson()
        throws IOException
    {
        return updateMetadataObject(this);
    }

    /** Check if index has been deprecated by a newer version of the index. */
    public boolean isDeprecated() {
        return deprecated;
    }

    /** Get last update time of the index. */
    public long getUpdateTime() {
        return updateTime;
    }

    IndexMetadata setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    IndexMetadata setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** Instantiate metadata from json object. */
    @NotNull public static IndexMetadata fromJson(@NotNull JsonObject metadata) {
        if (metadata.entrySet().isEmpty()) return defaultMetadata();

        final boolean deprecated = metadata.get(INDEX_DEPRECATED).getAsBoolean();
        final long    updateTime = metadata.get(INDEX_UPDATE_TIME).getAsLong();
        return new IndexMetadata(deprecated, updateTime);
    }

    /**
     * Get default metadata for indexes -> Not deprecated, zero milliseconds update time and no
     * updating node.
     */
    @NotNull static IndexMetadata defaultMetadata() {
        return new IndexMetadata(false, 0);
    }
}  // end class IndexMetadata
