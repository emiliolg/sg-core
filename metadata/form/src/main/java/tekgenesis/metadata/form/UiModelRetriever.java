
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.QName;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.metadata.form.widget.WidgetDef;

import static tekgenesis.common.Predefined.cast;

/**
 * Used to retrieve a {@link Form form} or a {@link WidgetDef widget definition} given a
 * {@link QName key}.
 */
public interface UiModelRetriever {

    //~ Methods ......................................................................................................................................

    /** Gets a {@link Form form} given a {@link QName key} (or throws an exception if not found). */
    @NotNull default Form getForm(@NotNull QName key) {
        return getOptionalForm(key).getOrFail("Form '" + key.getFullName() + Constants.NOT_FOUND);
    }

    /** Gets an optional {@link Form form} given a {@link QName key}. */
    @NotNull default Option<Form> getOptionalForm(@NotNull final QName key) {
        return cast(getUiModel(key).filter(m -> m instanceof Form));
    }

    /** Gets an optional {@link WidgetDef widget definition} given a {@link QName key}. */
    @NotNull default Option<WidgetDef> getOptionalWidget(@NotNull final QName key) {
        return cast(getUiModel(key).filter(m -> m instanceof WidgetDef));
    }

    /** Gets an optional {@link UiModel ui model} given a {@link QName key}. */
    @NotNull Option<UiModel> getUiModel(@NotNull QName key);

    /**
     * Gets a {@link WidgetDef widget definition} given a {@link QName key} (or throws an exception
     * if not found).
     */
    @NotNull default WidgetDef getWidget(@NotNull QName key) {
        return getOptionalWidget(key).getOrFail("Widget '" + key.getFullName() + Constants.NOT_FOUND);
    }
}
