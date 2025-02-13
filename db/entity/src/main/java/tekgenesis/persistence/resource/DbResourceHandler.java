
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.resource;

import java.io.OutputStream;
import java.io.Writer;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.Resource;
import tekgenesis.common.env.Environment;
import tekgenesis.database.Database;
import tekgenesis.persistence.ResourceHandler;

import static java.util.UUID.randomUUID;

import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.persistence.resource.DbResource.mimeType;
import static tekgenesis.type.resource.AbstractResource.MASTER;

/**
 * The default Implementation of a ResourceFactory.
 */
public class DbResourceHandler implements ResourceHandler {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Database db;

    private final Environment env;

    //~ Constructors .................................................................................................................................

    /** Create a ResourceManager. */
    public DbResourceHandler(final Environment env, @NotNull Database db) {
        this.env = env;
        this.db  = db;
    }

    //~ Methods ......................................................................................................................................

    /** Garbage Collect unused resources. */
    public void collect() {
        final ResourcesGC gc = new ResourcesGC(env, db);
        gc.purge();
    }

    @Override public Resource.Factory create() {
        final String uuid = randomUUID().toString();
        return create(uuid);
    }

    @Override public Resource.Factory create(String uuid) {
        return new DbResource.DbFactory(new DbResource(db, uuid), MASTER);
    }

    @Override public Option<Resource.Content> findContent(final String sha) {
        final String mimeType = mimeType(db, sha);
        if (mimeType == null) return empty();

        final Resource.Content c = new Resource.Content() {
                @Override public int copyTo(Writer writer) {
                    return DbResource.downloadTo(db, sha, writer);
                }
                @Override public int copyTo(OutputStream os) {
                    return DbResource.downloadTo(db, sha, os);
                }
                @Override public String getMimeType() {
                    return mimeType;
                }
            };
        return some(c);
    }

    @Override public Option<Resource> findResource(String uuid) {
        final Resource resource = new DbResource(db, uuid);
        if (resource.getEntries().isEmpty()) return empty();
        return some(resource);
    }
}  // end class DbResourceHandler
