
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.etl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Resource;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.form.InstanceReference;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.model.Model;
import tekgenesis.metadata.form.model.RowModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.MultipleWidget;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.type.Type;

import static tekgenesis.common.collections.Colls.emptyList;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.form.etl.FieldAccessors.findAssociatedInstance;
import static tekgenesis.metadata.form.InstanceReference.createInstanceReference;
import static tekgenesis.metadata.form.widget.Widget.ElemType;

/**
 * FormModel JSON Serializer.
 */
class FormJsonSerializer extends JsonSerializer<FormModel> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final JsonConfigurationImpl configuration;
    @NotNull private final List<String>          parameters;
    @NotNull private Seq<Widget>                 selection;

    //~ Constructors .................................................................................................................................

    FormJsonSerializer(@NotNull JsonConfigurationImpl configuration) {
        this.configuration = configuration;
        parameters         = new ArrayList<>();
        selection          = emptyList();
    }

    //~ Methods ......................................................................................................................................

    @Override public void serialize(@NotNull FormModel formModel, @NotNull JsonGenerator jg, @NotNull SerializerProvider provider)
        throws IOException
    {
        final Form form = formModel.metadata();
        form.getParameters().map(ModelField::getName).into(parameters);

        final boolean asObject = form.getChildren().size() > 1;
        if (asObject) jg.writeStartObject();

        for (final Widget widget : form)
            serializeWidget(widget, formModel, jg, provider, asObject);

        if (asObject) jg.writeEndObject();
    }

    void setSelection(@NotNull Seq<Widget> widgets) {
        selection = widgets;
    }

    private boolean mustSerialize(@NotNull Widget widget) {
        final boolean result;
        if (selection.isEmpty()) {  // No fields has been specified. Return all fields but parameters and generated
            result = !parameters.contains(widget.getName()) && (!widget.hasGeneratedName() || configuration.isIncludeGeneratedFields());
        }
        else result = selection.contains(widget);
        return result;
    }

    /** Serialize a given Entity instance to Json. */
    private void serializeEntity(@NotNull Widget widget, @NotNull String key, @NotNull JsonGenerator jg)
        throws IOException
    {
        final EntityInstance<?, ?> instance = findAssociatedInstance(widget, key);
        if (instance != null) {
            final InstanceReference r = createInstanceReference(createQName(widget.getType().getImplementationClassName()), instance.keyAsString());
            jg.writeString(r.toString());
        }
        else if (configuration.isIncludeNullFields()) jg.writeNull();
    }

    private void serializeScalar(@NotNull final Widget widget, final Type type, @Nullable final Object value, @NotNull final JsonGenerator jg,
                                 boolean asFields)
        throws IOException
    {
        if (value != null) {
            if (asFields) jg.writeFieldName(widget.getName());

            if (type.isResource()) jg.writeString(((Resource) value).getUuid());
            else if (type.isDatabaseObject()) serializeEntity(widget, (String) value, jg);
            else jg.writeObject(value);
        }
        else if (configuration.isIncludeNullFields()) {
            if (asFields) jg.writeFieldName(widget.getName());
            jg.writeNull();
        }
    }

    private void serializeSubform(@NotNull Widget widget, @NotNull Model model, @NotNull JsonGenerator jg, @NotNull SerializerProvider provider,
                                  boolean asFields)
        throws IOException
    {
        final FormModel subform = model.getSubform(widget);
        if (subform != null) {
            if (asFields) jg.writeFieldName(widget.getName());
            serialize(subform, jg, provider);
        }
        else if (configuration.isIncludeNullFields()) {
            if (asFields) jg.writeFieldName(widget.getName());
            jg.writeNull();
        }
    }

    /** Serialize a given TableModel to Json. */
    private void serializeTable(@NotNull MultipleWidget widget, @NotNull Model model, @NotNull JsonGenerator jg, @NotNull SerializerProvider provider,
                                boolean asFields)
        throws IOException
    {
        if (asFields) jg.writeFieldName(widget.getName());
        jg.writeStartArray();

        for (final RowModel row : model.getMultiple(widget)) {
            final boolean asObject = widget.getChildren().size() > 1;
            if (asObject) jg.writeStartObject();

            for (final Widget column : widget)
                serializeWidget(column, row, jg, provider, asObject);

            if (asObject) jg.writeEndObject();
        }
        jg.writeEndArray();
    }

    /** Serialize a given Widget to Json. */
    private void serializeWidget(@NotNull Widget widget, @NotNull Model model, @NotNull JsonGenerator jg, @NotNull SerializerProvider provider,
                                 boolean asFields)
        throws IOException
    {
        if (widget.getWidgetType().isGroup()) {
            for (final Widget child : widget)
                serializeWidget(child, model, jg, provider, asFields);
        }
        else if (mustSerialize(widget)) {
            if (widget.getElemType() == ElemType.MULTIPLE) serializeTable((MultipleWidget) widget, model, jg, provider, asFields);
            else if (widget.getWidgetType() == WidgetType.SUBFORM) serializeSubform(widget, model, jg, provider, asFields);
            else {
                final Type type = widget.getType();
                if (type.isArray()) {
                    if (asFields) jg.writeFieldName(widget.getName());
                    jg.writeStartArray();
                    for (final Object o : model.getArray(widget))
                        serializeScalar(widget, type, o, jg, false);
                    jg.writeEndArray();
                }
                else serializeScalar(widget, type, model.get(widget), jg, asFields);
            }
        }
    }  // end method serializeWidget
}  // end class FormJsonSerializer
