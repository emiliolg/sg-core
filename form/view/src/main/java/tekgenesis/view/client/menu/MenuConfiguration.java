
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.menu;

/**
 * Menu configuration.
 */
public interface MenuConfiguration {

    //~ Instance Fields ..............................................................................................................................

    /** Default BoxConfiguration. */
    MenuConfiguration DEFAULT = new Default();

    String LEFT_POSITION = "left";
    String TOP_POSITION  = "top";

    //~ Methods ......................................................................................................................................

    /** Specifies if menu root should be rendered. */
    boolean hideRoot();

    /** Specifies the menu id to load, empty for all the menus. */
    String getFqn();

    /** Specifies the max number of top level menus before defaulting to a root menu. */
    int getMaxMenus();

    /** Specifies the menu layout (top or left). */
    String getPosition();

    //~ Inner Classes ................................................................................................................................

    /**
     * Default MenuConfiguration.
     */
    class Default implements MenuConfiguration {
        @Override public boolean hideRoot() {
            return false;
        }

        @Override public String getFqn() {
            return "";
        }

        @Override public int getMaxMenus() {
            return 0;
        }

        @Override public String getPosition() {
            return LEFT_POSITION;
        }
    }
}  // end interface MenuConfiguration
