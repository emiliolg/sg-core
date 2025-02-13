
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.util.EnumSet;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.core.QName;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.form.dependency.PrecedenceData;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Modifier;

import static tekgenesis.type.MetaModelKind.WIDGET;
import static tekgenesis.type.Modifier.ABSTRACT;

/**
 * The class that contains the metadata for the widget definition.
 */
public class WidgetDef extends UiModel {

    //~ Constructors .................................................................................................................................

    WidgetDef() {}

    /** Copy constructor. */
    WidgetDef(WidgetDef w, ImmutableList<Widget> children, FieldOptions options) {
        this(w.modelKey,
            w.sourceName,
            w.binding,
            children,
            options,
            w.parameters,
            w.precedence,
            w.modifiers,
            w.multipleDimension,
            w.fieldDimension,
            w.subformDimension,
            w.widgetDefDimension,
            w.optionsDimension,
            w.configureDimension,
            false);
    }

    /** Expanded constructor. */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    WidgetDef(@NotNull QName modelKey, @NotNull String sourceName, @NotNull QName binding, @NotNull ImmutableList<Widget> children,
              @NotNull FieldOptions options, @NotNull List<ModelField> parameters, @Nullable PrecedenceData precedence,
              @NotNull EnumSet<Modifier> modifiers, int multipleDimension, int fieldDimension, int subformDimension, int widgetDefDimension,
              int optionsDimension, int configureDimension, boolean generated) {
        super(modelKey,
            sourceName,
            binding,
            children,
            options,
            parameters,
            precedence,
            multipleDimension,
            fieldDimension,
            subformDimension,
            widgetDefDimension,
            optionsDimension,
            configureDimension,
            generated,
            modifiers);
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean hasPermission(String permissionName) {
        return true;
    }  // Ideally should delegate on parent

    @NotNull @Override public MetaModelKind getMetaModelKind() {
        return WIDGET;
    }

    /** Return true if widget has abstract modifier. */
    public boolean isAbstract() {
        return modifiers.contains(ABSTRACT);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -5940327097930072016L;
}  // end class WidgetDef
