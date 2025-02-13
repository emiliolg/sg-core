
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.task;

import java.io.File;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.QName;

/**
 * An Importer Task.
 */
public class ImporterTask extends Task<ImporterTaskInstance> {

    //~ Constructors .................................................................................................................................

    /** Create an Importer Task by name. */
    public ImporterTask(@NotNull final String taskFqn) {
        super(taskFqn);
    }

    ImporterTask(@NotNull final Class<? extends ImporterTaskInstance> taskClass) {
        super(taskClass);
    }

    //~ Methods ......................................................................................................................................

    /**
     * Attempt to import the file. Returns true if the file was processed, false if the current
     * importer does not accepts it
     */
    public boolean processFile(final File file) {
        if (!getInstance().accepts(file)) return false;
        processing(file);
        getInstance().process(file);
        return true;
    }

    /**
     * Attempt to import the file. Returns true if the file was processed, false if the current
     * importer does not accepts it
     */
    public boolean processFile(final QName model, final File file) {
        if (!getInstance().accepts(model, file)) return false;
        processing(file);
        getInstance().process(model, file);
        return true;
    }

    private void processing(final File file) {
        getInstance().logInfo("Importing file '%s'.", file);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -3183509277597260454L;
}  // end class ImporterTask
