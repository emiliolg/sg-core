
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
import tekgenesis.report.fo.properties.Text;

import static tekgenesis.report.fo.Fo.NUMBER_COLUMNS_SPANNED;

/**
 * Table Cell Builder.
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class TableCellB extends FoBuilder<TableCellB, TableCell>
    implements Margin<TableCellB>, Border<TableCellB>, Color<TableCellB>, Text<TableCellB>, Padding<TableCellB>
{

    //~ Instance Fields ..............................................................................................................................

    private BlockB child = null;

    //~ Methods ......................................................................................................................................

    @Override public TableCellB alignCenter() {
        return TextHelper.alignCenter(this);
    }

    @Override public TableCellB alignJustify() {
        return TextHelper.alignJustify(this);
    }

    @Override public TableCellB alignLeft() {
        return TextHelper.alignLeft(this);
    }

    @Override public TableCellB alignRight() {
        return TextHelper.alignRight(this);
    }

    @Override public TableCellB background(String value) {
        return ColorHelper.background(this, value);
    }

    @Override public TableCellB bold() {
        return TextHelper.bold(this);
    }

    @Override public TableCellB borderBeforeStyle(String value) {
        return BorderHelper.borderBeforeStyle(this, value);
    }

    @Override public TableCellB borderBeforeWidth(String value) {
        return BorderHelper.borderBeforeWidth(this, value);
    }

    @Override public TableCellB borderBottom(String value) {
        return BorderHelper.borderBottom(this, value);
    }

    @Override public TableCellB borderColor(String value) {
        return BorderHelper.borderColor(this, value);
    }

    @Override public TableCellB borderLeft(String value) {
        return BorderHelper.borderLeft(this, value);
    }

    @Override public TableCellB borderRight(String value) {
        return BorderHelper.borderRight(this, value);
    }

    @Override public TableCellB borderStyle(String value) {
        return BorderHelper.borderStyle(this, value);
    }

    @Override public TableCellB borderTop(String value) {
        return BorderHelper.borderTop(this, value);
    }

    @Override public TableCellB borderWidth(String value) {
        return BorderHelper.borderWidth(this, value);
    }

    @Override public TableCell build() {
        return new TableCell(child.build()).withProperties(getProperties());
    }

    /** Adds child block to this cell. */
    public TableCellB child(BlockB c) {
        child = c;
        return this;
    }

    @Override public TableCellB color(String value) {
        return ColorHelper.color(this, value);
    }

    @Override public TableCellB family(String value) {
        return TextHelper.family(this, value);
    }

    @Override public TableCellB italic(String value) {
        return TextHelper.italic(this, value);
    }

    @Override public TableCellB lineHeight(String value) {
        return TextHelper.lineHeight(this, value);
    }

    @Override public TableCellB marginBottom(String value) {
        return MarginHelper.marginBottom(this, value);
    }

    @Override public TableCellB marginLeft(String value) {
        return MarginHelper.marginLeft(this, value);
    }

    @Override public TableCellB marginRight(String value) {
        return MarginHelper.marginRight(this, value);
    }

    @Override public TableCellB marginTop(String value) {
        return MarginHelper.marginTop(this, value);
    }

    @Override public TableCellB normal() {
        return TextHelper.normal(this);
    }

    @Override public TableCellB padding(String value) {
        return PaddingHelper.padding(this, value);
    }

    @Override public TableCellB paddingBefore(String value) {
        return PaddingHelper.paddingBefore(this, value);
    }

    @Override public TableCellB paddingBottom(String value) {
        return PaddingHelper.paddingBottom(this, value);
    }

    @Override public TableCellB paddingEnd(String value) {
        return PaddingHelper.paddingEnd(this, value);
    }

    @Override public TableCellB paddingLeft(String value) {
        return PaddingHelper.paddingLeft(this, value);
    }

    @Override public TableCellB paddingRight(String value) {
        return PaddingHelper.paddingRight(this, value);
    }

    @Override public TableCellB paddingStart(String value) {
        return PaddingHelper.paddingStart(this, value);
    }

    @Override public TableCellB paddingTop(String value) {
        return PaddingHelper.paddingTop(this, value);
    }

    @Override public TableCellB size(String value) {
        return TextHelper.size(this, value);
    }

    /**
     * Specifies the number of columns which this cell spans in the column-progression-direction
     * starting with the current column.
     */
    public TableCellB span(String value) {
        return addProperty(NUMBER_COLUMNS_SPANNED, value);
    }

    @Override public TableCellB subscript() {
        return TextHelper.subscript(this);
    }

    @Override public TableCellB superscript() {
        return TextHelper.superscript(this);
    }

    @Override public TableCellB underline() {
        return TextHelper.underline(this);
    }

    @Override public TableCellB verticalAlign(String value) {
        return TextHelper.verticalAlign(this, value);
    }

    //~ Methods ......................................................................................................................................

    /** Returns a new table cell. */
    public static TableCellB tableCell() {
        return new TableCellB();
    }
}  // end class TableCellB
