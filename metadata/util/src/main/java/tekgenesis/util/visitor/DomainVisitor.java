
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.util.visitor;

import tekgenesis.metadata.entity.Attribute;
import tekgenesis.metadata.entity.DbObject;
import tekgenesis.metadata.form.widget.Form;
import tekgenesis.metadata.form.widget.Widget;
import tekgenesis.repository.ModelRepository;
import tekgenesis.type.EnumType;
import tekgenesis.type.MetaModel;

/**
 * a Visitor for a Package.*
 *
 * @param  <T>  return type of the visit operation
 */
@SuppressWarnings({ "WeakerAccess", "UnusedReturnValue" })
public abstract class DomainVisitor<T> {

    //~ Constructors .................................................................................................................................

    protected DomainVisitor() {}

    //~ Methods ......................................................................................................................................

    /** Visit {@link DbObject}. */
    public T visit(DbObject entity) {
        for (final Attribute attribute : entity.attributes())
            visit(attribute);
        return getVisitReturnValue();
    }

    /** Visit {@link Form}. */
    public T visit(Form form) {
        visitChildren(form);
        return getVisitReturnValue();
    }

    /** Visit {@link EnumType}. */
    public T visit(EnumType aEnum) {
        return getVisitReturnValue();
    }

    /** Visit {@link Attribute}. */
    public T visit(Attribute attribute) {
        return getVisitReturnValue();
    }

    /** Visit {@link Widget}. */
    public T visit(Widget widget) {
        return getVisitReturnValue();
    }

    /** Visit {@link ModelRepository}. */

    public T visit(String aPackage, ModelRepository repository) {
        for (final MetaModel definition : repository.getModels(aPackage)) {
            if (definition instanceof DbObject) visit(((DbObject) definition));
            else if (definition instanceof EnumType) visit((EnumType) definition);
            else if (definition instanceof Form) visit((Form) definition);
        }
        return getVisitReturnValue();
    }
    T getVisitReturnValue() {
        return null;
    }

    private void visitChildren(Iterable<Widget> iterable) {
        for (final Widget widget : iterable) {
            if (widget.hasChildren()) visitChildren(widget);
            visit(widget);
        }
    }
}  // end interface DomainVisitor
