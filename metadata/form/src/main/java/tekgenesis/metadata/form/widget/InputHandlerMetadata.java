
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.form.widget;

import java.io.Serializable;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import static tekgenesis.metadata.form.widget.InputHandlerMetadata.InputHandlerType.INHERIT_FROM_TYPE;

/**
 * The input handler metadata.
 */
@SuppressWarnings("FieldMayBeFinal")
public class InputHandlerMetadata implements Serializable {

    //~ Instance Fields ..............................................................................................................................

    private InputHandlerType type;

    //~ Constructors .................................................................................................................................

    InputHandlerMetadata() {
        type = null;
    }

    private InputHandlerMetadata(@NotNull InputHandlerType type) {
        this.type = type;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the type of the input handler (Mask or inherit form type). */
    public InputHandlerType getType() {
        return type;
    }

    //~ Methods ......................................................................................................................................

    /** Returns the default input handler that is deduced from the type. */
    public static InputHandlerMetadata inheritFromType() {
        return INHERIT_INSTANCE;
    }

    /** Returns Mask aware input handler. */
    public static InputHandlerMetadata mask(@NotNull PredefinedMask predefinedMask, @NotNull final List<String> mask) {
        return mask.isEmpty() ? predefinedMask == PredefinedMask.NONE ? INHERIT_INSTANCE : new Predefined(predefinedMask) : new Mask(mask);
    }

    //~ Static Fields ................................................................................................................................

    private static final InputHandlerMetadata INHERIT_INSTANCE = new InputHandlerMetadata(INHERIT_FROM_TYPE);

    private static final long serialVersionUID = -4577960607621001573L;

    //~ Enums ........................................................................................................................................

    public enum InputHandlerType { INHERIT_FROM_TYPE, PREDEFINED_MASK, MASK }

    //~ Inner Classes ................................................................................................................................

    public static class Mask extends InputHandlerMetadata {
        private List<String> mask;

        Mask() {
            mask = null;
        }

        private Mask(List<String> mask) {
            super(InputHandlerType.MASK);
            this.mask = mask;
        }

        /** Returns the string mask. */
        public List<String> getMask() {
            return mask;
        }

        private static final long serialVersionUID = -1868339903600567819L;
    }

    public static class Predefined extends InputHandlerMetadata {
        private PredefinedMask type;

        Predefined() {
            type = null;
        }

        /** Predefined Mask. */
        public Predefined(PredefinedMask type) {
            super(InputHandlerType.PREDEFINED_MASK);
            this.type = type;
        }

        /** Returns mask type. */
        public PredefinedMask getMask() {
            return type;
        }

        private static final long serialVersionUID = -6198191280124509717L;
    }
}  // end class InputHandlerMetadata
