
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import tekgenesis.report.fo.Fo;

/**
 * A Helper Class.
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
class TextHelper {

    //~ Constructors .................................................................................................................................

    private TextHelper() {}

    //~ Methods ......................................................................................................................................

    static <T extends FoBuilder<T, ? extends Fo>> T alignCenter(T fo) {
        return fo.addProperty(TEXT_ALIGN, "center");
    }

    static <T extends FoBuilder<T, ? extends Fo>> T alignJustify(T fo) {
        return fo.addProperty(TEXT_ALIGN, "justify");
    }

    static <T extends FoBuilder<T, ? extends Fo>> T alignLeft(T fo) {
        return fo.addProperty(TEXT_ALIGN, "left");
    }

    static <T extends FoBuilder<T, ? extends Fo>> T alignRight(T fo) {
        return fo.addProperty(TEXT_ALIGN, "right");
    }

    static <T extends FoBuilder<T, ? extends Fo>> T bold(T fo) {
        return fo.addProperty(FONT_WEIGHT, "bold");
    }

    static <T extends FoBuilder<T, ? extends Fo>> T family(T fo, String value) {
        return fo.addProperty("font-family", value);
    }

    static <T extends FoBuilder<T, ? extends Fo>> T italic(T fo, String value) {
        return fo.addProperty("font-style", value);
    }

    static <T extends FoBuilder<T, ? extends Fo>> T lineHeight(T fo, String value) {
        return fo.addProperty("line-height", value);
    }

    static <T extends FoBuilder<T, ? extends Fo>> T normal(T fo) {
        return fo.addProperty(FONT_WEIGHT, "normal");
    }

    static <T extends FoBuilder<T, ? extends Fo>> T size(T fo, String value) {
        return fo.addProperty("font-size", value);
    }

    static <T extends FoBuilder<T, ? extends Fo>> T subscript(T fo) {
        return fo.addProperty(BASELINE_SHIFT, "sub");
    }

    static <T extends FoBuilder<T, ? extends Fo>> T superscript(T fo) {
        return fo.addProperty(BASELINE_SHIFT, "super");
    }

    static <T extends FoBuilder<T, ? extends Fo>> T underline(T fo) {
        return fo.addProperty("text-decoration", "underline");
    }

    static <T extends FoBuilder<T, ? extends Fo>> T verticalAlign(T fo, String value) {
        return fo.addProperty("vertical-align", value);
    }

    //~ Static Fields ................................................................................................................................

    static final String TEXT_ALIGN     = "text-align";
    static final String FONT_WEIGHT    = "font-weight";
    static final String BASELINE_SHIFT = "baseline-shift";
}  // end class TextHelper
