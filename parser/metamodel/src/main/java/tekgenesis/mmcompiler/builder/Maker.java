
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.*;
import tekgenesis.common.core.enumeration.Enumerations;
import tekgenesis.common.util.JavaReservedWords;
import tekgenesis.expr.Expression;
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.field.FieldOption;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.entity.SimpleType;
import tekgenesis.metadata.exception.*;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.mmcompiler.parser.ExpressionCompiler;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.*;

import static java.lang.Character.isLowerCase;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.first;
import static tekgenesis.common.core.Option.empty;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Strings.unCommentText;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.metadata.exception.BuilderErrors.*;
import static tekgenesis.mmcompiler.ast.MMToken.*;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;
import static tekgenesis.parser.ASTNode.Utils.childrenAsStrings;
import static tekgenesis.type.Types.nullType;

/**
 * Super class of the Makers for the different type of Models.
 */
abstract class Maker {

    //~ Instance Fields ..............................................................................................................................

    @NotNull BuilderFromAST                            builderFromAST;
    @NotNull final QContext                            context;
    @NotNull String                                    documentation;
    @NotNull ModelRepository                           repository;
    @NotNull MetaModelAST                              rootNode;
    @NotNull String                                    sourceName;
    @NotNull private final Tuple<MetaModelAST, String> idLabel;
    @NotNull private final QName                       modelName;

    //~ Constructors .................................................................................................................................

    Maker(@NotNull MetaModelAST rootNode, @NotNull BuilderFromAST builderFromAST, @NotNull String sourceName, @NotNull QContext context) {
        this.builderFromAST = builderFromAST;
        this.sourceName     = sourceName;
        this.context        = context;
        this.rootNode       = rootNode;
        repository          = builderFromAST.getRepository();
        idLabel             = retrieveLabeledId(rootNode);
        final MetaModelAST id = idLabel.first();
        modelName     = context.withPackage(id.getText());
        documentation = retrieveDocumentation();
    }

    //~ Methods ......................................................................................................................................

    @Override public String toString() {
        return modelName.toString();
    }

    protected abstract ModelBuilder<?, ?> createBuilder(QName fqn, String label, EnumSet<Modifier> modifiers);

    void checkBuilder(ModelBuilder<?, ?> builder) {
        for (final BuilderError error : builder.check())
            // todo Attribute Exceptions must inherit from AttributeBuilderException
            if (error instanceof NullableInPrimaryKeyException) error(error);
            else if (rootNode.getChild(0).isEmpty()) error(rootNode, error);
            else error(rootNode.getChild(0), error);
    }

    void error(MetaModelAST node, BuilderError e) {
        getErrorListener().error(node, e);
    }

    void make() {
        final MetaModelAST id = idLabel.first();

        final String idText = id.getText();

        if (isEmpty(idText)) return;

        if (isLowerCase(idText.charAt(0))) {
            error(id, lowerCaseModel(idText));
            return;
        }

        if (JavaReservedWords.isReserved(Strings.deCapitalizeFirst(idText))) {
            error(id, javaReservedWordModel(idText));
            return;
        }

        // if (repository.getModel(modelName).isDefined()) {
        // error(rootNode, new DuplicateDefinitionException(modelName.getFullName()));
        // return;
        // }

        try {
            final MetaModel build = createBuilder(modelName, idLabel.second(), retrieveModifiers(rootNode)).withDocumentation(documentation).build();
            builderFromAST.addModel(build, id);
        }
        catch (final BuilderException e) {
            error(id, e);
        }
        catch (final ExpressionCompiler.InvalidExpression e) {
            error(e.getExpressionAst(), e.getBuilderError());
        }
        catch (final UnmappedReferenceException e) {
            error(id, e.getCause());
        }
        catch (final IllegalOperationException e) {
            error(id, invalidExpression(e.getErrorMessage(), idText));
        }
    }

    @NotNull <T extends Enum<T>> Option<T> retrieveEnumValue(Class<T> enumeration, MetaModelAST node) {
        final String text = notEmpty(node.getText(), "");
        try {
            return some(Enumerations.valueOf(enumeration, text.toUpperCase()));
        }
        catch (final IllegalArgumentException e) {
            error(node, illegalEnumValue(enumeration, text));
        }
        return empty();
    }

    Map<String, ModelField> retrieveFieldIds(MetaModelAST node) {
        return retrieveFieldIds(node, node.children());
    }

    @NotNull Map<String, ModelField> retrieveFieldIds(MetaModelAST mainNode, Seq<MetaModelAST> children) {
        final Map<String, ModelField> result = new LinkedHashMap<>();
        for (final MetaModelAST n : children) {
            final String id = n.getChild(0).getText();
            if (!id.isEmpty()) {
                if (result.containsKey(id)) error(n, new DuplicateAttributeException(id, mainNode.getType().getText(), ""));
                else result.put(id, new ASTFieldReference(id, this, n));
            }
        }
        return result;
    }

    @Nullable String retrieveId(MetaModelAST node) {
        final Seq<MetaModelAST> children = node.children(IDENTIFIER);
        if (children.size() == 1) return children.getFirst().get().getText();
        return null;
    }

    Tuple<MetaModelAST, String> retrieveLabeledId(MetaModelAST node) {
        MetaModelAST id    = node.getEmptyNode();
        String       label = "";
        for (final MetaModelAST child : node.children(LABELED_ID)) {
            for (final MetaModelAST a : child) {
                if (a.hasType(IDENTIFIER)) id = a;
                else if (a.hasType(STRING_LITERAL)) label = Strings.decode(a.getText());
            }
        }
        return tuple(id, label);
    }

    @NotNull Type retrieveNotNullType(MetaModelAST fieldNode, String fieldName, @NotNull Function<MetaModelAST, Type> refHandler) {
        Type type = retrieveType(fieldNode, refHandler);
        if (type == null) {
            error(fieldNode, BuilderErrors.unspecifiedType(fieldName));

            retrieveType(fieldNode, refHandler);
            type = Types.stringType();
        }
        return type;
    }

    @Nullable FieldOption retrieveOption(MetaModelAST n) {
        final String      optionName = n.getChild(0).getText();
        final FieldOption opt        = FieldOption.fromId(optionName);

        if (opt != null) return opt;
        error(n, BuilderErrors.invalidOption(optionName));
        return null;
    }

    @Nullable Type retrieveType(MetaModelAST fieldNode, @NotNull Function<MetaModelAST, Type> refHandler) {
        for (final MetaModelAST child : fieldNode.children()) {
            if (child.hasType(ENTITY) || child.hasType(WIDGET) || child.hasType(ENUM)) return refHandler.apply(child);

            if (child.hasType(TYPE_NODE)) {
                final MetaModelAST t = child.getChild(0);
                return t.hasType(TYPE_REF) || t.hasType(STRUCT_REF) ? refHandler.apply(child) : retrieveType(child);
            }
        }
        return null;
    }

    @Nullable String retrieveUsing(MetaModelAST node) {
        final Seq<MetaModelAST> children = node.children(CLASS);
        if (children.size() == 1) {
            final MetaModelAST metaModelAST = first(children).get();
            final String       name         = retrieveReferenceQualifiedId(metaModelAST);
            return name.isEmpty() ? name : context.withPackage(name).getFullName();
        }
        return null;
    }

    /** Get the nth child as an ExpressionAST Node. */
    @NotNull Expression getAsExpression(MetaModelAST element, final Type resultType) {
        return getAsExpression(element, resultType, null, null);
    }

    /**
     * Get the nth child as an ExpressionAST Node, return the specified default if not child is
     * present.
     */
    @NotNull Expression getAsExpression(MetaModelAST element, @NotNull Expression defaultValue) {
        return getAsExpression(element, defaultValue.getType(), defaultValue);
    }

    @NotNull Expression getAsExpression(@NotNull MetaModelAST element, @NotNull Type resultType, @Nullable Type innerExprType) {
        return getAsExpression(element, resultType, null, innerExprType);
    }

    @NotNull String getDocumentationText(MetaModelAST ast) {
        if (!ast.hasType(DOCUMENTATION)) return "";
        final String text = ast.getChild(0).getText();
        return text.startsWith("//-") ? text.substring(3).trim() : unCommentText(text);
    }

    <T extends MetaModel> Option<T> getMetaModelReference(Class<T> clazz, MetaModelAST n) {
        final String    name  = retrieveReferenceQualifiedId(n);
        final Option<T> model = repository.getModel(context.extractQualification(name), context.extractName(name), clazz);
        if (model.isEmpty()) error(n, unresolvedReference(name));
        return model;
    }

    <T extends MetaModel> Option<T> getMetaModelReferenceCheckType(Class<T> clazz, MetaModelAST n) {
        final String            name  = retrieveReferenceQualifiedId(n);
        final Option<MetaModel> model = repository.getModel(context.extractQualification(name), context.extractName(name));
        if (model.isEmpty()) {
            error(n.getChild(0), unresolvedReference(name));
            return Option.empty();
        }
        Option<T>       result = model.castTo(clazz);
        final MetaModel type   = model.get();
        if (result.isEmpty() && type instanceof SimpleType) {
            final Type ft = ((SimpleType) type).getFinalType();
            if (ft instanceof UnresolvedTypeReference) result = repository.getModel(((UnresolvedTypeReference) ft).getKey()).castTo(clazz);
            else result = some(ft).castTo(clazz);
        }
        return result;
    }

    String getName() {
        return modelName.getName();
    }

    /** Retrieve a basic type based on an String. May be overridden to allow type extensions. */
    Type getTypeFromString(String typeName) {
        return Types.fromString(typeName);
    }

    private void error(BuilderError e) {
        getErrorListener().error(e);
    }

    @NotNull private String retrieveDocumentation() {
        for (final MetaModelAST ast : rootNode.children(DOCUMENTATION))
            if (ast.hasType(DOCUMENTATION)) return getDocumentationText(ast);
        return "";
    }

    private EnumSet<Modifier> retrieveModifiers(MetaModelAST node) {
        final EnumSet<Modifier> result = Modifier.emptySet();
        for (final MetaModelAST modifiers : node.children(MODIFIERS)) {
            for (final MetaModelAST m : modifiers) {
                final Modifier e = Modifier.fromId(m.getText());
                if (e != null) result.add(e);
            }
        }
        return result;
    }

    @Nullable private Type retrieveType(MetaModelAST node) {
        final MetaModelAST t        = node.getChild(0);
        final String       typeName = retrieveReferenceQualifiedId(t);

        Type type = getTypeFromString(typeName);
        if (type == nullType()) {
            error(t, BuilderErrors.unexpectedType(typeName));
            return null;
        }

        MetaModelAST next = node.getChild(1);
        if (next.hasType(LIST)) {
            type = type.applyParameters(childrenAsStrings(node.getChild(1)));
            next = node.getChild(2);
        }
        return next.hasType(ASTERISK) ? Types.arrayType(type) : type;
    }

    @NotNull private Expression getAsExpression(@NotNull MetaModelAST element, @NotNull Type resultType, @Nullable Expression defaultValue) {
        return getAsExpression(element, resultType, defaultValue, null);
    }

    @NotNull private Expression getAsExpression(@NotNull MetaModelAST element, @NotNull Type resultType, @Nullable Expression defaultValue,
                                                @Nullable Type innerExprType) {
        final Expression e = element.isEmpty() ? defaultValue
                                               : ExpressionCompiler.buildExpression(element, resultType, innerExprType).createExpression();
        if (e == null) throw new ExpressionCompiler.InvalidExpression(element);
        return e;
    }

    @NotNull private BuilderErrorListener getErrorListener() {
        return builderFromAST.getErrorListener();
    }

    //~ Methods ......................................................................................................................................

    /** returns an String based on a Qualified Id. Try this node and the first child. */
    static String retrieveNodeText(MetaModelAST t) {
        final StrBuilder result = new StrBuilder();
        for (final MetaModelAST id : t)
            result.appendElement(id.getText(), ".");
        return result.toString();
    }
}  // end class Maker
