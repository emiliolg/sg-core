
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import tekgenesis.codegen.common.MMCodeGenerator;
import tekgenesis.codegen.context.InterfaceTypeCodeGenerator;
import tekgenesis.codegen.entity.*;
import tekgenesis.codegen.form.*;
import tekgenesis.codegen.handler.HandlerBaseCodeGenerator;
import tekgenesis.codegen.handler.HandlerCodeGenerator;
import tekgenesis.codegen.handler.RemoteHandlerCodeGenerator;
import tekgenesis.codegen.html.HtmlFactoryCodeGenerator;
import tekgenesis.codegen.impl.java.ClassGenerator;
import tekgenesis.codegen.impl.java.JavaCodeGenerator;
import tekgenesis.codegen.impl.js.JsCodeGenerator;
import tekgenesis.codegen.js.AngularModuleCodeGenerator;
import tekgenesis.codegen.task.TaskBaseCodeGenerator;
import tekgenesis.codegen.task.TaskCodeGenerator;
import tekgenesis.codegen.task.TaskRefreshRemoteViewCodeGenerator;
import tekgenesis.codegen.type.StructTypeCodeGenerator;
import tekgenesis.codegen.type.UserStructTypeClassGenerator;
import tekgenesis.codegen.workflow.CaseBaseCodeGenerator;
import tekgenesis.codegen.workflow.WorkItemBaseCodeGenerator;
import tekgenesis.common.collections.*;
import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.util.Files;
import tekgenesis.metadata.entity.*;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.task.Task;
import tekgenesis.metadata.task.TaskType;
import tekgenesis.metadata.task.TransactionMode;
import tekgenesis.metadata.workflow.Case;
import tekgenesis.mmcompiler.ModelRepositoryLoader;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.*;
import tekgenesis.util.MMDumper;

import static tekgenesis.codegen.common.MMCodeGenConstants.*;
import static tekgenesis.codegen.sql.SqlCodeGenerator.generateSchema;
import static tekgenesis.codegen.workflow.CaseToEntity.createCaseEntities;
import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.collections.Colls.immutable;
import static tekgenesis.common.core.Constants.*;
import static tekgenesis.common.core.QName.extractName;
import static tekgenesis.common.core.Strings.capitalizeFirst;
import static tekgenesis.common.core.Strings.toCamelCase;
import static tekgenesis.common.util.Files.copyFiles;
import static tekgenesis.type.MetaModelKind.*;
import static tekgenesis.type.MetaModelKind.ENTITY;
import static tekgenesis.type.MetaModelKind.ENUM;
import static tekgenesis.type.MetaModelKind.TYPE;

/**
 * Utility class to generate Code for a Project.
 */
@SuppressWarnings({ "ClassWithTooManyMethods", "OverlyComplexClass" })
public class ProjectCodeGenerator {

    //~ Instance Fields ..............................................................................................................................

    private final List<MMCodeGenerator> baseGenerators;

    private final HashSet<DbObject> caseObjects;
    private boolean                 forceBase;

    private final List<File> generated;

    private final File generatedSourcesDir;

    private final List<String> importerTasks;

    private boolean metaModelsOnly;

    private final File mmDir;

    private final Predicate<MetaModel> MM_IN_DIRECTORY = new Predicate<MetaModel>() {
            @Override public boolean test(MetaModel mm) {
                return mm.getSourceName().startsWith(mmDir.getAbsolutePath());
            }
        };

    private final List<MetaModel> models;

    private final File                outputDir;
    private final String              project;
    private boolean                   remoteServices;
    private final ModelRepository     repository;
    private final Iterable<File>      resourcesDirs;
    private final File                sourcesDir;
    private final Set<ClassGenerator> userGenerators;
    private final List<String>        workItemTables;

    //~ Constructors .................................................................................................................................

    /**
     * Creates a code generator for the project.
     *
     * @param  project              Project name (if defined)
     * @param  repository           ModelRepository to obtain the entities.
     * @param  sourcesDir           The directory that holds the editable sourcesDir
     * @param  generatedSourcesDir  The directory that holds the generated sourcesDir
     * @param  resourcesDirs        Extra resources dirs that holds sql files from dependencies
     */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public ProjectCodeGenerator(String project, ModelRepository repository, File sourcesDir, File generatedSourcesDir, File outputDir, File mmDir,
                                Iterable<File> resourcesDirs) {
        this.project             = project;
        this.repository          = repository;
        this.sourcesDir          = sourcesDir;
        this.outputDir           = outputDir;
        this.generatedSourcesDir = generatedSourcesDir;
        models                   = new ArrayList<>();
        generated                = new ArrayList<>();
        caseObjects              = new HashSet<>();
        workItemTables           = new ArrayList<>();
        importerTasks            = new ArrayList<>();
        forceBase                = false;
        remoteServices           = false;
        this.mmDir               = mmDir;
        this.resourcesDirs       = resourcesDirs;
        baseGenerators           = new ArrayList<>();
        userGenerators           = new HashSet<>();
    }

    //~ Methods ......................................................................................................................................

    /** Generates the Java code for all entities in the given Model Repository. */
    List<File> generate()
        throws IOException
    {
        if (models.isEmpty()) models.addAll(repository.getModels());

        // Generate remote services only!
        if (remoteServices) generateRemoteServicesOnly();
        else if (metaModelsOnly) generateMetaModelsOnly();
        else {
            models.forEach(this::createGenerator);

            generateBaseClasses();

            final File htmlDir = new File(resourcesDir(sourcesDir), HTML_EXT);
            for (final File updated : HtmlFactoryCodeGenerator.generate(repository, generatedSourcesDir, htmlDir))
                generated.add(updated);

            generateUserClasses();

            generateSchema(resourcesDir(sourcesDir), models, repository, Colls.immutable(resourcesDirs));

            createServices();
            createSchemaList();
            createEntityTableList();
            createTaskList();

            createMMList(o -> true);
        }

        return generated;
    }  // end method generate

    /** Force base classes to to be generated. */
    ProjectCodeGenerator withForceBaseGeneration() {
        forceBase = true;
        return this;
    }

    /** Generates metamodel list and copy meta-models only. */
    ProjectCodeGenerator withMetaModelsOnly() {
        metaModelsOnly = true;
        return this;
    }

    /** Add the models in the specified Files to the list of models to to be generated. */
    void withModels(Iterable<String> sources) {
        for (final String source : sources)
            models.addAll(repository.getModelsByFile(source));
    }

    /** Remote services only generation. */
    ProjectCodeGenerator withRemoteServicesOnlyGeneration() {
        remoteServices = true;
        return this;
    }

    private void addCaseEntities(final Case c) {
        final Option<DbObject> mm = c.getBoundEntity();

        if (mm.isPresent()) {
            final Tuple<Entity, Entity> es = createCaseEntities(c, mm.get());

            final Entity caseEntity = es.first();
            addCaseTable(caseEntity);

            final Entity workItemEntity = es.second();
            addCaseTable(workItemEntity);
            addWorkItem(workItemEntity);
        }
    }

    private void addCaseTable(DbObject e) {
        caseObjects.add(e);
    }

    @SuppressWarnings("UnusedReturnValue")  // Generator
    private boolean addWorkItem(Entity entity) {
        return workItemTables.add(entity.getFullName());
    }

    private void collectReferences(@NotNull Set<MetaModel> references, @NotNull MetaModel model) {
        for (final MetaModel reference : model.getReferences()) {
            if (reference.getMetaModelKind() == TYPE || reference.getMetaModelKind() == ENUM) {
                if (references.add(reference)) collectReferences(references, reference);
            }
        }
    }

    private void copyMetamodelLocalization(File resourcesDir, MetaModel metaModel)
        throws IOException
    {
        final String                domainDir  = metaModel.getDomain().replace(".", "/");
        final ImmutableList<String> properties = Files.list(new File(resourcesDir, domainDir), ".*" + metaModel.getName() + ".*properties");
        if (!properties.isEmpty())  // noinspection ResultOfMethodCallIgnored
            new File(outputDir, domainDir).mkdirs();
        for (final String propertyFile : properties)
            Files.copy(new File(propertyFile), new File(outputDir, new File(domainDir, new File(propertyFile).getName()).getPath()), true);
    }

    private void createCaseGenerators(Case c, JavaCodeGenerator user, String domain) {
        final Option<DbObject> mm = c.getBoundEntity();

        if (mm.isPresent()) {
            final Tuple<Entity, Entity> es             = createCaseEntities(c, mm.get());
            final JavaCodeGenerator     base           = getBaseGenerator(domain);
            final Entity                caseEntity     = es.first();
            final Entity                workItemEntity = es.second();
            final DbObject              payload        = c.getBoundEntity().getOrFail("Undefined Payload");

            userGenerators.add(new UserClassGenerator(user, caseEntity, getBasePackage(domain), caseEntity.getName()));
            baseGenerators.add(new CaseBaseCodeGenerator(base, c, caseEntity, workItemEntity, payload));
            baseGenerators.add(new DbTableCodeGenerator(base, caseEntity));
            addCaseTable(caseEntity);

            userGenerators.add(new UserClassGenerator(user, workItemEntity, getBasePackage(domain), workItemEntity.getName()));
            baseGenerators.add(new WorkItemBaseCodeGenerator(base, caseEntity, workItemEntity, payload));
            baseGenerators.add(new DbTableCodeGenerator(base, workItemEntity));
            addCaseTable(workItemEntity);
            addWorkItem(workItemEntity);
        }
    }

    private void createDatabaseObjectGenerators(DbObject model, JavaCodeGenerator user, final String domain) {
        final JavaCodeGenerator generatedBase = getBaseGenerator(domain);
        userGenerators.add(new UserClassGenerator(user, model, getBasePackage(domain), model.getName()));
        final EntityBaseCodeGenerator e = new EntityBaseCodeGenerator(generatedBase, model, model.getName());
        baseGenerators.add(e);

        if (model.splitMutator()) {
            final String             className = model.getName() + FOR_UPDATE_SUFFIX;
            final UserClassGenerator e1        = new UserClassGenerator(user, model, getBasePackage(domain), className);
            userGenerators.add(e1);
            baseGenerators.add(new EntityBaseForUpdateCodeGenerator(e, generatedBase, model, className));
        }
        baseGenerators.add(new DbTableCodeGenerator(generatedBase, model));

        if (model.isSearchableByFields()) {
            final EntitySearcherBaseCodeGenerator generatedSearcher = new EntitySearcherBaseCodeGenerator(generatedBase, model);
            baseGenerators.add(generatedSearcher);
            userGenerators.add(new EntitySearcherCodeGenerator(user, model, generatedSearcher.getFullName()));
        }
    }

    /** Create the {@link Constants#ENTITY_LIST_FILE } file. */
    private void createEntityTableList()
        throws IOException
    {
        final File f = new File(outputDir, ENTITY_LIST_FILE);
        Files.ensureDirExists(f.getParentFile());

        final Writer writer = new FileWriter(f);

        for (final DbObject e : filter(repository.getModels(), DbObject.class).filter(this::isInMmDir).topologicalSort(this::dependenciesFor))

            writer.write(e.getFullName() + ' ' + e.getTableName() + "\n");

        for (final Case aCase : immutable(repository.getModels().filter(Case.class)).filter(databaseObject ->
                    databaseObject.getSourceName()
                                  .startsWith(mmDir.getAbsolutePath())))
            addCaseEntities(aCase);

        for (final DbObject caseObject : caseObjects)
            writer.write(caseObject.getFullName() + ' ' + caseObject.getTableName() + "\n");
        Files.close(writer);
    }

    private void createEnumGenerators(EnumType model, final String domain) {
        final JavaCodeGenerator codeGenerator = createJavaCodeGenerator(domain);
        if (model.isException()) baseGenerators.add(new ExceptionEnumCodeGenerator(codeGenerator, model));

        baseGenerators.add(new EnumCodeGenerator(codeGenerator, model, metaModelsOnly || remoteServices));
    }

    private void createFormGenerators(Form f, JavaCodeGenerator user, String domain) {
        final JavaCodeGenerator cg = createJavaCodeGenerator(domain);

        if (metaModelsOnly && f.isRemote()) {
            baseGenerators.add(new ExternalFormCodeGenerator(cg, f, project, repository));
            return;
        }

        final FormBaseCodeGenerator g = new FormBaseCodeGenerator(cg, f, repository.getModel(f.getBinding()), repository);
        userGenerators.add(new FormCodeGenerator(user, f, g));

        if (isNotEmpty(f.getHandlerClass())) {
            final String                       handler = capitalizeFirst(extractName(f.getHandlerClass()));
            final FormHandlerBaseCodeGenerator hbg     = new FormHandlerBaseCodeGenerator(cg, handler, f, BASE);
            userGenerators.add(new FormHandlerCodeGenerator(user, handler, f, hbg));
            baseGenerators.add(hbg);
        }

        baseGenerators.add(g);
    }

    private void createGenerator(MetaModel model) {
        final String domain = model.getDomain();

        final JavaCodeGenerator user = new JavaCodeGenerator(sourcesDir, domain);

        switch (model.getMetaModelKind()) {
        case ENTITY:
        case VIEW:
            createDatabaseObjectGenerators((DbObject) model, user, domain);

            if (model.getMetaModelKind() == VIEW && model.hasModifier(Modifier.REMOTE))
                createRemoteViewTaskCodeGenerator((DbObject) model, user, domain);

            break;

        case ENUM:
            createEnumGenerators((EnumType) model, domain);
            break;
        case WIDGET:
            createWidgetDefGenerators((WidgetDef) model, user, domain);
            break;
        case FORM:
            createFormGenerators((Form) model, user, domain);
            break;
        case HANDLER:
            createHandlerGenerators((Handler) model, user, domain);
            break;
        case TYPE:
            createTypeGenerator((TypeDef) model, user, domain);
            break;
        case TASK:
            final Task task = (Task) model;
            if (task.getType() == TaskType.IMPORTER) importerTasks.add(task.getFullName());
            createTaskGenerators(task, user, domain);
            break;
        case CASE:
            createCaseGenerators((Case) model, user, domain);
            break;
        default:
            // Generate nothing
        }
    }  // end method createGenerator

    private void createHandlerGenerators(Handler h, JavaCodeGenerator user, String domain) {
        final JavaCodeGenerator cg = createJavaCodeGenerator(domain);
        if (h.isRemote() && remoteServices) baseGenerators.add(new RemoteHandlerCodeGenerator(cg, h));
        else {
            final HandlerBaseCodeGenerator g = new HandlerBaseCodeGenerator(cg, h);
            userGenerators.add(new HandlerCodeGenerator(user, h, g));
            baseGenerators.add(g);
        }
    }  // end method createHandlerGenerators

    private JavaCodeGenerator createJavaCodeGenerator(final String domain) {
        return new JavaCodeGenerator(generatedSourcesDir, domain);
    }

    private JsCodeGenerator createJsCodeGenerator(final String domain) {
        outputDir.mkdirs();
        final File jsServices = new File(outputDir, "../../js/" + outputDir.getName());
        return new JsCodeGenerator(jsServices, domain);
    }

    /** Create the {@link ModelRepositoryLoader#META_MODEL_LIST } file. */
    private void createMetaModelList(Collection<String> mmFiles) {
        final File f = new File(outputDir, ModelRepositoryLoader.META_MODEL_LIST);
        Files.writeLines(f, mmFiles);
    }

    private void createMMList(@NotNull Predicate<MetaModel> filter)
        throws IOException
    {
        final Seq<MetaModel> metaModels = repository.getModels().filter(MM_IN_DIRECTORY).filter(filter);

        final MultiMap<File, String> mms = MultiMap.createSortedMultiMap();

        for (final MetaModel model : metaModels) {
            final String d   = new File(model.getSourceName()).getParent();
            final int    pos = d.length() - model.getDomain().length();
            mms.put(new File(d.substring(0, pos)), model.getSourceName().substring(pos));
        }

        final List<String> list = new ArrayList<>();
        mms.values().forEach(list::addAll);
        createMetaModelList(list);

        for (final File dir : mms.keys()) {
            if (metaModelsOnly) dumpMetaModels(dir, mms.get(dir));
            else copyFiles(dir, mms.get(dir), outputDir, false);
        }
    }

    private void createRemoteViewTaskCodeGenerator(DbObject model, JavaCodeGenerator user, String domain) {
        final String                             name      = capitalizeFirst(toCamelCase(domain.replace(".", "_"))) + "RefreshRemoteViewTask";
        final TaskRefreshRemoteViewCodeGenerator generator = new TaskRefreshRemoteViewCodeGenerator(createJavaCodeGenerator(domain),
                (View) model,
                name);
        baseGenerators.add(generator);

        final Task dummyModel = new Task(model.getSourceName(),
                domain,
                name,
                TaskType.RUNNABLE,
                name,
                "0 0 0/1",
                null,
                TransactionMode.NONE,
                0,
                null,
                Modifier.emptySet(),
                null);

        repository.add(dummyModel);
    }

    private void createSchemaList() {
        final MultiMap<String, String> schemas = MultiMap.createSortedMultiMap();

        for (final MetaModel model : repository.getModels().filter(MM_IN_DIRECTORY)) {
            final String schema = model.getSchema();
            if (!schema.isEmpty()) {
                schemas.put(schema);
                for (final MetaModel metaModel : model.getReferences()) {
                    final String s = schemaFor(metaModel);
                    if (!s.isEmpty() && !s.equals(schema)) schemas.put(schema, s);
                }
            }
        }
        final List<String> lines = new ArrayList<>();
        for (final String schema : schemas.keys())
            lines.add(schema + " " + schemas.get(schema).mkString(","));
        Files.writeLines(new File(outputDir, Constants.SCHEMA_LIST_FILE), lines);
    }

    private void createServiceModule(Handler handler) {
        if (handler.isRemote()) {
            final JsCodeGenerator            cg     = createJsCodeGenerator(handler.getDomain());
            final AngularModuleCodeGenerator module = new AngularModuleCodeGenerator(cg, handler);
            baseGenerators.add(module);
        }
    }

    /** Create the Services files. */
    private void createServices() {
        final File servicesDir = new File(outputDir, META_INF_SERVICES);

        // Work Item Tables
        Files.writeLines(new File(servicesDir, WORK_ITEM_TABLE), workItemTables);
        // Importer Tasks
        Files.writeLines(new File(servicesDir, IMPORTER), importerTasks);
        // Todo Domains
    }

    private void createTaskGenerators(Task task, JavaCodeGenerator user, final String domain) {
        final JavaCodeGenerator     cg = createJavaCodeGenerator(domain);
        final TaskBaseCodeGenerator g  = new TaskBaseCodeGenerator(cg, task, BASE);
        userGenerators.add(new TaskCodeGenerator(user, task, BASE));
        baseGenerators.add(g);
    }
    /** Create the {@link Constants#TASK_LIST_FILE } file. */
    private void createTaskList()
        throws IOException
    {
        final File f = new File(outputDir, TASK_LIST_FILE);
        Files.ensureDirExists(f.getParentFile());

        final Writer writer = new FileWriter(f);
        for (final Task e : filter(repository.getModels(), Task.class).filter(this::isInMmDir))
            writer.write(e.getFullName() + ' ' + e.getType() + "\n");
        Files.close(writer);
    }

    private void createTypeGenerator(TypeDef model, JavaCodeGenerator user, String domain) {
        if (model instanceof StructType) {
            final StructType        type = (StructType) model;
            final JavaCodeGenerator cg   = createJavaCodeGenerator(domain);

            if (type.hasModifier(Modifier.INTERFACE)) baseGenerators.add(new InterfaceTypeCodeGenerator(cg, type));
            else if (type.hasModifier(Modifier.FINAL) || remoteServices) baseGenerators.add(new StructTypeCodeGenerator(cg, type, true));
            else {
                baseGenerators.add(new StructTypeCodeGenerator(getBaseGenerator(domain), type));
                userGenerators.add(new UserStructTypeClassGenerator(user, type, getBasePackage(domain)).asSerializable());
            }
        }
    }

    private void createWidgetDefGenerators(WidgetDef w, JavaCodeGenerator user, String domain) {
        final JavaCodeGenerator cg = createJavaCodeGenerator(domain);

        final WidgetDefBaseCodeGenerator g = new WidgetDefBaseCodeGenerator(cg, w, repository.getModel(w.getBinding()), repository);
        userGenerators.add(new WidgetDefCodeGenerator(user, w, g));

        baseGenerators.add(g);
    }

    @NotNull private Iterable<DbObject> dependenciesFor(final DbObject dbObject) {
        final ArrayList<DbObject> result = new ArrayList<>();
        for (final Attribute attribute : dbObject.allAttributes()) {
            for (final DbObject dbo : attribute.asDatabaseObject()) {
                if (!attribute.isMultiple()) result.add(dbo);
            }
        }
        return result;
    }

    private void dumpMetaModels(File sourceDir, ImmutableCollection<String> mmFiles)
        throws IOException
    {
        for (final String mmFile : mmFiles) {
            final String sourceName = new File(sourceDir, mmFile).getAbsolutePath();

            final Set<MetaModel> metaModels = repository.getModelsByFile(sourceName)  //
                                              .filter(ProjectCodeGenerator::validModel)  //
                                              .topologicalSort(mm ->
                        mm.getReferences().filter(metaModel -> metaModel != null && metaModel.getSourceName().equals(sourceName)));

            if (!metaModels.isEmpty()) {
                final MMDumper dumper = MMDumper.createDumper(project, repository).withPackage().printRemoteAsExternal();
                final File     file   = new File(outputDir, mmFile);
                // noinspection ResultOfMethodCallIgnored
                file.getParentFile().mkdirs();
                final File resourcesDir = new File(sourceDir, "../resources");
                for (final MetaModel metaModel : metaModels) {
                    dumper.model(metaModel);
                    if (metaModel.getMetaModelKind() == FORM || metaModel.getMetaModelKind() == ENUM)
                        copyMetamodelLocalization(resourcesDir, metaModel);
                }
                final FileWriter output = new FileWriter(file);
                dumper.toWriter(output);
                output.close();
            }
        }
    }

    private void generateBaseClasses() {
        for (final MMCodeGenerator generator : baseGenerators) {
            final File source = new File(generator.getSourceName());
            if (forceBase) generator.generate();
            if (forceBase || generator.generateIfOlder(source)) generated.add(generator.getTargetFile());
        }
    }

    private void generateMetaModelsOnly()
        throws IOException
    {
        for (final MetaModel model : repository.getModels().filter(this::isInMmDir)) {
            if ((model.getMetaModelKind() == FORM && ((Form) model).isRemote()) || model.getMetaModelKind() == ENUM) createGenerator(model);
        }
        generateBaseClasses();

        createMMList(ProjectCodeGenerator::validModel);
    }

    /** Generates the Java code for remote services in the given Model Repository. */
    private List<File> generateRemoteServicesOnly() {
        final Set<Handler>   handlers   = new HashSet<>();
        final Set<MetaModel> references = new HashSet<>();

        for (final MetaModel model : models) {
            if (model.getMetaModelKind() == HANDLER) {
                final Handler handler = (Handler) model;
                if (handler.isRemote()) {
                    handlers.add(handler);
                    collectReferences(references, handler);
                }
            }
        }

        handlers.forEach(this::createGenerator);
        references.forEach(this::createGenerator);

        handlers.forEach(this::createServiceModule);
        // Create client side AngularJs module services

        generateBaseClasses();
        generateUserClasses();

        return generated;
    }

    private void generateUserClasses() {
        for (final ClassGenerator generator : userGenerators)
            if (generator.generateIfAbsent()) generated.add(generator.getTargetFile());
    }

    private JavaCodeGenerator getBaseGenerator(final String domain) {
        return new JavaCodeGenerator(generatedSourcesDir, getBasePackage(domain));
    }

    private String getBasePackage(final String domain) {
        return domain + ".g";
    }

    private boolean isInMmDir(final MetaModel mm) {
        assert mm != null;
        return !(mm instanceof Case) && mm.getSourceName().startsWith(mmDir.getAbsolutePath());
    }

    //~ Methods ......................................................................................................................................

    /** Return schema for meta model. */
    public static String schemaFor(MetaModel m) {
        String schema = m.getSchema();
        if (schema.isEmpty())
            if (m instanceof SimpleType) {
                final Type t = ((SimpleType) m).getFinalType();
                if (t instanceof MetaModel) schema = ((MetaModel) t).getSchema();
            }
        return schema;
    }

    /** Resources directory (relative to sources one). */
    private static File resourcesDir(File sourcesDir) {
        return new File(sourcesDir.getParent(), Constants.RESOURCES);
    }

    private static boolean validModel(final MetaModel metaModel) {
        if (metaModel == null) return false;
        final MetaModelKind mm = metaModel.getMetaModelKind();
        return isRemoteForm(metaModel, mm) || mm == ENUM || mm == ENTITY || mm == TYPE || metaModel instanceof SimpleType ||
               (mm == VIEW && !metaModel.hasModifier(Modifier.REMOTE));
    }

    private static boolean isRemoteForm(MetaModel model, MetaModelKind mm) {
        return mm == FORM && ((Form) model).isRemote();
    }
}  // end class ProjectCodeGenerator
