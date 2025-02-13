
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.showcase;

import org.jetbrains.annotations.NotNull;

import tekgenesis.form.Action;
import tekgenesis.form.OptionalWidget;

import static tekgenesis.common.Predefined.isEmpty;
import static tekgenesis.showcase.BinaryNodeBase.Field.VALUE;

/**
 * User class for widget: BinaryNode
 */
public abstract class BinaryNode extends BinaryNodeBase {

    //~ Methods ......................................................................................................................................

    public boolean contains(@NotNull String value) {
        final int cmp = getValue().compareTo(value);
        if (cmp == 0) return true;
        return (cmp > 0 ? getLeft() : getRight()).map(n -> n.contains(value)).orElse(false);
    }

    public void insert(@NotNull String value) {
        if (isEmpty(getValue())) setValue(value);
        else {
            final OptionalWidget<BinaryNode> branch = greater(value) ? getLeft() : getRight();
            branch.create().insert(value);
        }
    }

    @NotNull @Override public Action toggle() {
        final boolean select = isButton();
        select(select, getValue());
        setButton(select);
        return actions().getDefault();
    }

    @Override public String toString() {
        final StringBuilder builder = new StringBuilder();
        print(builder);
        return builder.toString();
    }

    @NotNull @Override BinaryNode defineLeft() {
        return new BinaryNode() {
            @Override void select(boolean select, String value) {
                BinaryNode.this.select(select, value);
            }
        };
    }

    @NotNull @Override BinaryNode defineRight() {
        return new BinaryNode() {
            @Override void select(boolean select, String value) {
                BinaryNode.this.select(select, value);
            }
        };
    }

    /** Recursively deselect all nodes. */
    void deselectAll() {
        setButton(false);
        getLeft().ifPresent(BinaryNode::deselectAll);
        getRight().ifPresent(BinaryNode::deselectAll);
    }

    /** Set selection and recursively deselect other nodes. */
    abstract void select(boolean select, String value);

    private boolean greater(String value) {
        return getValue().compareTo(value) > 0;
    }

    private void print(StringBuilder builder) {
        getLeft().ifPresent(l -> l.print(builder));
        if (isDefined(VALUE)) builder.append(' ').append(getValue()).append(' ');
        getRight().ifPresent(r -> r.print(builder));
    }
}
