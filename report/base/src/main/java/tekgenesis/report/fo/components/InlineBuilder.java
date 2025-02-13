
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import tekgenesis.report.fo.properties.Color;
import tekgenesis.report.fo.properties.Padding;
import tekgenesis.report.fo.properties.Text;

/**
 * Inline Builder.
 */
public class InlineBuilder extends FoBuilder<InlineBuilder, Inline> implements Color<InlineBuilder>, Text<InlineBuilder>, Padding<InlineBuilder> {

    //~ Methods ......................................................................................................................................

    @Override public InlineBuilder alignCenter() {
        return TextHelper.alignCenter(this);
    }

    @Override public InlineBuilder alignJustify() {
        return TextHelper.alignJustify(this);
    }

    @Override public InlineBuilder alignLeft() {
        return TextHelper.alignLeft(this);
    }

    @Override public InlineBuilder alignRight() {
        return TextHelper.alignRight(this);
    }

    @Override public InlineBuilder background(String value) {
        ColorHelper.background(this, value);
        return this;
    }

    @Override public InlineBuilder bold() {
        return TextHelper.bold(this);
    }

    public Inline build() {
        return new Inline(content).withProperties(getProperties());
    }

    @Override public InlineBuilder color(String value) {
        ColorHelper.color(this, value);
        return this;
    }

    @Override public InlineBuilder family(String value) {
        return TextHelper.family(this, value);
    }

    @Override public InlineBuilder italic(String value) {
        return TextHelper.italic(this, value);
    }

    @Override public InlineBuilder lineHeight(String value) {
        return TextHelper.lineHeight(this, value);
    }

    @Override public InlineBuilder normal() {
        return TextHelper.normal(this);
    }

    @Override public InlineBuilder padding(String value) {
        return PaddingHelper.padding(this, value);
    }

    @Override public InlineBuilder paddingBefore(String value) {
        return PaddingHelper.paddingBefore(this, value);
    }

    @Override public InlineBuilder paddingBottom(String value) {
        return PaddingHelper.paddingBottom(this, value);
    }

    @Override public InlineBuilder paddingEnd(String value) {
        return PaddingHelper.paddingEnd(this, value);
    }

    @Override public InlineBuilder paddingLeft(String value) {
        return PaddingHelper.paddingLeft(this, value);
    }

    @Override public InlineBuilder paddingRight(String value) {
        return PaddingHelper.paddingRight(this, value);
    }

    @Override public InlineBuilder paddingStart(String value) {
        return PaddingHelper.paddingStart(this, value);
    }

    @Override public InlineBuilder paddingTop(String value) {
        return PaddingHelper.paddingTop(this, value);
    }

    @Override public InlineBuilder size(String value) {
        return TextHelper.size(this, value);
    }

    @Override public InlineBuilder subscript() {
        return TextHelper.subscript(this);
    }

    @Override public InlineBuilder superscript() {
        return TextHelper.superscript(this);
    }

    @Override public InlineBuilder underline() {
        return TextHelper.underline(this);
    }

    @Override public InlineBuilder verticalAlign(String value) {
        return TextHelper.verticalAlign(this, value);
    }

    //~ Methods ......................................................................................................................................

    /** Returns new inline. */
    public static InlineBuilder inline() {
        return new InlineBuilder();
    }
}  // end class InlineBuilder
