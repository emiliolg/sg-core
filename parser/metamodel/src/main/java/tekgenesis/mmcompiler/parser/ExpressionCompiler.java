
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.code.Binder;
import tekgenesis.code.Evaluator;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Strings;
import tekgenesis.expr.*;
import tekgenesis.expr.ListExpression.AssignmentListExpression;
import tekgenesis.expr.exception.IllegalOperationException;
import tekgenesis.field.ModelField;
import tekgenesis.field.TypeField;
import tekgenesis.lexer.CharSequenceStream;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.entity.SearchField;
import tekgenesis.metadata.exception.BuilderError;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.EnumType;
import tekgenesis.type.Kind;
import tekgenesis.type.Type;
import tekgenesis.type.UnresolvedTypeReference;

import static tekgenesis.common.core.Constants.HEXADECIMAL_RADIX;
import static tekgenesis.common.core.Option.some;
import static tekgenesis.common.core.Suppliers.fromObject;
import static tekgenesis.expr.ExpressionFactory.*;
import static tekgenesis.md.MdConstants.DEPRECATED_FIELD;
import static tekgenesis.metadata.exception.BuilderErrors.invalidExpression;
import static tekgenesis.metadata.exception.BuilderErrors.unresolvedField;
import static tekgenesis.metadata.exception.BuilderErrors.unresolvedReference;
import static tekgenesis.metadata.exception.BuilderErrors.unresolvedSearchableField;
import static tekgenesis.mmcompiler.ast.MMToken.*;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;
import static tekgenesis.type.Kind.ANY;
import static tekgenesis.type.Types.anyType;
import static tekgenesis.type.Types.nullType;

/**
 * A Simple ExpressionAST Compiler.
 */
public class ExpressionCompiler extends MetaModelCompiler {

    //~ Constructors .................................................................................................................................

    /** Create an Expression Compiler. */
    @SuppressWarnings("WeakerAccess")
    public ExpressionCompiler(@NotNull final String expression) {
        super(MMToken.lexer().resetStream(new CharSequenceStream("", expression)), "");
    }

    //~ Methods ......................................................................................................................................

    /** Build a {@link Expression} from the AST. */
    @NotNull
    @SuppressWarnings("WeakerAccess")
    public Expression buildExpression(@NotNull final Type resultType) {
        return buildExpression(getAST(), resultType).createExpression();
    }

    @NotNull @Override public MetaModelAST getAST() {
        return super.getAST().getChild(0);
    }

    @Override protected void parse() {
        parser.parseExpression();
    }

    //~ Methods ......................................................................................................................................

    /** Build a {@link ExpressionAST } from the AST. */
    public static ExpressionAST buildExpression(@NotNull final MetaModelAST ast, @NotNull final Type resultType) {
        final IdentityHashMap<ExpressionAST, MetaModelAST> map = new IdentityHashMap<>();

        return new RootExpressionAst(buildExpression(ast, ast, map), resultType, ast, map);
               // Auto scaling of decimals to the defined type
               // final RootExpressionAst root = new RootExpressionAst(e, resultType, ast, map);
               // return resultType.getKind() == DECIMAL ? scale(root, ((DecimalType) resultType).getDecimals()) : root;
    }

    /** Build a {@link ExpressionAST } from the AST. */
    public static ExpressionAST buildExpression(@NotNull final MetaModelAST ast, @NotNull final Type resultType, @Nullable final Type innerExprType) {
        final IdentityHashMap<ExpressionAST, MetaModelAST> map = new IdentityHashMap<>();

        return new RootExpressionAst(buildExpression(ast, ast, map, innerExprType), resultType, ast, map);
    }

    /**
     * Handy method to create an evaluator, compile and evaluate an Expression You pay the setup
     * cost every time but is shorter ;-).
     */
    public static <T extends Binder & RefTypeSolver> Object evaluate(String expression, T environment) {
        final Evaluator          evaluator = new Evaluator();
        final ExpressionCompiler compiler  = new ExpressionCompiler(expression);
        return compiler.buildExpression(anyType()).compileBindAndEvaluate(environment, environment, evaluator);
    }

    private static ExpressionAST bld(MetaModelAST ast, int n, Map<ExpressionAST, MetaModelAST> map) {
        return buildExpression(ast.getChild(n), ast, map);
    }

    @NotNull private static ExpressionAST buildAssignmentExpression(MetaModelAST ast, Map<ExpressionAST, MetaModelAST> map) {
        final ExpressionAST         ref           = buildExpression(ast.getChild(0), ast, map);
        final ExpressionAST         value         = buildExpression(ast.getChild(1).getChild(0), ast.getChild(1), map);
        final Option<ExpressionAST> when          = ast.getChild(2).isEmpty() ? Option.empty() : some(buildExpression(ast.getChild(2), ast, map));
        final boolean               mustOrMustNot = ast.getChild(1).getType() == MMToken.EQ;

        return new AssignmentExpression(ref, value, when, mustOrMustNot);
    }

    private static ExpressionAST buildExpression(final MetaModelAST ast, final MetaModelAST parentAst, final Map<ExpressionAST, MetaModelAST> map) {
        return buildExpression(ast, parentAst, map, null);
    }

    @SuppressWarnings("IfStatementWithTooManyBranches")
    private static ExpressionAST buildExpression(final MetaModelAST ast, final MetaModelAST parentAst, final Map<ExpressionAST, MetaModelAST> map,
                                                 @Nullable final Type innerExprType) {
        final ExpressionAST result;
        final MMToken       type = ast.getType();

        if (type.isLiteral()) result = buildLiteral(ast);
        else if (type == MMToken.INTERPOLATION) result = buildLiteral(ast.getChild(0));
        else if (type == FIELD_REF) result = new RefAst(ast, retrieveReferenceQualifiedId(ast));
        else if (type == FILTER_REF) result = new FilterRef(ast, retrieveReferenceQualifiedId(ast));
        else if (type == FORBIDDEN) result = new ForbiddenExpression(ast.getChild(0).getChild(0).getText());
        else if (type == IS_UPDATE) result = new UpdateExpression();
        else if (type == MMToken.IS_READ_ONLY) result = new ReadOnlyExpression();
        else if (type == INVOKE) result = buildFunctionCall(ast, map);
        else if (type == IF) result = new IfExpression(bld(ast, 0, map), bld(ast, 1, map), bld(ast, 2, map));
        else if (type == ASSIGNMENT_FIELD) result = buildAssignmentExpression(ast, map);
        else if (type == ASSIGNMENT_LIST || type == INNER_ASSIGNMENT_LIST) {
            final ListExpression expr = new AssignmentListExpression(null, innerExprType, type != ASSIGNMENT_LIST);
            for (final MetaModelAST node : ast.children())
                expr.add(buildExpression(node, ast, map, innerExprType));
            result = expr;
        }
        else if (type == LIST) {
            final ListExpression expr = new ListExpression(innerExprType);
            for (final MetaModelAST node : ast.children())
                expr.add(buildExpression(node, ast, map, innerExprType));
            result = expr;
        }
        else {
            final BinaryExpression.Operator b = type.binaryOperator();
            if (b != null) result = new BinaryExpression(b, bld(ast, 0, map), bld(ast, 1, map));
            else {
                final UnaryExpression.Operator u = type.unaryOperator();
                if (u != null) result = new UnaryExpression(u, bld(ast, 0, map));
                else throw new InvalidExpression(parentAst);
            }
        }

        map.put(result, ast);
        return result;
    }

    private static ExpressionAST buildFunctionCall(final MetaModelAST ast, final Map<ExpressionAST, MetaModelAST> map) {
        String                    name = null;
        final List<ExpressionAST> args = new ArrayList<>();
        for (final MetaModelAST c : ast.children()) {
            if (name == null) name = c.getText();
            else args.add(buildExpression(c, c, map));
        }
        if (name == null) throw new IllegalStateException("Name is undefined");
        if (args.size() == 1) {
            final UnaryExpression result = UnaryExpression.fromId(name, args.get(0));
            if (result != null) return result;
        }
        return new FunctionCallNode(name, args);
    }

    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private static ExpressionAST buildLiteral(MetaModelAST ast) {
        final String  text = ast.getText();
        final MMToken type = ast.getType();
        switch (type) {
        case TRUE:
            return bool(true);
        case FALSE:
            return bool(false);
        case NULL:
            return nullValue();
        case STRING_LITERAL:
            return str(Strings.decode(text));
        case HEX_INT:
            return integer(Integer.parseInt(text, HEXADECIMAL_RADIX));
        case DEC_INT:
            return integer(Integer.parseInt(text));
        case FIXED_POINT_DECIMAL:
            return decimal(new BigDecimal(text));
        case DOUBLE_LITERAL:
            return real(Double.parseDouble(text));
        default:
            throw new IllegalArgumentException("Illegal Literal Token: " + text + " " + type);
        }
    }

    //~ Inner Classes ................................................................................................................................

    public static class FilterRef extends Ref {
        private final MetaModelAST ast;

        FilterRef(@NotNull MetaModelAST ast, String ref) {
            super(ref);
            this.ast = ast;
        }

        @NotNull @Override public ExpressionAST solveType(@NotNull RefTypeSolver refResolver) {
            final Type type = getTargetType();
            if (type == null) return super.solveType(refResolver);

            if (type.isDatabaseObject()) {
                final ImmutableList<SearchField> fields   = ((DbObject) type).searchByFields();
                final Option<SearchField>        fieldOpt = fields.getFirst(f -> f.getId().equals(getName()));
                if (fieldOpt.isEmpty() && !DEPRECATED_FIELD.equals(getName())) throw new InvalidExpression(ast, unresolvedSearchableField(getName()));
                return super.solveType((referenceName, isColumn) ->
                        fields.getFirst(field -> field.getId().equals(getName()))
                              .map(SearchField::getField)
                              .map(ModelField::getType)
                              .orElse(nullType()));
            }

            if (type.isEnum()) {
                final EnumType                    enumType = (EnumType) type;
                final Seq<? extends TypeField>    children = enumType.getChildren();
                final Option<? extends TypeField> fieldOpt = children.filter(c -> c.getName().equals(getName())).getFirst();
                if (fieldOpt.isEmpty()) throw new InvalidExpression(ast, unresolvedField(getName(), enumType.getFullName()));
                return super.solveType((referenceName, isColumn) -> fieldOpt.get().getType());
            }

            return super.solveType(refResolver);
        }

        @Override public boolean isFilterRef() {
            return true;
        }
    }

    public static class InvalidExpression extends RuntimeException {
        private final BuilderError builderError;
        private final MetaModelAST expressionAst;

        /** Creates an invalid expressions exception on the specified ast node. */
        public InvalidExpression(@NotNull final MetaModelAST expressionAst) {
            this(expressionAst, invalidExpression("Invalid expression", expressionAst.getText()));
        }

        private InvalidExpression(@NotNull final MetaModelAST expressionAst, @NotNull final IllegalOperationException cause) {
            this(expressionAst, invalidExpression(cause.getErrorMessage(), expressionAst.getText()));
        }

        private InvalidExpression(@NotNull final MetaModelAST expressionAst, @NotNull final BuilderError builderError) {
            super();
            this.expressionAst = expressionAst;
            this.builderError  = builderError;
        }

        /** Returns the builder error to be reported. */
        public BuilderError getBuilderError() {
            return builderError;
        }

        /** Returns the ast reference node that is unresolved. */
        public MetaModelAST getExpressionAst() {
            return expressionAst;
        }

        private static final long serialVersionUID = 3345553268264615254L;
    }

    public static class RefAst extends Ref {
        private final MetaModelAST ast;

        private RefAst(@NotNull MetaModelAST ast, String ref) {
            super(ref);
            this.ast = ast;
        }

        @NotNull @Override protected Type doSolveType(@NotNull final RefTypeSolver refResolver) {
            final Type type = super.doSolveType(refResolver);
            if (type.isUndefined()) throw new InvalidExpression(ast, unresolvedReference(getName()));
            return type;
        }
    }

    public static class RootExpressionAst extends ConversionOp {
        private final IdentityHashMap<ExpressionAST, MetaModelAST> map;

        private RootExpressionAst(@NotNull ExpressionAST e, @NotNull Type expectedType, @NotNull MetaModelAST ast,
                                  final IdentityHashMap<ExpressionAST, MetaModelAST> map) {
            super(e, expectedType instanceof UnresolvedTypeReference ? (UnresolvedTypeReference) expectedType : fromObject(expectedType));
            this.map = map;
            map.put(this, ast);
        }

        @NotNull @Override public ExpressionAST solveType(@NotNull final RefTypeSolver refResolver) {
            try {
                return super.solveType(refResolver);
            }
            catch (final IllegalOperationException e) {
                throw new InvalidExpression(map.get(e.getExpr()), e);
            }
        }

        @Override public String toString() {
            return getOperand().toString();
        }

        @Override protected boolean conversionNeeded(Type opType) {
            return getTargetKind() != ANY && (opType.getKind() == Kind.DECIMAL || super.conversionNeeded(opType));
        }
    }
}  // end class ExpressionCompiler
