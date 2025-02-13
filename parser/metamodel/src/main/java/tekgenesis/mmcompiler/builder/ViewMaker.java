
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.common.core.Strings;
import tekgenesis.common.core.Tuple;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.*;
import tekgenesis.metadata.exception.BuilderErrors;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.mmcompiler.ast.MMToken;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static java.lang.Integer.parseInt;

import static tekgenesis.mmcompiler.ast.MMToken.*;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;
import static tekgenesis.parser.ASTNode.Utils.assertType;

/**
 * Create an EntityBuilder.
 */
class ViewMaker extends CompositeMaker<View, ViewAttribute, AttributeBuilder> {

    //~ Instance Fields ..............................................................................................................................

    private boolean asView;

    //~ Constructors .................................................................................................................................

    ViewMaker(MetaModelAST entityNode, BuilderFromAST builderFromAST, String sourceName, QContext context) {
        super(entityNode, builderFromAST, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    @Override protected AttributeBuilder buildField(String viewName, MetaModelAST fieldNode) {
        if (asView || fieldNode.children().exists(n -> n.hasType(MMToken.OPTION))) return super.buildField(viewName, fieldNode);
        assertType(fieldNode, FIELD);

        final Tuple<MetaModelAST, String> labeledId     = retrieveLabeledId(fieldNode);
        String                            attributeName = retrieveFieldRef(fieldNode);
        final String                      name          = labeledId.first().getText();

        if (attributeName == null) {
            error(fieldNode, BuilderErrors.unspecifiedType(name));
            attributeName = "";
        }

        // if (Predefined.isEmpty(name)) name = attributeName;

        final ViewAttributeBuilder builder = new ViewAttributeBuilder(name, new ASTFieldReference(attributeName, this, fieldNode));
        builder.description(labeledId.second());
        return builder;
    }

    @SuppressWarnings("OverlyLongMethod")
    protected ViewBuilder createBuilder(QName fqn, String label, EnumSet<Modifier> modifiers) {
        final ViewBuilder builder = new ViewBuilder(sourceName, fqn.getQualification(), fqn.getName(), context.getSchema()).label(label)
                                    .withModifiers(modifiers);

        for (final MetaModelAST node : rootNode) {
            switch (node.getType()) {
            case DESCRIBED_BY:
                builder.describedBy(retrieveFieldIds(node).values());
                break;
            case IMAGE:
                final MetaModelAST image = node.getChild(0).getChild(0);
                builder.image(new ASTFieldReference(image.getText(), this, image));
                break;
            case AS:
                builder.as(Strings.decode(node.children(STRING_LITERAL).getFirst().get().getText()));
                asView = true;
                break;
            case INDEX:
                final Tuple<String, Collection<ModelField>> index = getIndex(node);
                try {
                    builder.withIndex(index.first(), index.second());
                }
                catch (final BuilderException e) {
                    error(node, e);
                }
                break;
            case PRIMARY_KEY:
                builder.primaryKey(retrieveFieldIds(node).values());
                break;
            case OF:
                builder.withEntities(retrieveMetaModels(node));
                break;
            case SEARCHABLE:
                addSearchable(builder, node);
                break;
            case UPDATABLE:
                builder.updatable();
                break;
            case REMOTABLE:
                builder.remotable();
                break;
            case OPTIMISTIC:
                builder.optimistic();
                break;
            case BATCH_SIZE:
                builder.batchSize(parseInt(node.getChild(0).getText()));
                break;
            case LIST:
                for (final MetaModelAST field : node.children(FIELD))
                    addField(builder, field, fqn.getName());
                break;
            default:
                // Ignore
            }
        }

        checkBuilder(builder);

        return builder;
    }  // end method createBuilder

    @Override AttributeBuilder createField(String nm, Type type) {
        return new AttributeBuilder(nm, type);
    }

    @Nullable private String retrieveFieldRef(MetaModelAST field) {
        final Seq<MetaModelAST> children = field.children(FIELD_REF);
        return children.size() == 1 ? retrieveId(children.getFirst().get()) : null;
    }

    private List<MetaModel> retrieveMetaModels(MetaModelAST node) {
        final List<MetaModel> result = new ArrayList<>();
        for (final MetaModelAST typeNode : node.children(DATAOBJECT_REF)) {
            final String            name = retrieveReferenceQualifiedId(typeNode);
            final String            fqn  = QName.qualify(context.extractQualification(name), context.extractName(name));
            final Option<MetaModel> o    = repository.getModel(QName.createQName(fqn));
            if (o.isPresent()) result.add(o.get());
            else result.add(new ASTUnresolvedTypeReference(this, sourceName, context, fqn, typeNode));
        }
        return result;
    }
}  // end class ViewMaker
