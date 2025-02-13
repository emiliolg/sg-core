
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
 * Importer Task interface.
 */
public abstract class ImporterTaskInstance extends TaskInstance<ImporterTaskInstance> {

    //~ Constructors .................................................................................................................................

    protected ImporterTaskInstance(final Task<ImporterTaskInstance> task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if it can import the file. */
    public abstract boolean accepts(File file);
    /** Returns true if it can import the file for the specific model. */
    public abstract boolean accepts(QName model, File file);
    /** Process the file to import. */
    public abstract void process(File file);
    /** Process the file to import for an specific model. */
    public abstract void process(QName model, File file);

    @NotNull @Override protected Status run() {
        // Todo this should check the directory and see if there is a file to import ...
        return Status.done();
    }
}
