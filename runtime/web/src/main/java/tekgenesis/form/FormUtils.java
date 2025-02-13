
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.*;
import tekgenesis.common.env.context.Context;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.FormBuilder;
import tekgenesis.metadata.form.widget.UiModelLocalizer;
import tekgenesis.metadata.form.widget.WidgetDef;
import tekgenesis.persistence.EntityInstance;
import tekgenesis.repository.ModelRepository;

import static tekgenesis.common.Predefined.isEmpty;

/**
 * Form Utils.
 */
public final class FormUtils {

    //~ Constructors .................................................................................................................................

    private FormUtils() {}

    //~ Methods ......................................................................................................................................

    /** Finds the Form with this id. */
    public static Option<Form> findForm(String formId) {
        final Tuple<String, String> formInfo = getFormInfo(formId);

        final String fqn   = formInfo.first();
        final String param = formInfo.second();

        final Option<Form> option = isEmpty(param) ? Context.getSingleton(ModelRepository.class).getModel(formId, Form.class) : Option.empty();
        return option.or(() -> Option.of(buildDynamicFormMetaModel(fqn, param)));
    }

    /** Localized form. */
    public static Form localize(final Form form) {
        return UiModelLocalizer.localizer(form).localize();
    }

    /** Localized widget definition. */
    public static WidgetDef localize(final WidgetDef widget) {
        return UiModelLocalizer.localizer(widget).localize();
    }

    /**
     * Handy method to properly convert a value into a real value if its Date, DateTime, Enum or
     * Entity.
     */
    @Contract("!null -> !null")
    @Nullable public static Object mapSetValue(@Nullable Object v) {
        if (v instanceof DateOnly) return ((DateOnly) v).toMilliseconds();
        if (v instanceof DateTime) return ((DateTime) v).toMilliseconds();
        if (v instanceof Enumeration) return ((Enumeration<?, ?>) v).name();
        if (v instanceof EntityInstance) return ((EntityInstance<?, ?>) v).keyAsString();
        if (v instanceof Suggestion) return ((Suggestion) v).getKey();
        return v;
    }

    /**
     * Returns the form info based on the form id. Tuple<String,String>. tuple.first() -> Form's fqn
     * , tuple.second() Form's param
     */
    @SuppressWarnings("WeakerAccess")
    public static Tuple<String, String> getFormInfo(@NotNull String formId) {
        final int i = formId.indexOf(':');
        return i > 0 ? Tuple.tuple(formId.substring(0, i), formId.substring(i + 1)) : Tuple.tuple(formId, "");
    }

    /** Build Dynamic Form. */
    static Form buildDynamicFormMetaModel(@NotNull DynamicFormInstance dynamic) {
        try {
            final ModelRepository repository = Context.getContext().getSingleton(ModelRepository.class);
            final FormBuilder     builder    = dynamic.resolve(repository);
            return dynamic.build(builder, repository);
        }
        catch (final BuilderException e) {
            throw new IllegalStateException("Error trying to create an instance of the dynamic form", e);
        }
    }

    private static Form buildDynamicFormMetaModel(@NotNull String fqn, @Nullable String params) {
        final DynamicFormInstance instance = new DynamicFormInstance(fqn, params);
        return buildDynamicFormMetaModel(instance);
    }
}  // end class FormUtils
