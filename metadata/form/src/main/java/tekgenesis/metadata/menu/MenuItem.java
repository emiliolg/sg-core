
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.metadata.menu;

import java.util.Iterator;

import org.jetbrains.annotations.NotNull;

import tekgenesis.common.collections.ImmutableList;
import tekgenesis.common.collections.Seq;
import tekgenesis.field.FieldOptions;
import tekgenesis.field.ModelField;
import tekgenesis.metadata.link.Assignment;
import tekgenesis.metadata.link.Link;
import tekgenesis.type.MetaModel;
import tekgenesis.type.MetaModelKind;
import tekgenesis.type.Type;
import tekgenesis.type.Types;

import static tekgenesis.common.collections.Colls.emptyIterable;
import static tekgenesis.field.FieldOption.*;
import static tekgenesis.type.MetaModelKind.FORM;
import static tekgenesis.type.MetaModelKind.LINK;

/**
 * This class represent a item inside the Menu.
 */
public final class MenuItem implements ModelField {

    //~ Instance Fields ..............................................................................................................................

    @NotNull private final Menu         menu;
    @NotNull private final FieldOptions options;

    //~ Constructors .................................................................................................................................

    MenuItem(@NotNull final Menu menu, @NotNull final FieldOptions options) {
        this.menu    = menu;
        this.options = options;
    }

    //~ Methods ......................................................................................................................................

    @Override public boolean hasChildren() {
        return false;
    }

    /** Get parameter assignations as an immutable list. */
    @NotNull public Seq<Assignment> getAssignments() {
        if (getModel().getMetaModelKind() == LINK) {
            final Link link = (Link) getModel();
            return link.getAssignments();
        }
        return emptyIterable();
    }

    /** Get list of assignments as a query string. */
    public String getAssignmentsAsQueryString() {
        String                     query              = "";
        final Iterator<Assignment> assignmentIterator = getAssignments().iterator();
        while (assignmentIterator.hasNext()) {
            final Assignment assignment = assignmentIterator.next();
            query += assignment.getField().getName() + "=" + assignment.getValue().toString();
            if (assignmentIterator.hasNext()) query += "&";
        }
        return query;
    }

    @NotNull @Override public ImmutableList<? extends ModelField> getChildren() {
        return ImmutableList.empty();
    }

    @NotNull @Override public String getLabel() {
        return options.getString(LABEL);
    }

    /** Return the menu parent. */
    @NotNull public Menu getMenu() {
        return menu;
    }

    @NotNull public MetaModel getModel() {
        return options.getMetaModelReference(MENU_ELEMENT_REF).get();
    }

    @NotNull @Override public String getName() {
        return options.getString(ID);
    }

    @NotNull @Override public FieldOptions getOptions() {
        return options;
    }

    /** Get the id of the target. */
    @NotNull public String getTarget() {
        if (isLink()) {
            final Link link = (Link) getModel();
            if (link.hasForm()) return link.getForm().getFullName();
            else return link.getLink();
        }
        return getModel().getFullName();
    }

    /** Get the target type @see TargetType. */
    @NotNull public MetaModelKind getTargetType() {
        if (isLink())
            if (((Link) getModel()).hasForm()) return FORM;

        return getModel().getMetaModelKind();
    }

    @NotNull @Override public Type getType() {
        return Types.anyType();
    }

    @Override public void setType(@NotNull Type type) {
        throw new UnsupportedOperationException("Setting type to MenuItem.");
    }

    private boolean isLink() {
        return getModel().getMetaModelKind() == LINK;
    }
}  // end class MenuItem
