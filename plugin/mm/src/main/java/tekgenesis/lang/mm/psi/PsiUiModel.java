
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.psi;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;
import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.lang.mm.MMElementType;
import tekgenesis.metadata.form.widget.UiModel;
import tekgenesis.type.MetaModelKind;

import static tekgenesis.common.collections.Colls.seq;
import static tekgenesis.lang.mm.MMElementType.LIST;
import static tekgenesis.lang.mm.MMElementType.WIDGET;

/**
 * Ui Model Psi.
 */
public abstract class PsiUiModel<T extends UiModel> extends PsiMetaModel<T> {

    //~ Constructors .................................................................................................................................

    /** Default constructor. */
    PsiUiModel(@NotNull MMElementType t, @NotNull MetaModelKind kind, @NotNull Class<T> clazz) {
        super(t, kind, clazz);
    }

    //~ Methods ......................................................................................................................................

    /** Return form field node. */
    @Nullable @Override public PsiWidget getFieldNullable(@NotNull String name) {
        return getPsiModelField(name, getFields());
    }

    @NotNull @Override public PsiWidget[] getFields() {
        final ImmutableList<PsiWidget> widgets = getWidgets().toList();
        return widgets.toArray(new PsiWidget[widgets.size()]);
    }

    @Override public abstract Icon getIcon(int flags);

    /** Get all widgets in form. */
    public Seq<PsiWidget> getWidgets() {
        final List<PsiWidget> widgets = retrieveWidgets(new ArrayList<>(), this);
        return widgets.isEmpty() ? Colls.emptyList() : seq(widgets);
    }

    /** Recursively retrieve element form fields. */
    private List<PsiWidget> retrieveWidgets(@NotNull final List<PsiWidget> widgets, @NotNull final MMCommonComposite element) {
        final MMCommonComposite[] lists = element.getChildrenAsPsiElements(LIST, MMCommonComposite[]::new);
        for (final MMCommonComposite list : lists) {
            for (final PsiWidget widget : list.getChildrenAsPsiElements(WIDGET, PsiWidget[]::new)) {
                widgets.add(widget);
                retrieveWidgets(widgets, widget);
            }
        }
        return widgets;
    }
}  // end class PsiUiModel
