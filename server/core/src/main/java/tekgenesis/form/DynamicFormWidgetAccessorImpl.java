
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import java.math.BigDecimal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.DateOnly;
import tekgenesis.common.core.DateTime;
import tekgenesis.common.core.Resource;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.model.KeyMap;
import tekgenesis.persistence.EntityInstance;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.form.FormUtils.mapSetValue;
import static tekgenesis.metadata.form.widget.WidgetTypes.isShowingEntity;

/**
 * Accessor implementation.
 */
public class DynamicFormWidgetAccessorImpl<T extends Model> implements DynamicFormWidgetAccessor {

    //~ Instance Fields ..............................................................................................................................

    private final Form form;
    private final T    model;

    //~ Constructors .................................................................................................................................

    DynamicFormWidgetAccessorImpl(@NotNull Form form, @NotNull T model) {
        this.form  = form;
        this.model = model;
    }

    //~ Methods ......................................................................................................................................

    /**
     * @param   fieldName  field name
     *
     * @return  Boolean
     */
    @Nullable public Boolean is(@NotNull String fieldName) {
        return get(fieldName);
    }

    /**
     * @param   fieldName  field name
     *
     * @return  DateOnly
     */
    @Nullable public DateOnly getDateOnly(@NotNull String fieldName) {
        final Long millis = get(fieldName);
        return millis == null ? null : DateOnly.fromMilliseconds(millis);
    }

    /**
     * @param   fieldName  field name
     *
     * @return  DateTime
     */
    @Nullable public DateTime getDateTime(@NotNull String fieldName) {
        final Long millis = get(fieldName);
        return millis == null ? null : DateTime.fromMilliseconds(millis);
    }

    /**
     * @param   fieldName  field name
     *
     * @return  BigDecimal
     */
    @Nullable public BigDecimal getDecimal(@NotNull String fieldName) {
        return get(fieldName);
    }

    /**
     * Set Value to the specific field.
     *
     * @param  fieldName  Field name
     * @param  v          value
     */
    public void setFieldValue(@NotNull String fieldName, @Nullable Object v) {
        final Widget widget = form.getElement(fieldName);
        if (v instanceof Resource) model.set(widget, ((Resource) v).getUuid());
        else model.set(widget, mapSetValue(v));

        if (v instanceof EntityInstance && isShowingEntity(widget.getWidgetType()))
            model.setOptions(widget, KeyMap.singleton(tuple(((EntityInstance<?, ?>) v).keyAsString(), v.toString())));
    }

    /**
     * @param   fieldName  field name
     *
     * @return  Integer
     */
    @Nullable public Integer getInt(@NotNull String fieldName) {
        return get(fieldName);
    }

    @Nullable @Override public Object getObject(String columnName) {
        return get(columnName);
    }

    @Nullable @Override public Resource getResource(String columnName) {
        return get(columnName);
    }

    /**
     * @param   fieldName  field name
     *
     * @return  String
     */
    @Nullable public String getString(@NotNull String fieldName) {
        return get(fieldName);
    }

    Form getForm() {
        return form;
    }

    T getModel() {
        return model;
    }

    @Nullable private <S> S get(@NotNull String fieldName) {
        return cast(model.get(fieldName));
    }
}  // end class DynamicFormWidgetAccessorImpl
