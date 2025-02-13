
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.ref.UiModelReferenceMapper;

import static tekgenesis.metadata.form.widget.WidgetType.WIDGET;

/**
 * Builder to create {@link WidgetDef} instances.
 */
public class WidgetDefBuilder extends UiModelBuilder<WidgetDef, WidgetDefBuilder> {

    //~ Constructors .................................................................................................................................

    WidgetDefBuilder(@NotNull String sourceName, @NotNull String pkg, @NotNull String name) {
        super(sourceName, pkg, name, WIDGET);
    }

    //~ Methods ......................................................................................................................................

    @Override public WidgetDef build()
        throws BuilderException
    {
        if (repository == null) throw new IllegalStateException(FORGOT_WITH_REPOSITORY);

        final ImmutableList<Widget> children = buildChildren(this, this);

        final QName key = QName.createQName(domain, id);

        final WidgetDef widgetDef = new WidgetDef(key,
                sourceName,
                getBindingKey(),
                children,
                getOptions(),
                parameters,
                null,
                modifiers,
                multipleDimension,
                fieldDimension,
                subformDimension,
                widgetDefDimension,
                globalOptionsDimension,
                configureDimension,
                generated);

        // Consolidate expressions
        final UiModelReferenceMapper m = new UiModelReferenceMapper(widgetDef, repository);

        int i = 0;
        for (final WidgetBuilder builder : Colls.deepSeq(this))
            builder.ordinal(i++).consolidateExpressions(m, widgetDef);

        return widgetDef;
    }
}  // end class WidgetDefBuilder
