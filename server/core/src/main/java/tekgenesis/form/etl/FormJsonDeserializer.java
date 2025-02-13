
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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Resource;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.ReflectedFormInstance;
import tekgenesis.form.ReflectedMultipleInstance;
import tekgenesis.form.exprs.ServerExpressions;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.metadata.form.widget.WidgetType;

import static com.fasterxml.jackson.core.JsonToken.*;

import static tekgenesis.common.logging.Logger.getLogger;
import static tekgenesis.form.etl.FieldAccessors.deserializeWidget;
import static tekgenesis.metadata.form.widget.WidgetType.*;
import static tekgenesis.metadata.form.widget.WidgetTypes.isMultiple;

/**
 * FormModel JSON Deserializer.
 */
class FormJsonDeserializer extends JsonDeserializer<FormModel> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final FormModel model;

    //~ Constructors .................................................................................................................................

    FormJsonDeserializer(@NotNull FormModel model) {
        this.model = model;
    }

    //~ Methods ......................................................................................................................................

    @Override public FormModel deserialize(@NotNull JsonParser jp, @NotNull DeserializationContext ctx)
        throws IOException
    {
        // final FormModel             model    = new FormModel(form);
        final Form                  form     = model.metadata();
        final ReflectedFormInstance instance = ReflectedFormInstance.create(model);
        // Do not compute load!
        ServerExpressions.bindListener(instance);

        JsonToken token;
        while ((token = jp.nextToken()) != null && token != END_OBJECT) {
            final String fieldName = jp.getCurrentName();
            jp.nextToken();
            if (form.containsElement(fieldName)) {
                final Widget     widget     = form.getElement(fieldName);
                final WidgetType widgetType = widget.getWidgetType();
                if (isMultiple(widgetType)) deserializeTable(instance.getMultipleInstance(widget), jp);
                else if (widgetType == GALLERY || widgetType == SHOWCASE || widgetType == UPLOAD) deserializeGallery(widget, instance, jp);
                else {
                    final String value = jp.getCurrentToken() == JsonToken.VALUE_NULL ? null : jp.getText();
                    deserializeWidget(widget, instance, value);
                }
            }
            else logger.warning("Field element '" + fieldName + "' is not declared in form '" + form.getKey() + "'");
        }

        return model;
    }

    private void deserializeGallery(@NotNull Widget widget, @NotNull ReflectedFormInstance instance, @NotNull JsonParser jp)
        throws IOException
    {
        final JsonToken currentToken = jp.getCurrentToken();
        // TODO how can I instantiate the resource from the uuid ?
        // avoid gallery deserialization
        if (widget.isMultiple()) instance.setField(widget, new ArrayList<Resource>());

        if (currentToken == START_ARRAY) {
            while (jp.getCurrentToken() != END_ARRAY)
                jp.nextToken();
        }
        else if (currentToken == START_OBJECT) {
            while (jp.getCurrentToken() != END_OBJECT)
                jp.nextToken();
        }
        else logger.warning("Invalid token for Gallery deserialization (token :'" + currentToken.toString() + "'");
    }

    private void deserializeTable(@NotNull ReflectedMultipleInstance accessor, @NotNull JsonParser jp)
        throws IOException
    {
        JsonToken jsonToken = jp.nextToken();

        if (jsonToken == JsonToken.START_OBJECT) {
            final Form                            form = model.metadata();
            ReflectedMultipleInstance.RowInstance row  = accessor.add();
            while ((jsonToken = jp.nextToken()) != END_ARRAY) {
                if (jsonToken == START_OBJECT) row = accessor.add();
                else if (jsonToken != END_OBJECT) {
                    final String fieldName = jp.getText();
                    jp.nextToken();
                    if (form.containsElement(fieldName)) {
                        final Widget widget = form.getElement(fieldName);
                        deserializeWidget(widget, row, jp.getText());
                    }
                }
            }
        }
        else if (jsonToken != JsonToken.END_ARRAY) throw new IOException("Invalid token for Table (token :'" + jsonToken.toString() + "'");
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger logger = getLogger(FormJsonDeserializer.class);
}  // end class FormJsonDeserializer
