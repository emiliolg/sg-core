
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.type;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.core.Constants;

/**
 * A {@link Type} that represents a View Type.
 */
public class HtmlType extends AbstractType {

    //~ Constructors .................................................................................................................................

    private HtmlType() {}

    //~ Methods ......................................................................................................................................

    @Override public String getImplementationClassName() {
        return Constants.HTML_CLASS;
    }

    @NotNull @Override public Kind getKind() {
        return Kind.HTML;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = 7933884796531012014L;

    static final HtmlType INSTANCE = new HtmlType();
}
