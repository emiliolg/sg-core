
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.etl;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.common.core.Constants;
import tekgenesis.form.BaseReflectedFormInstance;
import tekgenesis.form.FormEtl;
import tekgenesis.form.FormEtlConfiguration;
import tekgenesis.form.JsonConfiguration;
import tekgenesis.metadata.form.model.FormModel;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.form.etl.FieldAccessors.deserializeWidget;

/**
 * Base class for Form Export/Import.
 */
class FormEtlImpl<This extends FormEtl<This>> implements FormEtl<This> {

    //~ Instance Fields ..............................................................................................................................

    @NotNull FormIOBuilder<?> builder;

    @NotNull
    @SuppressWarnings("WeakerAccess")
    Charset          charset;

    @NotNull
    @SuppressWarnings("WeakerAccess")
    final BaseReflectedFormInstance instance;

    //~ Constructors .................................................................................................................................

    FormEtlImpl(@NotNull final BaseReflectedFormInstance instance) {
        this.instance = instance;
        charset       = Charset.forName(Constants.UTF8);
        builder       = new JsonIOBuilder();
    }

    //~ Methods ......................................................................................................................................

    /** Defines the the input/output encoding charset. */
    /*@SuppressWarnings("UnusedReturnValue")
     * public This encoding(@NotNull String encoding) {
     *  charset = Charset.forName(encoding);
     *  return cast(this);
     *}*/

    @Override public This arguments(@NotNull Map<String, String> arguments) {
        for (final Map.Entry<String, String> argument : arguments.entrySet()) {
            final Widget widget = widgetByName(argument.getKey());
            if (widget != null) deserializeWidget(widget, instance, argument.getValue());
        }
        return cast(this);
    }

    @Override public JsonConfiguration usingJson() {
        final JsonIOBuilder b = new JsonIOBuilder();
        builder = b;
        return b.configure();
    }

    @Override public void usingXml() {
        throw new IllegalStateException(Constants.TO_BE_IMPLEMENTED);
    }

    <T> Seq<T> seq(T[] array) {
        return ImmutableList.fromArray(array);
    }

    @NotNull Widget widgetByEnumOrdinal(final Enum<?> field) {
        return getFormMetaModel().getWidgetByOrdinal(field.ordinal());
    }

    @Nullable Widget widgetByName(final String field) {
        final Form metaModel = getFormMetaModel();
        return metaModel.containsElement(field) ? metaModel.getElement(field) : null;
    }

    private Form getFormMetaModel() {
        return instance.getForm();
    }

    //~ Inner Interfaces .............................................................................................................................

    /**
     * Actual Input used when importing.
     */
    interface FormInput extends Closeable {
        /** Read from input. */
        void read()
            throws IOException;
    }

    /**
     * Form Input Output builder.
     */
    interface FormIOBuilder<T extends FormEtlConfiguration<T>> {
        /** Return builder's configuration. */
        T configure();

        /** Create form input. */
        FormInput createInput(@NotNull Reader reader, @NotNull Charset charset, @NotNull FormModel model);

        /** Create form output. */
        FormOutput createOutput(@NotNull Writer writer, @NotNull Charset charset);

        /** Returns builder's file extension. */
        String getExtension();
    }

    /**
     * Actual Output used when exporting.
     */
    interface FormOutput extends Closeable {
        /** Write to output. */
        void write(@NotNull FormModel model, @NotNull Seq<Widget> widgets)
            throws IOException;
    }
}  // end class FormEtlImpl
