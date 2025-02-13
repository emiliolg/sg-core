
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Binder;
import tekgenesis.code.Evaluator;
import tekgenesis.code.SymbolNotFoundException;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Mutable;
import tekgenesis.common.core.Option;
import tekgenesis.expr.Expression;
import tekgenesis.expr.RefTypeSolver;
import tekgenesis.expr.SimpleReferenceSolver;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.MetaModelReference;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.*;
import tekgenesis.metadata.exception.*;
import tekgenesis.metadata.form.dependency.PrecedenceAnalyzer;
import tekgenesis.metadata.form.ref.UiModelRefTypeSolver;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.metadata.handler.Handler;
import tekgenesis.metadata.handler.Parameter;
import tekgenesis.metadata.handler.Route;
import tekgenesis.metadata.link.Assignment;
import tekgenesis.metadata.link.Link;
import tekgenesis.metadata.menu.Menu;
import tekgenesis.metadata.role.Role;
import tekgenesis.metadata.workflow.Case;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.*;
import tekgenesis.type.exception.UnresolvedRemoteViewException;
import tekgenesis.type.exception.UnresolvedTypeReferenceException;

import static tekgenesis.common.Predefined.isNotEmpty;
import static tekgenesis.common.collections.Colls.filter;
import static tekgenesis.common.core.Constants.PERMISSION_ALL;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.field.FieldOption.ABSTRACT;
import static tekgenesis.field.FieldOption.LABEL;
import static tekgenesis.field.FieldOptionType.METAMODEL_REFERENCE_T;
import static tekgenesis.metadata.exception.IllegalDatabaseSearchableFieldType.illegalEntity;
import static tekgenesis.metadata.exception.InvalidTypeForStructFieldException.invalidTypeForStructFieldException;
import static tekgenesis.metadata.exception.InvalidTypeUsageInRouteException.invalidTypeUsageInRouteException;
import static tekgenesis.type.MetaModelKind.*;
import static tekgenesis.type.Modifier.LOCAL;
import static tekgenesis.type.Types.arrayType;
import static tekgenesis.type.UnresolvedReference.*;
import static tekgenesis.type.permission.PredefinedPermission.isPredefined;

/**
 * An Implementation of a {@link ModelLinker}.
 */
@SuppressWarnings({ "OverlyComplexClass", "ClassWithTooManyMethods" })
public class ModelLinkerImpl implements ModelLinker {

    //~ Instance Fields ..............................................................................................................................

    private boolean               lastStage;
    private final ModelRepository repository;

    //~ Constructors .................................................................................................................................

    /** Create a Linker. */
    public ModelLinkerImpl(ModelRepository repository) {
        this.repository = repository;
        lastStage       = false;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean link(MetaModel model) {
        if (model instanceof ModelType) return linkComposite((ModelType) model);
        if (model instanceof Link) return linkLink((Link) model);
        if (model instanceof Menu) return linkMenu((Menu) model);
        if (model instanceof Role) return linkRole((Role) model);
        if (model instanceof Handler) return linkHandler((Handler) model);
        if (model instanceof Form) return linkForm((Form) model);
        if (model instanceof WidgetDef) return linkWidgetDef((WidgetDef) model);
        if (model instanceof Case) return linkCase((Case) model);
        //
        return true;
    }

    @Override public ModelLinker setLastStage(boolean b) {
        lastStage = b;
        return this;
    }

    /** Reports an error when the reference cannot be solved. */
    protected void unresolved(String fqn) {
        throw new UnresolvedTypeReferenceException(fqn);
    }

    private void checkLocalTypeUsage(Type type, Route route, Handler handler) {
        if (!lastStage && type instanceof StructType && ((StructType) type).hasModifier(LOCAL))
            throw invalidTypeUsageInRouteException(((StructType) type).getName(), route.getName(), handler.getName());
    }

    private boolean compileAndBind(Type fieldType, Expression expression) {
        final SolverAndBinder solver = resolveSolverAndBinderForEnums(fieldType);
        try {
            expression.compile(solver).bind(solver);
        }
        catch (final SymbolNotFoundException ignored) {
            return false;
        }
        return expression.isConstant() || fieldType.isEnum();
    }

    private void compileConstantValueExpression(String model, String name, Type type, Expression expression, Evaluator ev) {
        if (compileAndBind(type, expression)) {
            final Object value = expression.evaluate(ev, null);
            if (type.isEnum()) return;
            if (type.isTime() && value instanceof Long) return;
            final Type expressionType = Types.typeOf(value);
            if (!type.equivalent(expressionType) && (!type.isNumber() || !expressionType.isNumber()))
                throw new InvalidTypeForAssignmentException(name, type, expressionType, model);
        }
        else throw new ParameterNotAConstantException(expression.toString(), model);
    }

    private boolean compileWidgetExpressions(UiModel model, Widget widget) {
        final FieldOptions options = widget.getOptions();
        options.compile(new UiModelRefTypeSolver(model, repository));
        return true;
    }  // end method compileWidgetExpressions

    private void fillViewAttribute(final ViewAttribute viewAttribute, @NotNull final Attribute attribute) {
        viewAttribute.getOptions().put(FieldOption.OPTIONAL, attribute.getOptions().getExpression(FieldOption.OPTIONAL));
        if (attribute.isSigned()) viewAttribute.getOptions().put(FieldOption.SIGNED, true);
        if (attribute.isMultiple()) viewAttribute.multiple();
        if (viewAttribute.getType() instanceof View && isNotEmpty(attribute.getReverseReference()) &&
            ((View) viewAttribute.getType()).getAttributeByBaseAttributeName(attribute.getReverseReference()).isPresent())
            viewAttribute.setReverseReference(
                ((View) viewAttribute.getType()).getAttributeByBaseAttributeName(attribute.getReverseReference()).get().getName());

        if (attribute.isInner()) viewAttribute.inner();
    }

    private ModelField findFormWidget(Form form, FieldReference ref) {
        final Widget widget = form.getWidget(ref.getName()).getOrNull();
        return widget == null ? ref : widget;
    }

    @NotNull private ModelField findSuperAttribute(@Nullable DbObject dbObject, @NotNull FieldReference ref) {
        if (dbObject == null) return ref;
        final Attribute attribute = dbObject.getAttribute(ref.getName()).getOrNull();
        return attribute == null ? ref : attribute;
    }

    private boolean linkCase(final Case model) {
        return lastStage && solveChildrenOptionsReferences(model);
    }

    private boolean linkComposite(ModelType model) {
        final RefTypeSolver   solver = new ModelTypeRefSolver(model);
        final Mutable.Boolean result = new Mutable.Boolean(true);

        if (model instanceof Entity) result.setValue(solveEntityReferences((Entity) model));

        if (model instanceof View) return linkView((View) model);

        if (model instanceof DbObject) ((DbObject) model).compileSearchByOptions(solver);

        for (final ModelField field : model.getChildren()) {
            if (!solveReference(model, field)) result.setValue(false);
            else field.getOptions().compile(solver, true);
        }

        if (model instanceof StructType && !model.hasModifier(LOCAL)) model.getChildren()
            .filter(f -> !lastStage && f.getType().isEntity())
            .forEach(field -> {  //
                throw invalidTypeForStructFieldException(field.getName(), field.getType(), model.getName());
                /*Logger.getLogger(ModelLinkerImpl.class)
                 *    .warning("Type " + model.getName() + " doesn't allow fields of type Entity like " + field.getName());});*/
            });

        if (model instanceof EnumType) resolveValues((EnumType) model);

        if (model instanceof DbObject) ((DbObject) model).validateSearchableDatabaseTypes(searchField -> {
            if (!searchField.isValidTypeForDb()) {
                if (lastStage) throw illegalEntity(searchField.getFieldName(), searchField.getKind().getText(), model.getName());
                else result.setValue(false);
            }
        });

        return result.value();
    }

    private boolean linkForm(final Form form) {
        if (!lastStage) return false;

        final boolean result = solveAndCompileDescendants(form);

        form.setPrecedenceData(new PrecedenceAnalyzer(form).analyze());

        form.solve(field -> {
            if (field instanceof FieldReference) {
                final FieldReference ref    = (FieldReference) field;
                final ModelField     widget = findFormWidget(form, ref);
                if (lastStage || widget != field) ref.solve(widget);
                return widget;
            }
            return field;
        });

        return result;
    }

    private boolean linkHandler(Handler handler) {
        final Evaluator evaluator = new Evaluator();
        for (final Route route : handler.getChildren()) {
            // Solve route type reference
            if (!solveReference(handler, new MultipleTyped(route))) return false;
            // Solve route body reference
            if (!solveReference(handler, new MultipleTyped(new RouteBody(route)))) return false;
            // If body type is a local type, explode
            checkLocalTypeUsage(route.getType(), route, handler);
        }

        return solveReferencesInRoute(handler, evaluator);
    }

    private boolean linkLink(Link model) {
        final Evaluator ev     = new Evaluator();
        boolean         result = true;

        if (model.hasForm()) result = resolveAndCompileFormLink(model.getName(), ev, model.getForm(), model.getAssignments());

        return result;
    }

    private boolean linkMenu(Menu model) {
        final boolean result = model.getChildren().forAll(item -> {
                final FieldOptions options = item.getOptions();
                final boolean      solved  = solveOptionsReferences(options);
                if (solved) options.put(LABEL, item.getModel().getLabel());
                return solved;
            });

        if (result && lastStage) model.getChildren().forEach(item -> {       //
            final MetaModelKind k = item.getModel().getMetaModelKind();      //
            if (k != MENU && k != FORM && k != LINK) throw new UnexpectedMetaModelKindException(k, item.getModel().getFullName(), model.getName());
        });

        return result;
    }  // end method linkMenu

    private boolean linkModel(MetaModel model) {
        boolean result = true;
        for (final ModelField field : model.getChildren()) {
            if (!solveReference(model, field)) result = false;
        }
        return result;
    }

    private boolean linkRole(Role role) {
        boolean result = solveChildrenOptionsReferences(role);

        //J-
        if (result) {
            result = role.getChildren().forAll(p -> {
                final MetaModel model = p.getMetaModel();

                if (!(model instanceof Form))
                    throw new UnexpectedMetaModelKindException(model.getMetaModelKind(), model.getFullName(), role.getName());

                final String permission = p.getPermission();
                if (isPredefined(permission) || PERMISSION_ALL.equals(permission)) return true;

                if (!((Form) model).hasPermission(permission)) {
                    throw new PermissionNotDeclaredException(permission, model.getFullName());
                }

                return true;
            });
        }
        //J+

        return result;
    }

    private boolean linkView(View view) {
        final RefTypeSolver solver = new ModelTypeRefSolver(view);

        boolean result = solveViewReferences(view) && lastStage;

        if (result) {
            for (final ModelField field : view.getChildren()) {
                result = solveViewReference(view, field);
                if (result) field.getOptions().compile(solver);
            }
        }

        return result;
    }

    private boolean linkWidgetDef(final WidgetDef def) {
        if (!lastStage) return false;

        final boolean result = solveAndCompileDescendants(def);

        def.setPrecedenceData(new PrecedenceAnalyzer(def).analyze());

        /*form.solve(field -> {
         *  if (field instanceof FieldReference) {
         *      final FieldReference ref    = (FieldReference) field;
         *      final ModelField     widget = findFormWidget(form, ref);
         *      if (lastStage || widget != field) ref.solve(widget);
         *      return widget;
         *  }
         *  return field;
         *});*/

        return result;
    }

    @NotNull private Function<ModelField, ModelField> objectFieldReferencesSolver(@NotNull DbObject dbObject, List<ModelField> unresolved) {
        return value -> {
                   if (value instanceof FieldReference) {
                       final FieldReference ref       = (FieldReference) value;
                       ModelField           attribute = findSuperAttribute(dbObject, ref);
                       if (ref == attribute && dbObject instanceof View)
                           attribute = findSuperAttribute(((View) dbObject).getBaseEntity().getOrNull(), ref);
                       if (lastStage || attribute != value) ref.solve(attribute);
                       if (!(attribute instanceof Attribute)) unresolved.add(attribute);
                       return attribute;
                   }
                   return value;
               };
    }

    private boolean replaceViewEntityReferences(View view, ViewAttribute viewAttribute, FieldReference reference,
                                                @Nullable final Attribute attribute) {
        if (attribute == null) return false;

        final Type finalType = attribute.getFinalType();
        if ((finalType instanceof UnresolvedTypeReference || finalType.isEntity()) && view.isRemote()) {
            final Type             type       = solveType(attribute);
            final Option<DbObject> baseEntity = view.getBaseEntity();
            final View             viewType;
            if (type.getImplementationClassName().equals(baseEntity.get().getImplementationClassName())) viewType = view;
            else viewType = repository.getModels().filter(View.class).getFirst(v ->
                            v != null && v.isRemote() && v.getBaseEntityModelType() != null &&
                            v.getBaseEntityModelType().getFullName().equals(type.getImplementationClassName())).getOrNull();

            if (viewType != null) {
                viewAttribute.setType(viewType);
                fillViewAttribute(viewAttribute, attribute);
                return true;
            }

            final Type model = repository.getModel(createQName(type.getImplementationClassName())).castTo(Type.class).getOrNull();
            if (model == null || model.isDatabaseObject())
                throw new UnresolvedRemoteViewException(type.getImplementationClassName(), reference.getName());
            viewAttribute.setType(model.getFinalType());
        }
        fillViewAttribute(viewAttribute, attribute);

        return false;
    }  // end method replaceViewEntityReferences

    private boolean resolveAndCompileFormLink(String link, Evaluator ev, @NotNull final Form form, Seq<Assignment> assignments) {
        final List<ModelField> unresolved = new ArrayList<>();

        for (final Assignment assignment : assignments)
            assignment.solve(field -> {
                if (field instanceof FieldReference) {
                    final FieldReference ref    = (FieldReference) field;
                    final ModelField     widget = findFormWidget(form, ref);
                    if (lastStage || widget != field) ref.solve(widget);
                    if (widget == field) unresolved.add(field);
                    return widget;
                }
                return field;
            });

        if (!unresolved.isEmpty()) return false;

        for (final Assignment assignment : assignments) {
            final ModelField field = assignment.getField();
            if (field instanceof Widget) {
                if (!form.containsParameter(field)) {
                    if (lastStage) throw new ParameterNotSpecifiedException(field.getName(), form.getName(), link);
                    return false;
                }
                final Object e = assignment.getValue();
                if (e instanceof Expression) compileConstantValueExpression(link, field.getName(), field.getType(), (Expression) e, ev);
            }
        }

        return true;
    }

    private SolverAndBinder resolveSolverAndBinderForEnums(final Type type) {
        if (type.isEnum()) {
            final EnumType enumType = repository.getModel(type.getImplementationClassName(), EnumType.class).get();
            return new EnumSolverAndBinder(enumType);
        }
        return EMPTY;
    }

    // Compile and Evaluate Enum Values
    private void resolveValues(EnumType type) {
        final Evaluator ev = new Evaluator();
        for (final EnumValue enumValue : type.getValues()) {
            final Object[] values = enumValue.getValues();
            for (int i = 0; i < values.length; i++) {
                final Object value = values[i];
                if (value instanceof Expression) values[i] = ((Expression) value).compile(EMPTY).evaluate(ev, null);
            }
        }
    }

    private boolean solveAndCompileDescendants(UiModel model) {
        boolean result = true;
        for (final Widget widget : model.getDescendants()) {
            result &= solveOptionsReferences(widget.getOptions());
            result &= compileWidgetExpressions(model, widget);
        }
        return result;
    }

    private boolean solveChildrenOptionsReferences(MetaModel model) {
        return model.getChildren().map(ModelField::getOptions).forAll(this::solveOptionsReferences);
    }

    private boolean solveEntityReference(Attribute attr, Type type, UnresolvedTypeReference reference) {
        if (type instanceof Entity && !attr.hasOption(ABSTRACT)) {
            final Entity entity = (Entity) type;
            if (attr.isInner()) entity.setParent(some((Entity) attr.getDbObject()));

            final int ret = solveReverseReference(entity, attr);
            if (ret != OK) {
                if (lastStage) reference.reverseError(attr.getFullName(), ret);
                return false;
            }
        }
        // else if (attr.isMultiple()) throw new InvalidMultipleTypeException(attr.getName());
        return true;
    }

    private boolean solveEntityReferences(@NotNull final Entity entity) {
        final List<ModelField> unresolved = new ArrayList<>();
        entity.solveReferences(objectFieldReferencesSolver(entity, unresolved));
        return unresolved.isEmpty();
    }

    private boolean solveMetaModelReference(MetaModelReference reference) {
        return repository.getModel(createQName(reference.getFullName())).map(m -> {  //
                return reference.solve(m) != null;
            }).orElseGet(() -> {
            if (lastStage) reference.error();
            return false;
        });
    }

    private boolean solveOptionsReferences(FieldOptions options) {
        return filter(options, o -> o.getType() == METAMODEL_REFERENCE_T).map(options::getMetaModelReference).forAll(this::solveMetaModelReference);
    }

    private boolean solveReference(MetaModel model, Typed typed) {
        if (!(typed instanceof ViewAttribute && model instanceof View)) {
            final Type t = typed.getType();
            return !(t instanceof UnresolvedTypeReference) || solveReference(model, typed, (UnresolvedTypeReference) t);
        }
        return true;
    }  // end method solveReference

    private boolean solveReference(MetaModel model, Typed typed, UnresolvedTypeReference reference) {
        final ModelType modelType = repository.getModel(reference.getKey(), ModelType.class).orElse(reference);
        if (lastStage || modelType != reference) reference.solve(modelType);

        if (modelType instanceof UnresolvedTypeReference) return false;

        if (modelType instanceof SimpleType) {
            if (!linkModel(modelType)) return false;
        }

        if (typed instanceof Attribute && !solveEntityReference((Attribute) typed, modelType.getFinalType(), reference)) return false;

        modelType.addUsage(model);
        typed.setType(reference.get());
        return true;
    }

    private boolean solveReferencesInRoute(Handler handler, Evaluator evaluator) {
        for (final Route route : handler.getChildren()) {
            for (final Parameter parameter : route.getParameters()) {
                if (!solveReference(handler, new MultipleTyped(parameter))) return false;
                compileAndBind(Types.booleanType(), parameter.getOptional());

                // Compile, Bind, and Evaluate expected constant default value expression.
                final Expression defaultValue = parameter.getDefaultValue();
                if (!defaultValue.isNull())
                    compileConstantValueExpression(handler.getFullName(), parameter.getName(), parameter.getFinalType(), defaultValue, evaluator);

                checkLocalTypeUsage(parameter.getDeepestType(), route, handler);
            }
        }
        return true;
    }

    private int solveReverseReference(Entity entity, Attribute attribute) {
        if (!attribute.getReverseReference().isEmpty() || (entity.equals(attribute.getDbObject()) && !attribute.isMultiple())) return OK;

        String reverse = "";
        for (final Attribute a : entity.attributes()) {
            if (a != attribute && a.getFinalType().equals(attribute.getDbObject()) && (a.isMultiple() || attribute.isMultiple()) &&
                !a.hasOption(ABSTRACT))
            {
                if (!reverse.isEmpty()) return DUPLICATE;
                reverse = a.getName();
                if (attribute.isInner()) break;
            }
        }
        if (reverse.isEmpty() && attribute.isMultiple()) return NOT_FOUND;
        attribute.setReverseReference(reverse);
        return OK;
    }

    private Type solveType(@NotNull final Attribute attribute) {
        Type type = attribute.getType();
        if (attribute.getFinalType() instanceof UnresolvedTypeReference) {
            final UnresolvedTypeReference unresolvedType = (UnresolvedTypeReference) attribute.getFinalType();
            try {
                type = unresolvedType.get().getFinalType();
            }
            catch (final IllegalStateException | UnresolvedTypeReferenceException e) {
                // ignore
            }
        }
        return type;
    }

    private boolean solveViewAttributeReference(final View view, ViewAttribute viewAttribute) {
        if (viewAttribute.getBaseAttributeModelField() instanceof FieldReference && view.getBaseEntityModelType() instanceof DbObject) {
            final FieldReference reference = (FieldReference) viewAttribute.getBaseAttributeModelField();
            final Attribute      attribute = view.getBaseEntity().get().getAttribute(reference.getName()).getOrNull();
            if (replaceViewEntityReferences(view, viewAttribute, reference, attribute)) return true;

            if (lastStage || attribute != null) {
                if (attribute != null) reference.solve(attribute);
                else reference.solve(reference);
            }
            if (attribute == null) return false;
            viewAttribute.setBaseAttribute(attribute);
        }
        else return false;
        return true;
    }
    private boolean solveViewReference(MetaModel type, ModelField field) {
        if (field instanceof ViewAttribute && type instanceof View) return solveViewAttributeReference((View) type, (ViewAttribute) field);
        else {
            final Type t = field.getType();
            return !(t instanceof UnresolvedTypeReference) || solveReference(type, field, (UnresolvedTypeReference) t);
        }
    }

    private boolean solveViewReferences(@NotNull final View view) {
        final List<MetaModel> unresolved = new ArrayList<>();
        view.solveMetaModels(value -> {
            if (value instanceof UnresolvedTypeReference) {
                final UnresolvedTypeReference ref       = (UnresolvedTypeReference) value;
                final ModelType               modelType = repository.getModel(ref.getKey(), ModelType.class).orElse(ref);
                if (lastStage || modelType != value) ref.solve(modelType);
                if (!(modelType instanceof DbObject)) unresolved.add(modelType);
                else if (view.isRemote()) {
                    final DbObject dbObject = (DbObject) modelType;
                    if (!(dbObject.isRemotable() || dbObject.isSearchable()))
                        throw new RuntimeException(new RemoteViewOfNonRemotableException(view.getName(), dbObject.getFullName()).getMessage());
                }
                return modelType;
            }
            return value;
        });

        final List<ModelField> unresolvedFields = new ArrayList<>();
        view.solveSearchByFields(objectFieldReferencesSolver(view, unresolvedFields));
        view.solveDescribes(objectFieldReferencesSolver(view, unresolvedFields));

        return unresolved.isEmpty() && unresolvedFields.isEmpty();
    }  // end method solveViewReferences

    //~ Methods ......................................................................................................................................

    /** Utility to link a single form. */
    public static boolean linkForm(ModelRepository repository, Form form) {
        final ModelLinker linker = new ModelLinkerImpl(repository);
        linker.setLastStage(true);
        return linker.link(form);
    }

    //~ Static Fields ................................................................................................................................

    private static final SolverAndBinder EMPTY = new SolverAndBinder.Empty();

    //~ Inner Interfaces .............................................................................................................................

    private interface SolverAndBinder extends Binder, RefTypeSolver {
        class Empty extends Binder.Default implements SolverAndBinder {
            @NotNull @Override public Type doResolve(@NotNull String name, boolean isColumn) {
                throw new SymbolNotFoundException(name);
            }
        }
    }

    //~ Inner Classes ................................................................................................................................

    private static class EnumSolverAndBinder extends SimpleReferenceSolver implements SolverAndBinder {
        private EnumSolverAndBinder(EnumType enumType) {
            for (final EnumValue value : enumType.getValues())
                put(value.getName(), value.getName(), Types.stringType());
        }
    }

    private static class MultipleTyped implements Typed {
        private final boolean multiple;

        private final Typed typed;

        private MultipleTyped(Typed typed) {
            this.typed = typed;
            multiple   = typed.getType().isArray();
        }

        @NotNull @Override public Type getType() {
            final Type type = typed.getType();
            return multiple ? ((ArrayType) type).getElementType() : type;
        }

        @Override public void setType(@NotNull Type type) {
            typed.setType(multiple ? arrayType(type) : type);
        }
    }

    private static class RouteBody implements Typed {
        private final Route route;

        private RouteBody(final Route route) {
            this.route = route;
        }

        @NotNull @Override public Type getType() {
            return route.getBodyType();
        }

        @Override public void setType(@NotNull Type type) {
            route.setBodyType(type);
        }
    }
}  // end class ModelLinkerImpl
