
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.StrBuilder;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.logging.Logger;
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.metadata.common.ModelLinkerImpl;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.metadata.exception.BuilderErrorException;
import tekgenesis.metadata.form.UiModelRetriever;
import tekgenesis.metadata.form.dependency.TopologicalSorter.CycleDetectedPrettyException;
import tekgenesis.metadata.form.exprs.Expressions;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.parser.ExpressionCompiler;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.MetaModel;
import tekgenesis.type.ModelType;
import tekgenesis.type.Names;
import tekgenesis.type.exception.IllegalReferenceException;
import tekgenesis.type.exception.UnresolvedTypeReferenceException;

import static tekgenesis.common.Predefined.unreachable;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.metadata.exception.BuilderErrors.createError;
import static tekgenesis.metadata.exception.BuilderErrors.invalidExpression;
import static tekgenesis.mmcompiler.ast.MMToken.*;

/**
 * This class allows the building of {@link ModelRepository} based on a set of parsed AST.
 */
public class BuilderFromAST {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final List<CaseMaker> cases;

    @NotNull private final List<MetaModel> deletedModels;

    @NotNull private final BuilderErrorListener errorListener;
    @NotNull private final List<FormMaker>      forms;
    @NotNull private final List<HandlerMaker>   handlers;
    @NotNull private final ModelLinkerImpl      linker;
    @NotNull private final List<LinkMaker>      links;
    @NotNull private final List<MenuMaker>      menus;

    @NotNull private final ModelRepository repository;
    @NotNull private final List<RoleMaker> roles;
    @NotNull private final List<TaskMaker> tasks;

    @NotNull private final Map<QName, Tuple<MetaModel, MetaModelAST>> unresolvedModels;
    @NotNull private final List<ViewMaker>                            views;
    @NotNull private final List<WidgetDefMaker>                       widgets;

    //~ Constructors .................................................................................................................................

    /** Create a BuilderFromAST with an specified repository and error Listener. */
    public BuilderFromAST(@NotNull ModelRepository repository, @NotNull BuilderErrorListener errorListener) {
        this.repository    = repository;
        this.errorListener = errorListener;
        unresolvedModels   = new LinkedHashMap<>();
        widgets            = new ArrayList<>();
        forms              = new ArrayList<>();
        views              = new ArrayList<>();
        menus              = new ArrayList<>();
        roles              = new ArrayList<>();
        cases              = new ArrayList<>();
        tasks              = new ArrayList<>();
        handlers           = new ArrayList<>();
        links              = new ArrayList<>();
        deletedModels      = new ArrayList<>();
        linker             = new ModelLinkerImpl(repository);
    }

    //~ Methods ......................................................................................................................................

    /** Build the Repository for a set of ASTs. */
    public ModelRepository build(Iterable<Tuple<String, MetaModelAST>> trees) {
        forms.clear();

        for (final Tuple<String, MetaModelAST> ast : trees)
            buildEnumEntitiesAndTypes(ast.first(), ast.second());

        buildViews();

        linkModels();

        linker.setLastStage(true);

        linkModels();

        linker.setLastStage(false);

        buildFormsHandlersCasesMenusAndTasks();

        linker.setLastStage(true);

        linkModels();

        updateReferences();

        return repository;
    }

    /** Build the model for a single AST. */
    public ModelRepository build(String sourceName, MetaModelAST root) {
        return build(listOf(tuple(sourceName, root)));
    }

    void addModel(MetaModel model, MetaModelAST id) {
        if (!link(model, id)) unresolvedModels.put(model.getKey(), tuple(model, id));
        repository.add(model);
    }

    @NotNull BuilderErrorListener getErrorListener() {
        return errorListener;
    }

    @NotNull ModelRepository getRepository() {
        return repository;
    }

    /** Bind Form widgets' expressions. */
    private void bind(UiModelRetriever retriever, MetaModel metamodel) {
        if (metamodel instanceof UiModel) Expressions.bind(retriever, (UiModel) metamodel);
    }

    /** Build Enum and Entities. */
    private void buildEnumEntitiesAndTypes(String source, MetaModelAST root) {
        deletedModels.addAll(repository.deleteAll(source));

        final QContext context = retrieveQualificationContext(root);

        root.children(TYPE).forEach(t -> new TypeMaker(t, this, source, context).make());
        root.children(ENUM).forEach(t -> new EnumMaker(t, this, source, context).make());
        root.children(ENTITY).forEach(t -> new EntityMaker(t, this, source, context).make());

        root.children(VIEW).map(t -> new ViewMaker(t, this, source, context)).into(views);
        root.children(WIDGET).map(t -> new WidgetDefMaker(t, this, source, context)).into(widgets);
        root.children(FORM).map(t -> new FormMaker(t, this, source, context)).into(forms);
        root.children(CASE).map(t -> new CaseMaker(t, this, source, context)).into(cases);
        root.children(ROLE).map(t -> new RoleMaker(t, this, source, context)).into(roles);
        root.children(LINK).map(t -> new LinkMaker(t, this, source, context)).into(links);
        root.children(MENU).map(t -> new MenuMaker(t, this, source, context)).into(menus);
        root.children(TASK).map(t -> new TaskMaker(t, this, source, context)).into(tasks);
        root.children(HANDLER).map(t -> new HandlerMaker(t, this, source, context)).into(handlers);
    }

    private void buildFormsHandlersCasesMenusAndTasks() {
        widgets.forEach(Maker::make);
        forms.forEach(Maker::make);
        handlers.forEach(Maker::make);
        cases.forEach(Maker::make);
        tasks.forEach(Maker::make);
        links.forEach(Maker::make);
        menus.forEach(Maker::make);
        roles.forEach(Maker::make);
    }

    private void buildViews() {
        views.forEach(Maker::make);
    }

    private void error(MetaModelAST node, BuilderError e) {
        getErrorListener().error(node, e);
    }

    private boolean link(MetaModel model, MetaModelAST node) {
        try {
            return linker.link(model);
        }
        catch (final ExpressionCompiler.InvalidExpression e) {
            error(e.getExpressionAst(), e.getBuilderError());
        }
        catch (final BuilderErrorException e) {
            error(node, e.getBuilderError());
        }
        catch (final UnresolvedTypeReferenceException e) {
            error(node, createError(e.getMessage(), e.getReference()));
        }
        catch (final IllegalOperationException e) {
            error(node, invalidExpression(e.getErrorMessage(), node.getText()));
        }
        catch (final CycleDetectedPrettyException | IllegalReferenceException e) {
            error(node, createError(e.getMessage(), node.getText()));
        }
        return false;
    }

    private void linkModels() {
        final Iterator<Tuple<MetaModel, MetaModelAST>> iterator  = unresolvedModels.values().iterator();
        final UiModelRetriever                         retriever = key -> repository.getModel(key, UiModel.class);
        while (iterator.hasNext()) {
            final Tuple<MetaModel, MetaModelAST> t         = iterator.next();
            final MetaModel                      metaModel = t.first();
            if (link(metaModel, t.second())) {
                iterator.remove();
                bind(retriever, metaModel);
            }
        }
    }

    /** Retrieve import list. */
    private List<String> retrieveImports(MetaModelAST root) {
        final List<String> result = new ArrayList<>();
        for (final MetaModelAST child : root.children(IMPORT))
            result.add(retrieveReferenceQualifiedId(child));
        return result;
    }

    /** Retrieve text of a package node. */
    private String retrievePackageId(MetaModelAST ast) {
        final MetaModelAST packageNode = ast.getChild(0);
        return packageNode.hasType(PACKAGE) ? retrieveReferenceQualifiedId(packageNode) : "";
    }

    /** Retrieve qualification context for all metamodels in given file. */
    private QContext retrieveQualificationContext(MetaModelAST root) {
        final String       packageId = retrievePackageId(root);
        final String       schemaId  = retrieveSchemaId(root, packageId);
        final List<String> imports   = retrieveImports(root);
        return new QContext(packageId, schemaId, imports);
    }

    /** Retrieve text of a schema node or default to last package directory. */
    private String retrieveSchemaId(MetaModelAST root, String packageId) {
        String                     result = "";
        final Option<MetaModelAST> first  = root.children(SCHEMA).getFirst();
        if (first.isPresent()) {
            final MetaModelAST schemaTree = first.get();
            result = schemaTree.getChild(0).getText();
        }
        return Names.validateSchemaId(result, packageId);
    }

    /** Update References for deleted models. */
    private void updateReferences() {
        try {
            for (final MetaModel model : deletedModels) {
                final Seq<MetaModel> usages = model.getUsages();
                for (final MetaModel usage : usages) {
                    linker.link(usage);
                }
                // Check it the model has been deleted from the repository. Then deletes its usages

                if (repository.getModel(model.getKey()).isEmpty()) {
                    for (final MetaModel m : model.getReferences())
                        ((ModelType) m).removeUsage(model);
                }
            }
        }
        catch (final Exception e) {
            logger.error(e);
        }
        finally {
            // Clean
            deletedModels.clear();
        }
    }

    //~ Methods ......................................................................................................................................

    /** Returns an String based on a qualified id. */
    public static String retrieveReferenceQualifiedId(MetaModelAST t) {
        final StrBuilder result = new StrBuilder();
        retrieveReferenceQualifiedId(t, result);
        return result.toString();
    }

    private static void retrieveReferenceQualifiedId(MetaModelAST t, StrBuilder result) {
        for (final MetaModelAST node : t) {
            if (node.hasType(REFERENCE)) retrieveReferenceQualifiedId(node, result);
            else if (node.hasType(IDENTIFIER)) result.appendElement(node.getText(), ".");
            else throw unreachable();
        }
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = Logger.getLogger(BuilderFromAST.class);
}  // end class BuilderFromAST
