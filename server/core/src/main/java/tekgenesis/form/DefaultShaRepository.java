
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jetbrains.annotations.Nullable;

/**
 * Default implementation for ShaRepository. Fallbacks in WebResourceManager
 */
public class DefaultShaRepository implements ShaRepository {

    //~ Methods ......................................................................................................................................

    @Nullable @Override public InputStream get(String path, String sha) {
        final byte[] bytes = WebResourceManager.getInstance().readWebResource(path);
        return bytes == null ? null : new ByteArrayInputStream(bytes);
    }

    @Override public void put(String path, String sha, byte[] bytes) {}
}
