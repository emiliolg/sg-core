
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.dsl.schema;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tekgenesis.codegen.entity.DbTableCodeGenerator;
import tekgenesis.codegen.entity.UserClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.common.Predefined;
import tekgenesis.common.collections.ImmutableCollection;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.util.Files;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;

import static java.lang.String.format;

import static tekgenesis.dsl.schema.SchemaCompiler.createSchemaCompiler;
import static tekgenesis.md.MdConstants.isVersionField;

/**
 * Utility class to generate Code for a Project.
 */
public class SchemaCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final File                generatedSourcesDir;
    private final Map<String, String> include = new HashMap<>();

    private final ImmutableCollection<MetaModel> models;
    private final File                           sourcesDir;

    //~ Constructors .................................................................................................................................

    /** Creates a code generator for the project. */
    @SuppressWarnings("DuplicateStringLiteralInspection")
    public SchemaCodeGenerator(File schemaFile, File sourcesDir, File generatedSourcesDir) {
        final SchemaPackageBuilder builder = new SchemaPackageBuilder(createSchemaCompiler(schemaFile).parse(),
                new ModelRepository(),
                schemaFile.getPath(),
                schemaFile);

        this.sourcesDir = sourcesDir;
        final ImmutableList<String> strings = Files.readLines(Files.changeExtension(schemaFile, ".include"));
        for (final String includes : strings) {
            final int fieldsDeclaredIdx = includes.indexOf(':');

            final int    ends   = includes.contains("=") ? includes.indexOf("=") : includes.length();
            final String table  = fieldsDeclaredIdx == -1 ? includes : includes.substring(0, fieldsDeclaredIdx);
            final String fields = fieldsDeclaredIdx == -1 ? "" : includes.substring(fieldsDeclaredIdx + 1, ends);

            include.put(table, fields);
        }

        this.generatedSourcesDir = generatedSourcesDir;
        models                   = builder.build().getModels();
    }

    //~ Methods ......................................................................................................................................

    /** Generates the Java code.. */
    public List<File> generate() {
        return generate(false);
    }

    /** Generates the Java code. Optionally force class generation. */
    public List<File> generate(boolean forceGeneration) {
        final List<File> generated = new ArrayList<>();

        for (final Entity entity : models.filter(Entity.class)) {
            final String fields = include.get(entity.getTableName().getName());
            generated.addAll(generate(entity, fields, forceGeneration));
        }
        return generated;
    }

    private List<File> generate(Entity entity, String fields, boolean forceGeneration) {
        final String            schema      = entity.getDomain();
        final String            sourceName  = entity.getSourceName();
        final JavaCodeGenerator user        = new JavaCodeGenerator(sourcesDir, schema);
        final String            basePackage = schema + ".g";
        final JavaCodeGenerator base        = new JavaCodeGenerator(generatedSourcesDir, basePackage);

        final String[] newerFieldDef = Predefined.isEmpty(fields) ? null : fields.split(",");

        final UserClassGenerator        userGenerator        = new UserClassGenerator(user, entity, basePackage, entity.getName());
        final IxEntityBaseCodeGenerator baseGenerator        = new IxEntityBaseCodeGenerator(base, entity, newerFieldDef);
        final DbTableCodeGenerator      dbTableCodeGenerator = new DbTableCodeGenerator(base, entity) {
                @Override protected void addTableField(final TypeField f) {
                    if (!isVersionField(f.getName())) super.addTableField(f);
                }

                @Override protected String createEntityTableBody() {
                    return format("new %s.TableBase()", extractImport(entityClass));
                }
            };

        final List<File> updated = new ArrayList<>(3);

        if (userGenerator.generateIfAbsent()) updated.add(userGenerator.getTargetFile());

        if (forceGeneration) {
            baseGenerator.generate();
            dbTableCodeGenerator.generate();
            updated.add(baseGenerator.getTargetFile());
            updated.add(dbTableCodeGenerator.getTargetFile());
        }
        else {
            if (baseGenerator.generateIfOlder(new File(sourceName))) updated.add(baseGenerator.getTargetFile());
            if (dbTableCodeGenerator.generateIfOlder(new File(sourceName))) updated.add(dbTableCodeGenerator.getTargetFile());
        }

        return updated;
    }
}  // end class SchemaCodeGenerator
