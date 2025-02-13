
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import tekgenesis.report.fo.properties.Image;
import tekgenesis.report.fo.properties.Size;
import tekgenesis.report.fo.properties.Text;

/**
 * External Graphic Builder.
 */
public class ExternalGraphicB extends FoBuilder<ExternalGraphicB, ExternalGraphic>
    implements Image<ExternalGraphicB>, Size<ExternalGraphicB>, Text<ExternalGraphicB>
{

    //~ Instance Fields ..............................................................................................................................

    private String src = "";

    //~ Methods ......................................................................................................................................

    @Override public ExternalGraphicB alignCenter() {
        return TextHelper.alignCenter(this);
    }

    @Override public ExternalGraphicB alignJustify() {
        return TextHelper.alignJustify(this);
    }

    @Override public ExternalGraphicB alignLeft() {
        return TextHelper.alignLeft(this);
    }

    @Override public ExternalGraphicB alignRight() {
        return TextHelper.alignRight(this);
    }

    @Override public ExternalGraphicB bold() {
        return TextHelper.bold(this);
    }

    public ExternalGraphic build() {
        return new ExternalGraphic(src).withProperties(getProperties());
    }

    @Override public ExternalGraphicB contentHeight(String value) {
        return ImageHelper.contentHeight(this, value);
    }

    @Override public ExternalGraphicB contentWidth(String value) {
        return ImageHelper.contentWidth(this, value);
    }

    @Override public ExternalGraphicB family(String value) {
        return TextHelper.family(this, value);
    }

    @Override public ExternalGraphicB height(String value) {
        return SizeHelper.height(this, value);
    }

    @Override public ExternalGraphicB italic(String value) {
        return TextHelper.italic(this, value);
    }

    @Override public ExternalGraphicB lineHeight(String value) {
        return TextHelper.lineHeight(this, value);
    }

    @Override public ExternalGraphicB normal() {
        return TextHelper.normal(this);
    }

    @Override public ExternalGraphicB size(String value) {
        return TextHelper.size(this, value);
    }

    /** Sets src. */
    public ExternalGraphicB src(String source) {
        src = source;
        return this;
    }

    @Override public ExternalGraphicB subscript() {
        return TextHelper.subscript(this);
    }

    @Override public ExternalGraphicB superscript() {
        return TextHelper.superscript(this);
    }

    @Override public ExternalGraphicB underline() {
        return TextHelper.underline(this);
    }

    @Override public ExternalGraphicB verticalAlign(String value) {
        return TextHelper.verticalAlign(this, value);
    }

    @Override public ExternalGraphicB width(String value) {
        return SizeHelper.width(this, value);
    }

    //~ Methods ......................................................................................................................................

    /** Returns new external graphic. */
    public static ExternalGraphicB externalGraphic() {
        return new ExternalGraphicB();
    }
}  // end class ExternalGraphicB
