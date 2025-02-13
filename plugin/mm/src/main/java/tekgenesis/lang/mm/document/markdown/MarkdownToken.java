
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.lang.mm.document.markdown;

/**
 * TextTile tokens.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public interface MarkdownToken {

    //~ Instance Fields ..............................................................................................................................

    String BOLD   = "**";
    String BULLET = "*";

    String H1        = "#";
    String H2        = "##";
    String H3        = "###";
    String H4        = "####";
    String H5        = "#####";
    String H6        = "######";
    String ITALIC    = "*";
    String UNDERLINE = "~~";
}
