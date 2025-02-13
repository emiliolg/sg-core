
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.report.fo.components;

import java.util.ArrayList;
import java.util.List;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.report.fo.properties.Border;
import tekgenesis.report.fo.properties.Color;
import tekgenesis.report.fo.properties.Padding;
import tekgenesis.report.fo.properties.Size;

/**
 * Table Row Builder.
 */
public class TableRowB extends FoBuilder<TableRowB, TableRow> implements Border<TableRowB>, Color<TableRowB>, Padding<TableRowB>, Size<TableRowB> {

    //~ Instance Fields ..............................................................................................................................

    List<TableCellB> children = new ArrayList<>();

    //~ Methods ......................................................................................................................................

    @Override public TableRowB background(String value) {
        return ColorHelper.background(this, value);
    }

    @Override public TableRowB borderBeforeStyle(String value) {
        return BorderHelper.borderBeforeStyle(this, value);
    }

    @Override public TableRowB borderBeforeWidth(String value) {
        return BorderHelper.borderBeforeWidth(this, value);
    }

    @Override public TableRowB borderBottom(String value) {
        return BorderHelper.borderBottom(this, value);
    }

    @Override public TableRowB borderColor(String value) {
        return BorderHelper.borderColor(this, value);
    }

    @Override public TableRowB borderLeft(String value) {
        return BorderHelper.borderLeft(this, value);
    }

    @Override public TableRowB borderRight(String value) {
        return BorderHelper.borderRight(this, value);
    }

    @Override public TableRowB borderStyle(String value) {
        return BorderHelper.borderStyle(this, value);
    }

    @Override public TableRowB borderTop(String value) {
        return BorderHelper.borderTop(this, value);
    }

    @Override public TableRowB borderWidth(String value) {
        return BorderHelper.borderWidth(this, value);
    }

    @Override public TableRow build() {
        return new TableRow(buildChildren()).withProperties(getProperties());
    }

    /** Builds this rows cells. */
    public List<TableCell> buildChildren() {
        final List<TableCell> c = new ArrayList<>();
        for (final TableCellB child : children)
            c.add(child.build());
        return c;
    }

    /** Bulk cells add. */
    public TableRowB children(TableCellB... c) {
        children.addAll(ImmutableList.fromArray(c));
        return this;
    }

    @Override public TableRowB color(String value) {
        return ColorHelper.color(this, value);
    }

    @Override public TableRowB height(String value) {
        return SizeHelper.height(this, value);
    }

    @Override public TableRowB padding(String value) {
        return PaddingHelper.padding(this, value);
    }

    @Override public TableRowB paddingBefore(String value) {
        return PaddingHelper.paddingBefore(this, value);
    }

    @Override public TableRowB paddingBottom(String value) {
        return PaddingHelper.paddingBottom(this, value);
    }

    @Override public TableRowB paddingEnd(String value) {
        return PaddingHelper.paddingEnd(this, value);
    }

    @Override public TableRowB paddingLeft(String value) {
        return PaddingHelper.paddingLeft(this, value);
    }

    @Override public TableRowB paddingRight(String value) {
        return PaddingHelper.paddingRight(this, value);
    }

    @Override public TableRowB paddingStart(String value) {
        return PaddingHelper.paddingStart(this, value);
    }

    @Override public TableRowB paddingTop(String value) {
        return PaddingHelper.paddingTop(this, value);
    }

    @Override public TableRowB width(String value) {
        return SizeHelper.width(this, value);
    }

    //~ Methods ......................................................................................................................................

    /** Returns new table row. */
    public static TableRowB tableRow() {
        return new TableRowB();
    }
}  // end class TableRowB
