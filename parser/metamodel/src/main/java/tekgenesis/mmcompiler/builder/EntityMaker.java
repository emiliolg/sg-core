
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import java.util.Collection;
import java.util.EnumSet;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.QName;
import tekgenesis.common.core.Tuple;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.AttributeBuilder;
import tekgenesis.metadata.entity.Entity;
import tekgenesis.metadata.entity.EntityBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static java.lang.Integer.parseInt;

import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.metadata.entity.EntityBuilder.createAttribute;
import static tekgenesis.metadata.entity.EntityBuilder.entity;
import static tekgenesis.mmcompiler.ast.MMToken.*;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;
import static tekgenesis.parser.ASTNode.Utils.assertType;

/**
 * Create an EntityBuilder.
 */
class EntityMaker extends CompositeMaker<Entity, Attribute, AttributeBuilder> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private String parent;

    //~ Constructors .................................................................................................................................

    EntityMaker(MetaModelAST entityNode, BuilderFromAST builderFromAST, String sourceName, QContext context) {
        super(entityNode, builderFromAST, sourceName, context);
        parent = "";
    }

    //~ Methods ......................................................................................................................................

    @Override protected AttributeBuilder buildField(String entityName, MetaModelAST fieldNode) {
        assertType(fieldNode, FIELD);

        final Tuple<MetaModelAST, String> idLabel    = retrieveLabeledId(fieldNode);
        final String                      fieldName  = idLabel.first().getText();
        final EntityProcessRef            refHandler = new EntityProcessRef(entityName);

        final Type             type    = retrieveNotNullType(fieldNode, fieldName, refHandler);
        final AttributeBuilder builder = createAttribute(fieldName, type);

        if (refHandler.multiple) builder.multiple();
        if (refHandler.inner) builder.inner();

        builder.description(idLabel.second()).withReverseReference(refHandler.getReverse());
        addOptions(builder, type, fieldNode);
        addDocumentation(builder, fieldNode);
        return builder;
    }

    protected EntityBuilder createBuilder(QName fqn, String label, EnumSet<Modifier> modifiers) {
        final EntityBuilder builder = entity(sourceName, fqn.getQualification(), fqn.getName(), parent, context.getSchema()).label(label)
                                      .withModifiers(modifiers);

        rootNode.forEach(n -> addEntityOption(builder, fqn.getName(), n));

        checkBuilder(builder);

        return builder;
    }  // end method createBuilder

    @Override AttributeBuilder createField(String nm, Type type) {
        return createAttribute(nm, type);
    }

    @SuppressWarnings({ "OverlyLongMethod", "OverlyComplexMethod" })
    private void addEntityOption(final EntityBuilder builder, final String name, final MetaModelAST node) {
        switch (node.getType()) {
        case DESCRIBED_BY:
            builder.describedBy(retrieveFieldIds(node).values());
            break;
        case IMAGE:
            final MetaModelAST image = node.getChild(0).getChild(0);
            builder.image(new ASTFieldReference(image.getText(), this, image));
            break;
        case UNIQUE:
            final Tuple<String, Collection<ModelField>> uniqueIndex = getIndex(node);
            try {
                builder.withUnique(uniqueIndex.first(), uniqueIndex.second());
            }
            catch (final BuilderException e) {
                error(node, e);
            }
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
        case SEARCHABLE:
            addSearchable(builder, node);
            break;
        case DEPRECABLE:
            builder.deprecable();
            break;
        case AUDITABLE:
            builder.auditable();
            break;
        case REMOTABLE:
            builder.remotable();
            break;
        case OPTIMISTIC:
            builder.optimistic();
            break;
        case CACHE:
            final MetaModelAST child = node.getChild(0);
            if (child.isEmpty()) builder.cached();
            else {
                final String text = child.getText();
                if (text.equals(ALL.getText())) builder.fullyCached();
                else builder.cached(parseInt(text));
            }
            break;
        case TABLE:
            final String s = node.getChild(0).getText();
            final String t = node.getChild(1).getText();
            builder.databaseName(t.isEmpty() ? createQName(s) : createQName(s, t));
            break;
        case PRIMARY_KEY:
            builder.primaryKey(retrieveFieldIds(node).values());
            break;
        case FORM_REF:
            builder.defaultForm(node.getChild(0).getText());
            break;
        case LIST:
            for (final MetaModelAST field : node.children(FIELD))
                addField(builder, field, name);
            break;
        default:
            // Ignore
        }
    }  // end method addEntityOption

    //~ Inner Classes ................................................................................................................................

    private class EntityProcessRef implements Function<MetaModelAST, Type> {
        private final String entityName;
        private boolean      inner;
        private boolean      multiple;

        private String reverse;

        public EntityProcessRef(String entityName) {
            this.entityName = entityName;
            multiple        = false;
            reverse         = "";
        }

        @Override public Type apply(MetaModelAST typeNode) {
            final int          entityIdx   = typeNode.getChild(0).hasType(DOCUMENTATION) ? 1 : 0;
            final MetaModelAST typeRefNode = typeNode.getChild(entityIdx);
            final String       refName;
            if (typeNode.hasType(ENTITY)) {
                final EntityMaker maker = new EntityMaker(typeNode, builderFromAST, sourceName, context);
                maker.parent = entityName;
                maker.make();
                inner    = true;
                multiple = hasMultipleMark(typeRefNode, 1);
                refName  = typeRefNode.getChild(0).getText();
            }
            else {
                multiple = hasMultipleMark(typeNode, entityIdx + 1);
                reverse  = typeNode.getChild(entityIdx + (multiple ? 2 : 1)).getText();
                refName  = retrieveReferenceQualifiedId(typeRefNode);
            }
            return new ASTUnresolvedTypeReference(EntityMaker.this, sourceName, context, refName, typeRefNode);
        }

        public boolean isMultiple() {
            return multiple;
        }

        public String getReverse() {
            return reverse;
        }

        private boolean hasMultipleMark(MetaModelAST typeNode, int child) {
            return typeNode.getChild(child).hasType(ASTERISK);
        }
    }  // end class EntityProcessRef
}  // end class EntityMaker
