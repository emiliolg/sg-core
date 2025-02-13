
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

import tekgenesis.common.collections.Seq;
import tekgenesis.field.FieldOption;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.exception.BuilderException;

import static tekgenesis.field.FieldOption.*;
import static tekgenesis.metadata.form.widget.WidgetTypes.supports;

/**
 * Generates a {@link FormBuilder} for a given {@link Form}.
 */
public class MetadataToBuilder {

    //~ Instance Fields ..............................................................................................................................

    private final FormBuilder formBuilder;

    //~ Constructors .................................................................................................................................

    private MetadataToBuilder(final Form form, final String formExtensionFqn)
        throws BuilderException
    {
        formBuilder = new FormBuilder(form.getSourceName(), form.getDomain(), form.getName(), cloneOptions(form.getOptions()));
        formBuilder.with(FieldOption.EXTENDED_FORM, formExtensionFqn);

        traverseWidgets(form.getChildren(), formBuilder);
    }

    //~ Methods ......................................................................................................................................

    private void traverseWidgets(final Seq<? extends ModelField> children, final WidgetBuilder parent)
        throws BuilderException
    {
        for (final ModelField modelField : children) {
            if (modelField instanceof Widget) {
                final Widget w = (Widget) modelField;

                final WidgetBuilder widgetBuilder = new WidgetBuilder(w.getWidgetType(), cloneOptions(w.getOptions()));
                if (supports(w.getWidgetType(), w.getType())) widgetBuilder.withType(w.getType());
                traverseWidgets(w.getChildren(), widgetBuilder);
                parent.addConsolidatedWidget(widgetBuilder);
            }
        }
    }

    //~ Methods ......................................................................................................................................

    /** Re-constructs a {@link FormBuilder} from a {@link Form} metadata. */
    public static FormBuilder createBuilder(@NotNull final Form form, final String formExtensionFqn)
        throws BuilderException
    {
        return new MetadataToBuilder(form, formExtensionFqn).formBuilder;
    }

    @NotNull private static FieldOptions cloneOptions(final FieldOptions oldOptions) {
        final FieldOptions result = new FieldOptions();
        result.putAll(oldOptions);
        result.put(ORIGINAL_FORM_FIELD, true);  // mark original fields before custom extension
        result.remove(ORDINAL);
        result.remove(SLOT_FIELD);
        result.remove(SLOT_CONFIGURATION);
        result.remove(SLOT_SUBFORM);
        result.remove(SLOT_WIDGET);
        result.remove(SLOT_OPTIONS);
        result.remove(SLOT_GLOBAL_OPTIONS);
        return result;
    }
}  // end class MetadataToBuilder
