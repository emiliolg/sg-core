
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.entity;

import org.jetbrains.annotations.NotNull;

import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.type.Type;

import static tekgenesis.common.Predefined.cast;

/**
 * ViewAttribute for Views.
 */
public class ViewAttribute extends Attribute {

    //~ Instance Fields ..............................................................................................................................

    private ModelField baseAttribute;

    //~ Constructors .................................................................................................................................

    /** Constructs a ViewAttribute. */
    @SuppressWarnings("ConstructorWithTooManyParameters")
    public ViewAttribute(@NotNull View view, @NotNull String name, @NotNull String label, FieldOptions options, @NotNull ModelField baseAttribute,
                         @NotNull Type type, boolean synthesized) {
        super(view, name, type, label, options, false, false, "", synthesized, "");
        this.baseAttribute = baseAttribute;
    }

    //~ Methods ......................................................................................................................................

    /** Set attribute as inner. */
    public void inner() {
        inner = true;
    }

    /** Set attribute as multiple. */
    public void multiple() {
        multiple = true;
    }

    /** Returns the base attribute name. */
    public Attribute getBaseAttribute() {
        if (baseAttribute instanceof Attribute) return cast(baseAttribute);
        else throw new IllegalStateException("Base Attribute could not be resolved or has not been resolved yet");
    }

    /** Sets the base attribute. */
    public void setBaseAttribute(@NotNull Attribute baseAttribute) {
        this.baseAttribute = baseAttribute;
        setType(baseAttribute.getType());
    }

    /** Returns the base attribute modelfield. */
    public ModelField getBaseAttributeModelField() {
        return baseAttribute;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -621122711368452725L;
}  // end class ViewAttribute
