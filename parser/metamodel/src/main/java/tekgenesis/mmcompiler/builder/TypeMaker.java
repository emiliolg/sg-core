
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

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.common.ModelBuilder;
import tekgenesis.metadata.entity.StructBuilder;
import tekgenesis.metadata.entity.StructType;
import tekgenesis.metadata.entity.TypeDefBuilder;
import tekgenesis.metadata.entity.TypeFieldBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.inheritance.ExtendsFromFinalException;
import tekgenesis.metadata.exception.inheritance.FieldClashesWithUnrelatedTypeException;
import tekgenesis.metadata.exception.inheritance.InterfaceExtendsOnlyException;
import tekgenesis.metadata.exception.inheritance.MultipleInheritanceException;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static tekgenesis.common.collections.Colls.toList;
import static tekgenesis.mmcompiler.ast.MMToken.FIELD;
import static tekgenesis.mmcompiler.ast.MMToken.STRUCT_REF;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;

/**
 * Create an TypeDefBuilder.
 */
class TypeMaker extends CompositeMaker<StructType, TypeField, TypeFieldBuilder> {

    //~ Constructors .................................................................................................................................

    TypeMaker(MetaModelAST node, BuilderFromAST builderFromAST, String sourceName, QContext context) {
        super(node, builderFromAST, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    protected ModelBuilder<?, ?> createBuilder(QName fqn, String label, EnumSet<Modifier> modifiers) {
        final ImmutableList<MetaModelAST> nodes = toList(rootNode);

        final MetaModelAST       lastNode = nodes.get(nodes.size() - 1);
        final ModelBuilder<?, ?> builder  = lastNode.getType() == FIELD ? makeTypeDef(fqn, modifiers, lastNode)
                                                                        : makeStructBuilder(fqn, label, modifiers, nodes);

        checkBuilder(builder);
        return builder;
    }  // end method createBuilder

    void addArg(StructBuilder builder, MetaModelAST field, String name) {
        try {
            builder.addArgument(buildField(name, field));
        }
        catch (final BuilderException e) {
            error(field, e);
        }
    }

    /** Builds a field of the specified Type. */
    @Override TypeFieldBuilder createField(String nm, Type type) {
        return new TypeFieldBuilder(nm, type);
    }

    private void addSuperType(StructBuilder builder, MetaModelAST node, Option<StructType> superType) {
        try {
            builder.withSuperType(superType.get());
        }
        catch (ExtendsFromFinalException | FieldClashesWithUnrelatedTypeException | MultipleInheritanceException | InterfaceExtendsOnlyException e) {
            error(node, e);
        }
    }

    private StructBuilder makeStructBuilder(final QName fqn, final String label, final EnumSet<Modifier> modifiers,
                                            final ImmutableList<MetaModelAST> nodes) {
        final StructBuilder builder = StructBuilder.struct(sourceName, fqn.getQualification(), fqn.getName()).label(label).withModifiers(modifiers);

        for (final MetaModelAST node : nodes) {
            switch (node.getType()) {
            case LIST:
                for (final MetaModelAST field : node.children(FIELD))
                    addField(builder, field, fqn.getName());
                break;
            case ARG_LIST:
                for (final MetaModelAST field : node.children(FIELD))
                    addArg(builder, field, fqn.getName());
                break;
            case WIDGET:
                final QName widget = context.resolve(retrieveReferenceQualifiedId(node.getChild(0)));
                builder.defaultWidget(widget.getFullName());
                break;
            case EXTENDS:
                for (final MetaModelAST field : node.children(STRUCT_REF)) {
                    final Option<StructType> superType = getMetaModelReference(StructType.class, field);
                    if (superType.isPresent()) addSuperType(builder, node, superType);
                }
                break;
            default:
                // Ignore
            }
        }
        return builder;
    }

    private ModelBuilder<?, ?> makeTypeDef(final QName fqn, final EnumSet<Modifier> modifiers, final MetaModelAST node) {
        final TypeFieldBuilder field = buildField(fqn.getName(), node);
        return TypeDefBuilder.typeDef(sourceName, fqn.getQualification(), fqn.getName(), field.getType())
               .withModifiers(modifiers)
               .withOptions(field.getOptions());
    }
}  // end class TypeMaker
