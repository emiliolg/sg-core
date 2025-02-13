
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client;

import org.jetbrains.annotations.NotNull;

/**
 * Box configuration.
 */
public interface BoxConfiguration {

    //~ Instance Fields ..............................................................................................................................

    /** Default BoxConfiguration. */
    Default DEFAULT = new Default();

    //~ Methods ......................................................................................................................................

    /** Specifies if form will be focused on load. */
    boolean isFocusOnLoad();

    /**
     * Specifies if the box should initialize loading the user defined home form (only valid when
     * the form is bounded to history).
     */
    boolean isHomeFormEnabled();

    /** Specifies the form fqn to be used on load time. */
    @NotNull String getInitialFqn();

    /** Specifies the primary key to be used on load time. */
    @NotNull String getInitialPk();

    /** Specifies if form footer is hidden. */
    boolean isFooterHidden();

    /** Specifies if form header is hidden. */
    boolean isHeaderHidden();

    /** Specifies if form is bounded by history. */
    boolean isBoundedByHistory();

    //~ Inner Classes ................................................................................................................................

    /**
     * Default BoxConfiguration.
     */
    class Default implements BoxConfiguration {
        private boolean focus   = true;
        private boolean footer  = false;
        private boolean header  = false;
        private boolean history = false;
        private boolean home    = true;

        @Override public boolean isFocusOnLoad() {
            return focus;
        }

        @Override public boolean isHomeFormEnabled() {
            return home;
        }

        @NotNull @Override public String getInitialFqn() {
            return "";
        }

        @NotNull @Override public String getInitialPk() {
            return "";
        }

        @Override public boolean isFooterHidden() {
            return footer;
        }

        @Override public boolean isHeaderHidden() {
            return header;
        }

        @Override public boolean isBoundedByHistory() {
            return history;
        }

        /** Hide header and footer. */
        Default bodyOnly() {
            final Default copy = copy();
            copy.header = true;
            copy.footer = true;
            copy.home   = false;
            return copy;
        }

        /** Bound box by history. */
        Default boundedByHistory() {
            final Default copy = copy();
            copy.history = true;
            copy.home    = false;
            return copy;
        }

        Default notFocus() {
            final Default copy = copy();
            copy.focus = false;
            return copy;
        }

        private Default copy() {
            return new Default();
        }
    }  // end class Default
}  // end interface BoxConfiguration
