
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
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.QName;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.service.Method;
import tekgenesis.field.FieldOption;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.RaisingANonExceptionEnum;
import tekgenesis.metadata.exception.UnsupportedOptionException;
import tekgenesis.metadata.handler.HandlerBuilder;
import tekgenesis.metadata.handler.ParameterBuilder;
import tekgenesis.metadata.handler.RouteBuilder;
import tekgenesis.metadata.handler.SecureMethod;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.EnumType;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.core.Strings.unquote;
import static tekgenesis.mmcompiler.ast.MMToken.*;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;
import static tekgenesis.parser.ASTNode.Utils.assertType;
import static tekgenesis.type.Types.arrayType;

/**
 * Create {@link HandlerBuilder}.
 */
class HandlerMaker extends Maker {

    //~ Constructors .................................................................................................................................

    HandlerMaker(MetaModelAST handlerNode, BuilderFromAST builderFromAST, String sourceName, QContext context) {
        super(handlerNode, builderFromAST, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    /** Create a handler Builder. */
    @NotNull protected HandlerBuilder createBuilder(QName fqn, String label, EnumSet<Modifier> modifiers) {
        final HandlerBuilder builder = new HandlerBuilder(sourceName, fqn.getQualification(), fqn.getName()).label(label).withModifiers(modifiers);

        for (final MetaModelAST n : rootNode) {
            switch (n.getType()) {
            case FORM_REF:
                builder.withForm(n.getChild(0).getText());
                break;
            case ON_ROUTE:
                builder.onRoute(unquote(n.getChild(0).getText()));
                break;
            case SECURE_BY:
                retrieveEnumValue(SecureMethod.class, n.getChild(0)).ifPresent(builder::withSecureMethod);
                break;
            case UNRESTRICTED:
                builder.withSecureMethod(SecureMethod.UNRESTRICTED);
                break;
            case ENUM_REF:
                getMetaModelReference(EnumType.class, n).ifPresent(r -> {                               //
                    if (!r.isException()) error(n, new RaisingANonExceptionEnum(r.getName(), sourceName));  //
                    else builder.withRaiseEnum(r);                                                      //
                });
                break;
            case LIST:
                for (final MetaModelAST route : n.children(ROUTE))
                    addRoute(builder, route);
                break;
            default:
                // Ignore
            }
        }

        return builder;
    }  // end method createBuilder

    @NotNull @Override Type getTypeFromString(String typeName) {
        return Types.extendedFromString(typeName, super.getTypeFromString(typeName));
    }

    private void addParameter(RouteBuilder rb, MetaModelAST parameter) {
        try {
            rb.withParameter(buildParameter(rb, parameter));
        }
        catch (final BuilderException e) {
            error(parameter, e);
        }
    }

    private void addParameterOptions(ParameterBuilder builder, Type type, MetaModelAST parameter) {
        for (final MetaModelAST n : parameter) {
            try {
                if (n.hasType(OPTION)) buildParameterOption(builder, n, type.getFinalType());
            }
            catch (final BuilderException e) {
                error(n, e);
            }
        }
    }

    private void addRoute(HandlerBuilder builder, MetaModelAST route) {
        try {
            builder.addRoute(buildRoute(builder.getFullName(), route));
        }
        catch (final BuilderException e) {
            error(route, e);
        }
    }

    private void addRouteOption(RouteBuilder rb, FieldOption opt, MetaModelAST node, String route)
        throws BuilderException
    {
        switch (opt.getType()) {
        case ENUM_T:
            // Http Method option
            rb.withEnum(opt, Method.class, node.getChild(1).getEffectiveNode().getText());
            break;
        case BOOLEAN_T:
            rb.with(opt);
            break;
        case STRING_T:
            rb.with(opt, Strings.decode(node.getChild(1).getEffectiveNode().getText()));
            break;
        case TYPE_T:
            // Body option
            rb.with(opt, getTypeFromNode(node, route));
            break;
        case FIELDS_T:
            // Parameters option
            for (final MetaModelAST parameter : node.getChild(1).children(FIELD))
                addParameter(rb, parameter);
            break;
        default:
            error(node, new UnsupportedOptionException(opt));
        }
    }

    private ParameterBuilder buildParameter(RouteBuilder rb, MetaModelAST parameter) {
        final Tuple<MetaModelAST, String> idLabel   = retrieveLabeledId(parameter);
        final String                      fieldName = idLabel.first().getText();

        final TypeNodeProcessor referenceTypeProcessor = new TypeNodeProcessor();
        final Type              type                   = retrieveNotNullType(parameter, fieldName, referenceTypeProcessor);
        final Type              finalType              = referenceTypeProcessor.multiple ? arrayType(type) : type;

        final ParameterBuilder builder = rb.createParameterBuilder(fieldName, finalType);

        builder.description(idLabel.second());
        builder.route(rb.getRelativePath());

        addParameterOptions(builder, finalType, parameter);

        return builder;
    }

    private void buildParameterOption(ParameterBuilder builder, MetaModelAST n, final Type type)
        throws BuilderException
    {
        final FieldOption opt = retrieveOption(n);
        if (opt == null) return;

        switch (opt) {
        case OPTIONAL:
            builder.optional();
            break;
        case SIGNED:
            builder.with(FieldOption.SIGNED);
            break;
        case DEFAULT:
            builder.defaultValue(getAsExpression(n.getChild(1), type));
            break;
        default:
            // Ignore
        }
    }

    private void buildPath(RouteBuilder rb, MetaModelAST n) {
        final MetaModelAST path = n.getChild(0);
        try {
            rb.withPath(Strings.unquote(path.getText()));
        }
        catch (final BuilderException e) {
            error(path, e);
        }
    }

    private RouteBuilder buildRoute(String fqn, MetaModelAST node)
        throws BuilderException
    {
        assertType(node, ROUTE);

        final RouteBuilder builder = new RouteBuilder(fqn);

        for (final MetaModelAST n : node) {
            switch (n.getType()) {
            case PATH:
                buildPath(builder, n);
                break;
            case TYPE_NODE:
                builder.withType(getTypeFromNode(node, builder.getRelativePath()));
                break;
            case METHOD_REF:
                builder.withClassMethod(n.getChild(0).getText());
                break;
            case OPTION:
                final FieldOption opt = retrieveOption(n);
                if (opt != null) addRouteOption(builder, opt, n, builder.getRelativePath());
                break;
            default:
                // Ignore
            }
        }
        return builder;
    }

    private Type getTypeFromNode(MetaModelAST node, String name) {
        final TypeNodeProcessor processor = new TypeNodeProcessor();
        final Type              type      = retrieveNotNullType(node, name, processor);
        return processor.multiple ? arrayType(type) : type;
    }

    //~ Inner Classes ................................................................................................................................

    private class TypeNodeProcessor implements Function<MetaModelAST, Type> {
        private boolean multiple = false;

        @Override public Type apply(MetaModelAST typeNode) {
            multiple = hasMultipleMark(typeNode);
            final MetaModelAST typeRefNode = typeNode.getChild(0);
            return new ASTUnresolvedTypeReference(HandlerMaker.this, sourceName, context, retrieveReferenceQualifiedId(typeRefNode), typeRefNode);
        }

        private boolean hasMultipleMark(MetaModelAST typeNode) {
            return typeNode.getChild(1).hasType(ASTERISK);
        }
    }
}  // end class HandlerMaker
