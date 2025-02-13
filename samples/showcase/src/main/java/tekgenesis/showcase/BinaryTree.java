
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
import tekgenesis.form.FormTable;

/**
 * User class for form: BinaryTree
 */
public class BinaryTree extends BinaryTreeBase {

    //~ Methods ......................................................................................................................................

    /** Returns true if tree contains value. */
    public boolean contains(@NotNull String value) {
        return getHead().map(n -> n.contains(value)).orElse(false);
    }

    @NotNull @Override public Action deselect() {
        getHead().ifPresent(BinaryNode::deselectAll);
        reset(Field.SELECTED);
        return actions().getDefault();
    }

    /** Insert value into tree. */
    public BinaryTree insert(@NotNull String value) {
        getHead().create().insert(value);
        return this;
    }

    @Override public void load() {
        insert("M").insert("U").insert("R").insert("W").insert("F").insert("E").insert("H").insert("").insert("A");

        final BinaryNode head = getHead().create();

        final FormTable<LettersRow> letters = getLetters();
        for (int i = ((int) 'A'); i <= ((int) 'Z'); i++) {
            final String ref = String.valueOf((char) i);
            if (!head.contains(ref)) letters.add().setRef(ref);
        }
    }

    @NotNull @Override BinaryNode defineHead() {
        return new BinaryNode() {
            @Override void select(boolean select, String value) {
                deselectAll();
                BinaryTree.this.select(select, value);
            }
        };
    }

    private void select(boolean select, String value) {
        if (select) setSelected(value);
        else reset(Field.SELECTED);
    }

    //~ Inner Classes ................................................................................................................................

    public class LettersRow extends LettersRowBase {
        @NotNull @Override public Action select() {
            insert(getRef());
            remove();
            return actions().getDefault();
        }
    }
}  // end class BinaryTree
