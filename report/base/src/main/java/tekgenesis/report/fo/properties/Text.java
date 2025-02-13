
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.properties;

import tekgenesis.report.fo.Fo;
import tekgenesis.report.fo.components.FoBuilder;

/**
 * Text properties.
 */
public interface Text<T extends FoBuilder<T, ? extends Fo>> {

    //~ Methods ......................................................................................................................................

    /** Sets the align as center. */
    T alignCenter();
    /** Sets the align as justify. */
    T alignJustify();
    /** Sets the align as left. */
    T alignLeft();
    /** Sets the align as right. */
    T alignRight();
    /** Sets the text as bold. */
    T bold();
    /** Sets the specified font family. */
    T family(String value);
    /** Sets the specified italic font family. */
    T italic(String value);
    /** Sets the line height. */
    T lineHeight(String value);
    /** Sets the text as normal. */
    T normal();
    /** Sets the font size. */
    T size(String value);
    /** Sets the text as subscript. */
    T subscript();
    /** Sets the text as superscript. */
    T superscript();
    /** Sets the text as underline. */
    T underline();
    /** Sets alignment as vertical to the specified value. */
    T verticalAlign(String value);
}  // end interface Text
