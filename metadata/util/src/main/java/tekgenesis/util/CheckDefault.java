
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util;

import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetBuilder;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.metadata.form.widget.WidgetTypes;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.Type;

import static tekgenesis.common.core.QName.extractQualification;

/**
 * Util used for checking defaults in MetaModel.
 */
public class CheckDefault {

    //~ Constructors .................................................................................................................................

    private CheckDefault() {}

    //~ Methods ......................................................................................................................................

    /** checks if widget, is the default for the Type. */
    public static boolean isDefaultWidget(Widget widget, ModelRepository repository) {
        final WidgetType widgetType = widget.getWidgetType();

        if (!WidgetTypes.hasValue(widgetType)) return false;

        try {
            final Type             type       = widget.getType();
            final String           boundField = widget.getBinding();
            final Option<DbObject> model      = repository.getModel(QName.createQName(extractQualification(boundField)), DbObject.class);
            Option<Attribute>      attribute  = Option.empty();
            for (final DbObject entity : model)
                attribute = entity.getAttribute(QName.extractName(boundField));
            final boolean multiple    = attribute.isPresent() && attribute.get().isMultiple();
            final boolean synthesized = attribute.isPresent() && attribute.get().isSynthesized();

            if (WidgetTypes.fromType(type, multiple, synthesized) == null) return false;

            final WidgetBuilder wb = WidgetBuilder.widgetBuilder(type, multiple, synthesized);
            return wb.getWidgetType() == widgetType;
        }
        catch (final BuilderException e) {
            return false;
        }
    }
}
