
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.core.Option;
import tekgenesis.metadata.form.IndexedWidget;
import tekgenesis.metadata.form.Parent;
import tekgenesis.metadata.form.widget.Widget;

import static tekgenesis.common.Predefined.ensureNotNull;
import static tekgenesis.metadata.form.IndexedWidget.qualify;

/**
 * Parent {@link UiModelBase model} and {@link Widget anchor widget}.
 */
public class ParentModel implements Parent<Model> {

    //~ Instance Fields ..............................................................................................................................

    @Nullable Widget                anchor;
    @NotNull UiModelBase<?>         model;
    @NotNull final Option<Integer>  section;

    //~ Constructors .................................................................................................................................

    private ParentModel(@NotNull final UiModelBase<?> model, @Nullable final Widget anchor, @NotNull final Option<Integer> section) {
        this.model   = model;
        this.anchor  = anchor;
        this.section = section;
    }

    //~ Methods ......................................................................................................................................

    /** Return {@link Widget anchor widget}. */
    @NotNull public Widget anchor() {
        return ensureNotNull(anchor, "Should call init(metamodel) method first");
    }

    @NotNull @Override public String fqn() {
        return model.parent().map(p -> p.fqn() + "." + name()).orElseGet(this::name);
    }

    /** Associates {@link Widget widget} metamodel. */
    public ParentModel init(@NotNull final Widget a) {
        anchor = a;
        return this;
    }

    @NotNull @Override public Option<Integer> item() {
        return section;
    }

    @NotNull @Override public Model value() {
        return model;
    }

    void notifyDefinitionChangeListeners(@NotNull final IndexedWidget widget) {
        model.notifyDefinitionChange(qualify(anchor(), section, widget));
    }

    void notifyModelChangeListeners(@NotNull final IndexedWidget widget) {
        model.notifyModelChange(qualify(anchor(), section, widget));
    }

    /** Update {@link UiModelBase<?> parent model}. */
    void reparent(UiModelBase<?> actual) {
        model = actual;
    }

    FormModel root() {
        return model.root();
    }

    //~ Methods ......................................................................................................................................

    @NotNull static ParentModel createParent(@NotNull final UiModelBase<?> parent, @Nullable final Widget anchor,
                                             @NotNull final Option<Integer> section) {
        return new ParentModel(parent, anchor, section);
    }
}  // end class ParentModel
