
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.cache.database;

import org.infinispan.commons.marshall.AdvancedExternalizer;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a cache serializer. Provides class name and implementation to be used for cache
 * serialization.
 *
 * <p>In order to subscribe cache serializers, implement this interface and package it as a jar file
 * with a file named</p>
 *
 * <p>META-INF/services/tekgenesis.cache.database.CacheSerializer</p>
 *
 * <p>This file contains the single line:</p>
 *
 * <p>com.example.impl.CacheSerializerImpl</p>
 */
public interface CacheSerializer {

    //~ Methods ......................................................................................................................................

    /** Return AdvancedExternalizer. */
    AdvancedExternalizer<?> getExternalizer();

    /** Get the serializer Id. */
    @NotNull Integer getId();
}
