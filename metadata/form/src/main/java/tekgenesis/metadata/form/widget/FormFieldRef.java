
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

import org.jetbrains.annotations.NotNull;

/**
 * Represents the reference to a form field/widget.
 */
public interface FormFieldRef {

    //~ Methods ......................................................................................................................................

    /** Returns the id for the form widget. Typically declared in the metamodel file. */
    String id();

    /**
     * Map the current ordinal to the new ordinal if the model was extended with new fields
     * dynamically.
     */
    default int mapOrdinal(@NotNull UiModel model) {
        return model.mapOrdinal(ordinal());
    }

    /** Returns a global ordinal for the widget. Counting every widget no matter the hierarchy. */
    int ordinal();

    //~ Inner Classes ................................................................................................................................

    class Dynamic implements FormFieldRef, Serializable {
        private String id      = "";
        private int    ordinal = -1;

        /** Creates an unresolved Ref. Solve method should be called after the form is build. */
        public Dynamic() {}

        /** Creates an unresolved Ref. Solve method should be called after the form is build. */
        public Dynamic(final String id, final int ordinal) {
            solve(id, ordinal);
        }

        public String id() {
            return id;
        }

        @Override public int mapOrdinal(@NotNull final UiModel model) {
            return ordinal();
        }

        public int ordinal() {
            return checkState().ordinal;
        }

        @Override public String toString() {
            return id;
        }

        /**
         * For fields that are dynamically added, a solve with the actual id and ordinal is needed.
         */
        @SuppressWarnings("ParameterHidesMemberVariable")
        void solve(String id, int ordinal) {
            this.id      = id;
            this.ordinal = ordinal;
        }

        private Dynamic checkState() {
            if (ordinal != -1) return this;
            throw new IllegalStateException("Reference not yet solved");
        }

        private static final long serialVersionUID = 6083251881814274660L;
    }  // end class Dynamic
}  // end interface FormFieldRef
