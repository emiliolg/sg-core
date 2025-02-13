
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.io.InputStream;

/**
 * Interface to get resources by sha from external sources such as S3.
 */
public interface ShaRepository {

    //~ Methods ......................................................................................................................................

    /** Return InputStream for resource represented by sha. */
    InputStream get(String path, String sha);

    /** Stores resource by sha in repository. */
    void put(String path, String sha, byte[] bytes);
}
