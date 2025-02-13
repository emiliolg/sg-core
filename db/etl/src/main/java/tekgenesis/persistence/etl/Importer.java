
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.persistence.etl;

import java.io.File;

import tekgenesis.common.core.QName;

/**
 * Importer interface.
 */
@SuppressWarnings("WeakerAccess")
public interface Importer {

    //~ Methods ......................................................................................................................................

    /** Returns true if it can import the file. */
    boolean accepts(File file);
    /** Returns true if it can import the file for the specific model. */
    boolean accepts(QName model, File file);
    /** Process the file to import. */
    void process(File file);
    /** Process the file to import for an specific model. */
    void process(QName model, File file);
}
