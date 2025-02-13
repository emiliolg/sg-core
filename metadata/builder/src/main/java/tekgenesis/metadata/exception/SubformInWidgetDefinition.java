
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.exception;

import org.jetbrains.annotations.NotNull;

/**
 * Exception used when a WidgetDefinition has a subform as a field.
 */
public class SubformInWidgetDefinition extends BuilderException {

    //~ Constructors .................................................................................................................................

    /** SubformInWidgetDefinition constructor. */
    public SubformInWidgetDefinition(@NotNull String name) {
        super("Subform '" + name + "' isn't allowed in a Widget Definition", name);
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -2521793825164720396L;
}
