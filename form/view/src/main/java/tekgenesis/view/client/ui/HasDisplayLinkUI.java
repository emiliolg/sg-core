
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.view.client.ui;

import org.jetbrains.annotations.Nullable;

/**
 * A Widget that displays a link, used to set the link text for the expression 'display'
 */
public interface HasDisplayLinkUI extends HasClickUI {

    //~ Methods ......................................................................................................................................

    /** Set link text. */
    void setLinkText(@Nullable final String linkText);
}
