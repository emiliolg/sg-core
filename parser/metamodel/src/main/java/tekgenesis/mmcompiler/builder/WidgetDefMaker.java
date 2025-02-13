
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

import tekgenesis.common.core.QName;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.metadata.form.widget.WidgetDefBuilder;
import tekgenesis.mmcompiler.ast.MetaModelAST;
import tekgenesis.type.Modifier;
import tekgenesis.type.Type;

import static tekgenesis.metadata.form.widget.FormBuilderPredefined.widget;
import static tekgenesis.mmcompiler.ast.MMToken.LIST;

/**
 * Maker for creating a {@link WidgetDefBuilder}.
 */
public class WidgetDefMaker extends UiModelMaker<WidgetDef, WidgetDefBuilder> {

    //~ Constructors .................................................................................................................................

    WidgetDefMaker(MetaModelAST node, BuilderFromAST builder, String sourceName, QContext context) {
        super(node, builder, sourceName, context);
    }

    //~ Methods ......................................................................................................................................

    @Override WidgetDefBuilder createBuilder(QName fqn, String label, Type binding, EnumSet<Modifier> modifiers) {
        final WidgetDefBuilder builder = widget(sourceName, fqn.getQualification(), fqn.getName()).label(label).withModifiers(modifiers);

        if (binding.isDatabaseObject() || binding.isType()) builder.withBinding(binding);

        rootNode.forEach(n -> addWidgetDefOption(fqn, builder, n));

        if (rootNode.children(LIST).isEmpty())  // If no children, generate default widget definition
            generateDefaultModelUi(builder);

        checkBuilder(builder);

        return builder.withRepository(repository);
    }

    private void addWidgetDefOption(QName fqn, WidgetDefBuilder component, MetaModelAST n)
    {
        switch (n.getType()) {
        case PARAMETERS:
            component.parameters(retrieveFieldIds(n).values());
            break;
        case LIST:
            buildWidgets(component, component.getBinding(), n, new FieldsChecker(fqn.getName()));
            break;
        default:
            // Ignore
        }
    }
}  // end class WidgetDefMaker
