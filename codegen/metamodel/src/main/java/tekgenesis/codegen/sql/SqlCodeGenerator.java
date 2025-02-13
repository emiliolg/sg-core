
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.codegen.sql;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.workflow.CaseToEntity;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.MultiMap;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.util.Diff;
import tekgenesis.common.util.VersionString;
import tekgenesis.database.SchemaDefinition;
import tekgenesis.database.introspect.delta.DeltaGenerator;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.workflow.Case;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

import static java.lang.String.format;

import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Constants.GENERATED;
import static tekgenesis.common.util.Files.*;
import static tekgenesis.common.util.VersionString.VERSION_ONE;
import static tekgenesis.database.DbIntrospector.introspectSchema;
import static tekgenesis.database.SchemaDefinition.*;
import static tekgenesis.database.SqlConstants.hasGeneratedMark;

/**
 * Class to manage the Sql Code generation.
 */
public class SqlCodeGenerator {

    //~ Constructors .................................................................................................................................

    private SqlCodeGenerator() {}

    //~ Methods ......................................................................................................................................

    /**
     * Generates the sql structure for the given models in the resource directory for the given
     * source directory.
     */
    public static void generateSchema(@NotNull final File dir, @NotNull final Iterable<MetaModel> models, @NotNull final ModelRepository repository,
                                      Seq<File> resourcesDir)
        throws IOException
    {
        final MultiMap<String, MetaModel> schemaMms = collectSchemas(models, repository);

        for (final String schemaName : schemaMms.keys()) {
            try(final SchemaCreationGenerator g = new SchemaCreationGenerator(dir, schemaName)) {
                g.createSchema(schemaMms.get(schemaName));
                checkLastVersion(schemaName, g.getOutputFile(), resourcesDir);
            }
        }
    }

    private static void checkLastVersion(final String schemaName, final File sqlFile, Seq<File> resourcesDir)
        throws IOException
    {
        final TreeMap<VersionString, File> versions     = SchemaDefinition.findVersions(sqlFile);
        final ImmutableList<String>        currentLines = readLines(sqlFile);

        // No versions
        if (versions.isEmpty()) {
            copySql(sqlFile, currentLines, VERSION_ONE);
            return;
        }

        // Get last version
        final Map.Entry<VersionString, File> le              = versions.lastEntry();
        final VersionString                  lastVersion     = le.getKey();
        final File                           lastVersionFile = le.getValue();
        final ImmutableList<String>          versionLines    = readLines(lastVersionFile);

        if (hasGeneratedMark(versionLines)) {
            // Drop generated and try again
            dropGenerated(lastVersionFile);
            checkLastVersion(schemaName, sqlFile, resourcesDir);
            return;
        }

        // Compare with last version, if there are the same -> nothing to do
        if (Diff.ignoreAllSpace().diff(versionLines, currentLines).isEmpty()) return;

        // Need to create a Delta

        final DeltaGenerator dg = new DeltaGenerator(introspectSchema(schemaName, resourcesDir, lastVersionFile),
                introspectSchema(schemaName, resourcesDir, true, sqlFile));

        final VersionString newVersion = lastVersion.increment(dg.isMinor());

        final File deltaFile = new File(versionDirDelta(sqlFile.getParentFile().getParentFile(), newVersion), sqlFile.getName());
        ensureDirExists(deltaFile.getParentFile());

        try(final FileWriter writer = new FileWriter(deltaFile)) {
            dg.generate(writer);
        }
        copySql(sqlFile, currentLines, newVersion);
    }

    private static MultiMap<String, MetaModel> collectSchemas(final Iterable<MetaModel> models, final ModelRepository repository) {
        final Set<String> schemas = new HashSet<>();
        for (final MetaModel model : models) {
            final String schema = model.getSchema();
            if (!schema.isEmpty()) schemas.add(schema);
        }

        final MultiMap<String, MetaModel> schemaMms = MultiMap.createSortedMultiMap();
        for (final String schema : schemas) {
            for (final MetaModel metaModel : repository.getModelsBySchema(schema)) {
                schemaMms.put(schema, metaModel);

                /* Add case virtual entities. */
                if (metaModel instanceof Case) {
                    final Case                  caseType = (Case) metaModel;
                    final Tuple<Entity, Entity> pair     = createCaseEntities(caseType);
                    schemaMms.put(caseType.getSchema(), pair.first());
                    schemaMms.put(caseType.getSchema(), pair.second());
                }
            }
        }
        return schemaMms;
    }

    /** Create Version file as a copy of the sql file. */
    private static void copySql(final File sqlFile, final ImmutableList<String> currentLines, final VersionString version)
        throws IOException
    {
        final File dbDir = sqlFile.getParentFile().getParentFile();
        final File dir   = versionDir(dbDir, version);
        writeLines(new File(dir, sqlFile.getName()), generatedHeader().append(currentLines));

        // Copy overlay (if exists)
        final File ovlFile   = ovlFile(sqlFile);
        final File targetOvl = new File(dir, ovlFile.getName());

        if (ovlFile.exists()) copy(ovlFile, targetOvl, true);
        else remove(targetOvl);
    }

    private static Tuple<Entity, Entity> createCaseEntities(final Case caseType) {
        final DbObject type = caseType.getBoundEntity().getOrFail("Could not find entity '" + caseType.getBoundEntity().get() + "'");
        return CaseToEntity.createCaseEntities(caseType, type);
    }

    private static void dropGenerated(final File versionFile) {
        versionFile.delete();
        final File versionDir     = versionFile.getParentFile();
        final File versionBaseDir = versionDir.getParentFile();

        final File deltaDir = new File(versionBaseDir, DELTA_DIR);
        new File(deltaDir, versionFile.getName()).delete();

        if (deleteDirIfEmpty(versionDir) && deleteDirIfEmpty(deltaDir)) deleteDirIfEmpty(versionBaseDir);
    }

    private static ImmutableList<String> generatedHeader() {
        // noinspection MalformedFormatString
        return listOf(format("-- %s at %tF %<tT", GENERATED, Calendar.getInstance()), "");
    }

    private static File ovlFile(final File sqlFile) {
        return new File(sqlFile.getParentFile(), removeExtension(sqlFile) + "_ovl.sql");
    }
}  // end class SqlCodeGenerator
