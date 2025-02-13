
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import tekgenesis.report.fo.properties.Border;
import tekgenesis.report.fo.properties.Color;
import tekgenesis.report.fo.properties.Margin;
import tekgenesis.report.fo.properties.Padding;
import tekgenesis.report.fo.properties.Size;
import tekgenesis.report.fo.properties.Space;
import tekgenesis.report.fo.properties.Text;

import static tekgenesis.report.fo.document.LastPageNumberBuilder.lastPageNumber;
import static tekgenesis.report.fo.document.PageNumberBuilder.pageNumber;

/**
 * Block Builder.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class BlockB extends FoContainerB<BlockB, Block>
    implements Margin<BlockB>, Text<BlockB>, Color<BlockB>, Space<BlockB>, Size<BlockB>, Border<BlockB>, Padding<BlockB>
{

    //~ Methods ......................................................................................................................................

    @Override public BlockB alignCenter() {
        return TextHelper.alignCenter(this);
    }

    @Override public BlockB alignJustify() {
        return TextHelper.alignJustify(this);
    }

    @Override public BlockB alignLeft() {
        return TextHelper.alignLeft(this);
    }

    @Override public BlockB alignRight() {
        return TextHelper.alignRight(this);
    }

    @Override public BlockB background(String value) {
        return ColorHelper.background(this, value);
    }

    @Override public BlockB bold() {
        return TextHelper.bold(this);
    }

    @Override public BlockB borderBeforeStyle(String value) {
        return BorderHelper.borderBeforeStyle(this, value);
    }

    @Override public BlockB borderBeforeWidth(String value) {
        return BorderHelper.borderBeforeWidth(this, value);
    }

    @Override public BlockB borderBottom(String value) {
        return BorderHelper.borderBottom(this, value);
    }

    @Override public BlockB borderColor(String value) {
        return BorderHelper.borderColor(this, value);
    }

    @Override public BlockB borderLeft(String value) {
        return BorderHelper.borderLeft(this, value);
    }

    @Override public BlockB borderRight(String value) {
        return BorderHelper.borderRight(this, value);
    }

    @Override public BlockB borderStyle(String value) {
        return BorderHelper.borderStyle(this, value);
    }

    @Override public BlockB borderTop(String value) {
        return BorderHelper.borderTop(this, value);
    }

    @Override public BlockB borderWidth(String value) {
        return BorderHelper.borderWidth(this, value);
    }

    public Block build() {
        return new Block(content, buildChildren()).withProperties(getProperties());
    }

    @Override public BlockB color(String value) {
        return ColorHelper.color(this, value);
    }

    @Override public BlockB family(String value) {
        return TextHelper.family(this, value);
    }

    @Override public BlockB height(String value) {
        return SizeHelper.height(this, value);
    }

    /** Inline the content. */
    @SuppressWarnings({ "unchecked", "varargs" })
    public BlockB inline(String inlineContent) {
        return children(InlineBuilder.inline().content(inlineContent));
    }

    @Override public BlockB italic(String value) {
        return TextHelper.italic(this, value);
    }

    @Override public BlockB lineHeight(String value) {
        return TextHelper.lineHeight(this, value);
    }

    @Override public BlockB marginBottom(String value) {
        return MarginHelper.marginBottom(this, value);
    }

    @Override public BlockB marginLeft(String value) {
        return MarginHelper.marginLeft(this, value);
    }

    @Override public BlockB marginRight(String value) {
        return MarginHelper.marginRight(this, value);
    }

    @Override public BlockB marginTop(String value) {
        return MarginHelper.marginTop(this, value);
    }

    @Override public BlockB normal() {
        return TextHelper.normal(this);
    }

    @Override public BlockB padding(String value) {
        return PaddingHelper.padding(this, value);
    }

    @Override public BlockB paddingBefore(String value) {
        return PaddingHelper.paddingBefore(this, value);
    }

    @Override public BlockB paddingBottom(String value) {
        return PaddingHelper.paddingBottom(this, value);
    }

    @Override public BlockB paddingEnd(String value) {
        return PaddingHelper.paddingEnd(this, value);
    }

    @Override public BlockB paddingLeft(String value) {
        return PaddingHelper.paddingLeft(this, value);
    }

    @Override public BlockB paddingRight(String value) {
        return PaddingHelper.paddingRight(this, value);
    }

    @Override public BlockB paddingStart(String value) {
        return PaddingHelper.paddingStart(this, value);
    }

    @Override public BlockB paddingTop(String value) {
        return PaddingHelper.paddingTop(this, value);
    }

    @Override public BlockB size(String value) {
        return TextHelper.size(this, value);
    }

    @Override public BlockB spaceAfter(String value) {
        return addProperty("space-after", value);
    }

    @Override public BlockB spaceBefore(String value) {
        return addProperty("space-before", value);
    }

    @Override public BlockB subscript() {
        return TextHelper.subscript(this);
    }

    @Override public BlockB superscript() {
        return TextHelper.superscript(this);
    }

    @Override public BlockB underline() {
        return TextHelper.underline(this);
    }

    @Override public BlockB verticalAlign(String value) {
        return TextHelper.verticalAlign(this, value);
    }

    @Override public BlockB width(String value) {
        return SizeHelper.width(this, value);
    }

    /** Specify the last page number. */
    public BlockB withLastPageNumber() {
        children.add(lastPageNumber());
        return this;
    }

    /** Specify the page number. */
    public BlockB withPageNumber() {
        children.add(pageNumber());
        return this;
    }

    //~ Methods ......................................................................................................................................

    /** Create a block. */
    public static BlockB block() {
        return new BlockB();
    }
}  // end class BlockB
