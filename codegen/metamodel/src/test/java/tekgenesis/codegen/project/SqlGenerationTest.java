
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.project;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.util.Files;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.codegen.sql.SqlCodeGenerator.generateSchema;

@SuppressWarnings({ "JavaDoc", "DuplicateStringLiteralInspection", "MagicNumber" })
public class SqlGenerationTest {

    //~ Methods ......................................................................................................................................

    @Test public void createSchema()
        throws IOException
    {
        final File dbDir = new File("target/codegen/metamodel/test-output/resources");
        Files.remove(dbDir);

        final File seed = new File("codegen/metamodel/src/test/data/db");
        Files.copyDirectory(seed, new File(dbDir, "db"));

        final ModelRepository auth            = new ModelRepositoryLoader(new File("projects/authorization")).build();
        final ModelRepository modelRepository = new ModelRepositoryLoader(new File("samples"), auth).build();

        generateSchema(dbDir, modelRepository.getModels(), modelRepository, Colls.emptyIterable());
    }
    @Ignore public void createSchemaGenesis()
        throws IOException
    {
        final File dbDir = new File("target/codegen/metamodel/test-output/genesis");

        final File            dir             = new File("../../genesis");
        final ModelRepository modelRepository = new ModelRepositoryLoader(dir).build();

        generateSchema(dbDir, modelRepository.getModels(), modelRepository, Colls.emptyIterable());
    }
}
