
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
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.field.FieldOption;
import tekgenesis.field.ModelField;
import tekgenesis.field.TypeField;
import tekgenesis.metadata.entity.CompositeBuilder;
import tekgenesis.metadata.entity.CompositeFieldBuilder;
import tekgenesis.metadata.entity.DbObjectBuilder;
import tekgenesis.metadata.entity.SearchFieldBuilder;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.exception.UnsupportedOptionException;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.MetaModel;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.Predefined.notEmpty;
import static tekgenesis.common.collections.Colls.listOf;
import static tekgenesis.common.core.Strings.decode;
import static tekgenesis.common.core.Strings.parseAsInt;
import static tekgenesis.mmcompiler.ast.MMToken.*;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;
import static tekgenesis.parser.ASTNode.Utils.assertType;
import static tekgenesis.type.FieldReference.unresolvedFieldRef;

/**
 * Common stuff between TypeMaker and EntityMaker.
 */
abstract class CompositeMaker<M extends MetaModel, F extends TypeField, FB extends CompositeFieldBuilder<FB>> extends Maker {

    //~ Constructors .................................................................................................................................

    CompositeMaker(MetaModelAST entityNode, BuilderFromAST builderFromAST, String sourceName, QContext context) {
        super(entityNode, builderFromAST, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    void addDocumentation(FB builder, MetaModelAST fieldNode) {
        fieldNode.children(DOCUMENTATION).getFirst().ifPresent(n -> buildFieldDocumentation(builder, n));
    }

    void addField(CompositeBuilder<M, F, FB, ?> builder, MetaModelAST field, String name) {
        try {
            builder.addField(buildField(name, field));
        }
        catch (final BuilderException e) {
            error(field, e);
        }
    }

    void addOptions(FB builder, Type type, MetaModelAST fieldNode) {
        for (final MetaModelAST n : fieldNode) {
            try {
                if (n.hasType(OPTION)) buildFieldOption(builder, n, type.getFinalType());
            }
            catch (final BuilderException e) {
                error(n, e);
            }
        }
    }

    void addSearchable(@NotNull final DbObjectBuilder<M, F, FB, ?> builder, @NotNull final MetaModelAST node) {
        if (retrieveSearchableDatabase(node)) builder.searchByDatabase();
        searchByFields(builder, node);

        builder.defaultSearchable();
    }

    FB buildField(String name, MetaModelAST fieldNode) {
        assertType(fieldNode, FIELD);

        final Tuple<MetaModelAST, String> idLabel   = retrieveLabeledId(fieldNode);
        final String                      fieldName = idLabel.first().getText();

        final Type type    = retrieveNotNullType(fieldNode, fieldName, new TypeProcessRef());
        final FB   builder = createField(fieldName, type);
        addDocumentation(builder, fieldNode);

        addOptions(builder, type, fieldNode);
        return cast(builder);
    }

    abstract FB createField(String nm, Type type);

    Tuple<String, Collection<ModelField>> getIndex(MetaModelAST node) {
        final Seq<MetaModelAST> identifier = node.children(IDENTIFIER);
        final Seq<MetaModelAST> refs       = node.children(FIELD_REF);

        final Option<MetaModelAST> first = identifier.getFirst();
        final String               id    = first.isPresent() ? first.get().getChild(0).getText() : refs.getFirst().get().getChild(0).getText();

        return Tuple.tuple(id, retrieveFieldIds(node, refs).values());
    }

    private void addSearchableOption(SearchFieldBuilder builder, MetaModelAST n)
        throws BuilderException
    {
        final FieldOption opt = retrieveOption(n);
        if (opt == null) return;

        switch (opt) {
        case FILTER_ONLY:
        case ANALYZED:
        case PREFIX:
            builder.with(opt);
            break;
        case FUZZY:
        case BOOST:
        case SLOP:
            builder.with(opt, parseAsInt(n.getChild(1).getText(), 0));
            break;
        default:
            error(n, new UnsupportedOptionException(opt));
        }
    }

    private void buildFieldDocumentation(FB builder, MetaModelAST documentationNode) {
        builder.withFieldDocumentation(getDocumentationText(documentationNode));
    }

    private void buildFieldOption(FB builder, MetaModelAST n, final Type type)
        throws BuilderException
    {
        final FieldOption opt = retrieveOption(n);
        if (opt == null) return;

        switch (opt) {
        case CHECK:
            for (final MetaModelAST c : n.getChild(1))
                builder.check(getAsExpression(c.getChild(0), Types.booleanType()), decode(c.getChild(3).getText()));
            break;
        case DEFAULT:
            builder.defaultValue(getAsExpression(n.getChild(1), type));
            break;
        case OPTIONAL:
            builder.optional();
            break;
        case SIGNED:
        case PROTECTED:
        case ABSTRACT:
        case READ_ONLY:
            builder.with(opt);
            break;
        case COLUMN:
            builder.withOption(opt, buildIdList(n.getChild(1).getEffectiveNode()));
            break;
        case SERIAL:
            builder.serial(n.getChild(1).getText());
            break;
        case START_WITH:
            builder.withOption(opt, parseAsInt(n.getChild(1).getText(), 1));
            break;
        default:
            // Ignore
        }
    }

    private ImmutableList<String> buildIdList(final MetaModelAST cols) {
        if (!cols.hasType(LIST)) return listOf(cols.getText());
        final ImmutableList.Builder<String> list = ImmutableList.builder();
        for (final MetaModelAST col : cols)
            list.add(col.getText());
        return list.build();
    }

    @NotNull private SearchFieldBuilder buildSearchableField(@NotNull final MetaModelAST field) {
        final String             id   = retrieveId(field);
        final String             name = retrieveRefId(field);
        final SearchFieldBuilder b    = new SearchFieldBuilder(unresolvedFieldRef(name), notEmpty(id, name));

        for (final MetaModelAST opt : field.children(OPTION)) {
            try {
                addSearchableOption(b, opt);
            }
            catch (final BuilderException e) {
                error(opt, e);
            }
        }

        return b;
    }

    @NotNull private String retrieveRefId(@NotNull final MetaModelAST fieldNode) {
        for (final MetaModelAST ref : fieldNode.children(FIELD_REF)) {
            for (final MetaModelAST id : ref.children(IDENTIFIER))
                return id.getText();
        }
        return "";
    }

    private boolean retrieveSearchableDatabase(@NotNull final MetaModelAST node) {
        return !node.children(DATABASE).isEmpty();
    }

    private void searchByFields(@NotNull final DbObjectBuilder<M, F, FB, ?> builder, @NotNull final MetaModelAST node) {
        final Option<MetaModelAST> fieldList = node.children(LIST).getFirst();

        if (fieldList.isPresent()) {
            for (final MetaModelAST field : fieldList.get().children(FIELD)) {
                try {
                    builder.addSearchableField(buildSearchableField(field));
                }
                catch (final BuilderException e) {
                    error(field, e);
                }
            }
        }
    }

    //~ Inner Classes ................................................................................................................................

    private class TypeProcessRef implements Function<MetaModelAST, Type> {
        @Override public Type apply(MetaModelAST typeNode) {
            final MetaModelAST ref      = typeNode.getChild(0);
            final boolean      multiple = typeNode.getChild(1).hasType(ASTERISK);
            return new ASTUnresolvedTypeReference(CompositeMaker.this, sourceName, context, retrieveReferenceQualifiedId(ref), ref, multiple);
        }
    }
}  // end class CompositeMaker
