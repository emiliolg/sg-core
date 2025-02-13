
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

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.Predefined;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.menu.MenuBuilder;
import tekgenesis.metadata.menu.MenuItemBuilder;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.Modifier;

import static tekgenesis.common.core.Strings.fromCamelCase;
import static tekgenesis.common.core.Strings.toWords;
import static tekgenesis.field.FieldOption.MENU_ELEMENT_REF;
import static tekgenesis.metadata.menu.MenuBuilder.create;
import static tekgenesis.mmcompiler.ast.MMToken.MENU_ELEMENT;
import static tekgenesis.mmcompiler.builder.ASTMetaModelReference.unresolvedMetaModel;
import static tekgenesis.mmcompiler.builder.BuilderFromAST.retrieveReferenceQualifiedId;
import static tekgenesis.parser.ASTNode.Utils.assertType;

/**
 * Creator for MenuBuilders.
 */
class MenuMaker extends Maker {

    //~ Constructors .................................................................................................................................

    MenuMaker(@NotNull MetaModelAST rootNode, @NotNull BuilderFromAST builderFromAST, @NotNull String sourceName, @NotNull QContext context) {
        super(rootNode, builderFromAST, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    @Override protected MenuBuilder createBuilder(@NotNull QName fqn, @NotNull String label, @NotNull EnumSet<Modifier> modifiers) {
        // If the label is not defined we synthesized one from the id
        final String fixedLabel = Predefined.isEmpty(label) ? toWords(fromCamelCase(fqn.getName())) : label;

        final MenuBuilder builder = create(sourceName, fqn.getQualification(), fqn.getName()).label(fixedLabel).withModifiers(modifiers);

        for (final MetaModelAST node : rootNode) {
            switch (node.getType()) {
            case LIST:
                node.children(MENU_ELEMENT).forEach(f -> addItem(builder, f));
                break;
            default:
                // Ignore
            }
        }

        checkBuilder(builder);

        return builder;
    }

    private void addItem(@NotNull MenuBuilder builder, @NotNull MetaModelAST field) {
        try {
            final MenuItemBuilder item = buildItem(builder, field);
            if (item.hasId()) builder.addMenuItem(item);
        }
        catch (final BuilderException e) {
            error(field, e);
        }
    }

    private MenuItemBuilder buildItem(@NotNull MenuBuilder container, @NotNull MetaModelAST node)
        throws BuilderException
    {
        assertType(node, MENU_ELEMENT);

        final MenuItemBuilder builder = container.menuItem();

        for (final MetaModelAST n : node) {
            switch (n.getType()) {
            case REFERENCE:
                final String fqn = retrieveReferenceQualifiedId(n);
                builder.id(fqn);
                builder.with(MENU_ELEMENT_REF, unresolvedMetaModel(this, n, context, fqn));
                break;
            default:
                // Ignore
            }
        }
        return builder;
    }
}  // end class MenuMaker
