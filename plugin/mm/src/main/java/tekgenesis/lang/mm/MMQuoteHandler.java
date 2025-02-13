
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;

import static tekgenesis.lang.mm.MMElementType.LITERALS;

/**
 * Handle mm quotes. Close '"' when the user opens them automatically. Handle opening and closing
 * quotes.
 */
public class MMQuoteHandler extends SimpleTokenSetQuoteHandler {

    //~ Constructors .................................................................................................................................

    /** Quote handler constructor. */
    public MMQuoteHandler() {
        super(LITERALS);
    }
}
