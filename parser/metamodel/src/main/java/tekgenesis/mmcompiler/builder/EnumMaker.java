
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.mmcompiler.builder;

import java.io.Serializable;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.QName;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.EnumBuilder;
import tekgenesis.metadata.entity.TypeFieldBuilder;
import tekgenesis.metadata.exception.BuilderErrors;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.EnumType;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static tekgenesis.common.core.Strings.decode;
import static tekgenesis.metadata.entity.EnumBuilder.enumType;
import static tekgenesis.mmcompiler.ast.MMToken.CLASS;
import static tekgenesis.mmcompiler.ast.MMToken.DOCUMENTATION;
import static tekgenesis.mmcompiler.ast.MMToken.ENUM_FIELD;
import static tekgenesis.mmcompiler.ast.MMToken.FIELD;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;

/**
 * Creates an EnumBuilder from the AST.
 */
class EnumMaker extends CompositeMaker<EnumType, TypeField, TypeFieldBuilder> {

    //~ Constructors .................................................................................................................................

    EnumMaker(MetaModelAST enumNode, BuilderFromAST builderFromAST, String sourceName, QContext context) {
        super(enumNode, builderFromAST, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    protected EnumBuilder createBuilder(QName fqn, String label, EnumSet<Modifier> modifiers) {
        final EnumBuilder builder = enumType(sourceName, fqn.getQualification(), fqn.getName()).label(label).withModifiers(modifiers);

        for (final MetaModelAST node : rootNode) {
            switch (node.getType()) {
            case DEFAULT:
                builder.withDefault(node.getChild(0).getChild(0).getText());
                break;

            case PRIMARY_KEY:
                builder.withPrimaryKey(node.getChild(0).getChild(0).getText());
                break;
            case INDEX:
                builder.withIndex(node.getChild(0).getChild(0).getText());
                break;

            case WITH:
                for (final MetaModelAST field : node.children(FIELD))
                    addField(builder, field, fqn.getName());
                break;

            case IMPLEMENTS:
                for (final MetaModelAST clazz : node.children(CLASS))
                    builder.withInterface(retrieveReferenceQualifiedId(clazz));
                break;
            case LIST:
                for (final MetaModelAST e : node.children(ENUM_FIELD)) {
                    final MetaModelAST enumValue = e.getChild(0);
                    final MetaModelAST key       = enumValue.getChild(0);
                    if (!key.isEmpty()) parseEnumValue(builder, key.getText(), e);
                }

                break;
            default:
                // Ignore
            }
        }
        checkBuilder(builder);
        return builder;
    }  // end method createBuilder

    @Override TypeFieldBuilder createField(String nm, Type type) {
        return new TypeFieldBuilder(nm, type);
    }

    private void parseEnumValue(EnumBuilder builder, String id, MetaModelAST e) {
        final MetaModelAST desc = e.getChild(1);

        if (desc.isEmpty() || desc.hasType(DOCUMENTATION)) parseValue(builder, id, "", EnumType.SERIALIZABLES, e);
        else {
            final Collection<TypeFieldBuilder> fields = builder.getFields();
            final String                       label  = decode(desc.getText());
            if (fields.isEmpty()) parseValue(builder, id, label, EnumType.SERIALIZABLES, e);
            else {
                final Iterator<MetaModelAST> it = e.getChild(2).iterator();

                final Serializable[] values = new Serializable[fields.size()];
                int                  i      = 0;
                for (final TypeFieldBuilder field : fields) {
                    if (!it.hasNext()) {
                        if (field.isOptional())
                        // noinspection AssignmentToNull
                        values[i++] = null;
                        else {
                            error(e, BuilderErrors.missingEnumValue(builder.getFullName(), field.getName()));
                            break;
                        }
                    }
                    else values[i++] = getAsExpression(it.next(), field.getType());
                }
                if (it.hasNext()) {
                    final MetaModelAST next = it.next();
                    error(next, BuilderErrors.extraEnumValue(next.getText(), builder.getFullName()));
                }
                parseValue(builder, id, label, values, e);
            }
            if (EnumType.LABEL_LENGTH < label.length()) error(desc, BuilderErrors.enumValueLabelIsTooLong(label));
        }
        if (EnumType.KEY_LENGTH < id.length()) error(e.getChild(0), BuilderErrors.enumValueKeyIsTooLong(id));
    }

    private void parseValue(EnumBuilder builder, String id, String label, Serializable[] values, MetaModelAST e) {
        final Seq<MetaModelAST> children = e.children(DOCUMENTATION);
        if (children.isEmpty()) builder.value(id, label, values);
        else for (final MetaModelAST doc : children) {
            builder.value(id, label, getDocumentationText(doc), values);return;}
    }
}  // end class EnumMaker
