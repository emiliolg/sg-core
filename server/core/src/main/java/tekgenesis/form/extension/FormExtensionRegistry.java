
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.form.extension;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;

import javax.annotation.ParametersAreNonnullByDefault;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.core.Option;
import tekgenesis.common.core.Tuple;
import tekgenesis.common.env.context.Context;
import tekgenesis.common.logging.Logger;
import tekgenesis.form.*;
import tekgenesis.form.exception.EntityInstanceNotFoundException;
import tekgenesis.metadata.common.ModelLinkerImpl;
import tekgenesis.metadata.exception.BuilderException;
import tekgenesis.metadata.form.widget.*;
import tekgenesis.repository.ModelRepository;

import static java.lang.String.format;

import static tekgenesis.common.Predefined.cast;
import static tekgenesis.common.core.QName.createQName;
import static tekgenesis.common.core.Tuple.tuple;
import static tekgenesis.form.ServerUiModelRetriever.getRetriever;

/**
 * A registry of the user defined services to extend a form metadata.
 */
@ParametersAreNonnullByDefault public class FormExtensionRegistry {

    //~ Instance Fields ..............................................................................................................................

    private final Map<Class<? extends FormInstance<?>>, FormExtension<?, ?>> extensions;

    //~ Constructors .................................................................................................................................

    private FormExtensionRegistry() {
        extensions = new HashMap<>();

        for (final FormExtension<?, ?> i : ServiceLoader.load(FormExtension.class)) {
            final FormExtension<?, ?> old = extensions.put(i.getFormType(), i);
            if (old != null) LOGGER.error("There is more than one FormExtension pretending to extend the same form: " + i.getFormType());
        }
    }

    //~ Methods ......................................................................................................................................

    /** Extend the form metadata if there is a FormExtension defined. */
    public Form extendForm(@NotNull final Form form, @Nullable final String pk, @Nullable final String parameters) {
        // lookup for a defined extension
        final FormExtension<FormInstance<Object>, Object> extension = cast(extension(form));

        // no registered extension, use original metadata
        // an extension was already performed and the user decided to cache it, use original metadata
        if (extension == null || form.isExtended() && form.isCached()) return form;

        // call the listener and get the extended form metadata
        try {
            final ExtenderImpl<FormInstance<Object>, Object> extender = new ExtenderImpl<>(form, extension.getClass().getName());
            extension.extend(extender, pk, parameters);
            return extender.build();
        }
        catch (final BuilderException e) {
            LOGGER.error(String.format("Error trying to build extended form %s:%s. Using default form metadata.", form.getFullName(), pk), e);
            return form;
        }
    }

    /** Returns a FormExtension if any. */
    public Option<FormExtension<?, ?>> getExtension(@NotNull final Form form, @Nullable final FormInstance<?> i, @Nullable final UiModelAccessor f) {
        final FormExtension<FormInstance<?>, ?> extension = extension(form);
        if (extension != null) {
            try {
                // noinspection unchecked
                return Option.some(extension.clone(f, i));
            }
            catch (final CloneNotSupportedException e) {
                LOGGER.error(e);
            }
        }

        return Option.empty();
    }

    @Nullable private FormExtension<FormInstance<?>, ?> extension(@NotNull final Form form) {
        return form.isDynamic() ? null : cast(extensions.get(ReflectedFormInstance.getClass(form)));
    }

    //~ Methods ......................................................................................................................................

    /** Returns the singleton instance of the extension registry. */
    public static FormExtensionRegistry getInstance() {
        return INSTANCE;
    }

    /** Returns a localized and possibly extended form metadata. */
    @NotNull public static Form getLocalizedForm(final String fqn) {
        return getLocalizedForm(fqn, null, null);
    }

    /** Returns a localized and possibly extended form metadata. */
    @NotNull public static Form getLocalizedForm(String fqn, @Nullable String pk, @Nullable String parameters) {
        final Form form     = FormUtils.findForm(fqn).orElseThrow(() -> new EntityInstanceNotFoundException(fqn));
        final Form extended = getInstance().extendForm(form, pk, parameters);
        return FormUtils.localize(extended);
    }

    /** Returns a localized widget metadata. */
    @NotNull public static WidgetDef getLocalizedWidgetDef(final String fqn) {
        return FormUtils.localize(getRetriever().getWidget(createQName(fqn)));
    }

    //~ Static Fields ................................................................................................................................

    private static final Logger LOGGER = Logger.getLogger(FormExtensionRegistry.class);

    private static final FormExtensionRegistry INSTANCE = new FormExtensionRegistry();

    //~ Inner Classes ................................................................................................................................

    private class ExtenderImpl<F extends FormInstance<E>, E> implements FormExtension.Extender<F, E> {
        private FormBuilder  builder          = null;
        private boolean      cached           = false;
        private final String formExtensionFqn;
        private final Form   originalMetadata;

        private ExtenderImpl(final Form originalMetadata, final String formExtensionFqn) {
            this.originalMetadata = originalMetadata;
            this.formExtensionFqn = formExtensionFqn;
        }

        @Override public FormFieldRef add(final WidgetBuilder widgetBuilder)
            throws BuilderException
        {
            final FormFieldRef result = widgetBuilder.makeRef();
            builder().addWidget(widgetBuilder);
            return result;
        }

        @Override public FormFieldRef addAfter(final FormFieldRef ref, final WidgetBuilder widgetBuilder)
            throws BuilderException
        {
            final FormFieldRef                  result = widgetBuilder.makeRef();
            final Tuple<WidgetBuilder, Integer> i      = findWidgetIndex(ref, builder());
            if (i == null) throw notFoundError(ref);
            i.first().addWidget(i.second() + 1, widgetBuilder);
            return result;
        }

        @Override public FormFieldRef addBefore(final FormFieldRef ref, final WidgetBuilder widgetBuilder)
            throws BuilderException
        {
            final FormFieldRef                  result = widgetBuilder.makeRef();
            final Tuple<WidgetBuilder, Integer> i      = findWidgetIndex(ref, builder());
            if (i == null) throw notFoundError(ref);
            i.first().addWidget(i.second(), widgetBuilder);
            return result;
        }

        @Override public FormFieldRef addInside(final FormFieldRef ref, final WidgetBuilder widgetBuilder)
            throws BuilderException
        {
            final FormFieldRef result = widgetBuilder.makeRef();
            findWidget(ref).addWidget(widgetBuilder);
            return result;
        }

        @Override public FormExtension.Extender<F, E> cached(final boolean cache) {
            cached = cache;
            return this;
        }

        @Override public WidgetBuilder findWidget(final FormFieldRef ref)
            throws BuilderException
        {
            final Option<WidgetBuilder> result = Colls.deepSeq(builder()).getFirst(w -> w != null && ref.id().equals(w.getName()));
            if (result.isEmpty()) throw notFoundError(ref);
            return result.get();
        }

        @Override public WidgetBuilder onChange(final FormFieldRef ref, final Function<? extends FormExtension<F, E>, Action> fn)
            throws BuilderException
        {
            return findWidget(ref).onChange(fn);
        }

        @Override public WidgetBuilder onClick(final FormFieldRef ref, final Function<? extends FormExtension<F, E>, Action> fn)
            throws BuilderException
        {
            return findWidget(ref).onClick(fn);
        }

        @Override public void remove(final WidgetBuilder widgetBuilder)
            throws BuilderException
        {
            builder().removeWidget(widgetBuilder);
        }

        private Form build()
            throws BuilderException
        {
            if (builder == null) return originalMetadata;

            final Form result = builder().cached(cached).build();
            ModelLinkerImpl.linkForm(repository(), result);
            // if cached, leave in repository for next load
            if (cached) repository().add(result);
            return result;
        }

        private FormBuilder builder()
            throws BuilderException
        {
            if (builder == null) builder = MetadataToBuilder.createBuilder(originalMetadata, formExtensionFqn).withRepository(repository());
            return builder;
        }

        @Nullable private Tuple<WidgetBuilder, Integer> findWidgetIndex(final FormFieldRef ref, final WidgetBuilder parent) {
            // search on top level
            int i = 0;
            for (final WidgetBuilder child : parent) {
                if (ref.id().equals(child.getName())) return tuple(parent, i);
                i++;
            }

            // recursive find on children widgets
            for (final WidgetBuilder child : parent) {
                final Tuple<WidgetBuilder, Integer> result = findWidgetIndex(ref, child);
                if (result != null) return result;
            }

            // not found
            return null;
        }

        private IllegalArgumentException notFoundError(final FormFieldRef ref) {
            return new IllegalArgumentException(format("Widget %s not found on form", ref.id()));
        }

        private ModelRepository repository() {
            return Context.getContext().getSingleton(ModelRepository.class);
        }
    }  // end class ExtenderImpl
}  // end class FormExtensionRegistry
