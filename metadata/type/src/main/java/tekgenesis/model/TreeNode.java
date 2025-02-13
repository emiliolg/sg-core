
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import tekgenesis.common.collections.Colls;

/**
 * Metadata model TreeNode structure.
 */
@SuppressWarnings("FieldMayBeFinal")  // GWT
public class TreeNode implements Serializable, Iterable<TreeNode> {

    //~ Instance Fields ..............................................................................................................................

    private List<TreeNode> children;
    private String         key;
    private String         label;

    //~ Constructors .................................................................................................................................

    /** GWT serialization constructor. */
    public TreeNode() {
        children = Colls.emptyList();
        key      = null;
        label    = "";
    }

    /** Creates a new TreeNode with the given parameters. */
    public TreeNode(String key, @Nullable String label, List<TreeNode> children) {
        this.key      = key;
        this.label    = label;
        this.children = children;
    }

    //~ Methods ......................................................................................................................................

    /** Returns true if Node has Children. */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    @Override public Iterator<TreeNode> iterator() {
        return children.iterator();
    }

    /** Returns the key. */
    public String getKey() {
        return key;
    }

    /** Returns the label. */
    public String getLabel() {
        return label;
    }

    //~ Static Fields ................................................................................................................................

    private static final long serialVersionUID = -1233914842745524396L;
}  // end class TreeNode
