
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

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.QName;
import tekgenesis.task.ImporterTask;
import tekgenesis.test.basic.Author;

/**
 * An importer class for tests.
 */
@SuppressWarnings({ "DuplicateStringLiteralInspection", "UnusedDeclaration" })
public class TestImporter extends TestImporterBase {

    //~ Constructors .................................................................................................................................

    TestImporter(final ImporterTask task) {
        super(task);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean accepts(@NotNull final File file) {
        return "pepe".equals(file.getName());
    }

    @Override public void process(final File file) {
        final Author author = Author.create();
        author.setName("Importer");
        author.setLastName("Test 2");
        author.insert();
    }

    @Override public void process(final QName model, final File file) {
        final Author author = Author.create();
        author.setName("Importer Model");
        author.setLastName("Test");
        author.insert();
    }
}
