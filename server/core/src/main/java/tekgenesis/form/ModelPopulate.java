
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.lang.reflect.Field;

import tekgenesis.authorization.shiro.AuthorizationUtils;
import tekgenesis.code.Evaluator;
import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Enumeration;
import tekgenesis.common.core.Option;
import tekgenesis.common.util.Reflection;
import tekgenesis.expr.Expression;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.UiModelBase;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.model.KeyMap;
import tekgenesis.type.ArrayType;
import tekgenesis.type.EnumType;
import tekgenesis.type.Type;
import tekgenesis.type.assignment.AssignmentType;

import static tekgenesis.common.core.Strings.notNullValueOf;
import static tekgenesis.common.core.enumeration.Enumerations.getValuesFor;
import static tekgenesis.metadata.form.exprs.Expressions.evaluateAssignmentList;
import static tekgenesis.metadata.form.widget.WidgetTypes.hasOptions;
import static tekgenesis.metadata.form.widget.WidgetTypes.isShowingEntity;

/**
 * Populate {@link UiModelBase model} with enum options.
 */
class ModelPopulate {

    //~ Instance Fields ..............................................................................................................................

    private final Evaluator evaluator;

    private final UiModelBase<?> model;

    //~ Constructors .................................................................................................................................

    private ModelPopulate(UiModelBase<?> model) {
        this.model = model;
        evaluator  = new Evaluator();
    }

    //~ Methods ......................................................................................................................................

    private boolean filter(final Enumeration<?, ?> enumeration, final Iterable<AssignmentType> assignments) {
        for (final AssignmentType assignment : assignments) {
            final Option<Field> field = Reflection.findField(enumeration.getClass(), assignment.getField());
            if (field.isPresent()) {
                final Object fieldValue = Reflection.getFieldValue(enumeration, field.get());

                for (final String v : assignment.getFilteredValue()) {
                    if ((!v.equals(notNullValueOf(fieldValue)) && assignment.isEquals()) ||
                        (v.equals(notNullValueOf(fieldValue)) && !assignment.isEquals())) return false;
                }
            }
        }
        return true;
    }

    private void populateEnumOptions(Widget widget, EnumType modelType)
    {
        final Expression filter        = widget.getFilterExpression();
        final String     enumClassName = modelType.getFullName();

        if (filter.isEmpty()) model.setOptions(widget, KeyMap.fromEnum(enumClassName));
        else {
            final Iterable<AssignmentType> assignments = evaluateAssignmentList(evaluator, model, filter);

            final KeyMap options = KeyMap.create();
            Colls.filter(getValuesFor(enumClassName), e -> filter(e, assignments)).forEach(e -> options.put(e.name(), e.label()));
            model.setOptions(widget, options);
        }
    }  // end method populateEnumOptions

    private void traverse(Iterable<Widget> widgets) {
        for (final Widget widget : widgets) {
            traverse(widget);  // groups and tables

            final WidgetType widgetType = widget.getWidgetType();
            if (hasOptions(widgetType) || isShowingEntity(widgetType)) {
                Type modelType = widget.getType();
                modelType = modelType.isArray() ? ((ArrayType) modelType).getElementType() : modelType;
                if (modelType.isEnum()) populateEnumOptions(widget, (EnumType) modelType);
            }
        }
    }  // end method traverse

    //~ Methods ......................................................................................................................................

    static FormModel createFormModel(final Form form) {
        final FormModel model = new FormModel(form);
        model.setPermissions(AuthorizationUtils.getPermissions(form));
        return model;
    }

    /** Populate {@link UiModelBase model} with options. */
    static void modelPopulate(final UiModel metadata, final UiModelBase<?> model) {
        new ModelPopulate(model).traverse(metadata);
    }
}  // end class ModelPopulate
